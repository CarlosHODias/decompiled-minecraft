/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerMakeLove
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private long birthTimestamp;
/*     */   
/*     */   public VillagerMakeLove() {
/*  30 */     super(
/*  31 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  42 */     return isBreedingPossible(body);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  47 */     return (timestamp <= this.birthTimestamp && isBreedingPossible(body));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  52 */     AgeableMob breedTarget = body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  54 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)breedTarget, 0.5F, 2);
/*     */     
/*  56 */     level.broadcastEntityEvent((Entity)breedTarget, (byte)18);
/*  57 */     level.broadcastEntityEvent((Entity)body, (byte)18);
/*     */     
/*  59 */     int duration = 275 + body.getRandom().nextInt(50);
/*  60 */     this.birthTimestamp = timestamp + duration;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  65 */     Villager target = body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  67 */     if (body.distanceToSqr((Entity)target) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)target, 0.5F, 2);
/*     */     
/*  73 */     if (timestamp >= this.birthTimestamp) {
/*     */       
/*  75 */       body.eatAndDigestFood();
/*  76 */       target.eatAndDigestFood();
/*     */       
/*  78 */       tryToGiveBirth(level, body, target);
/*  79 */     } else if (body.getRandom().nextInt(35) == 0) {
/*  80 */       level.broadcastEntityEvent((Entity)target, (byte)12);
/*  81 */       level.broadcastEntityEvent((Entity)body, (byte)12);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void tryToGiveBirth(ServerLevel level, Villager body, Villager target) {
/*  87 */     Optional<BlockPos> childsBed = takeVacantBed(level, body);
/*  88 */     if (childsBed.isEmpty()) {
/*     */       
/*  90 */       level.broadcastEntityEvent((Entity)target, (byte)13);
/*  91 */       level.broadcastEntityEvent((Entity)body, (byte)13);
/*     */     } else {
/*  93 */       Optional<Villager> child = breed(level, body, target);
/*     */       
/*  95 */       if (child.isPresent()) {
/*  96 */         giveBedToChild(level, child.get(), childsBed.get());
/*     */       } else {
/*  98 */         level.getPoiManager().release(childsBed.get());
/*  99 */         level.debugSynchronizers().updatePoi(childsBed.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 106 */     body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   private boolean isBreedingPossible(Villager myBody) {
/* 110 */     Brain<Villager> brain = myBody.getBrain();
/* 111 */     Optional<AgeableMob> breedTarget = brain.getMemory(MemoryModuleType.BREED_TARGET)
/* 112 */       .filter(entity -> (entity.getType() == EntityType.VILLAGER));
/* 113 */     if (breedTarget.isEmpty()) {
/* 114 */       return false;
/*     */     }
/* 116 */     return (BehaviorUtils.targetIsValid(brain, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && 
/* 117 */       myBody.canBreed() && ((AgeableMob)
/* 118 */       breedTarget.get()).canBreed());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> takeVacantBed(ServerLevel level, Villager body) {
/* 122 */     return level.getPoiManager().take(p -> p.is(PoiTypes.HOME), (poiType, poiPos) -> canReach(body, poiPos, body), 
/*     */ 
/*     */         
/* 125 */         body.blockPosition(), 48);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReach(Villager body, BlockPos poiPos, Holder<PoiType> poiType) {
/* 131 */     Path path = body.getNavigation().createPath(poiPos, ((PoiType)poiType.value()).validRange());
/* 132 */     return (path != null && path.canReach());
/*     */   }
/*     */   
/*     */   private Optional<Villager> breed(ServerLevel level, Villager source, Villager target) {
/* 136 */     Villager child = source.getBreedOffspring(level, (AgeableMob)target);
/* 137 */     if (child == null) {
/* 138 */       return Optional.empty();
/*     */     }
/* 140 */     source.setAge(6000);
/* 141 */     target.setAge(6000);
/* 142 */     child.setAge(-24000);
/* 143 */     child.snapTo(source.getX(), source.getY(), source.getZ(), 0.0F, 0.0F);
/*     */     
/* 145 */     level.addFreshEntityWithPassengers((Entity)child);
/* 146 */     level.broadcastEntityEvent((Entity)child, (byte)12);
/*     */     
/* 148 */     return Optional.of(child);
/*     */   }
/*     */   
/*     */   private void giveBedToChild(ServerLevel level, Villager child, BlockPos bedPos) {
/* 152 */     GlobalPos globalBedPos = GlobalPos.of(level.dimension(), bedPos);
/* 153 */     child.getBrain().setMemory(MemoryModuleType.HOME, globalBedPos);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/VillagerMakeLove.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */