package ru.oshifugo.functionalclans.command.gui_items.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.FunctionalClans;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.Arrays;
import java.util.Objects;

import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;

public class Status extends ItemsBase{
    String renamed;

    public static Status oldStatus(ClanGUI ui, Player player, String oldStatusText) {
        return new Status(player, ui, "old_status", Material.GREEN_STAINED_GLASS, oldStatusText);
    }
    public static Status newStatus(ClanGUI ui, Player player) {
        return new Status(player, ui, "new_status", Material.GREEN_STAINED_GLASS, "");
    }

    Status(Player player, ClanGUI ui, String id, Material material, String displayName) {
        super(player, ui, id, material, displayName);
    }
    public void editCallback(String renamed) {
        if (Objects.equals(getId(), "new_status")) {
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS);
            setId(itemStack, "new_status");
            ItemMeta meta = itemStack.getItemMeta();
            String lore = getTranslate().get("status.lore");
            meta.setLore(Arrays.asList(lore.split("\\;")));
            itemStack.setItemMeta(meta);
            this.setItemBuilder(new ItemBuilder(itemStack).setDisplayName(renamed.replace("&", "§")));
            this.renamed = renamed;
            notifyWindows();
        }
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {

        switch (id) {
            case "new_status":
                player.setLevel(player.getLevel());
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.status")) {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                int max_status = FunctionalClans.getInstance().getConfig().getInt("max_status");
                if (renamed.length() > max_status) {
                    player.sendMessage(GUITranslate.getTranslate(player).get("status.too-many-letters", true)
                            .replace("{max}", String.valueOf(max_status)));
                    break;
                }
                if (members.containsKey(player.getName())) {
                    String clanName = members.get(player.getName())[2];
                    Clan.setStatus(clanName, renamed);
                    player.sendMessage(getTranslate().get("status.done", true));
                }
                break;
        }
        getUi().settings(player);
        getUi().display(getTranslate().get("root.settings.name"));
    }
}
