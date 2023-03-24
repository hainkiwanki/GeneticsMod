package com.hainkiwanki.geneticsmod.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.hainkiwanki.geneticsmod.GeneticsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> COBALT_ORE_VEINS_PER_CHUNK;
    public static final ForgeConfigSpec.ConfigValue<Integer> COBALT_ORE_VEIN_SIZE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<Integer>>> TEST_LIST;

    static {
        BUILDER.push("Configs for MCCourseMod");
        COBALT_ORE_VEINS_PER_CHUNK = BUILDER.comment("How many Cobalt Ore Veins spawn per chunk!")
                .define("Veins Per Chunk", 7);
        COBALT_ORE_VEIN_SIZE = BUILDER.comment("How many Cobalt Ore Blocks spawn in one Vein!")
                .define("Vein Size", 9);
        TEST_LIST = BUILDER.comment("Testing my list")
                .defineList("Shape List",
                List.of(
                    List.of(1,1,1),
                    List.of(2,2,2),
                    List.of(3,3,3)
                ), e -> true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
