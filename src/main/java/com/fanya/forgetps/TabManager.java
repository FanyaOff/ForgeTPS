package com.fanya.forgetps;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.Connection;

public class TabManager {
    public static void updateTabList(ServerPlayer player) {
        // Проверяем, инициализированы ли поля конфигурации
        if (ModConfig.tabHeader == null || ModConfig.tabFooter == null) {
            ForgeTPS.LOGGER.warn("Tab header or footer is null, reloading config");
            ModConfig.loadConfig();

            // Если после загрузки поля все еще null, выходим из метода
            if (ModConfig.tabHeader == null || ModConfig.tabFooter == null) {
                ForgeTPS.LOGGER.error("Failed to load tab header or footer from config");
                return;
            }
        }

        try {
            int tps = (int) Math.round(PerformanceMetrics.calculateAverageTPS());
            int mspt = (int) Math.round(PerformanceMetrics.calculateAverageMSPT());
            int ping = player.connection.getPlayer().latency;

            // Заменяем плейсхолдеры в header и footer
            String headerText = ModConfig.tabHeader.replace("%playername%", player.getName().getString());
            String footerText = ModConfig.tabFooter
                    .replace("%tps%", PerformanceMetrics.colorizeTPS(tps))
                    .replace("%mspt%", PerformanceMetrics.colorizeMSPT(mspt))
                    .replace("%ping%", PerformanceMetrics.colorizePing(ping));

            Component header = Component.literal(headerText);
            Component footer = Component.literal(footerText);

            setTabListHeaderAndFooter(player, header, footer);
        } catch (Exception e) {
            ForgeTPS.LOGGER.error("Error updating tab list: {}", e.getMessage());
        }
    }

    private static void setTabListHeaderAndFooter(ServerPlayer player, Component header, Component footer) {
        if (player != null && player.connection != null && player.connection.connection != null) {
            Connection connection = player.connection.connection;
            ClientboundTabListPacket packet = new ClientboundTabListPacket(header, footer);
            connection.send(packet);
        }
    }
}
