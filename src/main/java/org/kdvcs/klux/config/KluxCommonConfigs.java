package org.kdvcs.klux.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class KluxCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue RETURNSCEPTER_COOLDOWN;

    static {
        BUILDER.push("Return Scepter Settings");

        RETURNSCEPTER_COOLDOWN = BUILDER
                .comment("Cooldown time in ticks for Return Scepter (20 ticks = 1 second)")
                .defineInRange("returnScepterCooldownTicks", 1200, 1, 72000); // 默认 400 tick = 20 秒

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
