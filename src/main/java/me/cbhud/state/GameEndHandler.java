// GameEndHandler.java

package me.cbhud.state;

import me.cbhud.Autostart;
import me.cbhud.ConfigManager;
import me.cbhud.Main;
import me.cbhud.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class GameEndHandler implements Listener {

    private final Main plugin;
    private final ConfigManager configManager;
    private final Autostart autostart;
    public GameEndHandler(Main plugin, ConfigManager configManager, Autostart autostart) {
        this.plugin = plugin;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        this.autostart = autostart;
    }

    public void handleGameEnd() {
        plugin.getGame().setState(GameState.END);
        removeCustomZombies();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getGame().setState(GameState.LOBBY);
            teleportPlayersToLobby();
            if (Bukkit.getOnlinePlayers().size() >= configManager.getAutoStartPlayers()){
                autostart.checkAutoStart(Bukkit.getOnlinePlayers().size());
            }
        }, 20 * 15); // 20 ticks per second * 60 seconds = 1 minute
    }

    static String killername;

    public static String getKillername(){
        return  killername;
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {

            Player player = (Player) event.getEntity().getKiller();
        if (plugin.getGame().getState() != GameState.END) {
            // Handle zombie death event, e.g., end the game
            // You can check if the killed entity is your custom zombie and perform actions accordingly
            if (event.getEntity() instanceof Zombie && event.getEntity().getCustomName() != null
                    && event.getEntity().getCustomName().contains("King") && plugin.getGame().getState() == GameState.IN_GAME) {
                event.getDrops().clear();
                plugin.getCountdownTimer().cancelTimer();
                if (event.getEntity().getKiller() instanceof Player) {
                    killername = player.getName();
                }else{
                    killername = "unknown";
                }

                plugin.getWinner().setWinner(Team.VIKINGS);
                plugin.getGameEndHandler().handleGameEnd();
            }
        }
    }


    private void removeCustomZombies() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null &&
                        entity.getCustomName().contains("King")) {
                    entity.remove();
                }
            }
        }
    }



    private void teleportPlayersToLobby() {
        FileConfiguration config = plugin.getConfig();

        if (config.contains("lobby.world")) {
            String worldName = config.getString("lobby.world");
            double x = config.getDouble("lobby.x");
            double y = config.getDouble("lobby.y");
            double z = config.getDouble("lobby.z");
            float yaw = (float) config.getDouble("lobby.yaw");
            float pitch = (float) config.getDouble("lobby.pitch");

            Location lobbyLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(lobbyLocation);
                plugin.getSpectatorManager().setPlayerAsLobby(player);
            }
        } else {
            Bukkit.getLogger().warning("World is null!");
        }
    }
}
