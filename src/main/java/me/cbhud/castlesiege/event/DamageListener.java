package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.game.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class DamageListener implements Listener {
    private final CastleSiege plugin;
    private final Random rand = new Random();

    public DamageListener(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        // Only process damage events involving players
        if (!(event.getEntity() instanceof Player)) return;

        Player damagedPlayer = (Player) event.getEntity();

        // Cancel damage if the game is in the LOBBY or END states
        if (plugin.getGame().getState() == GameState.LOBBY || plugin.getGame().getState() == GameState.END) {
            event.setCancelled(true);
            return;
        }

        // Only process EntityDamageByEntityEvent events
        if (!(event instanceof EntityDamageByEntityEvent)) return;

        EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
        Player damager = getDamagerPlayer(damageByEntityEvent);

        if (damager == null) return;

        // Check team affiliations of the damaged player and damager
        Team damagedPlayerTeam = plugin.getTeamManager().getTeam(damagedPlayer);
        Team damagerTeam = plugin.getTeamManager().getTeam(damager);

        // Cancel event only if the damager and damaged player are on the same team
        if (damagedPlayerTeam != null && damagedPlayerTeam.equals(damagerTeam)) {
            event.setCancelled(true);
            return;
        }

        // If the damager is using a specific sword, apply a poison effect with a chance
        ItemMeta damagerItemMeta = damager.getInventory().getItemInMainHand().getItemMeta();
        if (damagerItemMeta != null && damagerItemMeta.equals(Manager.sword.getItemMeta())) {
            if (rand.nextInt(15) == 5) {
                damagedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
            }
        }
    }

    // Helper method to identify the damager as a player, if applicable
    private Player getDamagerPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        }
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        return null;
    }
}
