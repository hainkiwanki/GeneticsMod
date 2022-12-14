package com.hainkiwanki.geneticsmod.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> CAN_CLIPBONE = create("can_clipbone");
        public static final TagKey<EntityType<?>> CAN_KNIFE = create("can_knife");
        public static final TagKey<EntityType<?>> CAN_SWAB = create("can_swab");
        public static final TagKey<EntityType<?>> CAN_SYRINGE = create("can_syringe");

        public static TagKey<EntityType<?>> create(String pName) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(pName));
        }
    }
}
