package ru.oshifugo.functionalclans.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.oshifugo.functionalclans.FunctionalClans;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        FileConfiguration config = FunctionalClans.getInstance().getConfig();
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
