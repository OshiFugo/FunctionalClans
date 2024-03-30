package ru.oshifugo.functionalclans;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;

import java.io.File;

public class GUITranslate {
    protected String lang;
    protected File language_file;
    protected YamlConfiguration yml;
    protected FunctionalClans plugin;
    private static GUITranslate instance;



    public static void init(FunctionalClans plugin, String lang) {
        instance = new GUITranslate(plugin, lang);
    }

    public static GUITranslate getInstance() {
        return instance;
    }
    public static GUITranslatePlaceholder getTranslate(OfflinePlayer player) {
        return new GUITranslatePlaceholder(player, getInstance().lang, getInstance().yml);
    }



    protected GUITranslate(FunctionalClans plugin, String lang) {
        this.lang = lang;
        this.plugin = plugin;
        this.language_file = new File(plugin.getDataFolder() +  "/gui_lang_" + lang + ".yml");
        yml = YamlConfiguration.loadConfiguration(language_file);
    }





}
