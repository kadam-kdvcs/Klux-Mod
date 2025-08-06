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

public class FluidAssemblerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    public CountedIngredient ingredient;
    private final FluidStack fluidStack;
    private final int maxProgress;

    public FluidAssemblerRecipe(ResourceLocation id, ItemStack output,
                                CountedIngredient ingredient, FluidStack fluidStack, int maxProgress) {
        this.id = id;
        this.output = output;
        this.ingredient = ingredient;
        this.fluidStack = fluidStack;
        this.maxProgress = maxProgress;
    }

    public boolean matches(SimpleContainer pContainer, FluidStack machineFluid, Level pLevel) {
        if (pLevel.isClientSide()) return false;

        ItemStack item = pContainer.getItem(1);
        boolean testResult = ingredient.ingredient.test(item);
        boolean countSufficient = item.getCount() >= ingredient.count;
        boolean fluidMatch = fluidStack.isFluidEqual(machineFluid) &&
                machineFluid.getAmount() >= fluidStack.getAmount();

        return testResult && countSufficient && fluidMatch;
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
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
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

    public static class Type implements RecipeType<FluidAssemblerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "fluid_assembler";
    }


    public static class Serializer implements RecipeSerializer<FluidAssemblerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Klux.MODID, "fluid_assembler");

        @Override
        public FluidAssemblerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            CountedIngredient ingredient = CountedIngredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            FluidStack fluid = FluidJSONUtil.readFluid(json.getAsJsonObject("fluid"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);

            return new FluidAssemblerRecipe(recipeId, output, ingredient, fluid, maxProgress);
        }

        @Override
        public @Nullable FluidAssemblerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            CountedIngredient ingredient = CountedIngredient.fromNetwork(buf);
            FluidStack fluid = buf.readFluidStack();
            ItemStack output = buf.readItem();
            int maxProgress = buf.readInt();

            return new FluidAssemblerRecipe(id, output, ingredient, fluid, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidAssemblerRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidStack);
            buf.writeItemStack(recipe.getResultItem(null), false);
            buf.writeInt(recipe.getMaxProgress());
        }
    }
}