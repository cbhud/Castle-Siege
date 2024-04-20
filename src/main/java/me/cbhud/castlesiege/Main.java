/*  TO-DO List
*   1. Nove ideje 
*
*
*
* */


package me.cbhud.castlesiege;

import org.bukkit.plugin.java.*;
import me.cbhud.castlesiege.playerstate.*;
import me.cbhud.castlesiege.scoreboard.*;
import me.cbhud.castlesiege.gui.*;
import org.bukkit.command.*;
import me.cbhud.castlesiege.team.*;
import me.cbhud.castlesiege.kits.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import me.cbhud.castlesiege.event.*;
import me.cbhud.castlesiege.state.*;


public class Main extends JavaPlugin
{
    private Game game;
    private TypeManager type;
    private GameEndHandler gameEndHandler;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private PlayerStateManager playerStateManager;
    private ScoreboardManager scoreboardManager;
    private Timers timers;
    private MapRegeneration mapRegeneration;
    private TeamSelector teamSelector;
    private KitSelector kitSelector;
    private MobManager mobManager;
    private Manager manager;
    private PlayerKitManager playerKitManager;
    private DbConnection dbConnection;

    private ConfigManager configManager;

    public void onEnable() {

        configManager = new ConfigManager(this);
        configManager.setup();
        dbConnection = new DbConnection(this);
        dbConnection.connect();

        this.game = new Game(this);
        this.type = new TypeManager(this);
        this.playerKitManager = new PlayerKitManager();
        this.playerStateManager = new PlayerStateManager();
        this.teamManager = new TeamManager(this, this.getConfig());
        this.playerManager = new PlayerManager(this, this.playerStateManager, teamManager);
        this.mobManager = new MobManager(this, this.teamManager, configManager);
        this.timers = new Timers(this, configManager);
        this.gameEndHandler = new GameEndHandler(this, configManager, this.timers);
        manager = new Manager();
        this.getCommand("stats").setExecutor((CommandExecutor)new StatsCommand(dbConnection));
        this.getCommand("cs").setExecutor((CommandExecutor)new Commands(this, teamManager, mobManager));
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoin(this, this.teamManager, this.timers, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDeathHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new DamageListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RightClickEffects(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MiscEvents(this), (Plugin)this);
        this.saveDefaultConfig();
        this.reloadConfig();
        this.teamSelector = new TeamSelector();
        this.kitSelector = new KitSelector();
        this.scoreboardManager = new ScoreboardManager(this, this.teamManager, this.mobManager, configManager);
        this.game.setState(GameState.LOBBY);
        mapRegeneration = new MapRegeneration(this);
        getServer().getPluginManager().registerEvents(mapRegeneration, this);
        this.getServer().getConsoleSender().sendMessage("CastleSiege has been enabled!");
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage("Vikings has been disabled!");
        dbConnection.disconnect();
    }

    public Game getGame() {
        return this.game;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TypeManager getType(){
        return this.type;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public GameEndHandler getGameEndHandler() {
        return this.gameEndHandler;
    }

    public Timers getTimer() {
        return this.timers;
    }

    public PlayerStateManager getPlayerStateManager() {
        return this.playerStateManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public PlayerKitManager getPlayerKitManager() {
        return this.playerKitManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public TeamSelector getTeamSelector() {
        return this.teamSelector;
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public MobManager getMobManager(){
        return this.mobManager;
    }

    public KitSelector getKitSelector() {
        return this.kitSelector;
    }
    public MapRegeneration getMapRegeneration() {
        return mapRegeneration;
    }
}
