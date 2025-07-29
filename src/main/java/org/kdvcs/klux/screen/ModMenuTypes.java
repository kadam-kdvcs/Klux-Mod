package org.kdvcs.klux.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Klux.MODID);

    public static final RegistryObject<MenuType<CompressorMenu>> COMPRESSOR_MENU =
            registerMenuType("compressor_menu", CompressorMenu::new);

    public static final RegistryObject<MenuType<SeedMakerMenu>> SEED_MAKER_MENU =
            registerMenuType("seed_maker_menu", SeedMakerMenu::new);

    public static final RegistryObject<MenuType<DehydratorMenu>> DEHYDRATOR_MENU =
            registerMenuType("dehydrator_menu", DehydratorMenu::new);

    public static final RegistryObject<MenuType<ExtractorMenu>> EXTRACTOR_MENU =
            registerMenuType("extractor_menu", ExtractorMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
