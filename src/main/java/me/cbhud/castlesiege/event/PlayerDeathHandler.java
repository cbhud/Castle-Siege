package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.kits.KitType;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.state.Type;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDeathHandler implements Listener {

    private final Main plugin;
    private Location teamVikingSpawn;
    private Location teamFranksSpawn;

    public PlayerDeathHandler(Main plugin) {
        this.plugin = plugin;
        loadSpawnLocations();
    }

    private void loadSpawnLocations() {
        FileConfiguration config = plugin.getConfig();
        teamVikingSpawn = getSpawnLocationForTeam(config, Team.Vikings);
        teamFranksSpawn = getSpawnLocationForTeam(config, Team.Franks);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (plugin.getGame().getState() == GameState.IN_GAME) {


            if (plugin.getType().getState() == Type.Hardcore) {
                event.getDrops().clear();
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                plugin.getScoreboardManager().decrementTeamPlayersCount(player);
                player.sendTitle(ChatColor.RED + "You have died!", ChatColor.GRAY + "Better luck next time!", 10, 70, 20);
                if (plugin.getScoreboardManager().getVikings() < 1) {
                    plugin.getWinner().setWinner(Team.Franks);
                    plugin.getGameEndHandler().handleGameEnd();
                }
                if (killer != null) {
                    KitType killerKit = plugin.getPlayerKitManager().getSelectedKit(killer);
                    if (killerKit != null) {
                        applyKillEffects(killer, killerKit);
                    }
                }
                return;
            }


                event.getDrops().clear();
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                player.sendTitle(ChatColor.RED + "You died!", ChatColor.GRAY + "Respawning... 5 seconds", 10, 70, 20);

                Team team = plugin.getTeamManager().getTeam(player);
                Location spawnLocation = getSpawnLocationForTeam(team);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (spawnLocation != null) {
                        player.spigot().respawn();
                        player.teleport(spawnLocation);
                        plugin.getPlayerManager().setPlayerAsPlaying(player);
                        plugin.getPlayerKitManager().giveKit(player, plugin.getPlayerKitManager().getSelectedKit(player));
                    } else {
                        // Handle the case where the spawn location is null (not found)
                        // You may want to log a warning or take appropriate action
                        plugin.getLogger().warning("Spawn location not found for team: " + team);
                    }
                }, 5 * 20); // 5 seconds
            }
            if (killer != null) {
                KitType killerKit = plugin.getPlayerKitManager().getSelectedKit(killer);
                if (killerKit != null) {
                    applyKillEffects(killer, killerKit);
                }
            }
        }

    private Location getSpawnLocationForTeam(Team team) {
        return team == Team.Vikings ? teamVikingSpawn : (team == Team.Franks ? teamFranksSpawn : null);
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

    // Adding KillEffects functionality
    private void applyKillEffects(Player player, KitType kitType) {
        switch (kitType) {
            case MARKSMAN:
                player.getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW));
                break;
            case SPEARMAN:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                break;
            case KNIGHT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 1)));
                break;
            case BERSERKER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 150, 1));
                player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 1)));
                break;
            case SKALD:
                player.getInventory().addItem(Manager.harm);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                break;
            case WARRIOR:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                break;
        }
    }
}
