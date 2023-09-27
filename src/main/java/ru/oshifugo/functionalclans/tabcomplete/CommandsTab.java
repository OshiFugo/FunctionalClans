package ru.oshifugo.functionalclans.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;
import ru.oshifugo.functionalclans.sql.SQLiteUtility;
import ru.oshifugo.functionalclans.utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandsTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String[] clanInfo = null;
        String memberName = null;
        String clanName = null;
        try {
            clanInfo = SQLiteUtility.getClanByName(sender.getName());
            memberName = clanInfo[0];
            clanName = clanInfo[1];
        } catch (Exception e) {
            utility.debug("getClanByName -> null");
        }
        String leaderName = null;
        if (clanName != null) {
            leaderName = Clan.getLeader(clanName);
        }

        if (args.length == 1) {
            ArrayList<String> commands = new ArrayList<>();
            if (leaderName != null) {
                if (sender.hasPermission("fc.delete") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                    commands.add("delete");
                }
                if (sender.hasPermission("fc.rename") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                    commands.add("rename");
                }
                if (sender.hasPermission("fc.update") && clanName != null && !utility.config("add_max_player").equalsIgnoreCase("-1") && leaderName.equalsIgnoreCase(sender.getName())) {
                    commands.add("update");
                }
                if (leaderName.equalsIgnoreCase(sender.getName()) && (sender.hasPermission("fc.message") || sender.hasPermission("fc.status") || sender.hasPermission("fc.social") || sender.hasPermission("fc.type") || sender.hasPermission("fc.role") || sender.hasPermission("fc.sethome") || sender.hasPermission("fc.removehome"))) {
                    commands.add("settings");
                }
            }
            if (sender.hasPermission("fc.create") && clanName == null) {
                commands.add("create");
            }
            if (sender.hasPermission("fc.info") && clanName != null) {
                commands.add("info");
            }
            if (sender.hasPermission("fc.list")) {
                commands.add("list");
            }
            if (sender.hasPermission("fc.members") && clanName != null) {
                commands.add("members");
            }
            if (sender.hasPermission("fc.accept") && clanName == null) {
                commands.add("accept");
            }
            if (sender.hasPermission("fc.deny") && clanName == null) {
                commands.add("deny");
            }
            if (sender.hasPermission("fc.leave") && clanName != null) {
                commands.add("leave");
            }
            if (sender.hasPermission("fc.home") && clanName != null) {
                commands.add("home");
            }
            if (sender.hasPermission("fc.ally") && clanName != null) {
                commands.add("ally");
            }
            if (sender.hasPermission("fc.top")) {
                commands.add("top");
            }
            if (sender.hasPermission("fc.stats") && clanName != null) {
                commands.add("stats");
            }
            if (memberName != null) {
                if (sender.hasPermission("fc.cash") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 4) || Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 5)) {
                    commands.add("cash");
                }
                if (sender.hasPermission("fc.invite") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 3)) {
                    commands.add("invite");
                }
                if (sender.hasPermission("fc.kick") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 2)) {
                    commands.add("kick");
                }
                if (sender.hasPermission("fc.chat") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 7)) {
                    commands.add("chat");
                }
                if (sender.hasPermission("fc.msg") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 8)) {
                    commands.add("msg");
                }
                if (sender.hasPermission("fc.addrank") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                    commands.add("addrank");
                }
                if (sender.hasPermission("fc.removerank") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                    commands.add("removerank");
                }
            }
            return commands;
        }

        if (args[0].equalsIgnoreCase("settings") && args.length == 2) {
            ArrayList<String> commands = new ArrayList<>();
            if (sender.hasPermission("fc.message") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("message");
            }
            if (sender.hasPermission("fc.status") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("status");
            }
            if (sender.hasPermission("fc.social") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("social");
            }
            if (sender.hasPermission("fc.type") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("type");
            }
            if (sender.hasPermission("fc.role") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("role");
            }
            if (sender.hasPermission("fc.setrole") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("setrole");
            }
            if (sender.hasPermission("fc.sethome") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("sethome");
            }
            if (sender.hasPermission("fc.removehome") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                commands.add("removehome");
                return commands;
            }
        }
        if (clanName != null) {
            if (args.length == 3 && args[1].equalsIgnoreCase("type") && sender.hasPermission("fc.type")) {
                return Arrays.asList("0", "1");
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("ally") && sender.hasPermission("fc.ally")) {
                return Arrays.asList("add", "remove");
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("cash") && sender.hasPermission("fc.cash")) {
                return Arrays.asList("add", "remove");
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("invite") && sender.hasPermission("fc.invite")) {
                Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                ArrayList<String> onlinePlayersName = new ArrayList<>();
                for (Player onlinePlayer : onlinePlayers) {
                    String name = onlinePlayer.getName();
                    onlinePlayersName.add(name);
                }
                return onlinePlayersName;
            }
            if (args.length == 2 && ((args[0].equalsIgnoreCase("kick") && sender.hasPermission("fc.kick")) || (args[0].equalsIgnoreCase("addrank") && sender.hasPermission("fc.addrank")) || (args[0].equalsIgnoreCase("removerank") && sender.hasPermission("fc.removerank")) || (args[0].equalsIgnoreCase("leader") && sender.hasPermission("fc.leader")))) {
                return Member.getMembers(clanName);
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("stats") && sender.hasPermission("fc.stats")) {
                return Arrays.asList("rating", "kills", "deaths", "kdr");
            }
            if (args.length == 3 && args[0].equalsIgnoreCase("ally") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) && sender.hasPermission("fc.ally")) {
                return Clan.getlistUID();
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("top") && sender.hasPermission("fc.top")) {
            return Arrays.asList("rating", "members", "kills", "deaths", "kdr");
        }
        return new ArrayList<>();
    }
}
