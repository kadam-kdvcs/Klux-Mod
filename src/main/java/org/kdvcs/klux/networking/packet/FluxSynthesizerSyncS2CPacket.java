package org.kdvcs.klux.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import org.kdvcs.klux.block.entity.FluxSynthesizerBlockEntity;
import org.kdvcs.klux.screen.FluxSynthesizerMenu;

import java.util.function.Supplier;

public class FluxSynthesizerSyncS2CPacket {
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;
    private final BlockPos pos;

    public FluxSynthesizerSyncS2CPacket(FluidStack inputFluid, FluidStack outputFluid, BlockPos pos) {
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.pos = pos;
    }

    public FluxSynthesizerSyncS2CPacket(FriendlyByteBuf buf) {
        this.inputFluid = buf.readFluidStack();
        this.outputFluid = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(inputFluid);
        buf.writeFluidStack(outputFluid);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FluxSynthesizerBlockEntity blockEntity) {

                blockEntity.setFluids(inputFluid, outputFluid);

                if (Minecraft.getInstance().player.containerMenu instanceof FluxSynthesizerMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setInputFluid(inputFluid);
                    menu.setOutputFluid(outputFluid);
                }
            }
        });
        return true;
    }

}
