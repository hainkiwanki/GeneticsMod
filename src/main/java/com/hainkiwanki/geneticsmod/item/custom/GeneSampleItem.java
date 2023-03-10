package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.mobdata.MobData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GeneSampleItem extends Item {
    public GeneSampleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.hasTag()) {
            if(pStack.getTag().getInt("identified") == 0) {
                pTooltipComponents.add(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.unidentified")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            else {
                if(Screen.hasShiftDown()) {
                    CompoundTag nbtTag = pStack.getTag();
                    Set<String> tags = nbtTag.getAllKeys();
                    for (String tagKey : tags) {
                        if (tagKey.equals("identified") || tagKey.equals("mob_type"))
                            continue;

                        TranslatableComponent tc = new TranslatableComponent("tooltip.geneticsmod.genesampleitem." + tagKey);

                        float fResult = nbtTag.getFloat(tagKey);
                        String strOutput = fResult + "";
                        if((int)fResult == 0) {
                            if (tagKey.equals(MobData.IS_HOSTILE)) {
                                strOutput = (fResult) > 0.0f ? "Hostile" : (fResult) < 0.0f ? "Friendly" : "Neutral";
                            }
                            continue;
                        }
                        tc.append(new TextComponent(strOutput).withStyle(ChatFormatting.WHITE));
                        pTooltipComponents.add(tc.withStyle(ChatFormatting.GREEN));
                    }
                }
                else {
                    pTooltipComponents.add(
                            new TranslatableComponent("tooltip.geneticsmod.genesampleitem.hold").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                                    .append(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.shift").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC))
                                    .append(new TranslatableComponent("tooltip.geneticsmod.genesampleitem.details").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
                }
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
