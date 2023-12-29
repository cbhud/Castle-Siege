package me.cbhud.Events;

import me.cbhud.items.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CustomPlayerDeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player attacker = (Player) event.getDamager();
                if (attacker.getInventory().getItemInMainHand().getItemMeta().equals(Manager.axe.getItemMeta())) {
                    victim.getWorld().dropItemNaturally(victim.getLocation(), new ItemStack(Material.DIAMOND, 1));
                }
            }
        }
    }
}
