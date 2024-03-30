package ru.oshifugo.functionalclans;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import ru.oshifugo.functionalclans.command.AdminClanCommands;
import ru.oshifugo.functionalclans.command.ClanCommands;
import ru.oshifugo.functionalclans.events.Kill;
import ru.oshifugo.functionalclans.events.OnPlayerHit;
import ru.oshifugo.functionalclans.events.PlayerJoin;
import ru.oshifugo.functionalclans.events.SalaryEvents;
import ru.oshifugo.functionalclans.sql.SQLite;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.oshifugo.functionalclans.tabcomplete.AdminTab;
import ru.oshifugo.functionalclans.tabcomplete.CommandsTab;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class FunctionalClans extends JavaPlugin {
    public static FunctionalClans instance;

    private GUITranslate lang;
    private static Economy econ = null;


    public static HashMap<String, String[]> placeholders_config = new HashMap<>();

    public GUITranslate getLang() {
        return lang;
    }


    public int getDBVersion() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        int r = config.getInt("db-ver");
        return r;
    }
    public void setDBVersion(int value) throws IOException {
        File file = new File(getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("db-ver", value);
        config.save(file);
    }

    @Override
    public void onEnable() {

        long time = System.currentTimeMillis();

//        gui = Gui.normal();
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
        if (getServer().getPluginManager().getPlugin("SalaryManager") != null) {
            getServer().getPluginManager().registerEvents(new SalaryEvents(), this);
        }

        getCommand("clan").setExecutor(new ClanCommands());
        getServer().getPluginCommand("clan").setTabCompleter(new CommandsTab());
        getCommand("fc").setExecutor(new AdminClanCommands());
        getServer().getPluginCommand("fc").setTabCompleter(new AdminTab());
        Bukkit.getPluginManager().registerEvents(new Kill(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerHit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

//        saveResource("message.yml", true); // УБРАТЬ ПЕРЕД ОБНОВОЙ
        if (!new File(getDataFolder(), "message.yml").exists()) {
            saveResource("message.yml", false);
        }

        if (!new File(getDataFolder(), "gui_lang_en.yml").exists()) {
            saveResource("gui_lang_en.yml", true);
        }
        if (!new File(getDataFolder(), "gui_lang_ru.yml").exists()) {
            saveResource("gui_lang_ru.yml", true);
        }
        getConfig().addDefault("gui.lang", "en");
        if (getConfig().getBoolean("gui.override-lang")) {
            saveResource("gui_lang_en.yml", true);
        }
        GUITranslate.init(this, getConfig().getString("gui.lang"));
        lang = GUITranslate.getInstance();
        saveDefaultConfig();

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
