package com.hainkiwanki.geneticsmod.block;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.block.custom.GeneAnalyzerBlock;
import com.hainkiwanki.geneticsmod.block.custom.GeneIsolatorBlock;
import com.hainkiwanki.geneticsmod.block.custom.TerminalBlock;
import com.hainkiwanki.geneticsmod.item.ModCreativeModeTabs;
import com.hainkiwanki.geneticsmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GeneticsMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final RegistryObject<Block> TERMINAL = registerBlock("terminal", () ->
            new TerminalBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()), ModCreativeModeTabs.GENETICS_TAB);

    public static final RegistryObject<Block> GENE_ANALYZER = registerBlock("gene_analyzer", () ->
            new GeneAnalyzerBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()), ModCreativeModeTabs.GENETICS_TAB);

    public static final RegistryObject<Block> GENE_ISOLATOR = registerBlock("gene_isolator", () ->
            new GeneIsolatorBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()), ModCreativeModeTabs.GENETICS_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
