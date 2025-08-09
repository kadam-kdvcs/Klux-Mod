package org.kdvcs.klux.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.kdvcs.klux.Klux;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.item.custom.*;
import org.kdvcs.klux.sound.ModSounds;

import java.util.List;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Klux.MODID);

    public static final RegistryObject<Item> HUMICS = ITEMS.register("humics",
            () -> new Humics(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_,
                                            List<Component> p_41423_, TooltipFlag p_41424_) {
                    if (Screen.hasShiftDown()) {
                        p_41423_.add(Component.translatable("tooltip.klux.humics.tooltip").withStyle(ChatFormatting.GRAY));
                    } else {
                        p_41423_.add(Component.translatable("tooltip.press").withStyle(ChatFormatting.GRAY).
                                append(Component.keybind("key.shift").withStyle(ChatFormatting.GRAY)
                                .append(Component.translatable("tooltip.look").withStyle(ChatFormatting.GRAY))));
                    }
                    super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
                }
            }
    );

    //APATITE
    public static final RegistryObject<Item> APATITE = ITEMS.register("apatite",
            () -> new Item(new Item.Properties()));

    //AROMATIC RESONATOR
    public static final RegistryObject<Item> AROMATIC_RESONATOR = ITEMS.register("aromatic_resonator",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.aromatic_resonator.tooltip", ChatFormatting.DARK_PURPLE)));

    //WITHERED LEAF
    public static final RegistryObject<Item> WITHERED_LEAF = ITEMS.register("withered_leaf",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.withered_leaf.tooltip")));

    //REINFORCED AROMATIC CRYSTAL PLATE
    public static final RegistryObject<Item> REINFORCED_AROMATIC_CRYSTAL_PLATE = ITEMS.register("reinforced_aromatic_crystal_plate",
            () -> new Item(new Item.Properties()));

    //REDSTONE SILICON BOULE
    public static final RegistryObject<Item> REDSTONE_SILICON_BOULE = ITEMS.register("redstone_silicon_boule",
            () -> new Item(new Item.Properties()));

    //FIERY INGOT
    public static final RegistryObject<Item> FIERY_INGOT = ITEMS.register("fiery_ingot",
            () -> new Item(new Item.Properties()));

    //FIERY GEAR
    public static final RegistryObject<Item> FIERY_GEAR = ITEMS.register("fiery_gear",
            () -> new Item(new Item.Properties()));

    //PYRORED SILICRYSTAL
    public static final RegistryObject<Item> PYRORED_SILICRYSTAL = ITEMS.register("pyrored_silicrystal",
            () -> new Item(new Item.Properties()));

    //ACTIVATION POWDER
    public static final RegistryObject<Item> ACTIVATION_POWDER = ITEMS.register("activation_powder",
            () -> new Item(new Item.Properties()));

    //SLAG LUMP
    public static final RegistryObject<Item> SLAG_LUMP = ITEMS.register("slag_lump",
            () -> new Item(new Item.Properties()));

    //WROUGHT IRON GEAR
    public static final RegistryObject<Item> WROUGHT_IRON_GEAR = ITEMS.register("wrought_iron_gear",
            () -> new Item(new Item.Properties()));

    //REDSTONE CRYSTAL CORE
    public static final RegistryObject<Item> REDSTONE_CRYSTAL_CORE = ITEMS.register("redstone_crystal_core",
            () -> new Item(new Item.Properties()));

    //WROUGHT IRON INGOT
    public static final RegistryObject<Item> WROUGHT_IRON_INGOT = ITEMS.register("wrought_iron_ingot",
            () -> new Item(new Item.Properties()));

    //EXAMPLE TOOL
    public static final RegistryObject<Item> EXAMPLE_TOOL = ITEMS.register("example_tool",
            () -> new Item(new Item.Properties()));

    //PROTEIN_FIBER
    public static final RegistryObject<Item> PROTEIN_FIBER = ITEMS.register("protein_fiber",
            () -> new Item(new Item.Properties()));

    //RAW SILICON
    public static final RegistryObject<Item> RAW_SILICON = ITEMS.register("raw_silicon",
            () -> new Item(new Item.Properties()));

    //SILICON BOULE
    public static final RegistryObject<Item> SILICON_BOULE = ITEMS.register("silicon_boule",
            () -> new Item(new Item.Properties()));

    //ANCIENT COINS
    public static final RegistryObject<Item> ANCIENT_COINS = ITEMS.register("ancient_coins",
            () -> new Item(new Item.Properties()));

    //REINFORCED AROMATIC CRYSTAL GEAR
    public static final RegistryObject<Item> REINFORCED_AROMATIC_CRYSTAL_GEAR = ITEMS.register("reinforced_aromatic_crystal_gear",
            () -> new Item(new Item.Properties()));

    //TRIPLE SEALED TANK
    public static final RegistryObject<Item> TRIPLE_SEALED_TANK = ITEMS.register("triple_sealed_tank",
            () -> new Item(new Item.Properties()));

    //PRECISION SUBSTRATE
    public static final RegistryObject<Item> PRECISION_SUBSTRATE = ITEMS.register("precision_substrate",
            () -> new Item(new Item.Properties()));

    //PECTI BOND
    public static final RegistryObject<Item> PECTI_BOND = ITEMS.register("pecti_bond",
            () -> new Item(new Item.Properties()));

    //SOLV SHELL
    public static final RegistryObject<Item> SOLV_SHELL = ITEMS.register("solv_shell",
            () -> new Item(new Item.Properties()));

    //DISSOLVENT BEARING
    public static final RegistryObject<Item> DISSOLVENT_BEARING = ITEMS.register("dissolvent_bearing",
            () -> new Item(new Item.Properties()));

    //FILTER CORE
    public static final RegistryObject<Item> FILTER_CORE = ITEMS.register("filter_core",
            () -> new Item(new Item.Properties()));

    //HAY BALL
    public static final RegistryObject<Item> HAY_BALL = ITEMS.register("hay_ball",
            () -> new FuelItem(new Item.Properties(),400));

    //BRAN
    public static final RegistryObject<Item> BRAN = ITEMS.register("bran",
            () -> new FuelItem(new Item.Properties(),200));

    //WOVEN SUBSTRATE
    public static final RegistryObject<Item> WOVEN_SUBSTRATE = ITEMS.register("woven_substrate",
            () -> new Item(new Item.Properties()));

    //CARBOHYDRATE POWDER
    public static final RegistryObject<Item> CARBOHYDRATE_POWDER = ITEMS.register("carbohydrate_powder",
            () -> new Item(new Item.Properties()));

    //SCORIA POWDER
    public static final RegistryObject<Item> SCORIA_POWDER = ITEMS.register("scoria_powder",
            () -> new Item(new Item.Properties()));

    //MOLTEN CORE
    public static final RegistryObject<Item> MOLTEN_CORE = ITEMS.register("molten_core",
            () -> new Item(new Item.Properties()));

    //MINERAL POWDER
    public static final RegistryObject<Item> MINERAL_POWDER = ITEMS.register("mineral_powder",
            () -> new Item(new Item.Properties()));

    //PROTEIN CONCENTRATE
    public static final RegistryObject<Item> PROTEIN_CONCENTRATE = ITEMS.register("protein_concentrate",
            () -> new Item(new Item.Properties()));

    //VITAMIN DUST
    public static final RegistryObject<Item> VITAMIN_DUST = ITEMS.register("vitamin_dust",
            () -> new Item(new Item.Properties()));

    //UNIVERSAL FEED
    public static final RegistryObject<Item> UNIVERSAL_FEED = ITEMS.register("universal_feed",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.universal_feed.tooltip", ChatFormatting.GREEN)));

    //AROMA FAMILY
    //AROMATIC POWDER
    public static final RegistryObject<Item> AROMATIC_POWDER = ITEMS.register("aromatic_powder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HERBAL_ACTIVE_POWDER = ITEMS.register("herbal_active_powder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOODY_ESSENCE_POWDER = ITEMS.register("woody_essence_powder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WILDFLOWER_POLLEN_POWDER = ITEMS.register("wildflower_pollen_powder",
            () -> new Item(new Item.Properties()));

    //BOTANICAL RESIN BEAD
    public static final RegistryObject<Item> BOTANICAL_RESIN_BEAD = ITEMS.register("botanical_resin_bead",
            () -> new Item(new Item.Properties()));

    //ADHESIVE PASTE
    public static final RegistryObject<Item> ADHESIVE_PASTE = ITEMS.register("adhesive_paste",
            () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(1)));

    //PRESSING ROD ASSEMBLY
    public static final RegistryObject<Item> PRESSING_ROD_ASSEMBLY = ITEMS.register("pressing_rod_assembly",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.pressing_rod_assembly.tooltip")));

    //FERMENTED_AROMATIC_SLURRY
    public static final RegistryObject<Item> FERMENTED_AROMATIC_SLURRY = ITEMS.register("fermented_aromatic_slurry",
            () -> new Item(new Item.Properties()));

    //AROMATIC COAGULATE
    public static final RegistryObject<Item> AROMATIC_COAGULATE = ITEMS.register("aromatic_coagulate",
            () -> new Item(new Item.Properties()));

    //AROMATIC DUST
    public static final RegistryObject<Item> AROMATIC_DUST = ITEMS.register("aromatic_dust",
            () -> new Item(new Item.Properties()));

    //AROMATIC INGOT
    public static final RegistryObject<Item> AROMATIC_INGOT = ITEMS.register("aromatic_ingot",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.aromatic_ingot.tooltip", ChatFormatting.DARK_PURPLE),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.aromatic_ingot.tooltip2")));

    //CAKE BASE
    public static final RegistryObject<Item> ROUGH_CAKE_BASE = ITEMS.register("rough_cake_base",
            () -> new Item(new Item.Properties().food(ModFoods.ROUGH_CAKE_BASE)));

    //VITAMIN CAKE
    public static final RegistryObject<Item> VITAMIN_CAKE = ITEMS.register("vitamin_cake",
            () -> new ShiftTooltipItemBase(new Item.Properties().food(ModFoods.VITAMIN_CAKE),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.vitamin_cake.tooltip")));

    //APPLES
    public static final RegistryObject<Item> APPLE_COPPER = ITEMS.register("apple_copper",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_COPPER)));
    public static final RegistryObject<Item> APPLE_IRON = ITEMS.register("apple_iron",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_IRON)));
    public static final RegistryObject<Item> APPLE_LAPIS = ITEMS.register("apple_lapis",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_LAPIS)));
    public static final RegistryObject<Item> APPLE_DIAMOND = ITEMS.register("apple_diamond",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_DIAMOND)));
    public static final RegistryObject<Item> APPLE_EMERALD = ITEMS.register("apple_emerald",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_EMERALD)));
    public static final RegistryObject<Item> APPLE_AMETHYST = ITEMS.register("apple_amethyst",
            () -> new Item(new Item.Properties().food(ModFoods.APPLE_AMETHYST)));

    //EXTRACTION MESHES
    public static final RegistryObject<Item> EXTRACTION_MESH = ITEMS.register("extraction_mesh",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ADVANCED_EXTRACTION_MESH = ITEMS.register("advanced_extraction_mesh",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ULTIMATE_EXTRACTION_MESH = ITEMS.register("ultimate_extraction_mesh",
            () -> new Item(new Item.Properties().stacksTo(1)));

    //MULTIPHASE FLUID CONTAINER
    public static final RegistryObject<Item> MULTIPHASE_FLUID_CONTAINER = ITEMS.register("multiphase_fluid_container",
            () -> new MultiphaseFluidContainer(new Item.Properties()));

    //SEALED TANK
    public static final RegistryObject<Item> SEALED_TANK = ITEMS.register("sealed_tank",
            () -> new Item(new Item.Properties()));

    //POLYMER MEMBRANE PLATE
    public static final RegistryObject<Item> POLYMER_MEMBRANE_PLATE = ITEMS.register("polymer_membrane_plate",
            () -> new Item(new Item.Properties()));

    //FLUX CORE
    public static final RegistryObject<Item> FLUX_CORE = ITEMS.register("flux_core",
            () -> new Item(new Item.Properties()));

    //LEATHER PASTE
    public static final RegistryObject<Item> LEATHER_PASTE = ITEMS.register("leather_paste",
            () -> new Item(new Item.Properties()));

    //SPIDER SILK FIBER
    public static final RegistryObject<Item> SPIDER_SILK_FIBER = ITEMS.register("spider_silk_fiber",
            () -> new Item(new Item.Properties()));

    //SOFT ARMOR CLOTH
    public static final RegistryObject<Item> SOFT_ARMOR_CLOTH = ITEMS.register("soft_armor_cloth",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.soft_armor_cloth.tooltip")));

    //CRYSTALS
    public static final RegistryObject<Item> EARTH_CRYSTAL = ITEMS.register("earth_crystal",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FIRE_QUARTZ = ITEMS.register("fire_quartz",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ENDERGON_CRYSTAL = ITEMS.register("endergon_crystal",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHARGED_ENDERGON_CRYSTAL = ITEMS.register("charged_endergon_crystal",
            () -> new Item(new Item.Properties()));

    //MAGIC TOOLS
    public static final RegistryObject<Item> DETECTOR = ITEMS.register("detector",
            () -> new Detector(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> RETURN_SCEPTER = ITEMS.register("return_scepter",
            () -> new ReturnScepter(new Item.Properties().durability(5)));

    //MACHINE PARTS
    public static final RegistryObject<Item> REDSTONE_RESONATOR = ITEMS.register("redstone_resonator",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.redstone_resonator.tooltip", ChatFormatting.DARK_GREEN)));

    public static final RegistryObject<Item> AIR_DUCT = ITEMS.register("air_duct",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FURNACE_CORE = ITEMS.register("furnace_core",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.furnace_core.tooltip", ChatFormatting.DARK_RED)));

    public static final RegistryObject<Item> DEHYDRATED_SEEDS = ITEMS.register("dehydrated_seeds",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.dehydrated_seeds.tooltip")));

    public static final RegistryObject<Item> PUTRID_FAT = ITEMS.register("putrid_fat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROTTEN_FRUIT = ITEMS.register("rotten_fruit",
            () -> new ShiftTooltipItemBase(new Item.Properties(),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.rotten_fruit.tooltip1"),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.rotten_fruit.tooltip2")));

    //FOOD
    public static final RegistryObject<Item> SALAD = ITEMS.register("salad",
            () -> new ShiftTooltipItemBase(new Item.Properties().food(ModFoods.SALAD),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.salad.tooltip")));

    public static final RegistryObject<Item> NUTRI_BLOCK = ITEMS.register("nutri_block",
            () -> new ShiftTooltipItemBase(new Item.Properties().food(ModFoods.NUTRI_BLOCK),
                    new ShiftTooltipItemBase.TooltipLine("tooltip.klux.nutri_block.tooltip")));

    //MUSIC DISC
    public static final RegistryObject<Item> RUMBLE_MUSIC_DISC = ITEMS.register("rumble_music_disc",
            () -> new RecordItem(6, ModSounds.RUMBLE, new Item.Properties().stacksTo(1),3720));
    //MUSIC DISC
    public static final RegistryObject<Item> BIG_MUSIC_MUSIC_DISC = ITEMS.register("big_music_music_disc",
            () -> new RecordItem(6, ModSounds.BIG_MUSIC, new Item.Properties().stacksTo(1),2500));

    //CROP (SEEDS OF SPRING ONION)
    public static final RegistryObject<Item> SPRING_ONION_SEEDS = ITEMS.register("spring_onion_seeds",
            () -> new ItemNameBlockItem(ModBlocks.SPRING_ONION_CROP.get(), new Item.Properties()));

    //CROP (SEEDS OF RICE)
    public static final RegistryObject<Item> RICE_SEEDS = ITEMS.register("rice_seeds",
            () -> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(), new Item.Properties()));

    //SPRING_ONION
    public static final RegistryObject<Item> SPRING_ONION = ITEMS.register("spring_onion",
            () -> new Item(new Item.Properties().food(ModFoods.SPRING_ONION)));

    //RICE
    public static final RegistryObject<Item> RICE = ITEMS.register("rice",
            () -> new Item(new Item.Properties().food(ModFoods.RICE)));

    //HORSE MEAT
    public static final RegistryObject<Item> RAW_HORSE_MEAT = ITEMS.register("raw_horse_meat",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_HORSE_MEAT)));
    public static final RegistryObject<Item> COOKED_HORSE_MEAT = ITEMS.register("cooked_horse_meat",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_HORSE_MEAT)));

    public static final RegistryObject<Item> ROTTEN_FRUIT_SEEDS = ITEMS.register("rotten_fruit_seeds",
            () -> new ItemNameBlockItem(ModBlocks.ROTTEN_FRUIT_CROP.get(), new Item.Properties()));

    //MATERIALS
    public static final RegistryObject<Item> ENRICHED_COAL = ITEMS.register("enriched_coal",
            () -> new FuelItem(new Item.Properties(), 12800){
                @Override
                public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
                    p_41423_.add(Component.translatable("tooltip.klux.enriched_coal.tooltip").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
                }
            });

    //p43270(Damage), p43271(Speed)
    public static final RegistryObject<Item> EARTH_CRYSTAL_SWORD = ITEMS.register("earth_crystal_sword",
            () -> new SwordItem(ModToolTiers.EARTH_CRYSTAL,4,-2, new Item.Properties()));

    public static final RegistryObject<Item> EARTH_CRYSTAL_PICKAXE = ITEMS.register("earth_crystal_pickaxe",
            () -> new PickaxeItem(ModToolTiers.EARTH_CRYSTAL,2,-2.4f, new Item.Properties()));

    public static final RegistryObject<Item> EARTH_CRYSTAL_AXE = ITEMS.register("earth_crystal_axe",
            () -> new AxeItem(ModToolTiers.EARTH_CRYSTAL,13,-3.2f, new Item.Properties()));

    public static final RegistryObject<Item> EARTH_CRYSTAL_SHOVEL = ITEMS.register("earth_crystal_shovel",
            () -> new ShovelItem(ModToolTiers.EARTH_CRYSTAL,0,-3, new Item.Properties()));

    public static final RegistryObject<Item> EARTH_CRYSTAL_HOE = ITEMS.register("earth_crystal_hoe",
            () -> new HoeItem(ModToolTiers.EARTH_CRYSTAL,0,-3, new Item.Properties()));

    //HELMETS
    public static final RegistryObject<Item> EARTH_CRYSTAL_HELMET = ITEMS.register("earth_crystal_helmet",
            () -> new ModArmorItem(ModArmorMaterials.EARTH_CRYSTAL, ArmorItem.Type.HELMET, new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
                    if (Screen.hasShiftDown()) {
                        p_41423_.add(Component.translatable("tooltip.klux.earth_crystal_helmet.tooltip")
                                .withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC));
                    } else {
                        p_41423_.add(Component.translatable("tooltip.press").append(Component.keybind("key.shift")
                                .append(Component.translatable("tooltip.look").withStyle(ChatFormatting.GRAY))));
                    }
                    super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
                }
            });

    public static final RegistryObject<Item> EARTH_CRYSTAL_CHESTPLATE = ITEMS.register("earth_crystal_chestplate",
            () -> new ArmorItem(ModArmorMaterials.EARTH_CRYSTAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> EARTH_CRYSTAL_LEGGINGS = ITEMS.register("earth_crystal_leggings",
            () -> new ArmorItem(ModArmorMaterials.EARTH_CRYSTAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> EARTH_CRYSTAL_BOOTS = ITEMS.register("earth_crystal_boots",
            () -> new ArmorItem(ModArmorMaterials.EARTH_CRYSTAL, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> SOFT_HELMET = ITEMS.register("soft_helmet",
            () -> new ArmorItem(ModArmorMaterials.SOFT_CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SOFT_CHESTPLATE = ITEMS.register("soft_chestplate",
            () -> new ArmorItem(ModArmorMaterials.SOFT_CLOTH, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SOFT_LEGGINGS = ITEMS.register("soft_leggings",
            () -> new ArmorItem(ModArmorMaterials.SOFT_CLOTH, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SOFT_BOOTS = ITEMS.register("soft_boots",
            () -> new ArmorItem(ModArmorMaterials.SOFT_CLOTH, ArmorItem.Type.BOOTS, new Item.Properties()));

    //AROMATIC BUCKET
    public static final RegistryObject<Item> AROMATIC_BUCKET = ITEMS.register("aromatic_bucket",
            () -> new BucketItem(ModFluids.SOURCE_AROMATIC,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //PUTRESCENT
    public static final RegistryObject<Item> PUTRESCENT_SOLUTION_BUCKET = ITEMS.register("putrescent_solution_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PUTRESCENT_SOLUTION,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //PECTIN SLURRY
    public static final RegistryObject<Item> PECTIN_SLURRY_BUCKET = ITEMS.register("pectin_slurry_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PECTIN_SLURRY,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //PRIMAL ESSENCE BUCKET
    public static final RegistryObject<Item> PRIMAL_ESSENCE_BUCKET = ITEMS.register("primal_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PRIMAL_ESSENCE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //ENZYME SOLUTION BUCKET
    public static final RegistryObject<Item> ENZYME_SOLUTION_BUCKET = ITEMS.register("enzyme_solution_bucket",
            () -> new BucketItem(ModFluids.SOURCE_ENZYME_SOLUTION,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //BOTANIC ESSENTIAL OIL BUCKET
    public static final RegistryObject<Item> BOTANIC_ESSENTIAL_OIL_BUCKET = ITEMS.register("botanic_essential_oil_bucket",
            () -> new BucketItem(ModFluids.SOURCE_BOTANIC_ESSENTIAL_OIL,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //MINERAL SLURRY BUCKET
    public static final RegistryObject<Item> MINERAL_SLURRY_BUCKET = ITEMS.register("mineral_slurry_bucket",
            () -> new BucketItem(ModFluids.SOURCE_MINERAL_SLURRY,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //REPAIR BUCKET
    public static final RegistryObject<Item> REPAIR_BUCKET = ITEMS.register("repair_bucket",
            () -> new BucketItem(ModFluids.SOURCE_REPAIR,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //HERE TO REGISTER ALL ITEMS
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
