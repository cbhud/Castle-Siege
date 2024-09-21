package me.cbhud.castlesiege.commands;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.kits.KitType;
import me.cbhud.castlesiege.playerstate.PlayerStates;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.state.Type;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private final CastleSiege plugin;

    public Commands(CastleSiege plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /cs <endgame|setlobby|setmobspawn|start|teamspawn>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "type":
                return typeCommand(sender);
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
            if (sender.hasPermission("cs.admin")) {
                plugin.getMobManager().removeCustomZombie();
                plugin.getTimer().cancelTimer();
                teleportPlayersToLobby();
                plugin.getGame().setState(GameState.LOBBY);
                this.plugin.getMapRegeneration().regenerateChangedBlocks();
                plugin.tntThrower().clearCooldowns();
                Bukkit.broadcastMessage(plugin.getMessagesConfig().getForceStopMsg().toString());
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The game is not in GAME STATE. You cannot end it now.");
        }

        return true;
    }


    private void teleportPlayersToLobby() {
        // Submitting the lobby teleportation task asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
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
                    // Teleport players to the lobby asynchronously
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
        player.sendMessage(ChatColor.GREEN + "Lobby location set!");
        plugin.getLocationManager().loadSpawnLocations();

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

            player.sendMessage(ChatColor.GREEN + "King's spawn location has been updated.");
            plugin.getLocationManager().loadSpawnLocations();
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }

        return true;
    }

    private boolean startCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission("cs.admin")) {
            if (plugin.getGame().getState() != GameState.LOBBY) {
            sender.sendMessage(ChatColor.RED + "The game is not in LOBBY STATE. You cannot start it now.");
            return true;
        }

            Location mobSpawnLocation = plugin.getLocationManager().getMobLocation();
                if (mobSpawnLocation != null) {
                    plugin.getMobManager().spawnCustomMob(plugin.getConfig().getConfigurationSection("mobSpawnLocation"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + "Mob spawn location not set. Use /setmobspawn to set the location.");
                    return true;
                }

            plugin.getScoreboardManager().loadTeamCount();


            plugin.getGame().setState(GameState.IN_GAME);
                teleportTeamsToSpawnsAndApplyKits();

                plugin.getTimer().startTimer(plugin.getConfig().getInt("timerMinutes", 10));

                for (String line : plugin.getMessagesConfig().getStartMsg()) {
                    line = line.replace("{attackers}", plugin.getConfigManager().getConfig().getString("attackersTeamName"));
                    line = line.replace("{defenders}", plugin.getConfigManager().getConfig().getString("defendersTeamName"));
                    Bukkit.broadcastMessage(line);
                }
                if (plugin.getType().getState() == Type.Hardcore){
                for (String line : plugin.getMessagesConfig().getHardCoreEnabledMsg()){
                    Bukkit.broadcastMessage(line);
                    }
                }
            }else {
            sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
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
            String teamName1 = teamName.substring(0,1).toUpperCase() + teamName.substring(1).toLowerCase();
            Team team = Team.valueOf(teamName1);
            plugin.getLocationManager().setTeamSpawnLocation(team, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Spawn location for " + team + " set successfully!");
            plugin.getLocationManager().loadSpawnLocations();
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid team name. Available teams: Attackers, Defenders");
        }

        return true;
    }



    private void teleportTeamsToSpawnsAndApplyKits() {
        // Submitting the teleportation and kit application task asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Team[] teams = Team.values();
            for (Team team : teams) {
                Location teamSpawn = plugin.getLocationManager().getSpawnLocationForTeam(team);
                if (teamSpawn != null) {
                    // Teleport players in the team to their spawn location
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (plugin.getTeamManager().getTeam(player) == team) {
                            // Use teamSpawn directly instead of retrieving it from the configuration each time
                            plugin.getPlayerManager().setPlayerAsPlaying(player);

                            Bukkit.getScheduler().runTask(plugin, () -> player.teleport(teamSpawn));

                            // Apply kits to players asynchronously
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                if (plugin.getPlayerManager().getPlayerState(player) == PlayerStates.PLAYING) {
                                    KitType selectedKit = plugin.getPlayerKitManager().getSelectedKit(player);
                                    plugin.getPlayerKitManager().giveKit(player, selectedKit);
                                }
                            });
                        }

                    }
                }
            }
        });
    }




    private boolean typeCommand(CommandSender sender) {

        if (plugin.getGame().getState() == GameState.LOBBY) {
            if (sender.hasPermission("cs.admin")) {

                if (plugin.getType().getState() == Type.Normal) {
                    plugin.getType().setState(Type.Hardcore);
                    Bukkit.broadcastMessage(plugin.getMessagesConfig().getHardcoreMsg().toString() + " §aenabled!");
                }else{
                    plugin.getType().setState(Type.Normal);
                    Bukkit.broadcastMessage(plugin.getMessagesConfig().getHardcoreMsg().toString() + " §cdisabled!");
                }
                plugin.getScoreboardManager().updateScoreboardForAll();
            }
        }
        return true;
    }


}