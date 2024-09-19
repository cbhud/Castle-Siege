package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.KitSelector;
import me.cbhud.castlesiege.gui.TeamSelector;
import me.cbhud.castlesiege.kits.KitType;
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
        if (player.isOp() || player.hasPermission("cs.admin")) return;

        if (plugin.getGame().getState() == GameState.IN_GAME &&
                plugin.getTeamManager().getTeam(player) == Team.Defenders &&
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
                    plugin.getTeamManager().joinTeam(player, Team.Attackers);
                    break;
                case CYAN_STAINED_GLASS_PANE:
                    plugin.getTeamManager().joinTeam(player, Team.Defenders);
                    break;
            }
        }
        // Handle kit selection and purchase
        else if (event.getClickedInventory().getHolder() instanceof KitSelector) {
            event.setCancelled(true);

            KitType selectedKit = null;
            switch (event.getCurrentItem().getType()) {
                case IRON_AXE:
                    selectedKit = KitType.BERSERKER;
                    break;
                case BOW:
                    selectedKit = KitType.SKALD;
                    break;
                case TNT:
                    selectedKit = KitType.BOMBARDIER;
                    break;
                case IRON_SWORD:
                    selectedKit = KitType.WARRIOR;
                    break;
                case SHIELD:
                    selectedKit = KitType.KNIGHT;
                    break;
                case TRIDENT:
                    selectedKit = KitType.SPEARMAN;
                    break;
                case SPLASH_POTION:
                    selectedKit = KitType.WIZARD;
                    break;
                case CROSSBOW:
                    selectedKit = KitType.MARKSMAN;
                    break;
            }

            if (selectedKit != null) {
                // Right-click to attempt to buy the kit
                if (event.isRightClick()) {
                    int kitPrice = plugin.getPlayerKitManager().getKitPrice(selectedKit); // Get the kit price

                    if (plugin.getDbConnection().checkPlayerKit(player.getUniqueId(), selectedKit.name())){
                        player.sendMessage("§aYou alreday own this kit!");
                        return;
                    }

                    if (plugin.getDbConnection().removePlayerCoins(player.getUniqueId(), kitPrice)) {
                        // Successfully purchased, update player kits table
                        plugin.getDbConnection().unlockPlayerKit(player.getUniqueId(), selectedKit);
                        player.sendMessage("§aYou have successfully purchased the " + selectedKit + " kit!");

                    } else if(!plugin.getDbConnection().removePlayerCoins(player.getUniqueId(), kitPrice)) {
                        player.sendMessage("§cYou do not have enough coins to purchase this kit. You need " + kitPrice + " coins to purchase this kit!");
                    }
                }
                // Left-click to select the kit
                else if (event.isLeftClick()) {
                    boolean hasKit = plugin.getDbConnection().checkPlayerKit(player.getUniqueId(), selectedKit.name());

                    if (hasKit) {
                        if (plugin.getTeamManager().getTeam(player) != selectedKit.getTeam()){
                            player.sendMessage("§cYou can't select opposing team kit");
                            return;
                        }
                        plugin.getPlayerKitManager().selectKit(player, selectedKit);
                        player.sendMessage("§aYou have selected the " + selectedKit + " kit!");
                    } else {
                        player.sendMessage("§cYou do not have this kit unlocked. Right-click to purchase it.");
                    }
                }
            }
        }
    }

    private void updateKit(Player player, KitType kitType) {
        if (!plugin.getPlayerKitManager().getSelectedKit(player).equals(kitType)) {
            if (kitType.getTeam().equals(plugin.getTeamManager().getTeam(player))) {
                if (!plugin.getPlayerKitManager().selectKit(player, kitType)) {
                    player.sendMessage("§cYou need to purchase " + kitType + " kit to use it!");
                    return;
                }
                plugin.getPlayerKitManager().selectKit(player, kitType);
                player.sendMessage("§aYou have selected the " + kitType + " kit.");
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
