package me.cbhud.kits;

import me.cbhud.items.Manager;
import me.cbhud.team.Team;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static me.cbhud.items.Manager.*;

public enum KitType {



    MARKSMAN("MARKSMAN", Arrays.asList(
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
            Manager.stew
    ),
            true,  // <-- Add this parameter to indicate the kit has armor
            new ItemStack[]{
                    new ItemStack(Material.LEATHER_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.FRANKS),
    SPEARMAN("SPEARMAN", Arrays.asList(
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.TRIDENT) {{
                ItemMeta meta = getItemMeta();
                if (meta != null) {
                    meta.addEnchant(Enchantment.LOYALTY, 2, true); // Add the enchantment
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Hide the enchantment
                    setItemMeta(meta);
                }
            }},
            Manager.stew
    ),
            true,  // <-- Add this parameter to indicate the kit has armor
            new ItemStack[]{
                    new ItemStack(Material.CHAINMAIL_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.FRANKS),
    KNIGHT("KNIGHT", Arrays.asList(
            new ItemStack(Material.IRON_SWORD),
            Manager.stew
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.IRON_HELMET),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_BOOTS)
            },Team.FRANKS),

    //VIKINGS KITS ARE BELOW
    BERSERKER("BERSERKER", Arrays.asList(
            new ItemStack(combataxe),
            new ItemStack(axe),
            Manager.stew,
            Manager.rage
    ),
            true,  // <-- Add this parameter to indicate the kit has armor
            new ItemStack[]{
                    new ItemStack(Material.CHAINMAIL_HELMET),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.VIKINGS),
    SKALD("SKALD", Arrays.asList(
            new ItemStack(Material.BOW),
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.AIR),
            new ItemStack(Material.ARROW, 32),
            Manager.stew,
            sight
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.LEATHER_HELMET),
                    new ItemStack(Material.LEATHER_CHESTPLATE),
                    new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_BOOTS)
            },Team.VIKINGS),
    WARRIOR("Warrior", Arrays.asList(
            new ItemStack(Material.IRON_SWORD),
            Manager.stew,
            ragnarok
    ),
            true,
            new ItemStack[]{
                    new ItemStack(Material.CHAINMAIL_HELMET),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_BOOTS)
            },Team.VIKINGS);

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
            case VIKINGS:
                return SKALD;
            case FRANKS:
                return MARKSMAN;
            default:
                return null; // Handle other cases or return a general default kit
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
        return null;  // Return null if no matching KitType is found
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