package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.game.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MapRegeneration implements Listener {

    private final List<Location> originalLocation;
    private final List<Location> changedBlocks;
    private final List<Location> placedBlocks;
    private final CastleSiege plugin;
    private boolean isSaved;

    public MapRegeneration(CastleSiege plugin) {
        originalLocation = new ArrayList<>();
        changedBlocks = new ArrayList<>();
        placedBlocks = new ArrayList<>();
        this.plugin = plugin;
        this.isSaved = false;
    }

    public void setSaved(){
        this.isSaved = false;
    }
    public void saveOriginalFenceLocations() {
        if (isSaved){
            return;
        }
        // Only scan the overworld ("world") for oak fences
        Bukkit.getWorlds().stream()
                .filter(world -> world.getName().equalsIgnoreCase("world")) // Only the overworld
                .forEach(world -> {
                    // Iterate over the loaded chunks in the world
                    for (Chunk chunk : world.getLoadedChunks()) {
                        // Iterate through the blocks within the chunk
                        for (int x = 0; x < 16; x++) {  // Chunk width (16 blocks)
                            for (int y = 0; y < world.getMaxHeight(); y++) {  // Chunk height (up to world height)
                                for (int z = 0; z < 16; z++) {  // Chunk depth (16 blocks)
                                    Block block = chunk.getBlock(x, y, z);
                                    if (block.getType() == Material.OAK_FENCE) {
                                        originalLocation.add(block.getLocation());  // Track oak fences
                                    }
                                }
                            }
                        }
                    }
                    this.isSaved = true;
                });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGame().getState() == GameState.IN_GAME && event.getBlock().getType() == Material.OAK_FENCE) {
            event.setCancelled(false);
            Location location = event.getBlock().getLocation();
            changedBlocks.add(location); // Mark the oak fence as broken
            if (plugin.getTeamManager().getTeam(player) == Team.Defenders) {
                player.getInventory().addItem(new ItemStack(Material.OAK_FENCE, 1));
            }

            event.getBlock().setType(Material.AIR); // Remove the block
        } else {
            event.setCancelled(!player.hasPermission("cs.admin") || !player.isOp());
        }
    }

    public void add(Location location) {
        changedBlocks.add(location); // Add broken oak fence to the list
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGame().getState() == GameState.IN_GAME && event.getBlock().getType() == Material.OAK_FENCE && plugin.getTeamManager().getTeam(player) == Team.Defenders) {
            event.setCancelled(false);
            placedBlocks.add(event.getBlock().getLocation()); // Mark the oak fence as placed
        } else {
            event.setCancelled(!player.isOp() || !player.hasPermission("cs.admin"));
        }
    }

    public void regenerateChangedBlocks() {
        for (Location location : placedBlocks) {
            Block block = location.getBlock();
                block.setType(Material.AIR);
        }

        for (Location location : originalLocation) {
            Block block = location.getBlock();
            if (block.getType() == Material.AIR) {
                block.setType(Material.OAK_FENCE); // Regenerate the original oak fence block
            }
        }

        // Clear the tracking list after regeneration
        changedBlocks.clear();
        placedBlocks.clear();
    }
}
