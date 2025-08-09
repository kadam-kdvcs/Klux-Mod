package org.kdvcs.klux.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;

import java.util.List;

import static org.kdvcs.klux.worldgen.ModConfiguredFeatures.PINE_KEY;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> EARTH_CRYSTAL_ORE_PLACED_KEY = registerKey("earth_crystal_ore_placed");
    public static final ResourceKey<PlacedFeature> FIRE_QUARTZ_ORE_PLACED_KEY = registerKey("fire_quartz_ore_placed");
    public static final ResourceKey<PlacedFeature> ENDERGON_CRYSTAL_ORE_PLACED_KEY = registerKey("endergon_crystal_ore_placed");
    public static final ResourceKey<PlacedFeature> BONES_ORE_PLACED_KEY = registerKey("bones_ore_placed");

    public static final ResourceKey<PlacedFeature> PINE_PLACED_KEY = registerKey("pine_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, EARTH_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.EARTH_CRYSTAL_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, FIRE_QUARTZ_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.FIRE_QUARTZ_ORE_KEY),
                ModOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(40))));

        register(context, ENDERGON_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ENDERGON_CRYSTAL_ORE_KEY),
                ModOrePlacement.commonOrePlacement(6,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, BONES_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BONES_ORE_KEY),
                ModOrePlacement.commonOrePlacement(10,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80))));

        register(context, PINE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PINE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ModBlocks.PINE_SAPLING.get()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Klux.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}
