/*  TO-DO List
*   1. Nove ideje 
*
*
*
* */


package me.cbhud.castlesiege;

import me.cbhud.castlesiege.util.*;
import org.bukkit.plugin.java.*;
import me.cbhud.castlesiege.playerstate.*;
import me.cbhud.castlesiege.scoreboard.*;
import me.cbhud.castlesiege.gui.*;
import me.cbhud.castlesiege.team.*;
import me.cbhud.castlesiege.kits.*;
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
    private ScoreboardManager scoreboardManager;
    private Timers timers;
    private MapRegeneration mapRegeneration;
    private TeamSelector teamSelector;
    private KitSelector kitSelector;
    private MobManager mobManager;
    private LocationManager locationManager;
    private Manager manager;
    private PlayerKitManager playerKitManager;
    private DataManager dataManager;

    private ConfigManager configManager;

    private MessagesConfiguration messagesConfig;
    private TNTThrower tntThrower;

    public void onEnable() {

        configManager = new ConfigManager(this);
        configManager.setup();
        dataManager = new DataManager(this);
        dataManager.connect();
        messagesConfig = new MessagesConfiguration(this);
        messagesConfig.loadConfig();

        this.game = new Game(this);
        this.type = new TypeManager(this);
        this.playerKitManager = new PlayerKitManager(this);
        this.teamManager = new TeamManager(this, this.getConfig());
        this.playerManager = new PlayerManager(this, teamManager);
        this.mobManager = new MobManager(this, this.teamManager, configManager);
        this.locationManager = new LocationManager(this);
        this.timers = new Timers(this, configManager, messagesConfig);
        this.gameEndHandler = new GameEndHandler(this, configManager, this.timers,messagesConfig);
        this.saveDefaultConfig();
        this.reloadConfig();
        manager = new Manager(configManager);
        this.getCommand("stats").setExecutor(new StatsCommand(dataManager));
        this.getCommand("coins").setExecutor(new CoinsCommand(this));
        this.getCommand("cs").setExecutor(new Commands(this, teamManager, mobManager, messagesConfig));
        this.getServer().getPluginManager().registerEvents(new PlayerConnection(this, this.teamManager, this.timers, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents(new RightClickEffects(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents(new MiscEvents(this), (Plugin)this);

        this.teamSelector = new TeamSelector();
        this.kitSelector = new KitSelector();
        this.scoreboardManager = new ScoreboardManager(this, this.teamManager, this.mobManager, configManager);
        this.game.setState(GameState.LOBBY);
        mapRegeneration = new MapRegeneration(this);
        getServer().getPluginManager().registerEvents(mapRegeneration, this);
        tntThrower = new TNTThrower(this, this.configManager);
        getServer().getPluginManager().registerEvents(tntThrower, this);
        this.getServer().getConsoleSender().sendMessage("CastleSiege has been enabled!");
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage("CastleSiege has been disabled!");
        dataManager.disconnect();
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
    public TNTThrower tntThrower(){return this.tntThrower;}

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

    public DataManager getDbConnection() {
        return dataManager;
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

    public LocationManager getLocationManager(){return  locationManager;}
}
