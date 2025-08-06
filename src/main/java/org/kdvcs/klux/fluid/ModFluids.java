package org.kdvcs.klux.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.item.ModItems;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Klux.MODID);

    //AROMATIC
    public static final RegistryObject<FlowingFluid> SOURCE_AROMATIC = FLUIDS.register("aromatic_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.AROMATIC_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AROMATIC = FLUIDS.register("flowing_aromatic",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AROMATIC_FLUID_PROPERTIES));

    //PUTRESCENT SOLUTION
    public static final RegistryObject<FlowingFluid> SOURCE_PUTRESCENT_SOLUTION = FLUIDS.register("putrescent_solution_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PUTRESCENT_SOLUTION_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PUTRESCENT_SOLUTION = FLUIDS.register("flowing_putrescent_solution",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PUTRESCENT_SOLUTION_FLUID_PROPERTIES));

    //PECTIN SLURRY
    public static final RegistryObject<FlowingFluid> SOURCE_PECTIN_SLURRY = FLUIDS.register("pectin_slurry_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PECTIN_SLURRY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PECTIN_SLURRY = FLUIDS.register("flowing_pectin_slurry",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PECTIN_SLURRY_FLUID_PROPERTIES));

    //PRIMAL ESSENCE
    public static final RegistryObject<FlowingFluid> SOURCE_PRIMAL_ESSENCE = FLUIDS.register("primal_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PRIMAL_ESSENCE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PRIMAL_ESSENCE = FLUIDS.register("flowing_primal_essence",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PRIMAL_ESSENCE_FLUID_PROPERTIES));

    //ENZYME SOLUTION
    public static final RegistryObject<FlowingFluid> SOURCE_ENZYME_SOLUTION = FLUIDS.register("enzyme_solution_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ENZYME_SOLUTION_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ENZYME_SOLUTION = FLUIDS.register("flowing_enzyme_solution",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ENZYME_SOLUTION_FLUID_PROPERTIES));

    //BOTANIC ESSENTIAL OIL
    public static final RegistryObject<FlowingFluid> SOURCE_BOTANIC_ESSENTIAL_OIL = FLUIDS.register("botanic_essential_oil_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.BOTANIC_ESSENTIAL_OIL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BOTANIC_ESSENTIAL_OIL = FLUIDS.register("flowing_botanic_essential_oil",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BOTANIC_ESSENTIAL_OIL_FLUID_PROPERTIES));

    //PROPERTIES

    //AROMATIC
    public static final ForgeFlowingFluid.Properties AROMATIC_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.AROMATIC_FLUID_TYPE, SOURCE_AROMATIC, FLOWING_AROMATIC)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.AROMATIC_BLOCK)
            .bucket(ModItems.AROMATIC_BUCKET);

    //PUTRESCENT SOLUTION
    public static final ForgeFlowingFluid.Properties PUTRESCENT_SOLUTION_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PUTRESCENT_SOLUTION_FLUID_TYPE, SOURCE_PUTRESCENT_SOLUTION, FLOWING_PUTRESCENT_SOLUTION)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PUTRESCENT_SOLUTION_BLOCK)
            .bucket(ModItems.PUTRESCENT_SOLUTION_BUCKET);

    //PECTIN SLURRY
    public static final ForgeFlowingFluid.Properties PECTIN_SLURRY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PECTIN_SLURRY_FLUID_TYPE, SOURCE_PECTIN_SLURRY, FLOWING_PECTIN_SLURRY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PECTIN_SLURRY_BLOCK)
            .bucket(ModItems.PECTIN_SLURRY_BUCKET);

    //PRIMAL ESSENCE
    public static final ForgeFlowingFluid.Properties PRIMAL_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PRIMAL_ESSENCE_FLUID_TYPE, SOURCE_PRIMAL_ESSENCE, FLOWING_PRIMAL_ESSENCE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PRIMAL_ESSENCE_BLOCK)
            .bucket(ModItems.PRIMAL_ESSENCE_BUCKET);

    //ENZYME SOLUTION
    public static final ForgeFlowingFluid.Properties ENZYME_SOLUTION_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.ENZYME_SOLUTION_FLUID_TYPE, SOURCE_ENZYME_SOLUTION, FLOWING_ENZYME_SOLUTION)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.ENZYME_SOLUTION_BLOCK)
            .bucket(ModItems.ENZYME_SOLUTION_BUCKET);

    //BOTANIC ESSENTIAL OIL
    public static final ForgeFlowingFluid.Properties BOTANIC_ESSENTIAL_OIL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.BOTANIC_ESSENTIAL_OIL_FLUID_TYPE, SOURCE_BOTANIC_ESSENTIAL_OIL, FLOWING_BOTANIC_ESSENTIAL_OIL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.BOTANIC_ESSENTIAL_OIL_BLOCK)
            .bucket(ModItems.BOTANIC_ESSENTIAL_OIL_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
