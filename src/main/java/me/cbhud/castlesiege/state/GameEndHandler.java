package me.cbhud.castlesiege.state;

import me.cbhud.castlesiege.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import me.cbhud.castlesiege.team.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.file.*;

public class GameEndHandler implements Listener
{
    private final CastleSiege plugin;
    private Team winner;
    static String killername;


    public GameEndHandler(final CastleSiege plugin, Team winner) {
        this.plugin = plugin;
        this.winner = winner;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    public void handleGameEnd() {
        this.plugin.getGame().setState(GameState.END);
        plugin.getMobManager().removeCustomZombie();
        setPlayerWins();
        plugin.tntThrower().clearCooldowns();
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            this.plugin.getGame().setState(GameState.LOBBY);
            this.teleportPlayersToLobby();
        }, 200L);
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            this.plugin.getMapRegeneration().regenerateChangedBlocks();
            if (Bukkit.getOnlinePlayers().size() >= this.plugin.getConfigManager().getAutoStartPlayers()) {
                this.plugin.getTimer().checkAutoStart(Bukkit.getOnlinePlayers().size());
            }
        }, 300L);
    }

    public static String getKillername() {
        return killername;
    }

    @EventHandler
    public void onZombieDeath(final EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        if (this.plugin.getGame().getState() != GameState.END && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("King") && this.plugin.getGame().getState() == GameState.IN_GAME) {
            event.getDrops().clear();
            this.plugin.getTimer().cancelTimer();
            if (event.getEntity().getKiller() instanceof Player) {
                killername = player.getName();
                plugin.getDbConnection().incrementKingKills(event.getEntity().getKiller().getUniqueId());
            }
            else {
                killername = "unknown";
            }
            setWinner(Team.Attackers);
            handleGameEnd();
        }
    }

    private void setPlayerWins() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Team winner = getWinner();
            if (winner != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (plugin.getTeamManager().getTeam(player) == winner) {
                        plugin.getDbConnection().incrementWins(player.getUniqueId(), plugin.getConfigManager().getWc());
                    }
                }
            }
        });
    }


    private void teleportPlayersToLobby() {
        final FileConfiguration config = this.plugin.getConfig();
        if (config.contains("lobby.world")) {
            final String worldName = config.getString("lobby.world");
            final double x = config.getDouble("lobby.x");
            final double y = config.getDouble("lobby.y");
            final double z = config.getDouble("lobby.z");
            final float yaw = (float)config.getDouble("lobby.yaw");
            final float pitch = (float)config.getDouble("lobby.pitch");
            final Location lobbyLocation = new Location(this.plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(lobbyLocation);
                this.plugin.getPlayerManager().setPlayerAsLobby(player);
            }
        }
        else {
            Bukkit.getLogger().warning("World is null!");
        }
    }


    public void setWinner(Team winner) {
        this.winner = winner;
        broadcastWinnerMessage();
    }

    public Team getWinner() {
        return winner;
    }

    private void broadcastWinnerMessage() {
        if (winner != null) {
            String winnerName = winner.toString(); // Assumes that your Team enum has a proper toString method
            if ("Attackers".equalsIgnoreCase(winnerName)) {
                for (String line : plugin.getMessagesConfig().getVikingsWinMsg()) {
                    line = line.replace("{killer}", getKillername());
                    line = line.replace("{attackers}", plugin.getConfigManager().getAttacker());
                    Bukkit.broadcastMessage(line);
                }

                // Play Ender Dragon growl sound for VIKINGS win
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.RED + plugin.getConfigManager().getConfig().getString("attackersTeamName"), ChatColor.YELLOW + "won the game", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1.0f, 0.9f);
                }
            } else {

                for (String line : plugin.getMessagesConfig().getFranksWinMsg()) {
                    line = line.replace("{defenders}", plugin.getConfigManager().getDefender());
                    Bukkit.broadcastMessage(line);
                }

                // Play Note Block chime sound for FRANKS win
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.BLUE + plugin.getConfigManager().getConfig().getString("defendersTeamName"), ChatColor.YELLOW + "won the game", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.MASTER, 1.0f, 0.9f);
                }
            }
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "Game cancelled, both teams have disconnected.");
        }
    }


}
