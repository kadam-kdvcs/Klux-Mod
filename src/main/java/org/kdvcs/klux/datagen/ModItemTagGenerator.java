package org.kdvcs.klux.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_,
                               @Nullable ExistingFileHelper existingFileHelper) {

        super(p_275343_, p_275729_, p_275322_, Klux.MODID, existingFileHelper);

    }

    private static TagKey<Item> forgeTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("forge", name));
    }

    public static final TagKey<Item> FORGE_ORES = forgeTag("ores");
    public static final TagKey<Item> FORGE_SEEDS = forgeTag("seeds");
    public static final TagKey<Item> FORGE_RAWMEATS = forgeTag("rawmeats");
    public static final TagKey<Item> FORGE_GEMS = forgeTag("gems");
    public static final TagKey<Item> FORGE_INGOTS = forgeTag("ingots");
    public static final TagKey<Item> FORGE_DUSTS = forgeTag("dusts");
    public static final TagKey<Item> FORGE_PUTRESCENT = forgeTag("putrescent");
    public static final TagKey<Item> FORGE_FRUITS = forgeTag("fruits");
    public static final TagKey<Item> FORGE_SILICON = forgeTag("silicon");
    public static final TagKey<Item> FORGE_ORGANIC_FIBERS = forgeTag("organic_fibers");
    public static final TagKey<Item> FORGE_AROMA_POWDERS = forgeTag("aromapowders");

    //FLOWER FAMILY
    public static final TagKey<Item> FORGE_FLOWERS = forgeTag("flowers");

    public static final TagKey<Item> FORGE_EXTRACTION_MESHES =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge","extraction_meshes"));

    public static final TagKey<Item> FORGE_EXTRACTION_MESH_BASIC =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge","extraction_mesh_basic"));

    public static final TagKey<Item> FORGE_EXTRACTION_MESH_ADVANCED =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge","extraction_mesh_advanced"));

    public static final TagKey<Item> FORGE_EXTRACTION_MESH_ULTIMATE =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge","extraction_mesh_ultimate"));

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {

        this.tag(ModTags.Items.EARTH_CRYSTAL_ORES)
                .add(ModBlocks.EARTH_CRYSTAL_ORE.get().asItem())
                .add(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get().asItem());

        this.tag(ModTags.Items.FIRE_QUARTZ_ORES)
                .add(ModBlocks.FIRE_QUARTZ_ORE.get().asItem());

        this.tag(ModTags.Items.ENDERGON_CRYSTAL_ORES)
                .add(ModBlocks.ENDERGON_CRYSTAL_ORE.get().asItem());

        this.tag(ModTags.Items.BONES_ORES)
                .add(ModBlocks.BONES_ORE.get().asItem());

        this.tag(FORGE_ORES)
                .replace(false)
                .addTag(ModTags.Items.EARTH_CRYSTAL_ORES)
                .addTag(ModTags.Items.FIRE_QUARTZ_ORES)
                .addTag(ModTags.Items.ENDERGON_CRYSTAL_ORES)
                .addTag(ModTags.Items.BONES_ORES);

        this.tag(FORGE_FRUITS)
                .replace(false)
                .add(Items.APPLE);

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(

                        //EARTH CRYSTAL ARMOR
                        ModItems.EARTH_CRYSTAL_HELMET.get(),
                        ModItems.EARTH_CRYSTAL_CHESTPLATE.get(),
                        ModItems.EARTH_CRYSTAL_LEGGINGS.get(),
                        ModItems.EARTH_CRYSTAL_BOOTS.get(),

                        //SOFT ARMOR
                        ModItems.SOFT_HELMET.get(),
                        ModItems.SOFT_CHESTPLATE.get(),
                        ModItems.SOFT_LEGGINGS.get(),
                        ModItems.SOFT_BOOTS.get()

                );

        this.tag(FORGE_GEMS)
                .replace(false)
                .addTag(ModTags.Items.EARTH_CRYSTALS)
                .addTag(ModTags.Items.FIRE_QUARTZS)
                .addTag(ModTags.Items.ENDERGON_CRYSTALS)
                .addTag(ModTags.Items.APATITES);

        this.tag(ModTags.Items.EARTH_CRYSTALS)
                .add(ModItems.EARTH_CRYSTAL.get());

        this.tag(ModTags.Items.FIRE_QUARTZS)
                .add(ModItems.FIRE_QUARTZ.get());

        this.tag(ModTags.Items.ENDERGON_CRYSTALS)
                .add(ModItems.ENDERGON_CRYSTAL.get());

        this.tag(ModTags.Items.APATITES)
                .add(ModItems.APATITE.get());

        this.tag(FORGE_ORGANIC_FIBERS)
                .add(ModItems.SPIDER_SILK_FIBER.get())
                .add(ModItems.PROTEIN_FIBER.get());

        this.tag(FORGE_SILICON)
                .replace(false)
                .add(ModItems.SILICON_BOULE.get());

        this.tag(FORGE_PUTRESCENT)
                .replace(false)
                .add(Items.SPIDER_EYE)
                .add(Items.FERMENTED_SPIDER_EYE)
                .add(Items.ROTTEN_FLESH);

        //FLOWER FAMILY
        this.tag(FORGE_FLOWERS)
                .replace(false)
                .addTag(ModTags.Items.ESSENCE_FLOWERS)
                .addTag(ModTags.Items.HERBAL_FLOWERS)
                .addTag(ModTags.Items.WOODY_FLOWERS)
                .addTag(ModTags.Items.WILD_FLOWERS);

        this.tag(ModTags.Items.ESSENCE_FLOWERS)
                .add(Items.BLUE_ORCHID)
                .add(Items.ALLIUM)
                .add(Items.CORNFLOWER)
                .add(Items.PITCHER_PLANT)
                .add(Items.PINK_PETALS);

        this.tag(ModTags.Items.HERBAL_FLOWERS)
                .add(Items.DANDELION)
                .add(Items.AZURE_BLUET)
                .add(Items.LILY_OF_THE_VALLEY)
                .add(Items.OXEYE_DAISY)
                .add(Items.POPPY)
                .add(Items.FLOWERING_AZALEA_LEAVES)
                .add(Items.FLOWERING_AZALEA);

        this.tag(ModTags.Items.WOODY_FLOWERS)
                .add(Items.LILAC)
                .add(Items.PEONY)
                .add(Items.ROSE_BUSH)
                .add(Items.CHERRY_LEAVES)
                .add(Items.MANGROVE_PROPAGULE)
                .add(Items.SUNFLOWER);

        this.tag(ModTags.Items.WILD_FLOWERS)
                .add(Items.RED_TULIP)
                .add(Items.WHITE_TULIP)
                .add(Items.ORANGE_TULIP)
                .add(Items.PINK_TULIP)
                .add(Items.TORCHFLOWER);    //TODO: How about wither rose?

        //INGOTS
        this.tag(FORGE_INGOTS)
                .replace(false)
                .addTag(ModTags.Items.AROMATIC)
                .addTag(ModTags.Items.WROUGHT_IRON)
                .addTag(ModTags.Items.FIERY);

        this.tag(ModTags.Items.AROMATIC)
                .add(ModItems.AROMATIC_INGOT.get());
        this.tag(ModTags.Items.WROUGHT_IRON)
                .add(ModItems.WROUGHT_IRON_INGOT.get());
        this.tag(ModTags.Items.FIERY)
                .add(ModItems.FIERY_INGOT.get());

        //AROMA FAMILY
        this.tag(FORGE_AROMA_POWDERS)
                .replace(false)
                .addTag(ModTags.Items.POWDER_AROMATIC)
                .addTag(ModTags.Items.POWDER_HERBAL)
                .addTag(ModTags.Items.POWDER_WOODY)
                .addTag(ModTags.Items.POWDER_WILDFLOWER);

        this.tag(ModTags.Items.POWDER_AROMATIC)
                .add(ModItems.AROMATIC_POWDER.get());
        this.tag(ModTags.Items.POWDER_HERBAL)
                .add(ModItems.HERBAL_ACTIVE_POWDER.get());
        this.tag(ModTags.Items.POWDER_WOODY)
                .add(ModItems.WOODY_ESSENCE_POWDER.get());
        this.tag(ModTags.Items.POWDER_WILDFLOWER)
                .add(ModItems.WILDFLOWER_POLLEN_POWDER.get());

        //DUSTS
        this.tag(FORGE_DUSTS)
                .replace(false)
                .addTag(ModTags.Items.DUST_AROMATIC);

        this.tag(ModTags.Items.DUST_AROMATIC)
                .add(ModItems.AROMATIC_DUST.get());

        //MUSIC DISC
        this.tag(ItemTags.MUSIC_DISCS)
                .replace(false)
                .add(ModItems.RUMBLE_MUSIC_DISC.get(),
                        ModItems.BIG_MUSIC_MUSIC_DISC.get());

        //VANILLA TOOLS
        this.tag(ItemTags.PICKAXES)
                .replace(false)
                .add(ModItems.EARTH_CRYSTAL_PICKAXE.get());

        this.tag(ItemTags.AXES)
                .replace(false)
                .add(ModItems.EARTH_CRYSTAL_AXE.get());

        this.tag(ItemTags.SWORDS)
                .replace(false)
                .add(ModItems.EARTH_CRYSTAL_SWORD.get());

        this.tag(ItemTags.SHOVELS)
                .replace(false)
                .add(ModItems.EARTH_CRYSTAL_SHOVEL.get());

        this.tag(ItemTags.HOES)
                .replace(false)
                .add(ModItems.EARTH_CRYSTAL_HOE.get());

        //
        this.tag(FORGE_SEEDS)
                .replace(false)
                .add(ModItems.SPRING_ONION_SEEDS.get(),
                     ModItems.RICE_SEEDS.get(),
                        ModItems.COTTON_SEEDS.get());

        this.tag(FORGE_RAWMEATS)
                .replace(false)
                .add(Items.CHICKEN)
                .add(Items.BEEF)
                .add(Items.PORKCHOP)
                .add(Items.MUTTON)
                .add(Items.RABBIT)
                .add(Items.SALMON)
                .add(Items.COD)
                .add(Items.TROPICAL_FISH)
                .add(Items.PUFFERFISH)
                .add(ModItems.RAW_HORSE_MEAT.get());

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.PINE_LOG.get().asItem())
                .add(ModBlocks.PINE_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_PINE_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_PINE_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.PINE_PLANKS.get().asItem());

        this.tag(FORGE_EXTRACTION_MESH_BASIC)
                .add(ModItems.EXTRACTION_MESH.get());

        this.tag(FORGE_EXTRACTION_MESH_ADVANCED)
                .add(ModItems.ADVANCED_EXTRACTION_MESH.get());

        this.tag(FORGE_EXTRACTION_MESH_ULTIMATE)
                .add(ModItems.ULTIMATE_EXTRACTION_MESH.get());

    }

}
