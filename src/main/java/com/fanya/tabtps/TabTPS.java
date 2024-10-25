package com.fanya.tabtps;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static com.fanya.tabtps.TabManager.updateTabList;

@Mod(TabTPS.MODID)
public class TabTPS {
    public static final String MODID = "tabtps";
    public static final Logger LOGGER = LogUtils.getLogger();


    public TabTPS(){
        MinecraftForge.EVENT_BUS.register(this);
        ModConfig.loadConfig();
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            updateTabList(serverPlayer);
            updatePlayerTeam(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            updatePlayerTeam(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer && event.phase == TickEvent.Phase.END) {
            updatePlayerTeam(serverPlayer);
        }
    }

    private void updatePlayerTeam(ServerPlayer player) {
        Scoreboard scoreboard = player.getLevel().getScoreboard();
        String dimensionName = player.getLevel().dimension().location().toString();

        String teamName = "defaultTeam";
        ChatFormatting color = ChatFormatting.WHITE;

        if (ModConfig.enableNicknameColorChange) {
            color = ModConfig.customDimensionsColors.getOrDefault(dimensionName, ChatFormatting.GRAY);
        }

        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            team = scoreboard.addPlayerTeam(teamName);
            team.setDisplayName(Component.literal("d_"+player.getLevel().dimension().location().getPath()));
            team.setColor(color);
        } else if (team.getColor() != color) {
            team.setColor(color);
        }

        if (!team.getPlayers().contains(player.getScoreboardName())) {
            removePlayerFromAllTeams(player, scoreboard);
            LOGGER.info(player.getScoreboardName() + " moved to " + dimensionName);
            scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
        }
    }

    private void removePlayerFromAllTeams(ServerPlayer player, Scoreboard scoreboard) {
        for (PlayerTeam team : scoreboard.getPlayerTeams()) {
            if (team.getPlayers().contains(player.getScoreboardName())) {
                scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.nanoTime();

            if (PerformanceMetrics.lastTickTime != 0) {
                long tickDuration = currentTime - PerformanceMetrics.lastTickTime;

                PerformanceMetrics.sumTickTime += tickDuration;
                PerformanceMetrics.tickCount++;

                if (PerformanceMetrics.tickCount > 100) {
                    PerformanceMetrics.sumTickTime -= PerformanceMetrics.sumTickTime / PerformanceMetrics.tickCount;
                    PerformanceMetrics.tickCount--;
                }
            }

            PerformanceMetrics.lastTickTime = currentTime;

            MinecraftServer server = event.getServer();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                updateTabList(player);
            }
        }
    }
}
