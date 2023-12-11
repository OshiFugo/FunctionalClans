package ru.oshifugo.functionalclans.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.Main;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.util.EventListener;

public class PlayerJoin implements EventListener, Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        FileConfiguration config = Main.instance.getConfig();
        if (!config.getBoolean("welcome-message.allow")) {
            return;
        }
        Player p = e.getPlayer();
        String clanName = Member.getClan(p.getName());
        if (clanName == null) return;
        String message = Clan.getMessage(clanName);
        if (message == null)  return;
        if (message.isEmpty()) return;
        message = message.replace("&", "§");
        if (config.getBoolean("welcome-message.placeholders")) {
            message = PlaceholderAPI.setPlaceholders(p, message);
        }
        p.sendMessage(message);
    }
}
