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

public class FluxSynthesizerRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    public CountedIngredient ingredient1;
    public CountedIngredient ingredient2;
    private final FluidStack fluidInput;
    private final FluidStack fluidOutput;
    private final int maxProgress;

    public FluxSynthesizerRecipe(ResourceLocation id, ItemStack output, CountedIngredient ingredient1, CountedIngredient ingredient2,
                                 FluidStack fluidInput, FluidStack fluidOutput, int maxProgress) {
        this.id = id;
        this.output = output;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.fluidInput = fluidInput;
        this.fluidOutput = fluidOutput;
        this.maxProgress = maxProgress;
    }

    public boolean matches(SimpleContainer container, Level level, FluidStack machineFluidInput) {
        if (level.isClientSide()) return false;

        ItemStack item1 = container.getItem(1);
        ItemStack item2 = container.getItem(2);

        boolean itemsMatch = ingredient1.ingredient.test(item1) && item1.getCount() >= ingredient1.count
                && ingredient2.ingredient.test(item2) && item2.getCount() >= ingredient2.count;

        boolean fluidMatch = machineFluidInput.isFluidEqual(fluidInput) && machineFluidInput.getAmount() >= fluidInput.getAmount();

        return itemsMatch && fluidMatch;
    }

    public FluidStack getFluid() {
        return fluidInput;
    }

    public FluidStack getResultFluid() {
        return fluidOutput;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient1.ingredient);
        list.add(ingredient2.ingredient);
        return list;
    }

    //I NO LONGER USE THIS
    @Override
    public boolean matches(SimpleContainer p_44002_, Level p_44003_) {
        return false;
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

    public int getMaxProgress() {
        return maxProgress;
    }

    public static class Type implements RecipeType<FluxSynthesizerRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "flux_synthesizer";
    }

    public static class Serializer implements RecipeSerializer<FluxSynthesizerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "flux_synthesizer");

        @Override
        public FluxSynthesizerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            CountedIngredient ingredient1 = CountedIngredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient1"));
            CountedIngredient ingredient2 = CountedIngredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient2"));
            FluidStack fluidInput = FluidJSONUtil.readFluid(json.getAsJsonObject("fluid_input"));
            FluidStack fluidOutput = FluidJSONUtil.readFluid(json.getAsJsonObject("fluid_output"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);

            return new FluxSynthesizerRecipe(recipeId, output, ingredient1, ingredient2, fluidInput, fluidOutput, maxProgress);
        }

        @Nullable
        @Override
        public FluxSynthesizerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedIngredient ingredient1 = CountedIngredient.fromNetwork(buf);
            CountedIngredient ingredient2 = CountedIngredient.fromNetwork(buf);
            FluidStack fluidInput = buf.readFluidStack();
            FluidStack fluidOutput = buf.readFluidStack();
            ItemStack output = buf.readItem();
            int maxProgress = buf.readInt();

            return new FluxSynthesizerRecipe(id, output, ingredient1, ingredient2, fluidInput, fluidOutput, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluxSynthesizerRecipe recipe) {
            recipe.ingredient1.toNetwork(buf);
            recipe.ingredient2.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidInput);
            buf.writeFluidStack(recipe.fluidOutput);
            buf.writeItemStack(recipe.getResultItem(null), false);
            buf.writeInt(recipe.getMaxProgress());
        }
    }
}
