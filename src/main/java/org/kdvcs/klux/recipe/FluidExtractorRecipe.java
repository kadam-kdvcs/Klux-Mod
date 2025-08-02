package org.kdvcs.klux.recipe;

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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.util.FluidJSONUtil;

public class FluidExtractorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public CountedIngredient ingredient;
    private final FluidStack fluidStack;
    private final int maxProgress;

    public FluidExtractorRecipe(ResourceLocation id,
                                CountedIngredient ingredient,
                                FluidStack fluidStack,
                                int maxProgress) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluidStack = fluidStack;
        this.maxProgress = maxProgress;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;
        ItemStack item = container.getItem(0);
        return ingredient.ingredient.test(item) && item.getCount() >= ingredient.count;
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient.ingredient);
        return list;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY; // 不产生物品输出
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

    public int getMaxProgress() {
        return maxProgress;
    }

    public static class Type implements RecipeType<FluidExtractorRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "fluid_extractor";
    }

    public static class Serializer implements RecipeSerializer<FluidExtractorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Klux.MODID, "fluid_extractor");

        @Override
        public FluidExtractorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            CountedIngredient ingredient = CountedIngredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            FluidStack fluid = FluidJSONUtil.readFluid(json.getAsJsonObject("fluid"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);
            return new FluidExtractorRecipe(recipeId, ingredient, fluid, maxProgress);
        }

        @Override
        public @Nullable FluidExtractorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedIngredient ingredient = CountedIngredient.fromNetwork(buf);
            FluidStack fluid = buf.readFluidStack();
            int maxProgress = buf.readInt();
            return new FluidExtractorRecipe(id, ingredient, fluid, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidExtractorRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidStack);
            buf.writeInt(recipe.getMaxProgress());
        }
    }
}
