package me.cbhud.Commands;

import me.cbhud.items.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



/*
BASICALLY I CREATED COMMANDS FOR THIS SO I CAN TEST IT EASIER OFC
I WOULD NEED TO ADD MORE CHECKS AND GIVE <USERNAME> ALSO THROUGH CONSOLE
ITS JUST FOR TEST PURPOSE
 */
public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("givesoup")) {
            player.getInventory().addItem(Manager.stew);
        } else if (cmd.getName().equalsIgnoreCase("giveaxe")) {
            player.getInventory().addItem(Manager.axe);
        } else if (cmd.getName().equalsIgnoreCase("giveblood")) {
            player.getInventory().addItem(Manager.blood);
        }

        return true;
    }
}
