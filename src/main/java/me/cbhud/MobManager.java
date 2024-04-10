package me.cbhud;

import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MobManager implements Listener {

    private final Main plugin;
    private final TeamManager teamManager;
    private Zombie kingZombie;
    private Wolf wolf;

    private final ConfigManager configManager;

    public MobManager(Main plugin, TeamManager teamManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.configManager = configManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    public void spawnWolf(Player player) {
        Location spawnLocation = player.getLocation();

        wolf = (Wolf) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WOLF);
        wolf.setAdult();
        wolf.setCustomNameVisible(true);
        wolf.setCustomName(ChatColor.YELLOW + player.getName() + "'s Wolf");
        wolf.setOwner(player);
        wolf.setCanPickupItems(false); // Zombie cannot pick up items
        wolf.setAngry(true);
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
        return null;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                Team damagerTeam = teamManager.getTeam(damager);

                if (damagerTeam == Team.Vikings || damagerTeam == null) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();

                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    Team shooterTeam = teamManager.getTeam(shooter);

                    if (shooterTeam == Team.Vikings|| shooterTeam == null) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getEntity() instanceof Zombie && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("King")) {
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


    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        final Entity clickedEntity = event.getRightClicked();

        if (clickedEntity.getType() == EntityType.WOLF) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getEntity();

            // Check if the wolf already has a target or if it's targeting a player
            if (wolf.getTarget() == null || wolf.getTarget() instanceof Player) {
                // Iterate over nearby players
                for (Player nearbyPlayer : wolf.getWorld().getPlayers()) {
                    if (nearbyPlayer.getLocation().distance(wolf.getLocation()) <= 15) {
                        // Check if the nearby player belongs to the "Franks" team
                        if (teamManager.getTeam(nearbyPlayer) == Team.Franks) {
                            wolf.setTarget(nearbyPlayer);
                            break; // Stop searching once a player from the "Franks" team is found
                        }
                    }
                }
            }
        }
    }




}