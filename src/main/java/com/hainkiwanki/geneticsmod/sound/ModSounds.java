package com.hainkiwanki.geneticsmod.sound;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GeneticsMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static RegistryObject<SoundEvent> CLIPBONE_CRACK = registerSoundEvents("clipbone_crack");
    public static RegistryObject<SoundEvent> KNIFE_STAB = registerSoundEvents("knife_stab");
    public static RegistryObject<SoundEvent> SWAB = registerSoundEvents("swabber");
    public static RegistryObject<SoundEvent> SYRINGE_SUCK = registerSoundEvents("syringe_suck");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(GeneticsMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, ()->new SoundEvent(id));
    }
}
