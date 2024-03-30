package ru.oshifugo.functionalclans.command;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.oshifugo.functionalclans.FunctionalClans;

public class GUITranslatePlaceholder {
    OfflinePlayer player;
    protected String lang;
    protected YamlConfiguration yml;


    public GUITranslatePlaceholder(OfflinePlayer player, String lang, YamlConfiguration yml) {
        this.player = player;
        this.lang = lang;
        this.yml = yml;
    }

    public String get(String path) {
        String res = yml.getString(path);
        if (res == null) {
            Throwable t = new Throwable();
            StackTraceElement[] frames = t.getStackTrace();

            return String.format("§4Please inform to administrator:§f\n§3\"%s\"§f wasn't found in the language (§c%s§f) config. " +
                    "\n§c%s§f -> §9%s§f", path, lang, frames[1].getFileName(), frames[1].getClassName());
        }
        return PlaceholderAPI.setPlaceholders(player, res).replace("&", "§");
    }

    public String get(String path, boolean usePrefix) {
        if (usePrefix) {
            String prefix = FunctionalClans.instance.getConfig().getString("prefix");
            return prefix.replace("&", "§") + get(path);
        }
        return get(path);
    }

    public String getName(String path) {
        Object res = yml.get(path);
        if (res != null && yml.get(path + ".name") == null) {
            return null;
        }
        return get(path + ".name");

    }
    public String getLore(String path) {
        Object res = yml.get(path);
        if (res != null && yml.get(path + ".lore") == null) {
            return null;
        }
        return get(path + ".lore");
    }
}
