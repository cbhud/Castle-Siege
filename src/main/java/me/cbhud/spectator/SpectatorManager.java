package me.cbhud.spectator;

import me.cbhud.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.cbhud.spectator.PlayerStates.*;

public class SpectatorManager {

    private final Main plugin;
    private final PlayerStateManager playerStateManager;

    public SpectatorManager(Main plugin, PlayerStateManager playerStateManager) {
        this.plugin = plugin;
        this.playerStateManager = playerStateManager;
    }

    public void setPlayerState(Player player, PlayerStates state) {
        switch (state) {
            case PLAYING:
                setPlayerAsPlaying(player);
                break;
            case SPECTATOR:
                setPlayerAsSpectator(player);
                break;
            case PLAYERLOBBY:
                setPlayerAsLobby(player);
                break;
            default:
                break;
        }
    }

    public void setPlayerAsPlaying(Player player) {
        plugin.getPlayerStateManager().setPlayerState(player, PLAYING);
        player.setGameMode(GameMode.SURVIVAL);

        // Add any other necessary configurations for the PLAYING state
    }

    public void setPlayerAsLobby(Player player) {
        plugin.getPlayerStateManager().setPlayerState(player, PLAYERLOBBY);
        if(player.getGameMode() != GameMode.SURVIVAL){
            player.setGameMode(GameMode.SURVIVAL);
        }
        player.getActivePotionEffects().clear();
        player.getInventory().clear();
        player.setHealth(20);

        ItemStack clockItem = new ItemStack(Material.CLOCK);
        ItemMeta clockMeta = clockItem.getItemMeta();
        clockMeta.setDisplayName(ChatColor.YELLOW + "Select Team"); // Set your custom name here
        clockItem.setItemMeta(clockMeta);
        player.getInventory().setItem(3, clockItem);

        // Create a Nether Star item with a custom name
        ItemStack netherStarItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherStarMeta = netherStarItem.getItemMeta();
        netherStarMeta.setDisplayName(ChatColor.AQUA + "Select Kit"); // Set your custom name here
        netherStarItem.setItemMeta(netherStarMeta);
        player.getInventory().setItem(5, netherStarItem);

    }

    public void setPlayerAsSpectator(Player player) {
        plugin.getPlayerStateManager().setPlayerState(player, SPECTATOR);
        player.setGameMode(GameMode.SPECTATOR);
    }

    }


