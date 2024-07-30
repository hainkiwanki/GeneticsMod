package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.network.mobdata.MobDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Random;

public class GeneSamplerItem extends Item {
    // test nbt ingame: /data get entity @s SelectedItem
    protected SoundEvent SOUNDEVENT;
    protected TagKey<EntityType<?>> ENTITY_CAN_USE_CLIPBONE;
    protected HashMap<String, String> DROP_BY_ENTITY;

    protected float damageDealt = 0.0f;

    public GeneSamplerItem(TagKey<EntityType<?>> tagList, HashMap<String, String> entityDropList, Properties pProperties) {
        super(pProperties);
        this.ENTITY_CAN_USE_CLIPBONE = tagList;
        this.DROP_BY_ENTITY = entityDropList;
    }

    public void SetSoundEvent(SoundEvent pSoundEvent) {
        this.SOUNDEVENT = pSoundEvent;
    }

    public void OnUseCorrectTool(LivingEntity pInteractionTarget, Player pPlayer) {
        var entityType = pInteractionTarget.getType();
        var mobPath = ForgeRegistries.ENTITIES.getKey(entityType).toString();
        if (DROP_BY_ENTITY.containsKey(mobPath)) {
            ItemStack item = CreateItemStack(DROP_BY_ENTITY.get(mobPath));
            AddNbtToItem(pInteractionTarget, item);
            SpawnSampledItem(pInteractionTarget, item);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!pPlayer.level.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            boolean usedCorrectSampler = pInteractionTarget.getType().is(ENTITY_CAN_USE_CLIPBONE);

            if(usedCorrectSampler && pInteractionTarget.getHealth() > 0.0f) {
                pPlayer.getCooldowns().addCooldown(this, 20);
                OnUseCorrectTool(pInteractionTarget, pPlayer);

                // Play Sound
                pPlayer.getLevel().playSound(null, pPlayer.blockPosition(), SOUNDEVENT, SoundSource.BLOCKS, 1f, 1f);
                if(damageDealt > 0) {
                    pInteractionTarget.hurt(DamageSource.GENERIC, damageDealt);
                }
                pStack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
            else {
                if(pInteractionTarget.getHealth() > 0.0f) {
                    pPlayer.sendMessage(new TranslatableComponent("message.geneticsmod.on_sample_fail"
                            + pInteractionTarget.getClass().getSimpleName()), pPlayer.getUUID());
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public ItemStack CreateItemStack(String resLocation) {
        ResourceLocation resourceLocation = new ResourceLocation(resLocation);
        ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
        return itemStack;
    }

    public void AddNbtToItem(LivingEntity entity, ItemStack item) {
        entity.getCapability(MobDataProvider.MOB_DATA_CAPABILITY).ifPresent(data -> {
            CompoundTag tag = data.serializeNBT();
            tag.putInt("identified", 0);
            item.setTag(tag);
        });
    }


    public void SpawnSampledItem(LivingEntity entity, ItemStack item) {
        ItemEntity ent = entity.spawnAtLocation(item, 1.0f);
        Random rand = new java.util.Random();
        ent.setDeltaMovement(ent.getDeltaMovement().add(
                (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F),
                (double) (rand.nextFloat() * 0.05F),
                (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));

    }
}
