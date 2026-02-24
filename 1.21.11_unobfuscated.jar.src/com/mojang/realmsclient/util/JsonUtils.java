/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.util.UndashedUuid;
/*    */ import java.time.Instant;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ public class JsonUtils
/*    */ {
/*    */   public static <T> T getRequired(String key, JsonObject node, Function<JsonObject, T> parser) {
/* 15 */     JsonElement property = node.get(key);
/* 16 */     if (property == null || property.isJsonNull())
/* 17 */       throw new IllegalStateException("Missing required property: " + key); 
/* 18 */     if (!property.isJsonObject()) {
/* 19 */       throw new IllegalStateException("Required property " + key + " was not a JsonObject as espected");
/*    */     }
/* 21 */     return parser.apply(property.getAsJsonObject());
/*    */   }
/*    */   
/*    */   public static <T> T getOptional(String key, JsonObject node, Function<JsonObject, T> parser) {
/* 25 */     JsonElement property = node.get(key);
/* 26 */     if (property == null || property.isJsonNull())
/* 27 */       return null; 
/* 28 */     if (!property.isJsonObject()) {
/* 29 */       throw new IllegalStateException("Required property " + key + " was not a JsonObject as espected");
/*    */     }
/* 31 */     return parser.apply(property.getAsJsonObject());
/*    */   }
/*    */   
/*    */   public static String getRequiredString(String key, JsonObject node) {
/* 35 */     String result = getStringOr(key, node, null);
/* 36 */     if (result == null) {
/* 37 */       throw new IllegalStateException("Missing required property: " + key);
/*    */     }
/* 39 */     return result;
/*    */   }
/*    */   
/*    */   @Contract("_,_,!null->!null;_,_,null->_")
/*    */   public static String getStringOr(String key, JsonObject node, String defaultValue) {
/* 44 */     JsonElement element = node.get(key);
/* 45 */     if (element != null) {
/* 46 */       return element.isJsonNull() ? defaultValue : element.getAsString();
/*    */     }
/* 48 */     return defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   @Contract("_,_,!null->!null;_,_,null->_")
/*    */   public static UUID getUuidOr(String key, JsonObject node, UUID defaultValue) {
/* 54 */     String uuidAsString = getStringOr(key, node, null);
/* 55 */     if (uuidAsString == null) {
/* 56 */       return defaultValue;
/*    */     }
/* 58 */     return UndashedUuid.fromStringLenient(uuidAsString);
/*    */   }
/*    */   
/*    */   public static int getIntOr(String key, JsonObject node, int defaultValue) {
/* 62 */     JsonElement element = node.get(key);
/* 63 */     if (element != null) {
/* 64 */       return element.isJsonNull() ? defaultValue : element.getAsInt();
/*    */     }
/* 66 */     return defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public static long getLongOr(String key, JsonObject node, long defaultValue) {
/* 71 */     JsonElement element = node.get(key);
/* 72 */     if (element != null) {
/* 73 */       return element.isJsonNull() ? defaultValue : element.getAsLong();
/*    */     }
/* 75 */     return defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean getBooleanOr(String key, JsonObject node, boolean defaultValue) {
/* 80 */     JsonElement element = node.get(key);
/* 81 */     if (element != null) {
/* 82 */       return element.isJsonNull() ? defaultValue : element.getAsBoolean();
/*    */     }
/* 84 */     return defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Instant getDateOr(String key, JsonObject node) {
/* 89 */     JsonElement element = node.get(key);
/* 90 */     if (element != null) {
/* 91 */       return Instant.ofEpochMilli(Long.parseLong(element.getAsString()));
/*    */     }
/* 93 */     return Instant.EPOCH;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/JsonUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */