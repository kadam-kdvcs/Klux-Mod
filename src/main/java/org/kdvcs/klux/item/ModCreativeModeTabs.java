package org.kdvcs.klux.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.potion.ModPotions;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Klux.MODID);

    public static final RegistryObject<CreativeModeTab> KLUX_BASIC = CREATIVE_MODE_TABS.register("klux_basic",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EARTH_CRYSTAL.get()))
                    .title(Component.translatable("itemGroup.klux_basic"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.PINE_LOG.get());
                        output.accept(ModBlocks.PINE_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_PINE_LOG.get());
                        output.accept(ModBlocks.STRIPPED_PINE_WOOD.get());

                        output.accept(ModBlocks.PINE_PLANKS.get());
                        output.accept(ModBlocks.PINE_LEAVES.get());

                        output.accept(ModBlocks.PINE_SAPLING.get());

                        output.accept(ModBlocks.EARTH_CRYSTAL_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get());
                        output.accept(ModBlocks.FIRE_QUARTZ_ORE.get());
                        output.accept(ModBlocks.ENDERGON_CRYSTAL_ORE.get());

                        output.accept(ModItems.EARTH_CRYSTAL.get());
                        output.accept(ModItems.FIRE_QUARTZ.get());
                        output.accept(ModItems.ENDERGON_CRYSTAL.get());
                        output.accept(ModItems.CHARGED_ENDERGON_CRYSTAL.get());

                        output.accept(ModBlocks.EARTH_CRYSTAL_BLOCK.get());

                        output.accept(ModBlocks.HAY_BRICK.get());
                        output.accept(ModBlocks.HAY_STAIRS.get());
                        output.accept(ModBlocks.HAY_SLAB.get());
                        output.accept(ModBlocks.HAY_WALL.get());
                        output.accept(ModBlocks.IRON_SAND.get());

                        output.accept(ModBlocks.ICE_BRICK.get());
                        output.accept(ModBlocks.SANDSTONE_CLEAN.get());
                        output.accept(ModBlocks.SANDSTONE_CARVED.get());

                        output.accept(ModBlocks.DRIED_BRICK.get());
                        output.accept(ModBlocks.DRIED_BRICK_SMOOTH.get());
                        output.accept(ModBlocks.SNOW_BRICK.get());
                        output.accept(ModBlocks.WET_BRICK.get());

                        output.accept(ModItems.PARSNIP_SEEDS.get());

                        output.accept(ModItems.SPRING_ONION_SEEDS.get());
                        output.accept(ModBlocks.CACTUS_FRUIT.get());

                        output.accept(ModItems.ENRICHED_COAL.get());
                        output.accept(ModItems.HUMICS.get());
                        output.accept(ModItems.DEHYDRATED_SEEDS.get());
                        output.accept(ModItems.PUTRID_FAT.get());
                        output.accept(ModItems.ROTTEN_FRUIT.get());
                        output.accept(ModItems.ROTTEN_FRUIT_SEEDS.get());

                        output.accept(ModItems.HAY_BALL.get());
                        output.accept(ModItems.LEATHER_PASTE.get());

                        //FIBERS
                        output.accept(ModItems.SPIDER_SILK_FIBER.get());
                        output.accept(ModItems.PROTEIN_FIBER.get());

                        output.accept(ModItems.SOFT_ARMOR_CLOTH.get());
                        output.accept(ModItems.CARBOHYDRATE_POWDER.get());
                        output.accept(ModItems.MINERAL_POWDER.get());
                        output.accept(ModItems.PROTEIN_CONCENTRATE.get());
                        output.accept(ModItems.VITAMIN_DUST.get());
                        output.accept(ModItems.UNIVERSAL_FEED.get());

                        //AROMA FAMILY
                        output.accept(ModItems.AROMATIC_POWDER.get());
                        output.accept(ModItems.HERBAL_ACTIVE_POWDER.get());
                        output.accept(ModItems.WOODY_ESSENCE_POWDER.get());
                        output.accept(ModItems.WILDFLOWER_POLLEN_POWDER.get());

                        output.accept(ModItems.BOTANICAL_RESIN_BEAD.get());
                        output.accept(ModItems.ADHESIVE_PASTE.get());
                        output.accept(ModItems.PRESSING_ROD_ASSEMBLY.get());

                        //EXTRACTION MESHES
                        output.accept(ModItems.EXTRACTION_MESH.get());
                        output.accept(ModItems.ADVANCED_EXTRACTION_MESH.get());
                        output.accept(ModItems.ULTIMATE_EXTRACTION_MESH.get());

                        output.accept(ModItems.FERMENTED_AROMATIC_SLURRY.get());
                        output.accept(ModItems.AROMATIC_COAGULATE.get());
                        output.accept(ModItems.AROMATIC_DUST.get());
                        output.accept(ModItems.PECTI_BOND.get());
                        output.accept(ModItems.AROMATIC_INGOT.get());
                        output.accept(ModItems.WROUGHT_IRON_INGOT.get());
                        output.accept(ModItems.WROUGHT_IRON_GEAR.get());
                        output.accept(ModItems.SLAG_LUMP.get());

                        //LIQUID BUCKETS
                        output.accept(ModItems.AROMATIC_BUCKET.get());
                        output.accept(ModItems.PUTRESCENT_SOLUTION_BUCKET.get());
                        output.accept(ModItems.PECTIN_SLURRY_BUCKET.get());
                        output.accept(ModItems.PRIMAL_ESSENCE_BUCKET.get());
                        output.accept(ModItems.ENZYME_SOLUTION_BUCKET.get());
                        output.accept(ModItems.BOTANIC_ESSENTIAL_OIL_BUCKET.get());
                        output.accept(ModItems.MINERAL_SLURRY_BUCKET.get());
                        output.accept(ModItems.REPAIR_BUCKET.get());

                        output.accept(ModItems.REDSTONE_RESONATOR.get());
                        output.accept(ModItems.AROMATIC_RESONATOR.get());

                        output.accept(ModItems.FURNACE_CORE.get());
                        output.accept(ModItems.AIR_DUCT.get());
                        output.accept(ModItems.MULTIPHASE_FLUID_CONTAINER.get());

                        output.accept(ModItems.POLYMER_MEMBRANE_PLATE.get());
                        output.accept(ModItems.REINFORCED_AROMATIC_CRYSTAL_PLATE.get());
                        output.accept(ModItems.PRECISION_SUBSTRATE.get());
                        output.accept(ModItems.SOLV_SHELL.get());
                        output.accept(ModItems.DISSOLVENT_BEARING.get());
                        output.accept(ModItems.FILTER_CORE.get());

                        output.accept(ModItems.REINFORCED_AROMATIC_CRYSTAL_GEAR.get());

                        output.accept(ModItems.FLUX_CORE.get());
                        output.accept(ModItems.SCORIA_POWDER.get());
                        output.accept(ModItems.MOLTEN_CORE.get());
                        output.accept(ModItems.ANCIENT_COINS.get());
                        output.accept(ModItems.REDSTONE_CRYSTAL_CORE.get());
                        output.accept(ModItems.RAW_SILICON.get());
                        output.accept(ModItems.SILICON_BOULE.get());

                        output.accept(ModItems.SEALED_TANK.get());
                        output.accept(ModItems.TRIPLE_SEALED_TANK.get());

                        output.accept(ModItems.WITHERED_LEAF.get());
                        output.accept(ModItems.BRAN.get());

                        output.accept(ModItems.WOVEN_SUBSTRATE.get());

                            })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_MACHINES = CREATIVE_MODE_TABS.register("klux_machines",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.COMPRESSOR.get()))
                    .title(Component.translatable("itemGroup.klux_machines"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.COMPRESSOR.get());
                        output.accept(ModBlocks.DEHYDRATOR.get());
                        output.accept(ModBlocks.EXTRACTOR.get());
                        output.accept(ModBlocks.FLUID_ASSEMBLER.get());
                        output.accept(ModBlocks.FLUID_EXTRACTOR.get());
                        output.accept(ModBlocks.MULTIPHASE_FLUID_TANK.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_FRAME.get());

                        output.accept(ModBlocks.FLUX_SYNTHESIZER.get());
                        output.accept(ModBlocks.LIQUID_REACTOR.get());
                        output.accept(ModBlocks.UNIVERSAL_REPAIRER.get());
                        output.accept(ModBlocks.LIQUID_FILTER.get());
                        output.accept(ModBlocks.FIRE_QUARTZ_FRAME.get());

                            })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_FOODS = CREATIVE_MODE_TABS.register("klux_foods",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VITAMIN_CAKE.get()))
                    .title(Component.translatable("itemGroup.klux_foods"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.PARSNIP.get());
                        output.accept(ModItems.SPRING_ONION.get());
                        output.accept(ModItems.SALAD.get());
                        output.accept(ModItems.NUTRI_BLOCK.get());
                        output.accept(ModItems.ROUGH_CAKE_BASE.get());
                        output.accept(ModItems.VITAMIN_CAKE.get());

                        //APPLES
                        output.accept(ModItems.APPLE_COPPER.get());
                        output.accept(ModItems.APPLE_IRON.get());
                        output.accept(ModItems.APPLE_LAPIS.get());
                        output.accept(ModItems.APPLE_DIAMOND.get());
                        output.accept(ModItems.APPLE_EMERALD.get());
                        output.accept(ModItems.APPLE_AMETHYST.get());

                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_ARMORS_TOOLS = CREATIVE_MODE_TABS.register("klux_armors_tools",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SOFT_CHESTPLATE.get()))
                    .title(Component.translatable("itemGroup.klux_armors_tools"))
                    .displayItems((itemDisplayParameters, output) -> {

                        //ARMORS
                        //EARTH CRYSTAL ARMOR
                        output.accept(ModItems.EARTH_CRYSTAL_HELMET.get());
                        output.accept(ModItems.EARTH_CRYSTAL_CHESTPLATE.get());
                        output.accept(ModItems.EARTH_CRYSTAL_LEGGINGS.get());
                        output.accept(ModItems.EARTH_CRYSTAL_BOOTS.get());

                        //SOFT ARMOR
                        output.accept(ModItems.SOFT_HELMET.get());
                        output.accept(ModItems.SOFT_CHESTPLATE.get());
                        output.accept(ModItems.SOFT_LEGGINGS.get());
                        output.accept(ModItems.SOFT_BOOTS.get());

                        //Tools
                        output.accept(ModItems.EARTH_CRYSTAL_SWORD.get());
                        output.accept(ModItems.EARTH_CRYSTAL_SHOVEL.get());
                        output.accept(ModItems.EARTH_CRYSTAL_HOE.get());
                        output.accept(ModItems.EARTH_CRYSTAL_PICKAXE.get());
                        output.accept(ModItems.EARTH_CRYSTAL_AXE.get());
                        output.accept(ModItems.RETURN_SCEPTER.get());

                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_MISC = CREATIVE_MODE_TABS.register("klux_misc",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.EARTH_CRYSTAL_FENCE.get()))
                    .title(Component.translatable("itemGroup.klux_misc"))
                    .displayItems((itemDisplayParameters, output) -> {

                        //Functional
                        output.accept(ModBlocks.EARTH_CRYSTAL_BUTTON.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_DOOR.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_FENCE.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_FENCE_GATE.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_PRESSURE_PLATE.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_SLAB.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_STAIRS.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_WALL.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_TRAPDOOR.get());

                        output.accept(ModBlocks.SOUND_BLOCK.get());
                        output.accept(ModItems.RUMBLE_MUSIC_DISC.get());
                        output.accept(ModItems.BIG_MUSIC_MUSIC_DISC.get());

                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_POTIONS = CREATIVE_MODE_TABS.register("klux_potions",
            () -> CreativeModeTab.builder().icon(() -> {
                ItemStack stack = new ItemStack(Items.POTION);
                PotionUtils.setPotion(stack, ModPotions.BURN_POTION.get());
                return stack;
            })
                    .title(Component.translatable("itemGroup.klux_potions"))
                    .displayItems((itemDisplayParameters, output) -> {

                        //BASE
                        ItemStack basePotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(basePotionStack, ModPotions.BASE_POTION.get());
                        output.accept(basePotionStack);

                        ItemStack baseSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(baseSplashPotionStack, ModPotions.BASE_POTION.get());
                        output.accept(baseSplashPotionStack);

                        ItemStack baseLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(baseLingeringPotionStack, ModPotions.BASE_POTION.get());
                        output.accept(baseLingeringPotionStack);

                        //BURN
                        //COMMON
                        ItemStack burnPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(burnPotionStack, ModPotions.BURN_POTION.get());
                        output.accept(burnPotionStack);

                        ItemStack longBurnPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(longBurnPotionStack, ModPotions.LONG_BURN_POTION.get());
                        output.accept(longBurnPotionStack);

                        ItemStack strongBurnPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(strongBurnPotionStack, ModPotions.STRONG_BURN_POTION.get());
                        output.accept(strongBurnPotionStack);

                        //SPLASH
                        ItemStack burnSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(burnSplashPotionStack, ModPotions.BURN_POTION.get());
                        output.accept(burnSplashPotionStack);

                        ItemStack longBurnSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(longBurnSplashPotionStack, ModPotions.LONG_BURN_POTION.get());
                        output.accept(longBurnSplashPotionStack);

                        ItemStack strongBurnSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(strongBurnSplashPotionStack, ModPotions.STRONG_BURN_POTION.get());
                        output.accept(strongBurnSplashPotionStack);

                        //LINGERING
                        ItemStack burnLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(burnLingeringPotionStack, ModPotions.BURN_POTION.get());
                        output.accept(burnLingeringPotionStack);

                        ItemStack longBurnLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(longBurnLingeringPotionStack, ModPotions.LONG_BURN_POTION.get());
                        output.accept(longBurnLingeringPotionStack);

                        ItemStack strongBurnLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(strongBurnLingeringPotionStack, ModPotions.STRONG_BURN_POTION.get());
                        output.accept(strongBurnLingeringPotionStack);

                        //PURIFICATION
                        //COMMON
                        ItemStack purificationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(purificationPotionStack, ModPotions.PURIFICATION_POTION.get());
                        output.accept(purificationPotionStack);

                        ItemStack longPurificationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(longPurificationPotionStack, ModPotions.LONG_PURIFICATION_POTION.get());
                        output.accept(longPurificationPotionStack);

                        ItemStack strongPurificationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(strongPurificationPotionStack, ModPotions.STRONG_PURIFICATION_POTION.get());
                        output.accept(strongPurificationPotionStack);

                        //SPLASH
                        ItemStack purificationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(purificationSplashPotionStack, ModPotions.PURIFICATION_POTION.get());
                        output.accept(purificationSplashPotionStack);

                        ItemStack longPurificationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(longPurificationSplashPotionStack, ModPotions.LONG_PURIFICATION_POTION.get());
                        output.accept(longPurificationSplashPotionStack);

                        ItemStack strongPurificationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(strongPurificationSplashPotionStack, ModPotions.STRONG_PURIFICATION_POTION.get());
                        output.accept(strongPurificationSplashPotionStack);

                        //LINGERING
                        ItemStack purificationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(purificationLingeringPotionStack, ModPotions.PURIFICATION_POTION.get());
                        output.accept(purificationLingeringPotionStack);

                        ItemStack longPurificationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(longPurificationLingeringPotionStack, ModPotions.LONG_PURIFICATION_POTION.get());
                        output.accept(longPurificationLingeringPotionStack);

                        ItemStack strongPurificationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(strongPurificationLingeringPotionStack, ModPotions.STRONG_PURIFICATION_POTION.get());
                        output.accept(strongPurificationLingeringPotionStack);

                        //SATURATION
                        //COMMON
                        ItemStack saturationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(saturationPotionStack, ModPotions.SATURATION_POTION.get());
                        output.accept(saturationPotionStack);

                        ItemStack longSaturationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(longSaturationPotionStack, ModPotions.LONG_SATURATION_POTION.get());
                        output.accept(longSaturationPotionStack);

                        ItemStack strongSaturationPotionStack = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(strongSaturationPotionStack, ModPotions.STRONG_SATURATION_POTION.get());
                        output.accept(strongSaturationPotionStack);

                        //LONG
                        ItemStack saturationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(saturationSplashPotionStack, ModPotions.SATURATION_POTION.get());
                        output.accept(saturationSplashPotionStack);

                        ItemStack longSaturationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(longSaturationSplashPotionStack, ModPotions.LONG_SATURATION_POTION.get());
                        output.accept(longSaturationSplashPotionStack);

                        ItemStack strongSaturationSplashPotionStack = new ItemStack(Items.SPLASH_POTION);
                        PotionUtils.setPotion(strongSaturationSplashPotionStack, ModPotions.STRONG_SATURATION_POTION.get());
                        output.accept(strongSaturationSplashPotionStack);

                        //LINGERING
                        ItemStack saturationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(saturationLingeringPotionStack, ModPotions.SATURATION_POTION.get());
                        output.accept(saturationLingeringPotionStack);

                        ItemStack longSaturationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(longSaturationLingeringPotionStack, ModPotions.LONG_SATURATION_POTION.get());
                        output.accept(longSaturationLingeringPotionStack);

                        ItemStack strongSaturationLingeringPotionStack = new ItemStack(Items.LINGERING_POTION);
                        PotionUtils.setPotion(strongSaturationLingeringPotionStack, ModPotions.STRONG_SATURATION_POTION.get());
                        output.accept(strongSaturationLingeringPotionStack);

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
