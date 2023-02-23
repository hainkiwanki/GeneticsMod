package com.hainkiwanki.geneticsmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class GeneAnalyzerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final int analyzeTime;
    private final int energyConsumption;
    private final float successRate;

    private static final int DEFAULT_TIME = 200;
    private static final String TIME_PROPERTY = "time";
    private static final int DEFAULT_ENERGY_CONSUMPTION = 2048;
    private static final String ENERGY_CONSUMPTION_PROPERTY = "energy";
    private static final float DEFAULT_SUCCESS_RATE = 0.90f;
    private static final String SUCCESS_RATE_PROPERTY = "success_rate";

    private static final String RECIPE_TYPE = "gene_analyzing";

    public GeneAnalyzerRecipe(ResourceLocation pId, ItemStack pResult, int pTime, int pEnergy, float pSuccessRate) {
        id = pId;
        output = pResult;
        analyzeTime = pTime;
        energyConsumption = pEnergy;
        successRate = pSuccessRate;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return pContainer.getItem(1).is(output.getItem());
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<GeneAnalyzerRecipe> {
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<GeneAnalyzerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(GeneticsMod.MOD_ID, RECIPE_TYPE);

        @Override
        public GeneAnalyzerRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "sample"));
            // JsonElement jsonElement = GsonHelper.getAsJsonObject(json, "object");
            // Ingredient input = Ingredient.fromJson(jsonElement);

            int time = GsonHelper.getAsInt(json,TIME_PROPERTY, DEFAULT_TIME);
            int energyConsumption = GsonHelper.getAsInt(json, ENERGY_CONSUMPTION_PROPERTY, DEFAULT_ENERGY_CONSUMPTION);
            float successRate = GsonHelper.getAsFloat(json, SUCCESS_RATE_PROPERTY, DEFAULT_SUCCESS_RATE);

            return new GeneAnalyzerRecipe(id, output, time, energyConsumption, successRate);
        }

        @Nullable
        @Override
        public GeneAnalyzerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int time = buf.readInt();
            int energy = buf.readInt();
            float successRate = buf.readFloat();
            ItemStack output = buf.readItem();
            return new GeneAnalyzerRecipe(id, output, time, energy, successRate);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GeneAnalyzerRecipe recipe) {
            buf.writeInt(recipe.analyzeTime);
            buf.writeInt(recipe.energyConsumption);
            buf.writeFloat(recipe.successRate);
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }
    }

}
