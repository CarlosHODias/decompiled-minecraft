/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientChunkCache;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.server.IntegratedServer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerChunkCache;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ChunkDebugRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   private final Minecraft minecraft;
/* 28 */   private double lastUpdateTime = Double.MIN_VALUE;
/* 29 */   private final int radius = 12;
/*    */   private ChunkData data;
/*    */   
/*    */   public ChunkDebugRenderer(Minecraft minecraft) {
/* 33 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 38 */     double time = Util.getNanos();
/* 39 */     if (time - this.lastUpdateTime > 3.0E9D) {
/* 40 */       this.lastUpdateTime = time;
/*    */       
/* 42 */       IntegratedServer server = this.minecraft.getSingleplayerServer();
/*    */       
/* 44 */       if (server != null) {
/* 45 */         this.data = new ChunkData(this, server, camX, camZ);
/*    */       } else {
/* 47 */         this.data = null;
/*    */       } 
/*    */     } 
/*    */     
/* 51 */     if (this.data != null) {
/* 52 */       Map<ChunkPos, String> serverData = this.data.serverData.getNow(null);
/* 53 */       double y = (this.minecraft.gameRenderer.getMainCamera().position()).y * 0.85D;
/* 54 */       for (Map.Entry<ChunkPos, String> entry : this.data.clientData.entrySet()) {
/* 55 */         ChunkPos pos = entry.getKey();
/* 56 */         String value = entry.getValue();
/* 57 */         if (serverData != null) {
/* 58 */           value = value + value;
/*    */         }
/* 60 */         String[] parts = value.split("\n");
/* 61 */         int yOffset = 0;
/* 62 */         for (String part : parts) {
/* 63 */           Gizmos.billboardText(part, new Vec3(SectionPos.sectionToBlockCoord(pos.x, 8), y + yOffset, SectionPos.sectionToBlockCoord(pos.z, 8)), TextGizmo.Style.whiteAndCentered().withScale(2.4F)).setAlwaysOnTop();
/* 64 */           yOffset -= 2;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private final class ChunkData {
/*    */     private final Map<ChunkPos, String> clientData;
/*    */     private final CompletableFuture<Map<ChunkPos, String>> serverData;
/*    */     
/*    */     private ChunkData(ChunkDebugRenderer this$0, IntegratedServer server, double camX, double camZ) {
/* 75 */       ClientLevel clientLevel = this$0.minecraft.level;
/* 76 */       ResourceKey<Level> dimension = clientLevel.dimension();
/* 77 */       int cx = SectionPos.posToSectionCoord(camX);
/* 78 */       int cz = SectionPos.posToSectionCoord(camZ);
/*    */       
/* 80 */       ImmutableMap.Builder<ChunkPos, String> builder = ImmutableMap.builder();
/* 81 */       ClientChunkCache clientChunkSource = clientLevel.getChunkSource();
/* 82 */       for (int x = cx - 12; x <= cx + 12; x++) {
/* 83 */         for (int z = cz - 12; z <= cz + 12; z++) {
/* 84 */           ChunkPos pos = new ChunkPos(x, z);
/* 85 */           String result = "";
/* 86 */           LevelChunk clientChunk = clientChunkSource.getChunk(x, z, false);
/* 87 */           result = result + "Client: ";
/* 88 */           if (clientChunk == null) {
/* 89 */             result = result + "0n/a\n";
/*    */           } else {
/* 91 */             result = result + result;
/* 92 */             result = result + "\n";
/*    */           } 
/* 94 */           builder.put(pos, result);
/*    */         } 
/*    */       } 
/* 97 */       this.clientData = (Map<ChunkPos, String>)builder.build();
/* 98 */       this.serverData = server.submit(() -> {
/*    */             ServerLevel serverLevel = server.getLevel(dimension);
/*    */             if (serverLevel == null)
/*    */               return ImmutableMap.of(); 
/*    */             ImmutableMap.Builder<ChunkPos, String> serverBuilder = ImmutableMap.builder();
/*    */             ServerChunkCache serverChunkSource = serverLevel.getChunkSource();
/*    */             for (int x = cx - 12; x <= cx + 12; x++) {
/*    */               for (int z = cz - 12; z <= cz + 12; z++) {
/*    */                 ChunkPos pos = new ChunkPos(x, z);
/*    */                 serverBuilder.put(pos, "Server: " + serverChunkSource.getChunkDebugData(pos));
/*    */               } 
/*    */             } 
/*    */             return serverBuilder.build();
/*    */           });
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/ChunkDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */