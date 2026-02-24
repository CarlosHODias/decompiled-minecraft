/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.dto.GuardedSerializer;
/*    */ import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class RealmsPersistence
/*    */ {
/*    */   private static final String FILE_NAME = "realms_persistence.json";
/* 18 */   private static final GuardedSerializer GSON = new GuardedSerializer();
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public RealmsPersistenceData read() {
/* 22 */     return readFile();
/*    */   }
/*    */   
/*    */   public void save(RealmsPersistenceData data) {
/* 26 */     writeFile(data);
/*    */   }
/*    */   
/*    */   public static RealmsPersistenceData readFile() {
/* 30 */     Path file = getPathToData();
/*    */ 
/*    */     
/* 33 */     try { String contents = Files.readString(file, StandardCharsets.UTF_8);
/* 34 */       RealmsPersistenceData realmsPersistenceData = (RealmsPersistenceData)GSON.fromJson(contents, RealmsPersistenceData.class);
/*    */       
/* 36 */       if (realmsPersistenceData != null) {
/* 37 */         return realmsPersistenceData;
/*    */       } }
/* 39 */     catch (NoSuchFileException noSuchFileException) {  }
/* 40 */     catch (Exception e)
/* 41 */     { LOGGER.warn("Failed to read Realms storage {}", file, e); }
/*    */ 
/*    */     
/* 44 */     return new RealmsPersistenceData();
/*    */   }
/*    */   
/*    */   public static void writeFile(RealmsPersistenceData data) {
/* 48 */     Path file = getPathToData();
/*    */     
/*    */     try {
/* 51 */       Files.writeString(file, GSON.toJson(data), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/* 52 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   private static Path getPathToData() {
/* 57 */     return (Minecraft.getInstance()).gameDirectory.toPath().resolve("realms_persistence.json");
/*    */   }
/*    */   
/*    */   public static class RealmsPersistenceData implements ReflectionBasedSerialization {
/*    */     @SerializedName("newsLink")
/*    */     public String newsLink;
/*    */     @SerializedName("hasUnreadNews")
/*    */     public boolean hasUnreadNews;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/RealmsPersistence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */