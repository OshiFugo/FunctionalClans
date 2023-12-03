package ru.oshifugo.functionalclans.command.gui_items.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;

import java.util.HashMap;

public class RoleManager extends ItemsBase {
//    kick, join, perms, addmoney, withdraw, demote_promote, announcments,
//    join allieance, dissolve the alliance (9)


    public static void addRow(int row, Player player, ClanGUI ui) {
        ui.gui.setItem(row, 0, kick(player, ui, "role"));

    }

    public static ItemsBase kick(Player player, ClanGUI ui, String current) {
        return new RoleManager(player, ui, "kick", Material.NETHER_WART)
                .setTranslatedName("role-manager.kick")
                .replaceRole("{current}", current);
    }

    protected RoleManager(Player player, ClanGUI ui, String id, Material material) {
        super(player, ui, id, material);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {

    }
}
