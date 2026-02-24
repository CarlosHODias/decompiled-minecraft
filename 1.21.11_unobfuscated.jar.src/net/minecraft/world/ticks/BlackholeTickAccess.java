/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlackholeTickAccess
/*    */ {
/*  9 */   private static final TickContainerAccess<Object> CONTAINER_BLACKHOLE = new TickContainerAccess<Object>()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos pos, Object type) {
/* 16 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public int count() {
/* 21 */         return 0;
/*    */       }
/*    */     };
/*    */   
/* 25 */   private static final LevelTickAccess<Object> LEVEL_BLACKHOLE = new LevelTickAccess<Object>()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos pos, Object type) {
/* 32 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean willTickThisTick(BlockPos pos, Object type) {
/* 37 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public int count() {
/* 42 */         return 0;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public static <T> TickContainerAccess<T> emptyContainer() {
/* 48 */     return (TickContainerAccess)CONTAINER_BLACKHOLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> LevelTickAccess<T> emptyLevelList() {
/* 53 */     return (LevelTickAccess)LEVEL_BLACKHOLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ticks/BlackholeTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */