package org.kdvcs.klux.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.util.FluidJSONUtil;

public class GemDuplicatorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final FluidStack fluidStack;
    private final int maxProgress;
    private static final TagKey<?> GEM_TAG = net.minecraft.tags.ItemTags.create(new ResourceLocation("forge", "gems"));

    public GemDuplicatorRecipe(ResourceLocation id, FluidStack fluidStack, int maxProgress) {
        this.id = id;
        this.fluidStack = fluidStack;
        this.maxProgress = maxProgress;
    }

    public boolean matches(SimpleContainer container, FluidStack machineFluid, Level level) {
        if (level.isClientSide()) return false;

        ItemStack input = container.getItem(1);
        boolean isGem = input.is(ItemTags.create(new ResourceLocation("forge", "gems")));
        boolean hasEnoughCount = input.getCount() >= 1;
        boolean fluidMatch = fluidStack.isFluidEqual(machineFluid) && machineFluid.getAmount() >= fluidStack.getAmount();

        return isGem && hasEnoughCount && fluidMatch;
    }

    private FluidStack getMachineFluid() {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level p_44003_) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return container.getItem(1).copy();
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

    public FluidStack getFluid() {
        return fluidStack;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public static class Type implements RecipeType<GemDuplicatorRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "gem_duplicator";
    }

    public static class Serializer implements RecipeSerializer<GemDuplicatorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Klux.MODID, "gem_duplicator");

        @Override
        public GemDuplicatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidStack fluid = FluidJSONUtil.readFluid(GsonHelper.getAsJsonObject(json, "fluid"));
            int maxProgress = GsonHelper.getAsInt(json, "maxProgress", 78);
            return new GemDuplicatorRecipe(recipeId, fluid, maxProgress);
        }

        @Nullable
        @Override
        public GemDuplicatorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            FluidStack fluid = buf.readFluidStack();
            int maxProgress = buf.readInt();
            return new GemDuplicatorRecipe(id, fluid, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GemDuplicatorRecipe recipe) {
            buf.writeFluidStack(recipe.fluidStack);
            buf.writeInt(recipe.maxProgress);
        }
    }
}
