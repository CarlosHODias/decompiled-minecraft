/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Reader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.client.multiplayer.ClientRegistryLayer;
/*    */ import net.minecraft.client.renderer.item.ClientItem;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.PlaceholderLookupProvider;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ClientItemInfoLoader {
/* 28 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 29 */   private static final FileToIdConverter LISTER = FileToIdConverter.json("items");
/*    */   
/*    */   public static CompletableFuture<LoadedClientInfos> scheduleLoad(ResourceManager manager, Executor executor) {
/* 32 */     RegistryAccess.Frozen staticRegistries = ClientRegistryLayer.createRegistryAccess().compositeAccess();
/*    */     
/* 34 */     return CompletableFuture.supplyAsync(() -> LISTER.listMatchingResources(manager), executor)
/* 35 */       .thenCompose(resources -> {
/*    */           List<CompletableFuture<PendingLoad>> pendingLoads = new ArrayList<>(resources.size());
/*    */           resources.forEach(());
/*    */           return Util.sequence(pendingLoads).thenApply(());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final class PendingLoad
/*    */     extends Record
/*    */   {
/*    */     private final Identifier id;
/*    */ 
/*    */     
/*    */     private final ClientItem clientItemInfo;
/*    */ 
/*    */ 
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     private PendingLoad(Identifier id, ClientItem clientItemInfo)
/*    */     {
/* 75 */       this.id = id; this.clientItemInfo = clientItemInfo; } public Identifier id() { return this.id; } public ClientItem clientItemInfo() { return this.clientItemInfo; }
/*    */      }
/*    */   
/* 78 */   public static final class LoadedClientInfos extends Record { public LoadedClientInfos(Map<Identifier, ClientItem> contents) { this.contents = contents; } public Map<Identifier, ClientItem> contents() { return this.contents; }
/*    */ 
/*    */     
/*    */     private final Map<Identifier, ClientItem> contents;
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ClientItemInfoLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */