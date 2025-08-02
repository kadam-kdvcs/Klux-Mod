package org.kdvcs.klux.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import org.kdvcs.klux.Klux;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation AROMATIC_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_aromatic");
    public static final ResourceLocation PUTRESCENT_SOLUTION_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_putrescent_solution");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Klux.MODID);

    private static RegistryObject<FluidType> register(String name, ResourceLocation overlayTexture, int colorARGB, Vector3f fogColor, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                WATER_STILL_RL, WATER_FLOWING_RL, overlayTexture,
                colorARGB, fogColor, properties));
    }

    //AROMATIC
    public static final RegistryObject<FluidType> AROMATIC_FLUID_TYPE = register("aromatic_fluid", AROMATIC_OVERLAY_RL,
            0xA1E038D0, new Vector3f(224f / 255f, 56f / 255f, 208f / 255f),
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    //PUTRESCENT SOLUTION
    public static final RegistryObject<FluidType> PUTRESCENT_SOLUTION_FLUID_TYPE = register("putrescent_solution_fluid", PUTRESCENT_SOLUTION_OVERLAY_RL,
            0xA1FF0000, new Vector3f(1.0f, 0f, 0f),
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
