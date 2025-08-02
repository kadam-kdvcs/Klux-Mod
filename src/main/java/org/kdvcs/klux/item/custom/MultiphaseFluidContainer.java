package org.kdvcs.klux.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MultiphaseFluidContainer extends Item {

    private static final int CAPACITY = 32000;

    public MultiphaseFluidContainer(Properties properties) {
        super(properties.stacksTo(1));
    }

    /*
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        MultiphaseFluidContainerRenderer.registerClientExtensions(consumer);
    }
     */

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerProvider(stack, CAPACITY);
    }

    private static class FluidHandlerProvider implements ICapabilityProvider {
        private final LazyOptional<IFluidHandlerItem> fluidHandler;

        public FluidHandlerProvider(ItemStack stack, int capacity) {
            this.fluidHandler = LazyOptional.of(() -> new FluidTankHandler(stack, capacity));
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
                return fluidHandler.cast();
            }
            return LazyOptional.empty();
        }
    }

    private static class FluidTankHandler extends FluidHandlerItemStack {
        public FluidTankHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        protected void setContainerToEmpty() {
            CompoundTag tag = container.getTag();
            if (tag != null) {
                tag.remove("Fluid");
                if (tag.isEmpty()) {
                    container.setTag(null);
                }
            }
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull net.minecraftforge.fluids.FluidStack stack) {
            return true;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            net.minecraftforge.fluids.FluidStack fluidStack = handler.getFluidInTank(0);
            if (!fluidStack.isEmpty()) {
                tooltip.add(Component.translatable("tooltip.klux.fluid.amount",
                                fluidStack.getDisplayName(),
                                fluidStack.getAmount(),
                                handler.getTankCapacity(0))
                        .withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.translatable("tooltip.klux.fluid.empty").withStyle(ChatFormatting.GRAY));
            }
        });
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
