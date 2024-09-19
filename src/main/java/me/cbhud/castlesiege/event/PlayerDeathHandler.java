package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.kits.KitType;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.state.Type;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PlayerDeathHandler implements Listener {
    private final Main plugin;

    public PlayerDeathHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (plugin.getGame().getState() == GameState.IN_GAME) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                UUID playerId = player.getUniqueId();
                UUID killerId = (killer != null) ? killer.getUniqueId() : null;
                plugin.getDbConnection().incrementDeaths(playerId);
                if (killerId != null) {
                    plugin.getDbConnection().incrementKills(killerId, plugin.getConfigManager().getKc());
                }
            });

            if (plugin.getType().getState() == Type.Hardcore) {
                event.getDrops().clear();
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                plugin.getScoreboardManager().decrementTeamPlayersCount(player);
                player.sendTitle(ChatColor.RED + "You have died!", ChatColor.GRAY + "Better luck next time!", 10, 70, 20);
                if (plugin.getScoreboardManager().getVikings() < 1) {
                    plugin.getGameEndHandler().setWinner(Team.Defenders);
                    plugin.getGameEndHandler().handleGameEnd();
                }
                if (killer != null) {
                    KitType killerKit = plugin.getPlayerKitManager().getSelectedKit(killer);
                    if (killerKit != null) {
                        applyKillEffects(killer, killerKit);
                    }
                }
                return;
            }

            event.getDrops().clear();
            plugin.getPlayerManager().setPlayerAsSpectator(player);
            player.sendTitle(ChatColor.RED + "You died!", ChatColor.GRAY + "Respawning... 5 seconds", 10, 70, 20);

            Team team = plugin.getTeamManager().getTeam(player);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.spigot().respawn();
                    player.teleport(plugin.getLocationManager().getSpawnLocationForTeam(team));
                    plugin.getPlayerManager().setPlayerAsPlaying(player);
                    plugin.getPlayerKitManager().giveKit(player, plugin.getPlayerKitManager().getSelectedKit(player));
            }, 5 * 20); // 5 seconds
        }

        if (killer != null) {
            KitType killerKit = plugin.getPlayerKitManager().getSelectedKit(killer);
            if (killerKit != null) {
                applyKillEffects(killer, killerKit);
            }
        }
    }



    // Adding KillEffects functionality
    private void applyKillEffects(Player player, KitType kitType) {
        switch (kitType) {
            case MARKSMAN:
                player.getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                break;
            case SPEARMAN:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                break;
            case KNIGHT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                break;
            case BERSERKER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0));
                player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 1)));
                break;
            case SKALD:
                player.getInventory().addItem(Manager.harm);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
                break;
            case WARRIOR:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                break;
            case WIZARD:
                triggerArcaneBlast(player);
                break;
            case BOMBARDIER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1));
                break;
        }
    }



    private void triggerArcaneBlast(Player wizard) {
        int blastRadius = 10; // Adjust as needed
        double blastDamage = 3.0; // Adjust as needed

        Location blastLocation = wizard.getLocation();

        // Damage nearby enemies within the blast radius who are on Team Vikings
        for (Entity entity : blastLocation.getWorld().getNearbyEntities(blastLocation, blastRadius, blastRadius, blastRadius)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Attackers) {
                    nearbyPlayer.damage(blastDamage, wizard);
                }
            }
        }

        // Inform the wizard about the successful blast
        wizard.sendMessage("You unleash an arcane blast, damaging nearby enemies with explosion!");
    }



}
