/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public abstract class Goal
/*    */ {
/* 12 */   private final EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
/*    */   
/*    */   public abstract boolean canUse();
/*    */   
/*    */   public boolean canContinueToUse() {
/* 17 */     return canUse();
/*    */   }
/*    */   
/*    */   public boolean isInterruptable() {
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */   
/*    */   public void stop() {}
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */   
/*    */   public void setFlags(EnumSet<Flag> requiredControlFlags) {
/* 38 */     this.flags.clear();
/* 39 */     this.flags.addAll(requiredControlFlags);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   public EnumSet<Flag> getFlags() {
/* 48 */     return this.flags;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int adjustedTickDelay(int ticks) {
/* 53 */     return requiresUpdateEveryTick() ? ticks : reducedTickDelay(ticks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static int reducedTickDelay(int ticks) {
/* 60 */     return Mth.positiveCeilDiv(ticks, 2);
/*    */   }
/*    */   
/*    */   public enum Flag {
/* 64 */     MOVE,
/* 65 */     LOOK,
/* 66 */     JUMP,
/* 67 */     TARGET;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static ServerLevel getServerLevel(Entity entity) {
/* 74 */     return (ServerLevel)entity.level();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static ServerLevel getServerLevel(Level level) {
/* 81 */     return (ServerLevel)level;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/Goal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */