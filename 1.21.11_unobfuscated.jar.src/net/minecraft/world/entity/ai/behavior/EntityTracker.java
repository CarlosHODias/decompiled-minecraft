/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityTracker implements PositionTracker {
/*    */   private final Entity entity;
/*    */   private final boolean trackEyeHeight;
/*    */   private final boolean targetEyeHeight;
/*    */   
/*    */   public EntityTracker(Entity entity, boolean trackEyeHeight) {
/* 18 */     this(entity, trackEyeHeight, false);
/*    */   }
/*    */   
/*    */   public EntityTracker(Entity entity, boolean trackEyeHeight, boolean targetEyeHeight) {
/* 22 */     this.entity = entity;
/* 23 */     this.trackEyeHeight = trackEyeHeight;
/* 24 */     this.targetEyeHeight = targetEyeHeight;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 currentPosition() {
/* 29 */     return this.trackEyeHeight ? this.entity.position().add(0.0D, this.entity.getEyeHeight(), 0.0D) : this.entity.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 34 */     return this.targetEyeHeight ? BlockPos.containing((Position)this.entity.getEyePosition()) : this.entity.blockPosition();
/*    */   }
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity body) {
/*    */     LivingEntity livingEntity;
/* 39 */     Entity entity = this.entity; if (entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/* 40 */     else { return true; }
/*    */ 
/*    */     
/* 43 */     if (!livingEntity.isAlive()) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     Optional<NearestVisibleLivingEntities> visibleEntities = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/* 48 */     return (visibleEntities.isPresent() && ((NearestVisibleLivingEntities)visibleEntities.get()).contains(livingEntity));
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 52 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "EntityTracker for " + String.valueOf(this.entity);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/EntityTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */