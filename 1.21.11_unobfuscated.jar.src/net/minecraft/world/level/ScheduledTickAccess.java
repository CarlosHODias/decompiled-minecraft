/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.ticks.LevelTickAccess;
/*    */ import net.minecraft.world.ticks.ScheduledTick;
/*    */ import net.minecraft.world.ticks.TickPriority;
/*    */ 
/*    */ public interface ScheduledTickAccess {
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt, TickPriority paramTickPriority);
/*    */   
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt);
/*    */   
/*    */   LevelTickAccess<Block> getBlockTicks();
/*    */   
/*    */   default void scheduleTick(BlockPos pos, Block type, int tickDelay, TickPriority priority) {
/* 18 */     getBlockTicks().schedule(createTick(pos, type, tickDelay, priority));
/*    */   }
/*    */   
/*    */   default void scheduleTick(BlockPos pos, Block type, int tickDelay) {
/* 22 */     getBlockTicks().schedule(createTick(pos, type, tickDelay));
/*    */   }
/*    */   
/*    */   LevelTickAccess<Fluid> getFluidTicks();
/*    */   
/*    */   default void scheduleTick(BlockPos pos, Fluid type, int tickDelay, TickPriority priority) {
/* 28 */     getFluidTicks().schedule(createTick(pos, type, tickDelay, priority));
/*    */   }
/*    */   
/*    */   default void scheduleTick(BlockPos pos, Fluid type, int tickDelay) {
/* 32 */     getFluidTicks().schedule(createTick(pos, type, tickDelay));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/ScheduledTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */