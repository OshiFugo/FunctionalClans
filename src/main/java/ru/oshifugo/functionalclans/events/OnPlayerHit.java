package ru.oshifugo.functionalclans.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.util.EventListener;

public class OnPlayerHit implements EventListener, Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity whoDamage = event.getEntity();
        if (!(damager instanceof Player) || !(whoDamage instanceof Player)) return;
        Player p1 = (Player) damager;
        Player p2 = (Player) whoDamage;
        String clanName = Member.getClan(p1.getName());
        if (!clanName.equals(Member.getClan(p2.getName()))) return;
        if (Clan.getPVP(clanName)) return;
        event.setCancelled(true);
    }
}
