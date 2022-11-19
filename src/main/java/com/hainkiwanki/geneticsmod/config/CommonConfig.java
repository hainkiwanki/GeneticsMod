package com.hainkiwanki.geneticsmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.Map;

public class CommonConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Boolean> GENETICS_TOOL_DROPS;

    static {
        BUILDER.push("Genetics Mod Common Config File");
        GENETICS_TOOL_DROPS = BUILDER.comment("Item Drop per Tool per Mob").define("Droptable", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
