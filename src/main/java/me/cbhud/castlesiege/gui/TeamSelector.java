package me.cbhud.castlesiege.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.cbhud.castlesiege.team.Team;
import me.cbhud.castlesiege.CastleSiege; // Assuming you need access to the plugin instance
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamSelector {

    private final Gui gui;
    private final CastleSiege plugin;

    public TeamSelector(CastleSiege plugin) {
        this.plugin = plugin;
        gui = Gui.gui()
                .title(Component.text("§eSelect Team"))
                .rows(1)
                .create();

        init();
    }

    private void init() {
        GuiItem vikingTeamItem = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE)
                .name(Component.text(ChatColor.RED + plugin.getConfigManager().getAttacker()))
                .lore(Component.text("§7Assassinate the king and conquer the castle"))
                .asGuiItem(event -> handleTeamSelection(event, Team.Attackers));

        GuiItem franksTeamItem = ItemBuilder.from(Material.CYAN_STAINED_GLASS_PANE)
                .name(Component.text(ChatColor.AQUA + plugin.getConfigManager().getDefender()))
                .lore(Component.text("§7Defend the castle and king"))
                .asGuiItem(event -> handleTeamSelection(event, Team.Defenders));

        gui.setItem(3, franksTeamItem);
        gui.setItem(5, vikingTeamItem);
    }

    private void handleTeamSelection(InventoryClickEvent event, Team team) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        if (event.isRightClick()) {
            plugin.getTeamManager().joinTeam(player, team); // Method to handle the team joining logic
        }
        // Left-click to select the kit
        else if (event.isLeftClick()) {
            plugin.getTeamManager().joinTeam(player, team); // Method to handle the team joining logic
        }

    }

    public void open(Player player) {
        gui.open(player);
    }
}
