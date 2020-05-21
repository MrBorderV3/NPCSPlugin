package me.border.npcsplugin.v1_13R;

import me.border.npcs.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCCommand1_13 implements CommandExecutor {
    private me.border.npcs.NPCSPlugin plugin;

    public NPCCommand1_13(me.border.npcs.NPCSPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("npc").setExecutor(this);
    }

    public NPCManager1_13 npcManager13;

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args){
        if(!Utils.playerCheck(sender))return true;
        Player p = (Player) sender;
        if(!Utils.permCheck(p, "npc.npc"))return true;
        if(!Utils.argsCheck(p, 0, args))return true;
        Location location = new Location(p.getWorld(), Utils.cd("npcLocation.x"), Utils.cd("npcLocation.y"), Utils.cd("npcLocation.z"));
        npcManager13 = new NPCManager1_13("Bob", location);
        npcManager13.spawn();
        new BukkitRunnable(){
            public void run(){
                npcManager13.teleport(location.add(0, 1.2, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager13.teleport(location.add(0, -1.2, 0)), 5L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager13.teleport(location.add(0, 1.2, 0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager13.teleport(location.add(0, -1.2, 0)), 5L);
                    }
                }.runTaskLater(plugin, 20L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager13.entityMetadata((byte) 0x02);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager13.entityMetadata((byte) 0), 20L);
                    }
                }.runTaskLater(plugin, 40L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager13.entityMetadata((byte) 0x02);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager13.animation(0), 20L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager13.entityMetadata((byte) 0), 40L);
                    }
                }.runTaskLater(plugin, 80L);
            }
        }.runTaskLater(plugin, 20L);

        return false;
    }
}

