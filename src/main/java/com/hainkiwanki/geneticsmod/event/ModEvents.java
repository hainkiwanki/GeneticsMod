package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onLivingEntitySpawnEvent(LivingSpawnEvent e) {
        LivingEntity entity = e.getEntityLiving();
        CompoundTag nbt = new CompoundTag();


        nbt.putString("genetics.attributes", "" + entity.getMaxHealth());
        nbt.putString("genetics.mobname", entity.getClass().getSimpleName());

    };
}
