package me.cbhud.castlesiege.player;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.cbhud.castlesiege.CastleSiege;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.HashMap;
import java.util.Map;

import static me.cbhud.castlesiege.player.PlayerStates.*;

public class PlayerManager {

    private final CastleSiege plugin;
    private final Map<Player, PlayerStates> playerStates;

    public PlayerManager(CastleSiege plugin) {
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    public PlayerStates getPlayerState(Player player) {
        if (player != null) {
            return playerStates.getOrDefault(player, PLAYERLOBBY);
        }
        return PLAYERLOBBY;
    }

    public void setPlayerAsPlaying(Player player) {
        playerStates.put(player, PLAYING);
        player.setGameMode(GameMode.SURVIVAL);
    }


    public void setPlayerAsLobby(Player player) {
        playerStates.put(player, PLAYERLOBBY);
        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (plugin.getTeamManager().getTeam(player) == null) {

            if (!plugin.getTeamManager().tryRandomTeamJoin(player)) {
                player.sendTitle(ChatColor.GRAY + "Both teams are full, ", ChatColor.GRAY + "wait until the game finishes in spectator.", 10, 70, 20);
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                plugin.getLocationManager().teleport(player, plugin.getLocationManager().getMobLocation(), "Mob spawn location");
                return;
            }

            plugin.getTeamManager().tryRandomTeamJoin(player);
        }

        player.getInventory().clear();
        player.setHealth(20);
        player.setLevel(0);
        player.getActivePotionEffects().clear();

        player.getInventory().setItem(3, ItemBuilder.from(Material.CLOCK)
                .name(Component.text("§eSelect Team"))
                .lore(Component.text("§7Right-click to select team"))
                .build());

        player.getInventory().setItem(5, ItemBuilder.from(Material.NETHER_STAR)
                .name(Component.text("§eSelect Kit"))
                .lore(Component.text("§7Right-click to select the kit"))
                .build());
    }

    public void setPlayerAsSpectator(Player player) {
        playerStates.put(player, SPECTATOR);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SPECTATOR);

            }
        }.runTask(plugin);
    }


}
