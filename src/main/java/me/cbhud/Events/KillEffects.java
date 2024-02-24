package me.cbhud.Events;

import me.cbhud.Main;
import me.cbhud.items.Manager;
import me.cbhud.kits.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillEffects implements Listener {

    private final Main plugin;

    public KillEffects(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            // Check if the killer has a specific kit
            KitType killerKit = plugin.getPlayerKitManager().getSelectedKit(killer);

            if (killerKit != null) {
                applyKillEffects(killer, killerKit);
            }
        }
    }

    private void applyKillEffects(Player player, KitType kitType) {
        switch (kitType) {
            case MARKSMAN:
                player.getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW));
                break;
            case SPEARMAN:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                break;
            case KNIGHT:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 100, 1)));
                break;
            case BERSERKER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 150, 1));
                break;
            case SKALD:
                player.getInventory().addItem(Manager.harm);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                break;
            case WARRIOR:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 150, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                break;
            // Add cases for other kits as needed
        }
    }
}
