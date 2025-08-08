package org.kdvcs.klux.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SaturationEffect extends MobEffect {

    public SaturationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            int foodToAdd = 1 + amplifier;
            float saturationToAdd = 0.5f + amplifier * 0.25f;

            player.getFoodData().eat(foodToAdd, saturationToAdd);
            player.removeEffect(MobEffects.HUNGER);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
