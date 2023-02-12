package com.hainkiwanki.geneticsmod.network.packet;

import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeMobDataC2SPacket {

    private CompoundTag statNbt;
    private int entityID;

    public ChangeMobDataC2SPacket(CompoundTag nbt, int id) {
        statNbt = nbt;
        entityID = id;
    }

    public ChangeMobDataC2SPacket(FriendlyByteBuf buf) {
        statNbt = buf.readNbt();
        entityID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(statNbt);
        buf.writeInt(entityID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if(world == null) return;
            Entity entity = world.getEntity(entityID);
            if(entity == null || !(entity instanceof LivingEntity)) return;
            entity.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                data.loadNBTData(statNbt);
                entity.refreshDimensions();
            });
        });
        return true;
    }
}
