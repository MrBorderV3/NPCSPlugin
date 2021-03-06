package me.border.npcsplugin.v1_10R;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.border.npcs.util.Utils.*;
public class NPCCommand1_10 implements CommandExecutor {
    private me.border.npcs.NPCSPlugin plugin;

    public NPCCommand1_10(me.border.npcs.NPCSPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("npc").setExecutor(this);
    }

    public NPCManager1_10 npcManager10;

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args){
        if(!playerCheck(sender))return true;
        Player p = (Player) sender;
        if(!permCheck(p, "npc.npc"))return true;
        if(!argsCheck(p, 0, args))return true;
        Location location = new Location(p.getWorld(), cd("npcLocation.x"), cd("npcLocation.y"), cd("npcLocation.z"));
        npcManager10 = new NPCManager1_10("Bob", location);
        npcManager10.spawn();
        new BukkitRunnable(){
            public void run(){
                npcManager10.teleport(location.add(0, 1.2, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager10.teleport(location.add(0, -1.2, 0)), 5L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager10.teleport(location.add(0, 1.2, 0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager10.teleport(location.add(0, -1.2, 0)), 5L);
                    }
                }.runTaskLater(plugin, 20L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager10.entityMetadata((byte) 0x02);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager10.entityMetadata((byte) 0), 20L);
                    }
                }.runTaskLater(plugin, 40L);
                new BukkitRunnable(){
                    public void run(){
                        npcManager10.entityMetadata((byte) 0x02);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager10.animation(0), 20L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcManager10.entityMetadata((byte) 0), 40L);
                    }
                }.runTaskLater(plugin, 80L);
            }
        }.runTaskLater(plugin, 20L);

        return false;
    }
}

