package ru.oshifugo.functionalclans.command.gui_items.members;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {

    OfflinePlayer player;

    public BackItem(OfflinePlayer player) {
        super(true);
        this.player = player;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        GUITranslatePlaceholder t = GUITranslate.getTranslate(player);
        builder.setDisplayName(t.getName("page.prev-page"))
                .addLoreLines(gui.hasPreviousPage()
                        ? t.getLore("page.prev-page") + " §e" + gui.getCurrentPage() + "§7/§e" + gui.getPageAmount()
                        : t.getLore("page.you-cant-go-further"));

        return builder;
    }

}