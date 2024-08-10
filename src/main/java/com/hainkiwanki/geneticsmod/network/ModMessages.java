package com.hainkiwanki.geneticsmod.network;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.network.packet.EnergySyncS2CPacket;
import com.hainkiwanki.geneticsmod.network.packet.SyncPlayerResearchDataPacket;
import com.hainkiwanki.geneticsmod.network.packet.ModifyPlayerResearchDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static final String PROTOCOL = Integer.toString(1);

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(GeneticsMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> PROTOCOL)
                .clientAcceptedVersions(PROTOCOL::equals)
                .serverAcceptedVersions(PROTOCOL::equals)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ChangeMobDataC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChangeMobDataC2SPacket::decode)
                .encoder(ChangeMobDataC2SPacket::encode)
                .consumer(ChangeMobDataC2SPacket::handle)
                .add();

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumer(EnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(SyncPlayerResearchDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPlayerResearchDataPacket::decode)
                .encoder(SyncPlayerResearchDataPacket::encode)
                .consumer(SyncPlayerResearchDataPacket::handle)
                .add();

        net.messageBuilder(ModifyPlayerResearchDataPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ModifyPlayerResearchDataPacket::decode)
                .encoder(ModifyPlayerResearchDataPacket::encode)
                .consumer(ModifyPlayerResearchDataPacket::handle)
                .add();
    }

    public static void sendToClients(PacketDistributor.PacketTarget target, EnergySyncS2CPacket message) {
        INSTANCE.send(target, message);
    }

    public static void sendToClients(PacketDistributor.PacketTarget target, ChangeMobDataC2SPacket message) {
        INSTANCE.send(target, message);
    }

    public static void sendToClients(PacketDistributor.PacketTarget target, SyncPlayerResearchDataPacket message) {
        INSTANCE.send(target, message);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
