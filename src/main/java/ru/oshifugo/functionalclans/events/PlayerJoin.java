package ru.oshifugo.functionalclans.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.util.EventListener;

public class PlayerJoin implements EventListener, Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String clanName = Member.getClan(p.getName());
        if (clanName == null) return;
        String message = Clan.getMessage(clanName);
        if (message == null) return;
        p.sendMessage(message);
    }
}
