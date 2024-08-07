package com.hainkiwanki.geneticsmod.gui;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneAnalyzerMenu;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.menus.ResearchTableMenu;
import com.hainkiwanki.geneticsmod.gui.menus.TerminalMenu;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, GeneticsMod.MOD_ID);

    public static final RegistryObject<MenuType<TerminalMenu>> TERMINAL_MENU =
            registerMenuType(TerminalMenu::new, "terminal_menu");

    public static final RegistryObject<MenuType<GeneAnalyzerMenu>> GENE_ANALYZER_MENU =
            registerMenuType(GeneAnalyzerMenu::new, "gene_analyzer_menu");

    public static final RegistryObject<MenuType<GeneIsolatorMenu>> GENE_ISOLATOR_MENU =
            registerMenuType(GeneIsolatorMenu::new, "gene_isolator_menu");

    public static final RegistryObject<MenuType<ResearchTableMenu>> RESEARCH_TABLE_MENU =
            registerMenuType(ResearchTableMenu::new, "research_table_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
