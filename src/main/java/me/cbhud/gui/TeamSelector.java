package me.cbhud.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class TeamSelector implements InventoryHolder {

    private final Inventory inv;

    public TeamSelector() {
        inv = Bukkit.createInventory(this, 9, ChatColor.YELLOW + "Select Team");
        init();
    }

    private void init() {
        // Use items from the Manager class to create GUI items
        ItemStack item = Manager.createVikingTeamItem();
        ItemStack item2 = Manager.createFranksTeamItem();

        inv.setItem(5, item);
        inv.setItem(3, item2);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
