package me.cbhud.event;

import me.cbhud.Main;
import me.cbhud.gui.Manager;
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
    Random rand = new Random();

    public RightClickEffects(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack clickedItem = event.getItem();


        if (clickedItem != null && clickedItem.getType() == Material.CLOCK && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getTeamSelector().getInventory());
        }

        if (clickedItem != null && clickedItem.getType() == Material.NETHER_STAR && plugin.getGame().getState() == GameState.LOBBY) {
            player.openInventory(plugin.getKitSelector().getInventory());
        }

        if (plugin.getGame().getState() == GameState.IN_GAME && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {

            if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.stew.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.rage.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().remove(Manager.rage);
                }
            } else if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.ragnarok.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().remove(Manager.ragnarok);
                }
            } else if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 175, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else if (mainHandItem != null && mainHandItem.getItemMeta().equals(Manager.sight.getItemMeta())) {
                    player.getInventory().removeItem(Manager.sight);
                }
            } else if (clickedItem != null && clickedItem.getType() == Material.BONE && plugin.getTeamManager().getTeam(player) == Team.Vikings) {
                plugin.getWolfManager().spawnCustomMob(player);
                if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().remove(Material.BONE);
                }
            } else if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.attack.getItemMeta())) {
                int c = rand.nextInt(4);
                for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                    if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                            plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Vikings) {
                        switch (c){
                            case 0:
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                                nearbyPlayer.sendMessage(ChatColor.RED + "Opponent team wizard cast poison spell on you");
                                break;
                            case 1:
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                                nearbyPlayer.sendMessage(ChatColor.RED + "Opponent team wizard cast slowness spell on you");
                                break;
                            case 2:
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                                nearbyPlayer.sendMessage(ChatColor.RED + "Opponent team wizard cast slowness spell on you");
                                break;
                            case 3:
                                player.sendMessage(ChatColor.RED + "Your spell was weak this time!");
                                break;
                        }
                    }
                    if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                        player.getInventory().setItemInOffHand(null);
                    } else {
                        player.getInventory().remove(Manager.attack);
                    }
                }
            }
            else if (clickedItem != null && clickedItem.getItemMeta().equals(Manager.support.getItemMeta())) {
                int c = rand.nextInt(4);
                for (Player nearbyPlayer : player.getWorld().getPlayers()) {
                    if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 10 &&
                            plugin.getTeamManager().getTeam(nearbyPlayer) == Team.Franks) {
                        switch (c){
                            case 0:
                                nearbyPlayer.sendMessage(ChatColor.GREEN + "Your wizard cast regeneration on you!");
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                                player.sendMessage(ChatColor.GREEN + "You have casted team with regeneration!");
                                break;
                            case 1:
                                player.sendMessage(ChatColor.RED + "You have casted team with absorption!");
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 300, 1));
                                nearbyPlayer.sendMessage(ChatColor.GREEN + "Your wizard cast absorption on you!");                                break;
                            case 2:
                                player.sendMessage(ChatColor.RED + "You have casted team with speed!");
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                                nearbyPlayer.sendMessage(ChatColor.GREEN + "Your wizard cast speed on you!");                                break;
                            case 3:
                                player.sendMessage(ChatColor.RED + "Your spell was weak this time!");
                                break;
                        }
                    }
                    if (event.getHand().equals(org.bukkit.inventory.EquipmentSlot.OFF_HAND)) {
                        player.getInventory().setItemInOffHand(null);
                    } else {
                        player.getInventory().remove(Manager.support);
                    }
                }
            }
        }
    }
}
