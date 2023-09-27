package ru.oshifugo.functionalclans.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Ally {

    public static HashMap<String, String> AllyRequest = new HashMap<>();

    public static ArrayList<String> clan_ally(String UID) {
        ArrayList<String> alliance = new ArrayList<>();
        Set<String> setKeys = SQLiteUtility.clan_alliance.keySet();
        for(String k: setKeys) {
            if (SQLiteUtility.clan_alliance.get(k)[0].equalsIgnoreCase(UID)) {
                alliance.add(SQLiteUtility.clan_alliance.get(k)[1]);
            } else if (SQLiteUtility.clan_alliance.get(k)[1].equalsIgnoreCase(UID)) {
                alliance.add(SQLiteUtility.clan_alliance.get(k)[0]);
            }
        }
        return alliance;
    }
    public static boolean addAlly(String clanUID, String UID) {
        if (AllyRequest.get(UID) != null) {
            if (AllyRequest.get(UID).equalsIgnoreCase(clanUID)) {
                SQLiteUtility.clan_alliance.put(UID + "_" + clanUID, new String[]{UID, clanUID});
                SQLite.execute("INSERT INTO clan_alliance (UID_1, UID_2) VALUES ('" + UID + "','" + clanUID + "')");
                AllyRequest.remove(UID);
                return true;
            }
        }
        AllyRequest.put(clanUID, UID);
        return false;
    }
    public static void removeAlly(String clanUID, String UID) {
        SQLiteUtility.clan_alliance.remove(UID + "_" + clanUID);
        SQLiteUtility.clan_alliance.remove(clanUID + "_" + UID);
        SQLite.execute("DELETE FROM clan_alliance WHERE UID_1='" + UID + "' AND UID_2='" + clanUID + "'");
        SQLite.execute("DELETE FROM clan_alliance WHERE UID_1='" + clanUID + "' AND UID_2='" + UID + "'");
    }
    public static boolean hasAlly(String clanUID, String UID) {
        ArrayList<String> alliance = clan_ally(clanUID);
        for (int i = 0; i < alliance.size(); i++) {
            if (alliance.get(i).equalsIgnoreCase(UID)) { return true; }
        }
        return false;
    }

}
