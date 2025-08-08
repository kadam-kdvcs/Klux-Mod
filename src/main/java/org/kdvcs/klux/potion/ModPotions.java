package org.kdvcs.klux.potion;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.effect.ModEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, Klux.MODID);

    //SUPER POTION BASE
    public static final RegistryObject<Potion> BASE_POTION = POTIONS.register("base_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.EMPTY.get())));

    //BURN POTIONS
    public static final RegistryObject<Potion> BURN_POTION = POTIONS.register("burn_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.BURN.get(), 200, 0)));
    public static final RegistryObject<Potion> LONG_BURN_POTION = POTIONS.register("long_burn_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.BURN.get(), 400, 0)));
    public static final RegistryObject<Potion> STRONG_BURN_POTION = POTIONS.register("strong_burn_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.BURN.get(), 200, 1)));

    //PURIFICATION POTIONS
    public static final RegistryObject<Potion> PURIFICATION_POTION = POTIONS.register("purification_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.PURIFICATION.get(), 160, 0)));
    public static final RegistryObject<Potion> LONG_PURIFICATION_POTION = POTIONS.register("long_purification_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.PURIFICATION.get(), 800, 0)));
    public static final RegistryObject<Potion> STRONG_PURIFICATION_POTION = POTIONS.register("strong_purification_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.PURIFICATION.get(), 160, 1)));

    //SATURATION POTIONS
    public static final RegistryObject<Potion> SATURATION_POTION = POTIONS.register("saturation_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.SATURATION.get(), 300, 0)));
    public static final RegistryObject<Potion> LONG_SATURATION_POTION = POTIONS.register("long_saturation_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.SATURATION.get(), 600, 0)));
    public static final RegistryObject<Potion> STRONG_SATURATION_POTION = POTIONS.register("strong_saturation_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.SATURATION.get(), 300, 1)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
