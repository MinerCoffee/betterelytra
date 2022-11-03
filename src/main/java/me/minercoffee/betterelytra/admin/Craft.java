package me.minercoffee.betterelytra.admin;

import me.minercoffee.betterelytra.Main;
import me.minercoffee.betterelytra.utils.DataManager;
import me.minercoffee.betterelytra.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static me.minercoffee.betterelytra.Main.checkPlayerperms;
import static me.minercoffee.betterelytra.Main.plugin;

public class Craft implements CommandExecutor, Listener {
    public Craft(@NotNull Main plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(this, plugin);
        plugin.getCommand("bcraft").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (label.equalsIgnoreCase("bcraft")) {
            if (sender instanceof Player p) {
                if (checkPlayerperms(p)) {
                    if (args.length == 0) {
                        MainMenu(p);
                    }
                }
            }
        }
        return true;
    }
    @EventHandler
    public void onMenuClick(@NotNull InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory().equals(mainmenu)) {
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Main menu")) {
                if (e.getSlotType().equals(InventoryType.SlotType.CONTAINER)) {
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
                } else {
                    e.setCancelled(false);
                }
            }
        }
    }
    @EventHandler
    public void onCraft(@NotNull PrepareItemCraftEvent e){ //TODO use onCraftMenu and use the list or single string to set the matrix to.
        ItemBuilder itemBuilder = new ItemBuilder();
        ItemStack result = itemBuilder.getElytra();
        if (e.getInventory().getMatrix().length < 9){
            return;
        }
        String a = plugin.getConfig().getString("ingame_editor.index_0");
        String b = plugin.getConfig().getString("ingame_editor.index_1");
        String c = plugin.getConfig().getString("ingame_editor.index_2");
        String d = plugin.getConfig().getString("ingame_editor.index_3");
        String E = plugin.getConfig().getString("ingame_editor.index_4");
        String f = plugin.getConfig().getString("ingame_editor.index_5");
        String g = plugin.getConfig().getString("ingame_editor.index_6");
        String h = plugin.getConfig().getString("ingame_editor.index_7");
        String i = plugin.getConfig().getString("ingame_editor.index_8");

        Material A = null;
        if (a != null) {
            A = Material.matchMaterial(a);
        }
        ItemStack maxtrix_0 = null;
        if (A != null) {
            maxtrix_0 = new ItemStack(A, 1);
        }
        Material B = null;
        if (b != null) {
            B = Material.matchMaterial(b);
        }
        if (B != null) {
            ItemStack maxtrix_1 = new ItemStack(B, 1);
        }
        Material C = null;
        if (c != null) {
            C = Material.matchMaterial(c);
        }
        if (C != null) {
            ItemStack maxtrix_2 = new ItemStack(C, 1);
        }
        Material D = null;
        if (d != null) {
            D = Material.matchMaterial(d);
        }
        if (D != null) {
            ItemStack maxtrix_3 = new ItemStack(D, 1);
        }
        Material ef = null;
        if (E != null) {
            ef = Material.matchMaterial(E);
        }
        if (ef != null) {
            ItemStack maxtrix_4 = new ItemStack(ef, 1);
        }
        Material F = null;
        if (f != null) {
            F = Material.matchMaterial(f);
        }
        if (F != null) {
            ItemStack maxtrix_5 = new ItemStack(F, 1);
        }
        Material G = null;
        if (g != null) {
            G = Material.matchMaterial(g);
        }
        if (G != null) {
            ItemStack maxtrix_6 = new ItemStack(G, 1);
        }
        Material H = null;
        if (h != null) {
            H = Material.matchMaterial(h);
        }
        if (H != null) {
            ItemStack maxtrix_7 = new ItemStack(H, 1);
        }
        Material I = null;
        if (i != null) {
            I = Material.matchMaterial(i);
        }
        if (I != null) {
            ItemStack maxtrix_8 = new ItemStack(I, 1);
        }
        //method
        ItemStack finalMaxtrix_ = maxtrix_0;
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(4, finalMaxtrix_);
        }});
    }
    private void checkCraft(ItemStack result, @NotNull CraftingInventory inv, HashMap<Integer, ItemStack> ingredients){
        ItemStack [] matrix = inv.getMatrix();
        for (int i = 0; i < 9; i++){
            if (ingredients.containsKey(i)){
                if(matrix[i] == null || !matrix[i].equals(ingredients.get(i))){
                    return;
                }
            }else {
                if(matrix[1] != null){
                    return;
                }
            }
        }
        inv.setResult(result);
    }
    @EventHandler
    public void onCraftMenu(@NotNull InventoryClickEvent  e) { //TODO Make them save the item in the slot to the config.yml. Either by making a list or having separate cases.
        ItemBuilder itemBuilder = new ItemBuilder();
        Player p = (Player) e.getWhoClicked();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "CharcoalElytrakeyv1");
        ShapelessRecipe elytra = new ShapelessRecipe(key, itemBuilder.getElytra());
        ItemStack item = e.getCurrentItem();
        Configuration config = plugin.getConfig();
        if (e.getClickedInventory().equals(craftmenu)) {
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Ingame")) {
                if (item == null) return;
                switch (e.getSlot()) {
                    case 0 -> {
                        config.set("ingame_editor.index_0", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case   1 -> {
                        config.set("ingame_editor.index_1", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 11 -> {
                        config.set("ingame_editor.index_2", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 18 -> {
                        config.set("ingame_editor.index_3", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 19 ->{
                        config.set("ingame_editor.index_4", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 20 -> {
                        config.set("ingame_editor.index_5", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 10 ->  {
                        config.set("ingame_editor.index_6", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 9 -> {
                        config.set("ingame_editor.index_7", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 2 -> {
                        config.set("ingame_editor.index_8", item.getType());
                        System.out.println("test");
                        plugin.saveConfig();
                    }
                    case 3, 4, 5, 6, 7, 8, 16, 12, 13, 14, 17, 22, 21, 23, 24, 25 -> e.setCancelled(true);
                    case 15 -> {
                        e.setCancelled(true);
                        p.sendMessage("Saving recipe");
                        Bukkit.removeRecipe(key);
                        Bukkit.addRecipe(elytra);
                    }
                    case 26 -> {
                        e.setCancelled(true);
                        e.getClickedInventory().setItem(0,null);
                        e.getClickedInventory().setItem(1,null);
                        e.getClickedInventory().setItem(11,null);
                        e.getClickedInventory().setItem(18,null);
                        e.getClickedInventory().setItem(19,null);
                        e.getClickedInventory().setItem(20,null);
                        e.getClickedInventory().setItem(10,null);
                        e.getClickedInventory().setItem(9,null);
                        e.getClickedInventory().setItem(2,null);
                        p.closeInventory();
                        p.updateInventory();
                    }
                }
            } else {
                e.setCancelled(false);
            }
        }
    }
    private final Inventory craftmenu = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Ingame");
    private void CraftMenu(@NotNull Player p) {
        ItemBuilder itemBuilder = new ItemBuilder();
        ItemStack save = new ItemStack(Material.EMERALD_BLOCK, 1);
        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemStack fill = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemStack bench =  new ItemStack(Material.CRAFTING_TABLE);
        ItemStack result = itemBuilder.getElytra();
        ItemMeta savemeta = save.getItemMeta();
        if (savemeta != null){
            savemeta.addEnchant(Enchantment.DURABILITY, 1, false);
            savemeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            savemeta.setDisplayName("Save");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Click Save when you are ready to update the recipe.");
            savemeta.setLore(lore);
            save.setItemMeta(savemeta);
        }
        ItemMeta closemeta = close.getItemMeta();
        if (closemeta != null){
            closemeta.addEnchant(Enchantment.DURABILITY, 1, false);
            closemeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            closemeta.setDisplayName("Close");
            close.setItemMeta(closemeta);
        }
        ItemMeta meta = fill.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(" ");
            fill.setItemMeta(meta);
        }
        ItemMeta benchmeta = bench.getItemMeta();
        if (benchmeta != null){
            benchmeta.addEnchant(Enchantment.DURABILITY, 1, false);
            benchmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            benchmeta.setDisplayName("Crafting");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Drag the items on the slots.");
            lore.add(" and click Save.");
            lore.add("You may use up to 5 materials.");
            benchmeta.setLore(lore);
            bench.setItemMeta(benchmeta);
        }
        craftmenu.setItem(3, fill);
        craftmenu.setItem(4, fill);
        craftmenu.setItem(5, fill);
        craftmenu.setItem(6, fill);
        craftmenu.setItem(7, fill);
        craftmenu.setItem(8, fill);
        craftmenu.setItem(12, fill);
        craftmenu.setItem(16, fill);
        craftmenu.setItem(17, fill);
        craftmenu.setItem(21, fill);
        craftmenu.setItem(22, fill);
        craftmenu.setItem(23, fill);
        craftmenu.setItem(24, fill);
        craftmenu.setItem(25, fill);
        craftmenu.setItem(13, bench);
        craftmenu.setItem(14, result);
        craftmenu.setItem(15, save);
        craftmenu.setItem(26, close);
        p.openInventory(craftmenu);
    }
    private final Inventory mainmenu = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BLUE + "Main menu");
    private void MainMenu(@NotNull Player p) {
        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemStack recipe = new ItemStack(Material.ELYTRA, 1);
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = playerHead.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(p.getDisplayName());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Player Health: " + ChatColor.RED + p.getHealth());
            lore.add(ChatColor.GOLD + "EXP: " + ChatColor.AQUA + p.getExp());
            lore.add(ChatColor.GOLD + "Ping: " + ChatColor.AQUA + p.getPing());
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
        }
        mainmenu.setItem(0, playerHead);
        ItemMeta recipeItemMeta = recipe.getItemMeta();
        if (recipeItemMeta != null) {
            recipeItemMeta.setDisplayName("recipe");
            recipeItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            recipeItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            recipe.setItemMeta(recipeItemMeta);
        }
        mainmenu.setItem(2, recipe);
        ItemMeta metaclose = close.getItemMeta();
        if (metaclose != null) {
            metaclose.setDisplayName("Close");
            metaclose.addEnchant(Enchantment.DURABILITY, 1, true);
            metaclose.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            close.setItemMeta(metaclose);
        }
        mainmenu.setItem(4, close);
        p.openInventory(mainmenu);
    }
}