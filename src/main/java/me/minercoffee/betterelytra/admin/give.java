package me.minercoffee.betterelytra.admin;

import me.minercoffee.betterelytra.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static me.minercoffee.betterelytra.Main.checkPlayerperms;

public class give implements TabExecutor {
    private final ItemBuilder itemBuilder = new ItemBuilder();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players can run this command.");
        } else {
            if (label.equalsIgnoreCase("elytra")) {
                if (args.length == 0) {
                    return true;
                }
                if (args.length == 1){
                    if (checkPlayerperms(p) || p.isOp()) {
                        p.getInventory().addItem(itemBuilder.getElytra());
                    } else {
                        sender.sendMessage("You are not allowed to use this command.");
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsarguements = new ArrayList<>();
            subcommandsarguements.add("give");
            return subcommandsarguements;
        }
        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }
            return playerNames;
        }
        return null;
    }
}
