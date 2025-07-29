package org.kdvcs.klux.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final List<ItemLike> EARTH_CRYSTAL_SMELTABLES = List.of(ModBlocks.EARTH_CRYSTAL_ORE.get(),
            ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get());

    public ModRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> p_251297_) {

        //GENERATE A SMELTING RECIPE, p250789(Exp); p252144(Ticks), p251687(Group)
        oreSmelting(p_251297_, EARTH_CRYSTAL_SMELTABLES, RecipeCategory.MISC, ModItems.EARTH_CRYSTAL.get(),
                0.1f,130,"earthcrystal");

        //GENERATE A BLASTING RECIPE
        oreBlasting(p_251297_, EARTH_CRYSTAL_SMELTABLES, RecipeCategory.MISC, ModItems.EARTH_CRYSTAL.get(),
                0.1f,65,"earthcrystal");

        //FURNACE CORE
        SimpleCookingRecipeBuilder.blasting(
                            Ingredient.of(Items.FURNACE),
                            RecipeCategory.MISC,
                            ModItems.FURNACE_CORE.get(),
                            0,
                            600)
                    .unlockedBy(getHasName(Blocks.BLAST_FURNACE), has(Blocks.BLAST_FURNACE))
                    .save(p_251297_, new ResourceLocation(Klux.MODID, "furnacecore"));

        //AROMATIC INGOT
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.AROMATIC_DUST.get()),
                        RecipeCategory.MISC,
                        ModItems.AROMATIC_INGOT.get(),
                        1.2f,
                        240)
                .unlockedBy(getHasName(ModItems.AROMATIC_INGOT.get()), has(ModItems.AROMATIC_INGOT.get()))
                .save(p_251297_, new ResourceLocation(Klux.MODID, "aromaticingot"));

        //LEATHER
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.LEATHER_PASTE.get()),
                        RecipeCategory.MISC,
                        Items.LEATHER,
                        0.8f,
                        160)
                .unlockedBy(getHasName(ModItems.LEATHER_PASTE.get()), has(ModItems.LEATHER_PASTE.get()))
                .save(p_251297_, new ResourceLocation(Klux.MODID, "leatherpaste"));


        //GENERATE A SHAPED RECIPE
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EARTH_CRYSTAL_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.EARTH_CRYSTAL.get())
                .unlockedBy(getHasName(ModItems.EARTH_CRYSTAL.get()), has(ModItems.EARTH_CRYSTAL.get()))
                .save(p_251297_);

        //PRESSING ROD ASSEMBLY
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PRESSING_ROD_ASSEMBLY.get())
                .pattern("# #")
                .pattern("ABA")
                .pattern("# #")
                .define('#', Items.STICK)
                .define('A', Blocks.STONE)
                .define('B', ModItems.ADHESIVE_PASTE.get())
                .unlockedBy(getHasName(ModItems.PRESSING_ROD_ASSEMBLY.get()), has(ModItems.PRESSING_ROD_ASSEMBLY.get()))
                .save(p_251297_);

        //EXTRACTION MESH
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXTRACTION_MESH.get(),2)
                .pattern("###")
                .pattern("AAA")
                .pattern("###")
                .define('#', Items.IRON_INGOT)
                .define('A', ModItems.SPIDER_SILK_FIBER.get())
                .unlockedBy(getHasName(ModItems.EXTRACTION_MESH.get()), has(ModItems.EXTRACTION_MESH.get()))
                .save(p_251297_);

        //EXTRACTOR
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EXTRACTOR.get())
                .pattern("ESE")
                .pattern("KAK")
                .pattern("EBE")
                .define('E', Items.DIAMOND)
                .define('S', ModItems.REDSTONE_RESONATOR.get())
                .define('K', ModItems.PRESSING_ROD_ASSEMBLY.get())
                .define('A', ModBlocks.EARTH_CRYSTAL_FRAME.get())
                .define('B', ModItems.EXTRACTION_MESH.get())
                .unlockedBy(getHasName(ModBlocks.EXTRACTOR.get()), has(ModBlocks.EXTRACTOR.get()))
                .save(p_251297_);

        //HAY BRICK
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HAY_BRICK.get(),4)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.HAY_BALL.get())
                .unlockedBy(getHasName(ModItems.HAY_BALL.get()), has(ModItems.HAY_BALL.get()))
                .save(p_251297_);

        //COBWEB
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.COBWEB)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STRING)
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(p_251297_);

        //STRING
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING,9)
                .requires(Blocks.COBWEB)
                .unlockedBy(getHasName(Blocks.COBWEB), has(Blocks.COBWEB))
                .save(p_251297_);

        //HAY BALL
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HAY_BALL.get(),4)
                .requires(ModBlocks.HAY_BRICK.get())
                .unlockedBy(getHasName(ModBlocks.HAY_BRICK.get()), has(ModBlocks.HAY_BRICK.get()))
                .save(p_251297_);

        //FERMENTED AROMATIC SLURRY
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FERMENTED_AROMATIC_SLURRY.get(),4)
                .requires(ModItems.VITAMIN_DUST.get())
                .requires(ModItems.AROMATIC_POWDER.get())
                .requires(ModItems.ROTTEN_FRUIT.get())
                .requires(ModItems.ADHESIVE_PASTE.get())
                .unlockedBy(getHasName(ModItems.FERMENTED_AROMATIC_SLURRY.get()), has(ModItems.FERMENTED_AROMATIC_SLURRY.get()))
                .save(p_251297_);

        //AROMATIC DUST
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AROMATIC_DUST.get(),2)
                .requires(ModItems.AROMATIC_COAGULATE.get())
                .requires(ModItems.MINERAL_POWDER.get())
                .requires(ModItems.VITAMIN_DUST.get())
                .unlockedBy(getHasName(ModItems.AROMATIC_DUST.get()), has(ModItems.AROMATIC_DUST.get()))
                .save(p_251297_);

        //UNIVERSAL FEED
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.UNIVERSAL_FEED.get(),8)
                .requires(ModItems.CARBOHYDRATE_POWDER.get())
                .requires(ModItems.VITAMIN_DUST.get())
                .requires(ModItems.MINERAL_POWDER.get())
                .requires(ModItems.PROTEIN_CONCENTRATE.get())
                .unlockedBy(getHasName(ModItems.UNIVERSAL_FEED.get()), has(ModItems.UNIVERSAL_FEED.get()))
                .save(p_251297_);

        /*
        //SOFT ARMOR
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOFT_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ModItems.SOFT_ARMOR_CLOTH.get())
                .unlockedBy(getHasName(ModItems.SOFT_ARMOR_CLOTH.get()), has(ModItems.SOFT_ARMOR_CLOTH.get()))
                .save(p_251297_);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOFT_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.SOFT_ARMOR_CLOTH.get())
                .unlockedBy(getHasName(ModItems.SOFT_ARMOR_CLOTH.get()), has(ModItems.SOFT_ARMOR_CLOTH.get()))
                .save(p_251297_);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOFT_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.SOFT_ARMOR_CLOTH.get())
                .unlockedBy(getHasName(ModItems.SOFT_ARMOR_CLOTH.get()), has(ModItems.SOFT_ARMOR_CLOTH.get()))
                .save(p_251297_);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOFT_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.SOFT_ARMOR_CLOTH.get())
                .unlockedBy(getHasName(ModItems.SOFT_ARMOR_CLOTH.get()), has(ModItems.SOFT_ARMOR_CLOTH.get()))
                .save(p_251297_);

         */

        //SLIME BALL
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SLIME_BALL,4)
                .pattern("L")
                .pattern("#")
                .pattern("L")
                .define('L', Items.SUGAR)
                .define('#', ModItems.ROTTEN_FRUIT.get())
                .unlockedBy(getHasName(ModItems.ROTTEN_FRUIT.get()), has(ModItems.ROTTEN_FRUIT.get()))
                .save(p_251297_);

        //FRAME
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EARTH_CRYSTAL_FRAME.get())
                .pattern("#A#")
                .pattern("A A")
                .pattern("#A#")
                .define('#', ModItems.EARTH_CRYSTAL.get())
                .define('A', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.EARTH_CRYSTAL.get()), has(ModItems.EARTH_CRYSTAL.get()))
                .save(p_251297_);

        //HUMICS
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HUMICS.get(),4)
                .pattern("###")
                .pattern("AAD")
                .define('#', Items.WHEAT)
                .define('A', Items.SUGAR_CANE)
                .define('D', Blocks.DIRT)
                .unlockedBy(getHasName(Items.SUGAR_CANE), has(Items.SUGAR_CANE))
                .save(p_251297_);

        //REDSTONE RESONATOR
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REDSTONE_RESONATOR.get())
                .pattern("#A#")
                .pattern("ABA")
                .pattern("#A#")
                .define('#', Items.REDSTONE)
                .define('A', Items.GOLD_INGOT)
                .define('B', Items.LAPIS_LAZULI)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(p_251297_);

        //AIR DUCT
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AIR_DUCT.get())
                .pattern("#A#")
                .pattern("   ")
                .pattern("#A#")
                .define('#', Items.COPPER_INGOT)
                .define('A', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(p_251297_);

        //DEHYDRATOR
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DEHYDRATOR.get())
                .pattern("#A#")
                .pattern("CDC")
                .pattern("#B#")
                .define('#', ItemTags.TERRACOTTA)
                .define('A', ModItems.REDSTONE_RESONATOR.get())
                .define('C', ModItems.AIR_DUCT.get())
                .define('D', ModBlocks.EARTH_CRYSTAL_FRAME.get())
                .define('B', Blocks.FURNACE)
                .unlockedBy(getHasName(ModBlocks.EARTH_CRYSTAL_FRAME.get()), has(ModBlocks.EARTH_CRYSTAL_FRAME.get()))
                .save(p_251297_);

        //COMPRESSOR
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COMPRESSOR.get())
                .pattern("TAT")
                .pattern("#B#")
                .pattern("TAT")
                .define('#', ModItems.FURNACE_CORE.get())
                .define('A', ModItems.REDSTONE_RESONATOR.get())
                .define('B',ModBlocks.EARTH_CRYSTAL_FRAME.get())
                .define('T', Items.COPPER_INGOT)
                .unlockedBy(getHasName(ModBlocks.EARTH_CRYSTAL_FRAME.get()), has(ModBlocks.EARTH_CRYSTAL_FRAME.get()))
                .save(p_251297_);

        //GENERATE A SHAPELESS RECIPE, requires(Ingredient)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EARTH_CRYSTAL.get(),9)
                .requires(ModBlocks.EARTH_CRYSTAL_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.EARTH_CRYSTAL_BLOCK.get()), has(ModBlocks.EARTH_CRYSTAL_BLOCK.get()))
                .save(p_251297_);

    }

    protected static void oreSmelting(Consumer<FinishedRecipe> p_250654_, List<ItemLike> p_250172_,
                                      RecipeCategory p_250588_, ItemLike p_251868_, float p_250789_, int p_252144_, String p_251687_) {
        oreCooking(p_250654_, RecipeSerializer.SMELTING_RECIPE,
                p_250172_, p_250588_, p_251868_, p_250789_, p_252144_, p_251687_,
                "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> p_248775_, List<ItemLike> p_251504_,
                                      RecipeCategory p_248846_, ItemLike p_249735_, float p_248783_, int p_250303_, String p_251984_) {
        oreCooking(p_248775_, RecipeSerializer.BLASTING_RECIPE, p_251504_, p_248846_, p_249735_, p_248783_, p_250303_, p_251984_,
                "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> p_250791_, RecipeSerializer<? extends AbstractCookingRecipe> p_251817_, List<ItemLike> p_249619_, RecipeCategory p_251154_, ItemLike p_250066_, float p_251871_, int p_251316_, String p_251450_, String p_249236_) {
        for(ItemLike itemlike : p_249619_) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), p_251154_, p_250066_, p_251871_, p_251316_, p_251817_)
                    .group(p_251450_).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(p_250791_, Klux.MODID + ":" + getItemName(p_250066_) + p_249236_ + "_" + getItemName(itemlike));
        }

    }
}
