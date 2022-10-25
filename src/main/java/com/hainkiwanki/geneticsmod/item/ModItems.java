package com.hainkiwanki.geneticsmod.item;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.item.custom.DebugToolItem;
import com.hainkiwanki.geneticsmod.item.custom.DnaSamplerItem;
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

    public static final RegistryObject<Item> DNA_SAMPLER_TOOL =
            ITEMS.register("dna_sampler_tool", () -> new DnaSamplerItem(new Item.Properties().tab(ModCreativeModeTabs.GENETICS_TAB)));

}
