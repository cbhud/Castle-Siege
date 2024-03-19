package me.cbhud;

import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class WolfManager implements Listener {

    private final Main plugin;
    private final TeamManager teamManager;
    private Wolf wolf;
    private final ConfigManager configManager;

    public WolfManager(Main plugin, TeamManager teamManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void spawnCustomMob(Player player) {
        Location spawnLocation = player.getLocation();

        wolf = (Wolf) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WOLF);
        wolf.setAdult();
        wolf.setCustomNameVisible(true);
        wolf.setCustomName(ChatColor.YELLOW + player.getName() + "'s Wolf");
        wolf.setOwner(player);
        wolf.setCanPickupItems(false); // Zombie cannot pick up items
        wolf.setAngry(true);
        }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                Team damagerTeam = teamManager.getTeam(damager);

                if (damagerTeam == Team.Vikings || damagerTeam == null) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();

                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    Team shooterTeam = teamManager.getTeam(shooter);

                    if (shooterTeam == Team.Vikings|| shooterTeam == null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();

            // Check if the wolf already has a target or if it's targeting a player
            if (wolf.getTarget() == null || wolf.getTarget() instanceof Player) {
                // Iterate over nearby players
                for (Player nearbyPlayer : wolf.getWorld().getPlayers()) {
                    if (nearbyPlayer.getLocation().distance(wolf.getLocation()) <= 15) {
                        // Check if the nearby player belongs to the "Franks" team
                        if (teamManager.getTeam(nearbyPlayer) == Team.Franks) {
                            wolf.setTarget(nearbyPlayer);
                            break; // Stop searching once a player from the "Franks" team is found
                        }
                    }
                }
            }
        }
    }


}