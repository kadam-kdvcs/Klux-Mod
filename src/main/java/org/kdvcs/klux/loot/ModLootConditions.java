package org.kdvcs.klux.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;
import org.kdvcs.klux.loot.condition.BlockTagLootCondition;
import org.kdvcs.klux.loot.condition.CodecSerializer;

public class ModLootConditions {
    public static final ResourceKey<Registry<LootItemConditionType>> LOOT_CONDITION_TYPE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation("loot_condition_type"));

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS =
            DeferredRegister.create(LOOT_CONDITION_TYPE_REGISTRY_KEY, Klux.MODID);

    public static final RegistryObject<LootItemConditionType> BLOCK_TAG =
            LOOT_CONDITIONS.register("block_tag",
                    () -> new LootItemConditionType(new CodecSerializer<>(BlockTagLootCondition.CODEC)));

    public static void register(IEventBus eventBus) {
        LOOT_CONDITIONS.register(eventBus);
    }
}
