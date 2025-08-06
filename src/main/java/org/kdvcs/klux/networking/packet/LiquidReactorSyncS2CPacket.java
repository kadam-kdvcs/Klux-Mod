package org.kdvcs.klux.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import org.kdvcs.klux.block.entity.LiquidReactorBlockEntity;
import org.kdvcs.klux.screen.LiquidReactorMenu;

import java.util.function.Supplier;

public class LiquidReactorSyncS2CPacket {
    private final FluidStack inputFluid1;
    private final FluidStack inputFluid2;
    private final FluidStack outputFluid;
    private final BlockPos pos;

    public LiquidReactorSyncS2CPacket(FluidStack inputFluid1, FluidStack inputFluid2, FluidStack outputFluid, BlockPos pos) {
        this.inputFluid1 = inputFluid1;
        this.inputFluid2 = inputFluid2;
        this.outputFluid = outputFluid;
        this.pos = pos;
    }

    public LiquidReactorSyncS2CPacket(FriendlyByteBuf buf) {
        this.inputFluid1 = buf.readFluidStack();
        this.inputFluid2 = buf.readFluidStack();
        this.outputFluid = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(inputFluid1);
        buf.writeFluidStack(inputFluid2);
        buf.writeFluidStack(outputFluid);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof LiquidReactorBlockEntity blockEntity) {

                blockEntity.setFluids(inputFluid1, inputFluid2, outputFluid);

                if (Minecraft.getInstance().player != null &&
                        Minecraft.getInstance().player.containerMenu instanceof LiquidReactorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {

                    menu.setInputFluid1(inputFluid1);
                    menu.setInputFluid2(inputFluid2);
                    menu.setOutputFluid(outputFluid);
                }
            }
        });
        return true;
    }
}
