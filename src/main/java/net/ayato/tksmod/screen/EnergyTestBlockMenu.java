package net.ayato.tksmod.screen;

import net.ayato.tksmod.block.TKSBlocks;
import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class EnergyTestBlockMenu extends AbstractTKSMenu{
    public EnergyTestBlockMenu(int id, Inventory inv, FriendlyByteBuf extraData){
        super(TKSModMenuTypes.ENERGY_TEST_BLOCK.get(), id, inv, extraData, 2, 2);
    }

    public EnergyTestBlockMenu(int id, Inventory inv, AbstractTKSBlockEntity entity, ContainerData data){
        super(TKSModMenuTypes.ENERGY_TEST_BLOCK.get(), id, inv, entity, data, 2);
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
        return TKSBlocks.ENERGY_TEST_BLOCK.get();
    }
}
