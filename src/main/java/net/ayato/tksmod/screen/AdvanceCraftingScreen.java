package net.ayato.tksmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AdvanceCraftingScreen extends AbstractTKSScreen<AdvanceCraftingTableMenu>{
    public AdvanceCraftingScreen(AdvanceCraftingTableMenu advanceCraftingTableMenu, Inventory inventory, Component component) {
        super(advanceCraftingTableMenu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "advance_crafting_table";
    }

    @Override
    protected void renderProgress(PoseStack pPoseStack, int x, int y) {

    }
}
