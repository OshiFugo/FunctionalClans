package ru.oshifugo.functionalclans.command.gui_items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.settings.*;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.utility;

public class Settings extends ItemsBase {

    private boolean isOpened;
    private String clanName;

    public static Settings message(ClanGUI ui, Player player) {
        return (Settings) new Settings(player, ui, "message", Material.PAPER)
                .setTranslatedName("settings.message")
                .setTranslatedLore("settings.message");
    }
    public static Settings status(ClanGUI ui, Player player) {
        return (Settings) new Settings(player, ui, "status", Material.PURPLE_WOOL)
                .setTranslatedName("settings.status")
                .setTranslatedLore("settings.status");
    }
    public static Settings social(ClanGUI ui, Player player) {
        return (Settings) new Settings(player, ui, "social", Material.COOKED_CHICKEN)
                .setTranslatedName("settings.social")
                .setTranslatedLore("settings.social");
    }
    public static Settings rename(ClanGUI ui, Player player) {
        return (Settings) new Settings(player, ui, "rename", Material.FEATHER)
                .setTranslatedName("settings.rename")
                .setTranslatedLore("settings.rename");
    }
    public static Settings type(ClanGUI ui, Player player, String clanName) {
        Settings type = (Settings) new Settings(player, ui, "type", Material.IRON_DOOR)
                .setTranslatedName("settings.type")
                .setTranslatedLore("settings.type");
        type.clanName = clanName;
        if (Clan.getType(clanName) == 0) {
            type.replaceLore("{now}", type.getTranslate().get("settings.type.closed"));
        } else {
            type.replaceLore("{now}", type.getTranslate().get("settings.type.open"));
        }
        return type;
    }

    public static Settings setrole(ClanGUI ui, Player player) {
        return (Settings) new Settings(player, ui, "role-manager", Material.PLAYER_HEAD)
                .setTranslatedName("settings.role-manager")
                .setTranslatedLore("settings.role-manager");
    }

    Settings(Player player, ClanGUI ui, String id, Material material) {
        super(player, ui, id, material);
    }



    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id){
        switch (id) {
            case "message":
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.message"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                Message newMessage = getUi().message(player);
                getUi().displayAnvil(getTranslate().get("settings.message.name"), newMessage::editCallback);
                break;
            case "status":
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.status"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                Status newStatus = getUi().status(player);
                getUi().displayAnvil(getTranslate().get("settings.status.name"), newStatus::editCallback);
                break;
            case "social":
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.social"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                Social newSocial = getUi().social(player);
                getUi().displayAnvil(getTranslate().get("settings.social.name"), newSocial::editCallback);
                break;
            case "rename":
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.rename"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                Rename newRename = getUi().rename(player);
                getUi().displayAnvil(getTranslate().get("settings.rename.name"), newRename::editCallback);
                break;
            case "type":

                if (!utility.hasAnyOfPermsOrLeader(player, "fc.type"))
                {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                if (Clan.getType(clanName) == 0) {
                    Clan.setType(clanName, 1);
                } else {
                    Clan.setType(clanName, 0);
                }

                setTranslatedName("settings.type");
                setTranslatedLore("settings.type");
                if (Clan.getType(clanName) == 0) {
                    replaceLore("{now}", getTranslate().get("settings.type.closed"));
                } else {
                    replaceLore("{now}", getTranslate().get("settings.type.open"));
                }


                notifyWindows();
                break;
                // 0 - closed
            case "role-manager":
//                kick join perms, withdraw, demote_promote, announcments, join allieance, dissolve the alliance
                if (utility.hasAnyOfPermsOrLeader(player, "fc.setrole")) {
                    RoleManager.display(player, getUi());
                }
                break;
        }
    }
}
