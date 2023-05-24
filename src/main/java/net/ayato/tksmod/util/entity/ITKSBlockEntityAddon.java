package net.ayato.tksmod.util.entity;

import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITKSBlockEntityAddon {

    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side);
    public void onLoad();

    public void invalidateCaps();

    void saveAdditional(CompoundTag nbt);

    public void load(CompoundTag nbt);

    void runningAlways(Level level, BlockPos pos, BlockState state);

    void runningHaveRecipe(Level level, BlockPos pos, BlockState state);

    abstract boolean getCondition(Level level, BlockPos pos, BlockState state, AbstractTKSBlockEntity e);
}
