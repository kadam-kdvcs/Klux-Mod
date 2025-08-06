package org.kdvcs.klux.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.Serializer;

public class CodecSerializer<T> implements Serializer<T> {
    private final Codec<T> codec;

    public CodecSerializer(Codec<T> codec) {
        this.codec = codec;
    }

    @Override
    public void serialize(JsonObject json, T object, JsonSerializationContext context) {
        JsonObject encoded = (JsonObject) codec.encodeStart(JsonOps.INSTANCE, object)
                .resultOrPartial(System.err::println)
                .orElse(new JsonObject());
        for (String key : encoded.keySet()) {
            json.add(key, encoded.get(key));
        }
    }

    @Override
    public T deserialize(JsonObject json, JsonDeserializationContext context) {
        return codec.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(System.err::println)
                .orElse(null);
    }
}
