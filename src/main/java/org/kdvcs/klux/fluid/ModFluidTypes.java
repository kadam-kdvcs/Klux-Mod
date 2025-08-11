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
    public static final ResourceLocation PECTIN_SLURRY_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_pectin_slurry");
    public static final ResourceLocation PRIMAL_ESSENCE_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_primal_essence");
    public static final ResourceLocation ENZYME_SOLUTION_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_enzyme_solution");
    public static final ResourceLocation BOTANIC_ESSENTIAL_OIL_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_botanic_essential_oil");
    public static final ResourceLocation MINERAL_SLURRY_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_mineral_slurry");
    public static final ResourceLocation REPAIR_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_repair");

    public static final ResourceLocation QUARTZ_STILL_RL = new ResourceLocation(Klux.MODID, "block/quartz_slurry_still");
    public static final ResourceLocation QUARTZ_FLOWING_RL = new ResourceLocation(Klux.MODID, "block/quartz_slurry_flow");
    public static final ResourceLocation QUARTZ_OVERLAY_RL = new ResourceLocation(Klux.MODID, "fluid/in_quartz_slurry");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Klux.MODID);

    private static RegistryObject<FluidType> register(String name, ResourceLocation overlayTexture, int colorARGB, Vector3f fogColor, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                WATER_STILL_RL, WATER_FLOWING_RL, overlayTexture,
                colorARGB, fogColor, properties));
    }

    private static RegistryObject<FluidType> register(String name,
                                                      ResourceLocation stillTexture,
                                                      ResourceLocation flowingTexture,
                                                      ResourceLocation overlayTexture,
                                                      int colorARGB,
                                                      Vector3f fogColor,
                                                      FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                stillTexture, flowingTexture, overlayTexture,
                colorARGB, fogColor, properties));
    }

    //QUARTZ SLURRY
    public static final RegistryObject<FluidType> QUARTZ_SLURRY_TYPE = register(
            "quartz_slurry_fluid",
            QUARTZ_STILL_RL,
            QUARTZ_FLOWING_RL,
            QUARTZ_OVERLAY_RL,
            0xFFFFFFFF,
            new Vector3f(1.0f, 1.0f, 1.0f),
            FluidType.Properties.create().lightLevel(2).density(100).viscosity(20).canSwim(false).canPushEntity(false)
                    .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK)
    );

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

    //PECTIN SLURRY
    public static final RegistryObject<FluidType> PECTIN_SLURRY_FLUID_TYPE = register("pectin_slurry_fluid", PECTIN_SLURRY_OVERLAY_RL,
            0xA1AAFFAA, new Vector3f(0.667f, 1.0f, 0.667f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    //PRIMAL ESSENCE
    public static final RegistryObject<FluidType> PRIMAL_ESSENCE_FLUID_TYPE = register("primal_essence_fluid", PRIMAL_ESSENCE_OVERLAY_RL,
            0xA1FFA500, new Vector3f(1.0f, 0.647f, 0.0f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    //ENZYME SOLUTION
    public static final RegistryObject<FluidType> ENZYME_SOLUTION_FLUID_TYPE = register("enzyme_solution_fluid", ENZYME_SOLUTION_OVERLAY_RL,
            0xA1C8A2C8, new Vector3f(0.78f, 0.635f, 0.78f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                            SoundEvents.HONEY_DRINK));

    //BOTANIC ESSENTIAL OIL
    public static final RegistryObject<FluidType> BOTANIC_ESSENTIAL_OIL_FLUID_TYPE = register("botanic_essential_oil_fluid",
            BOTANIC_ESSENTIAL_OIL_OVERLAY_RL,
            0xA19D8D67, new Vector3f(0.616f, 0.553f, 0.404f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    //MINERAL_SLURRY
    public static final RegistryObject<FluidType> MINERAL_SLURRY_FLUID_TYPE = register("mineral_slurry_fluid",
            MINERAL_SLURRY_OVERLAY_RL,
            0xFFBF7359, new Vector3f(0.749f, 0.451f, 0.349f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    //REPAIR
    public static final RegistryObject<FluidType> REPAIR_FLUID_TYPE = register("repair_fluid",
            REPAIR_OVERLAY_RL,
            0xFF00FFFF, new Vector3f(0.0f, 1.0f, 1.0f),
            FluidType.Properties.create().lightLevel(2).density(75).viscosity(6000).canSwim(false).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
