package me.cbhud.castlesiege.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.kits.KitManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitSelector {

    private final Gui gui;
    private final CastleSiege plugin;

    public KitSelector(CastleSiege plugin) {
        this.plugin = plugin;
        gui = Gui.gui()
                .title(Component.text("§eSelect Kit"))
                .rows(3)
                .create();

        init();
    }

    private void init() {
        KitManager kitManager = plugin.getKitManager();

        GuiItem berserkerItem = ItemBuilder.from(Material.IRON_AXE)
                .name(Component.text("§cBerserker"))
                .lore(Component.text("§7Great warrior with throwable axe"),
                        Component.text("§6Price: 50 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Berserker")));

        GuiItem bombardierItem = ItemBuilder.from(Material.TNT)
                .name(Component.text("§cBombardier"))
                .lore(Component.text("§7Destroyer of enemies' fences"),
                        Component.text("§6Price: 30 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Bombardier")));

        GuiItem skaldItem = ItemBuilder.from(Material.BOW)
                .name(Component.text("§cSkald"))
                .lore(Component.text("§7Sharp shooter with poisonous arrows"),
                        Component.text("§6Price: Free"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Skald")));

        GuiItem warriorItem = ItemBuilder.from(Material.IRON_SWORD)
                .name(Component.text("§cWarrior"))
                .lore(Component.text("§7Legendary warrior with Ragnarok ability"),
                        Component.text("§6Price: 80 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Warrior")));

        GuiItem marksmanItem = ItemBuilder.from(Material.CROSSBOW)
                .name(Component.text("§bMarksman"))
                .lore(Component.text("§7Most precise shooter with special crossbow"),
                        Component.text("§6Price: Free"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Marksman")));

        GuiItem spearmanItem = ItemBuilder.from(Material.TRIDENT)
                .name(Component.text("§bSpearman"))
                .lore(Component.text("§7Armed with a spear"),
                        Component.text("§6Price: 20 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Spearman")));

        GuiItem wizardItem = ItemBuilder.from(Material.SPLASH_POTION)
                .name(Component.text("§bWizard"))
                .lore(Component.text("§7Empower your magic with wizard kit"),
                        Component.text("§6Price: 45 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Wizard")));

        GuiItem knightItem = ItemBuilder.from(Material.SHIELD)
                .name(Component.text("§bKnight"))
                .lore(Component.text("§7Honored soldier who served the king"),
                        Component.text("§6Price: 90 coins"),
                        Component.text("§7Left-click to select"),
                        Component.text("§7Right-click to purchase"))
                .asGuiItem(event -> handleKitSelection(event, kitManager.getKitByName("Knight")));

        // Add items to the GUI
        gui.setItem(14, berserkerItem);
        gui.setItem(15, bombardierItem);
        gui.setItem(16, skaldItem);
        gui.setItem(17, warriorItem);
        gui.setItem(9, marksmanItem);
        gui.setItem(10, spearmanItem);
        gui.setItem(11, wizardItem);
        gui.setItem(12, knightItem);
    }

    public void open(Player player) {
        gui.open(player);
    }

    private void handleKitSelection(InventoryClickEvent event, KitManager.KitData selectedKit) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        event.setCancelled(true);

        if (event.isRightClick()) {
            attemptToPurchaseKit(player, selectedKit);
        }
        else if (event.isLeftClick()) {
            selectKit(player, selectedKit);
            gui.close(player);
        }
    }

    private void attemptToPurchaseKit(Player player, KitManager.KitData kit) {
        int kitPrice = plugin.getPlayerKitManager().getKitPrice(kit);

        if (plugin.getDbConnection().checkPlayerKit(player.getUniqueId(), kit.getName())) {
            player.sendMessage("§aYou already own this kit!");
            return;
        }

        plugin.getDbConnection().removePlayerCoins(player.getUniqueId(), kitPrice, success -> {
            if (success) {
                plugin.getDbConnection().unlockPlayerKit(player.getUniqueId(), kit);
                player.sendMessage("§aYou have successfully purchased the " + kit.getName() + " kit!");
            } else {
                player.sendMessage("§cYou do not have enough coins to purchase this kit. You need " + kitPrice + " coins!");
            }
        });
    }

    private void selectKit(Player player, KitManager.KitData selectedKit) {
        plugin.getPlayerKitManager().selectKit(player, selectedKit.getName());
    }
}
