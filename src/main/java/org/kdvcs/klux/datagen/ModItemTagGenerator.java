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

    public static final TagKey<Item> FORGE_ORES = TagKey.create(Registries.ITEM, new ResourceLocation("forge","ores"));

    public static final TagKey<Item> FORGE_SEEDS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "seeds"));

    public static final TagKey<Item> FORGE_RAWMEATS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "rawmeats"));

    public static final TagKey<Item> FORGE_GEMS = TagKey.create(Registries.ITEM, new ResourceLocation("forge","gems"));

    public static final TagKey<Item> FORGE_INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation("forge","ingots"));

    public static final TagKey<Item> FORGE_DUSTS = TagKey.create(Registries.ITEM, new ResourceLocation("forge","dusts"));

    public static final TagKey<Item> FORGE_PUTRESCENT = TagKey.create(Registries.ITEM, new ResourceLocation("forge","putrescent"));

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {

        this.tag(ModTags.Items.EARTH_CRYSTAL_ORES)
                .add(ModBlocks.EARTH_CRYSTAL_ORE.get().asItem())
                .add(ModBlocks.DEEPSLATE_EARTH_CRYSTAL_ORE.get().asItem());

        this.tag(ModTags.Items.FIRE_QUARTZ_ORES)
                .add(ModBlocks.FIRE_QUARTZ_ORE.get().asItem());

        this.tag(ModTags.Items.ENDERGON_CRYSTAL_ORES)
                .add(ModBlocks.ENDERGON_CRYSTAL_ORE.get().asItem());

        this.tag(FORGE_ORES)
                .addTag(ModTags.Items.EARTH_CRYSTAL_ORES)
                .addTag(ModTags.Items.FIRE_QUARTZ_ORES)
                .addTag(ModTags.Items.ENDERGON_CRYSTAL_ORES);

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
                .add(ModItems.EARTH_CRYSTAL.get())
                .add(ModItems.FIRE_QUARTZ.get())
                .add(ModItems.ENDERGON_CRYSTAL.get());

        this.tag(FORGE_PUTRESCENT)
                .replace(false)
                .add(Items.SPIDER_EYE)
                .add(Items.FERMENTED_SPIDER_EYE)
                .add(Items.ROTTEN_FLESH);

        this.tag(FORGE_INGOTS)
                .replace(false)
                .add(ModItems.AROMATIC_INGOT.get());

        this.tag(FORGE_DUSTS)
                .replace(false)
                .add(ModItems.AROMATIC_DUST.get());

        this.tag(ItemTags.MUSIC_DISCS)
                .replace(false)
                .add(ModItems.RUMBLE_MUSIC_DISC.get());

        this.tag(FORGE_SEEDS)
                .replace(false)
                .add(ModItems.SPRING_ONION_SEEDS.get(),
                     ModItems.PARSNIP_SEEDS.get());

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
                .add(Items.PUFFERFISH);
    }

}
