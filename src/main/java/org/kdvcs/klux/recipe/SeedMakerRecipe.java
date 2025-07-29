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

public class SeedMakerRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int maxProgress;

    public SeedMakerRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, int maxProgress) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;

        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level p_44003_) {
        if(p_44003_.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(p_44002_.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
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

    public static class Type implements RecipeType<SeedMakerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "seed_maker";
    }

    public static class Serializer implements RecipeSerializer<SeedMakerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "seed_maker");

        @Override
        public SeedMakerRecipe fromJson(ResourceLocation p_44103_, JsonObject p_44104_) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44104_,"output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(p_44104_,"ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1,Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            int maxProgress = GsonHelper.getAsInt(p_44104_, "maxProgress", 48);
            return new SeedMakerRecipe(inputs, output, p_44103_, maxProgress);
        }

        @Override
        public @Nullable SeedMakerRecipe fromNetwork(ResourceLocation p_44105_, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            int maxProgress = pBuffer.readInt();
            return new SeedMakerRecipe(inputs, output, p_44105_, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SeedMakerRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null),false);
            pBuffer.writeInt(pRecipe.maxProgress);

        }
    }
}
