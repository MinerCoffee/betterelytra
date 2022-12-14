package me.minercoffee.betterelytra;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import me.minercoffee.betterelytra.admin.Craft;
import me.minercoffee.betterelytra.admin.give;
import me.minercoffee.betterelytra.admin.reload;
import me.minercoffee.betterelytra.utils.DataManager;
import me.minercoffee.betterelytra.utils.ItemBuilder;
import me.minercoffee.betterelytra.utils.Metrics;
import me.minercoffee.betterelytra.utils.UpdateCheckCommand;
import me.minercoffee.betterelytra.v1.ElytraListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.spigotmc.SpigotConfig.config;

public final class Main extends JavaPlugin implements Listener {
    public DataManager data;
    private final ItemBuilder itemBuilder = new ItemBuilder();

    public static void setInstance(Main instance) {
        Main.plugin = instance;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static Main plugin;

    public Main() {
    }

    @Override
    public void onEnable() {
        setInstance(this);
        PluginManager pm = getServer().getPluginManager();
        new Craft(this);
        pm.registerEvents(this, Main.this);
        new ElytraListener(this);
        getCommand("updater").setExecutor(new UpdateCheckCommand());
        getCommand("bereload").setExecutor(new reload());
        this.data = new DataManager(this);
        getCommand("elytra").setExecutor(new give());
        itemBuilder.init();
        ServerUtils();
        Recipes();
        updateConfig();
        try {
            new UpdateChecker(this, UpdateCheckSource.CUSTOM_URL, "https://github.com/MinerCoffee/betterelytra/blob/master/src/main/resources/lastestversion.txt")
                    .setDownloadLink("https://discord.com/channels/941600403513040916/941600403513040919")
                    .setChangelogLink("https://discord.gg/5nDbUY2qFy")
                    .setDonationLink("https://www.paypal.com/paypalme/MinerCoffee")
                    .setNotifyOpsOnJoin(true)
                    .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                    .setColoredConsoleOutput(true)
                    .checkEveryXHours(24)
                    .checkNow();
        } catch (Exception e){
            getLogger().log(Level.SEVERE, "Github site is down or the file location has been moved." + e);
            e.printStackTrace();
        }
    }
    public void Recipes() {
        NamespacedKey elytrav1 = new NamespacedKey(this, "CharcoalElytrakeyv1");
        ShapelessRecipe elytra = new ShapelessRecipe(elytrav1, itemBuilder.getElytra());
        try {
            if (!config.getBoolean("ingame_editor")) {
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_A")));
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_B")));
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_C")));
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_D")));
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_D")));
                elytra.addIngredient(Material.valueOf(plugin.getConfig().getString("recipe.ingredients_E")));
                plugin.saveConfig();
                getServer().addRecipe(elytra);
                data.reloadConfig();
            }
        }catch(IllegalArgumentException exception){
                exception.printStackTrace();
            }
    }
    public void ServerUtils() {
        int pluginId = 16692;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
    }
    public void updateConfig(){
        ConfigurationSection config = getConfig();
        config.addDefault("messages", "");
        config.addDefault("messages.launch", "&4&lYou are taking off! &r&l&8&nPress Shift to boost yourself!");
        config.addDefault("messages.cooldown", "&8&lYou cannot boost yet");
        saveConfig();
    }

    public static boolean checkPlayerperms(@NotNull Player p) {
        return p.hasPermission("BetterElytra.staff") || p.isOp();
    }
    @Override
    public void onDisable() {
    }
}