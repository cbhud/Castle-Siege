package me.cbhud.Events;
import me.cbhud.gui.KitSelector;
import me.cbhud.gui.TeamSelector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
public class InventoryEvent implements Listener {
    @EventHandler
    public void click(InventoryClickEvent event){
        if(event.getClickedInventory() == null){
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof TeamSelector){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() == null){return;}
            if(event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE){
                player.performCommand("teamjoin vikings");
            }
            if(event.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE){
                player.performCommand("teamjoin franks");
            }
        }
        if (event.getClickedInventory().getHolder() instanceof KitSelector){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() == null){return;}
            if(event.getCurrentItem().getType() == Material.IRON_AXE){
                player.performCommand("kit berserker");
            }
            if(event.getCurrentItem().getType() == Material.BOW){
                player.performCommand("kit skald");
            }
            if(event.getCurrentItem().getType() == Material.IRON_SWORD){
                player.performCommand("kit warrior");
            }
            if(event.getCurrentItem().getType() == Material.BLACK_BANNER){
                player.performCommand("kit knight");
            }
            if(event.getCurrentItem().getType() == Material.TRIDENT){
                player.performCommand("kit spearman");
            }
            if(event.getCurrentItem().getType() == Material.CROSSBOW){
                player.performCommand("kit marksman");
            }
        }
    }
}
