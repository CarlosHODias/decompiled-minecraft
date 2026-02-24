/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class RealmsServerPlayerLists extends Record {
/*    */   private final Map<Long, List<ResolvableProfile>> servers;
/*    */   
/*    */   public RealmsServerPlayerLists(Map<Long, List<ResolvableProfile>> servers) {
/* 24 */     this.servers = servers; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 24 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists; } public Map<Long, List<ResolvableProfile>> servers() { return this.servers; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsServerPlayerLists;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 27 */   } private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static RealmsServerPlayerLists parse(String json) {
/* 30 */     ImmutableMap.Builder<Long, List<ResolvableProfile>> elements = ImmutableMap.builder();
/*    */     
/*    */     try {
/* 33 */       JsonObject object = GsonHelper.parse(json);
/*    */       
/* 35 */       if (GsonHelper.isArrayNode(object, "lists")) {
/* 36 */         JsonArray jsonArray = object.getAsJsonArray("lists");
/*    */         
/* 38 */         for (JsonElement jsonElement : (Iterable<JsonElement>)jsonArray) {
/*    */           List<ResolvableProfile> players;
/* 40 */           JsonObject node = jsonElement.getAsJsonObject();
/* 41 */           String playerListString = JsonUtils.getStringOr("playerList", node, null);
/*    */           
/* 43 */           if (playerListString != null) {
/* 44 */             JsonElement element = LenientJsonParser.parse(playerListString);
/*    */             
/* 46 */             if (element.isJsonArray()) {
/* 47 */               players = parsePlayers(element.getAsJsonArray());
/*    */             } else {
/* 49 */               players = Lists.newArrayList();
/*    */             } 
/*    */           } else {
/* 52 */             players = Lists.newArrayList();
/*    */           } 
/*    */           
/* 55 */           elements.put(JsonUtils.getLongOr("serverId", node, -1L), players);
/*    */         } 
/*    */       } 
/* 58 */     } catch (Exception e) {
/* 59 */       LOGGER.error("Could not parse RealmsServerPlayerLists", e);
/*    */     } 
/*    */     
/* 62 */     return new RealmsServerPlayerLists((Map<Long, List<ResolvableProfile>>)elements.build());
/*    */   }
/*    */   
/*    */   private static List<ResolvableProfile> parsePlayers(JsonArray array) {
/* 66 */     List<ResolvableProfile> profiles = new ArrayList<>(array.size());
/*    */     
/* 68 */     for (JsonElement element : (Iterable<JsonElement>)array) {
/* 69 */       if (element.isJsonObject()) {
/* 70 */         UUID playerId = JsonUtils.getUuidOr("playerId", element.getAsJsonObject(), null);
/* 71 */         if (playerId != null && !Minecraft.getInstance().isLocalPlayer(playerId)) {
/* 72 */           profiles.add(ResolvableProfile.createUnresolved(playerId));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 77 */     return profiles;
/*    */   }
/*    */   
/*    */   public List<ResolvableProfile> getProfileResultsFor(long serverId) {
/* 81 */     List<ResolvableProfile> profileResults = this.servers.get(serverId);
/* 82 */     if (profileResults != null) {
/* 83 */       return profileResults;
/*    */     }
/* 85 */     return List.of();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsServerPlayerLists.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */