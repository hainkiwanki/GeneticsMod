package com.hainkiwanki.geneticsmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeModeTabs {
    public static final CreativeModeTab GENETICS_TAB = new CreativeModeTab("geneticsmodtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.DEBUG_TOOL.get());
        }
    };
}
