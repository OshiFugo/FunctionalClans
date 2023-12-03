package ru.oshifugo.functionalclans.tabcomplete;

import org.bukkit.command.CommandSender;

import javax.lang.model.type.UnionType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TabAccessor {
    static String replace = "***";
    private CommandSender sender;
    private List<String> commands;
    private String[] args;
    int level;

    TabAccessor(CommandSender sender, String[] args) {
        this.sender = sender;
        this.commands = new ArrayList<>();
        this.args = args;

    }

    public void setLevel(int level) {
        this.level = level;
    }
    String[] checkParse(String completePath) {
        String[] parsed = completePath.split("\\.");
        if (parsed.length == args.length) {
            for (int i = 0; i < parsed.length - 1; i++) {
                if (!Objects.equals(parsed[i], args[i]) && !Objects.equals(parsed[i], replace)){
                    return null;
                };
            }
            return parsed;
        }
        return null;
    }

    boolean add(String permission, String complete) {
        if (!sender.hasPermission(permission)) return false;
        String[] parsed = checkParse(complete);
        if (parsed == null) return false;

        if (parsed[parsed.length-1].startsWith(args[args.length - 1]) ||
                args[args.length - 1].isEmpty() || Objects.equals(args[args.length - 1], replace)) {
            commands.add(parsed[parsed.length-1]);
            return true;
        }
        return false;
    }
    void add(String permission, String[] completeList) {
        for (String complete : completeList) {
            this.add(permission, complete);
        }
    }
    void addList(String permission, String completePath, List<String> completeList) {
        for (String complete : completeList) {
            this.add(permission, completePath + "." + complete);
        }
    }
    void add(String permission, String completePath, String[] completeList) {
        for (String complete : completeList) {
            this.add(permission, completePath + "." + complete);
        }
    }
    void addFormat(String permission, String completePath, Object... format) {
        this.add(permission, String.format(completePath, format));
    }
    void addListFormat(String permission, String completePath, List<String> completeList, Object... format) {
        this.addList(permission, String.format(completePath, format), completeList);
    }
//    @SafeVarargs
//    final void addAnyListFormat(String permission, String completePath, List<Object>... format) {
//
//        for (int i = 0; i < format.length; i++) {
//            for (Object inList : format[i]) {
//                this.add(permission, String.format(completePath, inList));
//            }
//        }
//    }

    public List<String> getCommands() {
        return commands;
    }
}
