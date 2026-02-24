/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomLookAround;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class ArmadilloAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*  50 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16); private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F; private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F; private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.25F; private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F; private static final double DEFAULT_CLOSE_ENOUGH_DIST = 2.0D;
/*     */   private static final double BABY_CLOSE_ENOUGH_DIST = 1.0D;
/*  52 */   private static final ImmutableList<SensorType<? extends Sensor<? super Armadillo>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT, SensorType.ARMADILLO_SCARE_DETECTED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, (Object[])new MemoryModuleType[] { MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.DANGER_DETECTED_RECENTLY });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final OneShot<Armadillo> ARMADILLO_ROLLING_OUT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  80 */     ARMADILLO_ROLLING_OUT = BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.DANGER_DETECTED_RECENTLY)).apply((Applicative)i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Brain.Provider<Armadillo> brainProvider() {
/*  91 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Armadillo> brain) {
/*  95 */     initCoreActivity(brain);
/*  96 */     initIdleActivity(brain);
/*  97 */     initScaredActivity(brain);
/*     */     
/*  99 */     brain.setCoreActivities(Set.of(Activity.CORE));
/* 100 */     brain.setDefaultActivity(Activity.IDLE);
/* 101 */     brain.useDefaultActivity();
/* 102 */     return brain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Armadillo> brain) {
/* 106 */     brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new ArmadilloPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink()
/*     */           {
/*     */ 
/*     */ 
/*     */             
/*     */             protected boolean checkExtraStartConditions(ServerLevel level, Mob body)
/*     */             {
/* 113 */               if (body instanceof Armadillo) { Armadillo armadillo = (Armadillo)body; if (armadillo.isScared())
/* 114 */                   return false;  }
/*     */               
/* 116 */               return super.checkExtraStartConditions(level, body);
/*     */             }
/*     */           },  new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS), ARMADILLO_ROLLING_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Armadillo> brain) {
/* 126 */     brain.addActivity(Activity.IDLE, ImmutableList.of(
/* 127 */           Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 128 */           Pair.of(1, new AnimalMakeLove(EntityType.ARMADILLO, 1.0F, 1)), 
/* 129 */           Pair.of(2, new RunOne((List)ImmutableList.of(
/* 130 */                 Pair.of(new FollowTemptation(armadillo -> 1.25F, armadillo -> armadillo.isBaby() ? 1.0D : 2.0D), 1), 
/* 131 */                 Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F), 1)))), 
/*     */           
/* 133 */           Pair.of(3, new RandomLookAround((IntProvider)UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)), 
/* 134 */           Pair.of(4, new RunOne(
/* 135 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 138 */               (List)ImmutableList.of(
/* 139 */                 Pair.of(RandomStroll.stroll(1.0F), 1), 
/* 140 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1), 
/* 141 */                 Pair.of(new net.minecraft.world.entity.ai.behavior.DoNothing(30, 60), 1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initScaredActivity(Brain<Armadillo> brain) {
/* 148 */     brain.addActivityWithConditions(Activity.PANIC, ImmutableList.of(
/* 149 */           Pair.of(0, new ArmadilloBallUp())), 
/* 150 */         Set.of(Pair.of(MemoryModuleType.DANGER_DETECTED_RECENTLY, MemoryStatus.VALUE_PRESENT), 
/* 151 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   public static class ArmadilloBallUp
/*     */     extends Behavior<Armadillo> {
/* 156 */     static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
/*     */     
/*     */     static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
/*     */     static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
/* 160 */     int nextPeekTimer = 0;
/*     */     boolean dangerWasAround;
/*     */     
/*     */     public ArmadilloBallUp() {
/* 164 */       super(Map.of(), BALL_UP_STAY_IN_STATE);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void tick(ServerLevel level, Armadillo body, long timestamp) {
/* 169 */       super.tick(level, (LivingEntity)body, timestamp);
/* 170 */       if (this.nextPeekTimer > 0) {
/* 171 */         this.nextPeekTimer--;
/*     */       }
/* 173 */       if (body.shouldSwitchToScaredState()) {
/* 174 */         body.switchToState(Armadillo.ArmadilloState.SCARED);
/* 175 */         if (body.onGround()) {
/* 176 */           body.playSound(SoundEvents.ARMADILLO_LAND);
/*     */         }
/*     */         return;
/*     */       } 
/* 180 */       Armadillo.ArmadilloState state = body.getState();
/* 181 */       long dangerTickCounter = body.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
/* 182 */       boolean dangerIsAround = (dangerTickCounter > 75L);
/* 183 */       if (dangerIsAround != this.dangerWasAround) {
/* 184 */         this.nextPeekTimer = pickNextPeekTimer(body);
/*     */       }
/* 186 */       this.dangerWasAround = dangerIsAround;
/* 187 */       if (state == Armadillo.ArmadilloState.SCARED) {
/* 188 */         if (this.nextPeekTimer == 0 && body.onGround() && dangerIsAround) {
/* 189 */           level.broadcastEntityEvent((Entity)body, (byte)64);
/* 190 */           this.nextPeekTimer = pickNextPeekTimer(body);
/*     */         } 
/*     */ 
/*     */         
/* 194 */         if (dangerTickCounter < Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 195 */           body.playSound(SoundEvents.ARMADILLO_UNROLL_START);
/* 196 */           body.switchToState(Armadillo.ArmadilloState.UNROLLING);
/*     */         } 
/* 198 */       } else if (state == Armadillo.ArmadilloState.UNROLLING && dangerTickCounter > Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 199 */         body.switchToState(Armadillo.ArmadilloState.SCARED);
/*     */       } 
/*     */     }
/*     */     
/*     */     private int pickNextPeekTimer(Armadillo body) {
/* 204 */       return Armadillo.ArmadilloState.SCARED.animationDuration() + body.getRandom().nextIntBetweenInclusive(100, 400);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Armadillo body) {
/* 208 */       return body.onGround();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Armadillo body, long timestamp) {
/* 213 */       return body.getState().isThreatened();
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Armadillo body, long timestamp) {
/* 217 */       body.rollUp();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Armadillo body, long timestamp) {
/* 222 */       if (!body.canStayRolledUp())
/* 223 */         body.rollOut(); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ArmadilloPanic
/*     */     extends AnimalPanic<Armadillo> {
/*     */     public ArmadilloPanic(float speedMultiplier) {
/* 230 */       super(speedMultiplier, mob -> DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void start(ServerLevel level, Armadillo armadillo, long timestamp) {
/* 235 */       armadillo.rollOut();
/* 236 */       super.start(level, (PathfinderMob)armadillo, timestamp);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateActivity(Armadillo body) {
/* 241 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PANIC, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/armadillo/ArmadilloAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */