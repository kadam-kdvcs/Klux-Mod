package org.kdvcs.klux.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.block.custom.ParsnipCropBlock;
import org.kdvcs.klux.block.custom.RottenFruitCropBlock;
import org.kdvcs.klux.block.custom.SpringOnionCropBlock;
import org.kdvcs.klux.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.COMPRESSOR.get());
        this.dropSelf(ModBlocks.SOUND_BLOCK.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_BLOCK.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_FRAME.get());
        this.dropSelf(ModBlocks.DEHYDRATOR.get());
        this.dropSelf(ModBlocks.HAY_BRICK.get());
        this.dropSelf(ModBlocks.EXTRACTOR.get());
        this.dropSelf(ModBlocks.FLUID_ASSEMBLER.get());
        this.dropSelf(ModBlocks.FLUID_EXTRACTOR.get());
        this.dropSelf(ModBlocks.MULTIPHASE_FLUID_TANK.get());
        this.dropSelf(ModBlocks.FLUX_SYNTHESIZER.get());
        this.dropSelf(ModBlocks.FIRE_QUARTZ_FRAME.get());
        this.dropSelf(ModBlocks.LIQUID_REACTOR.get());
        this.dropSelf(ModBlocks.LIQUID_FILTER.get());

        this.dropSelf(ModBlocks.IRON_SAND.get());

        this.dropSelf(ModBlocks.DRIED_BRICK_SMOOTH.get());
        this.dropSelf(ModBlocks.DRIED_BRICK.get());
        this.dropSelf(ModBlocks.SNOW_BRICK.get());
        this.dropSelf(ModBlocks.WET_BRICK.get());

        this.dropSelf(ModBlocks.PINE_LOG.get());
        this.dropSelf(ModBlocks.PINE_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_PINE_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_PINE_WOOD.get());
        this.dropSelf(ModBlocks.PINE_PLANKS.get());
        this.dropSelf(ModBlocks.PINE_SAPLING.get());

        this.add(ModBlocks.PINE_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.PINE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));


        this.dropSelf(ModBlocks.HAY_SLAB.get());
        this.dropSelf(ModBlocks.HAY_STAIRS.get());
        this.dropSelf(ModBlocks.HAY_WALL.get());

        this.dropSelf(ModBlocks.ICE_BRICK.get());
        this.dropSelf(ModBlocks.SANDSTONE_CLEAN.get());
        this.dropSelf(ModBlocks.SANDSTONE_CARVED.get());

        //ORES
        this.add(ModBlocks.EARTH_CRYSTAL_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.EARTH_CRYSTAL_ORE.get(), ModItems.EARTH_CRYSTAL.get()));
        this.add(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get(), ModItems.EARTH_CRYSTAL.get()));
        this.add(ModBlocks.FIRE_QUARTZ_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.FIRE_QUARTZ_ORE.get(), ModItems.FIRE_QUARTZ.get()));
        this.add(ModBlocks.ENDERGON_CRYSTAL_ORE.get(),
                block -> createCopperLikeOreDrops(ModBlocks.ENDERGON_CRYSTAL_ORE.get(), ModItems.ENDERGON_CRYSTAL.get()));


        this.dropSelf(ModBlocks.EARTH_CRYSTAL_STAIRS.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_BUTTON.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_FENCE.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_FENCE_GATE.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_TRAPDOOR.get());
        this.dropSelf(ModBlocks.EARTH_CRYSTAL_WALL.get());

        this.add(ModBlocks.EARTH_CRYSTAL_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.EARTH_CRYSTAL_SLAB.get()));
        this.add(ModBlocks.EARTH_CRYSTAL_DOOR.get(),
                block -> createDoorTable(ModBlocks.EARTH_CRYSTAL_DOOR.get()));

        //PARSNIP CROPBLOCK (THIS IS A COMMON CROPBLOCK)
        LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.PARSNIP_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ParsnipCropBlock.AGE,3));

        this.add(ModBlocks.PARSNIP_CROP.get(), createCropDrops(ModBlocks.PARSNIP_CROP.get(), ModItems.PARSNIP.get(),
                ModItems.PARSNIP_SEEDS.get(), lootitemcondition$builder));

        //SPRING ONION CROPBLOCK (THIS CROPBLOCK IS 2 BLOCKS HIGH)
        LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.SPRING_ONION_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SpringOnionCropBlock.AGE,7))
                .or(LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(ModBlocks.SPRING_ONION_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SpringOnionCropBlock.AGE,8)));

        this.add(ModBlocks.SPRING_ONION_CROP.get(), createCropDrops(ModBlocks.SPRING_ONION_CROP.get(), ModItems.SPRING_ONION.get(),
                ModItems.SPRING_ONION_SEEDS.get(), lootitemcondition$builder2));

        //ROTTEN FRUIT CROPBLOCK
        LootItemCondition.Builder lootitemcondition$builder3 = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.ROTTEN_FRUIT_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RottenFruitCropBlock.AGE,3));

        this.add(ModBlocks.ROTTEN_FRUIT_CROP.get(), createCropDrops(ModBlocks.ROTTEN_FRUIT_CROP.get(), ModItems.ROTTEN_FRUIT.get(),
                ModItems.DEHYDRATED_SEEDS.get(), lootitemcondition$builder3));


        //FLOWER POT BLOCKS
        this.dropSelf(ModBlocks.CACTUS_FRUIT.get());
        this.add(ModBlocks.POTTED_CACTUS_FRUIT.get(), createPotFlowerItemTable(ModBlocks.CACTUS_FRUIT.get()));
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block p_251306_, Item item) {
        return createSilkTouchDispatchTable(p_251306_,
                this.applyExplosionDecay(p_251306_,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
