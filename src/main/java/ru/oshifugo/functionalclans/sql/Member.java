package ru.oshifugo.functionalclans.sql;

import org.bukkit.Bukkit;
import java.util.*;

public class Member {

    public static <K, V> Set<K> getKeys(Map<K, V> map, V value) {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> entry: map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
    public static Integer getCount(String clanName){
        Integer count = getKeys(SQLiteUtility.member_clan, clanName).size();
        return count;
    }
    public static Integer getOnlineCount(String clanName){
        ArrayList<String> mem = getMembers(clanName);
        int count = 0;
        for (int i = 0; i < mem.size(); i++) {
            if (Bukkit.getOfflinePlayer(mem.get(i)).isOnline()) {
                count++;
            }
        }
        return count;
    }
    public static ArrayList<String> getMembers(String clanName) {
       ArrayList<String> getmembers = new ArrayList<>(getKeys(SQLiteUtility.member_clan, clanName));
       return getmembers;
    }
    public static String getRank(String memberName) {
        String rank = SQLiteUtility.members.get(Bukkit.getOfflinePlayer(memberName).getName())[1];
        return rank;
    }
    public static String getRankName(String clanName, String MEMBERName) {
        String rankName = Clan.getRoleName(clanName, Integer.valueOf(getRank(MEMBERName)));
        return rankName;
    }
    public static boolean hasClan(String clanName, String player) {
        if (SQLiteUtility.member_clan.get(player) != null) {
            if (SQLiteUtility.member_clan.get(player).equalsIgnoreCase(clanName)) {
                return true;
            }
        }
        return false;
    }
    public static String getKills(String MEMBERName) {
        return SQLiteUtility.members.get(MEMBERName)[3];
    }
    public static String getDeaths(String MEMBERName) {
        return SQLiteUtility.members.get(MEMBERName)[4];
    }
    public static String getKDR(String MEMBERName) {
        int kills = Integer.valueOf(getKills(MEMBERName));
        int deaths = Integer.valueOf(getDeaths(MEMBERName));

        if (kills > 0 && deaths == 0) {
            return "100";
        } else if (kills == 0 && deaths == 0) {
            return "0";
        } else {
            return String.valueOf(kills/deaths);
        }
    }
    public static String getRating(String MEMBERName) {
        return SQLiteUtility.members.get(MEMBERName)[6];
    }
    public static String getClan(String MEMBERName) {
        return SQLiteUtility.member_clan.get(MEMBERName);
    }


}
