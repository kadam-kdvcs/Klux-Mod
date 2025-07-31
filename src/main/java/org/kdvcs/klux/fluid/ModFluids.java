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

    public static final RegistryObject<FlowingFluid> SOURCE_AROMATIC = FLUIDS.register("aromatic_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.AROMATIC_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AROMATIC = FLUIDS.register("flowing_aromatic",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AROMATIC_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties AROMATIC_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.AROMATIC_FLUID_TYPE, SOURCE_AROMATIC, FLOWING_AROMATIC)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.AROMATIC_BLOCK)
            .bucket(ModItems.AROMATIC_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}