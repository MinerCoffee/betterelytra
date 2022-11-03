package me.minercoffee.betterelytra.utils;

import me.minercoffee.betterelytra.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class ItemBuilder {
    public ItemBuilder(){
    }
    public ItemStack Elytra;
    public void init(){
        getElytra();
    }
    public ItemStack getElytra() {
        ItemStack item = new ItemStack(Material.ELYTRA, 1);
        ItemMeta meta = item.getItemMeta();
        String name = Main.getPlugin().getConfig().getString("name");
        if (meta != null) {
            meta.setDisplayName(ColorMsg.color(name));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ColorMsg.color("&2Crouch &7& &2Jump &7to Launch into the Air."));
            lore.add(ColorMsg.color("&7Press &2Shift &7While Flying To Boost."));
            meta.setLore(lore);
            meta.setCustomModelData(1997);
            item.setItemMeta(meta);
            Elytra = item;
        }
        return Elytra;
    }
    public boolean hasCharcoalElytra(@NotNull Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack chest = inventory.getChestplate();
        if (chest == null) return true;
        if (!chest.getItemMeta().hasCustomModelData()) {
            if (chest.clone().isSimilar(Elytra) || chest.isSimilar(Elytra)){
                ItemMeta meta = Elytra.getItemMeta();
                if (meta != null) {
                    meta.setCustomModelData(1997);
                    Elytra.setItemMeta(meta);
                }
            }
        }
        if (chest.getItemMeta().getCustomModelData() == 1997) return true;
        if (chest.clone().isSimilar(Elytra) || chest.isSimilar(Elytra)) return true;
        return chest.isSimilar(Elytra) || inventory.contains(Elytra);
    }
}
