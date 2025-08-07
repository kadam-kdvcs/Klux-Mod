package org.kdvcs.klux.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.recipe.*;
import org.kdvcs.klux.screen.*;
import org.kdvcs.klux.util.ModBrewingRecipe;
import org.kdvcs.klux.util.ModBrewingRecipeRegistry;

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
        registration.addRecipeCategories(new FluxSynthesizerCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new LiquidReactorCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new LiquidFilterCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new UniversalRepairerCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new ModBrewingCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CompressorRecipe> compressorRecipes = recipeManager.getAllRecipesFor(CompressorRecipe.Type.INSTANCE);

        List<DehydratorRecipe> dehydratorRecipes = recipeManager.getAllRecipesFor(DehydratorRecipe.Type.INSTANCE);
        List<ExtractorRecipe> extractorRecipes = recipeManager.getAllRecipesFor(ExtractorRecipe.Type.INSTANCE);

        List<FluidAssemblerRecipe> fluidAssemblerRecipes = recipeManager.getAllRecipesFor(FluidAssemblerRecipe.Type.INSTANCE);

        List<FluidExtractorRecipe> fluidExtractorRecipes = recipeManager.getAllRecipesFor(FluidExtractorRecipe.Type.INSTANCE);
        List<FluxSynthesizerRecipe> fluxSynthesizerRecipes = recipeManager.getAllRecipesFor(FluxSynthesizerRecipe.Type.INSTANCE);

        List<LiquidReactorRecipe> liquidReactorRecipes = recipeManager.getAllRecipesFor(LiquidReactorRecipe.Type.INSTANCE);
        List<LiquidFilterRecipe> liquidFilterRecipes = recipeManager.getAllRecipesFor(LiquidFilterRecipe.Type.INSTANCE);

        List<UniversalRepairerRecipe> universalRepairerRecipes = recipeManager.getAllRecipesFor(UniversalRepairerRecipe.Type.INSTANCE);
        List<ModBrewingRecipe> modBrewingRecipes = ModBrewingRecipeRegistry.getAll();

        //  HERE'S LIST OF ADDED FUELS
        List<FuelRecipe> fuelRecipes = new ArrayList<>();
        fuelRecipes.add(new FuelRecipe(new ItemStack(Items.COAL), 400));

        registration.addRecipes(CompressorCategory.COMPRESSOR_TYPE, compressorRecipes);

        registration.addRecipes(DehydratorCategory.DEHYDRATOR_TYPE, dehydratorRecipes);
        registration.addRecipes(DehydratorFuelCategory.TYPE, fuelRecipes);
        registration.addRecipes(ExtractorCategory.EXTRACTOR_TYPE, extractorRecipes);
        registration.addRecipes(FluidAssemblerCategory.FLUID_ASSEMBLER_TYPE, fluidAssemblerRecipes);

        registration.addRecipes(FluidExtractorCategory.FLUID_EXTRACTOR_TYPE, fluidExtractorRecipes);
        registration.addRecipes(FluxSynthesizerCategory.FLUX_SYNTHESIZER_TYPE, fluxSynthesizerRecipes);

        registration.addRecipes(LiquidReactorCategory.LIQUID_REACTOR_TYPE, liquidReactorRecipes);
        registration.addRecipes(LiquidFilterCategory.LIQUID_FILTER_TYPE, liquidFilterRecipes);

        registration.addRecipes(UniversalRepairerCategory.UNIVERSAL_REPAIRER_TYPE, universalRepairerRecipes);

        registration.addRecipes(ModBrewingCategory.TYPE, modBrewingRecipes);
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

        registration.addRecipeClickArea(FluxSynthesizerScreen.class, 58,38,34,14,
                FluxSynthesizerCategory.FLUX_SYNTHESIZER_TYPE);

        registration.addRecipeClickArea(LiquidReactorScreen.class, 41,54,9,17,
                LiquidReactorCategory.LIQUID_REACTOR_TYPE);
        registration.addRecipeClickArea(LiquidReactorScreen.class, 126,54,9,17,
                LiquidReactorCategory.LIQUID_REACTOR_TYPE);

        registration.addRecipeClickArea(LiquidFilterScreen.class, 99,38,27,17,
                LiquidFilterCategory.LIQUID_FILTER_TYPE);
        registration.addRecipeClickArea(UniversalRepairerScreen.class, 149,19,16,16,
                UniversalRepairerCategory.UNIVERSAL_REPAIRER_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR.get()), CompressorCategory.COMPRESSOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DEHYDRATOR.get()), DehydratorCategory.DEHYDRATOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EXTRACTOR.get()), ExtractorCategory.EXTRACTOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_ASSEMBLER.get()), FluidAssemblerCategory.FLUID_ASSEMBLER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_EXTRACTOR.get()), FluidExtractorCategory.FLUID_EXTRACTOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUX_SYNTHESIZER.get()), FluxSynthesizerCategory.FLUX_SYNTHESIZER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LIQUID_REACTOR.get()), LiquidReactorCategory.LIQUID_REACTOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DEHYDRATOR.get()), DehydratorFuelCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LIQUID_FILTER.get()), LiquidFilterCategory.LIQUID_FILTER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.UNIVERSAL_REPAIRER.get()), UniversalRepairerCategory.UNIVERSAL_REPAIRER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Items.BREWING_STAND), ModBrewingCategory.TYPE);
    }

}
