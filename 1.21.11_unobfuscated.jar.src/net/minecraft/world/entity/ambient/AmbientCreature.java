/*    */ package net.minecraft.world.entity.ambient;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AmbientCreature extends Mob {
/*    */   protected AmbientCreature(EntityType<? extends AmbientCreature> type, Level level) {
/*  9 */     super(type, level);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed() {
/* 14 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ambient/AmbientCreature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */