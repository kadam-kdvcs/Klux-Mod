package org.kdvcs.klux.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Klux.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_SUS_SAND_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_sus_sand_item", AddSusSandItemModifier.CODEC);

    @SuppressWarnings("unchecked")
    private static <T extends IGlobalLootModifier> Codec<T> castCodec(Codec<?> codec) {
        return (Codec<T>) codec;
    }

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> REPLACE_DROP =
            LOOT_MODIFIER_SERIALIZERS.register("replace_drop", () -> castCodec(ReplaceDropModifier.CODEC));

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
