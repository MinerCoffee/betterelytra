package me.minercoffee.betterelytra.v1;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleData {
    private static final Map<UUID, Integer> Trails = new HashMap<>();
    private final UUID uuid;

    public ParticleData(UUID uuid) {
        this.uuid = uuid;
    }

    public void setID(int id){
        Trails.put(uuid, id);
    }
    public int getID(){
        return Trails.get(uuid);
    }
    public boolean hasID(){
        return Trails.containsKey(uuid);
    }
    public void  removeID(){
        Trails.remove(uuid);
    }
    public void endTask(){
        if (getID() == 1) return;
        Bukkit.getScheduler().cancelTask(getID());
    }
    public  boolean hasFakeID(UUID uuid){
        if (Trails.containsKey(uuid)){
            return Trails.get(uuid) == 1;
        }
        return false;
    }
    public void stop() {
        if (hasID()) {
            endTask();
            removeID();
        }
    }
}
