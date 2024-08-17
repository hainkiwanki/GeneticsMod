package com.hainkiwanki.geneticsmod.research;

import com.google.gson.JsonObject;
import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ResearchUnlockCondition implements ICondition {
    public static final ResourceLocation ID = new ResourceLocation(GeneticsMod.MOD_ID, "research_unlock");
    private final String researchId;

    public ResearchUnlockCondition(String researchId) {
        this.researchId = researchId;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test() {
        return true;
    }

    public static class Serializer implements IConditionSerializer<ResearchUnlockCondition> {
        @Override
        public ResearchUnlockCondition read(JsonObject json) {
            return new ResearchUnlockCondition(GsonHelper.getAsString(json, "research"));
        }

        @Override
        public void write(JsonObject json, ResearchUnlockCondition value) {
            json.addProperty("research", value.researchId);
        }

        @Override
        public ResourceLocation getID() {
            return ResearchUnlockCondition.ID;
        }
    }
}
