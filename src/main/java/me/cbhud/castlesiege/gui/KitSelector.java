package me.cbhud.castlesiege.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitSelector implements InventoryHolder {

    private Inventory inv;

    public KitSelector() {
        inv = Bukkit.createInventory(this, 27, ChatColor.YELLOW + "Select Kit");
        init();
    }

    private void init() {
        ItemStack item;
        ItemStack item2;
        ItemStack item3;
        ItemStack item4;
        ItemStack item5;
        ItemStack item6;
        ItemStack item7;
        ItemStack item8;

        // Viking Kits
        item4 = createItem(ChatColor.RED + "Berserker", Material.IRON_AXE,
                "Great warrior with special ability of throwable axe", "50 coins");
        inv.setItem(14, item4);

        item8 = createItem(ChatColor.RED + "Bombardier", Material.TNT,
                "Destroyer of enemies' fences", "30 coins");
        inv.setItem(15, item8);

        item5 = createItem(ChatColor.RED + "Skald", Material.BOW,
                "Sharp shooter with special abilities of shooting poisonous arrows", "Free");
        inv.setItem(16, item5);

        item6 = createItem(ChatColor.RED + "Warrior", Material.IRON_SWORD,
                "Legendary warrior with special Ragnarok ability", "80 Coins");
        inv.setItem(17, item6);

        // Frankish Kits
        item = createItem(ChatColor.AQUA + "Marksman", Material.CROSSBOW,
                "Most precise shooter with special crossbow", "Free");
        inv.setItem(9, item);

        item2 = createItem(ChatColor.AQUA + "Spearman", Material.TRIDENT,
                "Armed with a spear", "20 coins");
        inv.setItem(10, item2);

        item7 = createItem(ChatColor.AQUA + "Wizard", Material.SPLASH_POTION,
                "Empower your magic with wizard kit", "45 coins");
        inv.setItem(11, item7);

        item3 = createItem(ChatColor.AQUA + "Knight", Material.SHIELD,
                "Honored soldier who served the king", "90 coins");
        inv.setItem(12, item3);
    }

    private ItemStack createItem(String name, Material mat, String description, String price) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        // Use a mutable list for lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + description);
        lore.add(ChatColor.YELLOW + "Price: " + price);
        lore.add(ChatColor.GRAY + "Left-Click to Select");
        lore.add(ChatColor.GRAY + "Right-Click to Purchase");

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
