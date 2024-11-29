package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.game.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class MiscEvents implements Listener {

    private final CastleSiege plugin;

    public MiscEvents(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onZombieDeath(final EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        if (this.plugin.getGame().getState() != GameState.END && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("King") && this.plugin.getGame().getState() == GameState.IN_GAME) {
            event.getDrops().clear();
            this.plugin.getTimer().cancelTimer();
            if (event.getEntity().getKiller() instanceof Player) {
                plugin.getGameEndHandler().setKiller(player.getName());
                plugin.getDbConnection().incrementKingKills(event.getEntity().getKiller().getUniqueId());
            }
            else {

                plugin.getGameEndHandler().setKiller("unknown");

            }
            plugin.getGameEndHandler().setWinner(Team.Attackers);
            plugin.getGameEndHandler().handleGameEnd();
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (player.isOp() || player.hasPermission("cs.admin")) return;

        if (plugin.getGame().getState() == GameState.IN_GAME &&
                plugin.getTeamManager().getTeam(player) == Team.Defenders &&
                event.getItem().getItemStack().getType() == Material.OAK_FENCE) {
            return;
        }

        event.setCancelled(true);
    }


}
