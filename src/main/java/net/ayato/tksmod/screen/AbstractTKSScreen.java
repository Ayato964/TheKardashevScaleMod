package net.ayato.tksmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ayato.tksmod.TheKardashevScaleMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AbstractTKSScreen <T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation TEXTURE =
            new ResourceLocation(TheKardashevScaleMod.MODID, "textures/gui/" + getTextureName() + ".png");

    public AbstractTKSScreen(T t, Inventory inventory, Component component) {
        super(t, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgress(poseStack, x, y);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
    protected abstract String getTextureName();
    protected abstract void renderProgress(PoseStack pPoseStack, int x, int y);
}
