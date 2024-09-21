package me.cbhud.castlesiege.kits;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerKitManager {
    private final Map<Player, KitType> selectedKits;
    private CastleSiege plugin;

    public PlayerKitManager(CastleSiege plugin) {

        this.plugin = plugin;
        this.selectedKits = new HashMap<>();
    }

    public void giveKit(Player player, KitType kitType) {

        player.getInventory().clear();

        for (ItemStack item : kitType.getItems()){
            player.getInventory().addItem(item);
        }

        if (kitType.hasArmor()){
            equipArmor(player, kitType.getArmorItems());
        }

        selectedKits.put(player, kitType);
    }

    public boolean hasSelectedKit(Player player) {
        return selectedKits.containsKey(player);
    }


    public boolean selectKit(Player player, KitType kitType) {
        if (kitType.getTeam() != plugin.getTeamManager().getTeam(player)){
            player.sendMessage("§cYou cannot select the kit from the opposing team!");
            return false;
        }
        if (plugin.getDbConnection().checkPlayerKit(player.getUniqueId(), kitType.toString())){
        selectedKits.put(player, kitType);
        plugin.getScoreboardManager().updateScoreboard(player);
            player.sendMessage("§aYou have selected the " + kitType + " kit!");
            return true;
        }else {
            player.sendMessage("§cYou do not have this kit unlocked. Right-click to purchase it.");
            return false;
        }
    }

    public KitType getSelectedKit(Player player) {
        return selectedKits.get(player);
    }

    public void setDefaultKit(Player player, Team team) {
        // Choose your default kit or provide some logic to determine it
        KitType defaultKit = KitType.getDefaultKit(team);

        if (defaultKit != null) {
            selectKit(player, defaultKit);
        }
    }

    public int getKitPrice(KitType kit) {
        switch (kit) {
            case BERSERKER:
                return 40;
            case BOMBARDIER:
                return 30;
            case WARRIOR:
                return 80;
            case KNIGHT:
                return 90;
            case SPEARMAN:
                return 20;
            case WIZARD:
                return 45;
            default:
                return 0;
        }
    }


    private void equipArmor(Player player, ItemStack[] armorItems) {
        if (armorItems.length == 4) {
            player.getInventory().setHelmet(armorItems[0]);
            player.getInventory().setChestplate(armorItems[1]);
            player.getInventory().setLeggings(armorItems[2]);
            player.getInventory().setBoots(armorItems[3]);
        }
    }

}
