package com.hainkiwanki.geneticsmod.config;

import com.hainkiwanki.geneticsmod.cap.EMobStat;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> EASY_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> NORMAL_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> HARD_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> EXPERT_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> NIGHTMARE_SHAPES;

    public static final ForgeConfigSpec.ConfigValue<List<? extends EMobStat>> EASY_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends EMobStat>> NORMAL_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends EMobStat>> HARD_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends EMobStat>> EXPERT_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends EMobStat>> NIGHTMARE_ATTRIBUTES;

    static {
        BUILDER.push("Configs for Genetics Mod");
        BUILDER.push("All possible Mob Stats available are the following: ");
        BUILDER.push(EMobStat.getStatNames());

        BUILDER.push("Explanation: ");
        BUILDER.push("GILLS is the ability to breath underwater (ex: fish, squid)");
        BUILDER.push("FERTILITY is the amount of babies a mob produces when breeding");
        BUILDER.push("OVIPAROUS is the ability to lay eggs (ex: chicken)");
        BUILDER.push("LACTATION is the ability to be milk-able (ex: cow, mooshrooms)");
        BUILDER.push("EXP_MOD is a multiplier which influences the amount of exp dropped on death");
        BUILDER.push("DROPS_MOD is a multiplier which influences the amount of items dropped on death");
        BUILDER.push("HOSTILITY is either Friendly (ex: cows), Neutral (ex: Bees), Hostile (ex: Zombie)");

        EASY_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered easy to sample")
                .defineList("Easy Attributes", List.of(EMobStat.SIZE, EMobStat.MOVE_SPEED), e -> true);
        NORMAL_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered normal to sample")
                .defineList("Medium Attributes", List.of(EMobStat.HEALTH, EMobStat.GILLS), e -> true);
        HARD_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered hard to sample")
                .defineList("Hard Attributes", List.of(EMobStat.ATTACK_DAMAGE, EMobStat.ATTACK_SPEED, EMobStat.FIRE_IMMUNE), e -> true);
        EXPERT_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered very hard to sample")
                .defineList("Expert Attributes", List.of(EMobStat.FERTILITY, EMobStat.OVIPAROUS, EMobStat.LACTATION), e -> true);
        NIGHTMARE_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered insanely hard to sample")
                .defineList("Nightmare Attributes", List.of(EMobStat.MATURING_TIME, EMobStat.EXP_MOD, EMobStat.DROPS_MOD, EMobStat.HOSTILITY), e -> true);


        EASY_SHAPES = BUILDER.comment("Define your easy shapes here, should be [2 x 2] big")
                .defineList("Easy Shapes arrays",
                List.of(List.of(" ", "x",
                                " ", "x"),
                        List.of("x", "x", " ", " ")), e -> true);

        NORMAL_SHAPES = BUILDER.comment("Define your medium shapes here, should be [3 x 3] big")
                .defineList("Medium Shapes arrays",
                        List.of(List.of(
                                "x", "x", " ",
                                "x", " ", " ",
                                "x", " ", " ")), e -> true);

        HARD_SHAPES = BUILDER.comment("Define your hard shapes here, should be [4 x 4] big")
                .defineList("Hard Shapes arrays",
                        List.of(List.of(
                            "x", " ", " ", " ",
                            "x", " ", " ", " ",
                            "x", " ", " ", " ",
                            "x", "x", "x", " "
                        )), e -> true);
        EXPERT_SHAPES = BUILDER.comment("Define your expert shapes here, should be [5 x 5] big")
                .defineList("Expert Shapes arrays",
                        List.of(List.of(
                            "x", " ", " ", " ", " ",
                            "x", " ", " ", " ", " ",
                            "x", " ", " ", " ", " ",
                            "x", " ", " ", "x", "x",
                            "x", "x", "x", "x", " "
                        )), e -> true);
        NIGHTMARE_SHAPES = BUILDER.comment("Define your nightmare shapes here, should be [6 x 6] big")
                .defineList("Nightmare Shapes arrays",
                        List.of(List.of(
                            "x", "x", "x", " ", " ", "x",
                            "x", " ", " ", " ", " ", "x",
                            "x", " ", " ", " ", " ", "x",
                            "x", " ", " ", "x", "x", "x",
                            "x", "x", "x", "x", " ", " ",
                            " ", " ", " ", " ", " ", " "
                        )), e -> true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
