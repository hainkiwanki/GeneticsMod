package com.hainkiwanki.geneticsmod.gui.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ModDNASlot extends SlotItemHandler {
    public ModDNASlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ModDNASlot.isDna(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack pStack) {
        return 1;
        //return ModDNASlot.isDna(pStack) ? 1 : super.getMaxStackSize(pStack);
    }

    public static boolean isDna(ItemStack stack) {
        return true;
        // return stack.is(Items.BUCKET);
    }
}
