package me.cbhud.castlesiege;

import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MapRegeneration implements Listener {

    private final Map<Location, Material> originalBlockStates;
    private final Map<Location, Material> changedBlocks;
    private final Main plugin;

    public MapRegeneration(Main plugin) {
        originalBlockStates = new HashMap<>();
        changedBlocks = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGame().getState() == GameState.IN_GAME && event.getBlock().getType() == Material.OAK_FENCE) {
            event.setCancelled(false);
            Location location = event.getBlock().getLocation();
            originalBlockStates.put(location, event.getBlock().getType());
            changedBlocks.put(location, Material.AIR);
            if(plugin.getTeamManager().getTeam(player) == Team.Franks){
                event.getBlock().setType(Material.AIR);

                // Give an OAK_FENCE to the player
                player.getInventory().addItem(new ItemStack(Material.OAK_FENCE, 1));
            }else {
                event.getBlock().setType(Material.AIR);
            }
        } else event.setCancelled(!player.hasPermission("viking.admin") || !player.isOp()); // Allow breaking any blocks

    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

        Player player = (Player) event.getPlayer();

        if (plugin.getGame().getState() == GameState.IN_GAME && event.getBlock().getType() == Material.OAK_FENCE && plugin.getTeamManager().getTeam(player) ==  Team.Franks){
                event.setCancelled(false);
                Location location = event.getBlock().getLocation();
                originalBlockStates.put(location, Material.AIR);
                changedBlocks.put(location, event.getBlock().getType());
        }else event.setCancelled(!player.isOp() || !player.hasPermission("viking.admin")); // Allow breaking any blocks
    }


    public void regenerateChangedBlocks() {
        for (Map.Entry<Location, Material> entry : changedBlocks.entrySet()) {
            Location location = entry.getKey();
            Material originalMaterial = originalBlockStates.get(location);
            if (originalMaterial != null) {
                location.getBlock().setType(originalMaterial);
            }
        }
        // Clear changed blocks after regeneration
        changedBlocks.clear();
        originalBlockStates.clear();
    }
}
