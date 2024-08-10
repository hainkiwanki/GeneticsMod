package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchData;
import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncPlayerResearchDataPacket {
    private PlayerResearchData data;

    public SyncPlayerResearchDataPacket(PlayerResearchData data) {
        this.data = data;
    }

    public static void encode(SyncPlayerResearchDataPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data.serialize());
    }

    public static SyncPlayerResearchDataPacket decode(FriendlyByteBuf buf) {
        PlayerResearchData data = new PlayerResearchData();
        data.deserialize(buf.readNbt());
        return new SyncPlayerResearchDataPacket(data);
    }

    public static void handle(final SyncPlayerResearchDataPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if(world != null)  {
                Entity entity = world.getEntity(message.data.getPlayerId());
                if(entity != null) {
                    entity.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(data -> {
                        data.copy(message.data);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
