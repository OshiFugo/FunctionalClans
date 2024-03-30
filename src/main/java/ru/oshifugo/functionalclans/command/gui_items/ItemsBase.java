package ru.oshifugo.functionalclans.command.gui_items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.FunctionalClans;
import ru.oshifugo.functionalclans.command.ClanGUI;
import ru.oshifugo.functionalclans.command.GUITranslatePlaceholder;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Arrays;

public abstract class ItemsBase extends AbstractItem {

    protected ItemBuilder itemBuilder;
    private final ClanGUI ui;
    private OfflinePlayer player;
    private GUITranslatePlaceholder translate;
    private String lore;
    private String name;

    public GUITranslatePlaceholder getTranslate() {
        return translate;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public ClanGUI getUi() {
        return ui;
    }


    protected ItemsBase(OfflinePlayer player, ClanGUI ui, String id, ItemStack itemStack, String displayName) {
        this.ui = ui;
        this.player = player;
        this.translate = GUITranslate.getTranslate(player);

        setId(itemStack, id);
        this.itemBuilder = new ItemBuilder(itemStack).setDisplayName(displayName);
    }
    protected ItemsBase(Player player, ClanGUI ui, String id, Material material, String displayName) {
        this.ui = ui;
        this.player = player;
        this.translate = GUITranslate.getTranslate(player);
        ItemStack itemStack = new ItemStack(material);
        setId(itemStack, id);
        this.itemBuilder = new ItemBuilder(itemStack).setDisplayName(displayName);
    }
    protected ItemsBase(Player player, ClanGUI ui, String id, Material material) {
        this.ui = ui;
        this.player = player;
        this.translate = GUITranslate.getTranslate(player);
        ItemStack itemStack = new ItemStack(material);
        setId(itemStack, id);
        this.itemBuilder = new ItemBuilder(itemStack);
    }

    public ItemsBase setDisplayedName(String name) {
        if (name != null) {
            this.name = name;
            itemBuilder.setDisplayName(name);
        }
        return this;
    }
    public ItemsBase setTranslatedName(String name) {
        setDisplayedName(this.getTranslate().getName(name));
        return this;
    }
    public ItemsBase setLore(String name) {
        lore = name;
        if (name != null) {
            itemBuilder.setLegacyLore(Arrays.asList(name.split("\\;")));
        }

        return this;
    }

    public ItemsBase setTranslatedLore(String name) {

        setLore(this.getTranslate().getLore(name));
        return this;
    }
    public ItemsBase replaceLore(String key, String value) {
        setLore(lore.replace(key, value));
        return this;
    }
    public ItemsBase replaceName(String key, String value) {
        setDisplayedName(name.replace(key, value));
        return this;
    }
    protected ItemsBase(Player player, ClanGUI ui, String id, Material material, String displayName, String lore) {
        ItemStack item = new ItemStack(material);
        this.player = player;
        this.translate = GUITranslate.getTranslate(player);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore.split("\\;")));
        item.setItemMeta(meta);
        this.ui = ui;
        setId(item, id);
        this.itemBuilder = new ItemBuilder(item).setDisplayName(displayName);
    }
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public void setItemBuilder(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public ItemProvider getItemProvider() {
        return itemBuilder;
    }

    protected void setId(ItemStack item, String id) {
        if (item == null) {
            utility.debug("setId -> item == null");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            utility.debug("setId -> meta == null");
            return;
        }
        NamespacedKey key = new NamespacedKey(FunctionalClans.getInstance(), "id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
    }


    abstract public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id);

    public String getId() {
        ItemStack item = itemBuilder.get();
        if (item == null) {
            utility.debug("getId -> item == null");
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            utility.debug("getId -> meta == null");
            return null;
        }
        NamespacedKey key = new NamespacedKey(FunctionalClans.getInstance(), "id");

        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) {
            utility.debug("getId -> id == null");
        }
        return id;
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        String id = getId();
        if (id == null) {
            utility.debug("handleClick -> id == null");
            return;
        }
        handleClick(clickType, player, inventoryClickEvent, id);
    };
}
