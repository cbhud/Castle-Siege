package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TNTThrower implements Listener {

    private static final double TNT_VELOCITY = 1.6;
    private CastleSiege plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private long COOLDOWN_DURATION;
    public TNTThrower(CastleSiege plugin) {
        this.plugin = plugin;
        this.COOLDOWN_DURATION = plugin.getConfigManager().getConfig().getInt("tntCooldown", 120) * 1000;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR && mainHandItem.getType() == Material.TNT && plugin.getGame().getState() == GameState.IN_GAME) {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            if (!cooldowns.containsKey(playerId) || currentTime - cooldowns.get(playerId) >= COOLDOWN_DURATION) {

                cooldowns.put(playerId, currentTime);

                Vector direction = player.getLocation().getDirection();
                direction.multiply(TNT_VELOCITY);

                TNTPrimed primedTnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                primedTnt.setVelocity(direction);
                event.setCancelled(true); // Cancel the event to prevent TNT placement
            } else {
                long remainingCooldown = COOLDOWN_DURATION - (currentTime - cooldowns.get(playerId));
                int remainingCooldownSeconds = (int) (remainingCooldown / 1000);
                for (String line : plugin.getMessagesConfig().getCooldown()){
                    line = line.replace("{cooldown}","" + remainingCooldownSeconds);
                    player.sendMessage(line);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;

        boolean hasOakFence = false;
        for (Iterator<Block> iterator = event.blockList().iterator(); iterator.hasNext();) {
            Block block = iterator.next();
            if (block.getType() == Material.OAK_FENCE) {
                block.breakNaturally();
                hasOakFence = true;
                plugin.getMapRegeneration().add(block.getLocation());
            }else {
                iterator.remove();
            }
        }

        if (!hasOakFence) {
            event.setCancelled(true);
        }
    }

    public void clearCooldowns() {
        cooldowns.clear();
    }

}