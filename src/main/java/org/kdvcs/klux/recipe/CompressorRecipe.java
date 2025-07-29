package org.kdvcs.klux.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;

public class CompressorRecipe implements Recipe<SimpleContainer> {

    private final CountedIngredient inputA;
    private final CountedIngredient inputB;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int maxProgress;

    public CompressorRecipe(CountedIngredient inputA, CountedIngredient inputB, ItemStack output, ResourceLocation id, int maxProgress) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
        this.id = id;
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public CountedIngredient getInputA() {
        return inputA;
    }

    public CountedIngredient getInputB() {
        return inputB;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        ItemStack slot0 = container.getItem(0);
        ItemStack slot1 = container.getItem(1);

        return inputA.ingredient.test(slot0) && slot0.getCount() >= inputA.count &&
                inputB.ingredient.test(slot1) && slot1.getCount() >= inputB.count;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, inputA.ingredient, inputB.ingredient);
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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

    public static class Type implements RecipeType<CompressorRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "compressor";
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "compressor");

        @Override
        public CompressorRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            if (ingredients.size() != 2) {
                throw new IllegalStateException("CompressorRecipe must have exactly 2 ingredients");
            }

            CountedIngredient inputA = CountedIngredient.fromJson(ingredients.get(0).getAsJsonObject());
            CountedIngredient inputB = CountedIngredient.fromJson(ingredients.get(1).getAsJsonObject());

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);

            return new CompressorRecipe(inputA, inputB, output, id, maxProgress);
        }

        @Override
        public @Nullable CompressorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedIngredient inputA = CountedIngredient.fromNetwork(buf);
            CountedIngredient inputB = CountedIngredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            int maxProgress = buf.readVarInt();
            return new CompressorRecipe(inputA, inputB, output, id, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompressorRecipe recipe) {
            recipe.inputA.toNetwork(buf);
            recipe.inputB.toNetwork(buf);
            buf.writeItem(recipe.output);
            buf.writeVarInt(recipe.maxProgress);
        }
    }
}
