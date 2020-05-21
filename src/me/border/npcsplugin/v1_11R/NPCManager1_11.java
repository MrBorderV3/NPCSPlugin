package me.border.npcsplugin.v1_11R;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;


class NPCManager1_11 {


    public NPCManager1_11(String name, Location location) {
        entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
        gameprofile = new GameProfile(UUID.randomUUID(), name);
        this.location = location.clone();
        nmsServer = ((CraftServer)Bukkit.getServer()).getServer();;
        nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        npc = new EntityPlayer(nmsServer, nmsWorld, this.gameprofile, new PlayerInteractManager(nmsWorld));
    }

    private int entityID;
    private Location location;
    private GameProfile gameprofile;
    private EntityPlayer npc;
    private WorldServer nmsWorld;
    private MinecraftServer nmsServer;

    public void animation(int animation) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        setValue(packet, "a", entityID);
        setValue(packet, "b", (byte) animation);
        sendPacket(packet);
    }

    public void spawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityID);
        setValue(packet, "b", gameprofile.getId());
        setValue(packet, "c", location.getX());
        setValue(packet, "d", location.getY());
        setValue(packet, "e", location.getZ());
        setValue(packet, "f", location.getYaw());
        setValue(packet, "g", location.getPitch());
        DataWatcher watcher = npc.getDataWatcher();
        watcher.set(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 0xFF);
        setValue(packet, "h", watcher);
        addToTablist();
        sendPacket(packet);
    }

    public void entityMetadata(byte m) {
        try {
            EntityEgg fakeEntity = new EntityEgg(null);
            Field field = Entity.class.getDeclaredField("Z");
            field.setAccessible(true);
            DataWatcherObject<Byte> datawatcherObject = (DataWatcherObject<Byte>) field.get(null);
            DataWatcher dataWatcher = new DataWatcher(fakeEntity);
            dataWatcher.register(datawatcherObject, m);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityID, dataWatcher, true);
            sendPacket(packet);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void teleport(Location location) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        setValue(packet, "a", entityID);
        setValue(packet, "b", location.getX());
        setValue(packet, "c", location.getY());
        setValue(packet, "d", location.getZ());
        setValue(packet, "e", getFixRotation(location.getYaw()));
        setValue(packet, "f", getFixRotation(location.getPitch()));
        sendPacket(packet);
        headRotation(location.getYaw(), location.getPitch());
        this.location = location.clone();
    }

    public void headRotation(float yaw, float pitch) {
        PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityID, getFixRotation(yaw), getFixRotation(pitch), true);
        PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
        setValue(packetHead, "a", entityID);
        setValue(packetHead, "b", getFixRotation(yaw));
        sendPacket(packet);
        sendPacket(packetHead);
    }

    public void addToTablist(){
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
        sendPacket(packet);
    }

    public int getFixLocation(double pos) {
        return (int) MathHelper.floor(pos * 32.0D);
    }

    public byte getFixRotation(float yawpitch) {
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

    public void setValue(Object obj,String name,Object value){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }catch(Exception e){}
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }

    public void sendPacket(Packet<?> packet, Player player){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendPacket(Packet<?> packet){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendPacket(packet,player);
        }
    }
}
