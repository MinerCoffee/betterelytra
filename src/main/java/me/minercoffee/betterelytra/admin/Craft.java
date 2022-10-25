package me.minercoffee.betterelytra.admin;

import me.minercoffee.betterelytra.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import static me.minercoffee.betterelytra.Main.checkPlayerperms;
import static me.minercoffee.betterelytra.admin.utils.openMainMenu;

public class Craft implements CommandExecutor, Listener {
    public Craft(@NotNull Main plugin){
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(this, plugin);
        plugin.getCommand("bcraft").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (label.equalsIgnoreCase("bcraft")) return true;
        if (sender instanceof Player p) {
            if (checkPlayerperms(p)) return true;
            if (args.length == 0) {
                openMainMenu(p);
            }
        }
        return true;
    }
    @EventHandler
    public void onMenuClick(@NotNull InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Main menu")) {
            switch (e.getCurrentItem().getType()) {
                case BARRIER -> {
                    p.closeInventory();
                    e.setCancelled(true);
                }
                case ELYTRA -> {
                    CraftMenu(p);
                    e.setCancelled(true);
                }
                case PLAYER_HEAD, AIR, VOID_AIR, CAVE_AIR -> e.setCancelled(true);
            }
            e.setCancelled(true);
        }
    }
    private void CraftMenu(@NotNull Player p){
        Inventory craft = Bukkit.createInventory(null, InventoryType.CRAFTING, "Crafting");
        p.openInventory(craft);
    }
}
