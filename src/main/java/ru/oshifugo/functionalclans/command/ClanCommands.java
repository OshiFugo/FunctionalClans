package ru.oshifugo.functionalclans.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.oshifugo.functionalclans.FunctionalClans;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.Utility;
import ru.oshifugo.functionalclans.command.subcommands.topCMD;
import ru.oshifugo.functionalclans.sql.*;

import java.util.ArrayList;

public class ClanCommands implements CommandExecutor {
    static String prefix = Utility.hex(Utility.config("prefix"));
    public static boolean check(CommandSender sender, String memberName, String clanName, String permission) {
        if (!sender.hasPermission("fc." + permission)) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
            return false;
        }
        if (memberName == null) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_clan")));
            return false;
        }
        if (!memberName.equalsIgnoreCase(Clan.getLeader(clanName))) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_leader")));
            return false;
        }
        return true;
    }

    public static boolean checkSmall(CommandSender sender, String memberName, String clanName, String permission) {
        if (!sender.hasPermission("fc." + permission)) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
            return false;
        }
        if (memberName == null) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_clan")));
            return false;
        }
        return true;
    }
    public static boolean removeEconomyCfg(Player player, String cfg) {
        Economy economy = FunctionalClans.getEconomy();
        if (!Utility.config(cfg).equalsIgnoreCase("-1")) {
            EconomyResponse response = economy.withdrawPlayer(player, Integer.valueOf(Utility.config(cfg)));
            if (!response.transactionSuccess()) {
                player.sendMessage(Utility.hex(prefix + Utility.lang(player,"common_errors.no_money") + Utility.config(cfg)));
                return false;
            }
        }
        return true;
    }
    public static boolean removeEconomy(Player player, String cash) {
        Economy economy = FunctionalClans.getEconomy();
        EconomyResponse response = economy.withdrawPlayer(player, Integer.valueOf(cash));
        if (!response.transactionSuccess()) {
            player.sendMessage(Utility.hex(prefix + Utility.lang(player,"common_errors.no_transfer_money") + cash));
            return false;
        }
        return true;
    }
    public static boolean addEconomy(Player player, String cash) {
        Economy economy = FunctionalClans.getEconomy();
        EconomyResponse response = economy.depositPlayer(player, Integer.valueOf(cash));
        if (!response.transactionSuccess()) {
            player.sendMessage(Utility.hex(prefix + Utility.lang(player,"common_errors.no_transfer_money") + cash));
            return false;
        }
        return true;
    }

    public void help(CommandSender sender, String clanName, String memberName, String type) {
        /*
            type: 0 - [ВСЕ] 1 - [1 ранг] 2 - [2 ранг] 3 - [3 ранг] 4 - [4 ранг] 5 - [Лидер] 6 - [Настройки] 7 - [Общие]
         */
        String leaderName = null;
        int i = 0;
        TextComponent message = new TextComponent(Utility.hex(prefix + Utility.lang(sender, "help.clan_msg") + "\n"));
        if (clanName != null) {
            leaderName = Clan.getLeader(clanName);
            message.addExtra(Utility.hex(Utility.lang(sender, "help.clan_msg1")));
            TextComponent text0 = new TextComponent();
            TextComponent text1 = new TextComponent();
            for (int i1 = 0; i1 < 8; i1++) {
                TextComponent button = new TextComponent();
                if (i1 > 0 && i1 < 5 && leaderName.equalsIgnoreCase(sender.getName())) {
                    button.addExtra(Utility.hex(String.format(Utility.lang(sender, String.format("help.button_%s", i1)), Clan.getRoleName(clanName, i1))));
                } else button.addExtra(Utility.hex(Utility.lang(sender, String.format("help.button_%s", i1))));
                button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/clan help %s", i1)));
                if (i1 > 0 && i1 < 5 && leaderName.equalsIgnoreCase(sender.getName())) {
                    text1.addExtra(button);
                } else {
                    if (leaderName.equalsIgnoreCase(sender.getName())) {
                        text0.addExtra(button);
                    } else if (i1 == 0 || i1 == 7) {
                        text0.addExtra(button);
                    }
                }

            }
            message.addExtra(text0);
            message.addExtra("\n");
            message.addExtra(text1);
            if (!text1.toPlainText().isEmpty()) {
                message.addExtra("\n");
            }
        } else {
            message.addExtra(Utility.hex(Utility.lang(sender, "help.clan_msg1")));
            TextComponent button = new TextComponent(Utility.hex(String.format(Utility.lang(sender, "help.button_0"))));
            button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan help 7"));
            message.addExtra(button);
            message.addExtra("\n");
        }
        if (type.equals("0") || type.equals("5")) {
            if (sender.hasPermission("fc.delete") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.delete.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan delete"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.leader") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.leader.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan leader"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.rename") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.rename.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan rename"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.update") && clanName != null && !Utility.config("add_max_player").equalsIgnoreCase("-1") && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.update_members.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan update"));
                message.addExtra(text);
                i++;
            }
        }
        if (type.equals("0") || type.equals("6")) {
            if (sender.hasPermission("fc.message") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.message.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings message"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.status") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.status.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings status"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.social") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.social.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings social"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.type") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.type.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings type"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.role") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.role.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings role"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.pvp") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang (sender, "commands.pvp.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings pvp"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.setrole") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.setrole.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings setrole"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.sethome") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.sethome.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings sethome"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.removehome") && clanName != null && leaderName.equalsIgnoreCase(sender.getName())) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.removehome.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan settings removehome"));
                message.addExtra(text);
                i++;
            }
        }
        if (type.equals("0") || type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) {
            if (sender.hasPermission("fc.ally") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 9)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 9))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.allyadd.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan ally add"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.ally") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 10)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 10))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.allyremove.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan ally remove"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.cash") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 4)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 4))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.cashadd.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan cash add"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.cash") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 5)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 5))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.cashremove.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan cash remove"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.invite") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 3)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 3))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.invite.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan invite"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.kick") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 2)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 2))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.kick.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan kick"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.chat") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 7)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 7))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.chat.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan chat"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.msg") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 8)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 8))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.msg.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan msg"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.addrank") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 6))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.addrank.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan addrank"));
                    message.addExtra(text);
                    i++;
                }
            }
            if (sender.hasPermission("fc.removerank") && clanName != null && Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                if ((type.equals("0") && !(type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))) || ((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) && Clan.hasRole(clanName, Integer.parseInt(type), 6))) {
                    TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.removerank.errors.e") + "\n"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan removerank"));
                    message.addExtra(text);
                    i++;
                }
            }
        }
        if (type.equals("0") || type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || type.equals("7")) {
            if (sender.hasPermission("fc.create") && clanName == null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.create.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan create"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.info") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.info.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan info"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.list")) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.list.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan list"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.members") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.members.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan members"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.accept") && clanName == null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.accept.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan accept"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.deny") && clanName == null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.deny.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan deny"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.leave") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.leave.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan leave"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.home") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.home.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan home"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.ally") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender, "commands.ally.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan ally"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.top")) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender,"commands.top.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan top"));
                message.addExtra(text);
                i++;
            }
            if (sender.hasPermission("fc.stats") && clanName != null) {
                TextComponent text = new TextComponent(Utility.hex(Utility.lang(sender,"commands.stats.errors.e") + "\n"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clan stats"));
                message.addExtra(text);
                i++;
            }
        }
        if (i == 0 && !type.equals("-1")) {
            message = new TextComponent();
            message.addExtra(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission") + "\n"));
        }
        sender.spigot().sendMessage(message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_console")));
            return true;
        }
        Player player = (Player) sender;
        String[] clanInfo = null;
        String memberName = null;
        String clanName = null;
        try {
            clanInfo = SQLiteUtility.getClanByName(sender.getName());
            memberName = clanInfo[0];
            clanName = clanInfo[1];
        } catch (Exception e) {
            Utility.debug("getClanByName -> null");
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            Boolean isActive = FunctionalClans.getInstance().getConfig().getBoolean("gui.active");
            if (!isActive) {
                help(sender, clanName, memberName, "-1");
                return true;
            }
            if (!ClanGUI.isSupported()) {
                player.sendMessage("§cSorry, your server version is bellow 1.14.0 (contact developer if not). This mean that you cannot use GUI :(");
                return true;
            }

            ClanGUI clanGUI = new ClanGUI(player);
            clanGUI.home(player);
            clanGUI.display(GUITranslate.getTranslate(player).get("root.name"));
//            help(sender, clanName, memberName, "-1");
            return true;
        } else if (args[0].equalsIgnoreCase("help")) {
            if (args.length == 2) {
                if (Integer.parseInt(args[1]) >= 0 && Integer.parseInt(args[1]) <= 9) {
                    help(sender, clanName, memberName, args[1]);
                    return true;
                }
            }
            help(sender, clanName, memberName, "-1");
            return true;

        }  else if (args[0].equalsIgnoreCase("settings") && args.length == 1) {
            help(sender, clanName, memberName, "6");
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission("fc.create")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return true;
            }
            if (memberName != null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.member_clan")));
                return true;
            }
            if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_name")));
                return true;
            }
            if (!(args[1].length() >= Integer.valueOf(Utility.config("min_name")) && args[1].length() <= Integer.valueOf(Utility.config("max_name")))) {
                sender.sendMessage(String.format(Utility.hex(prefix + Utility.lang(sender,"common_errors.name_length")), Utility.config("min_name"), Utility.config("max_name")));
                return true;
            }
            if (args[1].contains("'")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_name")));
                return true;
            }
            if (Utility.getRawClan(args[1]) == null) {
//                utility.getRawClan(renamed);
                if (Integer.valueOf(Clan.getCountClan()) == 9000) {
                    sender.sendMessage(Utility.hex("&c[&4ERROR&c] &4&lClan creation limit reached"));
                    return true;
                }
                if (!removeEconomyCfg(player, "creation_price")) {
                    return true;
                }
                SQLiteUtility.create(args[1], sender.getName());
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.create.message.msg")));

            } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.name_taken")));
            return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (!checkSmall(sender, memberName, clanName, "delete")) { return true; }
            TextComponent message = new TextComponent(Utility.hex(prefix + Utility.lang(sender,"commands.delete.message.line_1")));
            TextComponent msg = new TextComponent(Utility.hex(Utility.lang(sender,"commands.delete.message.line_2")));
            message.addExtra(msg);
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan delete fc" + Clan.getClanRealName(clanName)));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.delete.message.line_2-1"))).create()));
            if (!Clan.getLeader(clanName).equalsIgnoreCase(sender.getName().toLowerCase())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_leader")));
                return true;
            }
            if (args.length >= 2) {
                if (args[1].equalsIgnoreCase("fc" + Clan.getClanRealName(clanName))) {
                    if (!removeEconomyCfg(player, "delete_price")) {
                        return true;
                    }
                    SQLiteUtility.delete(Clan.getClanRealName(clanName));
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.delete.message.msg")));
                } else sender.spigot().sendMessage(message);
            } else sender.spigot().sendMessage(message);
            return true;
        } else if (args[0].equalsIgnoreCase("info")) {
            if (!checkSmall(sender, memberName, clanName, "info")) { return true; }
            TextComponent text = new TextComponent(Utility.hex("\n" + Utility.lang(sender,"commands.info.message.line_1") + " "));
            TextComponent ver = new TextComponent(Utility.hex(Utility.lang(sender,"commands.info.message.line_1-1") + "\n"));
            TextComponent social;
            TextComponent online;
            ver.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.info.message.line_1-2") + "\n" + Utility.lang(sender,"main.id") + "\n" + Utility.lang(sender,"commands.info.message.line_1-3"))).create()));
            text.addExtra(ver);
            text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_2") + "\n"));
            text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_3") + "\n"));
            text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_4") + "\n"));
            text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_5") + "\n"));
            text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_6") + "\n"));
            if (!Clan.getStatus(clanName).equalsIgnoreCase("null")) {
                text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_7") + "\n"));
            }
            if (!Clan.getSocial(clanName).equalsIgnoreCase("null")) {
                text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_8")));
                social = new TextComponent(Utility.hex(Utility.lang(sender,"commands.info.message.line_8-1")) + "\n");
                social.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + Clan.getSocial(clanName)));
                text.addExtra(social);
            }
            online = new TextComponent(Utility.hex(Utility.lang(sender,"commands.info.message.line_9") + "\n"));
            online.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.info.message.line_9-1"))).create()));
            online.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan members"));
            text.addExtra(online);
            if (!Clan.getMessage(clanName).equalsIgnoreCase("")) {
                text.addExtra(Utility.hex(Utility.lang(sender,"commands.info.message.line_10")+ "\n"));
            }
            sender.spigot().sendMessage(text);
            return true;

        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("pvp")) {
            if (!Utility.hasAnyOfPermsOrLeader(player, "fc.mpvp")) { return true; }
            Clan.togglePVP(clanName);
            String now;
            GUITranslatePlaceholder translate = GUITranslate.getTranslate(player);
            if (Clan.getPVP(clanName)) {
                now = translate.get("settings.pvp.enabled");
            } else {
                now = translate.get("settings.pvp.disabled");
            }
            player.sendMessage(translate.get("settings.pvp.success").replace("{now}", now));
            return true;

        }else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("role")) {
            if (!check(sender, memberName, clanName, "role")) { return true; }
            ArrayList<String> role = new ArrayList<>();
            int role_count = 5, max_role = 9;
            for (int i = 2, rank =  1; rank < role_count; i++) {
                if(Clan.hasRole(clanName, rank, i)) {
                    role.add(Utility.hex("&a" + Utility.lang(sender,"main.true")));
                } else role.add(Utility.hex("&4" + Utility.lang(sender,"main.false")));
                if (i == SQLiteUtility.clan_role.get(clanName + "_" + rank).length - 1) {
                    rank++;
                    i = 1;
                }
            }
            TextComponent message = new TextComponent(Utility.hex(prefix + Utility.lang(sender,"commands.role.message.msg") + "\n"));
            TextComponent message1 = new TextComponent("(!)");
            message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex("&7(&d&l*&7^^&d&l*&7)")).create()));
            message.addExtra(message1);
            message.addExtra(" |");
            for (int role_name_counter = 0, i = 0; i < max_role; i++) {
                TextComponent message_role_name = new TextComponent();
                if (i != max_role-1) {
                    message_role_name.addExtra(Utility.hex(" (?)  "));
                    message_role_name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.role.message.line_2-" + (role_name_counter + 1)))).create()));
                    message.addExtra(message_role_name);
                    message.addExtra(Utility.hex(("&7|")));
                } else {
                    message_role_name.addExtra(Utility.hex(" (?)"));
                    message_role_name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.role.message.line_2-" + (role_name_counter + 1)))).create()));
                    message.addExtra(message_role_name);
                    message.addExtra(Utility.hex("&7 |\n"));
                }
                role_name_counter++;
            }
            for (int i = 0, count = 0, role_counter = 0; role_counter < role_count - 1; i++) {
                if (i == 0) {
                    TextComponent button = new TextComponent(Utility.hex("1: &7|"));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Clan.getRoleName(clanName, 1))).create()));
                    message.addExtra(button);
                }
                count++;
                TextComponent button;
                if (i != 8 && i != 17 && i != 26 &&i != 35) { // При изменении кол-во ролей изменить
                    button = new TextComponent(Utility.hex(" &f(" + role.get(i) + "&f) "));
                } else button = new TextComponent(Utility.hex(" &f(" + role.get(i) + "&f)"));
                button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan settings setrole " + String.valueOf(role_counter + 1) + " " + String.valueOf(count + 1)));
                button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.role.message.line_2"))).create()));
                message.addExtra(button);
                message.addExtra(Utility.hex("&7|"));
                if (count == max_role) {
                    i = i + role.size()/4 - max_role;
                    role_counter++;
                    count = 0;
                    message.addExtra("\n");
                    if (role_counter < role_count - 1) {
                        TextComponent button1 = new TextComponent(Utility.hex(String.valueOf(role_counter + 1) + ": &7|"));
                        button1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Clan.getRoleName(clanName, role_counter + 1))).create()));
                        message.addExtra(button1);
                    }
                }
            }
            TextComponent button1 = new TextComponent(Utility.hex("\n" + Utility.lang(sender,"commands.role.message.line_1") + "\n"));
            button1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.role.message.line_1-1") + "\n" + Utility.lang(sender,"commands.role.message.line_1-2") + "\n" + Utility.lang(sender,"commands.role.message.line_1-3") + "\n" + Utility.lang(sender,"commands.role.message.line_1-4"))).create()));
            button1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan settings role"));
            message.addExtra(button1);
            sender.spigot().sendMessage(message);
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("setrole")) {
            if (!check(sender, memberName, clanName, "setrole")) { return true; }
            if (args.length == 4) {
                try {
                    if (!args[3].equalsIgnoreCase("1")) {
                        String roleName = "";
                        if (Integer.valueOf(args[3]) == 2) {
                            roleName = "kick";
                        } else if (Integer.valueOf(args[3]) == 3) {
                            roleName = "invite";
                        } else if (Integer.valueOf(args[3]) == 4) {
                            roleName = "cash_add";
                        } else if (Integer.valueOf(args[3]) == 5) {
                            roleName = "cash_remove";
                        } else if (Integer.valueOf(args[3]) == 6) {
                            roleName = "rmanage";
                        } else if (Integer.valueOf(args[3]) == 7) {
                            roleName = "chat";
                        } else if (Integer.valueOf(args[3]) == 8) {
                            roleName = "msg";
                        } else if (Integer.valueOf(args[3]) == 9) {
                            roleName = "alliance_add";
                        } else if (Integer.valueOf(args[3]) == 10) {
                            roleName = "alliance_remove";
                        }
                        if (Utility.configBoolean(roleName + "_enable")) {
                            Clan.setRole(clanName, Integer.valueOf(args[2]), Integer.valueOf(args[3]));
                            if (Utility.configBoolean("message_setrole")) {
                                String symbol = "&4" + Utility.lang(sender, "main.false");
                                if (Clan.hasRole(clanName, Integer.valueOf(args[2]), Integer.valueOf(args[3]))) {
                                    symbol = "&a" + Utility.lang(sender, "main.true");
                                }
                                sender.sendMessage(Utility.hex(prefix + String.format(Utility.lang(sender, "commands.setrole.message.msg"), Utility.lang(sender, "commands.role.message.line_2-" + String.valueOf(Integer.parseInt(args[3]) - 1)), args[2], symbol)));
                            }
                        } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.setrole.errors.e1")));
                    } else sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + " " + args[1] + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[1])))));
                } catch (Exception e) { sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + " " + args[1] + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[1]))))); }
            } else sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + " " + args[1] + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[1])))));
            return true;
        } else if (args[0].equalsIgnoreCase("members")) {
            if (!checkSmall(sender, memberName, clanName, "members")) { return true; }
            String role_5 = "";
            String role_4 = "";
            String role_3 = "";
            String role_2 = "";
            String role_1 = "";
            for (int i = 0; i < Member.getMembers(clanName).size(); i++) {
                int role = Integer.valueOf(Member.getRank(Member.getMembers(clanName).get(i)));
                if (role == 1) {
                    if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                        if (role_1.length() != 0) {
                            role_1 = role_1 + Utility.hex(", &a") + Member.getMembers(clanName).get(i);
                        } else role_1 = Utility.hex("\n" + Clan.getRoleName(clanName, 1) + "&a: " + Member.getMembers(clanName).get(i));
                    } else {
                        if (role_1.length() != 0) {
                            role_1 = role_1 +  Utility.hex(", &2") + Member.getMembers(clanName).get(i);
                        } else role_1 = Utility.hex("\n" + Clan.getRoleName(clanName, 1) + "&a:&2 " + Member.getMembers(clanName).get(i));
                    }
                } else if (role == 2) {
                    if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                        if (role_2.length() != 0) {
                            role_2 = role_2 + Utility.hex(", &a") + Member.getMembers(clanName).get(i);
                        } else role_2 = Utility.hex("\n" + Clan.getRoleName(clanName, 2) + "&a: " + Member.getMembers(clanName).get(i));
                    } else {
                        if (role_2.length() != 0) {
                            role_2 = role_2 +  Utility.hex(", &2") + Member.getMembers(clanName).get(i);
                        } else role_2 = Utility.hex("\n" + Clan.getRoleName(clanName, 2) + "&a:&2 " + Member.getMembers(clanName).get(i));
                    }
                } else if (role == 3) {
                    if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                        if (role_3.length() != 0) {
                            role_3 = role_3 + Utility.hex(", &a") + Member.getMembers(clanName).get(i);
                        } else role_3 = Utility.hex("\n" + Clan.getRoleName(clanName, 3) + "&a: " + Member.getMembers(clanName).get(i));
                    } else {
                        if (role_3.length() != 0) {
                            role_3 = role_3 +  Utility.hex(", &2") + Member.getMembers(clanName).get(i);
                        } else role_3 = Utility.hex("\n" + Clan.getRoleName(clanName, 3) + "&a:&2 " + Member.getMembers(clanName).get(i));
                    }
                } else if (role == 4) {
                    if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                        if (role_4.length() != 0) {
                            role_4 = role_4 + Utility.hex(", &a") + Member.getMembers(clanName).get(i);
                        } else role_4 = Utility.hex("\n" + Clan.getRoleName(clanName, 4) + "&a: " + Member.getMembers(clanName).get(i));
                    } else {
                        if (role_4.length() != 0) {
                            role_4 = role_4 +  Utility.hex(", &2") + Member.getMembers(clanName).get(i);
                        } else role_4 = Utility.hex("\n" + Clan.getRoleName(clanName, 4) + "&a:&2 " + Member.getMembers(clanName).get(i));
                    }
                } else if (role == 5) {
                    if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                        if (role_5.length() != 0) {
                            role_5 = role_5 + Utility.hex(", &a")  + Member.getMembers(clanName).get(i);
                        } else role_5 = Utility.hex("\n" + Clan.getRoleName(clanName, 5) + "&a: " + Member.getMembers(clanName).get(i));
                    } else {
                        if (role_5.length() != 0) {
                            role_5 = role_5 +  Utility.hex(", &2")  + Member.getMembers(clanName).get(i);
                        } else role_5 = Utility.hex("\n" + Clan.getRoleName(clanName, 5) + "&a:&2 " + Member.getMembers(clanName).get(i));
                    }
                }
            }
            String members = Utility.hex(role_5 + role_4 + role_3 + role_2 + role_1);
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.members.message.msg") + members));
            return true;
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("fc.list")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return true;
            }
            if (Clan.getlistClans().size() == 0) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_created_clans")));
                return true;
            }
            int page, page_list;
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                page = 1;
            }
            page_list = Clan.getlistClans().size()/7 + 1;
            if (!(page_list >= page && ((page - 1) * 7) >= 0)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"pages.no_page")));
                return true;
            }
            TextComponent message = new TextComponent(Utility.hex(prefix + Utility.lang(sender,"commands.list.message.line_1")));
            for (int i = (page - 1) * 7, count = 0; i < Clan.getlistClans().size(); i++) {
                if (count != 7) {
                    String ver = Utility.hex(Utility.lang(sender, "main.false"));
                    String type = Utility.hex(Utility.lang(sender, "main.closed"));
                    if (Clan.getVerification(Clan.getlistClans().get(i))) {
                        ver = Utility.hex(Utility.lang(sender,"main.true"));
                    }
                    if (Clan.getType(Clan.getlistClans().get(i)) == 1) {
                        type = Utility.hex(Utility.lang(sender, "main.open"));
                    }
                    TextComponent msgInvite = new TextComponent(Utility.hex(""));
                    if (!String.valueOf(Clan.getType(Clan.getlistClans().get(i))).equalsIgnoreCase("0")) {
                        if (Member.getClan(sender.getName()) == null && Member.getCount(Clan.getlistClans().get(i)) < Clan.getMax_player(Clan.getlistClans().get(i))) {
                            msgInvite.addExtra(Utility.hex("&a[&e+&a]"));
                            msgInvite.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(Utility.lang(sender,"commands.list.message.line_2"))).create()));;
                            msgInvite.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan request " + Clan.getUID(Clan.getlistClans().get(i))));
                        }
                    }
                    TextComponent msg = new TextComponent(Utility.hex(String.valueOf("\n&7- &f" + Clan.getlistClans().get(i) + "&2 [&f" + Member.getCount(Clan.getlistClans().get(i)) + "&2]" + " (&f" + Clan.getLeader(Clan.getlistClans().get(i)) + "&2) ")));
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex(String.format(Utility.lang(sender,"commands.list.message.line_1-1") + "\n" + Utility.lang(sender,"commands.list.message.line_1-2") + "\n" + Utility.lang(sender,"commands.list.message.line_1-3") + "\n" + Utility.lang(sender,"commands.list.message.line_1-4") + "\n" + Utility.lang(sender,"commands.list.message.line_1-5") + "\n" + Utility.lang(sender,"commands.list.message.line_1-6"), ver, type, Clan.getStatus(Clan.getlistClans().get(i)), Member.getCount(Clan.getlistClans().get(i)), Member.getOnlineCount(Clan.getlistClans().get(i)), Clan.getRating(Clan.getlistClans().get(i)), Clan.getUID(Clan.getlistClans().get(i))))).create()));
                    message.addExtra(msg);
                    message.addExtra(msgInvite);
                } else i = Clan.getlistClans().size();
                count++;
            }
            message.addExtra("\n");
            if (args.length >= 2) {
                message.addExtra(Utility.page_msg(sender, args, page, 7, Clan.getlistClans().size()));
            } else message.addExtra(Utility.page_msg(sender, args, 1, 7, Clan.getlistClans().size()));
            sender.spigot().sendMessage(message);
            sender.sendMessage(Utility.hex(Utility.lang(sender, "pages.line")));
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("message")) {
            if (!check(sender, memberName, clanName, "message")) { return true; }
            String message = "";
            for(int i = 2; i < args.length; ++i) {
                message = message + args[i] + " ";
            }
            Clan.setMessage(clanName, message);
            if (Utility.configBoolean("chat_message"))
                Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender,"commands.message.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("social")) {
            if (!check(sender, memberName, clanName, "social")) { return true; }
            String message = new String();
            if (args.length == 3) {
                message = args[2];
            } else message = null;
            Clan.setSocial(clanName, message);
            if (Utility.configBoolean("chat_social"))
                Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender,"commands.social.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("status")) {
            if (!check(sender, memberName, clanName, "status")) { return true; }
            String message = "";
            for(int i = 2; i < args.length; ++i) {
                message = message + args[i] + " ";
            }
            int max_status = FunctionalClans.getInstance().getConfig().getInt("max_status");
            if (message.length() > max_status) {
                player.sendMessage(GUITranslate.getTranslate(player).get("status.too-many-letters", true)
                        .replace("{max}", String.valueOf(max_status)));
                return true;
            }
            Clan.setStatus(clanName, message);
            if (Utility.configBoolean("chat_status")) {
                Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender, "commands.status.message.msg")));
            }
            return true;


        } else if (args[0].equalsIgnoreCase("home")) {
            if (!checkSmall(sender, memberName, clanName, "home")) { return true; }
            if (Utility.config("home").equalsIgnoreCase("0")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.command_disabled")));
                return true;
            }
            if ((!Clan.hasHome(clanName))) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.home.errors.e1")));
                return true;
            }
            if (Utility.config("home").equalsIgnoreCase("1")) {
                Location homeLocation = Clan.getHome(clanName);
                int timerInSeconds = Integer.parseInt(Utility.config("home_delay"));
                if (timerInSeconds > 0) {
                    final int[] timer = {timerInSeconds};
                    Location originalLocation = player.getLocation();
                    final int[] taskId = new int[1];
                    taskId[0] = Bukkit.getScheduler().runTaskTimer(FunctionalClans.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            if (Utility.config("home_protection_tp").equalsIgnoreCase("1")) {
                                if (!player.getLocation().equals(originalLocation)) {
                                    player.sendMessage(Utility.hex(prefix + Utility.lang(player, "commands.home.message.msg3")));
                                    Bukkit.getScheduler().cancelTask(taskId[0]);
                                    return;
                                }
                            }
                            if (timer[0] > 0) {
                                player.sendMessage(Utility.hex(prefix + String.format(Utility.lang(player, "commands.home.message.msg2"), timer[0])));
                                timer[0]--;
                            } else {
                                player.teleport(homeLocation);
                                player.sendMessage(Utility.hex(prefix + Utility.lang(player, "commands.home.message.msg")));
                                Bukkit.getScheduler().cancelTask(taskId[0]);
                            }
                        }
                    }, 0L, 20L).getTaskId();
                } else
                {
                    player.teleport(homeLocation);
                    player.sendMessage(Utility.hex(prefix + Utility.lang(player, "commands.home.message.msg")));
                }
            } else {
                Location location = Clan.getHome(clanName);
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.home.message.msg1")));
            }
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("sethome")) {
            if (!check(sender, memberName, clanName, "sethome")) { return true; }
            if (Utility.config("home").equalsIgnoreCase("0")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.command_disabled")));
                return true;
            }
            Clan.setHome(clanName, player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
            if (Utility.configBoolean("chat_home"))
                Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender,"commands.sethome.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("removehome")) {
            if (!check(sender, memberName, clanName, "removehome")) { return true; }
            if (Utility.config("home").equalsIgnoreCase("0")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.command_disabled")));
                return true;
            }
            Clan.setHome(clanName, (String)null, 0.0, 0.0, 0.0, 0.0F, 0.0F);
            if (Utility.configBoolean("chat_home"))
                Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender,"commands.removehome.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("type")) {
            if (!check(sender, memberName, clanName, "type")) { return true; }
            if (Clan.getVerification(clanName) == false) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_verification")));
                return true;
            }
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("0") || args[2].equalsIgnoreCase("1")) {
                    Clan.setType(clanName, Integer.valueOf(args[2]));
                    if (Utility.configBoolean("chat_type"))
                        Clan.broadcast(clanName, Utility.hex(Bukkit.getPlayer(memberName).getName() + Utility.lang(sender,"commands.type.message.msg")));
                    return true;
                }
            }
            sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + " " + args[1] + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[1])))));
            return true;
        } else if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c")) {
            if (!checkSmall(sender, memberName, clanName, "chat")) { return true; }
            if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 7)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            }
            String message = "";
            for (int i = 1; i < args.length; i++) {
                message = message + args[i] + " ";
            }
            if (message != "") {
                Clan.chat(clanName, memberName, message);
            } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender, "commands.chat.errors.e1")));
            return true;
        } else if (args[0].equalsIgnoreCase("msg")) {
            if (!checkSmall(sender, memberName, clanName, "msg")) { return true; }
            if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 8)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            }
            String message = "";
            for (int i = 1; i < args.length; i++) {
                message = message + args[i] + " ";
            }
            if (message != "") {
                Clan.msg(clanName, memberName, message);
            } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender, "commands.msg.errors.e1")));
            return true;
        } else if (args[0].equalsIgnoreCase("leader")) {
            if (!check(sender, memberName, clanName, "leader")) { return true; }
            if (args.length == 2) {
                if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    if (SQLiteUtility.member_clan.get(Bukkit.getOfflinePlayer(args[1]).getName()) == null) {
                        sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_clans")));
                        return true;
                    }
                    if (SQLiteUtility.member_clan.get(Bukkit.getOfflinePlayer(args[1]).getName()).equalsIgnoreCase(clanName)) {
                        if (!removeEconomyCfg(player, "leader_price")) {
                            return true;
                        }
                        Clan.setLeader(clanName, Bukkit.getOfflinePlayer(args[1]).getName());
                        if (Utility.configBoolean("chat_leader"))
                            Clan.broadcast(clanName, Utility.hex(Utility.lang(sender,"commands.leader.message.msg")));
                    } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_clan")));
                } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player")));
            } else sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[0])))));
            return true;
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (!check(sender, memberName, clanName, "rename")) {
                return true;
            } else if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_name")));
                return true;
            } else if (!(args[1].length() >= Integer.valueOf(Utility.config("min_name")) && args[1].length() <= Integer.valueOf(Utility.config("max_name")))) {
                    sender.sendMessage(String.format(Utility.hex(prefix + Utility.lang(sender,"common_errors.name_length")), Utility.config("min_name"), Utility.config("max_name")));
                return true;
            } else if (Utility.getRawClan(args[1]) != null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.name_taken")));
                return true;
            }
            if (!removeEconomyCfg(player, "rename_price")) {
                return true;
            }
            if (args[1].contains("'")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_name")));
                return true;
            }

            Clan.setClanName(clanName, args[1]);
            if (Utility.configBoolean("chat_rename"))
                Clan.broadcast(args[1], Utility.hex(Utility.lang(sender,"commands.rename.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (!checkSmall(sender, memberName, clanName, "invite")) {
                return true;
            } else if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 3)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            } else if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_players")));
                return true;
            } else if (!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_online")));
                return true;
            } else if (Member.getClan(Bukkit.getOfflinePlayer(args[1]).getName()) == null) {
                if (InviteRequest.request.get(Bukkit.getOfflinePlayer(args[1]).getName()) != null) {
                    if (InviteRequest.request.get(Bukkit.getOfflinePlayer(args[1]).getName()).equalsIgnoreCase(Clan.getUID(clanName))) {
                        sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.invite.message.msg1")));
                        return true;
                    }
                }
                if (Clan.getMax_player(clanName) <= Member.getCount(clanName)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.limit_maxplayer")));
                    return true;
                }
                if (!removeEconomyCfg(player, "invite_price")) {
                    return true;
                }
                InviteRequest.addRequest(clanName, Bukkit.getPlayer(args[1]).getName());
                Bukkit.getPlayer(args[1]).sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.invite.message.msg2") + "\n" + Utility.lang(sender,"commands.invite.message.msg2-1") + "\n" + Utility.lang(sender,"commands.invite.message.msg2-2")));
                sender.sendMessage(Utility.hex(String.format(prefix + Utility.lang(sender,"commands.invite.message.msg"), Bukkit.getPlayer(args[1]).getName())));
            } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.player_clan")));
            return true;
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (!sender.hasPermission("fc.accept")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return false;
            } else if (InviteRequest.request.get(sender.getName()) == null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_invite")));
                return true;
            } else if (Clan.getClanNameUID(InviteRequest.request.get(sender.getName())) == null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.accept.errors.e1")));
                InviteRequest.denyRequest(sender.getName());
                return true;
            }
            if (Clan.getMax_player(Clan.getClanNameUID(InviteRequest.request.get(sender.getName()))) <= Member.getCount(Clan.getClanNameUID(InviteRequest.request.get(sender.getName())))) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.limit_maxplayer")));
                return true;
            }
            if (Member.getClan(sender.getName()) != null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.you_player_clan")));
                return true;
            }
            InviteRequest.acceptRequest(sender.getName());
            if (Utility.configBoolean("chat_invite"))
                Clan.broadcast(Member.getClan(sender.getName()), String.format(Utility.hex(Utility.lang(sender,"commands.accept.message.msg")), sender.getName()));
            return true;
        } else if (args[0].equalsIgnoreCase("deny")) {
            if (!sender.hasPermission("fc.deny")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return false;
            } else if (InviteRequest.request.get(sender.getName()) == null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_invite")));
                return true;
            }
            InviteRequest.denyRequest(sender.getName());
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.deny.message.msg")));
            return true;
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (!checkSmall(sender, memberName, clanName, "kick")) {
                return true;
            } else if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            } else if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_players")));
                return true;
            } else if (!Member.hasClan(clanName, Bukkit.getOfflinePlayer(args[1]).getName())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_clan")));
                return true;
            }
            if (Clan.getRank(sender.getName()) > Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName())) {
                if (Utility.configBoolean("chat_kick"))
                    Clan.broadcast(clanName, String.format(Utility.hex(Utility.lang(sender,"commands.kick.message.msg")), sender.getName(), Bukkit.getOfflinePlayer(args[1]).getName()));
                Clan.kick(clanName, Bukkit.getOfflinePlayer(args[1]).getName());
            } else {
                if (sender.getName().equalsIgnoreCase(Bukkit.getOfflinePlayer(args[1]).getName())) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_yourself")));
                } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.kick.errors.e1")));
            }
            return true;
        } else if (args[0].equalsIgnoreCase("addrank")) {
            if (!checkSmall(sender, memberName, clanName, "addrank")) {
                return true;
            } else if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            } else if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_players")));
                return true;
            } else if (!Member.hasClan(clanName, Bukkit.getOfflinePlayer(args[1]).getName())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_clan")));
                return true;
            } else if (sender.getName().equalsIgnoreCase(Bukkit.getOfflinePlayer(args[1]).getName())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_yourself")));
                return true;
            } else if (!(Clan.getRank(sender.getName()) > Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName()))) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_rank")));
                return true;
            } else if (Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName()) > 3) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.addrank.errors.e1")));
                return true;
            }
            Clan.addrank(Bukkit.getOfflinePlayer(args[1]).getName());
            if (Utility.configBoolean("chat_addrank"))
                Clan.broadcast(clanName, String.format(Utility.hex(Utility.lang(sender,"commands.addrank.message.msg")), sender.getName(), Bukkit.getOfflinePlayer(args[1]).getName()));
            return true;
        } else if (args[0].equalsIgnoreCase("removerank")) {
            if (!checkSmall(sender, memberName, clanName, "removerank")) {
                return true;
            } else if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 6)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                return true;
            } else if (!(args.length == 2)) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_players")));
                return true;
            } else if (!Member.hasClan(clanName, Bukkit.getOfflinePlayer(args[1]).getName())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_player_clan")));
                return true;
            } else if (sender.getName().equalsIgnoreCase(Bukkit.getOfflinePlayer(args[1]).getName())) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_yourself")));
                return true;
            } else if (!(Clan.getRank(sender.getName()) > Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName()))) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_rank")));
                return true;
            } else if (Clan.getRank(Bukkit.getOfflinePlayer(args[1]).getName()) == 1) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.removerank.errors.e1")));
                return true;
            }
            Clan.removerank(Bukkit.getOfflinePlayer(args[1]).getName());
            if (Utility.configBoolean("chat_removerank"))
                Clan.broadcast(clanName, String.format(Utility.hex(Utility.lang(sender,"commands.removerank.message.msg")), sender.getName(), Bukkit.getOfflinePlayer(args[1]).getName()));
            return true;
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (!checkSmall(sender, memberName, clanName, "leave")) {
                return true;
            } else if (sender.getName().equalsIgnoreCase(Clan.getLeader(clanName))) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.leave.errors.e1")));
                return true;
            }
            if (Utility.configBoolean("chat_leave"))
                Clan.broadcast(clanName, String.format(Utility.hex(Utility.lang(sender,"commands.leave.message.msg")), sender.getName()));
            Clan.kick(clanName, sender.getName());
            return true;
        } else if (args[0].equalsIgnoreCase("ally")) {
            if (!checkSmall(sender, memberName, clanName, "ally")) {
                return true;
            } else if (args.length == 1) {
                ArrayList<String> ally = Ally.clan_ally(Clan.getUID(clanName));
                TextComponent message = new TextComponent(Utility.hex(prefix + Utility.lang(sender,"commands.ally.message.msg")));
                for (int i = 0; i < ally.size(); i++) {
                    TextComponent msg = new TextComponent(Utility.hex(Clan.getClanNameUID(ally.get(i))));
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utility.hex("&f" + ally.get(i))).create()));
                    if (i != ally.size() - 1) { msg.addExtra(", "); }
                    message.addExtra(msg);
                }
                sender.spigot().sendMessage(message);
                return true;
            } else if (args.length < 3) {
                sender.sendMessage(Utility.hex(String.format(Utility.lang(sender,"help.msg"), command.getName(), args[0]) + "\n" + String.format(Utility.lang(sender,"help.msg1"), Utility.lang(sender, String.format("commands.%s.errors.e", args[0])))));
                return true;
            }
            if (args[1].equalsIgnoreCase("add")) {
                if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 9)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                    return true;
                } else if (Clan.getVerification(clanName) == false) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_verification")));
                    return true;
                } else if (Clan.hasUID(args[2])) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_clan_uid")));
                    return true;
                } else if (Clan.getUID(clanName).equalsIgnoreCase(args[2])) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_yourself")));
                    return true;
                } else if (!Clan.getVerification(clanName)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_verification")));
                    return true;
                } else if (!Ally.hasAlly(Clan.getUID(clanName), args[2])) {
                    if (Ally.AllyRequest.get(Clan.getUID(clanName)) != null && Ally.AllyRequest.get(Clan.getUID(clanName)).equalsIgnoreCase(args[2])) {
                        sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.allyadd.errors.e1")));
                        return true;
                    }
                    if (Ally.addAlly(Clan.getUID(clanName), args[2])) {
                        if (Utility.configBoolean("chat_allyadd"))
                            Clan.broadcast(clanName, Utility.hex(Utility.lang(sender,"commands.allyadd.message.msg") + Clan.getClanNameUID(args[2])));
                            Clan.broadcast(Clan.getClanNameUID(args[2]), Utility.hex(Utility.lang(sender,"commands.allyadd.message.msg") + clanName));
                    } else {
                        if (Utility.configBoolean("chat_allyadd"))
                            Clan.broadcast(Clan.getClanNameUID(args[2]),  Utility.hex(Utility.lang(sender,"commands.allyadd.message.msg1") + clanName + "\n" + Utility.lang(sender,"commands.allyadd.message.msg3") + Clan.getUID(clanName)));
                        sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.allyadd.message.msg2")));
                    }
                } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.allyadd.errors.e2")));
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 10)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                    return true;
                } else if (Clan.hasUID(args[2])) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_clan_uid")));
                    return true;
                } else if (Clan.getUID(clanName).equalsIgnoreCase(args[2])) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_yourself")));
                    return true;
                } else if (Ally.hasAlly(Clan.getUID(clanName), args[2])) {
                    Ally.removeAlly(Clan.getUID(clanName), args[2]);
                    if (Utility.configBoolean("chat_allyremove"))
                        Clan.broadcast(clanName, String.format(Utility.hex(Utility.lang(sender,"commands.allyremove.message.msg")), sender.getName(), Clan.getClanNameUID(args[2])));
                        Clan.broadcast(Clan.getClanNameUID(args[2]), String.format(Utility.hex(Utility.lang(sender,"commands.allyremove.message.msg")), sender.getName(), clanName));
                } else sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.allyremove.errors.e1")));
                return true;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("update")) {
            if (!check(sender, memberName, clanName, "update")) {
                return true;
            } else if (args.length < 2) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_args")));
                return true;
            } else if (args[1].equalsIgnoreCase("members")) {
                int count = 0;
                if (Utility.config("add_max_player").equalsIgnoreCase("-1")) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.command_disabled")));
                    return true;
                } else if (Clan.getMax_player(clanName) >= Integer.valueOf(Utility.config("max_player_limit"))) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.update_members.errors.e1")));
                    return true;
                } else if (!removeEconomyCfg(player, "add_max_player")) {
                    return true;
                } else if (Clan.getMax_player(clanName) + Integer.valueOf(Utility.config("count_max_player")) <= Integer.valueOf(Utility.config("max_player_limit"))) {
                    count = Clan.getMax_player(clanName) + Integer.valueOf(Utility.config("count_max_player"));
                } else if (Clan.getMax_player(clanName) + Integer.valueOf(Utility.config("count_max_player")) > Integer.valueOf(Utility.config("max_player_limit"))) {
                    count = Integer.valueOf(Utility.config("max_player_limit"));
                }
                Clan.setMax_player(clanName, String.valueOf(count));
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.update_members.message.msg")));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("request")) {
            if (!sender.hasPermission("fc.request")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return true;
            } else if (args.length != 2) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_args")));
                return true;
            } else if (Member.getClan(sender.getName()) != null) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.you_player_clan")));
                return true;
            } else if (Clan.hasUID(args[1])) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_clan_uid")));
                return true;
            } else if (Clan.getType(Clan.getClanNameUID(args[1])) != 1) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.request.errors.e1")));
                return true;
            }
            InviteRequest.addRequest(Clan.getClanNameUID(args[1]), sender.getName());
            sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"commands.request.message.msg") + "\n" + Utility.lang(sender,"commands.request.message.msg1-1") + "\n" + Utility.lang(sender,"commands.request.message.msg1-2")));
            return true;
        } else if (args[0].equalsIgnoreCase("cash")) {
            if (!checkSmall(sender, memberName, clanName, "cash")) {
                return true;
            } else if (args.length != 3) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_args")));
                return true;
            }
            if (args[1].equalsIgnoreCase("add")) {
                if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 4)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                    return true;
                }
                try {
                    Integer.valueOf(args[2]);
                } catch (Exception e) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.transfer_amount")));
                    return true;
                }
                if (!removeEconomy(player, args[2])) {
                    return true;
                }
                Clan.setCash(clanName, String.valueOf(Integer.valueOf(args[2]) + Clan.getCash(clanName)));
                sender.sendMessage(String.format(Utility.hex(prefix + Utility.lang(sender,"commands.cashadd.message.msg")), args[2]));
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (!Clan.hasRole(clanName, Integer.valueOf(Member.getRank(memberName)), 5)) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission_role")));
                    return true;
                }
                try {
                    Integer.valueOf(args[2]);
                } catch (Exception e) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.transfer_amount")));
                    return true;
                }
                if (Clan.getCash(clanName) < Integer.valueOf(args[2])) {
                    sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_transfer_money") + args[2]));
                    return true;
                }
                if (!addEconomy(player, args[2])) {
                    return true;
                }
                Clan.setCash(clanName, String.valueOf(Clan.getCash(clanName) - Integer.valueOf(args[2])));
                sender.sendMessage(String.format(Utility.hex(prefix + Utility.lang(sender,"commands.cashremove.message.msg")), args[2]));
                return true;
            }
        } else if (args[0].equals("top")) {
            if (!sender.hasPermission("fc.top")) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_permission")));
                return true;
            } else if (Clan.getlistClans().size() == 0) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_created_clans")));
                return true;
            }
            topCMD.msgSorting(sender, args);
            return true;
        } else if (args[0].equals("stats")) {
            if (!checkSmall(sender, memberName, clanName, "stats")) {
                return true;
            } else if (Clan.getlistClans().size() == 0) {
                sender.sendMessage(Utility.hex(prefix + Utility.lang(sender,"common_errors.no_created_clans")));
                return true;
            }
            topCMD.msgSorting(sender, args);
            return true;
        }
        help(sender, clanName, memberName, "0");
        return true;
    }
}
