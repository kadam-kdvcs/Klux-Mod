package org.kdvcs.klux.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.loot.AddItemModifier;
import org.kdvcs.klux.loot.AddSusSandItemModifier;
import org.kdvcs.klux.loot.condition.BlockTagLootCondition;

import java.util.List;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Klux.MODID);
    }

    @Override
    protected void start() {
        /*add("salad_from_grass", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.4f).build()}, ModItems.EARTH_CRYSTAL.get()));

        add("salad_from_villager", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/villager")).build()
                }, ModItems.SALAD.get()));

        */

        add("raw_horse_meat_from_horse", new AddItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("entities/horse")).build()
                },
                ModItems.RAW_HORSE_MEAT.get(),
                1.0f,
                1,
                2,
                1
        ));

        add("enriched_coal_from_desert_pyramid", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/desert_pyramid")).build()
        }, ModItems.ENRICHED_COAL.get(),0.15f, 1, 3));

        //EMERALD APPLES
        add("emerald_apple_from_village_weaponsmith", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/village_weaponsmith")).build()
        }, ModItems.APPLE_EMERALD.get(),0.04f, 1, 1));

        add("emerald_apple_from_village_armorer", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/village_armorer")).build()
        }, ModItems.APPLE_EMERALD.get(),0.05f, 1, 1));

        add("emerald_apple_from_desert_pyramid", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/desert_pyramid")).build()
        }, ModItems.APPLE_EMERALD.get(),0.09f, 1, 2));

        add("emerald_apple_from_woodland_mansion", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/woodland_mansion")).build()
        }, ModItems.APPLE_EMERALD.get(),1f, 3, 6));

        add("emerald_apple_from_end_city_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/end_city_treasure")).build()
        }, ModItems.APPLE_EMERALD.get(),0.4f, 2, 3));

        add("emerald_apple_from_pillager_outpost", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/pillager_outpost")).build()
        }, ModItems.APPLE_EMERALD.get(),0.2f, 1, 2));

        //AMETHYST APPLES
        add("amethyst_apple_from_buried_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/buried_treasure")).build()
        }, ModItems.APPLE_AMETHYST.get(),1f, 2, 4));

        add("amethyst_apple_from_underwater_ruin_big", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/underwater_ruin_big")).build()
        }, ModItems.APPLE_AMETHYST.get(),0.7f, 1, 3));

        add("amethyst_apple_from_bastion_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_treasure")).build()
        }, ModItems.APPLE_AMETHYST.get(),0.5f, 1, 2));

        //ROTTEN FRUIT SEEDS
        add("rotten_fruit_seeds_from_desert_pyramid", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/desert_pyramid")).build()
        }, ModItems.ROTTEN_FRUIT_SEEDS.get(),0.11f, 1, 3));

        add("rotten_fruit_seeds_from_jungle_temple", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple")).build()
        }, ModItems.ROTTEN_FRUIT_SEEDS.get(),0.26f, 2, 4));

        add("rotten_fruit_seeds_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/ancient_city")).build()
        }, ModItems.ROTTEN_FRUIT_SEEDS.get(),0.32f, 3, 5));

        add("rotten_fruit_seeds_from_pillager_outpost", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/pillager_outpost")).build()
        }, ModItems.ROTTEN_FRUIT_SEEDS.get(),0.14f, 1, 3));

        add("rotten_fruit_seeds_from_ruined_portal", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/ruined_portal")).build()
        }, ModItems.ROTTEN_FRUIT_SEEDS.get(),0.2f, 2, 4));

        //ENRICHED COAL
        add("enriched_coal_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build()
        }, ModItems.ENRICHED_COAL.get()));

        //RICE SEEDS
        add("rice_seeds_from_grass", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.15f).build()}, ModItems.RICE_SEEDS.get()));

        //APATITE
        add("apatite_from_bones_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CALCITE).build(),
                LootItemRandomChanceCondition.randomChance(0.4f).build()}, ModItems.APATITE.get()));

//        add("spring_onion_seeds_from_grass", new AddItemModifier(new LootItemCondition[] {
//                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
//                LootItemRandomChanceCondition.randomChance(0.25f).build()}, ModItems.SPRING_ONION_SEEDS.get()));

        add("withered_leaf_from_leaves", new AddItemModifier(new LootItemCondition[] {
                new BlockTagLootCondition(TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "leaves"))),
                LootItemRandomChanceCondition.randomChance(0.14f).build()
        }, ModItems.WITHERED_LEAF.get()));

        add("raw_silicon_from_sand", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SAND).build(),
                LootItemRandomChanceCondition.randomChance(0.34f).build()}, ModItems.RAW_SILICON.get()));

    }
}
