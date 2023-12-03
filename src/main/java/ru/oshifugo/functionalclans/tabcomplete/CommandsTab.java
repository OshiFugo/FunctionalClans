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

import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;

public class CommandsTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String memberName = null;
        String clanName = null;
        String leaderName = null;
        if (members.containsKey(sender.getName())) {
            memberName = members.get(sender.getName())[0].toLowerCase();
            clanName = members.get(sender.getName())[2];
            leaderName = Clan.getLeader(clanName);
        }

        TabAccessor tabAccessor = new TabAccessor(sender, args);
        List<String> playerNamesList = new ArrayList<>();
        Bukkit.getServer().getOnlinePlayers()
                .forEach(player -> playerNamesList.add(player.getName()));

        tabAccessor.add("fc.create", "create");
        tabAccessor.add("fc.top", "top");
        tabAccessor.add("fc.list", "list");
        tabAccessor.add("fc.menu", "menu");

        if (leaderName != null && leaderName.equalsIgnoreCase(sender.getName())) {
            tabAccessor.add("fc.delete", "delete");
            tabAccessor.add("fc.rename", "rename");
            tabAccessor.add("fc.update", "update");
            tabAccessor.add("fc.settings", "settings");
//        *******************
//        ** settings part **
//        *******************
            tabAccessor.add("fc.message", "settings.message");
            tabAccessor.add("fc.status", "settings.status");
            tabAccessor.add("fc.social", "settings.social");
            tabAccessor.add("fc.type", "settings.type");
            tabAccessor.add("fc.role", "settings.role");
            tabAccessor.add("fc.setrole", "settings.setrole");
            tabAccessor.add("fc.sethome", "settings.sethome");
            tabAccessor.add("fc.removehome", "settings.removehome");

        }
        if (clanName != null) {
            tabAccessor.add("fc.info", "info");
            tabAccessor.add("fc.members", "members");
            tabAccessor.add("fc.accept", "accept");
            tabAccessor.add("fc.deny", "deny");
            tabAccessor.add("fc.leave", "leave");
            tabAccessor.add("fc.home", "home");
            tabAccessor.add("fc.ally", "ally");
            tabAccessor.add("fc.top", "top");
            tabAccessor.add("fc.stats", "stats");
//        *******************
//        **   ally part   **
//        *******************
            tabAccessor.addList("fc.ally", "ally", Arrays.asList("add", "remove"));
            tabAccessor.addList("fc.ally", "ally.add", Clan.getlistUID());
            tabAccessor.addList("fc.ally", "ally.remove", Clan.getlistUID());
//        **     other     **
            tabAccessor.addList("fc.cash", "cash", Arrays.asList("add", "remove"));
            tabAccessor.addList("fc.type", "***.type", Arrays.asList("0", "1"));
            tabAccessor.addList("fc.kick", "kick", Member.getMembers(clanName));
            tabAccessor.addList("fc.addrank", "addrank", Member.getMembers(clanName));
            tabAccessor.addList("fc.removerank", "removerank", Member.getMembers(clanName));
            tabAccessor.addList("fc.leader", "leader", Member.getMembers(clanName));
            tabAccessor.addList("fc.stats", "stats", Arrays.asList("rating", "kills", "deaths", "kdr"));
            tabAccessor.addList("fc.top", "top", Arrays.asList("rating", "members", "kills", "deaths", "kdr"));
            tabAccessor.addList("fc.invite", "invite", playerNamesList);
            tabAccessor.addList("fc.accept", "accept", playerNamesList);

        }

        if (memberName != null && clanName != null) {
            tabAccessor.add("fc.cash", "cash");
            tabAccessor.add("fc.invite", "invite");
            tabAccessor.add("fc.kick", "kick");
            tabAccessor.add("fc.chat", "cash");
            tabAccessor.add("fc.msg", "msg");
            tabAccessor.add("fc.addrank", "addrank");
            tabAccessor.add("fc.removerank", "removerank");
        }
        return tabAccessor.getCommands();
    }
}
