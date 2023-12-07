package ru.oshifugo.functionalclans.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.oshifugo.functionalclans.GUITranslate;
import ru.oshifugo.functionalclans.command.gui_items.Root;
import ru.oshifugo.functionalclans.command.gui_items.Settings;
import ru.oshifugo.functionalclans.command.gui_items.settings.Message;
import ru.oshifugo.functionalclans.command.gui_items.settings.Rename;
import ru.oshifugo.functionalclans.command.gui_items.settings.Social;
import ru.oshifugo.functionalclans.command.gui_items.settings.Status;
import ru.oshifugo.functionalclans.sql.Clan;
import ru.oshifugo.functionalclans.sql.Member;
import ru.oshifugo.functionalclans.utility;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;

import java.util.function.Consumer;

import static ru.oshifugo.functionalclans.sql.SQLiteUtility.members;

public class ClanGUI {
    public Gui gui;
    public AnvilWindow window;
    Player p;
    private ItemBuilder voidFill;
    public Gui getGui() {
        return gui;
    }

    public ItemBuilder getVoidFill() {
        return voidFill;
    }
    public static boolean isSupported() {
//        if version < 1.14: false
        String version = Bukkit.getServer().getClass().getPackage().getName();
        String[] ver = version.split("\\.");
        if (ver.length < 4) {
            utility.debug("ClanGUI -> Unknown version!");
            return true;
        }
        return Integer.parseInt(ver[3].split("_")[1]) >= 14;
    }

    public ClanGUI(Player player) {
        this.p = player;
        voidFill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(GUITranslate.getTranslate(player).get("glass-name"));

    }

    public void display(String title) {
        if (this.getGui() == null) {
            return;
        }
        Window window = Window.single()
                .setViewer(this.p)
                .setTitle(title)
                .setGui(this.getGui())
                .build();
        window.open();
    }



    public void home(Player player) {
        if (Member.getClan(player.getName()) == null) {
            gui = null;
            player.sendMessage(GUITranslate.getTranslate(player).get("other.clan-lack", true));
            return;
        }
            gui = Gui.empty(9, 3);
        gui.applyStructure(new Structure(
                "#########",
                "#.......#",
                "#########"
                ).addIngredient('#', voidFill)

        );
    //
        gui.setItem(2, 1, Root.settings(this, player));
        gui.setItem(4, 1, Root.information(this, player));
        gui.setItem(6, 1, Root.members(this, player));
    }
    public void settings(Player player) {
        gui = Gui.empty(9, 3);
        gui.applyStructure(new Structure(
                "####.####",
                "#.......#",
                "####.###.")
                .addIngredient('#', voidFill)
        );

        if (members.containsKey(p.getName())) {
            String clanName = members.get(p.getName())[2];
            gui.setItem(2, 1, Settings.message(this, player));
            gui.setItem(3, 1, Settings.status(this, player));
            gui.setItem(4, 0, Settings.social(this, player));
            gui.setItem(4, 2, Settings.rename(this, player));
            gui.setItem(5, 1, Settings.type(this, player, clanName));
            gui.setItem(6, 1, Settings.setrole(this, player));
            gui.setItem(8, 2, Root.go_back(this, player));
        }


    }
    public Message message(Player player) {
        gui = Gui.empty(3, 1);
        gui.applyStructure(new Structure(
                "...")
        );
        if (members.containsKey(p.getName())) {
            String clanName = members.get(p.getName())[2];
            gui.setItem(0, 0, Message.oldMessage(this, player, Clan.getMessage(clanName)));
        }
        Message newMessage = Message.newMessage(this, player);
        gui.setItem(2, 0, newMessage);
        return newMessage;

    }
    public Status status(Player player) {
        gui = Gui.empty(3, 1);
        gui.applyStructure(new Structure(
                "...")
        );
        if (members.containsKey(p.getName())) {
            String clanName = members.get(p.getName())[2];
            gui.setItem(0, 0, Status.oldStatus(this, player, Clan.getStatus(clanName)));
        }
        Status newStatus = Status.newStatus(this, player);
        gui.setItem(2, 0, newStatus);
        return newStatus;

    }

    public Social social(Player player) {
        gui = Gui.empty(3, 1);
        gui.applyStructure(new Structure(
                "...")
        );
        if (members.containsKey(p.getName())) {
            String clanName = members.get(p.getName())[2];
            gui.setItem(0, 0, Social.oldSocial(this, player, Clan.getSocial(clanName)));
        }
        Social newSocial = Social.newSocial(this, player);
        gui.setItem(2, 0, newSocial);
        return newSocial;
    }

    public Rename rename(Player player) {
        gui = Gui.empty(3, 1);
        gui.applyStructure(new Structure(
                "...")
        );
        if (members.containsKey(p.getName())) {
            String clanName = members.get(p.getName())[2];
            gui.setItem(0, 0, Rename.oldMessage(this, player, clanName));
        }
        Rename newSocial = Rename.newMessage(this, player);
        gui.setItem(2, 0, newSocial);
        return newSocial;

    }
    public void displayAnvil(String title) {
        window = AnvilWindow.single()
                .setViewer(p)
                .setGui(gui)
                .setTitle(title)
                .build();
        window.open();

    }

    public void displayAnvil(String title, Consumer<String> callback) {
        window = AnvilWindow.single()
                .setViewer(p)
                .setGui(gui)
                .setTitle(title)
                .addRenameHandler(callback)
                .build();
        window.open();


    }




}
