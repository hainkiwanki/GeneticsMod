package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.cap.research.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.research.PlayerResearchProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResearchC2SPacket {
    private PlayerResearchData data;

    public ResearchC2SPacket(PlayerResearchData data) {
        this.data = data;
    }

    public static void encode(ResearchC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data.serialize());
    }

    public static ResearchC2SPacket decode(FriendlyByteBuf buf) {
        PlayerResearchData data = new PlayerResearchData();
        data.deserialize(buf.readNbt());
        return new ResearchC2SPacket(data);
    }

    public static void handle(final ResearchC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if(world != null)  {
                Entity entity = world.getEntity(message.data.getPlayerId());
                if(entity instanceof Player) {
                    entity.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(data -> {
                        data.deserialize(message.data.serialize());
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
