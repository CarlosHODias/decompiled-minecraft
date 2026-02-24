/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerList {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   private static final ConsecutiveExecutor IO_EXECUTOR = new ConsecutiveExecutor((Executor)Util.backgroundExecutor(), "server-list-io");
/*     */   
/*     */   private static final int MAX_HIDDEN_SERVERS = 16;
/*     */   private final Minecraft minecraft;
/*  26 */   private final List<ServerData> serverList = Lists.newArrayList();
/*  27 */   private final List<ServerData> hiddenServerList = Lists.newArrayList();
/*     */   
/*     */   public ServerList(Minecraft minecraft) {
/*  30 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void load() {
/*     */     try {
/*  35 */       this.serverList.clear();
/*  36 */       this.hiddenServerList.clear();
/*     */       
/*  38 */       CompoundTag tag = NbtIo.read(this.minecraft.gameDirectory.toPath().resolve("servers.dat"));
/*  39 */       if (tag == null) {
/*     */         return;
/*     */       }
/*     */       
/*  43 */       tag.getListOrEmpty("servers").compoundStream().forEach(serverTag -> {
/*     */             ServerData serverData = ServerData.read(serverTag);
/*     */             
/*     */             if (serverTag.getBooleanOr("hidden", false)) {
/*     */               this.hiddenServerList.add(serverData);
/*     */             } else {
/*     */               this.serverList.add(serverData);
/*     */             } 
/*     */           });
/*  52 */     } catch (Exception e) {
/*  53 */       LOGGER.error("Couldn't load server list", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void save() {
/*     */     try {
/*  59 */       ListTag serverTags = new ListTag();
/*  60 */       for (ServerData server : this.serverList) {
/*  61 */         CompoundTag serverTag = server.write();
/*     */         
/*  63 */         serverTag.putBoolean("hidden", false);
/*  64 */         serverTags.add(serverTag);
/*     */       } 
/*  66 */       for (ServerData server : this.hiddenServerList) {
/*  67 */         CompoundTag serverTag = server.write();
/*     */         
/*  69 */         serverTag.putBoolean("hidden", true);
/*  70 */         serverTags.add(serverTag);
/*     */       } 
/*     */       
/*  73 */       CompoundTag tag = new CompoundTag();
/*  74 */       tag.put("servers", (Tag)serverTags);
/*     */       
/*  76 */       Path gameDirectoryPath = this.minecraft.gameDirectory.toPath();
/*  77 */       Path newFile = Files.createTempFile(gameDirectoryPath, "servers", ".dat", (FileAttribute<?>[])new FileAttribute[0]);
/*  78 */       NbtIo.write(tag, newFile);
/*     */       
/*  80 */       Path oldFile = gameDirectoryPath.resolve("servers.dat_old");
/*  81 */       Path currentFile = gameDirectoryPath.resolve("servers.dat");
/*  82 */       Util.safeReplaceFile(currentFile, newFile, oldFile);
/*  83 */     } catch (Exception e) {
/*  84 */       LOGGER.error("Couldn't save server list", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ServerData get(int index) {
/*  89 */     return this.serverList.get(index);
/*     */   }
/*     */   
/*     */   public ServerData get(String ip) {
/*  93 */     for (ServerData serverData : this.serverList) {
/*  94 */       if (serverData.ip.equals(ip)) {
/*  95 */         return serverData;
/*     */       }
/*     */     } 
/*  98 */     for (ServerData serverData : this.hiddenServerList) {
/*  99 */       if (serverData.ip.equals(ip)) {
/* 100 */         return serverData;
/*     */       }
/*     */     } 
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public ServerData unhide(String ip) {
/* 107 */     for (int i = 0; i < this.hiddenServerList.size(); i++) {
/* 108 */       ServerData serverData = this.hiddenServerList.get(i);
/* 109 */       if (serverData.ip.equals(ip)) {
/* 110 */         this.hiddenServerList.remove(i);
/* 111 */         this.serverList.add(serverData);
/* 112 */         return serverData;
/*     */       } 
/*     */     } 
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public void remove(ServerData thing) {
/* 119 */     if (!this.serverList.remove(thing)) {
/* 120 */       this.hiddenServerList.remove(thing);
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(ServerData server, boolean hidden) {
/* 125 */     if (hidden) {
/* 126 */       this.hiddenServerList.add(0, server);
/*     */       
/* 128 */       while (this.hiddenServerList.size() > 16) {
/* 129 */         this.hiddenServerList.remove(this.hiddenServerList.size() - 1);
/*     */       }
/*     */     } else {
/* 132 */       this.serverList.add(server);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/* 137 */     return this.serverList.size();
/*     */   }
/*     */   
/*     */   public void swap(int a, int b) {
/* 141 */     ServerData swap = get(a);
/* 142 */     this.serverList.set(a, get(b));
/* 143 */     this.serverList.set(b, swap);
/* 144 */     save();
/*     */   }
/*     */   
/*     */   public void replace(int id, ServerData data) {
/* 148 */     this.serverList.set(id, data);
/*     */   }
/*     */   
/*     */   private static boolean set(ServerData data, List<ServerData> list) {
/* 152 */     for (int i = 0; i < list.size(); i++) {
/* 153 */       ServerData target = list.get(i);
/*     */       
/* 155 */       if (Objects.equals(target.name, data.name) && target.ip.equals(data.ip)) {
/* 156 */         list.set(i, data);
/* 157 */         return true;
/*     */       } 
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   public static void saveSingleServer(ServerData data) {
/* 164 */     IO_EXECUTOR.schedule(() -> {
/*     */           ServerList list = new ServerList(Minecraft.getInstance());
/*     */           list.load();
/*     */           if (!set(data, list.serverList))
/*     */             set(data, list.hiddenServerList); 
/*     */           list.save();
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ServerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */