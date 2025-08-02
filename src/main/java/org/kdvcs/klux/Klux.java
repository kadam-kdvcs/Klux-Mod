package org.kdvcs.klux;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.kdvcs.klux.block.ModBlocks;
import org.kdvcs.klux.block.entity.ModBlockEntities;
import org.kdvcs.klux.config.KluxCommonConfigs;
import org.kdvcs.klux.fluid.ModFluidTypes;
import org.kdvcs.klux.fluid.ModFluids;
import org.kdvcs.klux.item.ModCreativeModeTabs;
import org.kdvcs.klux.item.ModItems;
import org.kdvcs.klux.item.renderer.MultiphaseFluidContainerRenderer;
import org.kdvcs.klux.loot.ModLootModifiers;
import org.kdvcs.klux.networking.ModMessages;
import org.kdvcs.klux.recipe.ModRecipes;
import org.kdvcs.klux.screen.*;
import org.kdvcs.klux.sound.ModSounds;
import org.slf4j.Logger;

@Mod(Klux.MODID)
public class Klux {

    public static final String MODID = "klux";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Klux() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        ModMessages.register();
        ModSounds.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KluxCommonConfigs.SPEC);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // LOGGER.info("Return Scepter Cooldown (ticks): {}", KluxCommonConfigs.RETURNSCEPTER_COOLDOWN.get());
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CACTUS_FRUIT.getId(), ModBlocks.POTTED_CACTUS_FRUIT);
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            MenuScreens.register(ModMenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
            MenuScreens.register(ModMenuTypes.DEHYDRATOR_MENU.get(), DehydratorScreen::new);
            MenuScreens.register(ModMenuTypes.EXTRACTOR_MENU.get(), ExtractorScreen::new);

            MenuScreens.register(ModMenuTypes.FLUID_ASSEMBLER_MENU.get(), FluidAssemblerScreen::new);
            MenuScreens.register(ModMenuTypes.FLUID_EXTRACTOR_MENU.get(), FluidExtractorScreen::new);

            MenuScreens.register(ModMenuTypes.MULTIPHASE_FLUID_TANK_MENU.get(), MultiphaseFluidTankScreen::new);

            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AROMATIC.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AROMATIC.get(), RenderType.translucent());

        }
    }
}
