package com.fanya.forgetps;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ForgeTPS.MODID)
public class ForgeTPS {
    public static final String MODID = "forgetps";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ForgeTPS() {
        LOGGER.info("Initializing ForgeTPS mod");
        // Загружаем конфигурацию до регистрации обработчиков событий
        ModConfig.loadConfig();
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("ForgeTPS initialized successfully");
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                LOGGER.debug("Player logged in: {}", serverPlayer.getName().getString());
                TabManager.updateTabList(serverPlayer);
                updatePlayerTeam(serverPlayer);
            }
        } catch (Exception e) {
            LOGGER.error("Error during player login: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        try {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                updatePlayerTeam(serverPlayer);
            }
        } catch (Exception e) {
            LOGGER.error("Error during dimension change: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        try {
            if (event.player instanceof ServerPlayer serverPlayer && event.phase == TickEvent.Phase.END) {
                updatePlayerTeam(serverPlayer);
            }
        } catch (Exception e) {
            LOGGER.error("Error during player tick: {}", e.getMessage());
        }
    }

    private void updatePlayerTeam(ServerPlayer player) {
        try {
            Scoreboard scoreboard = player.level().getScoreboard();
            String dimensionName = player.level().dimension().location().toString();

            String teamName = "d_" + dimensionName;
            ChatFormatting color = ChatFormatting.WHITE;

            if (ModConfig.enableNicknameColorChange) {
                color = ModConfig.customDimensionsColors.getOrDefault(dimensionName, ChatFormatting.GRAY);
            }

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setDisplayName(Component.literal("d_" + player.level().dimension().location().getPath()));
                team.setColor(color);
            } else if (team.getColor() != color) {
                team.setColor(color);
            }

            if (!team.getPlayers().contains(player.getScoreboardName())) {
                removePlayerFromAllTeams(player, scoreboard);
                LOGGER.debug("{} moved to {}", player.getScoreboardName(), dimensionName);
                scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
            }
        } catch (Exception e) {
            LOGGER.error("Error updating player team: {}", e.getMessage());
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
        try {
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
                    TabManager.updateTabList(player);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during server tick: {}", e.getMessage());
        }
    }
}
