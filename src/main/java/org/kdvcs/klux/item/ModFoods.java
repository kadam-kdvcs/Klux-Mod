package org.kdvcs.klux.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    //  EFFECT, DURATION, LEVEL (0 REFERS TO 1), POSSIBILITY (DEFAULT AS 1)
    //  1min = 1200tick

    public static final FoodProperties SALAD = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED,160, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100, 1), 1.0f).build();

    public static final FoodProperties PARSNIP = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).build();

    public static final FoodProperties SPRING_ONION = new FoodProperties.Builder().nutrition(2).saturationMod(0.25f).fast().build();

    public static final FoodProperties NUTRI_BLOCK = new FoodProperties.Builder().nutrition(7).saturationMod(0.7f).build();

    public static final FoodProperties ROUGH_CAKE_BASE = new FoodProperties.Builder().nutrition(1).saturationMod(0.15f).build();

    public static final FoodProperties VITAMIN_CAKE = new FoodProperties.Builder().nutrition(3).saturationMod(0.65f).build();

    public static final FoodProperties APPLE_COPPER = new FoodProperties.Builder().nutrition(3)
            .saturationMod(1.5f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.POISON,200, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,300, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600,0),1.0f).build();

    public static final FoodProperties APPLE_IRON = new FoodProperties.Builder().nutrition(4)
            .saturationMod(3f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,160, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,800, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400,1),1.0f).build();

    public static final FoodProperties APPLE_LAPIS = new FoodProperties.Builder().nutrition(4)
            .saturationMod(3.5f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN,160, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,40, 0), 1.0f).build();

    public static final FoodProperties APPLE_DIAMOND = new FoodProperties.Builder().nutrition(5)
            .saturationMod(5.5f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,3600, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,200, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,1),1.0f).build();

    public static final FoodProperties APPLE_EMERALD = new FoodProperties.Builder().nutrition(5)
            .saturationMod(6f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,4200, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK,3600, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,700,1),1.0f).build();

    public static final FoodProperties APPLE_AMETHYST = new FoodProperties.Builder().nutrition(6)
            .saturationMod(7f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,4800, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,600, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,1),1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,400,0),1.0f).build();
}
