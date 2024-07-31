package com.hainkiwanki.geneticsmod.cap;

public enum EMobStat {
    SIZE,
    HEALTH,
    ATTACK_DAMAGE,
    ATTACK_SPEED,
    MOVE_SPEED,
    MOB_TYPE,
    DROPS_MOD,
    EXP_MOD,
    FERTILITY,
    GILLS,
    FIRE_IMMUNE,
    OVIPAROUS,
    LACTATION,
    MATURING_TIME,
    HOSTILITY,
    IDENTIFIED;

    public String toStringKey() {
        return this.name().toLowerCase();
    }
};

