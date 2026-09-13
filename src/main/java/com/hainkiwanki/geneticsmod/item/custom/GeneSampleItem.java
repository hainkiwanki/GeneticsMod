package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.cap.mobdata.EMobStat;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneSampleItem extends Item {
    public GeneSampleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponent, TooltipFlag isAdvanced) {
        if(stack.hasTag()) {
            if(stack.getTag().getInt(EMobStat.IDENTIFIED.toStringKey()) == 0) {
                tooltipComponent.add(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.unidentified")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            else {
                if(Screen.hasShiftDown()) {
                    CompoundTag nbtTag = stack.getTag();
                    List<String> tagKeys = Utils.getImportantTags(stack);
                    for (String tag : tagKeys) {
                        TranslatableComponent tc = new TranslatableComponent("tooltip.geneticsmod.genesampleitem." + tag);
                        float fResult = nbtTag.getFloat(tag);
                        String strOutput = fResult + "";
                        if (tag.equals(EMobStat.HOSTILITY.toStringKey())) {
                            strOutput = (fResult) > 0.0f ? "Hostile" : (fResult) < 0.0f ? "Friendly" : "Neutral";
                        }
                        tc.append(": ").append(new TextComponent(strOutput).withStyle(ChatFormatting.WHITE));
                        tooltipComponent.add(tc.withStyle(ChatFormatting.GREEN));
                    }
                }
                else {
                    tooltipComponent.add(
                            new TranslatableComponent("tooltip.geneticsmod.genesampleitem.hold").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                                    .append(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.shift").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC))
                                    .append(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.details").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
                }
            }
        }
        super.appendHoverText(stack, level, tooltipComponent, isAdvanced);
    }
}
