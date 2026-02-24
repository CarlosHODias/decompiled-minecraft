/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public enum RealmsRegion
/*    */ {
/* 13 */   AUSTRALIA_EAST("AustraliaEast", "realms.configuration.region.australia_east"),
/* 14 */   AUSTRALIA_SOUTHEAST("AustraliaSoutheast", "realms.configuration.region.australia_southeast"),
/* 15 */   BRAZIL_SOUTH("BrazilSouth", "realms.configuration.region.brazil_south"),
/* 16 */   CENTRAL_INDIA("CentralIndia", "realms.configuration.region.central_india"),
/* 17 */   CENTRAL_US("CentralUs", "realms.configuration.region.central_us"),
/* 18 */   EAST_ASIA("EastAsia", "realms.configuration.region.east_asia"),
/* 19 */   EAST_US("EastUs", "realms.configuration.region.east_us"),
/* 20 */   EAST_US_2("EastUs2", "realms.configuration.region.east_us_2"),
/* 21 */   FRANCE_CENTRAL("FranceCentral", "realms.configuration.region.france_central"),
/* 22 */   JAPAN_EAST("JapanEast", "realms.configuration.region.japan_east"),
/* 23 */   JAPAN_WEST("JapanWest", "realms.configuration.region.japan_west"),
/* 24 */   KOREA_CENTRAL("KoreaCentral", "realms.configuration.region.korea_central"),
/* 25 */   NORTH_CENTRAL_US("NorthCentralUs", "realms.configuration.region.north_central_us"),
/* 26 */   NORTH_EUROPE("NorthEurope", "realms.configuration.region.north_europe"),
/* 27 */   SOUTH_CENTRAL_US("SouthCentralUs", "realms.configuration.region.south_central_us"),
/* 28 */   SOUTHEAST_ASIA("SoutheastAsia", "realms.configuration.region.southeast_asia"),
/* 29 */   SWEDEN_CENTRAL("SwedenCentral", "realms.configuration.region.sweden_central"),
/* 30 */   UAE_NORTH("UAENorth", "realms.configuration.region.uae_north"),
/* 31 */   UK_SOUTH("UKSouth", "realms.configuration.region.uk_south"),
/* 32 */   WEST_CENTRAL_US("WestCentralUs", "realms.configuration.region.west_central_us"),
/* 33 */   WEST_EUROPE("WestEurope", "realms.configuration.region.west_europe"),
/* 34 */   WEST_US("WestUs", "realms.configuration.region.west_us"),
/* 35 */   WEST_US_2("WestUs2", "realms.configuration.region.west_us_2"),
/* 36 */   INVALID_REGION("invalid", "");
/*    */   
/*    */   public final String nameId;
/*    */   public final String translationKey;
/*    */   
/*    */   RealmsRegion(String nameId, String translationKey) {
/* 42 */     this.nameId = nameId;
/* 43 */     this.translationKey = translationKey;
/*    */   }
/*    */   
/*    */   public static RealmsRegion findByNameId(String nameIdStr) {
/* 47 */     for (RealmsRegion value : values()) {
/* 48 */       if (value.nameId.equals(nameIdStr)) {
/* 49 */         return value;
/*    */       }
/*    */     } 
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public static class RealmsRegionJsonAdapter extends TypeAdapter<RealmsRegion> {
/* 56 */     private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */     
/*    */     public void write(JsonWriter jsonWriter, RealmsRegion realmsRegion) throws IOException {
/* 60 */       jsonWriter.value(realmsRegion.nameId);
/*    */     }
/*    */ 
/*    */     
/*    */     public RealmsRegion read(JsonReader jsonReader) throws IOException {
/* 65 */       String nameId = jsonReader.nextString();
/* 66 */       RealmsRegion realmsRegion = RealmsRegion.findByNameId(nameId);
/* 67 */       if (realmsRegion == null) {
/* 68 */         LOGGER.warn("Unsupported RealmsRegion {}", nameId);
/* 69 */         return RealmsRegion.INVALID_REGION;
/*    */       } 
/* 71 */       return realmsRegion;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */