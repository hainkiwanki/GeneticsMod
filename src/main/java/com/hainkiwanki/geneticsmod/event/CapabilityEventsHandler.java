package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.cap.mobdata.MobDataImpl;
import com.hainkiwanki.geneticsmod.cap.research.PlayerResearchProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(GeneticsMod.MOD_ID)
public class CapabilityEventsHandler {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobDataImpl.Provider.class);
        e.register(PlayerResearchProvider.class);
    }

    @SubscribeEvent
    public static void onAttackCapabilitiesMobData(AttachCapabilitiesEvent<Entity> e) {
        if((e.getObject() instanceof Mob)) {
            e.addCapability(MobDataImpl.Provider.RESOURCE_LOCATION, new MobDataImpl.Provider((LivingEntity) e.getObject()));
        }

        if((e.getObject() instanceof Player)) {
            e.addCapability(PlayerResearchProvider.RESOURCE_LOCATION, new PlayerResearchProvider());
        }
    }
}
