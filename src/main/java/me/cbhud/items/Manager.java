package me.cbhud.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;

public class Manager {
    public static ItemStack stew;
    public static ItemStack axe;
    public static ItemStack combataxe;
    public static ItemStack rage;
    public static ItemStack ragnarok;
    public static ItemStack sight;
    public static ItemStack harm;
    public static void init() {

        createAxe();
        createHarm();
        createStew();
        createCombatAxe();
        createRage();
        createRagnarok();
        createSight();
    }

    private static void createAxe(){
        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cThrowable Axe");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right-click holding this axe");
        lore.add("§7to throw it");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        axe = item;
    }

    private static void createStew(){
        ItemStack item = new ItemStack(Material.LEGACY_MUSHROOM_SOUP, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§dMagic Stew");
        List<String> lore = new ArrayList<>();
        lore.add("§7Stew will regenerate you");
        lore.add("§7for 5 seconds");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        stew = item;
    }

    private static void createCombatAxe(){
        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBerserker's Melee");
        List<String> lore = new ArrayList<>();
        lore.add("§7Berserker's melee axe");
        lore.add("§7Used in viking fights");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        combataxe = item;
    }

    private static void createRage(){
        ItemStack item = new ItemStack(Material.RED_DYE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBerserker's RAGE");
        List<String> lore = new ArrayList<>();
        lore.add("§7This will give you special berserker effects");
        lore.add("§7Right-click to use");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        rage = item;
    }

    private static void createRagnarok(){
        ItemStack item = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4Ragnarok");
        List<String> lore = new ArrayList<>();
        lore.add("§7This will give you special Ragnarok effects");
        lore.add("§7Strength n resistance but slowness aswell");
        lore.add("§7Right-click to use");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        ragnarok = item;
    }

    private static void createSight(){
        ItemStack item = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eSkald's Sight");
        List<String> lore = new ArrayList<>();
        lore.add("§7This will give you special Skald effects");
        lore.add("§7Special vision ability and jump boost");
        lore.add("§7Right-click to use");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        sight = item;
    }

    private static void createHarm() {
        ItemStack item = new ItemStack(Material.TIPPED_ARROW, 1);
        ItemMeta meta = item.getItemMeta();

        // Use the same meta instance for both ItemMeta and PotionMeta
        meta.setDisplayName("§eDamage Arrow");
        List<String> lore = new ArrayList<>();
        lore.add("§7This arrow gives special Skald effects");
        lore.add("§7Special vision ability and jump boost");
        lore.add("§7Right-click to use");
        meta.setLore(lore);

        // Set the base potion data with INSTANT_DAMAGE
        ((PotionMeta) meta).setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));

        item.setItemMeta(meta);
        harm = item;
    }
}
