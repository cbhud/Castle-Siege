package me.cbhud;

import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MobManager implements Listener {

    private final Main plugin;
    private final TeamManager teamManager;
    private Zombie kingZombie;
    private final ConfigManager configManager;

    public MobManager(Main plugin, TeamManager teamManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void spawnCustomMob(Location location, ConfigurationSection mobConfig) {
        Location spawnLocation = getLocationFromConfig(mobConfig);

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

        double maxHealth = configManager.getKingHealth();
        kingZombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        kingZombie.setHealth(maxHealth);

        // Add a golden helmet to the zombie
        ItemStack goldenHelmet = new ItemStack(Material.GOLDEN_HELMET);
        kingZombie.getEquipment().setHelmet(goldenHelmet);
    }

    private static Location getLocationFromConfig(ConfigurationSection config) {
        double x = config.getDouble("x");
        double y = config.getDouble("y");
        double z = config.getDouble("z");
        float yaw = (float) config.getDouble("yaw");
        float pitch = (float) config.getDouble("pitch");

        String worldName = config.getString("world");
        if (worldName == null) {
            return null;
        }

        // Create and return the Location object
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
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
        Bukkit.getLogger().info("King Zombie not found.");
        return null;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Zombie && event.getEntity().getCustomName().contains("King")) {
            Zombie zombie = (Zombie) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                Team damagerTeam = teamManager.getTeam(damager);

                if (damagerTeam == Team.Franks || damagerTeam == null) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();

                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    Team shooterTeam = teamManager.getTeam(shooter);

                    if (shooterTeam == Team.Franks|| shooterTeam == null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}