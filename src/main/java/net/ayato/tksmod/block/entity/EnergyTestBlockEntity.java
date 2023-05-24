package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.EnergyTestBlock;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
import net.ayato.tksmod.screen.EnergyTestBlockMenu;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.ayato.tksmod.util.entity.TKSEnergyEntityAddon;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class EnergyTestBlockEntity extends AbstractTKSBlockEntity{
    public EnergyTestBlockEntity(BlockPos pos, BlockState state) {
        super(TKSBlockEntities.ENERGY_TEST_BLOCK.get(), pos, state);
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

    @Override
    protected ArrayList<ITKSBlockEntityAddon> setAddons(ArrayList<ITKSBlockEntityAddon> ad) {
        ad.add(new TKSEnergyEntityAddon(this, getName(), 32, new TKSEnergyStorage(60000, 256) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                //ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
            }}));

        return ad;
    }

    @Override
    protected void runningAlways(Level level, BlockPos pos, BlockState state) {

    }

    @Override
    protected void runningHaveRecipe(Level level, BlockPos pos, BlockState state) {

    }

    @Override
    protected boolean getCondition(Level level, BlockPos pos, BlockState state) {
        return true;
    }

}
