package me.cbhud.Commands;

import me.cbhud.Main;
import me.cbhud.MobManager;
import me.cbhud.kits.KitType;
import me.cbhud.spectator.PlayerStates;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StartCommand implements org.bukkit.command.CommandExecutor {
    private final Main plugin;
    private final TeamManager teamManager;
    private final MobManager mobManager;

    private Location mobSpawnLocation;
    private Map<Team, Location> spawnLocations;

    public StartCommand(Main plugin, TeamManager teamManager, MobManager mobManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.mobManager = mobManager;

        loadConfigValues(); // Load configuration values during initialization
    }

    private void loadConfigValues() {
        FileConfiguration config = plugin.getConfig();

        // Load mob spawn location
        ConfigurationSection mobSpawnConfig = config.getConfigurationSection("mobSpawnLocation");
        if (mobSpawnConfig != null) {
            mobSpawnLocation = getLocationFromConfig(mobSpawnConfig);
        } else {
            plugin.getLogger().warning(ChatColor.RED + "Mob spawn location not set. Use /setmobspawn to set the location.");
        }

        // Load team spawn locations
        spawnLocations = new HashMap<>();
        Team[] teams = Team.values();
        for (Team team : teams) {
            String path = "spawnLocations." + team.toString().toLowerCase();
            if (config.contains(path)) {
                spawnLocations.put(team, getLocationFromConfig(config.getConfigurationSection(path)));
            } else {
                plugin.getLogger().warning(ChatColor.RED + "Spawn location not set for team: " + team);
            }
        }
    }

    private void teleportTeamsToSpawns() {
        Team[] teams = Team.values();

        for (Team team : teams) {
            Location teamSpawn = spawnLocations.get(team);
            if (teamSpawn != null) {
                // Teleport players in the team to their spawn location
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (teamManager.getTeam(player) == team) {
                        // Use teamSpawn directly instead of retrieving it from the configuration each time
                        plugin.getSpectatorManager().setPlayerAsPlaying(player);
                        player.teleport(teamSpawn);
                    }
                }
            }
        }
    }
    private void applyKitsToPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (plugin.getPlayerStateManager().getPlayerState(player) == PlayerStates.PLAYING) {
                Team playerTeam = plugin.getTeamManager().getTeam(player);
                KitType selectedKit = plugin.getPlayerKitManager().getSelectedKit(player);

                if (selectedKit != null && selectedKit.getTeam() == playerTeam) {
                    // Player has a selected kit matching their team, give the kit
                    plugin.getPlayerKitManager().giveKit(player, selectedKit);
                } else {
                    if (selectedKit == null) {
                        // Player hasn't selected a kit, give them the default kit for their team
                        KitType defaultKit = KitType.getDefaultKit(playerTeam);
                        if (defaultKit != null) {
                            plugin.getPlayerKitManager().selectKit(player, defaultKit);
                            plugin.getPlayerKitManager().giveKit(player, defaultKit);
                            player.sendMessage(ChatColor.RED + "Invalid or no kit selected. You have received the default kit for your team.");
                        }
                    }
                }
            } else {
                player.sendMessage("You haven't selected a kit!");
            }
        });
    }

    private Location getLocationFromConfig(ConfigurationSection config) {
        double x = config.getDouble("x");
        double y = config.getDouble("y");
        double z = config.getDouble("z");
        float yaw = (float) config.getDouble("yaw");
        float pitch = (float) config.getDouble("pitch");

        // Get the world from the configuration
        String worldName = config.getString("world");
        if (worldName == null) {
            // Handle case where world name is null
            // getLogger().warning("World name is null. Make sure to set a valid world name.");
            return null;
        }

        // Create and return the Location object
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (plugin.getGame().getState() == GameState.LOBBY) {
            if (sender.hasPermission("war.start")) {
                if (mobSpawnLocation != null) {
                    mobManager.spawnCustomMob(mobSpawnLocation, plugin.getConfig().getConfigurationSection("mobSpawnLocation"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + "Mob spawn location not set. Use /setmobspawn to set the location.");
                    return true;
                }

                plugin.getGame().setState(GameState.IN_GAME);
                teleportTeamsToSpawns();
                applyKitsToPlayers();

                int timerMinutes = plugin.getConfig().getInt("timerMinutes", 10);
                plugin.getCountdownTimer().startTimer(timerMinutes);

                Bukkit.broadcastMessage("§7§m----------------------------------");
                Bukkit.broadcastMessage("§bFranks §fmust defend the King.");
                Bukkit.broadcastMessage("§bFranks §fwin when countdown is over.");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§cVikings §fmust kill the King.");
                Bukkit.broadcastMessage("§cVikings §flose when countdown is over.");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§aThe game has started!");
                Bukkit.broadcastMessage("§7§m----------------------------------");

            } else {
                sender.sendMessage("You do not have permission to use this command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The game is not in LOBBY. You cannot start it now.");
        }

        return true;
    }
}
