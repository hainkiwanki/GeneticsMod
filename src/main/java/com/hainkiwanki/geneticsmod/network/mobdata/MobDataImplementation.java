package com.hainkiwanki.geneticsmod.network.mobdata;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

//public class MobDataImplementation {
//
//    public static class MobDataNBT implements INBTSerializable<CompoundTag> {
//        private Map<EMobStat, Float> mobDataMap = new HashMap<>(){};
//        private final LivingEntity livingEntity;
//
//        private MobDataNBT(LivingEntity livingEntity) {
//            this.livingEntity = livingEntity;
//        }
//
//        public void sync(@Nonnull LivingEntity livingEntity) {
//            ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(serializeNBT(), livingEntity.getId()));
//        }
//
//        public void setMobDataStat(EMobStat mobStat, float value, @Nonnull LivingEntity livingEntity) {
//            this.setStat(mobStat, value);
//            livingEntity.refreshDimensions();
//            sync(livingEntity);
//        }
//
//        @Override
//        public CompoundTag serializeNBT() {
//            CompoundTag props = new CompoundTag();
//            for(Map.Entry<EMobStat, Float> entry : mobDataMap.entrySet()) {
//                props.putFloat(entry.getKey().name(), entry.getValue());
//                if(entry.getKey() == EMobStat.SIZE) {
//                    System.out.println(entry.getKey().name() + ": " + entry.getValue());
//                }
//            }
//            return props;
//        }
//
//        @Override
//        public void deserializeNBT(CompoundTag nbt) {
//            EMobStat[] keys = EMobStat.values();
//            for(EMobStat mobStat : keys) {
//                setStat(mobStat, nbt.getFloat(mobStat.name()));
//                if(mobStat == EMobStat.SIZE) {
//                    System.out.println(mobStat + ": " +  nbt.getFloat(mobStat.name()));
//                }
//            }
//        }
//
//        public void setStat(EMobStat stat, float f) {
//            mobDataMap.put(stat, f);
//        }
//
//        public float getStat(EMobStat stat) {
//            if(!mobDataMap.containsKey(stat)) {
//                setStat(stat, 1.0f);
//            }
//            return mobDataMap.get(stat);
//        }
//
//        public boolean hasStat(EMobStat stat) {
//            return mobDataMap.containsKey(stat);
//        }
//    }
//}
