package org.kdvcs.klux.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.screen.renderer.FluidTankRenderer;
import org.kdvcs.klux.util.MouseUtil;

import java.util.List;
import java.util.Optional;

public class FluidExtractorScreen extends AbstractContainerScreen<FluidExtractorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Klux.MODID,"textures/gui/gui_fluid_extractor.png");

    private FluidTankRenderer renderer;

    public FluidExtractorScreen(FluidExtractorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        this.imageWidth = 176;
        this.imageHeight = 170;
    }

    @Override
    protected void init() {
        super.init();

        assignFluidRenderer();

        this.titleLabelX = 78;
        this.titleLabelY = 5;
    }

    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(64000, true, 16, 61);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x2A241E, false);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderFluidAreaTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (isMouseAboveArea(mouseX, mouseY, x, y, 89, 17)) {
            FluidStack fluidStack = menu.getFluidStack();

            if (!fluidStack.isEmpty()) {
                List<Component> tooltip = renderer.getTooltip(fluidStack,
                        Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - x, mouseY - y);
            }

        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
        renderer.render(guiGraphics.pose(), x + 89, y + 17, menu.getFluidStack());
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 51, y + 37, 176, 0, menu.getScaledProgress(), 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(mouseX, mouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
