/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public enum ServiceQuality
/*    */ {
/* 14 */   GREAT(1, "icon/ping_5"),
/* 15 */   GOOD(2, "icon/ping_4"),
/* 16 */   OKAY(3, "icon/ping_3"),
/* 17 */   POOR(4, "icon/ping_2"),
/* 18 */   UNKNOWN(5, "icon/ping_unknown");
/*    */   
/*    */   private final int value;
/*    */   private final Identifier icon;
/*    */   
/*    */   ServiceQuality(int value, String iconPath) {
/* 24 */     this.value = value;
/* 25 */     this.icon = Identifier.withDefaultNamespace(iconPath);
/*    */   }
/*    */   
/*    */   public static ServiceQuality byValue(int value) {
/* 29 */     for (ServiceQuality quality : values()) {
/* 30 */       if (quality.getValue() == value) {
/* 31 */         return quality;
/*    */       }
/*    */     } 
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 38 */     return this.value;
/*    */   }
/*    */   
/*    */   public Identifier getIcon() {
/* 42 */     return this.icon;
/*    */   }
/*    */   
/*    */   public static class RealmsServiceQualityJsonAdapter extends TypeAdapter<ServiceQuality> {
/* 46 */     private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */     
/*    */     public void write(JsonWriter jsonWriter, ServiceQuality quality) throws IOException {
/* 50 */       jsonWriter.value(quality.value);
/*    */     }
/*    */ 
/*    */     
/*    */     public ServiceQuality read(JsonReader jsonReader) throws IOException {
/* 55 */       int value = jsonReader.nextInt();
/* 56 */       ServiceQuality quality = ServiceQuality.byValue(value);
/* 57 */       if (quality == null) {
/* 58 */         LOGGER.warn("Unsupported ServiceQuality {}", value);
/* 59 */         return ServiceQuality.UNKNOWN;
/*    */       } 
/* 61 */       return quality;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/ServiceQuality.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */