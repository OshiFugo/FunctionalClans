package ru.oshifugo.functionalclans;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import ru.oshifugo.functionalclans.command.AdminClanCommands;
import ru.oshifugo.functionalclans.command.ClanCommands;
import ru.oshifugo.functionalclans.events.Kill;
import ru.oshifugo.functionalclans.sql.SQLite;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.oshifugo.functionalclans.tabcomplete.AdminTab;
import ru.oshifugo.functionalclans.tabcomplete.CommandsTab;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public final class Main extends JavaPlugin {
    public static Main instance;
    private static Economy econ = null;
    public static HashMap<String, String[]> placeholders_config = new HashMap<>();
    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        SQLite.connect();
        SQLite.getClans();
        if (!setupEconomy() ) {
            utility.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            utility.error("Disabled due to no Vault dependency found!");
            utility.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            utility.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            utility.error("Disabled due to PlaceholderAPI dependency not found!");
            utility.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("clan").setExecutor(new ClanCommands());
        getServer().getPluginCommand("clan").setTabCompleter(new CommandsTab());
        getCommand("fc").setExecutor(new AdminClanCommands());
        getServer().getPluginCommand("fc").setTabCompleter(new AdminTab());
        Bukkit.getPluginManager().registerEvents(new Kill(), this);
//        saveResource("message.yml", true); // УБРАТЬ ПЕРЕД ОБНОВОЙ
        if (!new File(getDataFolder(), "message.yml").exists()) {
            saveResource("message.yml", false);
        }
//        if (!new File(getDataFolder(), "message.yml").exists()) {
//            saveResource("message.yml", false);
//        }
        HashConfig();
        Metrics metrics = new Metrics(this, 	17919);
        new Expansion().register();
        utility.info(utility.hex("<#FF00FF>~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
        utility.info(utility.hex("<#00CED1>Successfully enabled. &7(" + ChatColor.YELLOW + (System.currentTimeMillis() - time) + " ms" + ChatColor.GREEN + "&7)"));
        utility.info(utility.hex("<#FF00FF>~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }

    public static void  HashConfig() {
        placeholders_config.put("settings", new String[]{utility.config("placeholder_space"), utility.config("placeholder_prefix"), utility.config("placeholder_suffix"), utility.config("placeholder_null"), utility.config("placeholder_null_list_symbol")});
        placeholders_config.put("placeholder_list", utility.configList("placeholder_list"));
        placeholders_config.put("placeholder_null_list", utility.configList("placeholder_null_list"));
    }

    @Override
    public void onDisable() {
        SQLite.disconnect();
        utility.info(utility.hex("<#FF00FF>~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
        utility.info(utility.hex("<#00CED1>Plugin disabled."));
        utility.info(utility.hex("<#FF00FF>~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
    }
}
