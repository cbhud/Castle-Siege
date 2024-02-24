package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
public class RespawnEvent implements Listener {
    private final Main plugin;

    private Location teamVikingSpawn;
    private Location teamFranksSpawn;

    public RespawnEvent(Main plugin) {
        this.plugin = plugin;
        loadSpawnLocations();
    }

    private void loadSpawnLocations() {
        FileConfiguration config = plugin.getConfig();
        teamVikingSpawn = getSpawnLocationForTeam(config, Team.VIKINGS);
        teamFranksSpawn = getSpawnLocationForTeam(config, Team.FRANKS);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            Player player = event.getEntity();
            event.getDrops().clear();
            plugin.getSpectatorManager().setPlayerAsSpectator(player);
            player.sendTitle(ChatColor.RED + "You died!", ChatColor.GRAY + "Respawning... 5 seconds", 10, 70, 20);

            Team team = plugin.getTeamManager().getTeam(player);
            Location spawnLocation = getSpawnLocationForTeam(team);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {


                if (spawnLocation != null) {
                    player.spigot().respawn();
                    player.teleport(spawnLocation);
                    plugin.getSpectatorManager().setPlayerAsPlaying(player);
                    plugin.getPlayerKitManager().giveKit(player, plugin.getPlayerKitManager().getSelectedKit(player));

                }
                else {
                    // Handle the case where the spawn location is null (not found)
                    // You may want to log a warning or take appropriate action
                    plugin.getLogger().warning("Spawn location not found for team: " + team);
                }
            }, 5 * 20); // 5 seconds
        }
    }

    private Location getSpawnLocationForTeam(Team team) {
        return team == Team.VIKINGS ? teamVikingSpawn : (team == Team.FRANKS ? teamFranksSpawn : null);
    }

    private Location getSpawnLocationForTeam(FileConfiguration config, Team team) {
        String path = "spawnLocations." + team.toString().toLowerCase();

        if (config.contains(path)) {
            return getLocationFromConfig(config.getConfigurationSection(path));
        } else {
            plugin.getLogger().warning("Spawn location not set for team: " + team);
            return null;
        }
    }

    private Location getLocationFromConfig(ConfigurationSection config) {
        double x = config.getDouble("x");
        double y = config.getDouble("y");
        double z = config.getDouble("z");
        float yaw = (float) config.getDouble("yaw");
        float pitch = (float) config.getDouble("pitch");

        String worldName = config.getString("world");
        if (worldName == null) {
            return null;
        }

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
