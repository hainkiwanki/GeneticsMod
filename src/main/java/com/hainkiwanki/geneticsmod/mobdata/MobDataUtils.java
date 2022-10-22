package com.hainkiwanki.geneticsmod.mobdata;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MobDataUtils {
    public static float getCapabilityStat(String stat, LivingEntity entity) {
        if(entity == null) return  1.0f;
        var wrapper = new Object(){ float mData = 1.0f; };
        entity.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            wrapper.mData = data.getStat(stat);
        });
        return wrapper.mData;
    }

    public static void setCapabilityStat(String stat, LivingEntity entity, float value) {
        if(entity == null) return;
        entity.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            data.setStat(stat, value);
        });
    }

    public static void IncrementCapabilityStat(String stat, LivingEntity entity, float increment) {
        if(entity == null) return;
        entity.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
            data.setStat(stat, data.getStat(stat) + increment);
        });
    }

    public static String getLivingEntityMobName(@NotNull LivingEntity entity) {
        return entity.getClass().getSimpleName();
    }
}
