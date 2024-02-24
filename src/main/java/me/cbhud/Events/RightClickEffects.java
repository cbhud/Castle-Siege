package me.cbhud.Events;

import me.cbhud.items.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RightClickEffects implements Listener {

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        final ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            ItemStack clickedItem = event.getItem();

            if (clickedItem.getItemMeta().equals(Manager.stew.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null); // Clear the item in the off hand
                } else {
                    player.getInventory().remove(Manager.stew);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.rage.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null); // Clear the item in the off hand
                } else {
                    player.getInventory().remove(Manager.rage);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.ragnarok.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null); // Clear the item in the off hand
                } else {
                    player.getInventory().remove(Manager.ragnarok);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3));

                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null); // Clear the item in the off hand
                } else if (mainHandItem != null && mainHandItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                    player.getInventory().removeItem(Manager.sight);
                }
            }
        }
    }
}