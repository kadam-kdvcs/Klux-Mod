package org.kdvcs.klux.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.custom.*;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.sound.ModSounds;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Klux.MODID);

    public static final RegistryObject<Block> HAY_BRICK = registerBlock("hay_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).strength(0.3f).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> EARTH_CRYSTAL_BLOCK = registerBlock("earth_crystal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> EARTH_CRYSTAL_FRAME = registerBlock("earth_crystal_frame",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.METAL)));

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

    //CROP
    public static final RegistryObject<Block> PARSNIP_CROP = BLOCKS.register("parsnip_crop",
            () -> new ParsnipCropBlock(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion().noCollission()));

    public static final RegistryObject<Block> SPRING_ONION_CROP = BLOCKS.register("spring_onion_crop",
            () -> new SpringOnionCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> ROTTEN_FRUIT_CROP = BLOCKS.register("rotten_fruit_crop",
            () -> new RottenFruitCropBlock(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion().noCollission()));

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

    //AROMATIC BLOCK
    public static final RegistryObject<LiquidBlock> AROMATIC_BLOCK = BLOCKS.register("aromatic_block",
            () -> new LiquidBlock(ModFluids.SOURCE_AROMATIC, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

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
