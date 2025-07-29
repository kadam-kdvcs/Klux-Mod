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
import org.kdvcs.klux.recipe.CompressorRecipe;
import org.kdvcs.klux.recipe.DehydratorRecipe;
import org.kdvcs.klux.recipe.ExtractorRecipe;
import org.kdvcs.klux.recipe.SeedMakerRecipe;
import org.kdvcs.klux.screen.CompressorScreen;
import org.kdvcs.klux.screen.DehydratorScreen;
import org.kdvcs.klux.screen.ExtractorScreen;
import org.kdvcs.klux.screen.SeedMakerScreen;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIKluxPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Klux.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new CompressorCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new SeedMakerCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new DehydratorCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new DehydratorFuelCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new ExtractorCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CompressorRecipe> compressorRecipes = recipeManager.getAllRecipesFor(CompressorRecipe.Type.INSTANCE);
        List<SeedMakerRecipe> seedMakerRecipes = recipeManager.getAllRecipesFor(SeedMakerRecipe.Type.INSTANCE);
        List<DehydratorRecipe> dehydratorRecipes = recipeManager.getAllRecipesFor(DehydratorRecipe.Type.INSTANCE);
        List<ExtractorRecipe> extractorRecipes = recipeManager.getAllRecipesFor(ExtractorRecipe.Type.INSTANCE);

        //  HERE'S LIST OF ADDED FUELS
        List<FuelRecipe> fuelRecipes = new ArrayList<>();
        fuelRecipes.add(new FuelRecipe(new ItemStack(Items.COAL), 400));

        registration.addRecipes(CompressorCategory.COMPRESSOR_TYPE, compressorRecipes);
        registration.addRecipes(SeedMakerCategory.SEED_MAKER_TYPE, seedMakerRecipes);
        registration.addRecipes(DehydratorCategory.DEHYDRATOR_TYPE, dehydratorRecipes);
        registration.addRecipes(DehydratorFuelCategory.TYPE, fuelRecipes);
        registration.addRecipes(ExtractorCategory.EXTRACTOR_TYPE, extractorRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 67,36,34,13,
                CompressorCategory.COMPRESSOR_TYPE);

        registration.addRecipeClickArea(SeedMakerScreen.class, 77,35,24,17,
                SeedMakerCategory.SEED_MAKER_TYPE);

        registration.addRecipeClickArea(DehydratorScreen.class, 80,36,22,14,
                DehydratorCategory.DEHYDRATOR_TYPE);

        registration.addRecipeClickArea(DehydratorScreen.class, 57,37,16,16,
                DehydratorFuelCategory.TYPE);

        registration.addRecipeClickArea(ExtractorScreen.class, 73,39,22,12,
                ExtractorCategory.EXTRACTOR_TYPE);
    }
}
