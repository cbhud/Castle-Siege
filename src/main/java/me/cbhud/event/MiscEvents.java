package me.cbhud.event;

import me.cbhud.Main;
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
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();

        if (player.hasPermission("viking.admin") || player.isOp()) {
            event.setCancelled(false); // Allow breaking any blocks
        } else {
                event.setCancelled(true);
            }
        }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block brokenBlock = event.getBlock();
        Player player = (Player) event.getPlayer();
        if (player.hasPermission("viking.admin") || player.isOp()) {
            event.setCancelled(false); // Allow breaking any blocks
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event){
        Player player = (Player) event.getEntity();
        if (player.isOp()){
            event.setCancelled(false);
        }
        else {
            event.setCancelled(true);
        }
    }

}






