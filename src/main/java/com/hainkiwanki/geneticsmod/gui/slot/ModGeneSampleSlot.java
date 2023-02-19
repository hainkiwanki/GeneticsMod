package com.hainkiwanki.geneticsmod.gui.slot;

import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ModGeneSampleSlot extends SlotItemHandler {
    public ModGeneSampleSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {

        return ModGeneSampleSlot.isGeneSample(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack pStack) {
        return 1;
    }

    public static boolean isGeneSample(ItemStack stack) {
        return stack.is(ModTags.ItemTags.SAMPLE_ITEM);
    }
}
