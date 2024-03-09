package me.cbhud.state;

import me.cbhud.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import me.cbhud.team.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

public class GameEndHandler implements Listener
{
    private final Main plugin;
    private final ConfigManager configManager;
    private final Autostart autostart;
    static String killername;

    public GameEndHandler(final Main plugin, final ConfigManager configManager, final Autostart autostart) {
        this.plugin = plugin;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.autostart = autostart;
    }

    public void handleGameEnd() {
        this.plugin.getGame().setState(GameState.END);
        this.removeCustomZombies();
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            this.plugin.getGame().setState(GameState.LOBBY);
            this.teleportPlayersToLobby();
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
            this.plugin.getCountdownTimer().cancelTimer();
            if (event.getEntity().getKiller() instanceof Player) {
                GameEndHandler.killername = player.getName();
            }
            else {
                GameEndHandler.killername = "unknown";
            }
            this.plugin.getWinner().setWinner(Team.VIKINGS);
            this.plugin.getGameEndHandler().handleGameEnd();
        }
    }

    private void removeCustomZombies() {
        for (final World world : Bukkit.getWorlds()) {
            for (final LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null && entity.getCustomName().contains("King")) {
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
