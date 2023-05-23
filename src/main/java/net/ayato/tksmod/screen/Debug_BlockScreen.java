package net.ayato.tksmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class Debug_BlockScreen extends AbstractTKSScreen<Debug_BlockMenu> {
    public Debug_BlockScreen(Debug_BlockMenu debug_blockMenu, Inventory inventory, Component component) {
        super(debug_blockMenu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "debug_block";
    }

    @Override
    protected void renderProgress(PoseStack pPoseStack, int x, int y) {
        if (menu.isCrafting()) {
            blit(pPoseStack, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
        }

    }
}
