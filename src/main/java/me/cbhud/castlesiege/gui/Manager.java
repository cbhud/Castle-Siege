package me.cbhud.castlesiege.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Manager {

    public static ItemStack axe;
    public static ItemStack combataxe;
    public static ItemStack stew;
    public static ItemStack rage;
    public static ItemStack ragnarok;
    public static ItemStack sight;
    public static ItemStack harm;
    public static ItemStack sword;
    public static ItemStack attack;
    public static ItemStack support;

    public Manager() {
        initItems();
    }

    private void initItems() {
        createAxe();
        createCombatAxe();
        createStew();
        createRage();
        createRagnarok();
        createSight();
        createHarm();
        createSword();
        createAttackWand();
        createSupportWand();
    }

    private void createAxe() {
        axe = ItemBuilder.from(Material.IRON_AXE)
                .name(Component.text("§cThrowable Axe"))
                .lore(Component.text("§7Right-click to throw"))
                .build();
    }

    private void createCombatAxe() {
        combataxe = ItemBuilder.from(Material.IRON_AXE)
                .name(Component.text("§eCombat Axe"))
                .lore(Component.text("§7Berserker's combat axe"))
                .build();
    }

    private void createStew() {
        stew = ItemBuilder.from(Material.MUSHROOM_STEW)
                .name(Component.text("§dMagic Stew"))
                .lore(Component.text("§7Regenerates for 5 seconds"))
                .build();
    }

    private void createRage() {
        rage = ItemBuilder.from(Material.RED_DYE)
                .name(Component.text("§cBerserker's RAGE"))
                .lore(Component.text("§7Gives special berserker effects"))
                .build();
    }

    private void createRagnarok() {
        ragnarok = ItemBuilder.from(Material.MAGMA_CREAM)
                .name(Component.text("§4Ragnarok"))
                .lore(Component.text("§7Strength and resistance but slowness"))
                .build();
    }

    private void createSight() {
        sight = ItemBuilder.from(Material.FERMENTED_SPIDER_EYE)
                .name(Component.text("§eSkald's Sight"))
                .lore(Component.text("§7Gives special Skald effects"))
                .build();
    }

    private void createHarm() {
        harm = ItemBuilder.from(Material.TIPPED_ARROW)
                .name(Component.text("§eDamage Arrow"))
                .lore(Component.text("§7Special effects when used"))
                .build();
    }

    private void createSword() {
        sword = ItemBuilder.from(Material.STONE_SWORD)
                .name(Component.text("§dMystic Sword"))
                .lore(Component.text("§70.06% chance to poison on hit"))
                .build();
    }

    private void createAttackWand() {
        attack = ItemBuilder.from(Material.BLAZE_ROD)
                .name(Component.text("§cAttack Wand"))
                .lore(Component.text("§7Cast mysterious spells on opponents!"))
                .build();
    }

    private void createSupportWand() {
        support = ItemBuilder.from(Material.STICK)
                .name(Component.text("§aSupport Wand"))
                .lore(Component.text("§7Cast mysterious spells to help teammates!"))
                .build();
    }



}
