package com.fanya.tabtps;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.Connection;

public class TabManager {
    public static void updateTabList(ServerPlayer player) {
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
    }

    private static void setTabListHeaderAndFooter(ServerPlayer player, Component header, Component footer) {
        Connection connection = player.connection.getConnection();
        ClientboundTabListPacket packet = new ClientboundTabListPacket(header, footer);
        connection.send(packet);
    }
}

