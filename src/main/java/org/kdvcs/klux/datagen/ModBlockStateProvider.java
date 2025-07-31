package org.kdvcs.klux.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.block.custom.ParsnipCropBlock;
import org.kdvcs.klux.block.custom.RottenFruitCropBlock;
import org.kdvcs.klux.block.custom.SpringOnionCropBlock;

import java.util.function.Function;

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

        blockWithItem(ModBlocks.SOUND_BLOCK);

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

        makeParsnipCrop((CropBlock) ModBlocks.PARSNIP_CROP.get(), "parsnip_stage", "parsnip_stage");
        makeSpringOnionCrop((CropBlock) ModBlocks.SPRING_ONION_CROP.get(),"spring_onion_stage_","spring_onion_stage_");
        makeRottenFruitCrop((CropBlock) ModBlocks.ROTTEN_FRUIT_CROP.get(), "rotten_fruit_stage", "rotten_fruit_stage");

        simpleBlockWithItem(ModBlocks.CACTUS_FRUIT.get(), models().cross(blockTexture(ModBlocks.CACTUS_FRUIT.get()).getPath(),
                blockTexture(ModBlocks.CACTUS_FRUIT.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_CACTUS_FRUIT.get(), models().singleTexture("potted_cactus_fruit", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.POTTED_CACTUS_FRUIT.get())).renderType("cutout"));


    }

    public void makeParsnipCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> parsnipStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeRottenFruitCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> rottenFruitStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeSpringOnionCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> springOnionStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] springOnionStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((SpringOnionCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((SpringOnionCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] parsnipStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((ParsnipCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((ParsnipCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] rottenFruitStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RottenFruitCropBlock) block).getAgeProperty()),
                new ResourceLocation(Klux.MODID, "block/" + textureName + state.getValue(((RottenFruitCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
