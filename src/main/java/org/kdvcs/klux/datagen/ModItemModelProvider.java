package org.kdvcs.klux.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.item.ModItems;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {

    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {

        trimMaterials.put(TrimMaterials.QUARTZ, 0.1f);
        trimMaterials.put(TrimMaterials.IRON, 0.2f);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3f);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4f);
        trimMaterials.put(TrimMaterials.COPPER, 0.5f);
        trimMaterials.put(TrimMaterials.GOLD, 0.6f);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7f);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8f);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9f);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0f);

    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Klux.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //CRYSTALS
        simpleItem(ModItems.EARTH_CRYSTAL);
        simpleItem(ModItems.FIRE_QUARTZ);
        simpleItem(ModItems.ENDERGON_CRYSTAL);
        simpleItem(ModItems.DEHYDRATED_SEEDS);
        simpleItem(ModItems.PUTRID_FAT);
        simpleItem(ModItems.ROTTEN_FRUIT);
        simpleItem(ModItems.ROTTEN_FRUIT_SEEDS);
        simpleItem(ModItems.AIR_DUCT);
        simpleItem(ModItems.HAY_BALL);
        simpleItem(ModItems.LEATHER_PASTE);
        simpleItem(ModItems.SPIDER_SILK_FIBER);
        simpleItem(ModItems.SOFT_ARMOR_CLOTH);
        simpleItem(ModItems.CARBOHYDRATE_POWDER);
        simpleItem(ModItems.MINERAL_POWDER);
        simpleItem(ModItems.PROTEIN_CONCENTRATE);
        simpleItem(ModItems.VITAMIN_DUST);
        simpleItem(ModItems.UNIVERSAL_FEED);
        simpleItem(ModItems.NUTRI_BLOCK);
        simpleItem(ModItems.AROMATIC_POWDER);
        simpleItem(ModItems.BOTANICAL_RESIN_BEAD);
        simpleItem(ModItems.ADHESIVE_PASTE);
        simpleItem(ModItems.PRESSING_ROD_ASSEMBLY);
        simpleItem(ModItems.SOLV_SHELL);
        simpleItem(ModItems.DISSOLVENT_BEARING);
        simpleItem(ModItems.FILTER_CORE);
        simpleItem(ModItems.SCORIA_POWDER);
        simpleItem(ModItems.MOLTEN_CORE);

        simpleItem(ModItems.PRECISION_SUBSTRATE);
        simpleItem(ModItems.PECTI_BOND);

        //EXTRACTION MESHES
        simpleItem(ModItems.EXTRACTION_MESH);
        simpleItem(ModItems.ADVANCED_EXTRACTION_MESH);
        simpleItem(ModItems.ULTIMATE_EXTRACTION_MESH);

        simpleItem(ModItems.FERMENTED_AROMATIC_SLURRY);
        simpleItem(ModItems.AROMATIC_COAGULATE);
        simpleItem(ModItems.AROMATIC_DUST);
        simpleItem(ModItems.AROMATIC_INGOT);
        simpleItem(ModItems.SEALED_TANK);
        simpleItem(ModItems.PECTIN_SLURRY_BUCKET);
        simpleItem(ModItems.BRAN);
        simpleItem(ModItems.PRIMAL_ESSENCE_BUCKET);
        simpleItem(ModItems.ENZYME_SOLUTION_BUCKET);
        simpleItem(ModItems.REINFORCED_AROMATIC_CRYSTAL_PLATE);
        simpleItem(ModItems.TRIPLE_SEALED_TANK);
        simpleItem(ModItems.WITHERED_LEAF);
        simpleItem(ModItems.WOVEN_SUBSTRATE);
        simpleItem(ModItems.ANCIENT_COINS);
        simpleItem(ModItems.REDSTONE_CRYSTAL_CORE);
        simpleItem(ModItems.RAW_SILICON);
        simpleItem(ModItems.SILICON_BOULE);
        simpleItem(ModItems.BIG_MUSIC_MUSIC_DISC);
        simpleItem(ModItems.CHARGED_ENDERGON_CRYSTAL);
        simpleItem(ModItems.PROTEIN_FIBER);
        simpleItem(ModItems.EXAMPLE_TOOL);
        simpleItem(ModItems.WROUGHT_IRON_INGOT);
        simpleItem(ModItems.WROUGHT_IRON_GEAR);
        simpleItem(ModItems.MINERAL_SLURRY_BUCKET);
        simpleItem(ModItems.REPAIR_BUCKET);
        simpleItem(ModItems.SLAG_LUMP);
        simpleItem(ModItems.HERBAL_ACTIVE_POWDER);
        simpleItem(ModItems.WOODY_ESSENCE_POWDER);
        simpleItem(ModItems.WILDFLOWER_POLLEN_POWDER);

        simpleItem(ModItems.APPLE_COPPER);
        simpleItem(ModItems.APPLE_IRON);
        simpleItem(ModItems.APPLE_LAPIS);
        simpleItem(ModItems.APPLE_DIAMOND);
        simpleItem(ModItems.APPLE_EMERALD);
        simpleItem(ModItems.APPLE_AMETHYST);

        simpleItem(ModItems.RETURN_SCEPTER);
        simpleItem(ModItems.SALAD);
        simpleItem(ModItems.DETECTOR);
        simpleItem(ModItems.ENRICHED_COAL);
        simpleItem(ModItems.PARSNIP);
        simpleItem(ModItems.PARSNIP_SEEDS);
        simpleItem(ModItems.SPRING_ONION);
        simpleItem(ModItems.SPRING_ONION_SEEDS);
        simpleItem(ModItems.RUMBLE_MUSIC_DISC);
        simpleItem(ModItems.HUMICS);
        simpleItem(ModItems.REDSTONE_RESONATOR);
        simpleItem(ModItems.FURNACE_CORE);
        simpleItem(ModItems.AROMATIC_BUCKET);
        simpleItem(ModItems.PUTRESCENT_SOLUTION_BUCKET);
        simpleItem(ModItems.MULTIPHASE_FLUID_CONTAINER);
        simpleItem(ModItems.POLYMER_MEMBRANE_PLATE);
        simpleItem(ModItems.FLUX_CORE);

        simpleItem(ModItems.BOTANIC_ESSENTIAL_OIL_BUCKET);
        simpleItem(ModItems.REINFORCED_AROMATIC_CRYSTAL_GEAR);

        simpleItem(ModItems.ROUGH_CAKE_BASE);
        simpleItem(ModItems.VITAMIN_CAKE);

        simpleBlockItem(ModBlocks.EARTH_CRYSTAL_DOOR);

        fenceItem(ModBlocks.EARTH_CRYSTAL_FENCE, ModBlocks.EARTH_CRYSTAL_BLOCK);
        buttonItem(ModBlocks.EARTH_CRYSTAL_BUTTON, ModBlocks.EARTH_CRYSTAL_BLOCK);
        wallItem(ModBlocks.EARTH_CRYSTAL_WALL, ModBlocks.EARTH_CRYSTAL_BLOCK);
        wallItem(ModBlocks.HAY_WALL, ModBlocks.HAY_BRICK);

        evenSimplerBlockItem(ModBlocks.EARTH_CRYSTAL_STAIRS);
        evenSimplerBlockItem(ModBlocks.EARTH_CRYSTAL_SLAB);
        evenSimplerBlockItem(ModBlocks.EARTH_CRYSTAL_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.EARTH_CRYSTAL_FENCE_GATE);

        evenSimplerBlockItem(ModBlocks.HAY_STAIRS);
        evenSimplerBlockItem(ModBlocks.HAY_SLAB);

        trapdoorItem(ModBlocks.EARTH_CRYSTAL_TRAPDOOR);

        //handheld
        handheldItem(ModItems.EARTH_CRYSTAL_SWORD);
        handheldItem(ModItems.EARTH_CRYSTAL_HOE);
        handheldItem(ModItems.EARTH_CRYSTAL_AXE);
        handheldItem(ModItems.EARTH_CRYSTAL_PICKAXE);
        handheldItem(ModItems.EARTH_CRYSTAL_SHOVEL);

        //ARMORS
        trimmedArmorItem(ModItems.EARTH_CRYSTAL_HELMET);
        trimmedArmorItem(ModItems.EARTH_CRYSTAL_CHESTPLATE);
        trimmedArmorItem(ModItems.EARTH_CRYSTAL_LEGGINGS);
        trimmedArmorItem(ModItems.EARTH_CRYSTAL_BOOTS);

        trimmedArmorItem(ModItems.SOFT_HELMET);
        trimmedArmorItem(ModItems.SOFT_CHESTPLATE);
        trimmedArmorItem(ModItems.SOFT_LEGGINGS);
        trimmedArmorItem(ModItems.SOFT_BOOTS);


        simpleBlockItemBlockTexture(ModBlocks.CACTUS_FRUIT);

        saplingItem(ModBlocks.PINE_SAPLING);

    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = Klux.MODID;

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath);
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png","textures");

                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                this.withExistingParent(itemRegistryObject.getId().getPath(),
                        mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));

            });
        }
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Klux.MODID,"block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Klux.MODID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Klux.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    //FENCE
    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(Klux.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    //BUTTON
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(Klux.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    //WALL
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(Klux.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Klux.MODID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Klux.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Klux.MODID,"block/" + item.getId().getPath()));
    }

}
