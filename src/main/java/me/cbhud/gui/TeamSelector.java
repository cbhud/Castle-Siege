package me.cbhud.gui;

import me.cbhud.ConfigManager;
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
        inv = Bukkit.createInventory(this, 9 ,"Select Team");
        init();
    }

    private void init(){

        ItemStack item;
        ItemStack item2;
        item = createItem(ChatColor.DARK_RED+"VIKINGS", Material.RED_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.RED + "VIKINGS (Attackers) have to assasin the king and conquer the castle"));
        inv.setItem(5, item);
        item2 = createItem(ChatColor.BLUE+"FRANKS", Material.CYAN_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.AQUA + "FRANKS (Defenders) have to defend the castle and king from VIKINGS"));
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
