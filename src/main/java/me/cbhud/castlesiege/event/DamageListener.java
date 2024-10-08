package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.entity.Player;
import java.util.Random;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener {
    private final CastleSiege plugin;

    Random rand = new Random();
    public DamageListener(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

            Player damagedPlayer = (Player) event.getEntity();

            if (plugin.getGame().getState() == GameState.LOBBY || plugin.getGame().getState() == GameState.END) {
                event.setCancelled(true);
                return;
            }

            if (!(event instanceof EntityDamageByEntityEvent)) {return;}

                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;

                if (!(damageByEntityEvent.getDamager() instanceof Player || damageByEntityEvent.getDamager() instanceof Projectile)) {return;}
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
                        Team damagedPlayerTeam = plugin.getTeamManager().getTeam(damagedPlayer);
                        Team damagerTeam = plugin.getTeamManager().getTeam(damager);

                        if (damagedPlayerTeam != null && damagedPlayerTeam.equals(damagerTeam)) {
                            event.setCancelled(true);
                        }
                        ItemMeta damagerItemMeta = damager.getInventory().getItemInMainHand().getItemMeta();
                        if (damagerItemMeta != null && damagerItemMeta.equals(Manager.sword.getItemMeta())) {
                            if (rand.nextInt(15) + 1 == 6){
                                damagedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
                            }
                        }
                    }
                }
            }