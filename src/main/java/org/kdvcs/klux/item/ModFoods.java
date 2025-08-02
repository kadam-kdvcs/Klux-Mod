package org.kdvcs.klux.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties SALAD = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED,160, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100, 1), 1.0f).build();

    public static final FoodProperties PARSNIP = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).build();

    public static final FoodProperties SPRING_ONION = new FoodProperties.Builder().nutrition(2).saturationMod(0.25f).fast().build();

    public static final FoodProperties NUTRI_BLOCK = new FoodProperties.Builder().nutrition(7).saturationMod(0.7f).build();

    public static final FoodProperties ROUGH_CAKE_BASE = new FoodProperties.Builder().nutrition(1).saturationMod(0.15f).build();

    public static final FoodProperties VITAMIN_CAKE = new FoodProperties.Builder().nutrition(3).saturationMod(0.65f).build();

}
