package org.kdvcs.klux.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.kdvcs.klux.screen.renderer.FluidTankRenderer;

import java.util.function.Consumer;

/*
 *
 * I have no idea to do this.
 *
 *
 * @Kadam =_=
 *
 */

@Deprecated
public class MultiphaseFluidContainerRenderer extends BlockEntityWithoutLevelRenderer {

    private final FluidTankRenderer fluidRenderer = new FluidTankRenderer(32000, true, 16, 16);

    public MultiphaseFluidContainerRenderer(BlockEntityRenderDispatcher dispatcher,
                                            EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource bufferSource, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal("MultiphaseFluidContainerRenderer renderByItem called!"));
        }

        mc.getItemRenderer().renderStatic(stack, displayContext, light, overlay, poseStack, bufferSource, null, 0);

        stack.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            FluidStack fluid = handler.getFluidInTank(0);
            if (fluid != null && !fluid.isEmpty()) {
                if (mc.player != null) {
                    mc.player.sendSystemMessage(Component.literal("Rendering fluid: " + fluid.getDisplayName().getString() + ", amount: " + fluid.getAmount()));
                }
                poseStack.pushPose();
                poseStack.translate(0.0F, 0.0F, 0.1F);
                fluidRenderer.render(poseStack, 0, 0, fluid);
                poseStack.popPose();
            } else {
                if (mc.player != null) {
                    mc.player.sendSystemMessage(Component.literal("No fluid found to render."));
                }
            }
        });
    }

    public static void registerClientExtensions(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer =
                    new MultiphaseFluidContainerRenderer(
                            net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                            net.minecraft.client.Minecraft.getInstance().getEntityModels()
                    );

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

}
