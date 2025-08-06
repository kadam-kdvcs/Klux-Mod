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

public class MultiphaseFluidTankScreen extends AbstractContainerScreen<MultiphaseFluidTankMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Klux.MODID, "textures/gui/gui_multiphase_fluid_tank.png");

    private FluidTankRenderer fluidRenderer;

    public MultiphaseFluidTankScreen(MultiphaseFluidTankMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 170;
    }

    @Override
    protected void init() {
        super.init();
        fluidRenderer = new FluidTankRenderer(256000, true, 72, 61);
        this.titleLabelX = 52;
        this.titleLabelY = 5;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x2A241E, false);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderFluidTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (isMouseOverArea(mouseX, mouseY, x, y, 52, 17)) {
            FluidStack fluidStack = menu.blockEntity.getFluidStack();

            if (!fluidStack.isEmpty()) {
                List<Component> tooltip = fluidRenderer.getTooltip(fluidStack,
                        Minecraft.getInstance().options.advancedItemTooltips
                                ? TooltipFlag.Default.ADVANCED
                                : TooltipFlag.Default.NORMAL);
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - x, mouseY - y);
            }
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

        fluidRenderer.render(guiGraphics.pose(), x + 52, y + 17, menu.blockEntity.getFluidStack());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseOverArea(int mouseX, int mouseY, int guiX, int guiY, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(mouseX, mouseY, guiX + offsetX, guiY + offsetY, fluidRenderer.getWidth(), fluidRenderer.getHeight());
    }
}
