/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum InsideBlockEffectType
/*    */ {
/* 14 */   CLEAR_FREEZE(Entity::clearFreeze),
/* 15 */   FIRE_IGNITE(BaseFireBlock::fireIgnite),
/* 16 */   LAVA_IGNITE(Entity::lavaIgnite),
/* 17 */   EXTINGUISH(Entity::clearFire), FREEZE(Entity::clearFire);
/*    */   static { FREEZE = new InsideBlockEffectType("FREEZE", 0, entity -> {
/*    */           entity.setIsInPowderSnow(true);
/*    */           if (entity.canFreeze())
/*    */             entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), entity.getTicksFrozen() + 1)); 
/*    */         }); } InsideBlockEffectType(Consumer<Entity> effect) {
/* 23 */     this.effect = effect;
/*    */   }
/*    */   private final Consumer<Entity> effect;
/*    */   public Consumer<Entity> effect() {
/* 27 */     return this.effect;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/InsideBlockEffectType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */