package me.latifil.bunkers.config;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    private final File file;
    private YamlConfiguration configuration;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Config(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        try {
            Files.createDirectories(file.getParentFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create plugin data folder", e);
        }

        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public double getDouble(String path) {
        return configuration.getDouble(path, 0.0);
    }

    public int getInt(String path) {
        return configuration.getInt(path, 0);
    }

    public boolean getBoolean(String path) {
        return configuration.getBoolean(path, false);
    }

    public Component getComponent(String path) {
        String raw = configuration.getString(path);
        if (raw == null) return null;
        return MINI_MESSAGE.deserialize(raw);
    }

    public String getMiniMessageString(String path) {
        Component component = getComponent(path);
        return component != null ? MINI_MESSAGE.serialize(component) : null;
    }

    public List<String> getStringList(String path) {
        if (!configuration.contains(path)) {
            return List.of("Invalid path.");
        }

        return configuration.getStringList(path).stream()
                .map(s -> MINI_MESSAGE.serialize(MINI_MESSAGE.deserialize(s)))
                .collect(Collectors.toList());
    }

    public List<String> getReversedStringList(String path) {
        List<String> list = getStringList(path);
        if (list == null || (list.size() == 1 && list.getFirst().equals("Invalid path."))) {
            return List.of("Invalid path.");
        }

        Collections.reverse(list);
        return list;
    }

    public void setLocation(String path, Location location) {
        configuration.set(path, location);
    }

    public Location getLocation(String path) {
        return configuration.getLocation(path);
    }
}
