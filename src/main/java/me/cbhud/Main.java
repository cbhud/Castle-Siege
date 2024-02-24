package me.cbhud;

import me.cbhud.Commands.*;
import me.cbhud.Events.*;
import me.cbhud.gui.KitSelector;
import me.cbhud.gui.TeamSelector;
import me.cbhud.items.Manager;
import me.cbhud.kits.KitCommand;
import me.cbhud.kits.PlayerKitManager;
import me.cbhud.scoreboard.ScoreboardManager;
import me.cbhud.spectator.PlayerStateManager;
import me.cbhud.spectator.SpectatorManager;
import me.cbhud.state.Game;
import me.cbhud.state.GameEndHandler;
import me.cbhud.state.GameState;
import me.cbhud.state.GameWinner;
import me.cbhud.team.TeamJoinCommand;
import me.cbhud.team.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
    private Game game;
    private GameEndHandler gameEndHandler;
    private GameWinner gameWinner;
    private TeamManager teamManager;
    private ConfigManager configManager;

    private static Main instance;
    private CountdownTimer countdownTimer;
    private SpectatorManager spectatorManager;
    private PlayerStateManager playerStateManager;
    private ScoreboardManager scoreboardManager;
    private Autostart autoStart;
    private TeamSelector teamSelector;
    private KitSelector kitSelector;

    private MobManager mobManager;
    private PlayerKitManager playerKitManager;

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager configManager = new ConfigManager(this);
        configManager.setup();

        game = new Game(this);
        playerKitManager = new PlayerKitManager();


        this.playerStateManager = new PlayerStateManager();
        this.spectatorManager = new SpectatorManager(this, playerStateManager);
        teamManager = new TeamManager(this, getConfig());
        mobManager = new MobManager(this, teamManager, configManager);

        autoStart = new Autostart(this, configManager);


        gameEndHandler = new GameEndHandler(this, configManager, autoStart);
        this.gameWinner = new GameWinner(this);

        Manager.init();

        countdownTimer = new CountdownTimer(this);

        // Start the scoreboard update task
        // Commands initialization
        getCommand("setlobby").setExecutor(new LobbyCommand(this));
        getCommand("start").setExecutor(new StartCommand(this, teamManager, mobManager));
        getCommand("teamjoin").setExecutor(new TeamJoinCommand(this, teamManager));
        getCommand("setspawn").setExecutor(new TeamSpawnCommand(this));
        getCommand("setmobspawn").setExecutor(new SetMobSpawnCommand(this));
        getCommand("endgame").setExecutor(new EndGameCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        // Event registration
        getServer().getPluginManager().registerEvents(new PlayerJoin(this, game, teamManager, autoStart, configManager), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new AxeEvent(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new RightClickEffects(),this);
        getServer().getPluginManager().registerEvents(new MiscEvents(this), this);
        getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new KillEffects(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeft(this, configManager, autoStart), this);
        saveDefaultConfig(); // Save the default config if it doesn't exist
        reloadConfig(); // Reload the config to ensure we have the latest
        teamSelector = new TeamSelector();
        kitSelector = new KitSelector();
        scoreboardManager = new ScoreboardManager(this, teamManager, mobManager, configManager);
        game.setState(GameState.LOBBY);
        getServer().getConsoleSender().sendMessage("CastleSiege has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("Vikings has been disabled!");
    }

    public Game getGame() {
        return game;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public ConfigManager getConfigManager(){
        return configManager;
    }

    public GameEndHandler getGameEndHandler() {
        return gameEndHandler;
    }

    public CountdownTimer getCountdownTimer() {
        return countdownTimer;
    }

    public GameWinner getWinner() {
        return gameWinner;
    }

    public PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }

    public SpectatorManager getSpectatorManager() {
        return spectatorManager;
    }

    public PlayerKitManager getPlayerKitManager(){return playerKitManager;}

    // SCOREBOARD

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TeamSelector getTeamSelector() {
        return teamSelector;
    }
    public KitSelector getKitSelector(){
        return kitSelector;
    }

}