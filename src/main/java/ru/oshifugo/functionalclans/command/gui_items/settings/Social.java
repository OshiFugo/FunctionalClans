package ru.oshifugo.functionalclans.command.gui_items.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.Utility;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.sql.Clan;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.Arrays;
import java.util.Objects;

import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;

public class Social extends ItemsBase{
    String renamed;

    public static Social oldSocial(ClanGUI ui, Player player, String oldSocialText) {
        return new Social(player, ui, "old_social", Material.GREEN_STAINED_GLASS, oldSocialText);
    }
    public static Social newSocial(ClanGUI ui, Player player) {
        return new Social(player, ui, "new_social", Material.GREEN_STAINED_GLASS, "");
    }

    Social(Player player, ClanGUI ui, String id, Material material, String displayName) {
        super(player, ui, id, material, displayName);
    }
    public void editCallback(String renamed) {
        if (Objects.equals(getId(), "new_social")) {
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS);
            setId(itemStack, "new_social");
            ItemMeta meta = itemStack.getItemMeta();
            String lore = getTranslate().get("social.lore");
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
            case "new_social":
                player.setLevel(player.getLevel());
                if (!Utility.hasAnyOfPermsOrLeader(player, "fc.social")) {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    return;
                }
                if (members.containsKey(player.getName())) {
                    String clanName = members.get(player.getName())[2];
                    Clan.setSocial(clanName, renamed);
                    player.sendMessage(getTranslate().get("social.done", true));
                    getUi().settings(player);
                    getUi().display(getTranslate().get("root.settings.name"));
                }
                break;

        }
    }
}
