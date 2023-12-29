package me.cbhud;

import me.cbhud.Commands.Commands;
import me.cbhud.Events.CustomPlayerDeathEvent;
import me.cbhud.Events.StewEvent;
import me.cbhud.items.Manager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getConsoleSender().sendMessage("Vikings has been enabled!");
        Manager.init();
        getCommand("givesoup").setExecutor(new Commands());
        getCommand("giveaxe").setExecutor(new Commands());
        getCommand("giveblood").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new CustomPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new StewEvent(), this);
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage("Vikings has been disabled!");
    }

    public static Main getInstance() {
        return instance;
    }
}
