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

public class ExtractorRecipe implements Recipe<SimpleContainer> {

    private final CountedIngredient input;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int maxProgress;

    public ExtractorRecipe(CountedIngredient input, ItemStack output, ResourceLocation id, int maxProgress) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.maxProgress = maxProgress;
    }

    public CountedIngredient getInput() {
        return input;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;
        ItemStack stack = container.getItem(0);
        return input.ingredient.test(stack) && stack.getCount() >= input.count;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, input.ingredient);
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

    public static class Type implements RecipeType<ExtractorRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "extractor";
    }

    public static class Serializer implements RecipeSerializer<ExtractorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "extractor");

        @Override
        public ExtractorRecipe fromJson(ResourceLocation id, JsonObject json) {
            // 读取 ingredients 数组，取第一个元素
            JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
            CountedIngredient input = CountedIngredient.fromJson(ingredientsArray.get(0).getAsJsonObject());

            // 读取输出
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            // 读取进度
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 48);

            return new ExtractorRecipe(input, output, id, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ExtractorRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
            buf.writeVarInt(recipe.maxProgress);
        }

        @Override
        public @Nullable ExtractorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedIngredient input = CountedIngredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            int maxProgress = buf.readVarInt();
            return new ExtractorRecipe(input, output, id, maxProgress);
        }
    }

}
