package me.cbhud.Events;

import me.cbhud.gui.KitSelector;
import me.cbhud.gui.TeamSelector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEvent implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        handleInventory(event);
    }

    @EventHandler
    public void drag(InventoryDragEvent event) {
        handleInventory(event);
    }

    private void handleInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getClickedInventory().getHolder() instanceof TeamSelector) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case RED_STAINED_GLASS_PANE:
                    player.performCommand("teamjoin vikings");
                    break;
                case CYAN_STAINED_GLASS_PANE:
                    player.performCommand("teamjoin franks");
                    break;
                default:
                    break;
            }
        }
        if (event.getClickedInventory().getHolder() instanceof KitSelector) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case IRON_AXE:
                    player.performCommand("kit berserker");
                    break;
                case BOW:
                    player.performCommand("kit skald");
                    break;
                case IRON_SWORD:
                    player.performCommand("kit warrior");
                    break;
                case SHIELD:
                    player.performCommand("kit knight");
                    break;
                case TRIDENT:
                    player.performCommand("kit spearman");
                    break;
                case CROSSBOW:
                    player.performCommand("kit marksman");
                    break;
                default:
                    break;
            }
        }
    }

    private void handleInventory(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof TeamSelector ||
                event.getInventory().getHolder() instanceof KitSelector) {
            event.setCancelled(true);
        }
    }
}
