package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeMobDataC2SPacket {

    private CompoundTag statNbt;
    private int entityID;

    public ChangeMobDataC2SPacket(CompoundTag nbt, int id) {
        this.statNbt = nbt;
        this.entityID = id;
    }

    public static void encode(ChangeMobDataC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.statNbt);
        buf.writeInt(msg.entityID);
    }

    public static ChangeMobDataC2SPacket decode(FriendlyByteBuf buf) {
        return new ChangeMobDataC2SPacket(buf.readNbt(), buf.readInt());
    }

    public static void handle(final ChangeMobDataC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if(world == null)  {
                return;
            }
            Entity entity = world.getEntity(message.entityID);
            if(!(entity instanceof LivingEntity)) {
                return;
            }
            entity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(data -> {
                data.deserializeNBT(message.statNbt);
                entity.refreshDimensions();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
