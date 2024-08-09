package com.hainkiwanki.geneticsmod.cap;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobDataImpl {
    private static class DefaultImpl implements IMobDataProvider {
        private final LivingEntity livingEntity;
        private boolean isDirty = true;
        private final Map<String, Float> mobDataMap = new HashMap<>(){};

        private DefaultImpl(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
        }

        @Override
        public void sync(LivingEntity livingEntity) {
            if(!this.isDirty) {
                return;
            }
            this.isDirty = false;
            ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(serializeNBT(), livingEntity.getId()));
            livingEntity.refreshDimensions();
        }

        @Override
        public void forceSync(LivingEntity livingEntity) {
            ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(serializeNBT(), livingEntity.getId()));
            livingEntity.refreshDimensions();
        }

        @Override
        public float getSize() {
            return getStat(EMobStat.SIZE);
        }

        @Override
        public void setSize(float f) {
            this.isDirty = true;
            setStat(EMobStat.SIZE, f);
            livingEntity.refreshDimensions();
            sync(this.livingEntity);
        }

        @Override
        public float getStat(EMobStat stat) {
            String key = stat.toStringKey();
            if(!mobDataMap.containsKey(key)) {
                initializeMap();
            }
            if(mobDataMap.get(key) == null) {
                System.out.println(key + " is null" + mobDataMap);
            }
            return mobDataMap.get(key) == null ? 0.0f : mobDataMap.get(key);
        }

        @Override
        public void setStat(EMobStat stat, float f) {
            String key = stat.toStringKey();
            if(!mobDataMap.containsKey(key)) {
                initializeMap();
            }
            mobDataMap.put(key, f);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            EMobStat[] statList = EMobStat.getFilteredStats();
            for (EMobStat stat: statList) {
                tag.putFloat(stat.toStringKey(), getStat(stat));
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            EMobStat[] statList = EMobStat.getFilteredStats();
            for (EMobStat stat: statList) {
                setStat(stat, nbt.getFloat(stat.toStringKey()));
            }
        }

        private void initializeMap() {
            mobDataMap.put(EMobStat.SIZE.toStringKey(), 1.0f);
            mobDataMap.put(EMobStat.HEALTH.toStringKey(), this.livingEntity.getMaxHealth());

            mobDataMap.put(EMobStat.DROPS_MOD.toStringKey(), 1.0f);
            mobDataMap.put(EMobStat.EXP_MOD.toStringKey(), 1.0f);
            mobDataMap.put(EMobStat.FERTILITY.toStringKey(), this.livingEntity instanceof AgeableMob ? 1.0f: 0.0f);

            float canBreath = this.livingEntity.canBreatheUnderwater() ? 1.0f : 0.0f;
            mobDataMap.put(EMobStat.GILLS.toStringKey(), canBreath);

            float isImmuneToFire = this.livingEntity.fireImmune() ? 1.0f : 0.0f;
            mobDataMap.put(EMobStat.FIRE_IMMUNE.toStringKey(), isImmuneToFire);

            float canLayEggs = 0.0f;
            if(this.livingEntity.getType().is(ModTags.EntityTypeTags.CAN_LAY_EGG))
                canLayEggs = 1.0f;
            mobDataMap.put(EMobStat.OVIPAROUS.toStringKey(), canLayEggs);

            float canMilk = 0.0f;
            if (this.livingEntity.getType().is(ModTags.EntityTypeTags.CAN_MILK))
                canMilk = 1.0f;
            mobDataMap.put(EMobStat.LACTATION.toStringKey(), canMilk);

            mobDataMap.put(EMobStat.MATURING_TIME.toStringKey(), 0.0f);

            if(NeutralMob.class.isAssignableFrom(this.livingEntity.getClass())) {
                mobDataMap.put(EMobStat.HOSTILITY.toStringKey(), 0.0f);
            } else if(Enemy.class.isAssignableFrom(this.livingEntity.getClass())) {
                mobDataMap.put(EMobStat.HOSTILITY.toStringKey(), 1.0f);
            } else {
                mobDataMap.put(EMobStat.HOSTILITY.toStringKey(), -1.0f);
            }

            mobDataMap.put(EMobStat.ATTACK_DAMAGE.toStringKey(), TryGetAttribute(this.livingEntity, Attributes.ATTACK_DAMAGE));
            mobDataMap.put(EMobStat.ATTACK_SPEED.toStringKey(), TryGetAttribute(this.livingEntity, Attributes.ATTACK_SPEED));
            mobDataMap.put(EMobStat.MOVE_SPEED.toStringKey(), TryGetAttribute(this.livingEntity, Attributes.MOVEMENT_SPEED));
        }

        private float TryGetAttribute(LivingEntity pEntity, Attribute pAttribute) {
            float result = 0.0f;
            if(pEntity.getAttributes().hasAttribute(pAttribute)){
                result = (float)pEntity.getAttributeBaseValue(pAttribute);
            }
            return result;
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(GeneticsMod.MOD_ID, "mobdata");

        private final DefaultImpl impl;
        private final LazyOptional<IMobDataProvider> cap;

        public Provider(LivingEntity livingEntity) {
            impl = new DefaultImpl(livingEntity);
            cap = LazyOptional.of(() -> impl);
        }

        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
            if (capability == GeneticsMod.MOB_DATA_CAPABILITY) {
                return cap.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
