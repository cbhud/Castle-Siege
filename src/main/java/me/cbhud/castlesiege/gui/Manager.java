package me.cbhud.castlesiege.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Manager implements InventoryHolder {

    private Inventory inv;

    // Items
    public static ItemStack stew;

    public static ItemStack bone2;

    public static ItemStack clock;
    public static ItemStack star;
    public static ItemStack axe;
    public static ItemStack combataxe;
    public static ItemStack rage;
    public static ItemStack ragnarok;
    public static ItemStack sight;
    public static ItemStack harm;
    public static ItemStack sword;

    public static ItemStack attack;
    public static ItemStack support;

    // GUIs
    private final Inventory teamSelector;
    private final Inventory kitSelector;

    public Manager() {
        this.inv = Bukkit.createInventory(this, 9, ChatColor.YELLOW + "Select Team");
        this.teamSelector = new TeamSelector().getInventory();
        this.kitSelector = new KitSelector().getInventory();

        initItems();
    }

    private void initItems() {
        createAxe();
        createHarm();
        createStew();
        createCombatAxe();
        createRage();
        createRagnarok();
        createSight();
        createClock();
        createStar();
        createSword();
        createAttackWand();
        createSupportWand();
        createBone();
    }

    private void createAxe() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cThrowable Axe");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right-click holding this axe");
        lore.add("§7to throw it");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        // Create an AttributeModifier to reduce damage by 2 (from 9 to 7)

        item.setItemMeta(meta);
        axe = item;
    }

    private void createBone() {
        ItemStack item = new ItemStack(Material.IRON_NUGGET, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aSpawn your wolf");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right-click holding this bone");
        lore.add("§7to spawn your wolf");
        meta.setLore(lore);
        item.setItemMeta(meta);
        bone2 = item;
    }

    private void createStew() {
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

    private void createCombatAxe() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBerserker's Melee");
        List<String> lore = new ArrayList<>();
        lore.add("§7Berserker's melee axe");
        lore.add("§7Used in viking fights");
        meta.setLore(lore);
        item.setItemMeta(meta);
        combataxe = item;
    }

    private void createRage() {
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

    private void createRagnarok() {
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

    private void createSight() {
        ItemStack item = new ItemStack(Material.FERMENTED_SPIDER_EYE, 1);
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

    private void createClock() {
        ItemStack item = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eSelect Team");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right click to select team");
        meta.setLore(lore);
        item.setItemMeta(meta);
        clock = item;
    }
    private void createStar() {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eSelect Kit");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right click to select the kit");
        meta.setLore(lore);
        item.setItemMeta(meta);
        star = item;
    }

    private void createHarm() {
        ItemStack item = new ItemStack(Material.TIPPED_ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eDamage Arrow");
        List<String> lore = new ArrayList<>();
        lore.add("§7This arrow gives special Skald effects");
        lore.add("§7Special vision ability and jump boost");
        lore.add("§7Right-click to use");
        meta.setLore(lore);
        ((PotionMeta) meta).setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
        item.setItemMeta(meta);
        harm = item;
    }

    private void createSword() {
        ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§dMystic Sword");
        List<String> lore = new ArrayList<>();
        lore.add("§7There is a 0.06% chance");
        lore.add("§7to poison someone when you hit him");
        lore.add("§7with this sword");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY,1, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        sword = item;
    }
    private void createAttackWand() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cAttack Wand");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right click to cast mysterious spell");
        lore.add("§7on your opponents!");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY,1, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        attack = item;
    }

    private void createSupportWand() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aSupport Wand");
        List<String> lore = new ArrayList<>();
        lore.add("§7Right click to cast mysterious spell");
        lore.add("§7to help your teammates!");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY,1, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        support = item;
    }

    public static ItemStack createVikingTeamItem() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Vikings");
        meta.setLore(Collections.singletonList(ChatColor.YELLOW + "Assassin the king and conquer the castle"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createFranksTeamItem() {
        ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Franks");
        meta.setLore(Collections.singletonList(ChatColor.YELLOW + "Defend the castle and king from Vikings"));
        item.setItemMeta(meta);
        return item;
    }

    public Inventory getTeamSelector() {
        return teamSelector;
    }

    public Inventory getKitSelector() {
        return kitSelector;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
