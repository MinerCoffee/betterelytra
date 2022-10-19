package me.minercoffee.betterelytra.admin;

import me.minercoffee.betterelytra.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.minercoffee.betterelytra.Main.checkPlayerperms;

public class reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("bereload")) {
            Player p = (Player) sender;
            if (checkPlayerperms(p) || p.isOp()) {
                if (args.length == 0) {
                    Main.getPlugin().reloadConfig();
                    p.sendMessage("The config has been reloaded!");
                } else {
                    sender.sendMessage("You are not allowed to use this command.");
                }
            }
        }
        return true;
    }


}
