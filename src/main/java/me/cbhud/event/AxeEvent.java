package me.cbhud.event;

import me.cbhud.Main;
import me.cbhud.gui.Manager;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AxeEvent implements Listener {

    private final Plugin plugin;
    private final Main main;

    public AxeEvent(Main main, Plugin plugin) {
        this.plugin = plugin;
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR && main.getTeamManager().getTeam(p) == Team.Vikings && main.getGame().getState() == GameState.IN_GAME && p.getInventory().getItemInMainHand().isSimilar(Manager.axe) || e.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK && main.getTeamManager().getTeam(p) == Team.Vikings && main.getGame().getState() == GameState.IN_GAME && p.getInventory().getItemInMainHand().isSimilar(Manager.axe)) {
            try {
                    Item axe = p.getWorld().dropItem(p.getEyeLocation(), p.getInventory().getItemInMainHand());
                    axe.setVelocity(p.getEyeLocation().getDirection().multiply(1.75));
                    p.getInventory().getItemInMainHand().setAmount(0);

                    new BukkitRunnable() {
                        public void run() {
                            for (Entity ent : axe.getNearbyEntities(0.2, 0.2, 0.2)) {
                                if (ent instanceof LivingEntity) {
                                    LivingEntity target = (LivingEntity) ent;
                                    if (ent == p) {
                                        continue;
                                    }
                                    target.damage(2.0);
                                    axe.setVelocity(new Vector(0, 0, 0));
                                    this.cancel();
                                    axe.remove();
                                }
                            }
                            if (axe.isOnGround()) {
                                axe.setVelocity(new Vector(0, 0, 0));
                                this.cancel();
                                axe.remove();
                            }
                        }
                    }.runTaskTimer(this.plugin, 0L, 1L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
