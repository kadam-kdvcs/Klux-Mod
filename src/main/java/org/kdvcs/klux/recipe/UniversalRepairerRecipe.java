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

public class UniversalRepairerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final FluidStack requiredFluid;
    private final int maxProgress;
    private final int repairAmount;

    public UniversalRepairerRecipe(ResourceLocation id, FluidStack requiredFluid, int maxProgress, int repairAmount) {
        this.id = id;
        this.requiredFluid = requiredFluid;
        this.maxProgress = maxProgress;
        this.repairAmount = repairAmount;
    }

    public UniversalRepairerRecipe(ResourceLocation id, FluidStack requiredFluid, int maxProgress) {
        this(id, requiredFluid, maxProgress, 1);
    }

    public boolean matches(SimpleContainer container, FluidStack machineFluid, Level level) {
        if (level.isClientSide()) return false;

        ItemStack item = container.getItem(0);
        if (item.isEmpty()) return false;
        if (!item.isDamageableItem()) return false;
        if (item.getDamageValue() == 0) return false;

        boolean fluidMatch = requiredFluid.isFluidEqual(machineFluid)
                && machineFluid.getAmount() >= requiredFluid.getAmount();

        return fluidMatch;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        ItemStack item = container.getItem(0);
        ItemStack copy = item.copy();
        copy.setDamageValue(0);
        return copy;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
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

    public FluidStack getRequiredFluid() {
        return requiredFluid;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getRepairAmount() {
        return repairAmount;
    }

    public static class Type implements RecipeType<UniversalRepairerRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "universal_repairer";
    }

    public static class Serializer implements RecipeSerializer<UniversalRepairerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "universal_repairer");

        @Override
        public UniversalRepairerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidStack fluid = FluidJSONUtil.readFluid(GsonHelper.getAsJsonObject(json, "fluid"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);
            int repairAmount = GsonHelper.getAsInt(json, "repairAmount", 1);
            return new UniversalRepairerRecipe(recipeId, fluid, maxProgress, repairAmount);
        }

        @Nullable
        @Override
        public UniversalRepairerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            FluidStack fluid = buf.readFluidStack();
            int maxProgress = buf.readInt();
            int repairAmount = buf.readInt();
            return new UniversalRepairerRecipe(id, fluid, maxProgress, repairAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UniversalRepairerRecipe recipe) {
            buf.writeFluidStack(recipe.getRequiredFluid());
            buf.writeInt(recipe.getMaxProgress());
            buf.writeInt(recipe.getRepairAmount());
        }
    }
}
