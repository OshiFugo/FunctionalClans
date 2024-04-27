package ru.oshifugo.functionalclans.listener;

import me.ford.salarymanager.OnSalaryEvent;
import me.ford.salarymanager.SalaryReportPaymentsEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;

import java.util.HashMap;
import java.util.Map;

public class SalaryListener implements Listener {

    private final Map<String, Integer> clansTaxes = new HashMap<>();

    @EventHandler
    private void onSalaryEvent(OnSalaryEvent event) {
        String clanName = Member.getClan(event.getPlayer().getName());
        if (clanName == null) return;
        int tax = Clan.getTax(clanName);
        if (tax == 0) return;
        double clanTaxes = event.getAmount() / 100 * tax;
        event.setAmount(event.getAmount() - clanTaxes);
        event.setShouldSendMessage(false);
        int clanCurrentCash = Clan.getCash(clanName);
        Clan.setCash(clanName, String.valueOf(clanCurrentCash + (int) clanTaxes));
        addTax(clanName, (int)clanTaxes);
//          "§cВы заработали §b%s₴. §cНалог клана: §b%d₴. §cИтого вы получаете: §a%d₴!",
        GUITranslatePlaceholder t = GUITranslate.getTranslate(event.getPlayer());

        event.getPlayer().sendMessage(
                t.get("salary-event.on-salary")
                        .replace("{earn}", String.valueOf((int) event.getAmount()))
                        .replace("{tax}", String.valueOf(tax))
                        .replace("{total}", String.valueOf((int) event.getAmount())));
    }

    @EventHandler
    //    §cВ казну клана §b%s§c упало: §b%d₴
    private void onSalaryReportPaymentsEvent(SalaryReportPaymentsEvent event) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String clanName = Member.getClan(player.getName());
            if (clanName == null) continue;
            if (!clansTaxes.containsKey(clanName)) continue;
            int tax = clansTaxes.get(clanName);
            if (tax == 0) continue;
            GUITranslatePlaceholder t = GUITranslate.getTranslate(player);
            player.sendMessage(t.get("salary-event.on-report")
                    .replace("{clan}", clanName)
                    .replace("{money}", String.valueOf(tax)));
        }
        clearTaxes();
    }

    private void addTax(String clan, int tax) {
        int clanTaxesNow = 0;
        if (clansTaxes.containsKey(clan)){
            clanTaxesNow = clansTaxes.get(clan);
        }
        clansTaxes.put(clan, clanTaxesNow + tax);
    }

    private void clearTaxes() {
        clansTaxes.clear();
    }

}

