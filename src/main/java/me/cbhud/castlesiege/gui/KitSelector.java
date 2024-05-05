package me.cbhud.castlesiege.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;
import java.util.List;

public class KitSelector implements InventoryHolder {

    private Inventory inv;

    public KitSelector(){
        inv = Bukkit.createInventory(this, 27 ,ChatColor.YELLOW + "Select Kit");
        init();
    }

    private void init(){

        ItemStack item;
        ItemStack item2;
        ItemStack item3;
        ItemStack item4;
        ItemStack item5;
        ItemStack item6;
        ItemStack item7;
        ItemStack item8;

        //viking
        item4 = createItem(ChatColor.RED + "Berserker", Material.IRON_AXE, Collections.singletonList(ChatColor.GRAY + "Great warrior with special ability of throwable axe"));
        inv.setItem(14, item4);
        item8 = createItem(ChatColor.RED + "Bombardier", Material.TNT, Collections.singletonList(ChatColor.GRAY + "Destroyer of enemies fences"));
        inv.setItem(15, item8);
        item5 = createItem(ChatColor.RED + "Skald", Material.BOW, Collections.singletonList(ChatColor.GRAY + "Sharp shooter with special abilities of shooting poisonous arrows"));
        inv.setItem(16, item5);
        item6 = createItem(ChatColor.RED + "Warrior", Material.IRON_SWORD, Collections.singletonList(ChatColor.GRAY + "Legendary warrior with special Ragnarok ability"));
        inv.setItem(17, item6);

        //franks
        item = createItem(ChatColor.AQUA + "Marksman", Material.CROSSBOW, Collections.singletonList(ChatColor.GRAY + "Most precise shooter with special crossbow"));
        inv.setItem(9, item);
        item2 = createItem(ChatColor.AQUA + "Spearman", Material.TRIDENT, Collections.singletonList(ChatColor.GRAY + "Armed with a spear"));
        inv.setItem(10, item2);
        item7 = createItem(ChatColor.AQUA + "Wizard", Material.SPLASH_POTION, Collections.singletonList(ChatColor.GRAY + "Empower your magic with wizard kit"));
        inv.setItem(11, item7);
        item3 = createItem(ChatColor.AQUA + "Knight", Material.SHIELD, Collections.singletonList(ChatColor.GRAY + "Honored soldier who served the king"));
        inv.setItem(12, item3);
    }

    private ItemStack createItem(String name, Material mat, List<String> lore){

        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
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
