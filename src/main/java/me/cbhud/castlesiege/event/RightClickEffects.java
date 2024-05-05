package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.kits.KitType;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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

        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR && plugin.getTeamManager().getTeam(player) == Team.Vikings && plugin.getGame().getState() == GameState.IN_GAME && player.getInventory().getItemInMainHand().isSimilar(Manager.axe) || event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK && plugin.getTeamManager().getTeam(player) == Team.Vikings && plugin.getGame().getState() == GameState.IN_GAME && player.getInventory().getItemInMainHand().isSimilar(Manager.axe)) {
            try {
                Item axe = player.getWorld().dropItem(player.getEyeLocation(), player.getInventory().getItemInMainHand());
                axe.setVelocity(player.getEyeLocation().getDirection().multiply(1.75));
                player.getInventory().getItemInMainHand().setAmount(0);

                new BukkitRunnable() {
                    public void run() {
                        for (Entity ent : axe.getNearbyEntities(0.4, 0.4, 0.4)) {
                            if (ent instanceof LivingEntity) {
                                LivingEntity target = (LivingEntity) ent;
                                if (ent == player) {
                                    continue;
                                }
                                target.damage(1.5);
                                axe.setVelocity(new Vector(0, 0, 0));
                                this.cancel();
                                axe.remove();
                            }
                        }
                        if (axe.isOnGround()) {
                            axe.setVelocity(new Vector(0, 0, 0));
                            this.cancel();
                            axe.remove();
                        }
                    }
                }.runTaskTimer(this.plugin, 0L, 1L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION, 1));
            return true;
        }

        if (item.getItemMeta().equals(Manager.attack.getItemMeta())) {
            for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                        plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Vikings) {
                    applyRandomEffect(nearbyPlayer);
                    player.sendMessage(ChatColor.RED + "You cast spell on your opponents");
                }
            }
            return true;
        }

        if (item.getItemMeta().equals(Manager.support.getItemMeta())) {
            for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                        plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Franks) {
                    applyRandomSupportEffect(nearbyPlayer);
                    player.sendMessage(ChatColor.GREEN + "You cast spell on your teammates helping them");
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
                break;
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