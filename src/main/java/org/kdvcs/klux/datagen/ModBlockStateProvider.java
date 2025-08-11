package org.kdvcs.klux.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.block.custom.CottonCropBlock;
import org.kdvcs.klux.block.custom.RiceCropBlock;
import org.kdvcs.klux.block.custom.RottenFruitCropBlock;
import org.kdvcs.klux.block.custom.SpringOnionCropBlock;

import java.util.function.Function;

/*
 *
 * credit to @Kaupenjoe
 *
 *
 */

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Klux.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.EARTH_CRYSTAL_BLOCK);
        blockWithItem(ModBlocks.HAY_BRICK);

        //ORES
        blockWithItem(ModBlocks.EARTH_CRYSTAL_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE);
        blockWithItem(ModBlocks.FIRE_QUARTZ_ORE);
        blockWithItem(ModBlocks.ENDERGON_CRYSTAL_ORE);
        blockWithItem(ModBlocks.EARTH_CRYSTAL_FRAME);
        blockWithItem(ModBlocks.FIRE_QUARTZ_FRAME);
        blockWithItem(ModBlocks.IRON_SAND);
        blockWithItem(ModBlocks.BACKGROUND_BRICK);

        blockWithItem(ModBlocks.SOUND_BLOCK);
        blockWithItem(ModBlocks.BONES_ORE);

        //STAIRS
        stairsBlock(((StairBlock) ModBlocks.EARTH_CRYSTAL_STAIRS.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));
        stairsBlock(((StairBlock) ModBlocks.HAY_STAIRS.get()), blockTexture(ModBlocks.HAY_BRICK.get()));

        //SLABS
        slabBlock(((SlabBlock) ModBlocks.EARTH_CRYSTAL_SLAB.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()),
                blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.HAY_SLAB.get()), blockTexture(ModBlocks.HAY_BRICK.get()),
                blockTexture(ModBlocks.HAY_BRICK.get()));

        //BUTTON
        buttonBlock(((ButtonBlock) ModBlocks.EARTH_CRYSTAL_BUTTON.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));

        //PRESSURE PLATE
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.EARTH_CRYSTAL_PRESSURE_PLATE.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));

        //FENCE
        fenceBlock(((FenceBlock) ModBlocks.EARTH_CRYSTAL_FENCE.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.EARTH_CRYSTAL_FENCE_GATE.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));

        //WALL
        wallBlock(((WallBlock) ModBlocks.EARTH_CRYSTAL_WALL.get()), blockTexture(ModBlocks.EARTH_CRYSTAL_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.HAY_WALL.get()), blockTexture(ModBlocks.HAY_BRICK.get()));

        //DOOR
        doorBlockWithRenderType(((DoorBlock) ModBlocks.EARTH_CRYSTAL_DOOR.get()), modLoc("block/earth_crystal_door_bottom"),
                modLoc("block/earth_crystal_door_top"), "cutout");

        //TRAPDOOR
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.EARTH_CRYSTAL_TRAPDOOR.get()), modLoc("block/earth_crystal_trapdoor"),
                true, "cutout");

        makeSpringOnionCrop((CropBlock) ModBlocks.SPRING_ONION_CROP.get(),"spring_onion_stage_","spring_onion_stage_");
        makeRottenFruitCrop((CropBlock) ModBlocks.ROTTEN_FRUIT_CROP.get(), "rotten_fruit_stage", "rotten_fruit_stage");
        makeRiceCrop((CropBlock) ModBlocks.RICE_CROP.get(), "rice_stage_", "rice_stage_");
        makeCottonCrop((CropBlock) ModBlocks.COTTON_CROP.get(), "cotton_stage_", "cotton_stage_");

        simpleBlockWithItem(ModBlocks.CACTUS_FRUIT.get(), models().cross(blockTexture(ModBlocks.CACTUS_FRUIT.get()).getPath(),
                blockTexture(ModBlocks.CACTUS_FRUIT.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_CACTUS_FRUIT.get(), models().singleTexture("potted_cactus_fruit", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.POTTED_CACTUS_FRUIT.get())).renderType("cutout"));

        logBlock(((RotatedPillarBlock) ModBlocks.PINE_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.PINE_WOOD.get()), blockTexture(ModBlocks.PINE_LOG.get()),
                blockTexture(ModBlocks.PINE_LOG.get()));

        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_PINE_LOG.get()), blockTexture(ModBlocks.STRIPPED_PINE_LOG.get()),
                new ResourceLocation(Klux.MODID, "block/stripped_pine_log_top"));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_PINE_WOOD.get()), blockTexture(ModBlocks.STRIPPED_PINE_LOG.get()),
                blockTexture(ModBlocks.STRIPPED_PINE_LOG.get()));

        blockItem(ModBlocks.PINE_LOG);
        blockItem(ModBlocks.PINE_WOOD);
        blockItem(ModBlocks.STRIPPED_PINE_LOG);
        blockItem(ModBlocks.STRIPPED_PINE_WOOD);

        blockWithItem(ModBlocks.PINE_PLANKS);

        leavesBlock(ModBlocks.PINE_LEAVES);

        saplingBlock(ModBlocks.PINE_SAPLING);

        blockWithItem(ModBlocks.ICE_BRICK);
        blockWithItem(ModBlocks.SANDSTONE_CARVED);
        blockWithItem(ModBlocks.SANDSTONE_CLEAN);
        blockWithItem(ModBlocks.DRIED_BRICK);
        blockWithItem(ModBlocks.SNOW_BRICK);
        blockWithItem(ModBlocks.DRIED_BRICK_SMOOTH);
        blockWithItem(ModBlocks.WET_BRICK);
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(Klux.MODID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    public void makeRottenFruitCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> rottenFruitStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeSpringOnionCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> springOnionStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    public void makeRiceCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> riceStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeCottonCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cottonStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] springOnionStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((SpringOnionCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((SpringOnionCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] rottenFruitStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RottenFruitCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((RottenFruitCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] riceStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RiceCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((RiceCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] cottonStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CottonCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((CottonCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
