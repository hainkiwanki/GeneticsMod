package com.hainkiwanki.geneticsmod.genetics;

import java.util.Locale;

public enum EBiologicalTag {
    ORGANIC(EBiologicalTagCategory.COMPOSITION),
    UNDEAD(EBiologicalTagCategory.COMPOSITION),
    CONSTRUCT(EBiologicalTagCategory.COMPOSITION),
    ELEMENTAL(EBiologicalTagCategory.COMPOSITION),
    MAGICAL(EBiologicalTagCategory.COMPOSITION),

    HUMANOID(EBiologicalTagCategory.BODY_TYPE),
    QUADRUPED(EBiologicalTagCategory.BODY_TYPE),
    AVIAN_BODY(EBiologicalTagCategory.BODY_TYPE),
    ARTHROPOD(EBiologicalTagCategory.BODY_TYPE),
    GELATINOUS(EBiologicalTagCategory.BODY_TYPE),
    OTHER(EBiologicalTagCategory.BODY_TYPE),

    MAMMAL(EBiologicalTagCategory.BIOLOGY),
    AVIAN_BIOLOGY(EBiologicalTagCategory.BIOLOGY),
    REPTILE(EBiologicalTagCategory.BIOLOGY),
    AMPHIBIAN(EBiologicalTagCategory.BIOLOGY),
    FISH(EBiologicalTagCategory.BIOLOGY),
    INVERTEBRATE(EBiologicalTagCategory.BIOLOGY),
    INSECT(EBiologicalTagCategory.BIOLOGY),
    ARACHNID(EBiologicalTagCategory.BIOLOGY),

    TERRESTRIAL(EBiologicalTagCategory.HABITAT),
    AQUATIC(EBiologicalTagCategory.HABITAT),
    AERIAL(EBiologicalTagCategory.HABITAT),
    NETHER(EBiologicalTagCategory.HABITAT),
    END(EBiologicalTagCategory.HABITAT),
    UNDERGROUND(EBiologicalTagCategory.HABITAT),
    COLD(EBiologicalTagCategory.HABITAT),
    HOT(EBiologicalTagCategory.HABITAT),

    WALKING(EBiologicalTagCategory.MOVEMENT),
    SWIMMING(EBiologicalTagCategory.MOVEMENT),
    FLYING(EBiologicalTagCategory.MOVEMENT),
    JUMPING(EBiologicalTagCategory.MOVEMENT),
    TELEPORTING(EBiologicalTagCategory.MOVEMENT),
    FLOATING(EBiologicalTagCategory.MOVEMENT),

    SKELETAL(EBiologicalTagCategory.SPECIAL),
    FIRE_BASED(EBiologicalTagCategory.SPECIAL),
    SCULK(EBiologicalTagCategory.SPECIAL),
    SLIME(EBiologicalTagCategory.SPECIAL),
    FUNGAL(EBiologicalTagCategory.SPECIAL),
    ETHEREAL(EBiologicalTagCategory.SPECIAL),
    PORCINE(EBiologicalTagCategory.SPECIAL),
    ARTIFICIAL(EBiologicalTagCategory.SPECIAL),
    ENDER(EBiologicalTagCategory.SPECIAL),
    ILLAGER(EBiologicalTagCategory.SPECIAL),
    ARMORED(EBiologicalTagCategory.SPECIAL);

    private final EBiologicalTagCategory category;

    EBiologicalTag(EBiologicalTagCategory category) {
        this.category = category;
    }

    public EBiologicalTagCategory getCategory() {
        return category;
    }

    public static EBiologicalTag fromString(String value) {
        return EBiologicalTag.valueOf(value.toUpperCase(Locale.ROOT));
    }
}