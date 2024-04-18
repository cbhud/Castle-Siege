package me.cbhud.castlesiege.state;

import me.cbhud.castlesiege.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import me.cbhud.castlesiege.team.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndHandler implements Listener
{
    private final Main plugin;
    private final ConfigManager configManager;
    private final Timers autostart;
    static String killername;

    public GameEndHandler(final Main plugin, final ConfigManager configManager, final Timers autostart) {
        this.plugin = plugin;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.autostart = autostart;
    }

    public void handleGameEnd() {
        this.plugin.getGame().setState(GameState.END);
        this.removeCustomZombies();
        setPlayerWins();
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            this.plugin.getGame().setState(GameState.LOBBY);
            this.teleportPlayersToLobby();
        }, 200L);
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            this.plugin.getMapRegeneration().regenerateChangedBlocks();
            if (Bukkit.getOnlinePlayers().size() >= this.configManager.getAutoStartPlayers()) {
                this.autostart.checkAutoStart(Bukkit.getOnlinePlayers().size());
            }
        }, 300L);
    }

    public static String getKillername() {
        return GameEndHandler.killername;
    }

    @EventHandler
    public void onZombieDeath(final EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        if (this.plugin.getGame().getState() != GameState.END && event.getEntity() instanceof Zombie && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("King") && this.plugin.getGame().getState() == GameState.IN_GAME) {
            event.getDrops().clear();
            this.plugin.getTimer().cancelTimer();
            if (event.getEntity().getKiller() instanceof Player) {
                GameEndHandler.killername = player.getName();
                plugin.getDbConnection().incrementKingKills(event.getEntity().getKiller().getUniqueId());
            }
            else {
                GameEndHandler.killername = "unknown";
            }
            this.plugin.getWinner().setWinner(Team.Vikings);
            this.plugin.getGameEndHandler().handleGameEnd();
        }
    }

    private void setPlayerWins(){
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.getTeamManager().getTeam(player) == plugin.getWinner().getWinner()) {
                    plugin.getDbConnection().incrementWins(player.getUniqueId());
                }
            }
    }

    private void removeCustomZombies() {
        for (final World world : Bukkit.getWorlds()) {
            for (final LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null && entity.getCustomName().contains("King") || entity instanceof Wolf && entity.getCustomName() != null && entity.getCustomName().contains("Wolf")) {
                    entity.remove();
                }
            }
        }
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
}
