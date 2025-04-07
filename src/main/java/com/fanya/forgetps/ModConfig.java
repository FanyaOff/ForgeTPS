package com.fanya.forgetps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {

    public static String tabHeader = "Hello, %playername%!";
    public static String tabFooter = "TPS: %tps% | Ping: %ping% | MSPT: %mspt%";
    public static boolean enableNicknameColorChange = true;
    public static Map<String, ChatFormatting> customDimensionsColors = new HashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("forgetps.json");

    public static void loadConfig() {
        // Инициализация значений по умолчанию
        tabHeader = "Hello, %playername%!";
        tabFooter = "TPS: %tps% | Ping: %ping% | MSPT: %mspt%";
        enableNicknameColorChange = true;
        customDimensionsColors.clear();

        // Добавляем цвета по умолчанию
        customDimensionsColors.put("minecraft:overworld", ChatFormatting.GREEN);
        customDimensionsColors.put("minecraft:the_nether", ChatFormatting.RED);
        customDimensionsColors.put("minecraft:the_end", ChatFormatting.LIGHT_PURPLE);

        // Если файл не существует, создаем его
        if (!Files.exists(CONFIG_PATH)) {
            saveDefaultConfig();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            Map<String, Object> config = GSON.fromJson(reader, Map.class);

            if (config == null) {
                ForgeTPS.LOGGER.error("Config file is empty or malformed, using defaults");
                saveDefaultConfig();
                return;
            }

            // Получаем значения из конфигурации
            if (config.containsKey("tabHeader")) {
                tabHeader = (String) config.get("tabHeader");
            }

            if (config.containsKey("tabFooter")) {
                tabFooter = (String) config.get("tabFooter");
            }

            if (config.containsKey("enableNicknameColorChange")) {
                Object value = config.get("enableNicknameColorChange");
                if (value instanceof Boolean) {
                    enableNicknameColorChange = (Boolean) value;
                }
            }

            // Безопасное получение customDimensions
            Object dimensionsObj = config.get("customDimensions");
            if (dimensionsObj != null) {
                try {
                    Type type = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> dimensions = GSON.fromJson(GSON.toJson(dimensionsObj), type);

                    if (dimensions != null) {
                        for (Map.Entry<String, String> entry : dimensions.entrySet()) {
                            try {
                                ChatFormatting color = ChatFormatting.valueOf(entry.getValue());
                                customDimensionsColors.put(entry.getKey(), color);
                            } catch (IllegalArgumentException e) {
                                ForgeTPS.LOGGER.warn("Invalid color format: {} for dimension: {}",
                                        entry.getValue(), entry.getKey());
                            }
                        }
                    }
                } catch (Exception e) {
                    ForgeTPS.LOGGER.error("Error parsing customDimensions: {}", e.getMessage());
                }
            }

            ForgeTPS.LOGGER.info("Config loaded successfully");

        } catch (JsonSyntaxException e) {
            ForgeTPS.LOGGER.error("Config file has invalid JSON syntax: {}", e.getMessage());
            saveDefaultConfig();
        } catch (IOException e) {
            ForgeTPS.LOGGER.error("Error reading config file: {}", e.getMessage());
            saveDefaultConfig();
        } catch (Exception e) {
            ForgeTPS.LOGGER.error("Unexpected error loading config: {}", e.getMessage());
            e.printStackTrace();
            saveDefaultConfig();
        }
    }

    private static void saveDefaultConfig() {
        try {
            // Создаем родительскую директорию, если она не существует
            Files.createDirectories(CONFIG_PATH.getParent());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                Map<String, Object> config = new HashMap<>();
                config.put("tabHeader", tabHeader);
                config.put("tabFooter", tabFooter);
                config.put("enableNicknameColorChange", enableNicknameColorChange);

                Map<String, String> dimensions = new HashMap<>();
                dimensions.put("minecraft:overworld", "GREEN");
                dimensions.put("minecraft:the_nether", "RED");
                dimensions.put("minecraft:the_end", "LIGHT_PURPLE");
                dimensions.put("undergarden:undergarden", "DARK_GREEN");
                dimensions.put("ae2:spatial_storage", "WHITE");

                config.put("customDimensions", dimensions);

                writer.write(GSON.toJson(config));
                ForgeTPS.LOGGER.info("Default config saved to {}", CONFIG_PATH);
            }
        } catch (IOException e) {
            ForgeTPS.LOGGER.error("Failed to save default config: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
