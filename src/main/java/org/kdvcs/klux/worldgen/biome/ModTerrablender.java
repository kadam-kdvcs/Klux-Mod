package org.kdvcs.klux.worldgen.biome;

import net.minecraft.resources.ResourceLocation;
import org.kdvcs.klux.Klux;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(Klux.MODID, "celeste"), 10));
    }
}
