package ru.oshifugo.functionalclans.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.oshifugo.functionalclans.sql.*;
import ru.oshifugo.functionalclans.utility;

public class Kill implements Listener {

    static String prefix = utility.config("prefix");

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        String userClan = Member.getClan(event.getEntity().getName());
        String killerClan = Member.getClan(event.getEntity().getKiller().getName());
        if (userClan == null || killerClan == null) {
            return;
        }
        if (userClan.equalsIgnoreCase(killerClan)) {
            return;
        }
        if (!Ally.hasAlly(Clan.getUID(userClan), Clan.getUID(killerClan))) {
            if (Clan.getVerification(userClan) == true && Clan.getVerification(killerClan) == true) {
                    SQLiteUtility.members.get(event.getEntity().getName())[4] = String.valueOf(Integer.valueOf(SQLiteUtility.members.get(event.getEntity().getName())[4]) + 1);
                    SQLiteUtility.members.get(event.getEntity().getKiller().getName())[3] = String.valueOf(Integer.valueOf(SQLiteUtility.members.get(event.getEntity().getKiller().getName())[3]) + 1);
                    SQLite.execute("UPDATE clan_members SET death='" + SQLiteUtility.members.get(event.getEntity().getName())[4] + "' WHERE name='" + event.getEntity().getName() + "'");
                    SQLite.execute("UPDATE clan_members SET kills='" + SQLiteUtility.members.get(event.getEntity().getKiller().getName())[3] + "' WHERE name='" + event.getEntity().getKiller().getName() + "'");
                    if (Clan.getRating(userClan) > Integer.valueOf(utility.config("min_rating"))) {
                        SQLiteUtility.clans.get(userClan)[3] = String.valueOf(Integer.valueOf(SQLiteUtility.clans.get(userClan)[3]) - Integer.valueOf(utility.config("kill_member")));
                        SQLiteUtility.clans.get(killerClan)[3] = String.valueOf(Integer.valueOf(SQLiteUtility.clans.get(killerClan)[3]) + Integer.valueOf(utility.config("kill_member")));
                        SQLite.execute("UPDATE clan_list SET rating='" + SQLiteUtility.clans.get(userClan)[3] + "' WHERE name='" + userClan + "'");
                        SQLite.execute("UPDATE clan_list SET rating='" + SQLiteUtility.clans.get(killerClan)[3] + "' WHERE name='" + killerClan + "'");
                        event.getEntity().getPlayer().sendMessage(String.format(utility.hex(prefix + utility.lang(event.getEntity(),"events.kill.msg")), "-" + Integer.valueOf(utility.config("kill_member"))));
                        event.getEntity().getKiller().getPlayer().sendMessage(String.format(utility.hex(prefix + utility.lang(event.getEntity(),"events.kill.msg")), "+" + Integer.valueOf(utility.config("kill_member"))));
                    } else {
                        Clan.setVerification(userClan, "false");
                        SQLiteUtility.clans.get(killerClan)[3] = String.valueOf(Integer.valueOf(SQLiteUtility.clans.get(killerClan)[3]) + Integer.valueOf(utility.config("kill_member")));
                        SQLite.execute("UPDATE clan_list SET rating='" + SQLiteUtility.clans.get(killerClan)[3] + "' WHERE name='" + killerClan + "'");
                        event.getEntity().getPlayer().sendMessage(String.format(utility.hex(prefix + utility.lang(event.getEntity(),"events.kill.msg")), "-" + Integer.valueOf(utility.config("kill_member"))));
                        event.getEntity().getKiller().getPlayer().sendMessage(String.format(utility.hex(prefix + utility.lang(event.getEntity(),"events.kill.msg")), "+" + Integer.valueOf(utility.config("kill_member"))));
                        Clan.broadcast(userClan, utility.lang(event.getEntity(),"events.kill.msg1"));
                    }
            }
        }

    }
}
