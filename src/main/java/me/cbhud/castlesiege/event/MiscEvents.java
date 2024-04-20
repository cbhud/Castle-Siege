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
                    KitType berserker = KitType.BERSERKER;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == berserker) {
                        return;
                    }
                    if (berserker.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, berserker);
                        player.sendMessage("§aYou have selected Berserker kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                            return;
                    }
                    break;
                case BOW:
                    KitType skald = KitType.SKALD;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == skald) {
                        return;
                    }
                    if (skald.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, skald);
                        player.sendMessage("§aYou have selected Skald kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case BONE:
                    KitType beastmaster = KitType.BEASTMASTER;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == beastmaster) {
                        return;
                    }
                    if (beastmaster.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, beastmaster);
                        player.sendMessage("§aYou have selected Beastmaster kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case IRON_SWORD:
                    KitType warrior = KitType.WARRIOR;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == warrior) {
                        return;
                    }
                    if (warrior.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, warrior);
                        player.sendMessage("§aYou have selected Warrior kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case SHIELD:
                    KitType knight = KitType.KNIGHT;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == knight) {
                        return;
                    }
                    if (knight.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, knight);
                        player.sendMessage("§aYou have selected Knight kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case TRIDENT:
                    KitType spearmen = KitType.SPEARMAN;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == spearmen) {
                        return;
                    }
                    if (spearmen.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, spearmen);
                        player.sendMessage("§aYou have selected Spearman kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case SPLASH_POTION:
                    KitType wizard = KitType.WIZARD;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == wizard) {
                        return;
                    }
                    if (wizard.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, wizard);
                        player.sendMessage("§aYou have selected Wizard kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
                    break;
                case CROSSBOW:
                    KitType marksman = KitType.MARKSMAN;
                    if (plugin.getPlayerKitManager().getSelectedKit(player) == marksman) {
                        return;
                    }
                    if (marksman.getTeam() == plugin.getTeamManager().getTeam(player)) {
                        plugin.getPlayerKitManager().selectKit(player, marksman);
                        player.sendMessage("§aYou have selected Marksman kit.");
                        plugin.getScoreboardManager().updateScoreboard(player);
                    } else {
                        return;
                    }
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
