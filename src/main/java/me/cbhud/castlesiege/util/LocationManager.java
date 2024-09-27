package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LocationManager {

    private final CastleSiege plugin;
    private Location teamVikingSpawn;
    private Location teamFranksSpawn;
    private Location lobbyLocation;
    private Location mobLocation;

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public Location getMobLocation() {
        return mobLocation;
    }


    public LocationManager(CastleSiege plugin){
        this.plugin = plugin;
        loadSpawnLocations();
    }

    public void loadSpawnLocations() {
        FileConfiguration config = plugin.getConfig();
        teamVikingSpawn = getSpawnLocationForTeam(config, Team.Attackers);
        teamFranksSpawn = getSpawnLocationForTeam(config, Team.Defenders);
        if (config.contains("lobby.world")) {
            lobbyLocation = getLobbyLocation(config, "lobby");
        }
        if (config.contains("mobSpawnLocation")) {
            mobLocation = getLobbyLocation(config, "mobSpawnLocation");
        }
    }

    private Location getLobbyLocation(FileConfiguration config, String key) {
        String worldName = config.getString(key + ".world");
        double x = config.getDouble(key + ".x");
        double y = config.getDouble(key + ".y");
        double z = config.getDouble(key + ".z");
        float yaw = (float) config.getDouble(key + ".yaw");
        float pitch = (float) config.getDouble(key + ".pitch");
        return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }



    public Location getSpawnLocationForTeam(Team team) {
        return team == Team.Attackers ? teamVikingSpawn : (team == Team.Defenders ? teamFranksSpawn : null);
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

    public Location getLocationFromConfig(ConfigurationSection config) {
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

    public void setTeamSpawnLocation(Team team, Location location) {
        FileConfiguration config = plugin.getConfig();
        String path = "spawnLocations." + team.toString().toLowerCase();

        // Save spawn location to config
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());

        plugin.saveConfig();
    }

    public void teleportPlayersToLobby() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (plugin.getLocationManager().getLobbyLocation() != null) {

                Location lobbyLocation = plugin.getLocationManager().getLobbyLocation();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.teleport(lobbyLocation);
                        plugin.getPlayerManager().setPlayerAsLobby(player);
                    });
                }
            } else {
                Bukkit.getLogger().warning("World is null!");
            }
        });
    }



}
