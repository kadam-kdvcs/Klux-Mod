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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.util.FluidJSONUtil;
import org.kdvcs.klux.util.MeshUtil;

import java.util.ArrayList;
import java.util.List;

public class LiquidFilterRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    public final List<WeightedOutput> outputs;
    public final CountedIngredient ingredient;
    public final Ingredient mesh;
    private final FluidStack fluidStack;
    private final int maxProgress;
    private final int requiredMeshLevel;

    public LiquidFilterRecipe(ResourceLocation id, List<WeightedOutput> outputs,
                              CountedIngredient ingredient, Ingredient mesh,
                              FluidStack fluidStack, int maxProgress, int requiredMeshLevel) {
        this.id = id;
        this.outputs = outputs;
        this.ingredient = ingredient;
        this.mesh = mesh;
        this.fluidStack = fluidStack;
        this.maxProgress = maxProgress;
        this.requiredMeshLevel = requiredMeshLevel;
    }

    //ORIGINAL METHOD
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return matches(container, level, FluidStack.EMPTY);
    }

    public boolean matches(SimpleContainer container, Level level, FluidStack fluidInTank) {
        if (level.isClientSide()) return false;

        ItemStack inputItem = container.getItem(1);
        ItemStack meshItem = container.getItem(2);

        if (!ingredient.ingredient.test(inputItem) || inputItem.getCount() < ingredient.count)
            return false;

        if (!mesh.isEmpty() && !MeshUtil.canMeshMatchRecipe(meshItem, mesh)) {
            return false;
        }

        if (fluidStack.isEmpty()) {
            return true;
        } else {
            return !fluidInTank.isEmpty() &&
                    fluidInTank.getFluid().isSame(fluidStack.getFluid()) &&
                    fluidInTank.getAmount() >= fluidStack.getAmount();
        }
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public List<WeightedOutput> getResults() {
        return outputs;
    }

    public Ingredient getMesh() {
        return mesh;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        if (!mesh.isEmpty()) {
            list.add(mesh);
        }
        list.add(ingredient.ingredient);
        return list;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).stack.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).stack.copy();
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

    public static class Type implements RecipeType<LiquidFilterRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "liquid_filter";
    }

    public static class Serializer implements RecipeSerializer<LiquidFilterRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "liquid_filter");

        @Override
        public LiquidFilterRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            CountedIngredient ingredient = CountedIngredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            Ingredient mesh = json.has("mesh") ? Ingredient.fromJson(json.get("mesh")) : Ingredient.EMPTY;
            FluidStack fluid = FluidJSONUtil.readFluid(json.getAsJsonObject("fluid"));

            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);
            int requiredMeshLevel = GsonHelper.getAsInt(json, "meshLevel", 1);

            List<WeightedOutput> outputs = new ArrayList<>();
            JsonArray outputArray = GsonHelper.getAsJsonArray(json, "outputs");
            for (int i = 0; i < outputArray.size(); i++) {
                JsonObject obj = outputArray.get(i).getAsJsonObject();
                outputs.add(WeightedOutput.fromJson(obj));
            }

            return new LiquidFilterRecipe(recipeId, outputs, ingredient, mesh, fluid, maxProgress, requiredMeshLevel);
        }

        @Override
        public @Nullable LiquidFilterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CountedIngredient ingredient = CountedIngredient.fromNetwork(buf);

            boolean hasMesh = buf.readBoolean();
            Ingredient mesh = hasMesh ? Ingredient.fromNetwork(buf) : Ingredient.EMPTY;

            FluidStack fluid = buf.readFluidStack();
            int maxProgress = buf.readInt();
            int requiredMeshLevel = buf.readInt();

            int outputCount = buf.readInt();
            List<WeightedOutput> outputs = new ArrayList<>();
            for (int i = 0; i < outputCount; i++) {
                outputs.add(WeightedOutput.fromNetwork(buf));
            }

            return new LiquidFilterRecipe(id, outputs, ingredient, mesh, fluid, maxProgress, requiredMeshLevel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, LiquidFilterRecipe recipe) {
            recipe.ingredient.toNetwork(buf);

            buf.writeBoolean(!recipe.mesh.isEmpty());
            if (!recipe.mesh.isEmpty()) {
                recipe.mesh.toNetwork(buf);
            }

            buf.writeFluidStack(recipe.fluidStack);
            buf.writeInt(recipe.maxProgress);
            buf.writeInt(recipe.requiredMeshLevel);

            buf.writeInt(recipe.outputs.size());
            for (WeightedOutput output : recipe.outputs) {
                output.toNetwork(buf);
            }
        }

    }
}
