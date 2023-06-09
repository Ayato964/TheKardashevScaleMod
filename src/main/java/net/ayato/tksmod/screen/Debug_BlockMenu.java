package net.ayato.tksmod.screen;

import net.ayato.tksmod.block.TKSBlocks;
import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class Debug_BlockMenu extends AbstractTKSMenu {


    public Debug_BlockMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(TKSModMenuTypes.DEBUG_BLOCK_MENU.get(), id, inv, extraData, 2, 2);
    }
    public Debug_BlockMenu(int id, Inventory inv, AbstractTKSBlockEntity entity, ContainerData data){
        super(TKSModMenuTypes.DEBUG_BLOCK_MENU.get(), id, inv, entity, data, 2);
    }

    @Override
    protected int getSlotCount() {
        return 2;
    }

    @Override
    protected void setSlots(IItemHandler handler) {
        this.addSlot(new SlotItemHandler(handler, 0, 86, 15));
        this.addSlot(new SlotItemHandler(handler, 1, 86, 60));
    }

    @Override
    protected int getProgressBarHeight() {
        return 27;
    }

    @Override
    protected Block getMyBlock() {
        return TKSBlocks.DEBUG_BLOCK.get();
    }
/*
    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }
*/
    /*
    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }

     */
}
