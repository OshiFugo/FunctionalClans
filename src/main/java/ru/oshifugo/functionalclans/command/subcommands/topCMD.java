package ru.oshifugo.functionalclans.command.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import ru.oshifugo.functionalclans.Utility;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class topCMD {
    public static String prefix = Utility.config("prefix");
    private String name;
    private int count;

    public topCMD(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public static void msgSorting(CommandSender sender, String[] args) {
        ArrayList<topCMD> cash = new ArrayList<>();
        if (args.length != 1) {
        if (args[0].equals("top") || args[0].equals("stats")) {
            cash = sorting(sender, args);
            if (cash == null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_args")));
                return;
            }
        }
        String page, type = args[1], arrow, function;
        Boolean[] bool = new Boolean[2];
        try {
            page = String.valueOf(Integer.parseInt(args[3]));
        } catch (Exception e) {
            page = "1";
        }
        page = String.valueOf(Integer.parseInt(page) * 7 - 7);
        try {
            if (args[2].equals("min")) {
                arrow = Utility.lang(sender,"top.text_12");
            } else arrow = Utility.lang(sender,"top.text_11");
        } catch (Exception e) {
            arrow = Utility.lang(sender,"top.text_11");
        }
        try {
            function = args[2];
        } catch (Exception e) {
            function = "max";
        }
        try {
            cash.get(Integer.parseInt(page)).getName();

            if ((Integer.parseInt(page) + 7) / 7 - 1 == 0) {
                bool[0] = false;
            } else bool[0] = true;
            try {
                cash.get(Integer.parseInt(page) + 7).getName();
                bool[1] = true;
            } catch (Exception e) {
                bool[1] = false;
            }
        } catch (Exception e) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_page")));
            return;
        }
        TextComponent message = new TextComponent();
        switch (args[0]) {
            case "top": message = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_1"))); break;
            case "stats": message = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_2"))); break;
            default: break;
        }
        switch (type) {
            case "rating": message.addExtra(Utility.hex(Utility.lang(sender,"top.text_3") + " " + arrow + "\n")); break;
            case "members": message.addExtra(Utility.hex(Utility.lang(sender,"top.text_4") + " " + arrow + "\n")); break;
            case "kills": message.addExtra(Utility.hex(Utility.lang(sender,"top.text_5") + " " + arrow + "\n")); break;
            case "deaths": message.addExtra(Utility.hex(Utility.lang(sender,"top.text_6") + " " + arrow + "\n")); break;
            case "kdr": message.addExtra(Utility.hex(Utility.lang(sender,"top.text_7") + " " + arrow + "\n")); break;
            default: break;
        }
        for (int i = Integer.parseInt(page), count = Integer.parseInt(page) + 1; i < Integer.parseInt(page) + 7; i++, count++) {
            try {
                message.addExtra(Utility.hex("&f" + String.valueOf(count) + ". " + String.valueOf(cash.get(i).getName() + "&f, &6" + cash.get(i).getCount() + "\n")));
            } catch (Exception e) {
                i = Integer.parseInt(page) + 7;
            }
        }
        if (bool[0] == true) {
            TextComponent msg = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_8")));
            if (bool[1] == true) {
                msg.addExtra(Utility.hex(String.format(" &f|&a %s/%s", String.valueOf((Integer.parseInt(page) + 7) / 7), String.valueOf((int) Math.ceil((double) cash.size() / 7)))));
            }
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan " + args[0] + " " + type + " " + function + " " + String.valueOf((Integer.parseInt(page) + 7) / 7 - 1)));
            message.addExtra(msg);
        } else {
            TextComponent msg = new TextComponent(Utility.hex(String.format(Utility.lang(sender,"pages.page"), "1", String.valueOf((int) Math.ceil((double) cash.size() / 7)))));
            message.addExtra(msg);
        }
        if (bool[1] == true) {
            TextComponent msg = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_9")));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan " + args[0] + " " + type + " " + function + " " + String.valueOf((Integer.parseInt(page) + 7) / 7 + 1)));
            message.addExtra(msg);
        } else {
            TextComponent msg = new TextComponent(Utility.hex(String.format(" | " + Utility.lang(sender,"pages.page"), String.valueOf((int) Math.ceil((double) cash.size() / 7)), String.valueOf((int) Math.ceil((double) cash.size() / 7)))));
            message.addExtra(msg);
        }
        message.addExtra(Utility.hex("\n" + Utility.lang(sender,"top.text_10")));
        sender.spigot().sendMessage(message);
        } else {
            if (args[0].equals("top")) {
                if (Member.getClan(sender.getName()) == null) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"top.text_0")));
                    return;
                }
                TextComponent message = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_1") + "\n"));
                for (int i = 0; i < 5; i++) {
                    String[] func = new String[]{"rating", "members", "kills", "deaths", "kdr"};
                    cash = sorting(sender, new String[]{"top", func[i], "max"});
                    for (int i1 = 0; i1 < cash.size(); i1++) {
                        if (cash.get(i1).getName().equals(Member.getClan(sender.getName()))) {
                            String text = "";
                            switch (func[i]) {
                                case "rating": text = Utility.lang(sender,"top.text_3"); break;
                                case "members": text = Utility.lang(sender,"top.text_4"); break;
                                case "kills": text = Utility.lang(sender,"top.text_5"); break;
                                case "deaths": text = Utility.lang(sender,"top.text_6"); break;
                                case "kdr": text = Utility.lang(sender,"top.text_7"); break;
                                default: break;
                            }
                            message.addExtra(Utility.hex("&a- " + text + String.format(Utility.lang(sender,"top.text_13"), i1 + 1) + "\n"));
                        }
                    }
                }
                message.addExtra(Utility.hex(Utility.lang(sender,"top.text_10")));
                sender.spigot().sendMessage(message);
            } else {
                TextComponent message = new TextComponent(Utility.hex(Utility.lang(sender,"top.text_2") + "\n"));
                for (int i = 0; i < 4; i++) {
                    String[] func = new String[]{"rating", "kills", "deaths", "kdr"};
                    cash = sorting(sender, new String[]{"stats", func[i], "max"});
                    for (int i1 = 0; i1 < cash.size(); i1++) {
                        if (cash.get(i1).getName().equals(sender.getName())) {
                            String text = "";
                            switch (func[i]) {
                                case "rating": text = Utility.lang(sender,"top.text_3"); break;
                                case "kills": text = Utility.lang(sender,"top.text_5"); break;
                                case "deaths": text = Utility.lang(sender,"top.text_6"); break;
                                case "kdr": text = Utility.lang(sender,"top.text_7"); break;
                                default: break;
                            }
                            message.addExtra(Utility.hex("&a- " + text + String.format(Utility.lang(sender,"top.text_13"), i1 + 1) + "\n"));
                        }
                    }
                }
                message.addExtra(Utility.hex(Utility.lang(sender,"top.text_10")));
                sender.spigot().sendMessage(message);
            }
        }
    }
    public static ArrayList<topCMD> sorting(CommandSender sender, String[] args) {
        String[] function = new String[1];
        String[] arrow = {Utility.lang(sender,"top.text_8")};
        String type = args[1];
        ArrayList<topCMD> cash = new ArrayList<topCMD>();

        if (args[0].equals("top") && (args[1].equals("rating") || args[1].equals("kills") || args[1].equals("deaths") || args[1].equals("members") || args[1].equals("kdr"))) {
            ArrayList<String> clans = Clan.getlistClans();
            for (int i = 0; i < clans.size(); i++) {
                if (type.equals("rating")) {
                    topCMD count = new topCMD(clans.get(i), Clan.getRating(clans.get(i)));
                    cash.add(count);
                } else if (type.equals("kills")) {
                    ArrayList<String> membersList = Member.getMembers(clans.get(i));
                    int kills = 0;
                    for (int i1 = 0; i1 < membersList.size(); i1++) {
                        kills = kills + Integer.parseInt(Member.getKills(membersList.get(i1)));
                    }
                    topCMD count = new topCMD(clans.get(i), kills);
                    cash.add(count);
                } else if (type.equals("deaths")) {
                    ArrayList<String> membersList = Member.getMembers(clans.get(i));
                    int deaths = 0;
                    for (int i1 = 0; i1 < membersList.size(); i1++) {
                        deaths = deaths + Integer.parseInt(Member.getDeaths(membersList.get(i1)));
                    }
                    topCMD count = new topCMD(clans.get(i), deaths);
                    cash.add(count);
                } else if (type.equals("members")) {
                    topCMD count = new topCMD(clans.get(i), Member.getCount(clans.get(i)));
                    cash.add(count);
                } else if (type.equals("kdr")) {
                    int kills = 0;
                    int deaths = 0;
                    ArrayList<String> membersList = Member.getMembers(clans.get(i));
                    for (int i1 = 0; i1 < membersList.size(); i1++) {
                        kills = kills + Integer.parseInt(Member.getKills(membersList.get(i1)));
                        deaths = deaths + Integer.parseInt(Member.getDeaths(membersList.get(i1)));
                    }
                    if (kills > 0 && deaths == 0) {
                        topCMD count = new topCMD(clans.get(i), 100);
                        cash.add(count);
                    } else if (kills == 0 && deaths == 0) {
                        topCMD count = new topCMD(clans.get(i), 0);
                        cash.add(count);
                    } else {
                        topCMD count = new topCMD(clans.get(i), kills / deaths);
                        cash.add(count);
                    }
                }
            }
        } else if (args[0].equals("stats") && (args[1].equals("rating") || args[1].equals("kills") || args[1].equals("deaths") || args[1].equals("kdr"))) {
            if (Member.getClan(sender.getName()) == null) {
                return null;
            }
            ArrayList<String> clan_members = Member.getMembers(Member.getClan(sender.getName()));
            for (int i = 0; i < Member.getMembers(Member.getClan(sender.getName())).size(); i++) {
                if (type.equals("rating")) {
                    topCMD count = new topCMD(clan_members.get(i), Integer.parseInt(Member.getRating(clan_members.get(i))));
                    cash.add(count);
                } else if (type.equals("kills")) {
                    topCMD count = new topCMD(clan_members.get(i), Integer.parseInt(Member.getKills(clan_members.get(i))));
                    cash.add(count);
                } else if (type.equals("deaths")) {
                    topCMD count = new topCMD(clan_members.get(i), Integer.parseInt(Member.getDeaths(clan_members.get(i))));
                    cash.add(count);
                } else if (type.equals("kdr")) {
                    int kills = Integer.parseInt(Member.getKills(clan_members.get(i)));
                    int deaths = Integer.parseInt(Member.getDeaths(clan_members.get(i)));
                    ;
                    if (kills > 0 && deaths == 0) {
                        topCMD count = new topCMD(clan_members.get(i), 100);
                        cash.add(count);
                    } else if (kills == 0 && deaths == 0) {
                        topCMD count = new topCMD(clan_members.get(i), 0);
                        cash.add(count);
                    } else {
                        topCMD count = new topCMD(clan_members.get(i), kills / deaths);
                        cash.add(count);
                    }
                }
            }
        } else return null;
        Collections.sort(cash, new Comparator<topCMD>() {
            @Override
            public int compare(topCMD o1, topCMD o2) {
                try {
                    function[0] = args[2];
                    if (function[0].equalsIgnoreCase("min")) {
                        return Integer.compare(o1.count, o2.count);
                    } else {
                        return Integer.compare(o2.count, o1.count);
                    }
                } catch (Exception e) {
                    return Integer.compare(o2.count, o1.count);
                }
            }
        });
        return cash;
    }
}
