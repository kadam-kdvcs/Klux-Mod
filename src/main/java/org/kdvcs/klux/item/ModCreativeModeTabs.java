package org.kdvcs.klux.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Klux.MODID);

    public static final RegistryObject<CreativeModeTab> KLUX_BASIC = CREATIVE_MODE_TABS.register("klux_basic",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EARTH_CRYSTAL.get()))
                    .title(Component.translatable("itemGroup.klux_basic"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.EARTH_CRYSTAL.get());
                        output.accept(ModItems.FIRE_QUARTZ.get());
                        output.accept(ModItems.ENDERGON_CRYSTAL.get());

                        output.accept(ModBlocks.EARTH_CRYSTAL_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get());
                        output.accept(ModBlocks.FIRE_QUARTZ_ORE.get());
                        output.accept(ModBlocks.ENDERGON_CRYSTAL_ORE.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_BLOCK.get());

                        output.accept(ModBlocks.HAY_BRICK.get());
                        output.accept(ModItems.SALAD.get());
                        output.accept(ModItems.NUTRI_BLOCK.get());
                        output.accept(ModItems.PARSNIP.get());
                        output.accept(ModItems.PARSNIP_SEEDS.get());
                        output.accept(ModItems.SPRING_ONION.get());
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
                        output.accept(ModItems.SPIDER_SILK_FIBER.get());
                        output.accept(ModItems.SOFT_ARMOR_CLOTH.get());
                        output.accept(ModItems.CARBOHYDRATE_POWDER.get());
                        output.accept(ModItems.MINERAL_POWDER.get());
                        output.accept(ModItems.PROTEIN_CONCENTRATE.get());
                        output.accept(ModItems.VITAMIN_DUST.get());
                        output.accept(ModItems.UNIVERSAL_FEED.get());
                        output.accept(ModItems.AROMATIC_POWDER.get());
                        output.accept(ModItems.BOTANICAL_RESIN_BEAD.get());
                        output.accept(ModItems.ADHESIVE_PASTE.get());
                        output.accept(ModItems.PRESSING_ROD_ASSEMBLY.get());
                        output.accept(ModItems.EXTRACTION_MESH.get());
                        output.accept(ModItems.FERMENTED_AROMATIC_SLURRY.get());
                        output.accept(ModItems.AROMATIC_COAGULATE.get());
                        output.accept(ModItems.AROMATIC_DUST.get());
                        output.accept(ModItems.AROMATIC_INGOT.get());

                            })
                    .build());

    public static final RegistryObject<CreativeModeTab> KLUX_MACHINES = CREATIVE_MODE_TABS.register("klux_machines",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.COMPRESSOR.get()))
                    .title(Component.translatable("itemGroup.klux_machines"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.COMPRESSOR.get());
                        output.accept(ModBlocks.SEED_MAKER.get());
                        output.accept(ModBlocks.DEHYDRATOR.get());
                        output.accept(ModBlocks.EXTRACTOR.get());
                        output.accept(ModBlocks.EARTH_CRYSTAL_FRAME.get());
                        output.accept(ModItems.REDSTONE_RESONATOR.get());
                        output.accept(ModItems.FURNACE_CORE.get());
                        output.accept(ModItems.AIR_DUCT.get());

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

                    })
                    .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
