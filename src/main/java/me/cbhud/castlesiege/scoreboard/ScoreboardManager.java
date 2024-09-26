package me.cbhud.castlesiege.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private final CastleSiege plugin;

    public final Map<Player, FastBoard> scoreboards;



    public ScoreboardManager(CastleSiege plugin) {
        this.plugin = plugin;
        this.scoreboards = new HashMap<>();
    }

    public String getTeamName(Team team) {
        if (team == Team.Attackers) {
            return plugin.getConfigManager().getConfig().getString("attackersTeamName");
        } else {
            return plugin.getConfigManager().getConfig().getString("defendersTeamName");
        }
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
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();
        ChatColor titleColor = plugin.getConfigManager().getTitleColor();

        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        board.updateTitle(title);
        board.updateLine(0, " ");
        board.updateLine(1, mainColor +"Online: " +secondaryColor+ Bukkit.getOnlinePlayers().size());
        board.updateLine(2, " ");
        board.updateLine(3, mainColor + "Type: " + secondaryColor + plugin.getType().getState());
        Team team = plugin.getTeamManager().getTeam(player);
        board.updateLine(4, " ");
        board.updateLine(5, mainColor +"Team: " +secondaryColor+ (team != null ? getTeamName(team) : "No Team"));
        board.updateLine(6, " ");
        board.updateLine(7, mainColor +"Kit: " +secondaryColor+ (plugin.getPlayerKitManager().hasSelectedKit(player) ?
                plugin.getPlayerKitManager().getSelectedKit(player).getName() : "Select kit"));
        board.updateLine(8, " ");
        board.updateLine(9, mainColor +"Coins: " +secondaryColor+ plugin.getDbConnection().getPlayerCoins(player.getUniqueId()));
        board.updateLine(10," ");
        board.updateLine(11, bottomline);
    }

    private void setupInGameScoreboard(FastBoard board) {
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();

        ChatColor titleColor = plugin.getConfigManager().getTitleColor();

        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        board.updateTitle(title);
        board.updateLine(0, " ");
        updateInGameScoreboard(board); // Updating the in-game scoreboard initially
    }

    private void setupEndScoreboard(FastBoard board) {
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();

        ChatColor titleColor = plugin.getConfigManager().getTitleColor();
        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        board.updateTitle(title);
        board.updateLines("");
        board.updateLine(0, " ");
        board.updateLine(1, mainColor +"Winners: " + secondaryColor + getTeamName(plugin.getGameEndHandler().getWinner()));
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
                .filter(p -> plugin.getTeamManager().getTeam(p) == Team.Attackers)
                .count();
        franks = (int) Bukkit.getOnlinePlayers().stream()
                .filter(p -> plugin.getTeamManager().getTeam(p) == Team.Defenders)
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
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();

        ChatColor titleColor = plugin.getConfigManager().getTitleColor();

        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        board.updateLines("", "", "");
        board.updateLine(1, mainColor +"Online: " + secondaryColor + Bukkit.getOnlinePlayers().size());
        board.updateLine(2, " ");
        board.updateLine(3, mainColor + "Type: " + secondaryColor + plugin.getType().getState());
        Team team = plugin.getTeamManager().getTeam(player);
        board.updateLine(4, " ");
        board.updateLine(5, mainColor +"Team: " + secondaryColor + (team != null ? getTeamName(team) : "No Team"));
        board.updateLine(6, " ");
        board.updateLine(7, mainColor +"Kit: " + secondaryColor + (plugin.getPlayerKitManager().hasSelectedKit(player) ? plugin.getPlayerKitManager().getSelectedKit(player).getName() : "Select kit"));
        board.updateLine(8, " ");
        board.updateLine(9, mainColor +"Coins: " +secondaryColor+ plugin.getDbConnection().getPlayerCoins(player.getUniqueId()));
        board.updateLine(10," ");
        board.updateLine(11, bottomline);
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
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();

        ChatColor titleColor = plugin.getConfigManager().getTitleColor();

        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        int secondsLeft = plugin.getTimer().getSecondsLeft();
        board.updateLine(1, mainColor +"Countdown: " + secondaryColor + formatTime(secondsLeft));
        Zombie king = plugin.getMobManager().getKingZombie();
        board.updateLine(2, " ");
        double kingHealth = plugin.getMobManager().getZombieHealth(king);
        board.updateLine(3, mainColor +"King's Health: " + secondaryColor + (int) kingHealth);
        board.updateLine(4, " ");
        board.updateLine(5, mainColor + getTeamName(Team.Defenders) +": " + secondaryColor + franks);
        board.updateLine(6, " ");
        board.updateLine(7, mainColor + getTeamName(Team.Attackers) +": " + secondaryColor + vikings);
    }

    private void updateEndScoreboard(FastBoard board) {
        ChatColor mainColor = plugin.getConfigManager().getMainColor();
        ChatColor secondaryColor = plugin.getConfigManager().getSecondaryColor();
        ChatColor bottomColor = plugin.getConfigManager().getBottomColor();

        ChatColor titleColor = plugin.getConfigManager().getTitleColor();

        board.updateLines();
        String title = titleColor + "§l" + plugin.getConfigManager().getTitle();
        String bottomline = bottomColor + plugin.getConfigManager().getBottomline();
        board.updateLine(0, " ");
        board.updateLine(1, mainColor + "Winnners: " + secondaryColor + getTeamName(plugin.getGameEndHandler().getWinner()));
        board.updateLine(2, " ");
        board.updateLine(3, bottomline);
    }

    public void updateScoreboardForAll() {
        Bukkit.getOnlinePlayers().forEach(this::updateScoreboard);
    }

    public void decrementTeamPlayersCount(Player player) {
        Team team = plugin.getTeamManager().getTeam(player);
        if (team != null) {
                switch (team) {
                    case Defenders:
                        if(franks > 0){
                        franks--;}
                        break;
                    case Attackers:
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
