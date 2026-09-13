package com.hainkiwanki.geneticsmod.item.custom;

import com.hainkiwanki.geneticsmod.cap.mobdata.MobDataImpl;
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
    protected TagKey<EntityType<?>> CAN_USE_SAMPLER_ON_ENTITY;
    protected HashMap<String, String> DROP_BY_ENTITY;

    protected float damageDealt = 0.0f;

    public GeneSamplerItem(TagKey<EntityType<?>> tagList, HashMap<String, String> entityDropList, Properties properties) {
        super(properties);
        this.CAN_USE_SAMPLER_ON_ENTITY = tagList;
        this.DROP_BY_ENTITY = entityDropList;
    }

    public void SetSoundEvent(SoundEvent soundEvent) {
        this.SOUNDEVENT = soundEvent;
    }

    public void OnUseCorrectTool(LivingEntity interactionTarget, Player player) {
        var entityType = interactionTarget.getType();
        var mobPath = ForgeRegistries.ENTITIES.getKey(entityType).toString();
        if (DROP_BY_ENTITY.containsKey(mobPath)) {
            ItemStack item = CreateItemStack(DROP_BY_ENTITY.get(mobPath));
            AddNbtToItem(interactionTarget, item);
            SpawnSampledItem(interactionTarget, item);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand interactionHand) {
        if(!player.level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {
            boolean usedCorrectSampler = interactionTarget.getType().is(CAN_USE_SAMPLER_ON_ENTITY);

            if(usedCorrectSampler && interactionTarget.getHealth() > 0.0f) {
                player.getCooldowns().addCooldown(this, 20);
                OnUseCorrectTool(interactionTarget, player);

                // Play Sound
                player.getLevel().playSound(null, player.blockPosition(), SOUNDEVENT, SoundSource.BLOCKS, 1f, 1f);
                if(damageDealt > 0) {
                    interactionTarget.hurt(DamageSource.GENERIC, damageDealt);
                }
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
            else {
                player.sendMessage(new TranslatableComponent("message.geneticsmod.on_sample_fail"), player.getUUID());
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public ItemStack CreateItemStack(String resLocation) {
        ResourceLocation resourceLocation = new ResourceLocation(resLocation);
        ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
        return itemStack;
    }

    public void AddNbtToItem(LivingEntity entity, ItemStack item) {
        entity.getCapability(MobDataImpl.MOB_DATA_CAPABILITY).ifPresent(data -> {
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
