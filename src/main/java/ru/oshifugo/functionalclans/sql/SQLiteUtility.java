package ru.oshifugo.functionalclans.sql;

import ru.oshifugo.functionalclans.Main;
import ru.oshifugo.functionalclans.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SQLiteUtility {
    public static HashMap<String, String[]> clans = new HashMap();
    public static HashMap<String, String[]> members = new HashMap<>();
    public static HashMap<String, String> member_clan = new HashMap<>();
    public static HashMap<String, String[]> clan_role = new HashMap<>();
    public static HashMap<String, String[]> clan_alliance = new HashMap<>();

    public static String[] getClanByName(String player) {
        String[] q = {members.get(player)[0].toLowerCase(), members.get(player)[2]};
        utility.debug("getClanByName -> memberName: " + q[0] + " clanName: " + q[1]);
        return q;
    }
    public static void create(String clan, String leader) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        String d = formater.format(date.getTime());
        String uid;
        if (Clan.createUID() != null) {
            uid = Clan.createUID();
        } else return;
        String[] c = {clan, leader, utility.config("cash"), utility.config("rating"), utility.config("type"), utility.config("tax"), "&7╰(*´︶`*)╯&c♡", "null", String.valueOf(utility.configBoolean("verification")), utility.config("max_player"), "null", "null", String.valueOf(0.0), String.valueOf(0.0), String.valueOf(0.0), String.valueOf(0.0F), String.valueOf(0.0F), d, uid, leader};
        String[] m = {leader, "5", clan, "0", "0", "0", "0"};
        String[] role_1 = {"1", utility.config("role_1"), String.valueOf(utility.configBoolean("kick_default")), String.valueOf(utility.configBoolean("invite_default")), String.valueOf(utility.configBoolean("cash_add_default")), String.valueOf(utility.configBoolean("cash_remove_default")), String.valueOf(utility.configBoolean("rmanage_default")), String.valueOf(utility.configBoolean("chat_default")), String.valueOf(utility.configBoolean("msg_default")), String.valueOf(utility.configBoolean("alliance_add_default")), String.valueOf(utility.configBoolean("alliance_remove_default"))};
        String[] role_2 = {"2", utility.config("role_2"), String.valueOf(utility.configBoolean("kick_default")), String.valueOf(utility.configBoolean("invite_default")), String.valueOf(utility.configBoolean("cash_add_default")), String.valueOf(utility.configBoolean("cash_remove_default")), String.valueOf(utility.configBoolean("rmanage_default")), String.valueOf(utility.configBoolean("chat_default")), String.valueOf(utility.configBoolean("msg_default")), String.valueOf(utility.configBoolean("alliance_add_default")), String.valueOf(utility.configBoolean("alliance_remove_default"))};
        String[] role_3 = {"3", utility.config("role_3"), String.valueOf(utility.configBoolean("kick_default")), String.valueOf(utility.configBoolean("invite_default")), String.valueOf(utility.configBoolean("cash_add_default")), String.valueOf(utility.configBoolean("cash_remove_default")), String.valueOf(utility.configBoolean("rmanage_default")), String.valueOf(utility.configBoolean("chat_default")), String.valueOf(utility.configBoolean("msg_default")), String.valueOf(utility.configBoolean("alliance_add_default")), String.valueOf(utility.configBoolean("alliance_remove_default"))};
        String[] role_4 = {"4", utility.config("role_4"), String.valueOf(utility.configBoolean("kick_default")), String.valueOf(utility.configBoolean("invite_default")), String.valueOf(utility.configBoolean("cash_add_default")), String.valueOf(utility.configBoolean("cash_remove_default")), String.valueOf(utility.configBoolean("rmanage_default")), String.valueOf(utility.configBoolean("chat_default")), String.valueOf(utility.configBoolean("msg_default")), String.valueOf(utility.configBoolean("alliance_add_default")), String.valueOf(utility.configBoolean("alliance_remove_default"))};
        clans.put(clan, c);
        members.put(leader, m);
        member_clan.put(leader, clan);
        clan_role.put(clan + "_1", role_1);
        clan_role.put(clan + "_2", role_2);
        clan_role.put(clan + "_3", role_3);
        clan_role.put(clan + "_4", role_4);
        SQLite.execute("INSERT INTO clan_list (name, leader, cash, rating, type, tax, status, social, verification, max_player, message, world, x, y, z, xcur, ycur, date, uid, creator) VALUES ('" + clan + "', '" + leader + "', '" + utility.config("cash") + "', '" + utility.config("rating") + "', '" + utility.config("type") + "', '" + utility.config("tax") + "', '" + "&7╰(*´︶`*)╯&c♡" + "', 'null', '" + utility.configBoolean("verification") + "', '" + utility.config("max_player") + "', 'null', 'null', '0', '0', '0', '0', '0', '" + d + "', '" + uid + "', '" + leader + "')");
        SQLite.execute("INSERT INTO clan_members (name, role, clan, kills, death, quest, rating) VALUES ('" + leader + "', '5', '" + clan + "', '0', '0', '0', '0')");
        SQLite.execute("INSERT INTO clan_permissions (clan, role, role_name, kick, invite, cash_add, cash_remove, rmanage, chat, msg, alliance_add, alliance_remove) VALUES ('" + clan +"', '1', '" + utility.config("role_1") +"', '" + utility.configBoolean("kick_default") +"', '" + utility.configBoolean("invite_default") +"', '" + utility.configBoolean("cash_add_default") +"', '" + utility.configBoolean("cash_remove_default") +"', '" + utility.configBoolean("rmanage_default") +"', '" + utility.configBoolean("chat_default") +"', '" + utility.configBoolean("msg_default") +"', '" + utility.configBoolean("alliance_add_default") +"', '" + utility.configBoolean("alliance_remove_default") +"')");
        SQLite.execute("INSERT INTO clan_permissions (clan, role, role_name, kick, invite, cash_add, cash_remove, rmanage, chat, msg, alliance_add, alliance_remove) VALUES ('" + clan +"', '2', '" + utility.config("role_2") +"', '" + utility.configBoolean("kick_default") +"', '" + utility.configBoolean("invite_default") +"', '" + utility.configBoolean("cash_add_default") +"', '" + utility.configBoolean("cash_remove_default") +"', '" + utility.configBoolean("rmanage_default") +"', '" + utility.configBoolean("chat_default") +"', '" + utility.configBoolean("msg_default") +"', '" + utility.configBoolean("alliance_add_default") +"', '" + utility.configBoolean("alliance_remove_default") +"')");
        SQLite.execute("INSERT INTO clan_permissions (clan, role, role_name, kick, invite, cash_add, cash_remove, rmanage, chat, msg, alliance_add, alliance_remove) VALUES ('" + clan +"', '3', '" + utility.config("role_3") +"', '" + utility.configBoolean("kick_default") +"', '" + utility.configBoolean("invite_default") +"', '" + utility.configBoolean("cash_add_default") +"', '" + utility.configBoolean("cash_remove_default") +"', '" + utility.configBoolean("rmanage_default") +"', '" + utility.configBoolean("chat_default") +"', '" + utility.configBoolean("msg_default") +"', '" + utility.configBoolean("alliance_add_default") +"', '" + utility.configBoolean("alliance_remove_default") +"')");
        SQLite.execute("INSERT INTO clan_permissions (clan, role, role_name, kick, invite, cash_add, cash_remove, rmanage, chat, msg, alliance_add, alliance_remove) VALUES ('" + clan +"', '4', '" + utility.config("role_4") +"', '" + utility.configBoolean("kick_default") +"', '" + utility.configBoolean("invite_default") +"', '" + utility.configBoolean("cash_add_default") +"', '" + utility.configBoolean("cash_remove_default") +"', '" + utility.configBoolean("rmanage_default") +"', '" + utility.configBoolean("chat_default") +"', '" + utility.configBoolean("msg_default") +"', '" + utility.configBoolean("alliance_add_default") +"', '" + utility.configBoolean("alliance_remove_default") +"')");
    }
    public static void delete(String clan) {
        ArrayList<String> mem = Member.getMembers(clan);
        for (int i = 0; i < mem.size(); i++) {
            members.remove(mem.get(i));
            member_clan.remove(mem.get(i));
            SQLite.execute("DELETE FROM clan_members WHERE name='" + mem.get(i) + "'");
        }
        SQLite.execute("DELETE FROM clan_list WHERE name='" + clan + "'");
        SQLite.execute("DELETE FROM clan_permissions WHERE clan='" + clan + "'");
        clan_role.remove(clan);
        clans.remove(clan);
    }
    public static void update() {
        clans = new HashMap<>();
        members = new HashMap<>();
        member_clan = new HashMap<>();
        clan_role = new HashMap<>();
        clan_alliance = new HashMap<>();
        InviteRequest.request = new HashMap<>();
        SQLite.getClans();

        Main.placeholders_config = new HashMap<>(); // Неработает, конфиг тот же
        Main.HashConfig(); // Неработает, конфиг тот же
    }
}
