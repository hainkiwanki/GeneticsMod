package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if(!(e.getObject() instanceof LivingEntity)) return;

        if(!e.getObject().getCapability(MobDataProvider.MOB_DATA).isPresent()) {
            e.addCapability(new ResourceLocation(GeneticsMod.MOD_ID, "mobdata"), new MobDataProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobData.class);
    }
}
