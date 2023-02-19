package com.hainkiwanki.geneticsmod.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> CAN_CLIP = create("can_clip");
        public static final TagKey<EntityType<?>> CAN_KNIFE = create("can_knife");
        public static final TagKey<EntityType<?>> CAN_SWAB = create("can_swab");
        public static final TagKey<EntityType<?>> CAN_SYRINGE = create("can_syringe");
        public static final TagKey<EntityType<?>> CAN_LAY_EGG = create("can_lay_egg");
        public static final TagKey<EntityType<?>> CAN_MILK = create("can_milk");

        public static TagKey<EntityType<?>> create(String pName) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(pName));
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> SAMPLE_ITEM = create("sample");

        public static TagKey<Item> create(String pName) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(pName));
        }
    }
}
