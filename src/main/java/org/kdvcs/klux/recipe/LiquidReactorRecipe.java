package org.kdvcs.klux.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class LiquidReactorRecipe implements Recipe<LiquidReactorFluidInventory> {

    private final ResourceLocation id;

    private final Fluid fluidInput1;
    private final int fluidInput1Amount;

    private final Fluid fluidInput2;
    private final int fluidInput2Amount;

    private final FluidStack outputFluid;

    private final int maxProgress;

    public LiquidReactorRecipe(ResourceLocation id,
                               Fluid fluidInput1, int fluidInput1Amount,
                               Fluid fluidInput2, int fluidInput2Amount,
                               FluidStack outputFluid,
                               int maxProgress) {
        this.id = id;
        this.fluidInput1 = fluidInput1;
        this.fluidInput1Amount = fluidInput1Amount;
        this.fluidInput2 = fluidInput2;
        this.fluidInput2Amount = fluidInput2Amount;
        this.outputFluid = outputFluid;
        this.maxProgress = maxProgress;
    }

    /*
    @Override
    public boolean matches(LiquidReactorFluidInventory inv, Level level) {
        FluidStack in1 = inv.getInput1();
        FluidStack in2 = inv.getInput2();

        if (in1 == null || in2 == null) return false;

        return in1.getFluid().isSame(fluidInput1)
                && in2.getFluid().isSame(fluidInput2)
                && in1.getAmount() >= fluidInput1Amount
                && in2.getAmount() >= fluidInput2Amount;
    }

     */

    @Override
    public boolean matches(LiquidReactorFluidInventory inv, Level level) {
        FluidStack in1 = inv.getInput1();
        FluidStack in2 = inv.getInput2();

        if (in1 == null || in2 == null) return false;

        boolean matchDirect = in1.getFluid().isSame(fluidInput1) && in2.getFluid().isSame(fluidInput2)
                && in1.getAmount() >= fluidInput1Amount && in2.getAmount() >= fluidInput2Amount;

        boolean matchSwapped = in1.getFluid().isSame(fluidInput2) && in2.getFluid().isSame(fluidInput1)
                && in1.getAmount() >= fluidInput2Amount && in2.getAmount() >= fluidInput1Amount;

        return matchDirect || matchSwapped;
    }

    @Override
    public ItemStack assemble(LiquidReactorFluidInventory inv, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    public FluidStack getOutputFluid() {
        return outputFluid.copy();
    }

    public Fluid getFluidInput1() {
        return fluidInput1;
    }

    public Fluid getFluidInput2() {
        return fluidInput2;
    }

    public int getFluidInput1Amount() {
        return fluidInput1Amount;
    }

    public int getFluidInput2Amount() {
        return fluidInput2Amount;
    }

    public int getMaxProgress() {
        return maxProgress;
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

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    public static class Type implements RecipeType<LiquidReactorRecipe> {
        public static final Type INSTANCE = new Type();
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<LiquidReactorRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public LiquidReactorRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject fluid1Json = json.getAsJsonObject("fluid_input1");
            Fluid fluidInput1 = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(fluid1Json, "fluid")));
            int fluidInput1Amount = GsonHelper.getAsInt(fluid1Json, "amount");

            JsonObject fluid2Json = json.getAsJsonObject("fluid_input2");
            Fluid fluidInput2 = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(fluid2Json, "fluid")));
            int fluidInput2Amount = GsonHelper.getAsInt(fluid2Json, "amount");

            JsonObject outputFluidJson = json.getAsJsonObject("output_fluid");
            Fluid outputFluidType = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(outputFluidJson, "fluid")));
            int outputFluidAmount = GsonHelper.getAsInt(outputFluidJson, "amount");
            FluidStack outputFluid = new FluidStack(outputFluidType, outputFluidAmount);

            int maxProgress = GsonHelper.getAsInt(json, "max_progress", 78);

            return new LiquidReactorRecipe(id,
                    fluidInput1, fluidInput1Amount,
                    fluidInput2, fluidInput2Amount,
                    outputFluid,
                    maxProgress);
        }

        @Override
        public LiquidReactorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Fluid fluidInput1 = ForgeRegistries.FLUIDS.getValue(buf.readResourceLocation());
            int fluidInput1Amount = buf.readVarInt();

            Fluid fluidInput2 = ForgeRegistries.FLUIDS.getValue(buf.readResourceLocation());
            int fluidInput2Amount = buf.readVarInt();

            Fluid outputFluidType = ForgeRegistries.FLUIDS.getValue(buf.readResourceLocation());
            int outputFluidAmount = buf.readVarInt();

            int maxProgress = buf.readVarInt();

            return new LiquidReactorRecipe(id,
                    fluidInput1, fluidInput1Amount,
                    fluidInput2, fluidInput2Amount,
                    new FluidStack(outputFluidType, outputFluidAmount),
                    maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, LiquidReactorRecipe recipe) {
            buf.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(recipe.fluidInput1));
            buf.writeVarInt(recipe.fluidInput1Amount);

            buf.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(recipe.fluidInput2));
            buf.writeVarInt(recipe.fluidInput2Amount);

            buf.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(recipe.outputFluid.getFluid()));
            buf.writeVarInt(recipe.outputFluid.getAmount());

            buf.writeVarInt(recipe.maxProgress);
        }
    }
}
