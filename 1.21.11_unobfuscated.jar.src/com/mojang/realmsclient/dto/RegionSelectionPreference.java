/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public enum RegionSelectionPreference
/*    */ {
/* 12 */   AUTOMATIC_PLAYER(0, "realms.configuration.region_preference.automatic_player"),
/* 13 */   AUTOMATIC_OWNER(1, "realms.configuration.region_preference.automatic_owner"),
/* 14 */   MANUAL(2, "");
/*    */   
/* 16 */   public static final RegionSelectionPreference DEFAULT_SELECTION = AUTOMATIC_PLAYER;
/*    */   
/*    */   public final int id;
/*    */   public final String translationKey;
/*    */   
/*    */   RegionSelectionPreference(int id, String translationKey) {
/* 22 */     this.id = id;
/* 23 */     this.translationKey = translationKey;
/*    */   }
/*    */   
/*    */   public static class RegionSelectionPreferenceJsonAdapter extends TypeAdapter<RegionSelectionPreference> {
/* 27 */     private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */     
/*    */     public void write(JsonWriter jsonWriter, RegionSelectionPreference regionSelectionPreference) throws IOException {
/* 31 */       jsonWriter.value(regionSelectionPreference.id);
/*    */     }
/*    */ 
/*    */     
/*    */     public RegionSelectionPreference read(JsonReader jsonReader) throws IOException {
/* 36 */       int id = jsonReader.nextInt();
/* 37 */       for (RegionSelectionPreference value : RegionSelectionPreference.values()) {
/* 38 */         if (value.id == id) {
/* 39 */           return value;
/*    */         }
/*    */       } 
/* 42 */       LOGGER.warn("Unsupported RegionSelectionPreference {}", id);
/* 43 */       return RegionSelectionPreference.DEFAULT_SELECTION;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RegionSelectionPreference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */