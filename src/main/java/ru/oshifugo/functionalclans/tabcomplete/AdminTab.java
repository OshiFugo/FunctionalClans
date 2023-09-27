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
        if (args.length == 1) {
            ArrayList<String> commands = new ArrayList<>();
            if (sender.hasPermission("fc.admin.update")) {
                commands.add("update");
            }
            if (sender.hasPermission("fc.admin.verify")) {
                commands.add("verify");
            }
            if (sender.hasPermission("fc.admin.info")) {
                commands.add("info");
            }
            if (sender.hasPermission("fc.admin.members")) {
                commands.add("members");
            }
            if (sender.hasPermission("fc.admin.delete")) {
                commands.add("delete");
            }
            if (sender.hasPermission("fc.admin.leader")) {
                commands.add("leader");
            }
            return commands;
        }
        if (args.length == 2 && ((args[0].equalsIgnoreCase("verify") && sender.hasPermission("fc.admin.verify")) || (args[0].equalsIgnoreCase("info") && sender.hasPermission("fc.admin.info")) || (args[0].equalsIgnoreCase("members") && sender.hasPermission("fc.admin.members")) || (args[0].equalsIgnoreCase("delete") && sender.hasPermission("fc.admin.delete")) || (args[0].equalsIgnoreCase("leader") && sender.hasPermission("fc.admin.leader")))) {
            return Clan.getlistUID();
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("leader") && sender.hasPermission("fc.admin.leader")) {
            return Member.getMembers(Clan.getClanNameUID(args[1]));
        }
        return new ArrayList<>();
    }
}
