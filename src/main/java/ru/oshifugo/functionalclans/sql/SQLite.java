package ru.oshifugo.functionalclans.sql;

import ru.oshifugo.functionalclans.Main;
import ru.oshifugo.functionalclans.utility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;

public class SQLite {
    public static Connection connection = null;
    public static ResultSet resultSet = null;

    public static void connect() {
        try {
            if (!Main.instance.getDataFolder().mkdirs()) {
                Main.instance.getDataFolder().mkdirs();
            }
            Class.forName("org.sqlite.JDBC").getConstructor().newInstance();
            String defaultPVP = Main.instance.getConfig().getString("default-pvp");
            connection = DriverManager.getConnection("jdbc:sqlite://" + Main.instance.getDataFolder().getAbsolutePath() + "/clans.db");
            execute("CREATE TABLE IF NOT EXISTS `clan_list` (`id` INTEGER PRIMARY KEY,`name` varchar(255) NOT NULL,`leader` varchar(255) NOT NULL,`cash` varchar(255) NOT NULL,`rating` varchar(255) NOT NULL,`type` varchar(255) NOT NULL,`tax` varchar(255) NOT NULL,`status` varchar(255) NOT NULL,`social` varchar(255) NOT NULL,`verification` varchar(255) NOT NULL,`max_player` varchar(255) NOT NULL,`message` varchar(255) NOT NULL,`world` varchar(255) NOT NULL,`x` varchar(255) NOT NULL,`y` varchar(255) NOT NULL,`z` varchar(255) NOT NULL,`xcur` varchar(255) NOT NULL,`ycur` varchar(255) NOT NULL,`date` varchar(255) NOT NULL,`uid` varchar(255) NOT NULL,`creator` varchar(255) NOT NULL, `pvp` varchar(255) NOT NULL DEFAULT " + defaultPVP  + ")");
            execute("CREATE TABLE IF NOT EXISTS `clan_members` (`id` INTEGER PRIMARY KEY,`name` varchar(255) NOT NULL,`role` varchar(255) NOT NULL,`clan` varchar(255) NOT NULL,`kills` varchar(255) NOT NULL,`death` varchar(255) NOT NULL,`quest` varchar(255) NOT NULL,`rating` varchar(255) NOT NULL)");
            execute("CREATE TABLE IF NOT EXISTS `clan_permissions` (`id` INTEGER PRIMARY KEY,`clan` varchar(255) NOT NULL,`role` varchar(255) NOT NULL,`role_name` varchar(255) NOT NULL,`kick` boolean NOT NULL,`invite` boolean NOT NULL,`cash_add` boolean NOT NULL,`cash_remove` boolean NOT NULL,`rmanage` boolean NOT NULL,`chat` boolean NOT NULL,`msg` boolean NOT NULL,`alliance_add` boolean NOT NULL,`alliance_remove` boolean NOT NULL)");
            execute("CREATE TABLE IF NOT EXISTS 'clan_alliance' ('id' INTEGER PRIMARY KEY, 'UID_1' varchar(255) NOT NULL, 'UID_2' varchar(255) NOT NULL)");

//            update alters
            update2p1p0();


        } catch (Exception e) {
            utility.error("An error occurred while connecting to the database.");
        }
    }

    private static void update2p1p0() throws IOException {
        if (Main.instance.getDBVersion() < 2) {
            execute("ALTER TABLE clan_list ADD COLUMN pvp varchar(255) DEFAULT '1'");
            Main.instance.setDBVersion(2);
        }
    }

    public static boolean hasConnected() {
        try {
            return !connection.isClosed();
        } catch (Exception var1) {
            return false;
        }
    }
    public static void execute(String query) {
        if (!hasConnected()) {
            connect();
        }
        utility.debug("Sended Query: " + query);
        try {
            connection.createStatement().execute(query);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
    public static ResultSet executeQuery(String query) {
        if (!hasConnected()) {
            connect();
        }
        utility.debug("Sended Query: " + query);
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery(query);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return rs;
    }
    public static void getClans() {
        try {
            int a, b, p, d;
            try {
                resultSet = executeQuery("SELECT * FROM clan_list");
                for (a = 0; resultSet.next(); a++) {
                    String[] clan = {resultSet.getString("name"), resultSet.getString("leader"), resultSet.getString("cash"), resultSet.getString("rating"), resultSet.getString("type"), resultSet.getString("tax"), resultSet.getString("status"), resultSet.getString("social"), resultSet.getString("verification"), resultSet.getString("max_player"), resultSet.getString("message"), resultSet.getString("world"), resultSet.getString("x"), resultSet.getString("y"), resultSet.getString("z"), resultSet.getString("xcur"), resultSet.getString("ycur"), resultSet.getString("date"), resultSet.getString("uid"), resultSet.getString("creator"), resultSet.getString("pvp")};
                    SQLiteUtility.clans.put(resultSet.getString("name"), clan);
                }
            } catch (Exception errors) {
                a = -1;
                utility.error("getClans -> SELECT * FROM clan_list");
            }
            try {
                resultSet = executeQuery("SELECT * FROM clan_members");
                for (b = 0; resultSet.next(); b++) {
                    String[] m = {resultSet.getString("name"), resultSet.getString("role"), resultSet.getString("clan"), resultSet.getString("kills"), resultSet.getString("death"), resultSet.getString("quest"), resultSet.getString("rating")};
                    SQLiteUtility.members.put(resultSet.getString("name"), m);
                    SQLiteUtility.member_clan.put(resultSet.getString("name"), resultSet.getString("clan"));
                }
            } catch (Exception errors) {
                b = -1;
                utility.error("getClans -> SELECT * FROM clan_members");
            }
            try {
                resultSet = executeQuery("SELECT * FROM clan_permissions");
                for (p = 0; resultSet.next(); p++) {
                    String[] role = {resultSet.getString("role"), resultSet.getString("role_name"), resultSet.getString("kick"), resultSet.getString("invite"), resultSet.getString("cash_add"), resultSet.getString("cash_remove"), resultSet.getString("rmanage"), resultSet.getString("chat"), resultSet.getString("msg"), resultSet.getString("alliance_add"), resultSet.getString("alliance_remove")};

                    SQLiteUtility.clan_role.put(resultSet.getString("clan") + "_" + resultSet.getString("role"), role);
                }
            } catch (Exception errors) {
                p = -1;
                utility.error("getClans -> SELECT * FROM clan_permissions");
            }
            try {
                resultSet = executeQuery("SELECT * FROM clan_alliance");
                for (d = 0; resultSet.next(); d++) {
                    String[] alliance = {resultSet.getString("UID_1"), resultSet.getString("UID_2")};

                    SQLiteUtility.clan_alliance.put(resultSet.getString("UID_1") + "_" + resultSet.getString("UID_2"), alliance);
                }
            } catch (Exception errors) {
                d = -1;
                utility.error("getClans -> SELECT * FROM clan_alliance");
            }
            utility.debug("The clan base is loaded. (" + a + ")");
            utility.debug("The player base is loaded. (" + b + ")");
            utility.debug("The rank database is loaded. (" + p + ")");
            utility.debug("Alliance database is loading. (" + d + ")");
        } catch (Exception e) {
            utility.error("An error occurred while requesting clans.");
        }
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

}
