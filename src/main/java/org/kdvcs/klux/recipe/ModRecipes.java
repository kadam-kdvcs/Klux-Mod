package org.kdvcs.klux.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;

public class ModRecipes {

    // 注册 Serializer
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Klux.MODID);

    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIALIZER =
            SERIALIZERS.register("compressor", () -> CompressorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<DehydratorRecipe>> DEHYDRATOR_SERIALIZER =
            SERIALIZERS.register("dehydrator", () -> DehydratorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ExtractorRecipe>> EXTRACTOR_SERIALIZER =
            SERIALIZERS.register("extractor", () -> ExtractorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FluidAssemblerRecipe>> FLUID_ASSEMBLER_SERIALIZER =
            SERIALIZERS.register("fluid_assembler", () -> FluidAssemblerRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FluidExtractorRecipe>> FLUID_EXTRACTOR_SERIALIZER =
            SERIALIZERS.register("fluid_extractor", () -> FluidExtractorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FluxSynthesizerRecipe>> FLUX_SYNTHESIZER_SERIALIZER =
            SERIALIZERS.register("flux_synthesizer", () -> FluxSynthesizerRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<LiquidReactorRecipe>> LIQUID_REACTOR_SERIALIZER =
            SERIALIZERS.register("liquid_reactor", () -> LiquidReactorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<LiquidFilterRecipe>> LIQUID_FILTER_SERIALIZER =
            SERIALIZERS.register("liquid_filter", () -> LiquidFilterRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ProportionalArmorRecipe>> PROPORTIONAL_ARMOR_SERIALIZER =
            SERIALIZERS.register("proportional_armor", () -> ProportionalArmorRecipe.Serializer.INSTANCE);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Klux.MODID);

    public static final RegistryObject<RecipeType<ProportionalArmorRecipe>> PROPORTIONAL_ARMOR_TYPE =
            RECIPE_TYPES.register("proportional_armor", () -> new RecipeType<>() {
                public String toString() {
                    return Klux.MODID + ":proportional_armor";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
