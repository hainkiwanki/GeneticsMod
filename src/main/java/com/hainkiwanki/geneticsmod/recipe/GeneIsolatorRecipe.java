package com.hainkiwanki.geneticsmod.recipe;

import com.google.gson.JsonObject;
import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GeneIsolatorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final int analyzeTime;
    private final float successRate;

    private static final int DEFAULT_TIME = 200;
    private static final String TIME_PROPERTY = "time";
    private static final float DEFAULT_SUCCESS_RATE = 0.90f;
    private static final String SUCCESS_RATE_PROPERTY = "success_rate";

    private static final String RECIPE_TYPE = "gene_analyzing";

    public GeneIsolatorRecipe(ResourceLocation pId, ItemStack pResult, int pTime, float pSuccessRate) {
        id = pId;
        output = pResult;
        analyzeTime = pTime;
        successRate = pSuccessRate;
    }

    public int getAnalyzeTime() {
        return analyzeTime;
    }

    public float getSuccessRate() {
        return successRate;
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

    public static class Type implements RecipeType<GeneIsolatorRecipe> {
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<GeneIsolatorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(GeneticsMod.MOD_ID, RECIPE_TYPE);

        @Override
        public GeneIsolatorRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "sample"));
            int time = GsonHelper.getAsInt(json,TIME_PROPERTY, DEFAULT_TIME);
            float successRate = GsonHelper.getAsFloat(json, SUCCESS_RATE_PROPERTY, DEFAULT_SUCCESS_RATE);

            return new GeneIsolatorRecipe(id, output, time, successRate);
        }

        @Nullable
        @Override
        public GeneIsolatorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int time = buf.readInt();
            float successRate = buf.readFloat();
            ItemStack output = buf.readItem();
            return new GeneIsolatorRecipe(id, output, time, successRate);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GeneIsolatorRecipe recipe) {
            buf.writeInt(recipe.analyzeTime);
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
