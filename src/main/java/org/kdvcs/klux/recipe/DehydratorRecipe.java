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

public class DehydratorRecipe implements Recipe<SimpleContainer> {

    private final Ingredient inputIngredient;
    public final int ingredientCount;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int maxProgress;

    public DehydratorRecipe(Ingredient inputIngredient, int ingredientCount, ItemStack output, ResourceLocation id, int maxProgress) {
        this.inputIngredient = inputIngredient;
        this.ingredientCount = ingredientCount;
        this.output = output;
        this.id = id;
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        ItemStack stackInSlot = container.getItem(0);
        return inputIngredient.test(stackInSlot) && stackInSlot.getCount() >= ingredientCount;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(1, inputIngredient);
        return list;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
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

    public static class Type implements RecipeType<DehydratorRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "dehydrator";
    }

    public static class Serializer implements RecipeSerializer<DehydratorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "dehydrator");

        @Override
        public DehydratorRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("ingredient"));
            int count = GsonHelper.getAsInt(json, "ingredientCount", 1);
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);

            return new DehydratorRecipe(input, count, output, id, maxProgress);
        }

        @Override
        public @Nullable DehydratorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {

            Ingredient input = Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            ItemStack output = buffer.readItem();
            int maxProgress = buffer.readVarInt();

            return new DehydratorRecipe(input, count, output, id, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DehydratorRecipe recipe) {

            recipe.inputIngredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.ingredientCount);
            buffer.writeItem(recipe.output);
            buffer.writeVarInt(recipe.maxProgress);

        }
    }
}
