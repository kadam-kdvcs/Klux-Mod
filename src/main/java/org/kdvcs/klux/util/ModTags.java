package org.kdvcs.klux.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.kdvcs.klux.Klux;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Klux.MODID, name));
        }

        public static TagKey<Block> forgeTag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation("forge", name));
        }

        public static final TagKey<Block> EARTH_CRYSTAL_ORES = forgeTag("ores/earth_crystal");
        public static final TagKey<Block> FIRE_QUARTZ_ORES = forgeTag("ores/fire_quartz");
        public static final TagKey<Block> ENDERGON_CRYSTAL_ORES = forgeTag("ores/endergon_crystal");

        public static final TagKey<Block> DETECTOR_VALUABLES = tag("detector_valuables");
        public static final TagKey<Block> NEEDS_EARTH_CRYSTAL_TOOL = tag("needs_earth_crystal_tool");

    }

    public static class Items{

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Klux.MODID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

        public static final TagKey<Item> EARTH_CRYSTAL_ORES = forgeTag("ores/earth_crystal");
        public static final TagKey<Item> FIRE_QUARTZ_ORES = forgeTag("ores/fire_quartz");
        public static final TagKey<Item> ENDERGON_CRYSTAL_ORES = forgeTag("ores/endergon_crystal");

        public static final TagKey<Item> EXTRACTION_MESHES = forgeTag("extraction_meshes");
        public static final TagKey<Item> EXTRACTION_MESH_BASIC = forgeTag("extraction_mesh_basic");
        public static final TagKey<Item> EXTRACTION_MESH_ADVANCED = forgeTag("extraction_mesh_advanced");
        public static final TagKey<Item> EXTRACTION_MESH_ULTIMATE = forgeTag("extraction_mesh_ultimate");

    }
}
