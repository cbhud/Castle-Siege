package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.KitSelector;
import me.cbhud.castlesiege.gui.TeamSelector;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
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
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (player.isOp() || player.hasPermission("viking.admin")) return;

        if (plugin.getGame().getState() == GameState.IN_GAME &&
                plugin.getTeamManager().getTeam(player) == Team.Franks &&
                event.getItem().getItemStack().getType() == Material.OAK_FENCE) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

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
            }
        } else if (event.getClickedInventory().getHolder() instanceof KitSelector) {
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
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof TeamSelector ||
                event.getInventory().getHolder() instanceof KitSelector) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
    }
}
