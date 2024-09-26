package me.cbhud.castlesiege.kits;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class KitManager {
    private final CastleSiege plugin;
    private List<KitData> kits; // Holds all the kits

    public KitManager(CastleSiege plugin) {
        this.plugin = plugin;
        createKitsFileIfNotExists(); // Ensure kits.yml exists
        kits = new ArrayList<>();
        kits = loadKits();
    }

    // Load kits from kits.yml
    public List<KitData> loadKits() {
        if (!kits.isEmpty()) return kits; // Avoid reloading if kits are already loaded

        File kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(kitsFile);

        for (String kitName : config.getConfigurationSection("kits").getKeys(false)) {
            String path = "kits." + kitName;

            List<String> kitItems = config.getStringList(path + ".kitItems");
            int price = config.getInt(path + ".price");
            Team team = Team.valueOf(config.getString(path + ".team"));

            List<ItemStack> items = new ArrayList<>();

            for (String itemString : kitItems) {
                String[] itemData = itemString.split(":");
                Material material = Material.getMaterial(itemData[0].toUpperCase());

                if (material == null) {
                    // Extract the custom item name correctly
                    String customItemName = itemData.length > 1 ? itemData[1] : itemData[0]; // Use itemData[1] as the custom item name
                    ItemStack customItem = createCustomItem(customItemName);

                    // Check if the custom item is not null before adding to the list
                    if (customItem != null && customItem.getType() != Material.AIR) {
                        items.add(customItem);
                    } else {
                        // Optionally handle the case where the custom item could not be created
                        System.out.println("Failed to create custom item: " + customItemName);
                    }
                } else {
                    int amount = itemData.length > 1 ? Integer.parseInt(itemData[1]) : 1;
                    items.add(new ItemStack(material, amount));
                }
            }

            kits.add(new KitData(kitName, items, price, team));
        }

        return kits;
    }

    private ItemStack createCustomItem(String customItemName) {
        // Define logic to create custom items, e.g., "Stew", "Spear", etc.
        switch (customItemName.toLowerCase()) {
            case "stew":
                return new ItemStack(Manager.stew); // Assuming Manager.stew exists
            case "spear":
            return new ItemStack(Manager.spear); // Assuming Manager.stew exists
            case "axe":
            return new ItemStack(Manager.axe); // Assuming Manager.stew exists

            case "rage":
            return new ItemStack(Manager.rage); // Assuming Manager.stew exists

            case "ragnarok":
            return new ItemStack(Manager.ragnarok); // Assuming Manager.stew exists

            case "sight":
            return new ItemStack(Manager.sight); // Assuming Manager.stew exists

            case "sword":
            return new ItemStack(Manager.sword); // Assuming Manager.stew exists

            case "attack":
            return new ItemStack(Manager.attack); // Assuming Manager.stew exists

            case "support":
            return new ItemStack(Manager.support); // Assuming Manager.stew exists

            default:
                return new ItemStack(Material.STONE); // Default item if not found
        }
    }

    private void createKitsFileIfNotExists() {
        File kitsFile = new File(plugin.getDataFolder(), "kits.yml");

        if (!kitsFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try (InputStream in = plugin.getResource("kits.yml")) {
                if (in != null) {
                    Files.copy(in, kitsFile.toPath());
                    plugin.getLogger().info("kits.yml created successfully.");
                } else {
                    plugin.getLogger().severe("kits.yml not found in resources!");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create kits.yml file.");
                e.printStackTrace();
            }
        }
    }

    // Get a specific kit by name
    public KitData getKitByName(String kitName) {
        for (KitData kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }
        return null;
    }

    // Inner class to hold kit data
    public static class KitData {
        private final String name;
        private final List<ItemStack> items;
        private final int price;
        private final Team team;

        public KitData(String name, List<ItemStack> items, int price, Team team) {
            this.name = name;
            this.items = items;
            this.price = price;
            this.team = team;
        }

        public String getName() {
            return name;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        public int getPrice() {
            return price;
        }

        public Team getTeam() {
            return team;
        }
    }
}

