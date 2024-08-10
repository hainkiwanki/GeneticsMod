package com.hainkiwanki.geneticsmod.cap.mobdata;

import java.util.Arrays;

public enum EMobStat {
    SIZE,
    HEALTH,
    ATTACK_DAMAGE,
    ATTACK_SPEED,
    MOVE_SPEED,
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

    public static EMobStat[] getFilteredStats() {
        return Arrays.stream(EMobStat.values()).filter(stat -> stat != EMobStat.IDENTIFIED).toArray(EMobStat[]::new);
    }

    public static String getStatNames() {
        StringBuilder output = new StringBuilder();
        EMobStat[] stats = EMobStat.getFilteredStats();
        for (int i = 0; i < stats.length; i++) {
            output.append(stats[i].name());
            if (i < stats.length - 1) {
                output.append(", ");
            }
        }
        return output.toString();
    }
}

