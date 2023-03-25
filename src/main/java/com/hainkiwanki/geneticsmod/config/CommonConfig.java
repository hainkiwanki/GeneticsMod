package com.hainkiwanki.geneticsmod.config;

import com.hainkiwanki.geneticsmod.network.mobdata.MobData;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> EASY_SHAPES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> NORMAL_SHAPES;

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


        EASY_SHAPES = BUILDER.comment("Define your easy shapes here, should be [3 x 3] big")
                .defineList("Easy Shapes arrays",
                List.of(List.of(" ", "x", " ", " ", "x", " ", " ", "x", "x"),
                        List.of("x", "x", " ", " ", "x", "x", " ", " ", " ")), e -> true);

        NORMAL_SHAPES = BUILDER.comment("Define your medium shapes here, should be [4 x 4] big")
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
