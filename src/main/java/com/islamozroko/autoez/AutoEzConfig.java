package com.islamozroko.autoez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoEzConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("autoez.json").toFile();
    
    public boolean enabled = true;
    public int radius = 15;
    public List<String> phrases = new ArrayList<>(Arrays.asList("ez", "gg", "изi"));
    
    public static AutoEzConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                AutoEzConfig config = GSON.fromJson(reader, AutoEzConfig.class);
                if (config != null) return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AutoEzConfig config = new AutoEzConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
