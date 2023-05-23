package net.ayato.tksmod.screen;

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
}
