/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.debug.DebugGameEventInfo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GameEventDispatcher
/*    */ {
/*    */   public GameEventDispatcher(ServerLevel level) {
/* 19 */     this.level = level;
/*    */   }
/*    */   private final ServerLevel level;
/*    */   public void post(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
/* 23 */     int radius = ((GameEvent)gameEvent.value()).notificationRadius();
/* 24 */     BlockPos center = BlockPos.containing((Position)position);
/* 25 */     int sectionMinX = SectionPos.blockToSectionCoord(center.getX() - radius);
/* 26 */     int sectionMinY = SectionPos.blockToSectionCoord(center.getY() - radius);
/* 27 */     int sectionMinZ = SectionPos.blockToSectionCoord(center.getZ() - radius);
/* 28 */     int sectionMaxX = SectionPos.blockToSectionCoord(center.getX() + radius);
/* 29 */     int sectionMaxY = SectionPos.blockToSectionCoord(center.getY() + radius);
/* 30 */     int sectionMaxZ = SectionPos.blockToSectionCoord(center.getZ() + radius);
/*    */     
/* 32 */     List<GameEvent.ListenerInfo> toHandleByDistance = new ArrayList<>();
/*    */     
/*    */     GameEventListenerRegistry.ListenerVisitor visitListeners = (listener, pos) -> {
/*    */         if (gameEvent.getDeliveryMode() == GameEventListener.DeliveryMode.BY_DISTANCE) {
/*    */           toHandleByDistance.add(new GameEvent.ListenerInfo(toHandleByDistance, toHandleByDistance, gameEvent, gameEvent, pos));
/*    */         } else {
/*    */           gameEvent.handleGameEvent(this.level, toHandleByDistance, gameEvent, toHandleByDistance);
/*    */         } 
/*    */       };
/*    */     
/*    */     boolean applicable = false;
/* 43 */     for (int chunkX = sectionMinX; chunkX <= sectionMaxX; chunkX++) {
/* 44 */       for (int chunkZ = sectionMinZ; chunkZ <= sectionMaxZ; chunkZ++) {
/* 45 */         LevelChunk levelChunk = this.level.getChunkSource().getChunkNow(chunkX, chunkZ);
/*    */         
/* 47 */         if (levelChunk != null) {
/* 48 */           for (int section = sectionMinY; section <= sectionMaxY; section++) {
/* 49 */             applicable |= levelChunk.getListenerRegistry(section).visitInRangeListeners(gameEvent, position, context, visitListeners);
/*    */           }
/*    */         }
/*    */       } 
/*    */     } 
/* 54 */     if (!toHandleByDistance.isEmpty()) {
/* 55 */       handleGameEventMessagesInQueue(toHandleByDistance);
/*    */     }
/* 57 */     if (applicable) {
/* 58 */       this.level.debugSynchronizers().broadcastEventToTracking(BlockPos.containing((Position)position), DebugSubscriptions.GAME_EVENTS, new DebugGameEventInfo(gameEvent, position));
/*    */     }
/*    */   }
/*    */   
/*    */   private void handleGameEventMessagesInQueue(List<GameEvent.ListenerInfo> listenerInfos) {
/* 63 */     Collections.sort(listenerInfos);
/* 64 */     for (GameEvent.ListenerInfo listenerInfo : listenerInfos) {
/* 65 */       GameEventListener listener = listenerInfo.recipient();
/* 66 */       listener.handleGameEvent(this.level, listenerInfo.gameEvent(), listenerInfo.context(), listenerInfo.source());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/GameEventDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */