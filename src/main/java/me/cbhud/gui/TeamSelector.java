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

public class TeamSelector implements InventoryHolder {

    private Inventory inv;

    public TeamSelector(){
        inv = Bukkit.createInventory(this, 9 ,ChatColor.YELLOW + "Select Team");
        init();
    }

    private void init(){

        ItemStack item;
        ItemStack item2;
        item = createItem(ChatColor.RED+"Vikings", Material.RED_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.YELLOW + "Assasin the king and conquer the castle"));
        inv.setItem(5, item);
        item2 = createItem(ChatColor.BLUE+"Franks", Material.CYAN_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.YELLOW + "Defend the castle and king from Vikings"));
        inv.setItem(3, item2);
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
