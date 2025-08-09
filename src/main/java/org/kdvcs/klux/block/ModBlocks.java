package org.kdvcs.klux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.custom.*;
import org.kdvcs.klux.block.entity.FluxSynthesizerBlockEntity;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.sound.ModSounds;
import org.kdvcs.klux.worldgen.tree.PineTreeGrower;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Klux.MODID);

    //YOU CAN'T GET THIS, ANYWAY.
    public static final RegistryObject<Block> BACKGROUND_BRICK = registerBlock("background_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1f).sound(SoundType.STONE).noCollission().noLootTable()));

    public static final RegistryObject<Block> HAY_BRICK = registerBlock("hay_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).strength(0.3f).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> EARTH_CRYSTAL_BLOCK = registerBlock("earth_crystal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> EARTH_CRYSTAL_FRAME = registerBlock("earth_crystal_frame",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.METAL)));

    public static final RegistryObject<Block> FIRE_QUARTZ_FRAME = registerBlock("fire_quartz_frame",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).sound(SoundType.METAL)));

    //IRON SAND
    public static final RegistryObject<Block> IRON_SAND = registerBlock("iron_sand",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundType.SAND).strength(0.6f)));

    //STAIRS
    public static final RegistryObject<Block> EARTH_CRYSTAL_STAIRS = registerBlock("earth_crystal_stairs",
            () -> new StairBlock(() -> ModBlocks.EARTH_CRYSTAL_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> HAY_STAIRS = registerBlock("hay_stairs",
            () -> new StairBlock(() -> ModBlocks.HAY_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).sound(SoundType.GRASS)));

    //SLABS
    public static final RegistryObject<Block> EARTH_CRYSTAL_SLAB = registerBlock("earth_crystal_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> HAY_SLAB = registerBlock("hay_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).sound(SoundType.GRASS)));

    //BUTTONS
    public static final RegistryObject<Block> EARTH_CRYSTAL_BUTTON = registerBlock("earth_crystal_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON).sound(SoundType.AMETHYST),
                    BlockSetType.IRON,10,true));

    //PRESSURE PLATES
    public static final RegistryObject<Block> EARTH_CRYSTAL_PRESSURE_PLATE = registerBlock("earth_crystal_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST),
                    BlockSetType.IRON));

    //FENCES
    public static final RegistryObject<Block> EARTH_CRYSTAL_FENCE = registerBlock("earth_crystal_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    //FENCE GATES
    public static final RegistryObject<Block> EARTH_CRYSTAL_FENCE_GATE = registerBlock("earth_crystal_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST),
                    SoundEvents.AMETHYST_BLOCK_PLACE, SoundEvents.AMETHYST_BLOCK_PLACE));

    //WALLS
    public static final RegistryObject<Block> EARTH_CRYSTAL_WALL = registerBlock("earth_crystal_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> HAY_WALL = registerBlock("hay_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).sound(SoundType.GRASS)));

    //DOOR
    public static final RegistryObject<Block> EARTH_CRYSTAL_DOOR = registerBlock("earth_crystal_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion(),
                    BlockSetType.IRON));

    //TRAPDOOR
    public static final RegistryObject<Block> EARTH_CRYSTAL_TRAPDOOR = registerBlock("earth_crystal_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion(),
                    BlockSetType.IRON));

    public static final RegistryObject<Block> SPRING_ONION_CROP = BLOCKS.register("spring_onion_crop",
            () -> new SpringOnionCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> ROTTEN_FRUIT_CROP = BLOCKS.register("rotten_fruit_crop",
            () -> new RottenFruitCropBlock(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion().noCollission()));

    public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop",
            () -> new RiceCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    //FLOWER
    public static final RegistryObject<Block> CACTUS_FRUIT = registerBlock("cactus_fruit",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_CACTUS_FRUIT = BLOCKS.register("potted_cactus_fruit",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.CACTUS_FRUIT,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).noOcclusion()));

    //ORE
    //EARTH CRYSTAL
    public static final RegistryObject<Block> EARTH_CRYSTAL_ORE = registerBlock("earth_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(1.6f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    public static final RegistryObject<Block> DEEPSLATE_EARTH_CRYSTAL_ORE = registerBlock("deepslate_earth_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(2.2f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    //FIRE QUARTZ
    public static final RegistryObject<Block> FIRE_QUARTZ_ORE = registerBlock("fire_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2.4f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    //ENDERGON CRYSTAL
    public static final RegistryObject<Block> ENDERGON_CRYSTAL_ORE = registerBlock("endergon_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    //BONES
    public static final RegistryObject<Block> BONES_ORE = registerBlock("bones_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(1.2f).requiresCorrectToolForDrops(), UniformInt.of(1, 3)));

    //SOUND BLOCK
    public static final RegistryObject<Block> SOUND_BLOCK = registerBlock("sound_block",
            () -> new SoundBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(ModSounds.SOUND_BLOCK_SOUNDS)));

    //COMPRESSOR BLOCK
    public static final RegistryObject<Block> COMPRESSOR = registerBlock("compressor",
            CompressorBlock::new);

    public static final RegistryObject<Block> DEHYDRATOR = registerBlock("dehydrator",
            DehydratorBlock::new);

    public static final RegistryObject<Block> EXTRACTOR = registerBlock("extractor",
            ExtractorBlock::new);

    public static final RegistryObject<Block> FLUID_ASSEMBLER = registerBlock("fluid_assembler",
            FluidAssemblerBlock::new);

    public static final RegistryObject<Block> FLUID_EXTRACTOR = registerBlock("fluid_extractor",
            FluidExtractorBlock::new);

    public static final RegistryObject<Block> MULTIPHASE_FLUID_TANK = registerBlock("multiphase_fluid_tank",
            MultiphaseFluidTankBlock::new);

    public static final RegistryObject<Block> FLUX_SYNTHESIZER = registerBlock("flux_synthesizer",
            FluxSynthesizerBlock::new);

    public static final RegistryObject<Block> LIQUID_REACTOR = registerBlock("liquid_reactor",
            LiquidReactorBlock::new);

    public static final RegistryObject<Block> LIQUID_FILTER = registerBlock("liquid_filter",
            LiquidFilterBlock::new);

    public static final RegistryObject<Block> UNIVERSAL_REPAIRER = registerBlock("universal_repairer",
            UniversalRepairerBlock::new);


    public static final RegistryObject<Block> GEM_DUPLICATOR = registerBlock("gem_duplicator",
            GemDuplicatorBlock::new);

    //AROMATIC BLOCK
    public static final RegistryObject<LiquidBlock> AROMATIC_BLOCK = BLOCKS.register("aromatic_block",
            () -> new LiquidBlock(ModFluids.SOURCE_AROMATIC, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //PUTRESCENT SOLUTION BLOCK
    public static final RegistryObject<LiquidBlock> PUTRESCENT_SOLUTION_BLOCK = BLOCKS.register("putrescent_solution_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PUTRESCENT_SOLUTION, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //PECTIN SLURRY BLOCK
    public static final RegistryObject<LiquidBlock> PECTIN_SLURRY_BLOCK = BLOCKS.register("pectin_slurry_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PECTIN_SLURRY, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //PRIMAL ESSENCE BLOCK
    public static final RegistryObject<LiquidBlock> PRIMAL_ESSENCE_BLOCK = BLOCKS.register("primal_essence_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PRIMAL_ESSENCE, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //ENZYME SOLUTION BLOCK
    public static final RegistryObject<LiquidBlock> ENZYME_SOLUTION_BLOCK = BLOCKS.register("enzyme_solution_block",
            () -> new LiquidBlock(ModFluids.SOURCE_ENZYME_SOLUTION, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //BOTANIC ESSENTIAL OIL BLOCK
    public static final RegistryObject<LiquidBlock> BOTANIC_ESSENTIAL_OIL_BLOCK = BLOCKS.register("botanic_essential_oil_block",
            () -> new LiquidBlock(ModFluids.SOURCE_BOTANIC_ESSENTIAL_OIL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //MINERAL SLURRY BLOCK
    public static final RegistryObject<LiquidBlock> MINERAL_SLURRY_BLOCK = BLOCKS.register("mineral_slurry_block",
            () -> new LiquidBlock(ModFluids.SOURCE_MINERAL_SLURRY, BlockBehaviour.Properties.copy(Blocks.LAVA).noLootTable()));

    //REPAIR BLOCK
    public static final RegistryObject<LiquidBlock> REPAIR_BLOCK = BLOCKS.register("repair_block",
            () -> new LiquidBlock(ModFluids.SOURCE_REPAIR, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    //PINE LOG
    public static final RegistryObject<Block> PINE_LOG = registerBlock("pine_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(3f)));
    public static final RegistryObject<Block> PINE_WOOD = registerBlock("pine_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(3f)));
    public static final RegistryObject<Block> STRIPPED_PINE_LOG = registerBlock("stripped_pine_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(3f)));
    public static final RegistryObject<Block> STRIPPED_PINE_WOOD = registerBlock("stripped_pine_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(3f)));

    public static final RegistryObject<Block> PINE_PLANKS = registerBlock("pine_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<Block> PINE_LEAVES = registerBlock("pine_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final RegistryObject<Block> PINE_SAPLING = registerBlock("pine_sapling",
            () -> new SaplingBlock(new PineTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> ICE_BRICK = registerBlock("ice_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_ICE).sound(SoundType.GLASS).strength(0.6f)));

    public static final RegistryObject<Block> SNOW_BRICK = registerBlock("snow_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK).sound(SoundType.SNOW).strength(0.4f)));

    public static final RegistryObject<Block> WET_BRICK = registerBlock("wet_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.ROOTED_DIRT).strength(0.5f)));

    public static final RegistryObject<Block> SANDSTONE_CLEAN = registerBlock("sandstone_clean",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).sound(SoundType.STONE).strength(1.2f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SANDSTONE_CARVED = registerBlock("sandstone_carved",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).sound(SoundType.STONE).strength(1.2f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DRIED_BRICK = registerBlock("dried_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS).sound(SoundType.STONE).strength(1.4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DRIED_BRICK_SMOOTH = registerBlock("dried_brick_smooth",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS).sound(SoundType.STONE).strength(1.4f).requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
