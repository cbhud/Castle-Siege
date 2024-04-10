package me.cbhud.event;

import me.cbhud.Main;
import me.cbhud.gui.KitSelector;
import me.cbhud.gui.TeamSelector;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class MiscEvents implements Listener {

    private final Main plugin;

    public MiscEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDamage(final PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event){
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.isOp() || player.hasPermission("viking.admin") || plugin.getGame().getState() == GameState.IN_GAME && plugin.getTeamManager().getTeam(player) == Team.Franks && event.getItem().getItemStack().getType() == Material.OAK_FENCE){
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    //EventHandler Class

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
                    plugin.getPlayerKitManager().setDefaultKit(player, Team.Vikings);
                    plugin.getTeamManager().joinTeam(player, Team.Vikings);
                    break;
                case CYAN_STAINED_GLASS_PANE:
                    plugin.getPlayerKitManager().setDefaultKit(player, Team.Franks);
                    plugin.getTeamManager().joinTeam(player, Team.Franks);
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
                case BONE:
                    player.performCommand("kit beastmaster");
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
                case SPLASH_POTION:
                    player.performCommand("kit wizard");
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
            if(player.isOp()){
                event.setCancelled(false);
            }else {
            event.setCancelled(true);
        }
        }
    }

    //


}






