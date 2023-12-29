package me.cbhud.Events;

import me.cbhud.items.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StewEvent implements Listener {

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        ItemStack item = new ItemStack(Material.BOWL, 1);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(Manager.stew.getItemMeta())) {
                    Player player = event.getPlayer();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 150, 1));

                    ItemStack bowl = new ItemStack(Material.BOWL, 1);

                    player.setItemInHand(bowl);

                    player.sendMessage("Pojeo si supu");
                }
            }
        }
    }
}