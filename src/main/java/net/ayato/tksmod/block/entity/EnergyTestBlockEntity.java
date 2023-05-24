package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.EnergyTestBlock;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
import net.ayato.tksmod.screen.EnergyTestBlockMenu;
import net.ayato.tksmod.util.entity.TKSEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyTestBlockEntity extends AbstractTKSBlockEntity{
    /*ENERGY!*/
    private final TKSEnergyStorage ENERGY_STORAGE = new TKSEnergyStorage(60000, 256) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            //ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };
    private static final int ENERGY_REQ = 32;

    /*ENERGY!*/

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public EnergyTestBlockEntity(BlockPos pos, BlockState state) {
        super(TKSBlockEntities.ENERGY_TEST_BLOCK.get(), pos, state);
    }

    /*ENERGY!*/
    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    /*ENERGY!*/
    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        /*ENERGY!*/
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        /*ENERGY!*/
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        /*ENERGY!*/
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        /*ENERGY!*/
        nbt.putInt("energy_test_block", ENERGY_STORAGE.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        /*ENERGY!*/
        ENERGY_STORAGE.setEnergy(nbt.getInt("energy_test_block"));
    }

    /*ENERGY!*/
    private static void extractEnergy(EnergyTestBlockEntity pEntity) {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    /*ENERGY!*/
    private static boolean hasEnoughEnergy(EnergyTestBlockEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    /*ENERGY!*/
    private static boolean hasGemInFirstSlot(EnergyTestBlockEntity pEntity) {
        return pEntity.itemStackHandler.getStackInSlot(2).getItem() == Items.COAL;
    }
    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, E e) {
        if(level.isClientSide()){
            return;
        }
        if(e instanceof EnergyTestBlockEntity) {
            AbstractTKSBlockEntity entity = ((AbstractTKSBlockEntity) e);
            /*ENERGY!*/
            if (hasGemInFirstSlot((EnergyTestBlockEntity) entity)) {
                ((EnergyTestBlockEntity) entity).ENERGY_STORAGE.receiveEnergy(64, false);
            }

            if (hasRecipe(e) && hasEnoughEnergy((EnergyTestBlockEntity) entity)/*ENERGY!*/) {
                entity.progress++;
                extractEnergy((EnergyTestBlockEntity) entity);
                setChanged(level, blockPos, state);

                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                } else {
                    setChanged(level, blockPos, state);
                }
            }
        }
    }
    @Override
    protected String getName() {
        return EnergyTestBlock.ID;
    }

    @Override
    protected int getStackBoxCount() {
        return 3;
    }

    @Override
    protected AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player) {
        return new EnergyTestBlockMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getMaxProgress() {
        return 20;
    }

    @Override
    protected int getContainerDataCount() {
        return 2;
    }

    @Override
    protected Optional<? extends AbstractTKSRecipe> getRecipe(SimpleContainer inventory, Level level) {
        return level.getRecipeManager().getRecipeFor(EnergyTestBlockRecipe.Type.INSTANCE, inventory, level);
    }
}
