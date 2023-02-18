package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.block.entity.ModBlockEntities;
import com.hainkiwanki.geneticsmod.block.entity.client.TerminalBlockRenderer;
import com.hainkiwanki.geneticsmod.gui.ModMenuTypes;
import com.hainkiwanki.geneticsmod.gui.TerminalScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TERMINAL.get(), TerminalBlockRenderer::new);
    }
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.TERMINAL_MENU.get(), TerminalScreen::new);
    }
}
