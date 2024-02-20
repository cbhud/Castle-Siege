package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import static org.bukkit.Material.OAK_FENCE;

public class MiscEvents implements Listener {

    private final Main plugin;

    public MiscEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null
                && event.getItem().getType() == Material.CLOCK && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getTeamSelector().getInventory());
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null
                && event.getItem().getType() == Material.NETHER_STAR && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getKitSelector().getInventory());
        }
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
    public void onBlockBreak(BlockBreakEvent event){
        Block brokenBlock = event.getBlock();
        Player player = (Player) event.getPlayer();
        if(player.hasPermission("viking.admin")){
            event.setCancelled(false);
            return;
        }
        if (player.isOp()) {
            event.setCancelled(false);
            return;
        }
        if (plugin.getGame().getState() == GameState.IN_GAME){
            if(player.hasPermission("viking.admin")){
                event.setCancelled(false);
                return;
            }
            if (player.isOp()) {
                event.setCancelled(false);
                return;
            }
            if (brokenBlock.getType() == OAK_FENCE){
                event.setCancelled(false);
            }

        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block brokenBlock = event.getBlock();
        Player player = (Player) event.getPlayer();
        if(player.hasPermission("viking.admin")){
            event.setCancelled(false);
            return;
        }
        if (player.isOp()) {
            event.setCancelled(false);
            return;
        }
        if (plugin.getGame().getState() == GameState.IN_GAME){
            if(player.hasPermission("viking.admin")){
                event.setCancelled(false);
                return;
            }
            if (player.isOp()) {
                event.setCancelled(false);
                return;
            }
            if (brokenBlock.getType() == OAK_FENCE){
                event.setCancelled(false);
            }

        }
        event.setCancelled(true);
    }



}






