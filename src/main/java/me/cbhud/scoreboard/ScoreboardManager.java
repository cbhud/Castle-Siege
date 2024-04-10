package me.cbhud.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.cbhud.ConfigManager;
import me.cbhud.Main;
import me.cbhud.MobManager;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private final Main plugin;
    private final TeamManager teamManager;
    private final MobManager mobManager;
    private final ConfigManager configManager;
    public final Map<Player, FastBoard> scoreboards;



    public ScoreboardManager(Main plugin, TeamManager teamManager, MobManager mobManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.mobManager = mobManager;
        this.configManager = configManager;
        this.scoreboards = new HashMap<>();
    }

    private int vikings;
    private int franks;


    public void setupScoreboard(Player player) {

        FastBoard board = new FastBoard(player);
        scoreboards.put(player, board);

        switch (plugin.getGame().getState()) {
            case LOBBY:
                setupLobbyScoreboard(board, player);
                break;
            case IN_GAME:
                setupInGameScoreboard(board);
                break;
            case END:
                setupEndScoreboard(board);
                break;
            // Add more cases for other states if needed
            default:
                break;
        }
    }

    private void setupLobbyScoreboard(FastBoard board, Player player) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();
        ChatColor titleColor = configManager.getTitleColor();

        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        board.updateTitle(title);
        board.updateLine(0, " ");
        board.updateLine(1, mainColor +"Online: " +secondaryColor+ Bukkit.getOnlinePlayers().size());
        board.updateLine(2, " ");
        board.updateLine(3, mainColor + "Type: " + secondaryColor + plugin.getType().getState());
        Team team = teamManager.getTeam(player);
        board.updateLine(4, " ");
        board.updateLine(5, mainColor +"Team: " +secondaryColor+ (team != null ? team.toString() : "No Team"));
        board.updateLine(6, " ");
        board.updateLine(7, mainColor +"Kit: " +secondaryColor+ (plugin.getPlayerKitManager().hasSelectedKit(player) ?
                plugin.getPlayerKitManager().getSelectedKit(player).getDisplayName() : "Select kit"));
        board.updateLine(8," ");
        board.updateLine(9, bottomline);
    }

    private void setupInGameScoreboard(FastBoard board) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();

        ChatColor titleColor = configManager.getTitleColor();

        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        board.updateTitle(title);
        board.updateLine(0, " ");
        updateInGameScoreboard(board); // Updating the in-game scoreboard initially
    }

    private void setupEndScoreboard(FastBoard board) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();

        ChatColor titleColor = configManager.getTitleColor();
        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        board.updateTitle(title);
        board.updateLines("");
        board.updateLine(0, " ");
        board.updateLine(1, mainColor +"Winners: " + secondaryColor + plugin.getWinner().getWinner());
        board.updateLine(2, " ");
        board.updateLine(7, bottomline);
    }

    public int getVikings(){
        return vikings;
    }

    public int getFranks(){
        return franks;
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void loadTeamCount() {
        vikings = (int) Bukkit.getOnlinePlayers().stream()
                .filter(p -> teamManager.getTeam(p) == Team.Vikings)
                .count();
        franks = (int) Bukkit.getOnlinePlayers().stream()
                .filter(p -> teamManager.getTeam(p) == Team.Franks)
                .count();
    }


    public void updateScoreboard(Player player) {
        FastBoard board = scoreboards.get(player);
        if (board != null) {
            switch (plugin.getGame().getState()) {
                case LOBBY:
                    updateLobbyScoreboard(board, player);
                    break;
                case IN_GAME:
                    updateInGameScoreboard(board); // Continue updating in-game scoreboard
                    break;
                case END:
                    updateEndScoreboard(board);
                    break;
                // Add more cases for other states if needed
                default:
                    break;
            }
        }
    }

    private void updateLobbyScoreboard(FastBoard board, Player player) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();

        ChatColor titleColor = configManager.getTitleColor();

        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        board.updateLines("", "", "");
        board.updateLine(1, mainColor +"Online: " + secondaryColor + Bukkit.getOnlinePlayers().size());
        board.updateLine(2, " ");
        board.updateLine(3, mainColor + "Type: " + secondaryColor + plugin.getType().getState());
        Team team = teamManager.getTeam(player);
        board.updateLine(4, " ");
        board.updateLine(5, mainColor +"Team: " + secondaryColor + (team != null ? team.toString() : "No Team"));
        board.updateLine(6, " ");
        board.updateLine(7, mainColor +"Kit: " + secondaryColor + (plugin.getPlayerKitManager().hasSelectedKit(player) ? plugin.getPlayerKitManager().getSelectedKit(player).getDisplayName() : "Select kit"));
        board.updateLine(8, "");
        board.updateLine(9, bottomline);
    }

    // Updated method to include the team parameter
    public void updateLobbyScoreboardForTeam(Team team) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            FastBoard board = scoreboards.get(player);
            if (board != null && plugin.getGame().getState() == GameState.LOBBY) {
                updateLobbyScoreboard(board, player); // Updated to include the player parameter
            }
        });
    }

    public void updateInGameScoreboard(FastBoard board) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();

        ChatColor titleColor = configManager.getTitleColor();

        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        int secondsLeft = plugin.getTimer().getSecondsLeft();
        board.updateLine(1, mainColor +"Countdown: " + secondaryColor + formatTime(secondsLeft));
        Zombie king = mobManager.getKingZombie();
        double kingHealth = mobManager.getZombieHealth(king);
        board.updateLine(3, mainColor +"King's Health: " + secondaryColor + (int) kingHealth);
        board.updateLine(5, mainColor +"Vikings: " + secondaryColor + vikings);
        board.updateLine(7, mainColor +"Franks: " + secondaryColor + franks);
        board.updateLine(8, " ");
        board.updateLine(9, bottomline);
    }

    private void updateEndScoreboard(FastBoard board) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        ChatColor bottomColor = configManager.getBottomColor();

        ChatColor titleColor = configManager.getTitleColor();

        board.updateLines();
        String title = titleColor + "§l" + configManager.getTitle();
        String bottomline = bottomColor + configManager.getBottomline();
        board.updateLine(0, " ");
        board.updateLine(1, mainColor + "Winnners: " + secondaryColor + plugin.getWinner().getWinner());
        board.updateLine(2, " ");
        board.updateLine(3, bottomline);
    }

    public void updateScoreboardForAll() {
        Bukkit.getOnlinePlayers().forEach(this::updateScoreboard);
    }

    public void decrementTeamPlayersCount(Player player) {
        ChatColor mainColor = configManager.getMainColor();
        ChatColor secondaryColor = configManager.getSecondaryColor();
        Team team = teamManager.getTeam(player);
        if (team != null) {
                switch (team) {
                    case Franks:
                        if(franks > 0){
                        franks--;}
                        break;
                    case Vikings:
                        if(vikings > 0) {
                            vikings--;
                        }
                        break;
                }
            }
        }



    public void removeScoreboard(Player player) {
        FastBoard board = scoreboards.remove(player);
        if (board != null) {
            board.delete();
        }
    }
}
