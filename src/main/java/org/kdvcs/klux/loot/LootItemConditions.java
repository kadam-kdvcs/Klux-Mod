package org.kdvcs.klux.loot;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.*;

/**
 *
 *
 * Registers and manages custom LootItemConditionTypes for the mod.
 *
 * This class provides:
 * - Registration of various loot conditions so that they can be recognized by Minecraft's loot system.
 * - Utility methods to combine multiple predicates with logical AND / OR.
 * - Gson adapter creation for JSON serialization/deserialization of loot conditions.
 * - A Codec for serializing/deserializing arrays of loot conditions.
 *
 * Use this class when defining complex loot drop conditions involving factors like:
 * - Random chance
 * - Entity properties (e.g. killed by player)
 * - Block state checks
 * - Tool matches
 * - Location, time, weather, or other game state checks
 *
 * @Kadam
 *
 *
 */

public class LootItemConditions {

    public static final LootItemConditionType INVERTED = register("inverted", new InvertedLootItemCondition.Serializer());
    public static final LootItemConditionType ANY_OF = register("any_of", new AnyOfCondition.Serializer());
    public static final LootItemConditionType ALL_OF = register("all_of", new AllOfCondition.Serializer());
    public static final LootItemConditionType RANDOM_CHANCE = register("random_chance", new LootItemRandomChanceCondition.Serializer());
    public static final LootItemConditionType RANDOM_CHANCE_WITH_LOOTING = register("random_chance_with_looting", new LootItemRandomChanceWithLootingCondition.Serializer());
    public static final LootItemConditionType ENTITY_PROPERTIES = register("entity_properties", new LootItemEntityPropertyCondition.Serializer());
    public static final LootItemConditionType KILLED_BY_PLAYER = register("killed_by_player", new LootItemKilledByPlayerCondition.Serializer());
    public static final LootItemConditionType ENTITY_SCORES = register("entity_scores", new EntityHasScoreCondition.Serializer());
    public static final LootItemConditionType BLOCK_STATE_PROPERTY = register("block_state_property", new LootItemBlockStatePropertyCondition.Serializer());
    public static final LootItemConditionType MATCH_TOOL = register("match_tool", new MatchTool.Serializer());
    public static final LootItemConditionType TABLE_BONUS = register("table_bonus", new BonusLevelTableCondition.Serializer());
    public static final LootItemConditionType SURVIVES_EXPLOSION = register("survives_explosion", new ExplosionCondition.Serializer());
    public static final LootItemConditionType DAMAGE_SOURCE_PROPERTIES = register("damage_source_properties", new DamageSourceCondition.Serializer());
    public static final LootItemConditionType LOCATION_CHECK = register("location_check", new LocationCheck.Serializer());
    public static final LootItemConditionType WEATHER_CHECK = register("weather_check", new WeatherCheck.Serializer());
    public static final LootItemConditionType REFERENCE = register("reference", new ConditionReference.Serializer());
    public static final LootItemConditionType TIME_CHECK = register("time_check", new TimeCheck.Serializer());
    public static final LootItemConditionType VALUE_CHECK = register("value_check", new ValueCheckCondition.Serializer());

    private static LootItemConditionType register(String id, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(id), new LootItemConditionType(serializer));
    }

    public static Object createGsonAdapter() {
        return GsonAdapterFactory.builder(BuiltInRegistries.LOOT_CONDITION_TYPE, "condition", "condition", LootItemCondition::getType).build();
    }

    public static <T> Predicate<T> andConditions(Predicate<T>[] conditions) {
        switch (conditions.length) {
            case 0: return t -> true;
            case 1: return conditions[0];
            case 2: return conditions[0].and(conditions[1]);
            default: return t -> {
                for (Predicate<T> c : conditions) {
                    if (!c.test(t)) return false;
                }
                return true;
            };
        }
    }

    public static <T> Predicate<T> orConditions(Predicate<T>[] conditions) {
        switch (conditions.length) {
            case 0: return t -> false;
            case 1: return conditions[0];
            case 2: return conditions[0].or(conditions[1]);
            default: return t -> {
                for (Predicate<T> c : conditions) {
                    if (c.test(t)) return true;
                }
                return false;
            };
        }
    }

    public static final Codec<LootItemCondition[]> CODEC = Codec.list(LootItemConditionCodec.INSTANCE)
            .xmap(
                    list -> list.toArray(new LootItemCondition[0]),
                    arr -> List.of(arr)
            );
}
