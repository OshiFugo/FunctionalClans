package ru.oshifugo.functionalclans.sql;

import java.util.HashMap;

public class InviteRequest {
    public static HashMap<String, String> request = new HashMap();
    public static void addRequest(String clanName, String player) {
        request.put(player, Clan.getUID(clanName));
    }
    public static void acceptRequest(String player) {
        String name = Clan.getClanNameUID(InviteRequest.request.get(player));
        String[] member = {player, "1", name, "0", "0", "0", "0", "0"};
        SQLiteUtility.members.put(player, member);
        SQLiteUtility.member_clan.put(player, name);
        SQLite.execute("INSERT INTO clan_members (id, name, role, clan, kills, death, quest, rating) VALUES ('" + InviteRequest.request.get(player) + "', '" + player + "', '1', '" + name + "', '0', '0', '0', '0')");
        request.remove(player);
    }
    public static void denyRequest(String player) {
        request.remove(player);
    }

}