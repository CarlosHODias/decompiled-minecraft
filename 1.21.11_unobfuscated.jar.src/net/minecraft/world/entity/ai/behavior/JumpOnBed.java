/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JumpOnBed
/*     */   extends Behavior<Mob>
/*     */ {
/*     */   private static final int MAX_TIME_TO_REACH_BED = 100;
/*     */   private static final int MIN_JUMPS = 3;
/*     */   private static final int MAX_JUMPS = 6;
/*     */   private static final int COOLDOWN_BETWEEN_JUMPS = 5;
/*     */   private final float speedModifier;
/*     */   private BlockPos targetBed;
/*     */   private int remainingTimeToReachBed;
/*     */   private int remainingJumps;
/*     */   private int remainingCooldownUntilNextJump;
/*     */   
/*     */   public JumpOnBed(float speedModifier) {
/*  34 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.speedModifier = speedModifier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  43 */     return (body.isBaby() && nearBed(level, body));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/*  48 */     super.start(level, body, timestamp);
/*     */     
/*  50 */     getNearestBed(body).ifPresent(targetBed -> {
/*     */           this.targetBed = level;
/*     */           this.remainingTimeToReachBed = 100;
/*     */           this.remainingJumps = 3 + level.random.nextInt(4);
/*     */           this.remainingCooldownUntilNextJump = 0;
/*     */           startWalkingTowardsBed(level, level);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/*  61 */     super.stop(level, body, timestamp);
/*     */     
/*  63 */     this.targetBed = null;
/*  64 */     this.remainingTimeToReachBed = 0;
/*  65 */     this.remainingJumps = 0;
/*  66 */     this.remainingCooldownUntilNextJump = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  71 */     return (body.isBaby() && this.targetBed != null && 
/*     */       
/*  73 */       isBed(level, this.targetBed) && 
/*  74 */       !tiredOfWalking(level, body) && 
/*  75 */       !tiredOfJumping(level, body));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/*  85 */     if (!onOrOverBed(level, body)) {
/*  86 */       this.remainingTimeToReachBed--;
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     if (this.remainingCooldownUntilNextJump > 0) {
/*  91 */       this.remainingCooldownUntilNextJump--;
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     if (onBedSurface(level, body)) {
/*  96 */       body.getJumpControl().jump();
/*  97 */       this.remainingJumps--;
/*  98 */       this.remainingCooldownUntilNextJump = 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startWalkingTowardsBed(Mob body, BlockPos bedPos) {
/* 103 */     body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(bedPos, this.speedModifier, 0));
/*     */   }
/*     */   
/*     */   private boolean nearBed(ServerLevel level, Mob body) {
/* 107 */     return (onOrOverBed(level, body) || getNearestBed(body).isPresent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean onOrOverBed(ServerLevel level, Mob body) {
/* 114 */     BlockPos bodyPos = body.blockPosition();
/* 115 */     BlockPos oneBelow = bodyPos.below();
/* 116 */     return (isBed(level, bodyPos) || isBed(level, oneBelow));
/*     */   }
/*     */   
/*     */   private boolean onBedSurface(ServerLevel level, Mob body) {
/* 120 */     return isBed(level, body.blockPosition());
/*     */   }
/*     */   
/*     */   private boolean isBed(ServerLevel level, BlockPos bodyPos) {
/* 124 */     return level.getBlockState(bodyPos).is(BlockTags.BEDS);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getNearestBed(Mob body) {
/* 128 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_BED);
/*     */   }
/*     */   
/*     */   private boolean tiredOfWalking(ServerLevel level, Mob body) {
/* 132 */     return (!onOrOverBed(level, body) && this.remainingTimeToReachBed <= 0);
/*     */   }
/*     */   
/*     */   private boolean tiredOfJumping(ServerLevel level, Mob body) {
/* 136 */     return (onOrOverBed(level, body) && this.remainingJumps <= 0);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/JumpOnBed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */