package me.cbhud.Events;

import me.cbhud.items.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AxeEvent implements Listener {

    private final Plugin plugin;

    public AxeEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR || e.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            try {
                if (p.getInventory().getItemInMainHand().isSimilar(Manager.axe)) {
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
                } else {
                    // Log a warning if the axe material is not found or does not match
                    this.plugin.getLogger().warning("Axe material not found or does not match configured material: ");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
