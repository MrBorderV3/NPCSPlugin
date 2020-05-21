package me.border.npcs;


import me.border.npcs.ver1_10.NPCCommand1_10;
import me.border.npcs.ver1_12.NPCCommand1_12;
import me.border.npcs.ver1_14.NPCCommand1_14;
import me.border.npcs.ver1_8.NPCCommand1_8;
import me.border.npcs.ver1_9.NPCCommand1_9;
import me.border.npcs.util.Utils;
import me.border.npcs.ver1_11.NPCCommand1_11;
import me.border.npcs.ver1_13.NPCCommand1_13;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NPCSPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        new Utils(this);
        if (Bukkit.getVersion().contains("1.8")) {
            Bukkit.getLogger().severe("Found NMS Version 1.8");
            new NPCCommand1_8(this);
        } else if (Bukkit.getVersion().contains("1.9")) {
            Bukkit.getLogger().severe("Found NMS Version 1.9");
            new NPCCommand1_9(this);
        } else if (Bukkit.getVersion().contains("1.10")) {
            Bukkit.getLogger().severe("Found NMS Version 1.10");
            new NPCCommand1_10(this);
        }else if(Bukkit.getVersion().contains("1.11")){
            Bukkit.getLogger().severe("Found NMS Version 1.11");
            new NPCCommand1_11(this);
        } else if (Bukkit.getVersion().contains("1.12")) {
            Bukkit.getLogger().severe("Found NMS Version 1.12");
            new NPCCommand1_12(this);
        }else if(Bukkit.getVersion().contains("1.13")){
            Bukkit.getLogger().severe("Found NMS Version 1.13");
            new NPCCommand1_13(this);
        }else if(Bukkit.getVersion().contains("1.14")) {
            Bukkit.getLogger().severe("Found NMS Version 1.14");
            new NPCCommand1_14(this);
        } else {
            Bukkit.getLogger().severe("No NMS Version found between 1.8-1.14 disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
