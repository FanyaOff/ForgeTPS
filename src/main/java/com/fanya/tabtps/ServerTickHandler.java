package com.fanya.tabtps;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerTickHandler {
    private static long lastTickTime = 0;
    private static float tps = 20.0f;
    private static float mspt = 50.0f;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            long currentTime = System.nanoTime();
            if (lastTickTime != 0) {
                long timeSpent = currentTime - lastTickTime;
                mspt = timeSpent / 1_000_000.0f / 50.0f;
                tps = Math.min(20.0f, 1_000_000_000.0f / timeSpent * 20.0f);
            }
            lastTickTime = currentTime;
        }
    }

    public static float getTPS() {
        return tps;
    }

    public static float getMSPT() {
        return mspt;
    }
}
