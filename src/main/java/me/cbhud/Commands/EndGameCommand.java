package me.cbhud.Commands;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class EndGameCommand implements org.bukkit.command.CommandExecutor {
    private final Main plugin;

    public EndGameCommand(Main plugin) {
        this.plugin = plugin;
    }


    private void teleportPlayersToLobby() {
        FileConfiguration config = plugin.getConfig();

        if (config.contains("lobby.world")) {
            String worldName = config.getString("lobby.world");
            double x = config.getDouble("lobby.x");
            double y = config.getDouble("lobby.y");
            double z = config.getDouble("lobby.z");
            float yaw = (float) config.getDouble("lobby.yaw");
            float pitch = (float) config.getDouble("lobby.pitch");

            Location lobbyLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(lobbyLocation);
                plugin.getSpectatorManager().setPlayerAsLobby(player);
            }
        } else {
            Bukkit.getLogger().warning("World is null!");
        }
    }
    private void removeCustomZombie() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null &&
                        entity.getCustomName().equals("§6§lKing Charles")) {
                        entity.remove();
                }
            }
        }
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            if (sender.hasPermission("war.start")) {
                    removeCustomZombie();
                    plugin.getCountdownTimer().cancelTimer();
                    teleportPlayersToLobby();
                    plugin.getGame().setState(GameState.LOBBY);
                Bukkit.broadcastMessage(ChatColor.RED + "The Game has been force-stopped!");

            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The game is not in GAME STATE. You cannot end it now.");
        }

        return true;
    }
}
