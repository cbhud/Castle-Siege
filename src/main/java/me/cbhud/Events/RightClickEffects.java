package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.items.Manager;
import me.cbhud.state.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RightClickEffects implements Listener {


    private final Main plugin;

    public RightClickEffects(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            ItemStack clickedItem = event.getItem();

            if (clickedItem.getType() == Material.CLOCK && plugin.getGame().getState() == GameState.LOBBY) {
                player.openInventory(plugin.getTeamSelector().getInventory());
            }

            if (clickedItem.getType() == Material.NETHER_STAR && plugin.getGame().getState() == GameState.LOBBY) {
                player.openInventory(plugin.getKitSelector().getInventory());
            }

            if (clickedItem.getItemMeta().equals(Manager.stew.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.rage.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().remove(Manager.rage);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.ragnarok.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().remove(Manager.ragnarok);
                }
            } else if (clickedItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 175, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else if (mainHandItem != null && mainHandItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                    player.getInventory().removeItem(Manager.sight);
                }
            }
        }
    }
}
