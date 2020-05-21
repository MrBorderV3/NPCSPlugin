package me.border.npcs.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    static me.border.npcs.NPCSPlugin plugin;
    public Utils(me.border.npcs.NPCSPlugin plugin){
        this.plugin = plugin;
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String cs(String s) {
        return plugin.getConfig().getString(s);
    }

    public static String ucs(String s) {
        return Utils.colorize(plugin.getConfig().getString(s));
    }

    public static List<String> csl(String s) {
        return plugin.getConfig().getStringList(s);
    }

    public static int ci(String s) {
        return plugin.getConfig().getInt(s);
    }

    public static double cd(String s){return plugin.getConfig().getDouble(s);}

    public static boolean cb(String s) {
        return plugin.getConfig().getBoolean(s);
    }

    // Check if the args are right, returns true/false.
    public static boolean argsCheck(CommandSender sender, int allowed, String[] args) {
        if (args.length != allowed) {
            sender.sendMessage(Utils.ucs("illegalArguments"));
            return false;
        }
        return true;
    }

    // Check if the target is a player, returns true/false.
    public static boolean playerCheck(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }
        sender.sendMessage(Utils.ucs("notAPlayer"));
        return false;
    }

    // Check if the target is offline/null, returns true/false.
    public static boolean offlineCheck(Player target, CommandSender sender, String replacement) {
        if (target == null) {
            sender.sendMessage(Utils.ucs("targetNull").replaceAll("%target%", replacement));
            return false;
        }
        return true;
    }

    // Check if the sender has the needed permission, returns true/false.
    public static boolean permCheck(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        }
        sender.sendMessage(Utils.ucs("noPermission"));
        return false;
    }

    // Check if the sender and the target are the same, returns true/false.
    public static boolean duplicateCheck(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage(Utils.ucs("duplicateMessage"));
            return false;
        }
        return true;
    }

    // Append args to a message and return the appended string
    public static String appendArgs(String[] args) {
        StringBuilder x = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            x.append(args[i]).append(" ");
        }
        return x.toString();
    }

    // Send message to a CommandSender
    public static void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(Utils.ucs(path));
    }

    // Send message to a CommandSender with a .replaceAll
    public static void sendMsg(CommandSender sender, String path, String replace, String replacement) {
        sender.sendMessage(Utils.ucs(path).replaceAll(replace, replacement));
    }

    // Send message to a CommandSender with 2 .replaceAll
    public static void sendMsg(CommandSender sender, String path, String replace, String replacement, String replace2, String replacement2) {
        sender.sendMessage(Utils.ucs(path).replaceAll(replace, replacement).replaceAll(replace2, replacement2));
    }

    // Send a raw message to a CommandSender
    public static void sendRawMsg(CommandSender sender, String msg){
        sender.sendMessage(Utils.colorize(msg));
    }

    // Send message to a player
    public static void sendMsg(Player player, String path) {
        player.sendMessage(Utils.ucs(path));
    }

    // Send message to a player with a .replaceAll
    public static void sendMsg(Player player, String path, String replace, String replacement) {
        player.sendMessage(Utils.ucs(path).replaceAll(replace, replacement));
    }

    // Send message to a player with 2 .replaceAll
    public static void sendMsg(Player player, String path, String replace, String replacement, String replace2, String replacement2) {
        player.sendMessage(Utils.ucs(path).replaceAll(replace, replacement).replaceAll(replace2, replacement2));
    }

    // Send a raw message to a player
    public static void sendRawMsg(Player player, String msg){
        player.sendMessage(Utils.colorize(msg));
    }

    // Convert a string list into messages and send them to a CommandSender
    public static void sendMessageList(CommandSender sender, String path) {
        for (String output : Utils.csl(path)) {
            sender.sendMessage(Utils.colorize(output));
        }
    }

    // Convert a string list into messages and send them to a player
    public static void sendMessageList(Player player, String path) {
        for (String output : Utils.csl(path)) {
            player.sendMessage(Utils.colorize(output));
        }
    }

    // Util to create ItemStacks easier and quickly
    public static ItemStack createItem(Material material, String displayName, ArrayList<String> lore, short data) {
        ItemStack itemStack = new ItemStack(material, 1, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.colorize(displayName));
        // Avoid exceptions and make putting no lore possible
        if (lore != null) {
            ArrayList<String> newLore = new ArrayList<>();
            for (String loreline : lore) {
                newLore.add(Utils.colorize(loreline));
            }
            itemMeta.setLore(newLore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
