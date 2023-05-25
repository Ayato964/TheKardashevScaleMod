package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.AdvancedCraftingTable;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.screen.AdvanceCraftingTableMenu;
import net.ayato.tksmod.screen.EnergyTestBlockMenu;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.ayato.tksmod.util.entity.TKSItemSlotEntityAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Optional;

public class AdvancedCraftingTableEntity extends AbstractTKSBlockEntity{
    public AdvancedCraftingTableEntity(BlockPos pos, BlockState state) {
        super(TKSBlockEntities.ADVANCED_CRAFTING_TABLE.get(), pos, state);
    }

    @Override
    protected String getName() {
        return AdvancedCraftingTable.ID;
    }

    @Override
    protected AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player) {
        return new AdvanceCraftingTableMenu(id, inventory,this, this.data);
    }

    @Override
    protected int getMaxProgress() {
        return 0;
    }

    @Override
    protected int getContainerDataCount() {
        return 2;
    }

    @Override
    protected ArrayList<ITKSBlockEntityAddon> setAddons(ArrayList<ITKSBlockEntityAddon> ad) {
        ad.add(new TKSItemSlotEntityAddon(this, getName(), 2, new int[]{0}, new int[]{1}, new ItemStackHandler(2){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        }) {
            @Override
            public Optional<? extends AbstractTKSRecipe> getRecipe(SimpleContainer inventory, Level level) {
                return Optional.empty();
            }
        });
        return ad;
    }

    @Override
    protected void runningAlways(Level level, BlockPos pos, BlockState state) {

    }

    @Override
    protected void runningHaveRecipe(Level level, BlockPos pos, BlockState state) {

    }

    @Override
    protected void runningMainProgressMaxed(Level level, BlockPos blockPos, BlockState state) {

    }

    @Override
    protected boolean getCondition(Level level, BlockPos pos, BlockState state) {
        return true;
    }
}
