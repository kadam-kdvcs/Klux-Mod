package org.kdvcs.klux.event;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.item.ModItems;

@Mod.EventBusSubscriber(modid = Klux.MODID)
public class AnimalBehaviorHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Animal animal && event.getEntity() instanceof PathfinderMob pathfinderMob) {
            pathfinderMob.goalSelector.addGoal(3, new TemptGoal(
                    pathfinderMob, 1.25D, Ingredient.of(ModItems.UNIVERSAL_FEED.get()), false
            ));
        }
    }
}
