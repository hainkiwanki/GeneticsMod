package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.mobdata.MobDataUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DebugToolItem extends Item {

    public DebugToolItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            String msg = "";

            if(Screen.hasShiftDown()) {
                MobDataUtils.IncrementCapabilityStat(MobData.SIZE, pInteractionTarget, 0.1f);
            }
            msg += "CLASS: " + MobDataUtils.getLivingEntityMobName(pInteractionTarget);
            msg += ", DATA: " + MobDataUtils.getCapabilityStat(MobData.SIZE, pInteractionTarget);

            pPlayer.sendMessage(new TextComponent(msg), pPlayer.getUUID());
        }
        return InteractionResult.SUCCESS;
    }
}
