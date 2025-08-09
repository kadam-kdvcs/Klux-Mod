package org.kdvcs.klux.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public enum LootItemConditionCodec implements com.mojang.serialization.Codec<LootItemCondition> {
    INSTANCE;

    @Override
    public <T> DataResult<T> encode(LootItemCondition input, DynamicOps<T> ops, T prefix) {
        Serializer<LootItemCondition> serializer = (Serializer<LootItemCondition>) input.getType().getSerializer();
        if (serializer == null) {
            return DataResult.error(() -> "No serializer for condition type: " + input.getType());
        }
        if (!(ops instanceof JsonOps)) {
            return DataResult.error(() -> "Only JsonOps supported");
        }
        try {

            JsonObject json = new JsonObject();
            serializer.serialize(json, input, null);

            JsonElement jsonElement = json;

            @SuppressWarnings("unchecked")
            T result = (T) jsonElement;

            return DataResult.success(result);
        } catch (Exception e) {
            return DataResult.error(() -> "Serialization failed: " + e.getMessage());
        }
    }

    @Override
    public <T> DataResult<Pair<LootItemCondition, T>> decode(DynamicOps<T> ops, T input) {
        if (!(ops instanceof JsonOps)) {
            return DataResult.error(() -> "Only JsonOps supported");
        }
        try {

            JsonElement element = (JsonElement) input;
            if (!element.isJsonObject()) {
                return DataResult.error(() -> "Expected JsonObject");
            }
            JsonObject json = element.getAsJsonObject();

            if (!json.has("condition")) {
                return DataResult.error(() -> "Missing condition type");
            }
            ResourceLocation type = new ResourceLocation(json.get("condition").getAsString());
            LootItemConditionType typeObj = BuiltInRegistries.LOOT_CONDITION_TYPE.get(type);
            if (typeObj == null) {
                return DataResult.error(() -> "Unknown condition type: " + type);
            }
            Serializer<LootItemCondition> serializer = (Serializer<LootItemCondition>) typeObj.getSerializer();
            LootItemCondition condition = serializer.deserialize(json, null);

            return DataResult.success(Pair.of(condition, input));
        } catch (Exception e) {
            return DataResult.error(() -> "Deserialization failed: " + e.getMessage());
        }
    }
}
