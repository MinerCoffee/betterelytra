package me.minercoffee.betterelytra.v1;

import me.minercoffee.betterelytra.Main;
import me.minercoffee.betterelytra.utils.ColorMsg;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChargeBar {
    public static BossBar chargeBar;
    public static ArrayList<Player> charged = new ArrayList<>();
    public static int task;

    public ChargeBar() {
    }
    public static void run(Player p) {
        chargeBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        chargeBar.setProgress(0.0D);
        chargeBar.setVisible(false);
        chargeBar.addPlayer(p);
        charge(p);
    }
    public static void stop(Player p) {
        Bukkit.getScheduler().cancelTask(task);
        if (chargeBar != null) {
            chargeBar.removePlayer(p);
        }
        charged.remove(p);
    }
    private static void charge(final Player p) {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            double progress = 0.0D;
            final double time = 0.025D;
            public void run() {
                ChargeBar.chargeBar.setProgress(this.progress);
                this.progress += time;
                if (this.progress >= 1.0D) {
                    Bukkit.getScheduler().cancelTask(ChargeBar.task);
                    ChargeBar.chargeBar.setProgress(1.0D);
                    ChargeBar.charged.add(p);
                    ChargeBar.runWhenFull(p);
                }

            }
        }, 0L, 1L);
    }
    private static void runWhenFull(@NotNull Player p) {
        ConfigurationSection config = Main.getPlugin().getConfig();
        p.sendMessage(ColorMsg.color(config.getString("messages.launch")));
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10, 0, false, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 0, false, false, false));
        p.spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 80, 5, 10, 5);
    }
}