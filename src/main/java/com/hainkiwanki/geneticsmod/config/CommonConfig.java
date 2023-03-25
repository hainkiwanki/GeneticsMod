package com.hainkiwanki.geneticsmod.config;

import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> EASY_SHAPE_SIZE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> EASY_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<Integer> MEDIUM_SHAPE_SIZE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> MEDIUM_SHAPES;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EASY_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NORMAL_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HARD_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXPERT_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NIGHTMARE_ATTRIBUTES;

    static {
        BUILDER.push("Configs for Genetics Mod");

        EASY_ATTRIBUTES = BUILDER.comment("All possible attributes: " + MobData.getAttributesAsString())
                .comment("Which mob attribute is considered easy to sample")
                .defineList("Easy Attributes", List.of(MobData.SIZE, MobData.MOVE_SPEED), e -> true);
        NORMAL_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered normal to sample")
                .defineList("Medium Attributes", List.of(MobData.HEALTH, MobData.BREATH_UNDER_WATER), e -> true);
        HARD_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered hard to sample")
                .defineList("Hard Attributes", List.of(MobData.ATTACK_DAMAGE, MobData.ATTACK_SPEED, MobData.IMMUNE_FIRE), e -> true);
        EXPERT_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered very hard to sample")
                .defineList("Expert Attributes", List.of(MobData.FERTILITY, MobData.CAN_LAY_EGG, MobData.CAN_MILK), e -> true);
        NIGHTMARE_ATTRIBUTES = BUILDER.comment("Which mob attribute is considered insanely hard to sample")
                .defineList("Nightmare Attributes", List.of(MobData.MATURING_TIME, MobData.EXP_MODIFIER, MobData.DROPS_MODIFIER, MobData.IS_HOSTILE), e -> true);


        EASY_SHAPE_SIZE = BUILDER.comment("How big the easy shapes will be").define("Easy Shape Size", 3);
        EASY_SHAPES = BUILDER.comment("Define your easy shapes here, should be [EASY_SHAPE_SIZE x EASY_SHAPE_SIZE] big")
                .defineList("Easy Shapes arrays",
                List.of(List.of(" ", "x", " ", " ", "x", " ", " ", "x", "x"),
                        List.of("x", "x", " ", " ", "x", "x", " ", " ", " ")), e -> true);

        MEDIUM_SHAPE_SIZE = BUILDER.comment("How big the medium shapes will be").define("Medium Shape Size", 4);
        MEDIUM_SHAPES = BUILDER.comment("Define your medium shapes here, should be [MEDIUM_SHAPE_SIZE x MEDIUM_SHAPE_SIZE] big")
                .defineList("Medium Shapes arrays",
                        List.of(List.of(
                                "x", "x", " ", " ",
                                        "x", " ", " ", "x",
                                        "x", " ", " ", "x",
                                        "x", "x", "x", "x")), e -> true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
