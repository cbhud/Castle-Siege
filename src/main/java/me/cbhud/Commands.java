package me.cbhud;

import me.cbhud.kits.KitType;
import me.cbhud.playerstate.PlayerStates;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {
    private final Main plugin;
    private final TeamManager teamManager;
    private final MobManager mobManager;

    private Location mobSpawnLocation;
    private Map<Team, Location> spawnLocations;

    public Commands(Main plugin, TeamManager teamManager, MobManager mobManager) {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /cs <endgame|setlobby|setmobspawn|start|teamspawn>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "endgame":
                return endGameCommand(sender);
            case "setlobby":
                return lobbyCommand(sender);
            case "setmobspawn":
                return setMobSpawnCommand(sender);
            case "start":
                return startCommand(sender);
            case "teamspawn":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /cs teamspawn <teamName>");
                    return true;
                }
                return teamSpawnCommand(sender, args[1]);
            default:
                sender.sendMessage(ChatColor.RED + "Unknown command. Available commands: endgame, setlobby, setmobspawn, start, teamspawn");
                return true;
        }
    }

    private boolean endGameCommand(CommandSender sender) {
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            if (sender.hasPermission("viking.admin")) {
                removeCustomZombie();
                plugin.getCountdownTimer().cancelTimer();
                teleportPlayersToLobby();
                plugin.getGame().setState(GameState.LOBBY);
                Bukkit.broadcastMessage(ChatColor.RED + "Game force-stopped!");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The game is not in GAME STATE. You cannot end it now.");
        }

        return true;
    }

    private void removeCustomZombie() {
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null &&
                        entity.getCustomName().equals("§6§lKing Charles")) {
                    entity.remove();
                }
            }
        }
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
                plugin.getPlayerManager().setPlayerAsLobby(player);
            }
        } else {
            Bukkit.getLogger().warning("World is null!");
        }
    }

    private boolean lobbyCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Save the player's current location as the lobby location
        plugin.getConfig().set("lobby.world", player.getWorld().getName());
        plugin.getConfig().set("lobby.x", player.getLocation().getX());
        plugin.getConfig().set("lobby.y", player.getLocation().getY());
        plugin.getConfig().set("lobby.z", player.getLocation().getZ());
        plugin.getConfig().set("lobby.yaw", player.getLocation().getYaw());
        plugin.getConfig().set("lobby.pitch", player.getLocation().getPitch());

        plugin.saveConfig();
        player.getWorld().setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        player.sendMessage("Lobby location set! RESTART THE SERVER BEFORE YOU START");

        return true;
    }

    private boolean setMobSpawnCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Save individual components of the spawn location to the configuration file
            plugin.getConfig().set("mobSpawnLocation.x", player.getLocation().getX());
            plugin.getConfig().set("mobSpawnLocation.y", player.getLocation().getY());
            plugin.getConfig().set("mobSpawnLocation.z", player.getLocation().getZ());
            plugin.getConfig().set("mobSpawnLocation.yaw", player.getLocation().getYaw());
            plugin.getConfig().set("mobSpawnLocation.pitch", player.getLocation().getPitch());
            plugin.getConfig().set("mobSpawnLocation.world", player.getLocation().getWorld().getName());
            plugin.saveConfig();

            player.sendMessage(ChatColor.RED + "King's (Zombie) spawn location has been updated. RESTART THE SERVER BEFORE YOU START!!!");
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }

        return true;
    }

    private boolean startCommand(CommandSender sender) {
        if (plugin.getGame().getState() == GameState.LOBBY) {
            if (sender.hasPermission("viking.admin")) {
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

    private boolean teamSpawnCommand(CommandSender sender, String teamName) {
        if (!(sender instanceof Player) || !sender.hasPermission("viking.admin")) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        try {
            Team team = Team.valueOf(teamName.toUpperCase());
            setSpawnLocation(team, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Spawn location for " + team + " set successfully! RESTART THE SERVER BEFORE YOU START");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid team name. Available teams: VIKINGS FRANKS");
        }

        return true;
    }

    private void setSpawnLocation(Team team, Location location) {
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

    private void teleportTeamsToSpawns() {
        Team[] teams = Team.values();

        for (Team team : teams) {
            Location teamSpawn = spawnLocations.get(team);
            if (teamSpawn != null) {
                // Teleport players in the team to their spawn location
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (teamManager.getTeam(player) == team) {
                        // Use teamSpawn directly instead of retrieving it from the configuration each time
                        plugin.getPlayerManager().setPlayerAsPlaying(player);
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
}
