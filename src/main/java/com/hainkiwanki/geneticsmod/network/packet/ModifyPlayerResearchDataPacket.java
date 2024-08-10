package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.cap.researchdata.PlayerResearchProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModifyPlayerResearchDataPacket {
    private final int rp;
    private List<String> nodesUnlocked;

    public ModifyPlayerResearchDataPacket() {
        this(0, null);
    }

    public ModifyPlayerResearchDataPacket(int researchPoints) {
        this(researchPoints, null);
    }

    public ModifyPlayerResearchDataPacket(List<String> newNodes) {
        this(0, newNodes);
    }

    public ModifyPlayerResearchDataPacket(int researchPoints, List<String> newNodes) {
        this.rp = researchPoints;
        if(newNodes != null) {
            this.nodesUnlocked = new ArrayList<String>(newNodes);
        }
    }

    public static void encode(ModifyPlayerResearchDataPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.rp);
    }

    public static ModifyPlayerResearchDataPacket decode(FriendlyByteBuf buf) {
        return new ModifyPlayerResearchDataPacket(buf.readInt());
    }

    public static void handle(final ModifyPlayerResearchDataPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player != null) {
                player.getCapability(PlayerResearchProvider.PLAYER_RESEARCH_DATA).ifPresent(data -> {
                    data.increaseResearchPoints(message.rp);
                    player.sendMessage(new TextComponent("Data: " + data.getResearchPoints()), player.getUUID());
                    ModMessages.sendToClients(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncPlayerResearchDataPacket(data));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
