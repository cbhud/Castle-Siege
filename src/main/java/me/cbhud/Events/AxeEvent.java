package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.items.Manager;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import java.util.*;

public class AxeEvent implements Listener
{
    private final Main plugin;
    private ArrayList<ArmorStand> axes;

    public AxeEvent(Main plugin) {
        this.plugin = plugin;
        this.axes = new ArrayList<ArmorStand>();
    }

    @EventHandler
    public void noManipulate(final PlayerArmorStandManipulateEvent event) {
        if (this.axes.contains(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void axeThrow(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem() != null && event.getPlayer().getItemInHand().isSimilar(Manager.axe)) {
            final Player p = event.getPlayer();
            final Vector v = p.getLocation().add(p.getLocation().getDirection().multiply(10)).toVector().subtract(p.getLocation().toVector()).normalize();
            p.getInventory().removeItem(new ItemStack[] { p.getInventory().getItem(p.getInventory().getHeldItemSlot()) });
            this.axe((Entity)p, v, event.getItem());
        }
    }

    @EventHandler
    public void axeThrow2(final PlayerInteractEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getItemInHand().isSimilar(Manager.axe)) {
            final Player p = event.getPlayer();
            final Vector v = p.getLocation().add(p.getLocation().getDirection().multiply(10)).toVector().subtract(p.getLocation().toVector()).normalize();
            this.axe((Entity)p, v, event.getPlayer().getInventory().getItemInMainHand());
            p.getInventory().removeItem(new ItemStack[] { p.getInventory().getItem(p.getInventory().getHeldItemSlot()) });
        }
    }

    public void axe(final Entity e, final Vector v, final ItemStack m) {
        final Location to = new Location(e.getWorld(), e.getLocation().getX(), e.getLocation().getY() + 1.0, e.getLocation().getZ());
        final ArmorStand a = (ArmorStand)e.getWorld().spawnEntity(to, EntityType.ARMOR_STAND);
        a.setVisible(false);
        a.setSmall(true);
        a.setItemInHand(m);
        this.axes.add(a);
        this.axe2(e, a, v.multiply(1.2), 0, m);
    }

    public void axe2(final Entity ef, final ArmorStand a, final Vector v, final int recurse, final ItemStack m) {
        if (recurse < 300) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(AxeEvent.this.plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    // Calculate the axe's new location based on its velocity
                    Location newLocation = a.getLocation().add(a.getVelocity());

                    // Check for entities in the axe's path
                    for (final Entity e : a.getWorld().getNearbyEntities(newLocation, 1.0, 1.0, 1.0)) {
                        if (e != ef && e != a && e instanceof Damageable) {
                            ((Damageable) e).damage(2.0);
                            a.getLocation().getWorld().dropItem(a.getLocation(), m);
                            AxeEvent.this.axes.remove(a);
                            a.remove();
                            return; // Stop processing once we hit an entity
                        }
                    }

                    // Continue the axe's movement
                    final double x = a.getRightArmPose().getX();
                    a.setRightArmPose(new EulerAngle(x + 0.6, 0.0, 0.0));
                    final Vector vec = new Vector(v.getX(), v.getY() - 0.03, v.getZ());
                    a.setVelocity(vec);

                    // Check if the axe has hit the ground
                    if (!a.isDead() && a.isOnGround()) {
                        a.getLocation().getWorld().dropItem(a.getLocation(), new ItemStack(m));
                        AxeEvent.this.axes.remove(a);
                        a.remove();
                    } else if (!a.isDead()) {
                        AxeEvent.this.axe2(ef, a, vec, recurse + 1, m);
                    }
                }
            }, 2L);
        } else {
            // Axe has reached maximum recursion, remove it
            a.getLocation().getWorld().dropItem(a.getLocation(), new ItemStack(m));
            this.axes.remove(a);
            a.remove();
        }
    }
    }

