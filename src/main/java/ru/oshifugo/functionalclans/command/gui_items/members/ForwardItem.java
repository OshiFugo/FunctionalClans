package ru.oshifugo.functionalclans.command.gui_items.members;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ForwardItem extends PageItem {
    OfflinePlayer player;

    public ForwardItem(OfflinePlayer player) {
        super(true);
        this.player = player;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        GUITranslatePlaceholder t = GUITranslate.getTranslate(player);
        builder.setDisplayName(t.getName("page.next-page"))
                .addLoreLines(gui.hasNextPage()
                        ? t.getLore("page.next-page") + " §e" + (gui.getCurrentPage() + 2) + "§7/§e" + gui.getPageAmount()
                        : t.getLore("page.no-more-pages"));

        return builder;
    }

}