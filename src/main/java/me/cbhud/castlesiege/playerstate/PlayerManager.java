package me.cbhud.castlesiege.playerstate;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.team.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.cbhud.castlesiege.playerstate.PlayerStates.*;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTask(plugin);
    }

    public void setPlayerAsLobby(Player player) {
        playerStates.put(player, PLAYERLOBBY);
        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (plugin.getTeamManager().getTeam(player) == null) {
            tryRandomTeamJoin(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTask(plugin);

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

    private boolean tryRandomTeamJoin(Player player) {
        Team[] teams = Team.values();
        List<Team> teamList = Arrays.asList(teams);
        Collections.shuffle(teamList);

        for (Team team : teamList) {
            if (plugin.getTeamManager().getPlayersInTeam(team) < plugin.getTeamManager().getMaxPlayersPerTeam()) {
                plugin.getTeamManager().joinTeam(player, team);
                return true;
            }
        }
        return false;
    }
}
