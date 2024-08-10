package com.hainkiwanki.geneticsmod.item;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.item.custom.*;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GeneticsMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    public static final RegistryObject<Item> DEBUG_TOOL =
            ITEMS.register("debug_tool", () -> new DebugToolItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<ClipboneItem> DNA_SAMPLER_CLIPPER = ITEMS.register("dna_sampler_clipper",
            () -> new ClipboneItem(ModTags.EntityTypeTags.CAN_CLIP,
                    new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB).stacksTo(1).durability(64)));

    public static final RegistryObject<KnifeItem> DNA_SAMPLER_KNIFE = ITEMS.register("dna_sampler_knife",
            () -> new KnifeItem(ModTags.EntityTypeTags.CAN_KNIFE,
                    new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB).stacksTo(1).durability(64)));

    public static final RegistryObject<SyringeItem> DNA_SAMPLER_SYRINGE = ITEMS.register("dna_sampler_syringe",
            () -> new SyringeItem(ModTags.EntityTypeTags.CAN_SYRINGE,
                    new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB).stacksTo(1).durability(128)));

    public static final RegistryObject<SwabItem> DNA_SAMPLER_SWAB = ITEMS.register("dna_sampler_swab",
            () -> new SwabItem(ModTags.EntityTypeTags.CAN_SWAB,
                    new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB).stacksTo(1)));


    public static final RegistryObject<Item> BLAZE_CLIPPING = ITEMS.register("blaze_clipping", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> GUARDIAN_ROD = ITEMS.register("guardian_rod", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> ELDER_GUARDIAN_ROD = ITEMS.register("elder_guardian_rod", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> COW_HORN = ITEMS.register("cow_horn", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> RAVAGER_HORN = ITEMS.register("ravager_horn", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> PIGLIN_TUSK = ITEMS.register("piglin_tusk", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> RIB_BONE = ITEMS.register("rib_bone", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> WITHER_RIB_BONE = ITEMS.register("wither_rib_bone", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> CREEPER_CHUNK = ITEMS.register("creeper_chunk", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SNOW_GOLEM_CHUNK = ITEMS.register("snow_golem_chunk", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> IRON_GOLEM_CHUNK = ITEMS.register("iron_golem_chunk", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SHULKER_FRAGMENT = ITEMS.register("shulker_fragment", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> ENDERMITE_TAIL = ITEMS.register("endermite_tail", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SILVERFISH_TAIL = ITEMS.register("silverfish_tail", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> CAVE_SPIDER_LEG = ITEMS.register("cave_spider_leg", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SPIDER_LEG = ITEMS.register("spider_leg", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> CAVE_SPIDER_SKIN = ITEMS.register("cave_spider_skin", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SPIDER_SKIN = ITEMS.register("spider_skin", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> CREEPER_SKIN = ITEMS.register("creeper_skin", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> HOGLIN_EAR = ITEMS.register("hoglin_ear", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> ZOMBIE_FLESH = ITEMS.register("zombie_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> HUSK_FLESH = ITEMS.register("husk_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> DROWNED_FLESH = ITEMS.register("drowned_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> DOLPHIN_FLESH = ITEMS.register("dolphin_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> ENDERMAN_FLESH = ITEMS.register("enderman_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> PHANTOM_FLESH = ITEMS.register("phantom_flesh", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> ZOMBIE_HORSE_MEAT = ITEMS.register("zombie_horse_meat", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> GHAST_TENTACLE = ITEMS.register("ghast_tentacle", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> GLOW_SQUID_TENTACLE = ITEMS.register("glow_squid_tentacle", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SQUID_TENTACLE = ITEMS.register("squid_tentacle", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> STRIDER_HAIR = ITEMS.register("strider_hair", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> HOLGIN_HAIR =  ITEMS.register("hoglin_hair", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> PUFFERFISH_SPIKES = ITEMS.register("pufferfish_spikes", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SCALES_PUFFERFISH = ITEMS.register("scales_pufferfish", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SCALES_COD = ITEMS.register("scales_cod", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SCALES_SLAMON = ITEMS.register("scales_salmon", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SCALES_TROPICAL_FISH = ITEMS.register("scales_tropical_fish", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> TURTLE_SHELL = ITEMS.register("turtle_shell", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

    public static final RegistryObject<Item> SQUID_INK = ITEMS.register("squid_ink", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> SLIME_BLOB = ITEMS.register("slime_blob", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> MAGMA_SLIME_BLOB = ITEMS.register("magma_slime_blob", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
    public static final RegistryObject<Item> VEX_ECTOPLASM = ITEMS.register("vex_ectoplasm", () -> new GeneSampleItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));
}
