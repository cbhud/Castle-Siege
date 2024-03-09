package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
    public void onBlockBreak(BlockBreakEvent event){
        Block brokenBlock = event.getBlock();
        Player player = (Player) event.getPlayer();
        if (player.hasPermission("viking.admin") || player.isOp()) {
            event.setCancelled(false); // Allow breaking any blocks
        } else {
            // Check if the broken block is a fence and the game is in progress
            if (brokenBlock.getType() == Material.OAK_FENCE && plugin.getGame().getState() == GameState.IN_GAME) {
                event.setCancelled(false); // Allow breaking the fence during the game
            } else {
                event.setCancelled(true); // Cancel breaking the block
            }
        }    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block brokenBlock = event.getBlock();
        Player player = (Player) event.getPlayer();
        if (player.hasPermission("viking.admin") || player.isOp()) {
            event.setCancelled(false); // Allow breaking any blocks
        } else {
            if (brokenBlock.getType() == Material.OAK_FENCE && plugin.getGame().getState() == GameState.IN_GAME && plugin.getTeamManager().getTeam(player) == Team.FRANKS) {
                event.setCancelled(false); // Allow breaking the fence during the game
            } else {
                event.setCancelled(true); // Cancel breaking the block
            }
        }
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event){
        Player player = (Player) event.getEntity();
        if (player.isOp()){
            event.setCancelled(false);
        }
        if (plugin.getTeamManager().getTeam(player) == Team.FRANKS && plugin.getGame().getState() == GameState.IN_GAME){
            event.setCancelled(false);
        }else {
            event.setCancelled(true);
        }
    }

}






