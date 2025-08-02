package org.kdvcs.klux.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.networking.packet.FluidExtractorSyncS2CPacket;
import org.kdvcs.klux.networking.packet.FluidAssemblerSyncS2CPacket;
import org.kdvcs.klux.networking.packet.MultiphaseFluidTankSyncS2CPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Klux.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FluidAssemblerSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidAssemblerSyncS2CPacket::new)
                .encoder(FluidAssemblerSyncS2CPacket::toBytes)
                .consumerMainThread(FluidAssemblerSyncS2CPacket::handle)
                .add();

        net.messageBuilder(FluidExtractorSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidExtractorSyncS2CPacket::new)
                .encoder(FluidExtractorSyncS2CPacket::toBytes)
                .consumerMainThread(FluidExtractorSyncS2CPacket::handle)
                .add();

        net.messageBuilder(MultiphaseFluidTankSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MultiphaseFluidTankSyncS2CPacket::new)
                .encoder(MultiphaseFluidTankSyncS2CPacket::toBytes)
                .consumerMainThread(MultiphaseFluidTankSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}