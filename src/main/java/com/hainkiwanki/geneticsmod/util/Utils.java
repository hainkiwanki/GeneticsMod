package com.hainkiwanki.geneticsmod.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Utils {
    public static String getItemPath(ItemStack item) {
        return getItemPath(item.getItem());
    }

    public static String getItemPath(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public static String getItemNamespace(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getNamespace();
    }

    public static String getMobName(@NotNull LivingEntity entity) {
        return entity.getClass().getSimpleName();
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
        return isMouseOver(mouseX, mouseY, x, y, 16);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}