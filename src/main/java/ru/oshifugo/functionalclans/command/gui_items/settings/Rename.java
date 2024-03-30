package ru.oshifugo.functionalclans.command.gui_items.settings;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.FunctionalClans;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.Arrays;
import java.util.Objects;

import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;


public class Rename extends ItemsBase{
    String renamed;

    public static Rename oldMessage(ClanGUI ui, Player player, String oldMessageText) {
        return new Rename(player, ui, "old_message", Material.GREEN_STAINED_GLASS, oldMessageText);
    }
    public static Rename newMessage(ClanGUI ui, Player player) {
        return new Rename(player, ui, "new_message", Material.GREEN_STAINED_GLASS, "");
    }

    Rename(Player player, ClanGUI ui, String id, Material material, String displayName) {
        super(player, ui, id, material, displayName);
    }
    public void editCallback(String renamed) {
        if (Objects.equals(getId(), "new_message")) {
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS);
            setId(itemStack, "new_message");
            ItemMeta meta = itemStack.getItemMeta();
            int price = FunctionalClans.getInstance().getConfig().getInt("rename_price");
            String lore = getTranslate().get("rename.charging");
            meta.setLore(Arrays.asList(lore.replace("{money}", String.valueOf(price)).split("\\;")));
            itemStack.setItemMeta(meta);
            this.setItemBuilder(new ItemBuilder(itemStack).setDisplayName(renamed));
            this.renamed = renamed;
            notifyWindows();
        }
    }
    public static int getFormatLength(String text) {
        int l = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '&' && text.charAt(i) != '§') {
                l++;
            }
        }
        return l;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        switch (id) {
            case "new_message":
                FileConfiguration config = FunctionalClans.getInstance().getConfig();
                player.setLevel(player.getLevel());
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.rename")) {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    break;
                }
                int price = config.getInt("rename_price");
                if (price == 0) {
                    player.sendMessage(getTranslate().get("other.undefined-price", true));
                    break;
                }
//                min_name: 3
                //max_name: 30
                int minName = config.getInt("min_name");
                int maxName = config.getInt("max_name");
                int renamedLength = getFormatLength(renamed);
                if (!(minName <= renamedLength && renamedLength <= maxName)) {
                    player.sendMessage(getTranslate().get("rename.out-of-letters", true)
                            .replace("{min}", String.valueOf(minName))
                            .replace("{max}", String.valueOf(maxName)));
                    break;
                }
                if (utility.getRawClan(renamed) != null) {
                    player.sendMessage(getTranslate().get("rename.used-name", true));
                    break;
                }
                if (members.containsKey(player.getName())) {
                    if (price != -1) {
                        EconomyResponse result = FunctionalClans.getEconomy().withdrawPlayer(player, price);
                        if (!result.transactionSuccess()) {
                            player.sendMessage(getTranslate().get("rename.n-enough-money", true)
                                    .replace("{money}", String.valueOf(price)));
                            break;
                        }
                        player.sendMessage(getTranslate().get("other.charged", true).replace( "{money}", String.valueOf(price)));
                    }
                    String clanName = members.get(player.getName())[2];
                    Clan.setClanName(clanName, renamed);
                    player.sendMessage(getTranslate().get("rename.done", true));

                }
                break;
        }
        getUi().settings(player);
        getUi().display(getTranslate().get("root.settings.name"));
    }
}
