package org.kdvcs.klux.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Klux.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Blocks.EARTH_CRYSTAL_ORES)
                .add(ModBlocks.EARTH_CRYSTAL_ORE.get())
                .add(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get());

        tag(ModTags.Blocks.FIRE_QUARTZ_ORES)
                .add(ModBlocks.FIRE_QUARTZ_ORE.get());

        tag(ModTags.Blocks.ENDERGON_CRYSTAL_ORES)
                .add(ModBlocks.ENDERGON_CRYSTAL_ORE.get());

        tag(Tags.Blocks.ORES)
                .addTag(ModTags.Blocks.EARTH_CRYSTAL_ORES)
                .addTag(ModTags.Blocks.FIRE_QUARTZ_ORES)
                .addTag(ModTags.Blocks.ENDERGON_CRYSTAL_ORES);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.EARTH_CRYSTAL_ORE.get(),
                        ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get(),
                        ModBlocks.EARTH_CRYSTAL_BLOCK.get(),
                        ModBlocks.SOUND_BLOCK.get(),
                        ModBlocks.FIRE_QUARTZ_ORE.get(),
                        ModBlocks.ENDERGON_CRYSTAL_ORE.get(),
                        ModBlocks.COMPRESSOR.get(),
                        ModBlocks.DEHYDRATOR.get(),
                        ModBlocks.EXTRACTOR.get(),
                        ModBlocks.FLUID_ASSEMBLER.get(),
                        ModBlocks.FLUID_EXTRACTOR.get(),
                        ModBlocks.MULTIPHASE_FLUID_TANK.get(),
                        ModBlocks.FLUX_SYNTHESIZER.get(),
                        ModBlocks.FIRE_QUARTZ_FRAME.get(),
                        ModBlocks.LIQUID_REACTOR.get(),
                        ModBlocks.ICE_BRICK.get(),
                        ModBlocks.SANDSTONE_CLEAN.get(),
                        ModBlocks.SANDSTONE_CARVED.get(),
                        ModBlocks.DRIED_BRICK_SMOOTH.get(),
                        ModBlocks.DRIED_BRICK.get(),
                        ModBlocks.LIQUID_FILTER.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.IRON_SAND.get())
                .add(ModBlocks.SNOW_BRICK.get())
                .add(ModBlocks.WET_BRICK.get());


        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.DRIED_BRICK_SMOOTH.get(),
                        ModBlocks.DRIED_BRICK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.EARTH_CRYSTAL_ORE.get(),
                        ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get(),
                        ModBlocks.COMPRESSOR.get(),
                        ModBlocks.DEHYDRATOR.get(),
                        ModBlocks.EXTRACTOR.get(),
                        ModBlocks.FLUID_ASSEMBLER.get(),
                        ModBlocks.FLUID_EXTRACTOR.get(),
                        ModBlocks.MULTIPHASE_FLUID_TANK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.FIRE_QUARTZ_ORE.get(),

                        ModBlocks.FLUX_SYNTHESIZER.get(),
                        ModBlocks.FIRE_QUARTZ_FRAME.get(),
                        ModBlocks.LIQUID_REACTOR.get(),
                        ModBlocks.LIQUID_FILTER.get());

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(ModBlocks.ENDERGON_CRYSTAL_ORE.get());

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.EARTH_CRYSTAL_FENCE.get());
        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.EARTH_CRYSTAL_FENCE_GATE.get());
        this.tag(BlockTags.WALLS)
                .add(ModBlocks.EARTH_CRYSTAL_WALL.get(),
                        ModBlocks.HAY_WALL.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.PINE_LOG.get())
                .add(ModBlocks.PINE_WOOD.get())
                .add(ModBlocks.STRIPPED_PINE_LOG.get())
                .add(ModBlocks.STRIPPED_PINE_LOG.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.PINE_PLANKS.get());
    }

}
