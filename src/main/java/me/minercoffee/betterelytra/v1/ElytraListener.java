package me.minercoffee.betterelytra.v1;

import me.minercoffee.betterelytra.Main;
import me.minercoffee.betterelytra.utils.ColorMsg;
import me.minercoffee.betterelytra.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.minercoffee.betterelytra.Main.checkPlayerperms;

public class ElytraListener implements Listener {
    private final Main plugin;
    private final List<Player> sneakingPlayers = new ArrayList<>();
    private final List<Player> chargingPlayers = new ArrayList<>();
    private final HashMap<Player, Long> cooldowns = new HashMap<>();

    public ElytraListener(@NotNull Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean isHoldingSneak(Player p) {
        return this.sneakingPlayers.contains(p);
    }
    private final ItemBuilder itemBuilder = new ItemBuilder();

    private static int getCooldown() {
        return Main.getPlugin().getConfig().getInt("cooldown") * 1000;
    }

    private float getVelocityMultiplier() {
        return Main.getPlugin().getConfig().getInt("speed");
    }

    @EventHandler
    void onQuit(@NotNull PlayerQuitEvent e){
        ParticleData p = new ParticleData(e.getPlayer().getUniqueId());
        if (p.hasID()){
            p.endTask();
        }
    }

    @EventHandler
    public void boost(@NotNull PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ConfigurationSection config = Main.getPlugin().getConfig();
        ParticleData particleData = new ParticleData(p.getUniqueId());
        Effects trails = new Effects(p);
        if (p.isGliding()) {
            if (this.isHoldingSneak(p)) {
                if (itemBuilder.hasCharcoalElytra(p)) {
                    if (this.cooldowns.containsKey(p) && this.cooldowns.get(p) > System.currentTimeMillis()) {
                        particleData.stop();
                        p.sendMessage(ColorMsg.color(config.getString("messages.cooldown")));
                    } else {
                        p.spawnParticle(Particle.ASH, p.getLocation(), 30, 2, 0, 2, 1);
                        this.cooldowns.put(p, System.currentTimeMillis() + (long) getCooldown());
                        p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 10.0F, 3.0F);
                        p.setVelocity(p.getLocation().getDirection().multiply(this.getVelocityMultiplier()));
                        trails.startTotem();
                    }
                }
            }
        } else {
            particleData.stop();
        }
    }
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
       e.getPlayer().setResourcePack("https://www.dropbox.com/sh/gl3vifm2pal1hxr/AAA-U-1deNQrOn2HLWaosgqVa?dl=1"); //direct download to your texture pack

    }

    @EventHandler
    public void launch(@NotNull PlayerStatisticIncrementEvent e) {
        Player p = e.getPlayer();
        ParticleData particleData = new ParticleData(p.getUniqueId());
        Effects trails = new Effects(p);
        if (itemBuilder.hasCharcoalElytra(p)) {
            if (e.getStatistic().equals(Statistic.JUMP)) {
                if (!(p.getLocation().getPitch() < -90.0F)) {
                    if (this.chargingPlayers.contains(p) && ChargeBar.charged.contains(p)) {
                        ChargeBar.chargeBar.removePlayer(p);
                        p.setVelocity(p.getLocation().getDirection().multiply(1).setY(8));
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 10.0F, 1.0F);
                        trails.startSmoke();
                    }
                } else {
                    particleData.stop();
                }
            }
        }
    }

    @EventHandler
    public void sneak(@NotNull PlayerToggleSneakEvent e) {
        final Player p = e.getPlayer();
        if (itemBuilder.hasCharcoalElytra(p)) {
            if (e.isSneaking()) {
                this.sneakingPlayers.add(p);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> ElytraListener.this.sneakingPlayers.remove(p), 1L);
                if (!p.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType().isAir()) {
                    p.getLocation().getBlock();
                    if (p.getLocation().getBlock().getType() != Material.WATER) {
                        if (!p.isFlying() && !p.isGliding() && !p.isSwimming()) {
                            this.chargingPlayers.add(p);
                            ChargeBar.run(p);
                        }
                    }
                }
            } else {
                ChargeBar.stop(p);
                this.chargingPlayers.remove(p);
                this.sneakingPlayers.remove(p);
            }
        }
    }
    @EventHandler
    public void remove(@NotNull PlayerJoinEvent e){
        FileConfiguration config = Main.getPlugin().getConfig();
        if(checkPlayerperms(e.getPlayer())){
            if (config.getBoolean("remove_recipe")){
                NamespacedKey elytrav1 = new NamespacedKey(Main.getPlugin(), "CharcoalElytrakeyv1");
                Main.getPlugin().getServer().removeRecipe(elytrav1);
            }
        }
    }
    @EventHandler
    public void Flydamage(@NotNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (itemBuilder.hasCharcoalElytra(p)) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL) || (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void AnvilUsage(@NotNull PrepareAnvilEvent e) {
        Player player = (Player) e.getView().getPlayer();
        AnvilInventory inv = e.getInventory();
        if (inv.getItem(0) == null || inv.getItem(1) == null) return;
        ItemStack item = new ItemStack(Objects.requireNonNull(inv.getItem(0)));
        ItemStack enchantment = new ItemStack(Objects.requireNonNull(e.getInventory().getItem(1)));
        if (item.isSimilar(itemBuilder.Elytra)) {
            ItemMeta meta_enchantment = enchantment.getItemMeta();
            item.setItemMeta(meta_enchantment);
            e.setResult(item);
            player.updateInventory();
            player.closeInventory();
        }
    }
    @EventHandler
    public void onPrepareCraft(@NotNull PrepareItemCraftEvent e){
        ItemStack A = plugin.getConfig().getItemStack("recipe.ingredients_A");
        ItemStack B = plugin.getConfig().getItemStack("recipe.ingredients_B");
        ItemStack C = plugin.getConfig().getItemStack("recipe.ingredients_C");
        ItemStack D = plugin.getConfig().getItemStack("recipe.ingredients_D");
        ItemStack result = itemBuilder.getElytra();
        if (e.getInventory().getMatrix().length < 9){
            return;
        }
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(0, B);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(1, A);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(2, B);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(3, A);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(4, C);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(5, A);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(6, D);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(7, A);
        }});
        checkCraft(result, e.getInventory(), new HashMap<>() {{
            put(8, D);
        }});
    }
    public void checkCraft(ItemStack result, @NotNull CraftingInventory inv, HashMap<Integer, ItemStack> ingredients){
        ItemStack[] matrix = inv.getMatrix();
        for (int i = 0; i <9; i++){
            if (ingredients.containsKey(i)){
                if (matrix[i] == null || !matrix[i].equals(ingredients.get(i))){
                    return;
                }
            } else {
                if (matrix[i] != null) {
                    return;
                }
            }
        }
        inv.setResult(result);
    }
}