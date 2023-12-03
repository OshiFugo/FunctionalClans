package ru.oshifugo.functionalclans.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;
import java.util.ArrayList;
import java.util.List;

public class AdminTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        TabAccessor tabAccessor = new TabAccessor(sender, args);
        tabAccessor.add("fc.admin.update", "update");
        tabAccessor.add("fc.admin.verify", "verify");
        tabAccessor.addList("fc.admin.verify", "verify", Clan.getlistUID());

        tabAccessor.add("fc.admin.members","members");
        tabAccessor.addList("fc.admin.members","members", Clan.getlistUID());

        tabAccessor.add("fc.admin.delete", "delete");
        tabAccessor.addList("fc.admin.delete", "delete", Clan.getlistUID());

        tabAccessor.add("fc.admin.leader", "leader");
        tabAccessor.addList("fc.admin.leader", "leader", Clan.getlistUID());
        if (args.length == 2) {
            tabAccessor.addList("fc.admin.leader", "leader", Member.getMembers(Clan.getClanNameUID(args[1])));
        }

        tabAccessor.add("fc.admin.info", "info");

        return tabAccessor.getCommands();


    }
}
