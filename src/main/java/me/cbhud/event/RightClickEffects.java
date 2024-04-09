package me.cbhud.event;

import me.cbhud.Main;
import me.cbhud.gui.Manager;
import me.cbhud.kits.KitType;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class RightClickEffects implements Listener {

    private final Main plugin;
    private final Random rand = new Random();
    private static final int EFFECT_DURATION = 100;

    public RightClickEffects(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = event.getItem();

        if (clickedItem == null  || plugin.getGame().getState() == GameState.END){
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;}

        if (useSpecialItem(player, clickedItem)){
            removeItem(player, clickedItem);
        }
    }

    private boolean useSpecialItem(Player player, ItemStack item) {
        if (item.getType() == Material.CLOCK && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getTeamSelector().getInventory());
            return true;
        }

        if (item.getType() == Material.NETHER_STAR && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getKitSelector().getInventory());
            return true;
        }

        if (item.getItemMeta().equals(Manager.stew.getItemMeta())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, EFFECT_DURATION, 1));
            return true;
        }

        if (item.getItemMeta().equals(Manager.rage.getItemMeta())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, EFFECT_DURATION, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, EFFECT_DURATION, 0));
            return true;
        }

        if (item.getItemMeta().equals(Manager.ragnarok.getItemMeta())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, EFFECT_DURATION, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION, 0));
            return true;
        }

        if (item.getItemMeta().equals(Manager.sight.getItemMeta())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, EFFECT_DURATION * 2, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, EFFECT_DURATION, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, EFFECT_DURATION, 1));
            return true;
        }

        if (item.getItemMeta().equals(Manager.bone2.getItemMeta()) && plugin.getTeamManager().getTeam(player) == Team.Vikings && plugin.getPlayerKitManager().getSelectedKit(player) == KitType.BEASTMASTER) {
            plugin.getWolfManager().spawnCustomMob(player);
            return true;
        }

        if (item.getItemMeta().equals(Manager.attack.getItemMeta())) {
            for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                        plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Vikings) {
                    applyRandomEffect(nearbyPlayer);
                }
            }
            return true;
        }

        if (item.getItemMeta().equals(Manager.support.getItemMeta())) {
            for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                        plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Franks) {
                    applyRandomSupportEffect(nearbyPlayer);
                }
            }
            return true;
        }

        return false;
    }

    private void applyRandomEffect(Player player) {
        int effectIndex = rand.nextInt(4);
        switch (effectIndex) {
            case 0:
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, EFFECT_DURATION, 1));
                player.sendMessage(ChatColor.RED + "Opponent team wizard cast poison spell on you");
                break;
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, EFFECT_DURATION, 1));
                player.sendMessage(ChatColor.RED + "Opponent team wizard cast slowness spell on you");
                break;
            case 2:
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, EFFECT_DURATION, 1));
                player.sendMessage(ChatColor.RED + "Opponent team wizard cast blindness spell on you");
                break;
            default:
                player.sendMessage(ChatColor.RED + "Your spell was weak this time!");
        }
    }

    private void applyRandomSupportEffect(Player player) {
        int effectIndex = rand.nextInt(3);
        switch (effectIndex) {
            case 0:
                player.sendMessage(ChatColor.GREEN + "Your wizard cast regeneration on you!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, EFFECT_DURATION, 1));
                break;
            case 1:
                player.sendMessage(ChatColor.GREEN + "Your wizard cast absorption on you!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, EFFECT_DURATION * 3, 1));
                break;
            case 2:
                player.sendMessage(ChatColor.GREEN + "Your wizard cast speed on you!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION, 1));
                break;
            default:
                player.sendMessage(ChatColor.RED + "Your spell was weak this time!");
        }
    }

    private void removeItem(Player player, ItemStack item) {
        if(plugin.getGame().getState() != GameState.IN_GAME){
            return;
        }
        if (player.getInventory().getItemInMainHand().equals(item)) {
            player.getInventory().setItemInMainHand(null);
        } else if (player.getInventory().getItemInOffHand().equals(item)) {
            player.getInventory().setItemInOffHand(null);
        } else {
            player.getInventory().remove(item);
        }
    }
}
