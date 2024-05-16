package net.resolutemc.dragonfly.config;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public enum ConfigCreator {
    MESSAGES;

    public File getFile() {
        return new File(DragonFly.getInstance().getDataFolder(), this.toString().toLowerCase(Locale.ROOT) + ".yml");
    }

    public FileConfiguration get() {
        return YamlConfiguration.loadConfiguration(getFile());
    }

    public void save(FileConfiguration configuration) {
        try {
            configuration.save(getFile());
        }catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void create() {
        DragonFly.getInstance().saveResource(this.toString().toLowerCase(Locale.ROOT) + ".yml", false);
    }
}