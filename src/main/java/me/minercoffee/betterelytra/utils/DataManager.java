package me.minercoffee.betterelytra.utils;

import me.minercoffee.betterelytra.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;


    public DataManager(@NotNull Main plugin){
        this.plugin = plugin;
        //saves/initializes
        saveDefaultConfig();
        loadFromResource("config.yml", new File(plugin.getDataFolder(), "config.yml"));
    }

    public void reloadConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("config.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
        saveConfig();
    }
    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig(){
        if (this.dataConfig == null || this.configFile == null)
            return;
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }
    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }
        public static @Nullable FileConfiguration loadFromResource(String resourceName, @NotNull File out){
            try {
                InputStream in = Main.getPlugin().getResource(resourceName);

                if (!out.exists()){
                    Main.getPlugin().getDataFolder().mkdir();
                    out.createNewFile();
                }
                FileConfiguration file = YamlConfiguration.loadConfiguration(out);
                if (in != null){
                    InputStreamReader inReader =  new InputStreamReader(in);
                    file.setDefaults(YamlConfiguration.loadConfiguration(inReader));
                    file.options().copyDefaults(true);
                    file.save(out);
                }
                return file;
            } catch (IOException ex){
                ex.printStackTrace();
                return null;
            }
        }
    }