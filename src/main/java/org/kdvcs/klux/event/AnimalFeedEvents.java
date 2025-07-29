package org.kdvcs.klux.event;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.item.ModItems;

@Mod.EventBusSubscriber(modid = Klux.MODID)
public class AnimalFeedEvents {

    @SubscribeEvent
    public static void onAnimalFeed(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Animal animal)) return;

        ItemStack stack = event.getItemStack();
        if (!stack.is(ModItems.UNIVERSAL_FEED.get())) return;

        if (animal.isBaby()) {
            int currentAge = animal.getAge(); // negative
            int reduction = Math.max((int) (currentAge * 0.1), -200);
            animal.setAge(currentAge - reduction);
            spawnParticles(animal);
            consumeItem(stack, event);
            event.setCanceled(true);

        } else if (animal.getAge() == 0) {
            animal.setInLove(event.getEntity());
            consumeItem(stack, event);
            event.setCanceled(true);
        }
    }

    private static void consumeItem(ItemStack stack, PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    private static void spawnParticles(Animal animal) {
        for (int i = 0; i < 5; i++) {
            double offsetX = (animal.level().random.nextDouble() - 0.5) * 0.5;
            double offsetY = animal.level().random.nextDouble() * 0.5 + 0.5;
            double offsetZ = (animal.level().random.nextDouble() - 0.5) * 0.5;

            animal.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                    animal.getX() + offsetX,
                    animal.getY() + offsetY,
                    animal.getZ() + offsetZ,
                    0.0, 0.0, 0.0
            );
        }
    }

}
