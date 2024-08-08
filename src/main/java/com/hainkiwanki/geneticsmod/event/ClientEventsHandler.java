package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.block.ModBlockEntities;
import com.hainkiwanki.geneticsmod.block.entity.client.TerminalBlockRenderer;
import com.hainkiwanki.geneticsmod.gui.ModMenuTypes;
import com.hainkiwanki.geneticsmod.gui.renderer.GeneAnalyzerScreen;
import com.hainkiwanki.geneticsmod.gui.renderer.GeneIsolatorScreen;
import com.hainkiwanki.geneticsmod.gui.renderer.ResearchTableScreen;
import com.hainkiwanki.geneticsmod.gui.renderer.TerminalScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventsHandler {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        System.out.println("ClientEventsHandler, registerRenderers start");
        event.registerBlockEntityRenderer(ModBlockEntities.TERMINAL.get(), TerminalBlockRenderer::new);
    }
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        System.out.println("ClientEventsHandler, clientSetup start");
        MenuScreens.register(ModMenuTypes.TERMINAL_MENU.get(), TerminalScreen::new);
        MenuScreens.register(ModMenuTypes.GENE_ANALYZER_MENU.get(), GeneAnalyzerScreen::new);
        MenuScreens.register(ModMenuTypes.GENE_ISOLATOR_MENU.get(), GeneIsolatorScreen::new);
        MenuScreens.register(ModMenuTypes.RESEARCH_TABLE_MENU.get(), ResearchTableScreen::new);
    }
}
