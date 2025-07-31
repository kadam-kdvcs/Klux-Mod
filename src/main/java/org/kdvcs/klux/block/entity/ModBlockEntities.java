package org.kdvcs.klux.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Klux.MODID);

    public static final RegistryObject<BlockEntityType<CompressorBlockEntity>> COMPRESSOR_BE =
            BLOCK_ENTITIES.register("compressor_be", () ->
                    BlockEntityType.Builder.of(CompressorBlockEntity::new,
                            ModBlocks.COMPRESSOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<DehydratorBlockEntity>> DEHYDRATOR_BE =
            BLOCK_ENTITIES.register("dehydrator_be", () ->
                    BlockEntityType.Builder.of(DehydratorBlockEntity::new,
                            ModBlocks.DEHYDRATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExtractorBlockEntity>> EXTRACTOR_BE =
            BLOCK_ENTITIES.register("extractor_be", () ->
                    BlockEntityType.Builder.of(ExtractorBlockEntity::new,
                            ModBlocks.EXTRACTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<FluidAssemblerBlockEntity>> FLUID_ASSEMBLER_BE =
            BLOCK_ENTITIES.register("fluid_assembler_be", () ->
                    BlockEntityType.Builder.of(FluidAssemblerBlockEntity::new,
                            ModBlocks.FLUID_ASSEMBLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
