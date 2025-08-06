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

public class LiquidReactorScreen extends AbstractContainerScreen<LiquidReactorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Klux.MODID, "textures/gui/gui_liquid_reactor.png");

    private FluidTankRenderer rendererInput1;
    private FluidTankRenderer rendererInput2;
    private FluidTankRenderer rendererOutput;

    public LiquidReactorScreen(LiquidReactorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        this.imageWidth = 176;
        this.imageHeight = 170;
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRenderer();

        this.titleLabelX = 67;
        this.titleLabelY = 5;
    }

    private void assignFluidRenderer() {
        rendererInput1 = new FluidTankRenderer(48000, true, 24, 33);
        rendererInput2 = new FluidTankRenderer(48000, true, 24, 33);
        rendererOutput = new FluidTankRenderer(96000, true, 70, 33);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x2A241E, false);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderFluidAreaTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        FluidStack fluidStack1 = menu.getInputFluid1();
        FluidStack fluidStack2 = menu.getInputFluid2();
        FluidStack outputFluidStack = menu.getOutputFluid();

        if (isMouseAboveArea(mouseX, mouseY, x, y, 12, 45, 24, 33) && !fluidStack1.isEmpty()) {
            List<Component> tooltip = rendererInput1.getTooltip(fluidStack1,
                    Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - x, mouseY - y);
            return;
        }

        if (isMouseAboveArea(mouseX, mouseY, x, y, 140, 45, 24, 33) && !fluidStack2.isEmpty()) {
            List<Component> tooltip = rendererInput2.getTooltip(fluidStack2,
                    Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - x, mouseY - y);
            return;
        }

        if (isMouseAboveArea(mouseX, mouseY, x, y, 53, 45, 70, 33) && !outputFluidStack.isEmpty()) {
            List<Component> tooltip = rendererOutput.getTooltip(outputFluidStack,
                    Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrows(guiGraphics, x, y);
        rendererInput1.render(guiGraphics.pose(), x + 12, y + 45, menu.getInputFluid1());
        rendererInput2.render(guiGraphics.pose(), x + 140, y + 45, menu.getInputFluid2());
        rendererOutput.render(guiGraphics.pose(), x + 53, y + 45, menu.getOutputFluid());
    }

    private void renderProgressArrows(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 41, y + 54, 176, 0, menu.getScaledProgress(), 15);

            int arrowWidth = 9;
            int drawWidth = Math.min(menu.getScaledProgress(), arrowWidth);
            int drawX = x + 126 + arrowWidth - drawWidth;
            int textureU = 176 + (arrowWidth - drawWidth);
            int textureV = 15;

            guiGraphics.blit(TEXTURE, drawX, y + 54, textureU, textureV, drawWidth, 15);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(mouseX, mouseY, x + offsetX, y + offsetY, width, height);
    }
}
