package org.kdvcs.klux.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.recipe.*;
import org.kdvcs.klux.screen.*;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIKluxPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return
                new ResourceLocation(Klux.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new CompressorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DehydratorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DehydratorFuelCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new ExtractorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FluidAssemblerCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new FluidExtractorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CompressorRecipe> compressorRecipes = recipeManager.getAllRecipesFor(CompressorRecipe.Type.INSTANCE);

        List<DehydratorRecipe> dehydratorRecipes = recipeManager.getAllRecipesFor(DehydratorRecipe.Type.INSTANCE);
        List<ExtractorRecipe> extractorRecipes = recipeManager.getAllRecipesFor(ExtractorRecipe.Type.INSTANCE);

        List<FluidAssemblerRecipe> fluidAssemblerRecipes = recipeManager.getAllRecipesFor(FluidAssemblerRecipe.Type.INSTANCE);

        List<FluidExtractorRecipe> fluidExtractorRecipes = recipeManager.getAllRecipesFor(FluidExtractorRecipe.Type.INSTANCE);

        //  HERE'S LIST OF ADDED FUELS
        List<FuelRecipe> fuelRecipes = new ArrayList<>();
        fuelRecipes.add(new FuelRecipe(new ItemStack(Items.COAL), 400));

        registration.addRecipes(CompressorCategory.COMPRESSOR_TYPE, compressorRecipes);

        registration.addRecipes(DehydratorCategory.DEHYDRATOR_TYPE, dehydratorRecipes);
        registration.addRecipes(DehydratorFuelCategory.TYPE, fuelRecipes);
        registration.addRecipes(ExtractorCategory.EXTRACTOR_TYPE, extractorRecipes);
        registration.addRecipes(FluidAssemblerCategory.FLUID_ASSEMBLER_TYPE, fluidAssemblerRecipes);

        registration.addRecipes(FluidExtractorCategory.FLUID_EXTRACTOR_TYPE, fluidExtractorRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 67,36,34,13,
                CompressorCategory.COMPRESSOR_TYPE);

        registration.addRecipeClickArea(DehydratorScreen.class, 80,36,22,14,
                DehydratorCategory.DEHYDRATOR_TYPE);

        registration.addRecipeClickArea(DehydratorScreen.class, 57,37,16,16,
                DehydratorFuelCategory.TYPE);

        registration.addRecipeClickArea(ExtractorScreen.class, 73,39,22,12,
                ExtractorCategory.EXTRACTOR_TYPE);

        registration.addRecipeClickArea(FluidAssemblerScreen.class, 115,37,22,20,
                FluidAssemblerCategory.FLUID_ASSEMBLER_TYPE);

        registration.addRecipeClickArea(FluidExtractorScreen.class, 52,35,22,20,
                FluidExtractorCategory.FLUID_EXTRACTOR_TYPE);
    }
}
