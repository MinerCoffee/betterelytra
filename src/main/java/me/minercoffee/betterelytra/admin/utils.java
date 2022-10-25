package me.minercoffee.betterelytra.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class utils {

    public utils() {
    }

    public static void openMainMenu(Player p) {
        Inventory recipemainmenu = Bukkit.createInventory(p, 9, ChatColor.BLUE + "Main menu");
        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemStack recipe = new ItemStack(Material.ELYTRA, 1);
        ArrayList<Player> list = new ArrayList<>(p.getServer().getOnlinePlayers());
        for (Player player : list) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
            ItemMeta meta = playerHead.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(player.getDisplayName());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GOLD + "Player Health: " + ChatColor.RED + player.getHealth());
                lore.add(ChatColor.GOLD + "EXP:" + ChatColor.AQUA + player.getExp());
                lore.add(ChatColor.GOLD + "Ping:" + ChatColor.AQUA + player.getPing());
                meta.setLore(lore);
                playerHead.setItemMeta(meta);
            }
            recipemainmenu.setItem(0, playerHead);
        }
        ItemMeta meta = recipe.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("recipe");
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            recipe.setItemMeta(meta);
        }
        recipemainmenu.setItem(4, recipe);
        ItemMeta metaclose = close.getItemMeta();
        if (metaclose != null) {
            metaclose.setDisplayName("Close");
            metaclose.addEnchant(Enchantment.DURABILITY, 1, true);
            metaclose.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            close.setItemMeta(metaclose);
        }
        recipemainmenu.setItem(9, close);
        p.openInventory(recipemainmenu);
    }
}
