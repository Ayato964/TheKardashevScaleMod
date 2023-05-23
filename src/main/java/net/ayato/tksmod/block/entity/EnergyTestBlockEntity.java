package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.EnergyTestBlock;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
import net.ayato.tksmod.screen.EnergyTestBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
        return 2;
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
