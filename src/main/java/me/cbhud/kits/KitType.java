package me.cbhud.kits;

import me.cbhud.gui.Manager;
import me.cbhud.team.Team;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

import static me.cbhud.gui.Manager.*;

public enum KitType {



    MARKSMAN("Marksman", Arrays.asList(
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.CROSSBOW) {{
                ItemMeta meta = getItemMeta();
                if (meta != null) {
                    meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true); // Add the enchantment
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Hide the enchantment
                    setItemMeta(meta);
                }
            }},
            new ItemStack(Material.AIR),
            new ItemStack(Material.ARROW, 32),
            Manager.stew,
            Manager.stew,
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.IRON_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.Franks),
    SPEARMAN("Spearman", Arrays.asList(
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.TRIDENT) {{
                ItemMeta meta = getItemMeta();
                if (meta != null) {
                    meta.addEnchant(Enchantment.LOYALTY, 2, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    setItemMeta(meta);
                }
            }},
            Manager.stew,
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.IRON_HELMET),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_BOOTS)
            },Team.Franks),
    WIZARD("Wizard", Arrays.asList(
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.AIR),
            Manager.attack,
            support,
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    createDyedArmor(Material.LEATHER_HELMET, Color.BLUE),
                    createDyedArmor(Material.LEATHER_CHESTPLATE, Color.BLUE),
                    createDyedArmor(Material.LEATHER_LEGGINGS, Color.BLUE),
                    createDyedArmor(Material.LEATHER_BOOTS, Color.BLUE)
            },Team.Franks),
    KNIGHT("Knight", Arrays.asList(
            new ItemStack(Material.IRON_SWORD),
            Manager.stew,
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.IRON_HELMET),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_BOOTS)
            },Team.Franks),

    BERSERKER("Berserker", Arrays.asList(
            new ItemStack(combataxe),
            new ItemStack(axe),
            Manager.stew,
            Manager.stew,
            Manager.rage
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.CHAINMAIL_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.Vikings),
    SKALD("Skald", Arrays.asList(
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.BOW),
            new ItemStack(Material.AIR),
            new ItemStack(Material.ARROW, 32),
            Manager.stew,
            Manager.stew,
            sight
    ),
            true,
            new ItemStack[]{
                    createDyedArmor(Material.LEATHER_HELMET, Color.RED),
                    createDyedArmor(Material.LEATHER_CHESTPLATE, Color.RED),
                    createDyedArmor(Material.LEATHER_LEGGINGS, Color.RED),
                    createDyedArmor(Material.LEATHER_BOOTS, Color.RED)
            },Team.Vikings),
    BEASTMASTER("Beastmaster", Arrays.asList(
            new ItemStack(Material.STONE_SWORD),
            bone2,
            Manager.stew,
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.LEATHER_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.Vikings),
    WARRIOR("Warrior", Arrays.asList(
            new ItemStack(Material.IRON_SWORD),
            Manager.stew,
            Manager.stew,
            ragnarok
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.IRON_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.Vikings);


    private final String displayName;
    private final List<ItemStack> items;
    private final boolean hasArmor;
    private final ItemStack[] armorItems;
    private final Team team;

    KitType(String displayName, List<ItemStack> items, boolean hasArmor, ItemStack[] armorItems, Team team) {
        this.displayName = displayName;
        this.items = items;
        this.hasArmor = hasArmor;
        this.armorItems = armorItems;
        this.team = team;
    }

    public static KitType getDefaultKit(Team team) {
        switch (team) {
            case Vikings:
                return SKALD;
            case Franks:
                return MARKSMAN;
            default:
                return null;
        }
    }


    public String getDisplayName() {
        return displayName;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public static KitType getByName(String name) {
        for (KitType kitType : values()) {
            if (kitType.name().equalsIgnoreCase(name)) {
                return kitType;
            }
        }
        return null;
    }

    private static ItemStack createDyedArmor(Material material, Color color) {
        ItemStack armorItem = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) armorItem.getItemMeta();
        if (meta != null) {
            meta.setColor(color);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            armorItem.setItemMeta(meta);
        }
        return armorItem;
    }


    public boolean hasArmor() {
        return hasArmor;
    }

    public Team getTeam() {
        return team;
    }

    public ItemStack[] getArmorItems() {
        return armorItems;
    }
}