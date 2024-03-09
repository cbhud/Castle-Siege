package me.cbhud;

import org.bukkit.plugin.java.*;
import me.cbhud.playerstate.*;
import me.cbhud.scoreboard.*;
import me.cbhud.gui.*;
import me.cbhud.items.*;
import org.bukkit.command.*;
import me.cbhud.team.*;
import me.cbhud.Commands.*;
import me.cbhud.kits.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import me.cbhud.Events.*;
import me.cbhud.state.*;

public class Main extends JavaPlugin
{
    private Game game;
    private GameEndHandler gameEndHandler;
    private GameWinner gameWinner;
    private TeamManager teamManager;
    private ConfigManager configManager;
    private static Main instance;
    private CountdownTimer countdownTimer;
    private PlayerManager playerManager;
    private PlayerStateManager playerStateManager;
    private ScoreboardManager scoreboardManager;
    private Autostart autoStart;
    private TeamSelector teamSelector;
    private KitSelector kitSelector;
    private MobManager mobManager;
    private PlayerKitManager playerKitManager;

    public void onEnable() {
        Main.instance = this;
        final ConfigManager configManager = new ConfigManager(this);
        configManager.setup();
        this.game = new Game(this);
        this.playerKitManager = new PlayerKitManager();
        this.playerStateManager = new PlayerStateManager();
        this.playerManager = new PlayerManager(this, this.playerStateManager);
        this.teamManager = new TeamManager(this, this.getConfig());
        this.mobManager = new MobManager(this, this.teamManager, configManager);
        this.autoStart = new Autostart(this, configManager);
        this.gameEndHandler = new GameEndHandler(this, configManager, this.autoStart);
        this.gameWinner = new GameWinner();
        Manager.init();
        this.countdownTimer = new CountdownTimer(this);
        this.getCommand("setlobby").setExecutor((CommandExecutor)new LobbyCommand(this));
        this.getCommand("start").setExecutor((CommandExecutor)new StartCommand(this, this.teamManager, this.mobManager));
        this.getCommand("teamjoin").setExecutor((CommandExecutor)new TeamJoinCommand(this, this.teamManager));
        this.getCommand("setspawn").setExecutor((CommandExecutor)new TeamSpawnCommand(this));
        this.getCommand("setmobspawn").setExecutor((CommandExecutor)new SetMobSpawnCommand(this));
        this.getCommand("endgame").setExecutor((CommandExecutor)new EndGameCommand(this));
        this.getCommand("kit").setExecutor((CommandExecutor)new KitCommand(this));
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoin(this, this.game, this.teamManager, this.autoStart, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDeathHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AxeEvent((Plugin)this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new DamageListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RightClickEffects(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MiscEvents(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerLeft(this, configManager, this.autoStart), (Plugin)this);
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

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
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
