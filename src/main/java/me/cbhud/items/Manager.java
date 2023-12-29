package me.cbhud.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Manager {
    public static ItemStack blood;
    public static ItemStack stew;

    public static ItemStack axe;

    public static void init() {


        createBlood();
        createAxe();
        createStew();

    }


    private static void createAxe(){
        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBerserker's Axe");
        List<String> lore = new ArrayList<>();
        lore.add("§7When you kill someone with this axe");
        lore.add("§7he will drop viking blood");
        lore.add("You can make viking stew with it");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        axe = item;
    }


    private static void createBlood(){
        ItemStack item = new ItemStack(Material.RED_DYE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cViking's Blood");
        List<String> lore = new ArrayList<>();
        lore.add("§7You can craft viking stew with this");
        lore.add("You can make viking stew with it");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        blood = item;
    }

    private static void createStew(){
        ItemStack item = new ItemStack(Material.LEGACY_MUSHROOM_SOUP, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Viking Stew");
        List<String> lore = new ArrayList<>();
        lore.add("§8Viking stew will regenerate you");
        lore.add("§7Gives you Regeneration for 5 seconds");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        stew = item;

        ShapelessRecipe stewRecipe = new ShapelessRecipe(NamespacedKey.minecraft("vikings_stew"), item);
        stewRecipe.addIngredient(Material.BOWL);
        if(Manager.blood != null) {
            stewRecipe.addIngredient(Manager.blood.getType());
        } else {
            Bukkit.getLogger().severe("Error: 'blood' item is null.");
            return;}

        Bukkit.getServer().addRecipe(stewRecipe);

    }



}
