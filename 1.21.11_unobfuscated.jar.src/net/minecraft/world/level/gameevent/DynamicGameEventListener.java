/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicGameEventListener<T extends GameEventListener>
/*    */ {
/*    */   private final T listener;
/*    */   private SectionPos lastSection;
/*    */   
/*    */   public DynamicGameEventListener(T listener) {
/* 21 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   public void add(ServerLevel level) {
/* 25 */     move(level);
/*    */   }
/*    */   
/*    */   public T getListener() {
/* 29 */     return this.listener;
/*    */   }
/*    */   
/*    */   public void remove(ServerLevel level) {
/* 33 */     ifChunkExists((LevelReader)level, this.lastSection, dispatcher -> dispatcher.unregister((GameEventListener)this.listener));
/*    */   }
/*    */   
/*    */   public void move(ServerLevel level) {
/* 37 */     this.listener.getListenerSource().getPosition((Level)level)
/* 38 */       .map(SectionPos::of)
/* 39 */       .ifPresent(currentSection -> {
/*    */           if (this.lastSection == null || !this.lastSection.equals(level)) {
/*    */             ifChunkExists((LevelReader)level, this.lastSection, ());
/*    */             this.lastSection = level;
/*    */             ifChunkExists((LevelReader)level, this.lastSection, ());
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void ifChunkExists(LevelReader level, SectionPos sectionPos, Consumer<GameEventListenerRegistry> action) {
/* 49 */     if (sectionPos == null) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     ChunkAccess chunk = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FULL, false);
/*    */     
/* 55 */     if (chunk != null)
/* 56 */       action.accept(chunk.getListenerRegistry(sectionPos.y())); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/DynamicGameEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */