/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class FollowTemptation
/*    */   extends Behavior<PathfinderMob> {
/*    */   public static final int TEMPTATION_COOLDOWN = 100;
/*    */   public static final double DEFAULT_CLOSE_ENOUGH_DIST = 2.5D;
/*    */   public static final double BACKED_UP_CLOSE_ENOUGH_DIST = 3.5D;
/*    */   private final Function<LivingEntity, Float> speedModifier;
/*    */   private final Function<LivingEntity, Double> closeEnoughDistance;
/*    */   private final boolean lookInTheEyes;
/*    */   
/*    */   public FollowTemptation(Function<LivingEntity, Float> speedModifier) {
/* 29 */     this(speedModifier, entity -> 2.5D);
/*    */   }
/*    */   
/*    */   public FollowTemptation(Function<LivingEntity, Float> speedModifier, Function<LivingEntity, Double> closeEnoughDistance) {
/* 33 */     this(speedModifier, closeEnoughDistance, false);
/*    */   }
/*    */   
/*    */   public FollowTemptation(Function<LivingEntity, Float> speedModifier, Function<LivingEntity, Double> closeEnoughDistance, boolean lookInTheEyes) {
/* 37 */     super((Map<MemoryModuleType<?>, MemoryStatus>)Util.make(() -> {
/*    */             ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> builder = ImmutableMap.builder();
/*    */             builder.put(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED);
/*    */             builder.put(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED);
/*    */             builder.put(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT);
/*    */             builder.put(MemoryModuleType.IS_TEMPTED, MemoryStatus.VALUE_ABSENT);
/*    */             builder.put(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_PRESENT);
/*    */             builder.put(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
/*    */             builder.put(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT);
/*    */             return builder.build();
/*    */           }));
/* 48 */     this.speedModifier = speedModifier;
/* 49 */     this.closeEnoughDistance = closeEnoughDistance;
/* 50 */     this.lookInTheEyes = lookInTheEyes;
/*    */   }
/*    */   
/*    */   protected float getSpeedModifier(PathfinderMob body) {
/* 54 */     return (Float)this.speedModifier.apply(body);
/*    */   }
/*    */   
/*    */   private Optional<Player> getTemptingPlayer(PathfinderMob body) {
/* 58 */     return body.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long timestamp) {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/* 68 */     return (getTemptingPlayer(body).isPresent() && !body.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET) && !body.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 73 */     body.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 78 */     Brain<?> brain = body.getBrain();
/* 79 */     brain.setMemory(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, 100);
/* 80 */     brain.eraseMemory(MemoryModuleType.IS_TEMPTED);
/* 81 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 82 */     brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, PathfinderMob body, long timestamp) {
/* 87 */     Player player = getTemptingPlayer(body).get();
/* 88 */     Brain<?> brain = body.getBrain();
/* 89 */     brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)player, true));
/* 90 */     double closeEnough = (Double)this.closeEnoughDistance.apply(body);
/* 91 */     if (body.distanceToSqr((Entity)player) < Mth.square(closeEnough)) {
/* 92 */       brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/*    */     } else {
/* 94 */       brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker((Entity)player, this.lookInTheEyes, this.lookInTheEyes), getSpeedModifier(body), 2));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/FollowTemptation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */