// GameWinner.java

package me.cbhud.state;

import me.cbhud.team.Team;
import org.bukkit.Bukkit;
import me.cbhud.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class GameWinner {

    private Team winner;
    private GameState state;
    private final Main plugin;

    public GameWinner(Main plugin) {
        this.plugin = plugin;
        this.state = GameState.LOBBY;
    }

    // Other methods...

    public void setWinner(Team winner) {
        this.winner = winner;
        broadcastWinnerMessage();
    }

    public Team getWinner() {
        return winner;
    }

    private void broadcastWinnerMessage() {
        if (winner != null) {
            String winnerName = winner.toString(); // Assumes that your Team enum has a proper toString method
            if ("VIKINGS".equals(winnerName)) {
                Bukkit.broadcastMessage("§7§m--------------------------");
                Bukkit.broadcastMessage("§cVikings conquered the castle");
                Bukkit.broadcastMessage(ChatColor.RED + GameEndHandler.getKillername() + " §cslaughtered §e§lKing Charles");
                Bukkit.broadcastMessage("§7§m--------------------------");

                // Play Ender Dragon growl sound for VIKINGS win
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.RED + "Vikings", ChatColor.RED + "won the game", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1.0f, 0.9f);
                }
            } else {
                Bukkit.broadcastMessage("§7§m--------------------------");
                Bukkit.broadcastMessage("§bFranks defended the castle!");
                Bukkit.broadcastMessage("§e§lKing Charles §fhas survived the siege!");
                Bukkit.broadcastMessage("§7§m--------------------------");

                // Play Note Block chime sound for FRANKS win
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.BLUE + "Franks", ChatColor.BLUE + "won the game", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.MASTER, 1.0f, 0.9f);
                }
            }
        } else {
            Bukkit.getLogger().warning("Winner is null. Make sure to set a valid winner.");
        }
    }
}
