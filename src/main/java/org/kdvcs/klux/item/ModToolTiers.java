package org.kdvcs.klux.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.util.ModTags;

import java.util.List;

public class ModToolTiers {
    public static final Tier EARTH_CRYSTAL = TierSortingRegistry.registerTier(
            new ForgeTier(2,750,5.2f,0,15,
                    ModTags.Blocks.NEEDS_EARTH_CRYSTAL_TOOL, () -> Ingredient.of(ModItems.EARTH_CRYSTAL.get())),
            new ResourceLocation(Klux.MODID,"earth_crystal"), List.of(Tiers.STONE), List.of());
}
