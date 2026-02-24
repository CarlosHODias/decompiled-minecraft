/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ 
/*    */ public class UploadTokenCache
/*    */ {
/*  8 */   private static final Long2ObjectMap<String> TOKEN_CACHE = (Long2ObjectMap<String>)new Long2ObjectOpenHashMap();
/*    */   
/*    */   public static String get(long realmId) {
/* 11 */     return (String)TOKEN_CACHE.get(realmId);
/*    */   }
/*    */   
/*    */   public static void invalidate(long realmId) {
/* 15 */     TOKEN_CACHE.remove(realmId);
/*    */   }
/*    */   
/*    */   public static void put(long realmId, String token) {
/* 19 */     TOKEN_CACHE.put(realmId, token);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/UploadTokenCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */