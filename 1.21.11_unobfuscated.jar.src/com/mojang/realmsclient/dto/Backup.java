/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.time.Instant;
/*    */ import java.time.ZoneId;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Backup
/*    */   extends ValueObject
/*    */ {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public final String backupId;
/*    */   
/*    */   public final Instant lastModified;
/*    */   
/*    */   public final long size;
/*    */   
/*    */   public boolean uploadedVersion;
/*    */   
/*    */   public final Map<String, String> metadata;
/* 32 */   public final Map<String, String> changeList = new HashMap<>();
/*    */   
/*    */   private Backup(String backupId, Instant lastModified, long size, Map<String, String> metadata) {
/* 35 */     this.backupId = backupId;
/* 36 */     this.lastModified = lastModified;
/* 37 */     this.size = size;
/* 38 */     this.metadata = metadata;
/*    */   }
/*    */   
/*    */   public ZonedDateTime lastModifiedDate() {
/* 42 */     return ZonedDateTime.ofInstant(this.lastModified, ZoneId.systemDefault());
/*    */   }
/*    */   
/*    */   public static Backup parse(JsonElement node) {
/* 46 */     JsonObject object = node.getAsJsonObject();
/*    */     try {
/* 48 */       String backupId = JsonUtils.getStringOr("backupId", object, "");
/* 49 */       Instant lastModifiedDate = JsonUtils.getDateOr("lastModifiedDate", object);
/* 50 */       long size = JsonUtils.getLongOr("size", object, 0L);
/* 51 */       Map<String, String> metadata = new HashMap<>();
/* 52 */       if (object.has("metadata")) {
/* 53 */         JsonObject metadataObject = object.getAsJsonObject("metadata");
/* 54 */         Set<Map.Entry<String, JsonElement>> jsonElementSet = metadataObject.entrySet();
/* 55 */         for (Map.Entry<String, JsonElement> elem : jsonElementSet) {
/* 56 */           if (!((JsonElement)elem.getValue()).isJsonNull()) {
/* 57 */             metadata.put(elem.getKey(), ((JsonElement)elem.getValue()).getAsString());
/*    */           }
/*    */         } 
/*    */       } 
/* 61 */       return new Backup(backupId, lastModifiedDate, size, metadata);
/* 62 */     } catch (Exception e) {
/* 63 */       LOGGER.error("Could not parse Backup", e);
/*    */       
/* 65 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/Backup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */