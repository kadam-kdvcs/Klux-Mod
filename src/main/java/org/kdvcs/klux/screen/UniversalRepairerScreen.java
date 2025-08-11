package org.kdvcs.klux.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.screen.renderer.FluidTankRenderer;
import org.kdvcs.klux.util.MouseUtil;

import java.util.List;
import java.util.Optional;

public class UniversalRepairerScreen extends AbstractContainerScreen<UniversalRepairerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Klux.MODID, "textures/gui/gui_universal_repairer.png");

    private FluidTankRenderer fluidRenderer;

    public UniversalRepairerScreen(UniversalRepairerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 170;
    }

    @Override
    protected void init() {
        super.init();

        fluidRenderer = new FluidTankRenderer(64000, true, 54, 61);

        this.titleLabelX = 67;
        this.titleLabelY = 5;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int guiLeft = (width - imageWidth) / 2;
        int guiTop = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, imageWidth, imageHeight);

        renderProgressBar(guiGraphics, guiLeft, guiTop);
        fluidRenderer.render(guiGraphics.pose(), guiLeft + 61, guiTop + 17, menu.getFluidStack());

        //METER
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 200);
        guiGraphics.blit(TEXTURE, guiLeft + 61, guiTop + 17, 191, 0, 54, 61);
        guiGraphics.pose().pushPose();
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int guiLeft, int guiTop) {
        if (menu.isRepairing()) {
            int progressWidth = menu.getScaledProgress();
            guiGraphics.blit(TEXTURE, guiLeft + 149, guiTop + 19, 176, 0, progressWidth, 15);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x2A241E, false);

        int guiLeft = (width - imageWidth) / 2;
        int guiTop = (height - imageHeight) / 2;

        renderFluidTooltip(guiGraphics, mouseX, mouseY, guiLeft, guiTop);
    }

    private void renderFluidTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if (isMouseOverArea(mouseX, mouseY, guiLeft, guiTop, 61, 17)) {
            FluidStack fluid = menu.getFluidStack();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = fluidRenderer.getTooltip(fluid,
                        Minecraft.getInstance().options.advancedItemTooltips
                                ? net.minecraft.world.item.TooltipFlag.Default.ADVANCED
                                : net.minecraft.world.item.TooltipFlag.Default.NORMAL);
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX - guiLeft, mouseY - guiTop);
            }
        }
    }

    private boolean isMouseOverArea(int mouseX, int mouseY, int guiLeft, int guiTop, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(mouseX, mouseY, guiLeft + offsetX, guiTop + offsetY,
                fluidRenderer.getWidth(), fluidRenderer.getHeight());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
