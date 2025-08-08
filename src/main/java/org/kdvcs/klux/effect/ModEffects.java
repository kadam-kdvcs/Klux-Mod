package org.kdvcs.klux.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Klux.MODID);

    public static final RegistryObject<MobEffect> EMPTY = MOB_EFFECTS.register("empty",
            () -> new EmptyEffect(MobEffectCategory.NEUTRAL, 0x00008B));

    public static final RegistryObject<MobEffect> BURN = MOB_EFFECTS.register("burn",
            () -> new BurnEffect(MobEffectCategory.HARMFUL, 0xFF6600));

    public static final RegistryObject<MobEffect> PURIFICATION = MOB_EFFECTS.register("purification",
            () -> new PurificationEffect(MobEffectCategory.BENEFICIAL, 0xE0FFE0));

    public static final RegistryObject<MobEffect> SATURATION = MOB_EFFECTS.register("saturation",
            () -> new SaturationEffect(MobEffectCategory.BENEFICIAL, 0xF4E842));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
