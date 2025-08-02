package org.kdvcs.klux.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import org.kdvcs.klux.block.entity.FluidExtractorBlockEntity;
import org.kdvcs.klux.block.entity.MultiphaseFluidTankBlockEntity;
import org.kdvcs.klux.screen.FluidExtractorMenu;
import org.kdvcs.klux.screen.MultiphaseFluidTankMenu;

import java.util.function.Supplier;

public class MultiphaseFluidTankSyncS2CPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public MultiphaseFluidTankSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public MultiphaseFluidTankSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof MultiphaseFluidTankBlockEntity blockEntity) {
                blockEntity.setFluid(this.fluidStack);

                if(Minecraft.getInstance().player.containerMenu instanceof MultiphaseFluidTankMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluid(this.fluidStack);
                }
            }
        });
        return true;
    }
}
