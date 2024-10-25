package com.fanya.tabtps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {

    public static String tabHeader;
    public static String tabFooter;
    public static boolean enableNicknameColorChange;
    public static Map<String, ChatFormatting> customDimensionsColors = new HashMap<>();

    private static final Gson GSON = new Gson();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("tabtps.json");

    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            Map<String, Object> config = GSON.fromJson(reader, Map.class);

            tabHeader = (String) config.getOrDefault("tabHeader", "Добро пожаловать, %playername%!");
            tabFooter = (String) config.getOrDefault("tabFooter", "TPS: %tps% | Ping: %ping% | MSPT: %mspt%");
            enableNicknameColorChange = (boolean) config.getOrDefault("enableNicknameColorChange", true);

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> dimensions = GSON.fromJson(GSON.toJson(config.get("customDimensions")), type);

            for (Map.Entry<String, String> entry : dimensions.entrySet()) {
                try {
                    ChatFormatting color = ChatFormatting.valueOf(entry.getValue());
                    customDimensionsColors.put(entry.getKey(), color);
                } catch (IllegalArgumentException e) {

                }
            }

        } catch (IOException e) {
            saveDefaultConfig();
        }
    }

    private static void saveDefaultConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            Map<String, Object> config = new HashMap<>();
            config.put("tabHeader", "Hello, %playername%!");
            config.put("tabFooter", "TPS: %tps% | Ping: %ping% | MSPT: %mspt%");
            config.put("enableNicknameColorChange", true);
            Map<String, String> dimensions = new HashMap<>();
            dimensions.put("minecraft:overworld", "GREEN");
            dimensions.put("minecraft:the_nether", "RED");
            dimensions.put("minecraft:the_end", "LIGHT_PURPLE");
            dimensions.put("undergarden:undergarden", "DARK_GREEN");
            dimensions.put("ae2:spatial_storage", "WHITE");

            config.put("customDimensions", dimensions);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
