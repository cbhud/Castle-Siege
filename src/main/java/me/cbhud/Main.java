/*  TO-DO List
*   1. Nove ideje 
*
*
*
* */


package me.cbhud;

import org.bukkit.plugin.java.*;
import me.cbhud.playerstate.*;
import me.cbhud.scoreboard.*;
import me.cbhud.gui.*;
import org.bukkit.command.*;
import me.cbhud.team.*;
import me.cbhud.kits.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import me.cbhud.event.*;
import me.cbhud.state.*;

public class Main extends JavaPlugin
{
    private Game game;
    private TypeManager type;
    private GameEndHandler gameEndHandler;
    private GameWinner gameWinner;
    private TeamManager teamManager;
    private CountdownTimer countdownTimer;
    private PlayerManager playerManager;
    private PlayerStateManager playerStateManager;
    private ScoreboardManager scoreboardManager;
    private Autostart autoStart;
    private TeamSelector teamSelector;
    private KitSelector kitSelector;
    private MobManager mobManager;
    private WolfManager wolfManager;
    private Manager manager;
    private PlayerKitManager playerKitManager;

    private ConfigManager configManager;

    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setup();
        this.game = new Game(this);
        this.type = new TypeManager(this);
        this.playerKitManager = new PlayerKitManager();
        this.playerStateManager = new PlayerStateManager();
        this.teamManager = new TeamManager(this, this.getConfig());
        this.playerManager = new PlayerManager(this, this.playerStateManager, teamManager);
        this.mobManager = new MobManager(this, this.teamManager, configManager);
        this.wolfManager = new WolfManager(this, this.teamManager, configManager);
        this.autoStart = new Autostart(this, configManager);
        this.gameEndHandler = new GameEndHandler(this, configManager, this.autoStart);
        this.gameWinner = new GameWinner();
        manager = new Manager();
        this.countdownTimer = new CountdownTimer(this);
        this.getCommand("kit").setExecutor((CommandExecutor)new KitCommand(this));
        this.getCommand("cs").setExecutor((CommandExecutor)new Commands(this, teamManager, mobManager));
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoin(this, this.game, this.teamManager, this.autoStart, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDeathHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AxeEvent(this, (Plugin)this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new DamageListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RightClickEffects(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MiscEvents(this), (Plugin)this);
        this.saveDefaultConfig();
        this.reloadConfig();
        this.teamSelector = new TeamSelector();
        this.kitSelector = new KitSelector();
        this.scoreboardManager = new ScoreboardManager(this, this.teamManager, this.mobManager, configManager);
        this.game.setState(GameState.LOBBY);
        this.getServer().getConsoleSender().sendMessage("CastleSiege has been enabled!");
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage("Vikings has been disabled!");
    }

    public Game getGame() {
        return this.game;
    }

    public WolfManager getWolfManager(){return this.wolfManager;}
    public TypeManager getType(){
        return this.type;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public GameEndHandler getGameEndHandler() {
        return this.gameEndHandler;
    }

    public CountdownTimer getCountdownTimer() {
        return this.countdownTimer;
    }

    public GameWinner getWinner() {
        return this.gameWinner;
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

    public KitSelector getKitSelector() {
        return this.kitSelector;
    }
}
