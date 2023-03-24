package com.hainkiwanki.geneticsmod.util;

import com.hainkiwanki.geneticsmod.gui.renderer.components.Pos2i;
import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }

    public static List<String> getImportantTags(ItemStack itemStack) {
        List<String> tagList = null;
        if(itemStack.is(ModTags.ItemTags.SAMPLE_ITEM)) {
            tagList = new ArrayList<String>();
            for (String tag : itemStack.getTag().getAllKeys()) {
                if(tag.equals(MobData.MOB_TYPE) || tag.equals(MobData.IDENTIFIED)) {
                    continue;
                }
                float fRes = itemStack.getTag().getFloat(tag);
                if((int)fRes == 0 && !tag.equals(MobData.IS_HOSTILE)) {
                    continue;
                }
                tagList.add(tag);
            }
        }

        return tagList;
    }

    public static int Convert2DTo1D(int row, int col, int maxCols) {
        return row * maxCols + col;
    }

    public static Pos2i Convert1DTo2D(int index, int maxCols) {
        return new Pos2i(index % maxCols, index / maxCols);
    }
}