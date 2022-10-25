package com.hainkiwanki.geneticsmod;

import com.hainkiwanki.geneticsmod.block.ModBlocks;
import com.hainkiwanki.geneticsmod.block.entity.ModBlockEntities;
import com.hainkiwanki.geneticsmod.gui.ModMenuTypes;
import com.hainkiwanki.geneticsmod.gui.TerminalScreen;
import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.AxolotlAttackablesSensor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GeneticsMod.MOD_ID)
public class GeneticsMod
{

    // AxolotlAttackablesSensor
    // EntityTypeTags
    // Minecraft > data > minecraft > tags > entity_types
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "geneticsmod";
    public GeneticsMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }
}
