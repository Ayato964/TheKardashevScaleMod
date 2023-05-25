package net.ayato.tksmod.screen;

import net.ayato.tksmod.block.AdvancedCraftingTable;
import net.ayato.tksmod.block.TKSBlocks;
import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AdvanceCraftingTableMenu extends AbstractTKSMenu{
    public AdvanceCraftingTableMenu(int id, Inventory inv, FriendlyByteBuf extraData){
        super(TKSModMenuTypes.ADVANCE_CRAFTING_TABLE.get(), id, inv, extraData, 2, 10);
    }

    public AdvanceCraftingTableMenu(int id, Inventory inv, AbstractTKSBlockEntity entity, ContainerData data){
        super(TKSModMenuTypes.ADVANCE_CRAFTING_TABLE.get(), id, inv, entity, data, 10);
    }

    @Override
    protected int getSlotCount() {
        return 10;
    }

    @Override
    protected void setSlots(IItemHandler handler) {
        int index = 0;
        for(int i = 0; i < 3; i ++) {
            for(int c = 0; c < 3; c ++) {
                this.addSlot(new SlotItemHandler(handler, index, 26 + c * 18, 18 + i * 18));
                index ++;
            }
        }
        this.addSlot(new SlotItemHandler(handler, index, 117, 37));

    }

    @Override
    protected int getProgressBarHeight() {
        return 27;
    }

    @Override
    protected Block getMyBlock() {
        return TKSBlocks.ADVANCED_CRAFTING_TABLE.get();
    }
}
