package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.network.ModMessages;
import com.hainkiwanki.geneticsmod.network.packet.ChangeMobDataC2SPacket;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class DebugToolItem extends Item {

    public DebugToolItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            String msg = "";

            CompoundTag mobNbt = new CompoundTag();
            if(Screen.hasShiftDown()) {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                    data.setStat(MobData.SIZE, data.getStat(MobData.SIZE) + 0.1f);
                    data.saveNBTData(mobNbt);
                    ModMessages.send(PacketDistributor.TRACKING_ENTITY.with(() -> pInteractionTarget), new ChangeMobDataC2SPacket(mobNbt, pInteractionTarget.getId()));
                    pPlayer.sendMessage(new TextComponent("Mobsize increased: " + data.getStat(MobData.SIZE)), pPlayer.getUUID());
                });
            } else
            {
                pInteractionTarget.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                    data.setStat(MobData.SIZE, data.getStat(MobData.SIZE) - 0.1f);
                    data.saveNBTData(mobNbt);
                    ModMessages.send(PacketDistributor.TRACKING_ENTITY.with(() -> pInteractionTarget), new ChangeMobDataC2SPacket(mobNbt, pInteractionTarget.getId()));
                    pPlayer.sendMessage(new TextComponent("Mobsize decreased: " + data.getStat(MobData.SIZE)), pPlayer.getUUID());
                });
            }

            boolean state = pInteractionTarget.getType().is(ModTags.EntityTypeTags.CAN_CLIPBONE);
            pPlayer.sendMessage(new TextComponent("Can clipbone mob: " + state), pPlayer.getUUID());
        }
        return InteractionResult.SUCCESS;
    }
}
