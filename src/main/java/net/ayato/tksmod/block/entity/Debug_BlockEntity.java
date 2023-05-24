package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.Debug_Block;
import net.ayato.tksmod.recipe.Debug_BlockRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
import net.ayato.tksmod.screen.Debug_BlockMenu;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Optional;

public class Debug_BlockEntity extends AbstractTKSBlockEntity{
    public static final String ID = Debug_Block.ID;
    public Debug_BlockEntity(BlockPos pos, BlockState state) {
        super(TKSBlockEntities.DEBUG_BLOCK.get(), pos, state);
    }

    @Override
    protected Optional<Debug_BlockRecipe> getRecipe(SimpleContainer inventory, Level level) {
        return level.getRecipeManager().getRecipeFor(Debug_BlockRecipe.Type.INSTANCE, inventory, level);
    }

    @Override
    protected ArrayList<ITKSBlockEntityAddon> setAddons(ArrayList<ITKSBlockEntityAddon> ad) {
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

    @Override
    protected String getName() {
        return ID;
    }

    @Override
    protected int getStackBoxCount() {
        return 2;
    }

    @Override
    protected AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player) {
        return new Debug_BlockMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getMaxProgress() {
        return 78;
    }

    @Override
    protected int getContainerDataCount() {
        return 2;
    }


}
