package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.item.ModItems;
import com.hainkiwanki.geneticsmod.mobdata.MobDataProvider;
import com.hainkiwanki.geneticsmod.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class DnaSamplerItem extends Item {
    public DnaSamplerItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            Item inHandItem = pStack.getItem();
            var entityTypes = Utils.entityTypesPerSamplerItem.get(inHandItem);
            boolean usedCorrectSampler = pInteractionTarget.getType().is(entityTypes);
            if(usedCorrectSampler) {
                var entityType = pInteractionTarget.getType();
                var mobPath = ForgeRegistries.ENTITIES.getKey(entityType).toString();
                if(Utils.entityDrops.get(inHandItem).containsKey(mobPath)) {
                    var mobDrop = Utils.entityDrops.get(inHandItem).get(mobPath);

                    ResourceLocation resourceLocation = new ResourceLocation(mobDrop);
                    ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));

                    pInteractionTarget.getCapability(MobDataProvider.MOB_DATA).ifPresent(data -> {
                        CompoundTag tag = new CompoundTag();
                        data.saveNBTData(tag);
                        item.setTag(tag);
                    });
                    ItemEntity ent = pInteractionTarget.spawnAtLocation(item, 1.0f);
                    Random rand = new java.util.Random();
                    ent.setDeltaMovement(ent.getDeltaMovement().add(
                            (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F),
                            (double) (rand.nextFloat() * 0.05F),
                            (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                }
            }
        }
        return InteractionResult.CONSUME;
    }
}
