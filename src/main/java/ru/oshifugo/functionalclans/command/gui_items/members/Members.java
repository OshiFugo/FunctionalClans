package ru.oshifugo.functionalclans.command.gui_items.members;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.command.gui_items.Root;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

public class Members extends ItemsBase {


    private static ItemStack getHead(String playerName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getPlayer(playerName));
        skull.setItemMeta(skullMeta);
        return skull;
    }
//        } else if (params.equals("player_rating")) {
//        }
    public static ItemsBase getMember(OfflinePlayer player, ClanGUI ui) {
        String clanName = Member.getClan(player.getName());
        String online;

        if (player.isOnline()) {
            online = GUITranslate.getTranslate(player).get("members.online");
        } else {
            online = GUITranslate.getTranslate(player).get("members.offline");
        }
        return new Members(player, ui, player.getName(), getHead(player.getName()), player.getName())
                .setTranslatedLore("members.member")
                .replaceLore("{name}", player.getName())
                .replaceLore("{player_rank}", Member.getRankName(clanName, player.getName()))
                .replaceLore("{player_kills}", Member.getKills(player.getName()))
                .replaceLore("{player_deaths}", Member.getDeaths(player.getName()))
                .replaceLore("{player_kdr}", Member.getKDR(player.getName()))
                .replaceLore("{player_rating}", Member.getRating(player.getName()))
                .replaceLore("{online}", online)
                .replaceLore("&", "§");
    }
    public static void display(Player player, ClanGUI ui) {
        Item border = new SimpleItem(ui.getVoidFill());

        ui.gui = Gui.empty(9, 5);

        Window.single();
        String clanName = Member.getClan(player.getName());
        if (clanName == null) return;
        List<String> members = Member.getMembers(clanName);
        List<Item> players = new ArrayList<>();
        Collections.sort(members);

        for (String member : members) {
            players.add(getMember(Bukkit.getOfflinePlayer(member), ui));
        }
        ui.gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # +")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
                .addIngredient('#', border)
                .addIngredient('<', new BackItem(player))
                .addIngredient('>', new ForwardItem(player))
                .addIngredient('+', Root.go_back(ui, player))
                .setContent(players)
                .build();

        ui.display(GUITranslate.getTranslate(player).get("root.members.name"));
    }

    protected Members(OfflinePlayer player, ClanGUI ui, String id, ItemStack item, String name) {
        super(player, ui, id, item, name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        String clanName = Member.getClan(id);
        if (clanName == null) {
            return;
        }
        if (!clanName.equals(Member.getClan(player.getName()))) {
            player.sendMessage("Wow... Calm down, man");
            return;
        }

        if (player.getName().equals(id)) {
            player.sendMessage(getTranslate().get("members.self-click", true));
            return;
        }

        if (clickType == ClickType.MIDDLE) {
            if (!utility.hasAnyOfPermsOrLeader(player, "fc.kick")) {
                player.sendMessage(getTranslate().get("other.perm-lack", true));
                return;
            }
            Player kickedPlayer = Bukkit.getServer().getPlayer(id);
            if (kickedPlayer != null) {
                kickedPlayer.sendMessage(getTranslate().get("members.you-hb-kicked", true));
            }
            Clan.kick(clanName, id);
            Member.getMembers(clanName).forEach(member -> {
                Player memberPlayer = Bukkit.getServer().getPlayer(member);
                if (memberPlayer == null) return;
                memberPlayer.sendMessage(getTranslate().get("members.kick-propaganda", true)
                        .replace("{kicker}", player.getName()).replace("kicked", id));
            });
            display(player, getUi());
            return;
        }
        Integer rank = Integer.valueOf(Member.getRank(id));
        if (clickType == ClickType.LEFT) {
            if (!utility.hasAnyOfPermsOrLeader(player, "fc.rmanage")) {
                player.sendMessage(getTranslate().get("other.perm-lack", true));
                return;
            }
            if (rank == 1) {
                player.sendMessage(getTranslate().get("members.lowest-rank", true));
                return;
            }
            Clan.removerank(id);
            player.sendMessage(getTranslate().get("members.was-demoted")
                    .replace("{player}", player.getName())
                    .replace("{demoted}", id)
                    .replace("{rank}", Member.getRankName(clanName, id))
                    .replace("&", "§"));
            Player playerId = Bukkit.getServer().getPlayer(id);
            if (playerId != null) {
                playerId.sendMessage(getTranslate().get("members.was-demoted")
                        .replace("{player}", player.getName())
                        .replace("{demoted}", id)
                        .replace("{rank}", Member.getRankName(clanName, id))
                        .replace("&", "§"));
            }
            display(player, getUi());
            return;
        }
        if (clickType == ClickType.RIGHT) {
            if (!utility.hasAnyOfPermsOrLeader(player, "fc.rmanage")) {
                player.sendMessage(getTranslate().get("other.perm-lack", true));
                return;
            }
            if (rank == 4) {
                player.sendMessage(getTranslate().get("members.was-promoted", true));
                return;
            }
            Clan.addrank(id);
            player.sendMessage(getTranslate().get("members.was-promoted")
                    .replace("{player}", player.getName())
                    .replace("{promoted}", id)
                    .replace("{rank}", Member.getRankName(clanName, id))
                    .replace("&", "§"));
            Player playerId = Bukkit.getServer().getPlayer(id);
            if (playerId != null) {
                playerId.sendMessage(getTranslate().get("members.was-promoted")
                        .replace("{player}", player.getName())
                        .replace("{promoted}", id)
                        .replace("{rank}", Member.getRankName(clanName, id))
                        .replace("&", "§"));
            }
            display(player, getUi());

        }
    }
}
