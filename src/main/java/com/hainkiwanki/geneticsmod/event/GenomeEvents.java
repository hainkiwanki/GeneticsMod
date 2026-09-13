package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.genetics.gene.GeneDefinition;
import com.hainkiwanki.geneticsmod.genetics.gene.GeneRegistry;
import com.hainkiwanki.geneticsmod.genetics.gene.GeneReloadListener;
import com.hainkiwanki.geneticsmod.genetics.genome.BaseGenomeReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = GeneticsMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class GenomeEvents {
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BaseGenomeReloadListener());
        event.addListener(new GeneReloadListener());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ResourceLocation flightId = new ResourceLocation("geneticsmod", "flight");
        GeneDefinition flight = GeneRegistry.get(flightId);
        if (flight != null) {
            System.out.println("Flight required: " + flight.getRequiredTags());
            System.out.println("Flight compatible: " + flight.getCompatibilityTags());
            System.out.println("Flight incompatible: " + flight.getIncompatibilityTags());
        }
    }
}