/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class Sensing {
/*    */   private final Mob mob;
/* 12 */   private final IntSet seen = (IntSet)new IntOpenHashSet();
/* 13 */   private final IntSet unseen = (IntSet)new IntOpenHashSet();
/*    */   
/*    */   public Sensing(Mob mob) {
/* 16 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.seen.clear();
/* 21 */     this.unseen.clear();
/*    */   }
/*    */   
/*    */   public boolean hasLineOfSight(Entity target) {
/* 25 */     int targetId = target.getId();
/* 26 */     if (this.seen.contains(targetId)) {
/* 27 */       return true;
/*    */     }
/* 29 */     if (this.unseen.contains(targetId)) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     ProfilerFiller profiler = Profiler.get();
/* 34 */     profiler.push("hasLineOfSight");
/* 35 */     boolean hasLineOfSight = this.mob.hasLineOfSight(target);
/* 36 */     profiler.pop();
/* 37 */     if (hasLineOfSight) {
/* 38 */       this.seen.add(targetId);
/*    */     } else {
/* 40 */       this.unseen.add(targetId);
/*    */     } 
/* 42 */     return hasLineOfSight;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/Sensing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */