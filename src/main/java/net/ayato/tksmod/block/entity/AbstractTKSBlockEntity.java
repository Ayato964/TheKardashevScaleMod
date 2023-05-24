package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.ayato.tksmod.util.entity.TKSEnergyEntityAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractTKSBlockEntity extends BlockEntity implements MenuProvider {

    private final ArrayList<ITKSBlockEntityAddon> addons = setAddons(new ArrayList<>());

    protected final ContainerData data;
    /**
     * Progress is dynamic.
     */
    public int progress = 0;
    //private int maxProgress = 78;
    public int maxProgress = getMaxProgress();



    public AbstractTKSBlockEntity(BlockEntityType<?> type,BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress;
                    case 1 -> AbstractTKSBlockEntity.this.maxProgress;
                    default -> getAdditionalProgressData(index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress = value;
                    case 1->AbstractTKSBlockEntity.this.maxProgress = value;
                    default -> setAdditionalProgressData(index, value);
                }

            }

            @Override
            public int getCount() {
                return Math.max(getContainerDataCount(), 2);
            }


        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.tksmod." + getName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return getCreateMenu(id, inventory, player);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        for(ITKSBlockEntityAddon addon : addons){
            LazyOptional<T> l = addon.getCapability(cap, side);
            if(l != null)
                return l;
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        for(ITKSBlockEntityAddon addon : addons) addon.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for(ITKSBlockEntityAddon addon : addons) addon.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt(getName() + ".progress", this.progress);

        for(ITKSBlockEntityAddon addon : addons) addon.saveAdditional(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        progress = nbt.getInt(getName() + ".progress");
        for(ITKSBlockEntityAddon addon : addons) addon.load(nbt);

        super.load(nbt);
    }
    public void drops(){
        for(ITKSBlockEntityAddon addon : addons) addon.drops(level, worldPosition);
    }
    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, E e) {
        if(level.isClientSide()){
            return;
        }
        if(e instanceof AbstractTKSBlockEntity) {
            AbstractTKSBlockEntity entity = ((AbstractTKSBlockEntity) e);
            entity.runningAlways(level, blockPos, state);

            for(ITKSBlockEntityAddon addon : entity.addons) addon.runningAlways(level, blockPos, state);

            if (entity.getCondition(level, blockPos, state) && entity.getAddonsConditions(level, blockPos, state)) {
                entity.progress++;
                for(ITKSBlockEntityAddon addon : entity.addons) addon.runningHaveRecipe(level, blockPos, state);
                entity.runningHaveRecipe(level, blockPos, state);
                setChanged(level, blockPos, state);

                if (entity.progress >= entity.maxProgress) {
                    entity.runningMainProgressMaxed(level, blockPos, state);
                    for(ITKSBlockEntityAddon addon : entity.addons) addon.runningMainProgressMaxed(level, blockPos, state, entity);
                    entity.resetProgress();
                } else {
                    setChanged(level, blockPos, state);
                }

            }
        }
    }

    protected boolean getAddonsConditions(Level level, BlockPos blockPos, BlockState state){
        for(ITKSBlockEntityAddon addon : addons){
            if(!addon.getCondition(level, blockPos, state, this)){
                return false;
            }
        }
        return true;
    }


    private void resetProgress() {
        this.progress = 0;
    }




    protected abstract String getName();
    protected abstract int getStackBoxCount();
    protected abstract AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player);
    protected abstract int getMaxProgress();
    protected int getAdditionalProgressData(int index){return 0;}
    protected void setAdditionalProgressData(int index, int value){}
    protected abstract int getContainerDataCount();

    protected abstract ArrayList<ITKSBlockEntityAddon> setAddons(ArrayList<ITKSBlockEntityAddon> ad);

    /**
     * Methods to write when an entity is unique and does something special.
     * @param level
     * @param pos
     * @param state
     */
    protected abstract void runningAlways(Level level, BlockPos pos, BlockState state);

    /**
     * Methods to write when the entity is unique and does something special when the recipe is established
     * @param level
     * @param pos
     * @param state
     */
    protected abstract void runningHaveRecipe(Level level, BlockPos pos, BlockState state);


    protected abstract void runningMainProgressMaxed(Level level, BlockPos blockPos, BlockState state);


    protected abstract boolean getCondition(Level level, BlockPos pos, BlockState state);

    public ITKSBlockEntityAddon getAddonInstance(ITKSBlockEntityAddon.Type type) {
        for(ITKSBlockEntityAddon a :addons){
            if(a.getType() == type)
                return a;
        }
        throw new IllegalArgumentException("The Addon Error !!!!!\n");
    }
}
