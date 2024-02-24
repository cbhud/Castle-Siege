package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
public class DamageListener implements Listener {
    private final Main plugin;
    private final TeamManager teamManager;

    public DamageListener(Main plugin) {

        this.plugin = plugin;
        this.teamManager = plugin.getTeamManager();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();

            // Check if the game state is LOBBY or END
            if (plugin.getGame().getState() == GameState.LOBBY || plugin.getGame().getState() == GameState.END) {
                // Cancel the damage event in the lobby or end state
                event.setCancelled(true);
                return;
            }

            // Check if the damage is caused by another entity
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;

                // Check if the damager is a player or projectile (e.g., arrows)
                if (damageByEntityEvent.getDamager() instanceof Player || damageByEntityEvent.getDamager() instanceof Projectile) {
                    Player damager = null;

                    if (damageByEntityEvent.getDamager() instanceof Player) {
                        damager = (Player) damageByEntityEvent.getDamager();
                    } else if (damageByEntityEvent.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) damageByEntityEvent.getDamager();
                        if (projectile.getShooter() instanceof Player) {
                            damager = (Player) projectile.getShooter();
                        }
                    }

                    if (damager != null) {
                        // Check if the damaged player and the damager are in the same team
                        Team damagedPlayerTeam = teamManager.getTeam(damagedPlayer);
                        Team damagerTeam = teamManager.getTeam(damager);

                        if (damagedPlayerTeam != null && damagedPlayerTeam.equals(damagerTeam)) {
                            // Players in the same team, cancel the damage event
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
