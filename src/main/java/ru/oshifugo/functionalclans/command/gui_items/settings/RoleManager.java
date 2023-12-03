package ru.oshifugo.functionalclans.command.gui_items.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.command.gui_items.Root;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;
import ru.oshifugo.functionalclans.sql.SQLiteUtility;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;

import java.util.HashMap;
import java.util.Objects;

public class RoleManager extends ItemsBase {
//    kick, join, perms, addmoney, withdraw, demote_promote, announcments,
//    join allieance, dissolve the alliance (9)

    public int row;
    public static void display(Player player, ClanGUI ui) {
        String clanName = Member.getClan(player.getName());
        if (clanName == null) {
            utility.debug("displayRow -> clanName == 0");
            return;
        }
//        ranks.put("fc.kick", 2);
//        ranks.put("fc.invite", 3);
//        ranks.put("fc.cash_add", 4);
//        ranks.put("fc.cash_remove", 5);
//        ranks.put("fc.rmanage", 6); - demote/promote
//        ranks.put("fc.chat", 7);
//        ranks.put("fc.msg", 8);
//        ranks.put("fc.alliance_add",9);
//        ranks.put("fc.alliance_remove", 10);


        ui.gui = Gui.empty(9, 6);
        ui.gui.applyStructure(new Structure(
                        "#########",
                        ".........",
                        ".........",
                        ".........",
                        ".........",
                        "#########"
                ).addIngredient('#', ui.getVoidFill())

        );
        for (int i = 1; i <= 4; i++) {
            setRow(ui, player, clanName, i);
        }
        ui.gui.setItem(8, 5, Root.go_back(ui, player));
        ui.display(GUITranslate.getTranslate(player).get("settings.role-manager.name"));

    }
    public static void setRow(ClanGUI ui, Player player, String clanName, int row) {
        String[] role = SQLiteUtility.clan_role.get(clanName + "_" + row);
        role[1] = role[1].replace("&", "§");
        ui.gui.setItem(0, row, kick(player, ui, normalizeCurrent(player, role[2]), role[1], row));
        ui.gui.setItem(1, row, invite(player, ui, normalizeCurrent(player, role[3]), role[1], row));
        ui.gui.setItem(2, row, cash_add(player, ui, normalizeCurrent(player, role[4]), role[1], row));
        ui.gui.setItem(3, row, cash_remove(player, ui, normalizeCurrent(player, role[5]), role[1], row));
        ui.gui.setItem(4, row, rmanage(player, ui, normalizeCurrent(player, role[6]), role[1], row));
        ui.gui.setItem(5, row, chat(player, ui, normalizeCurrent(player, role[7]), role[1], row));
        ui.gui.setItem(6, row, msg(player, ui, normalizeCurrent(player, role[8]), role[1], row));
        ui.gui.setItem(7, row, alliance_add(player, ui, normalizeCurrent(player, role[9]), role[1], row));
        ui.gui.setItem(8, row, alliance_remove(player, ui, normalizeCurrent(player, role[10]), role[1], row));
    }
    private static String normalizeCurrent(Player p, String current) {
        if (Objects.equals(current, "true")) {
            return GUITranslate.getTranslate(p).get("role-manager.current_true");
        }
        return GUITranslate.getTranslate(p).get("role-manager.current_false");
    }


    public static ItemsBase kick(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "kick", Material.NETHER_WART, row)
                .setTranslatedName("role-manager.kick")
                .setTranslatedLore("role-manager.kick")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }

    public static ItemsBase invite(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "invite", Material.GOLD_NUGGET, row)
                .setTranslatedName("role-manager.invite")
                .setTranslatedLore("role-manager.invite")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase cash_add(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "cash_add", Material.GREEN_STAINED_GLASS, row)
                .setTranslatedName("role-manager.cash_add")
                .setTranslatedLore("role-manager.cash_add")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase cash_remove(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "cash_remove", Material.RED_STAINED_GLASS, row)
                .setTranslatedName("role-manager.cash_remove")
                .setTranslatedLore("role-manager.cash_remove")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase rmanage(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "rmanage", Material.PLAYER_HEAD, row)
                .setTranslatedName("role-manager.rmanage")
                .setTranslatedLore("role-manager.rmanage")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase chat(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "chat", Material.PAPER, row)
                .setTranslatedName("role-manager.chat")
                .setTranslatedLore("role-manager.chat")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase msg(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "msg", Material.FEATHER, row)
                .setTranslatedName("role-manager.msg")
                .setTranslatedLore("role-manager.msg")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase alliance_add(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "alliance_add", Material.DIAMOND_SWORD, row)
                .setTranslatedName("role-manager.alliance_add")
                .setTranslatedLore("role-manager.alliance_add")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    public static ItemsBase alliance_remove(Player player, ClanGUI ui, String current, String rank, int row) {
        return new RoleManager(player, ui, "alliance_remove", Material.GOLDEN_PICKAXE, row)
                .setTranslatedName("role-manager.alliance_remove")
                .setTranslatedLore("role-manager.alliance_remove")
                .replaceName("{rank}", rank)
                .replaceLore("{current}", current);
    }
    private static int getRoleNumber(String roleName) {
        HashMap<String, Integer> roleMap = new HashMap<>();
        roleMap.put("kick", 2);
        roleMap.put("invite", 3);
        roleMap.put("cash_add", 4);
        roleMap.put("cash_remove", 5);
        roleMap.put("rmanage", 6);
        roleMap.put("chat", 7);
        roleMap.put("msg", 8);
        roleMap.put("alliance_add", 9);
        roleMap.put("alliance_remove", 10);

        return roleMap.getOrDefault(roleName, -1); // -1 for unknown role
    }
    protected RoleManager(Player player, ClanGUI ui, String id, Material material, int row) {
        super(player, ui, id, material);
        this.row = row;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        if (!utility.hasAnyOfPermsOrLeader(player, "fc.setrole")) {
            player.sendMessage(getTranslate().get("other.perm-lack", true));
            return;
        }
        Clan.setRole(Member.getClan(player.getName()), row, getRoleNumber(id));
        display(player, getUi());
    }
}
