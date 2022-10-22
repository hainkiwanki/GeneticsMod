package com.hainkiwanki.geneticsmod.network;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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

        net.messageBuilder(ChangeMobDataC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ChangeMobDataC2SPacket::new)
                .encoder(ChangeMobDataC2SPacket::toBytes)
                .consumer(ChangeMobDataC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    /*public static <MSG> void sendToPlayer(MSG message, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }*/
}
