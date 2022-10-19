package me.minercoffee.betterelytra.v1;

import me.minercoffee.betterelytra.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Effects {

    private int taskID;
    private final Player player;

    public Effects(Player player) {
        this.player = player;
    }
    public void startTotem() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            double var = 0;
            Location loc, first, second;
            final ParticleData particle = new ParticleData(player.getUniqueId());
            @Override
            public void run() {
                if (!particle.hasID()){
                    particle.setID(taskID);
                }
                var += Math.PI / 16;
                loc = player.getLocation();
                first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                player.getWorld().spawnParticle(Particle.TOTEM, first, 0);
                player.getWorld().spawnParticle(Particle.TOTEM, second, 0);
            }
        }, 0 ,1); //changes the speed
    }
    public void startSmoke(){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            double var = 0;
            Location loc, first, second;
            final ParticleData particle = new ParticleData(player.getUniqueId());
            @Override
            public void run() {
                if (!particle.hasID()){
                    particle.setID(taskID);
                }
                var += Math.PI / 16;
                loc = player.getLocation();
                first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                player.getWorld().spawnParticle(Particle.SMOKE_LARGE, first, 0);
                player.getWorld().spawnParticle(Particle.SMALL_FLAME, second, 0);
            }
        }, 0 ,1); //changes the speed
    }
}