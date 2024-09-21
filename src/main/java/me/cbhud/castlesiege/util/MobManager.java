package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.inventory.ItemStack;

public class MobManager implements Listener {

    private final CastleSiege plugin;
    private Zombie kingZombie;
    private final double TNT_DAMAGE;

    public MobManager(CastleSiege plugin) {
        this.plugin = plugin;
        TNT_DAMAGE = plugin.getConfigManager().getConfig().getDouble("tntDamage", 4);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void spawnCustomMob(ConfigurationSection mobConfig) {
        Location spawnLocation = plugin.getLocationManager().getLocationFromConfig(mobConfig);

        if (spawnLocation == null) {
            // Handle case where spawnLocation is null
            return;
        }

        kingZombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        kingZombie.setCustomNameVisible(true);
        kingZombie.setCustomName("§6§lKing Charles");

        kingZombie.setAI(false); // Disable AI movement
        kingZombie.setSilent(true); // Make the zombie silent
        kingZombie.setCanPickupItems(false); // Zombie cannot pick up items
        kingZombie.setRemoveWhenFarAway(false); // Zombie does not despawn when far away
        kingZombie.setAdult();

        double maxHealth = plugin.getConfigManager().getKingHealth();
        kingZombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        kingZombie.setHealth(maxHealth);

        // Add a golden helmet to the zombie
        kingZombie.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
    }


    public double getZombieHealth(Zombie zombie) {
        if (isKingZombie(zombie)) {
            return zombie.getHealth();
        }
        return 0.0;
    }

    private boolean isKingZombie(Zombie zombie) {
        return zombie != null && zombie == kingZombie;
    }

    public Zombie getKingZombie() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null && entity.getCustomName().contains("King Charles")) {
                    Zombie kingZombie = (Zombie) entity;
                    double health = kingZombie.getHealth();
                    return kingZombie;
                }
            }
        }
        return null;
    }

    public void removeCustomZombie() {
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Zombie && entity.getCustomName() != null && entity.getCustomName().equals("§6§lKing Charles") ) {
                    entity.remove();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof TNTPrimed) {

            if (event.getEntity() instanceof Zombie) {
                Zombie zombie = (Zombie) event.getEntity();
                if (zombie.getCustomName() != null && zombie.getCustomName().contains("King")) {
                    event.setCancelled(true);
                }
            }

            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (plugin.getTeamManager().getTeam(player) != Team.Attackers) {
                    event.setDamage(TNT_DAMAGE);
                }
            }


        }

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                Team damagerTeam = plugin.getTeamManager().getTeam(damager);

                if (damagerTeam == Team.Defenders || damagerTeam == null) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();

                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    Team shooterTeam = plugin.getTeamManager().getTeam(shooter);

                    if (shooterTeam == Team.Defenders || shooterTeam == null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }