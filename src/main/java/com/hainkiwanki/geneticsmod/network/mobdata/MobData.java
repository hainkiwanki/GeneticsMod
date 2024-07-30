package com.hainkiwanki.geneticsmod.network.mobdata;

import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobData implements INBTSerializable<CompoundTag> {
    private Map<EMobStat, Float> mobDataMap = new HashMap<>(){};

    public static String getAttributesAsString() {
        String str = EMobStat.SIZE + ", " +
                EMobStat.HEALTH + ", " +
                EMobStat.ATTACK_DAMAGE + ", " +
                EMobStat.ATTACK_SPEED + ", " +
                EMobStat.MOVE_SPEED + ", " +
                EMobStat.DROPS_MOD + ", " +
                EMobStat.EXP_MOD + ", " +
                EMobStat.FERTILITY + ", " +
                EMobStat.GILLS + ", " +
                EMobStat.FIRE_IMMUNE + ", " +
                EMobStat.OVIPAROUS + ", " +
                EMobStat.LACTATION + ", " +
                EMobStat.MATURING_TIME + ", " +
                EMobStat.HOSTILITY + ", ";
        return str;
    }

    public void initialize(LivingEntity pEntity) {
        mobDataMap.put(EMobStat.SIZE, 1.0f);
        mobDataMap.put(EMobStat.HEALTH, pEntity.getMaxHealth());

        mobDataMap.put(EMobStat.DROPS_MOD, 1.0f);
        mobDataMap.put(EMobStat.EXP_MOD, 1.0f);
        mobDataMap.put(EMobStat.FERTILITY, pEntity instanceof AgeableMob ? 1.0f: 0.0f);

        float canBreath = pEntity.canBreatheUnderwater() ? 1.0f : 0.0f;
        mobDataMap.put(EMobStat.GILLS, canBreath);

        float isImmuneToFire = pEntity.fireImmune() ? 1.0f : 0.0f;
        mobDataMap.put(EMobStat.FIRE_IMMUNE, isImmuneToFire);

        float canLayEggs = 0.0f;
        if(pEntity.getType().is(ModTags.EntityTypeTags.CAN_LAY_EGG))
            canLayEggs = 1.0f;
        mobDataMap.put(EMobStat.OVIPAROUS, canLayEggs);

        float canMilk = 0.0f;
        if (pEntity.getType().is(ModTags.EntityTypeTags.CAN_MILK))
            canMilk = 1.0f;
        mobDataMap.put(EMobStat.OVIPAROUS, canMilk);

        mobDataMap.put(EMobStat.MATURING_TIME, 0.0f);

        if(NeutralMob.class.isAssignableFrom(pEntity.getClass())) {
            mobDataMap.put(EMobStat.HOSTILITY, 0.0f);
        } else if(Enemy.class.isAssignableFrom(pEntity.getClass())) {
            mobDataMap.put(EMobStat.HOSTILITY, 1.0f);
        } else {
            mobDataMap.put(EMobStat.HOSTILITY, -1.0f);
        }

        mobDataMap.put(EMobStat.ATTACK_DAMAGE, TryGetAttribute(pEntity, Attributes.ATTACK_DAMAGE));
        mobDataMap.put(EMobStat.ATTACK_SPEED, TryGetAttribute(pEntity, Attributes.ATTACK_SPEED));
        mobDataMap.put(EMobStat.MOVE_SPEED, TryGetAttribute(pEntity, Attributes.MOVEMENT_SPEED));
    }

    private float TryGetAttribute(LivingEntity pEntity, Attribute pAttribute) {
        float result = 0.0f;
        if(pEntity.getAttributes().hasAttribute(pAttribute)){
            result = (float)pEntity.getAttributeBaseValue(pAttribute);
        }
        return result;
    }

    public boolean hasStat(EMobStat stat) {
        return mobDataMap.containsKey(stat);
    }

    public float getStat(EMobStat stat) {
        if(!mobDataMap.containsKey(stat)) {
            setStat(stat, 1.0f);
        }
        return mobDataMap.get(stat);
    }

    public void setStat(EMobStat stat, float f) {
        mobDataMap.put(stat, f);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag props = new CompoundTag();
        for(Map.Entry<EMobStat, Float> entry : mobDataMap.entrySet()) {
            props.putFloat(entry.getKey().name(), entry.getValue());
        }
        return props;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        EMobStat[] keys = EMobStat.values();
        for(EMobStat mobStat : keys) {
            setStat(mobStat, nbt.getFloat(mobStat.name()));
        }
    }

    public void sync(@Nonnull LivingEntity livingEntity) {
        ModMessages.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ChangeMobDataC2SPacket(serializeNBT(), livingEntity.getId()));
    }

    public void setMobDataStat(EMobStat mobStat, float value, @Nonnull LivingEntity livingEntity) {
        this.setStat(mobStat, value);
        livingEntity.refreshDimensions();
        sync(livingEntity);
    }
}
