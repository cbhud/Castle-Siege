package me.cbhud.castlesiege.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.kits.KitType;
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
        GuiItem berserkerItem = ItemBuilder.from(Material.IRON_AXE)
                .name(Component.text("§cBerserker"))
                .lore(Component.text("§7Great warrior with throwable axe"), Component.text("§6Price: 50 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.BERSERKER));

        GuiItem bombardierItem = ItemBuilder.from(Material.TNT)
                .name(Component.text("§cBombardier"))
                .lore(Component.text("§7Destroyer of enemies' fences"), Component.text("§6Price: 30 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.BOMBARDIER));

        GuiItem skaldItem = ItemBuilder.from(Material.BOW)
                .name(Component.text("§cSkald"))
                .lore(Component.text("§7Sharp shooter with poisonous arrows"), Component.text("§6Price: Free"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.SKALD));

        GuiItem warriorItem = ItemBuilder.from(Material.IRON_SWORD)
                .name(Component.text("§cWarrior"))
                .lore(Component.text("§7Legendary warrior with Ragnarok ability"), Component.text("§6Price: 80 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.WARRIOR));

        GuiItem marksmanItem = ItemBuilder.from(Material.CROSSBOW)
                .name(Component.text("§bMarksman"))
                .lore(Component.text("§7Most precise shooter with special crossbow"), Component.text("§6Price: Free"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.MARKSMAN));

        GuiItem spearmanItem = ItemBuilder.from(Material.TRIDENT)
                .name(Component.text("§bSpearman"))
                .lore(Component.text("§7Armed with a spear"), Component.text("§6Price: 20 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.SPEARMAN));

        GuiItem wizardItem = ItemBuilder.from(Material.SPLASH_POTION)
                .name(Component.text("§bWizard"))
                .lore(Component.text("§7Empower your magic with wizard kit"), Component.text("§6Price: 45 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.WIZARD));

        GuiItem knightItem = ItemBuilder.from(Material.SHIELD)
                .name(Component.text("§bKnight"))
                .lore(Component.text("§7Honored soldier who served the king"), Component.text("§6Price: 90 coins"), Component.text("§7Left-click to select"), Component.text("§7Right-click to select"))
                .asGuiItem(event -> handleKitSelection(event, KitType.KNIGHT));

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

    private void handleKitSelection(InventoryClickEvent event, KitType selectedKit) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        event.setCancelled(true); // Cancel the event upfront

        // Right-click to attempt to buy the kit
        if (event.isRightClick()) {
            attemptToPurchaseKit(player, selectedKit);
        }
        // Left-click to select the kit
        else if (event.isLeftClick()) {
            selectKit(player, selectedKit);
        }
    }

    private void attemptToPurchaseKit(Player player, KitType kit) {
        int kitPrice = plugin.getPlayerKitManager().getKitPrice(kit);

        if (plugin.getDbConnection().checkPlayerKit(player.getUniqueId(), kit.name())) {
            player.sendMessage("§aYou already own this kit!");
            return;
        }

        plugin.getDbConnection().removePlayerCoins(player.getUniqueId(), kitPrice, success -> {
            if (success) {
                plugin.getDbConnection().unlockPlayerKit(player.getUniqueId(), kit);
                player.sendMessage("§aYou have successfully purchased the " + kit + " kit!");
            } else {
                player.sendMessage("§cYou do not have enough coins to purchase this kit. You need " + kitPrice + " coins!");
            }
        });
    }

    private void selectKit(Player player, KitType selectedKit) {
            plugin.getPlayerKitManager().selectKit(player, selectedKit);
    }

}
