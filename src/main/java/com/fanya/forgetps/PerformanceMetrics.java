package com.fanya.forgetps;

public class PerformanceMetrics {
    public static float sumTickTime = 0.0f;
    public static int tickCount = 0;
    public static long lastTickTime = 0;

    public static double calculateAverageTPS() {
        if (tickCount < 100) {
            return 20.0;
        }

        double averageTickTime = sumTickTime / tickCount;

        double tps = Math.min(1000000000.0 * (1.0 / averageTickTime), 20.0);
        return tps;
    }

    public static double calculateAverageMSPT() {
        if (tickCount < 100) {
            return 50.0;
        }

        double averageTickTime = sumTickTime / tickCount;

        double mspt = averageTickTime * 0.000001;
        return mspt;
    }

    public static String colorizeTPS(int tps) {
        if (tps >= 18) {
            return "§a"+tps+"§r";
        } else if (tps >= 15) {
            return "§e"+tps+"§r";
        } else {
            return "§c"+tps+"§r";
        }
    }

    public static String colorizeMSPT(int mspt) {
        if (mspt <= 50) {
            return "§a"+mspt+"§r";
        } else if (mspt <= 70) {
            return "§e"+mspt+"§r";
        } else {
            return "§c"+mspt+"§r";
        }
    }

    public static String colorizePing(int ping) {
        if (ping <= 50) {
            return "§a"+ping+"§r";
        } else if (ping <= 150) {
            return "§e"+ping+"§r";
        } else {
            return "§c"+ping+"§r";
        }
    }
}
