package ru.oshifugo.functionalclans.command.gui_items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.utility;

public class Root extends ItemsBase{
    static public Root settings(ClanGUI ui, Player player) {
        return (Root) new Root(player, ui, "settings", Material.BEDROCK)
                .setTranslatedName("root.settings")
                .setTranslatedLore("root.settings");
    }
    static public Root actions(ClanGUI ui, Player player) {
        return (Root) new Root(player, ui, "actions", Material.NETHER_STAR)
                .setTranslatedName("root.actions")
                .setTranslatedLore("root.actions");
    }
    static public Root info(ClanGUI ui, Player player) {
        return (Root) new Root(player, ui, "info", Material.GLOWSTONE)
                .setTranslatedName("root.info")
                .setTranslatedLore("root.info");
    }
    static public Root go_back(ClanGUI ui, Player player) {
        return (Root) new Root(player, ui, "go_back", Material.BARRIER)
                .setTranslatedName("root.go_back")
                .setTranslatedLore("root.go_back");
    }


    Root(Player player, ClanGUI ui, String id, Material material) {
        super(player, ui, id, material);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        switch (id) {
            case "settings":

                if (!utility.hasAnyOfPermsOrLeader(player, "fc.message", "fc.rename",
                        "fc.social, fc.status", "fc.type", "fc.role"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                this.getUi().settings(player);
                this.getUi().display("Settings");
                break;
            case "go_back":
                this.getUi().home(player);
                this.getUi().display("Clans");
                break;
        }
    }

}
