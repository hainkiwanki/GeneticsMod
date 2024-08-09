package com.hainkiwanki.geneticsmod;

import com.hainkiwanki.geneticsmod.block.ModBlocks;
import com.hainkiwanki.geneticsmod.block.ModBlockEntities;
import com.hainkiwanki.geneticsmod.cap.IMobDataProvider;
import com.hainkiwanki.geneticsmod.cap.MobDataImpl;
import com.hainkiwanki.geneticsmod.config.CommonConfig;
import com.hainkiwanki.geneticsmod.gui.ModMenuTypes;
import com.hainkiwanki.geneticsmod.item.ModItemProperties;
import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.recipe.ModRecipes;
import com.hainkiwanki.geneticsmod.sound.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
    public static Capability<IMobDataProvider> MOB_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public GeneticsMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModSounds.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModRecipes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, MOD_ID + "-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
        e.register(MobDataImpl.Provider.class);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModItems.DNA_SAMPLER_SYRINGE.get().SetSoundEvent(ModSounds.SYRINGE_SUCK.get());
        ModItems.DNA_SAMPLER_SWAB.get().SetSoundEvent(ModSounds.SWAB.get());
        ModItems.DNA_SAMPLER_KNIFE.get().SetSoundEvent(ModSounds.KNIFE_STAB.get());
        ModItems.DNA_SAMPLER_CLIPPER.get().SetSoundEvent(ModSounds.CLIPBONE_CRACK.get());

        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModItemProperties.addCustomItemProperties();
    }
}
