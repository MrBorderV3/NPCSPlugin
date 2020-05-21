package me.border.npcsplugin.v1_8R;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;


import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;


 class NPCManager1_8 {

     public NPCManager1_8(String name, Location location) {
         entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
         gameprofile = new GameProfile(UUID.randomUUID(), name);
         this.location = location.clone();
     }

     private int entityID;
     private Location location;
     private GameProfile gameprofile;

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
         setValue(packet, "c", getFixLocation(location.getX()));
         setValue(packet, "d", getFixLocation(location.getY()));
         setValue(packet, "e", getFixLocation(location.getZ()));
         setValue(packet, "f", getFixRotation(location.getYaw()));
         setValue(packet, "g", getFixRotation(location.getPitch()));
         setValue(packet, "h", 0);
         DataWatcher w = new DataWatcher(null);
         w.a(6, (float) 20);
         w.a(10, (byte) 127);
         setValue(packet, "i", w);
         addToTablist();
         sendPacket(packet);
     }

     public void entityMetadata(int i, byte m) {
         EntityEgg fakeEntity = new EntityEgg(null);
         DataWatcher dataWatcher = new DataWatcher(fakeEntity);
         dataWatcher.a(i, m);
         PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityID, dataWatcher, true);
         sendPacket(packet);
     }

     public void teleport(Location location) {
         PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
         setValue(packet, "a", entityID);
         setValue(packet, "b", getFixLocation(location.getX()));
         setValue(packet, "c", getFixLocation(location.getY()));
         setValue(packet, "d", getFixLocation(location.getZ()));
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

     public void addToTablist() {
         PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
         PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
         @SuppressWarnings("unchecked")
         List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
         players.add(data);
         setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
         setValue(packet, "b", players);
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
