package ru.oshifugo.functionalclans.sql;

import ru.oshifugo.functionalclans.Main;
import ru.oshifugo.functionalclans.utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class Clan {
    public static String getClanRealName(String clanName) {
      String s = SQLiteUtility.clans.get(clanName)[0];
        return s;
    }
    public static String getClanNameUID(String UID) {
        String s = "";
        ArrayList<String> clansList = getlistClans();
        for (int i = 0; i < Integer.valueOf(getCountClan()); i++) {
            if (getUID(clansList.get(i)).equalsIgnoreCase(UID)) {
                s = clansList.get(i);
                i = Integer.valueOf(getCountClan());
            }
        }
        return s;
    }
    public static void togglePVP(String clanName) {
        setPVP(clanName, !getPVP(clanName));
    }
    public static boolean getPVP(String clanName) {
        return Objects.equals(SQLiteUtility.clans.get(clanName)[20], "1");
    }
    public static String getLeader(String clanName) {
        return SQLiteUtility.clans.get(clanName)[1];
    }
    public static Integer getCash(String clanName) {
        return Integer.valueOf(SQLiteUtility.clans.get(clanName)[2]);
    }
    public static Integer getRating(String clanName) {
        return Integer.valueOf(SQLiteUtility.clans.get(clanName)[3]);
    }
    public static Integer getType(String clanName) {
        return Integer.valueOf(SQLiteUtility.clans.get(clanName)[4]);
    }
    public static Integer getTax(String clanName) {
        return Integer.valueOf(SQLiteUtility.clans.get(clanName)[5]);
    }
    public static String getStatus(String clanName) {
        return SQLiteUtility.clans.get(clanName)[6];
    }
    public static String getSocial(String clanName) {
        return SQLiteUtility.clans.get(clanName)[7];
    }
    public static boolean getVerification(String clanName) {
        return Boolean.valueOf(SQLiteUtility.clans.get(clanName)[8]);
    }
    public static Integer getMax_player(String clanName) {
        return Integer.valueOf(SQLiteUtility.clans.get(clanName)[9]);
    }
    public static String getMessage(String clanName) {
        String s = SQLiteUtility.clans.get(clanName)[10];
        if (Objects.equals(s, "null")) return "";
        return s;
    }
    public static String getWorld(String clanName) {
        String s = SQLiteUtility.clans.get(clanName)[11];
        return s;
    }
    public static Float getX(String clanName) {
        Float i = Float.valueOf(SQLiteUtility.clans.get(clanName)[12]);
        return i;
    }
    public static Float getY(String clanName) {
        Float i = Float.valueOf(SQLiteUtility.clans.get(clanName)[13]);
        return i;
    }
    public static Float getZ(String clanName) {
        Float i = Float.valueOf(SQLiteUtility.clans.get(clanName)[14]);
        return i;
    }
    public static Float getXcur(String clanName) {
        Float i = Float.valueOf(SQLiteUtility.clans.get(clanName)[15]);
        return i;
    }
    public static Float getYcur(String clanName) {
        Float i = Float.valueOf(SQLiteUtility.clans.get(clanName)[16]);
        return i;
    }
    public static Location getHome(String clanName) {
        return new Location(Bukkit.getWorld(getWorld(clanName)), getX(clanName), getY(clanName), getZ(clanName), getXcur(clanName), getYcur(clanName));
    }
    public static String getDate(String clanName) {
        String s = SQLiteUtility.clans.get(clanName)[17];
        return s;
    }

    public static String getUID(String clanName) {
        String s = SQLiteUtility.clans.get(clanName)[18];
        return s;
    }
    public static String getCreator(String clanName) {
        String s = SQLiteUtility.clans.get(clanName)[19];
        return s;
    }
    public static ArrayList<String> getlistClans() {
        ArrayList<String> listClans = new ArrayList<>();
        for(String keys: SQLiteUtility.clans.keySet()) {
            listClans.add(keys);
        }
        return listClans;
    }
    public static ArrayList<String> getlistUID() {
        ArrayList<String> listClans = getlistClans();
        ArrayList<String> listUID = new ArrayList<>();
        for(int i = 0; i < listClans.size(); i++) {
            listUID.add(getUID(listClans.get(i)));
        }
        return listUID;
    }
    public static String getCountClan() {
        return String.valueOf(getlistClans().size());
    }
    public static String getRoleName(String clanName, Integer rank) {
        if (rank == 5) { return utility.hex(utility.lang(Main.instance.getServer().getPlayer(clanName), "main.leader")); }
        return SQLiteUtility.clan_role.get(clanName + "_" + rank)[1];
    }
    public static Integer getRank(String memberName) {
        return Integer.valueOf(SQLiteUtility.members.get(memberName)[1]);
    }
    public static boolean hasHome(String clanName) {
        return getX(clanName) != 0.0 || getY(clanName) != 0.0 || getZ(clanName) != 0.0 || getXcur(clanName) != 0.0F || getYcur(clanName) != 0.0F;
    }
    public static boolean hasUID(String cUID) {
        ArrayList<String> listUID = new ArrayList<>();
        for (int i = 0; i < getlistClans().size(); i++) {
            listUID.add(getUID(getlistClans().get(i)));
        }
        for(String keys: listUID) {
            if (keys.equalsIgnoreCase(cUID)) { return false; }
        }
        return true;
    }
    public static boolean hasClanName(String clanName) {
        for(String keys: getlistClans()) {
            if (keys.toLowerCase().equalsIgnoreCase(clanName.toLowerCase())) { return false; }
        }
        return true;
    }
    public static boolean hasRole(String clanName, Integer rank, Integer role) {
        if (rank == 5) {return true;}
        if (SQLiteUtility.clan_role.get(clanName + "_" + rank)[role].equalsIgnoreCase("1") || SQLiteUtility.clan_role.get(clanName + "_" + rank)[role].equalsIgnoreCase("true")) {
            return true;
        } else return false;
    }
    public static void setRole(String clanName, Integer rank, Integer role) {
        Boolean bool = !hasRole(clanName, rank, role);
        SQLiteUtility.clan_role.get(clanName + "_" + rank)[role] = bool.toString();
        String roleName = null;
        if (role == 2) {
            roleName = "kick";
        } else if (role == 3) {
            roleName = "invite";
        } else if (role == 4) {
            roleName = "cash_add";
        } else if (role == 5) {
            roleName = "cash_remove";
        } else if (role == 6) {
            roleName = "rmanage";
        } else if (role == 7) {
            roleName = "chat";
        } else if (role == 8) {
            roleName = "msg";
        } else if (role == 9) {
            roleName = "alliance_add";
        } else if (role == 10) {
            roleName = "alliance_remove";
        }
        assert roleName != null;
        SQLite.execute("UPDATE clan_permissions SET {role} = ? WHERE clan = ? AND role = ?"
                .replace("{role}", roleName),
                String.valueOf(bool), clanName, String.valueOf(rank));

    }
    public static void setMessage(String clanName, String message) {
        SQLiteUtility.clans.get(clanName)[10] = message;
        SQLite.execute("UPDATE clan_list SET message = ? WHERE name = ?", message, clanName);
    }
    public static void setSocial(String clanName, String message) {
        SQLiteUtility.clans.get(clanName)[7] = String.valueOf(message);
        SQLite.execute("UPDATE clan_list SET social = ? WHERE name = ?", message, clanName);
    }
    public static void setStatus(String clanName, String message) {
        SQLiteUtility.clans.get(clanName)[6] = message;
        SQLite.execute("UPDATE clan_list SET status = ? WHERE name = ?", message, clanName);

    }
    public static void setMax_player(String clanName, String count) {
        SQLiteUtility.clans.get(clanName)[9] = count;
        SQLite.execute("UPDATE clan_list SET max_player = ? WHERE name = ?", count, clanName);
    }
    public static void setVerification(String clanName, String bool) {
        SQLiteUtility.clans.get(clanName)[8] = bool;
        SQLite.execute("UPDATE clan_list SET verification = ? WHERE name = ?", bool, clanName);
    }
    public static void setCash (String clanName, String cash) {
        SQLiteUtility.clans.get(clanName)[2] = cash;
        SQLite.execute("UPDATE clan_list SET cash = ? WHERE name = ?", cash, clanName);

    }

    public static void setHome(String clanName, String world, double x, double y, double z, float xcur, float ycur) {
        SQLiteUtility.clans.get(clanName)[11] = world;
        SQLiteUtility.clans.get(clanName)[12] = String.valueOf(x);
        SQLiteUtility.clans.get(clanName)[13] = String.valueOf(y);
        SQLiteUtility.clans.get(clanName)[14] = String.valueOf(z);
        SQLiteUtility.clans.get(clanName)[15] = String.valueOf(xcur);
        SQLiteUtility.clans.get(clanName)[16] = String.valueOf(ycur);
        SQLite.execute("UPDATE clan_list SET world='" + world + "', x='" + x + "', y='" + y + "', z='" + z + "', xcur='" + xcur + "', ycur='" + ycur + "' WHERE name='" + clanName + "'");
    }
    public static void setType(String clanName, Integer type) {
        SQLiteUtility.clans.get(clanName)[4] = String.valueOf(type);
        SQLite.execute("UPDATE clan_list SET type = ? WHERE name = ?", String.valueOf(type), clanName);

    }
    public static void setPVP(String clanName, boolean isActive) {
        String raw = isActive ? "1" : "0";
        SQLiteUtility.clans.get(clanName)[20] = raw;
        SQLite.execute("UPDATE clan_list SET pvp = ? WHERE name = ?", raw, clanName);
    }
    public static void setLeader(String clanName, String newLeader) {
        String leader = getLeader(clanName);
        SQLiteUtility.clans.get(clanName)[1] = newLeader;
        SQLiteUtility.members.get(leader)[1] = "1";
        SQLiteUtility.members.get(newLeader)[1] = "5";
        SQLite.execute("UPDATE clan_list SET leader='" + newLeader + "' WHERE name='" + clanName + "'");
        SQLite.execute("UPDATE clan_members SET role='" + "1" + "' WHERE name='" + leader + "'");
        SQLite.execute("UPDATE clan_members SET role='" + "5" + "' WHERE name='" + newLeader + "'");

    }
    public static void setClanName(String clanName, String newName) {
        ArrayList<String> m_c = Member.getMembers(clanName);
        for (int i = 0; i < m_c.size(); i++) {
            SQLiteUtility.members.get(m_c.get(i))[2] = newName;
            SQLiteUtility.member_clan.put(m_c.get(i), newName);
            SQLite.execute("UPDATE clan_members SET clan = ? WHERE name = ?", newName, m_c.get(i));

        }
        for (int i = 0; i < 4; i++) {
            SQLiteUtility.clan_role.put(newName + "_" + String.valueOf(i + 1), SQLiteUtility.clan_role.get(clanName + "_" + String.valueOf(i + 1)));
            SQLite.execute("UPDATE clan_permissions SET clan = ? WHERE clan = ? AND role = ?", newName, clanName, String.valueOf(i + 1));

        }
        SQLiteUtility.clans.put(newName, SQLiteUtility.clans.get(clanName));
        SQLiteUtility.clans.remove(clanName);
        SQLiteUtility.clans.get(newName)[0] = newName;
        SQLite.execute("UPDATE clan_list SET name = ? WHERE name = ?", newName, clanName);
    }
    public static String createUID() {
        if (Integer.valueOf(getCountClan()) == 9000) { return null; }
        String UID;
        Random rand = new Random();
        UID = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10));
        if (hasUID(UID)) {
            return UID;
        } else {
            return createUID();
        }
    }
    public static void chat(String clanName, String memberName, String message) {
        String msg = "";
        for (int i = 0; i < Member.getMembers(clanName).size(); i++) {
            if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                Bukkit.getPlayer(Member.getMembers(clanName).get(i)).sendMessage(utility.hex("&f[&7@&f] " + Member.getRankName(clanName, memberName) + " &7[" + Bukkit.getPlayer(memberName).getName() + "&7]: &f" + message));
            }
        }
    }
    public static void msg(String clanName, String memberName, String message) {
        String msg = "";
        for (int i = 0; i < Member.getMembers(clanName).size(); i++) {
            if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                Bukkit.getPlayer(Member.getMembers(clanName).get(i)).sendMessage(utility.hex("&f[&4@&f] " + Member.getRankName(clanName, memberName) + " &7[" + Bukkit.getPlayer(memberName).getName() + "&7]: &a" + message));
            }
        }
    }
    public static void broadcast(String clanName, String message) {
        String msg = "";
        for (int i = 0; i < Member.getMembers(clanName).size(); i++) {
            if (Bukkit.getOfflinePlayer(Member.getMembers(clanName).get(i)).isOnline()) {
                Bukkit.getPlayer(Member.getMembers(clanName).get(i)).sendMessage(utility.hex("&f[&c@&f]: " + message));
            }
        }
    }
    public static void kick(String clanName, String player) {
        SQLiteUtility.member_clan.remove(player);
        SQLiteUtility.members.remove(player);
        SQLite.execute("DELETE FROM clan_members WHERE clan='" + clanName + "' AND name='" + player + "'");
    }
    public static void addrank(String player) {
        SQLiteUtility.members.get(player)[1] = String.valueOf(Integer.valueOf(SQLiteUtility.members.get(player)[1]) + 1);
        SQLite.execute("UPDATE clan_members SET role='" + SQLiteUtility.members.get(player)[1] + "' WHERE name='" + player + "'");
    }
    public static void removerank(String player) {
        SQLiteUtility.members.get(player)[1] = String.valueOf(Integer.valueOf(SQLiteUtility.members.get(player)[1]) - 1);
        SQLite.execute("UPDATE clan_members SET role='" + SQLiteUtility.members.get(player)[1] + "' WHERE name='" + player + "'");
    }

}
