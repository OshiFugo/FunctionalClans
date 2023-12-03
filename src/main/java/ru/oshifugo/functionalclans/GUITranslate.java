package ru.oshifugo.functionalclans;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;

import java.io.File;

public class GUITranslate {
    protected String lang;
    protected File language_file;
    protected YamlConfiguration yml;
    protected Main plugin;
    private static GUITranslate instance;



    public static void init(Main plugin, String lang) {
        instance = new GUITranslate(plugin, lang);
    }

    public static GUITranslate getInstance() {
        return instance;
    }
    public static GUITranslatePlaceholder getTranslate(Player player) {
        return new GUITranslatePlaceholder(player, getInstance().lang, getInstance().yml);
    }



    protected GUITranslate(Main plugin, String lang) {
        this.lang = lang;
        this.plugin = plugin;
        this.language_file = new File(plugin.getDataFolder() +  "/gui_lang_" + lang + ".yml");
        yml = YamlConfiguration.loadConfiguration(language_file);
    }





}
