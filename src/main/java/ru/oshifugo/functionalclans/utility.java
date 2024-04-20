package ru.oshifugo.functionalclans;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utility {
//    public static String ColorTratslate(String msg) {
//        msg = ChatColor.translateAlternateColorCodes('&', msg);
//        return msg;
//    }

    public static String getRawClan(String name) {
        for (String clan : Clan.getlistClans()) {
            if (getRawName(clan).equals(getRawName(name))) {
                return clan;
            }
        }
        return null;
    }

    public static String getRawName(String name) {
//        ff&
        StringBuilder total = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '&' || name.charAt(i) == '§') {
                i ++;
                continue;
            }
            total.append(name.charAt(i));
        }
        return total.toString();
    }

    public static boolean hasAnyOfPermsOrLeader(Player player, String... _perms) {
        Map<String, Integer> ranks = new HashMap<>();

        ranks.put("fc.kick", 2);
        ranks.put("fc.invite", 3);
        ranks.put("fc.cash_add", 4);
        ranks.put("fc.cash_remove", 5);
//        ranks.put("fc.rmanage", 6);
        ranks.put("fc.chat", 7);
        ranks.put("fc.msg", 8);
        ranks.put("fc.alliance_add",9);
        ranks.put("fc.alliance_remove", 10);
        //
        //
        ranks.put("fc.create", -2);
        ranks.put("fc.top", -1);
        ranks.put("fc.list", -1);
        ranks.put("fc.menu", -1);
        ranks.put("fc.delete", -2);
        ranks.put("fc.rename", -2);
        ranks.put("fc.update", -2);
        ranks.put("fc.settings", -2);
        ranks.put("fc.message", -2);
        ranks.put("fc.mpvp", -2);
        ranks.put("fc.status", -2);
        ranks.put("fc.social", -2);
        ranks.put("fc.type", -2);
        ranks.put("fc.role", -2);
        ranks.put("fc.setrole", -2);
        ranks.put("fc.sethome", -2);
        ranks.put("fc.removehome", -2);
        ranks.put("fc.info", -1);
        ranks.put("fc.members", -1);
        ranks.put("fc.accept", -1);
        ranks.put("fc.deny", -1);
        ranks.put("fc.leave", -1);
        ranks.put("fc.home", -1);
        ranks.put("fc.ally", -1);
        ranks.put("fc.top", -1);
        ranks.put("fc.stats", -1);

        String clanName = Member.getClan(player.getName());
//        Clan.getRank(sender.getName()) > Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName())
        List<String> perms = Arrays.asList(_perms);
        AtomicBoolean returnValue = new AtomicBoolean(false);

        perms.forEach(perm -> {
            if (!ranks.containsKey(perm)) {
                throw new RuntimeException(String.format("Permission %s was not found. Report to the plugin creator", perm));
            }
            int rank = ranks.get(perm);
            if (!player.hasPermission(perm)) {
                return;
            }
            if (rank == -1){
                returnValue.set(true);
            }
            if (clanName == null) return;
            else if (rank == -2 && Clan.getLeader(clanName).equals(player.getName())) {
               returnValue.set(true);
            }
            else if (rank != -1 && rank != -2 && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(player.getName())), rank)) {
                returnValue.set(true);
            }

        });
        return returnValue.get();
    }

    public static String config(String cfg) {
        cfg = FunctionalClans.getInstance().getConfig().getString(cfg);
        return cfg;
    }

    public static boolean configBoolean(String cfg) {
        boolean bool;
        bool = FunctionalClans.getInstance().getConfig().getBoolean(cfg);
        return bool;
    }

    public static String[] configList(String cfg) {
        List<String> myArray =  FunctionalClans.getInstance().getConfig().getStringList(cfg);
        String[] response = myArray.toArray(new String[myArray.size()]);
        int i = 0;
        for (String element : myArray) {
            response[i] = element;
            i++;
        }
        return response;
    }
//    public static String lang(String cfg) {
//        if (Files.exists(Paths.get(Main.instance.getDataFolder() + "/message.yml"))) {
//            File langYml = new File(Main.instance.getDataFolder()+"/message.yml");
//            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langYml);
//            if (langConfig.getString(utility.config("lang") + "." + cfg) == null) {
//                cfg = " There was an error in message.yml. The required key could not be found. Recheck the values.";
//            } else cfg = langConfig.getString(utility.config("lang") + "."  + cfg);
//        } else cfg = " Could not find message.yml file";
//        return cfg;
//    }
    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[2048];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }
    public static String lang(CommandSender sender, String cfg) {
        if (Files.exists(Paths.get(FunctionalClans.getInstance().getDataFolder() + "/message.yml"))) {
            File langYml = new File(FunctionalClans.getInstance().getDataFolder()+"/message.yml");
            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langYml);
            if (langConfig.getString(utility.config("lang") + "." + cfg) == null) {
                File temp = new File("temp");
                InputStream stream = FunctionalClans.getInstance().getResource("message.yml");
                assert stream != null;
                try {
                    copyInputStreamToFile(stream, temp);
                    langConfig = YamlConfiguration.loadConfiguration(temp);
                    cfg = langConfig.getString(utility.config("lang") + "."  + cfg);
                    if (cfg == null) {
                        cfg = " There was an error in message.yml. The required key could not be found. Recheck the values.";
                    }
                } catch (IOException e) {
                    cfg = " There was an error in message.yml. The required key could not be found. Recheck the values.";
                }

            } else cfg = langConfig.getString(utility.config("lang") + "."  + cfg);
        } else cfg = " Could not find message.yml file";
        if (!(sender instanceof Player)) {
            return cfg;
        }
        Player player = (Player) sender;
        return PlaceholderAPI.setPlaceholders(player, cfg);
    }

//    public static String quest(String cfg) {
//        if (Files.exists(Paths.get(Main.instance.getDataFolder() + "/quest.yml"))) {
//            File langYml = new File(Main.instance.getDataFolder()+"/quest.yml");
//            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langYml);
//            cfg = langConfig.getString(cfg);
//        } else cfg = " Could not find quest.yml file";
//        return cfg;
//    }
    public static void info(Object text) {
        Bukkit.getConsoleSender().sendMessage(hex("[" + FunctionalClans.getInstance().getName() + "] " + text));
    }
    public static void warning(Object text) {
        Bukkit.getConsoleSender().sendMessage(utility.hex("&6[" + FunctionalClans.getInstance().getName() + "] [warning]" + text));
    }
    public static void error(Object text) {
        Bukkit.getConsoleSender().sendMessage(hex("&4[" + FunctionalClans.getInstance().getName() + "] [ERROR] " + text));
    }
    public static void debug(Object text) {
        if (FunctionalClans.getInstance().getConfig().getBoolean("debug")) {
            Bukkit.getConsoleSender().sendMessage(hex("&e[" + FunctionalClans.getInstance().getName() + "] [Debug] " + text));
        }
    }
    public static String hex(String msg) {
        String version = Bukkit.getServer().getBukkitVersion();
        if (version.startsWith("1.15") || version.startsWith("1.14") || version.startsWith("1.13") || version.startsWith("1.12") || version.startsWith("1.11") || version.startsWith("1.10") || version.startsWith("1.9") || version.startsWith("1.8")) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            return msg;
        } else {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            Matcher matcher = Pattern.compile("<#[A-Fa-f0-9]{6}>").matcher(msg);
            int hexAmount;
            for(hexAmount = 0; matcher.find(); ++hexAmount) {
                matcher.region(matcher.end() - 1, msg.length());
            }
            int startIndex = 0;
            for(int hexIndex = 0; hexIndex < hexAmount; ++hexIndex) {
                int msgIndex = msg.indexOf("<#", startIndex);
                String hex = msg.substring(msgIndex + 1, msgIndex + 8);
                startIndex = msgIndex + 2;
                msg = msg.replace("<" + hex + ">", net.md_5.bungee.api.ChatColor.of(hex) + "");
            }
            return msg;
        }
    }

    public static TextComponent page_msg(CommandSender sender, String[] args, int page, int max_line, int count) {
        TextComponent msg = new TextComponent("");
        if (count == 0) {
            msg.addExtra(utility.hex(utility.lang(sender, "pages.no_values")));
            return msg;
        }
        int end_count = page * max_line;
        int start_count = end_count - max_line;
        int page_max = (int) Math.ceil((double) count / max_line);
        if (count < start_count) {
            msg.addExtra(utility.hex(utility.lang(sender, "pages.no_page")));
            return msg;
        }
        TextComponent back = new TextComponent(), next = new TextComponent();
        String cmd = "";
        if (args[0].equalsIgnoreCase("list")) {
            cmd = "/clan list %s";
        }
        if (start_count - 1 > 0) {
            back.addExtra(utility.hex(utility.lang(sender, "pages.back")));
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(cmd, String.valueOf(page - 1))));
        }
        if (end_count <= count) {
            next.addExtra(utility.hex(utility.lang(sender, "pages.next")));
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(cmd, String.valueOf(page + 1))));
        }
        msg.addExtra(back);
        msg.addExtra(utility.hex(String.format(utility.lang(sender,"pages.page"), page, page_max)));
        msg.addExtra(next);
        return msg;
    }
}
