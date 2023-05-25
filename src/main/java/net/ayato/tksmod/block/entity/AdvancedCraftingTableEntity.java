package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.AdvancedCraftingTable;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.recipe.AdvancedCraftingTableRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
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
    protected void runningNotHasRecipe(Level level, BlockPos blockPos, BlockState state) {
        ((TKSItemSlotEntityAddon) getAddonInstance(ITKSBlockEntityAddon.Type.ITEM)).itemStackHandler.extractItem(9, 1 , false);
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
        stopProgress();
        ad.add(new TKSItemSlotEntityAddon(this, getName(), 10, new int[]{0, 1, 2,
                                                                                        3, 4, 5,
                                                                                        6, 7, 8}, new int[]{9}, new ItemStackHandler(10){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        }) {
            @Override
            public Optional<? extends AbstractTKSRecipe> getRecipe(SimpleContainer inventory, Level level) {
                return level.getRecipeManager().getRecipeFor(AdvancedCraftingTableRecipe.Type.INSTANCE, inventory, level);
            }
        }.editRunningHasRecipe(this::runningHasRecipe).editRunningMainProgressMaxed((addon, inventory, recipe) -> {}));
        return ad;
    }
    boolean tmpBool = false;
    private void runningHasRecipe(TKSItemSlotEntityAddon addon, SimpleContainer simpleContainer, Optional<? extends AbstractTKSRecipe> abstractTKSRecipe) {
        if(addon.itemStackHandler.getStackInSlot(9).isEmpty()) {
            if(tmpBool){
                tmpBool = false;
                addon.removeInputItemAll();
            }else {
                addon.setOutputItem(9, abstractTKSRecipe, 1);
                tmpBool = true;
            }
        }
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
