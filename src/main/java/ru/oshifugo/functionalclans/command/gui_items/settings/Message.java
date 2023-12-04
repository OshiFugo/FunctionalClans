package ru.oshifugo.functionalclans.command.gui_items.settings;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.Main;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.gui_items.ItemsBase;
import ru.oshifugo.functionalclans.command.gui_items.Root;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import java.util.Arrays;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;
import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;


public class Message extends ItemsBase{
    OfflinePlayer p;
    String renamed;

    public static Message oldMessage(ClanGUI ui, Player player, String oldMessageText) {

        return new Message(player, ui, "old_message", Material.GREEN_STAINED_GLASS, oldMessageText);
    }
    public static Message newMessage(ClanGUI ui, Player player) {
        return new Message(player, ui, "new_message", Material.GREEN_STAINED_GLASS, "", player);

    }

    Message(Player player, ClanGUI ui, String id, Material material, String displayName) {
        super(player, ui, id, material, displayName);
    }
    Message(Player player, ClanGUI ui, String id, Material material, String displayName, OfflinePlayer p) {
        super(player, ui, id, material, displayName);
        this.p = p;
    }


    public void editCallback(String renamed) {
        if (Objects.equals(getId(), "new_message")) {
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS);
            setId(itemStack, "new_message");
            ItemMeta meta = itemStack.getItemMeta();
            String lore = getTranslate().get("message.lore");
            meta.setLore(Arrays.asList(lore.split("\\;")));
            itemStack.setItemMeta(meta);

            this.setItemBuilder(new ItemBuilder(itemStack).setDisplayName(PlaceholderAPI.setPlaceholders(p, renamed.replace("&", "§"))));
            this.renamed = renamed;
            notifyWindows();
        }
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        switch (id) {
            case "new_message":
                player.setLevel(player.getLevel());
                if (!utility.hasAnyOfPermsOrLeader(player, "fc.message")) {
                    player.sendMessage(getTranslate().get("other.perm-lack", true));
                    return;
                }
                if (members.containsKey(player.getName())) {
                    String clanName = members.get(player.getName())[2];
                    Clan.setMessage(clanName, renamed);
                    player.sendMessage(getTranslate().get("message.done", true));
                    getUi().settings(player);
                    getUi().display(getTranslate().get("root.settings.name"));
                }
                break;

        }
    }
}
