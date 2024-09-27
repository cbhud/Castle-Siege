package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
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
    private final CastleSiege plugin;

    public MapRegeneration(CastleSiege plugin) {
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
            originalBlockStates.put(location, Material.OAK_FENCE);
            changedBlocks.put(location, Material.AIR);
            if(plugin.getTeamManager().getTeam(player) == Team.Defenders){
                player.getInventory().addItem(new ItemStack(Material.OAK_FENCE, 1));
            }

                event.getBlock().setType(Material.AIR);

        } else event.setCancelled(!player.hasPermission("cs.admin") || !player.isOp());

    }


    public void add(Location location){
        originalBlockStates.put(location, Material.OAK_FENCE);
        changedBlocks.put(location, Material.AIR);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

        Player player = (Player) event.getPlayer();

        if (plugin.getGame().getState() == GameState.IN_GAME && event.getBlock().getType() == Material.OAK_FENCE && plugin.getTeamManager().getTeam(player) ==  Team.Defenders){
                event.setCancelled(false);
                Location location = event.getBlock().getLocation();
                originalBlockStates.put(location, Material.AIR);
                changedBlocks.put(location, Material.OAK_FENCE);
        }else event.setCancelled(!player.isOp() || !player.hasPermission("cs.admin"));
    }

    public void regenerateChangedBlocks() {
        for (Map.Entry<Location, Material> entry : changedBlocks.entrySet()) {
            Location location = entry.getKey();
            Material originalMaterial = originalBlockStates.get(location);
            if (originalMaterial != null) {
                location.getBlock().setType(originalMaterial);
            }
        }
        changedBlocks.clear();
        originalBlockStates.clear();
    }
}
