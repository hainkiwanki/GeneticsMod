package com.hainkiwanki.geneticsmod.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMobDataProvider extends INBTSerializable<CompoundTag> {
    void sync(LivingEntity livingEntity);

    void forceSync(LivingEntity livingEntity);

    float getSize();

    void setSize(float f);
}
