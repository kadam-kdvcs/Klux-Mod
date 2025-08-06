package org.kdvcs.klux.worldgen.biome.surface;

import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.worldgen.biome.ModBiomes;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource RED_TERRACOTTA = makeStateRule(Blocks.RED_TERRACOTTA);
    private static final SurfaceRules.RuleSource IRON_SAND = makeStateRule(ModBlocks.IRON_SAND.get());

    public static SurfaceRules.RuleSource makeRules() {
        return SurfaceRules.sequence(

                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(ModBiomes.CELESTE),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, GRASS_BLOCK),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT)
                        )
                )

        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
