package net.ayato.tksmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ayato.tksmod.block.entity.EnergyTestBlockEntity;
import net.ayato.tksmod.screen.render.EnergyInfoArea;
import net.ayato.tksmod.util.MouseUtil;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.ayato.tksmod.util.entity.TKSEnergyEntityAddon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class EnergyTestBlockScreen extends AbstractTKSScreen<EnergyTestBlockMenu> {
    private EnergyInfoArea energyInfoArea;
    public EnergyTestBlockScreen(EnergyTestBlockMenu energyTestBlockMenu, Inventory inventory, Component component) {
        super(energyTestBlockMenu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "debug_block";
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }
    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 156, y + 13, ((TKSEnergyEntityAddon)menu.blockEntity.getAddonInstance(ITKSBlockEntityAddon.Type.ENERGY)).getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 8, 64)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }
    @Override
    protected void renderProgress(PoseStack pPoseStack, int x, int y) {
        if (menu.isCrafting()) {
            blit(pPoseStack, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
        }

    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
        energyInfoArea.draw(poseStack);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
            return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
        }
}
