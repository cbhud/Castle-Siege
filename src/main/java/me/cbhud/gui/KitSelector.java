package me.cbhud.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class KitSelector implements InventoryHolder {

    private Inventory inv;

    public KitSelector(){
        inv = Bukkit.createInventory(this, 9 ,"Select Kit");
        init();
    }

    private void init(){

        ItemStack item;
        ItemStack item2;
        ItemStack item3;
        ItemStack item4;
        ItemStack item5;
        ItemStack item6;

        //viking
        item4 = createItem(ChatColor.RED + "Berserker", Material.IRON_AXE, Collections.singletonList(ChatColor.GRAY + "Great warrior with special ability of throwable axe"));
        inv.setItem(5, item4);
        item5 = createItem(ChatColor.RED + "Skald", Material.BOW, Collections.singletonList(ChatColor.GRAY + "Sharp shooters with special abilities of shooting poisonous arrows"));
        inv.setItem(6, item5);
        item6 = createItem(ChatColor.RED + "Warrior", Material.IRON_SWORD, Collections.singletonList(ChatColor.GRAY + "Legendary warrior with special Ragnarok ability"));
        inv.setItem(7, item6);

        //franks
        item = createItem(ChatColor.AQUA + "Marksman", Material.CROSSBOW, Collections.singletonList(ChatColor.GRAY + "Frankish most precise shooters with special cross-bow"));
        inv.setItem(1, item);
        item2 = createItem(ChatColor.AQUA + "Spearman", Material.TRIDENT, Collections.singletonList(ChatColor.GRAY + "Frankish soldier armed with a spear"));
        inv.setItem(2, item2);
        item3 = createItem(ChatColor.AQUA + "Knight", Material.BLACK_BANNER, Collections.singletonList(ChatColor.GRAY + "Honored soldier who served the king"));
        inv.setItem(3, item3);
    }

    private ItemStack createItem(String name, Material mat, List<String> lore){

        ItemStack item = new ItemStack(mat,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
