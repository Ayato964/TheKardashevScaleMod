package net.ayato.tksmod.util.entity;

import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.ayato.tksmod.block.entity.EnergyTestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TKSEnergyEntityAddon implements ITKSBlockEntityAddon{
    /*ENERGY!*/
    private final TKSEnergyStorage ENERGY_STORAGE;
    private int ENERGY_REQ = 32;
    /*ENERGY!*/
    public final String BLOCK_ID;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private final AbstractTKSBlockEntity PERCENT;

    public TKSEnergyEntityAddon(AbstractTKSBlockEntity percent, String blockId, int ENERGY_REQ, TKSEnergyStorage storage){
        PERCENT = percent;
        BLOCK_ID = blockId;
        this.ENERGY_REQ = ENERGY_REQ;
        ENERGY_STORAGE = storage;
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
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        return null;
    }

    @Override
    public void onLoad() {
        /*ENERGY!*/
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);

    }

    @Override
    public void invalidateCaps() {
        /*ENERGY!*/
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        /*ENERGY!*/
        nbt.putInt(BLOCK_ID, ENERGY_STORAGE.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        /*ENERGY!*/
        ENERGY_STORAGE.setEnergy(nbt.getInt(BLOCK_ID));
    }

    @Override
    public void runningAlways(Level level, BlockPos pos, BlockState state) {
        /*ENERGY!*/
        if (hasGemInFirstSlot()) {
            ENERGY_STORAGE.receiveEnergy(64, false);
        }

    }

    @Override
    public void runningHaveRecipe(Level level, BlockPos pos, BlockState state) {
        extractEnergy();
    }

    @Override
    public boolean getCondition(Level level, BlockPos pos, BlockState state, AbstractTKSBlockEntity e) {
        return hasEnoughEnergy(e);
    }

    @Override
    public Type getType() {
        return Type.ENERGY;
    }

    /*ENERGY!*/
    private void extractEnergy(){
        ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    /*ENERGY!*/
    private boolean hasEnoughEnergy(AbstractTKSBlockEntity entity) {
        return ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * entity.maxProgress;
    }

    /*ENERGY!*/
    private boolean hasGemInFirstSlot() {
        return ((TKSItemSlotEntityAddon)PERCENT.getAddonInstance(Type.ITEM)).itemStackHandler.getStackInSlot(2).getItem() == Items.COAL;
    }
}
