/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Digging;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Emerging;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.ForceUnmount;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Roar;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SetRoarTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Sniffing;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.TryToSniff;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WardenAi
/*     */ {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.5F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_INVESTIGATING = 0.7F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.2F;
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 18;
/*  61 */   private static final int DIGGING_DURATION = Mth.ceil(100.0F);
/*  62 */   public static final int EMERGE_DURATION = Mth.ceil(133.59999F);
/*  63 */   public static final int ROAR_DURATION = Mth.ceil(84.0F);
/*  64 */   private static final int SNIFFING_DURATION = Mth.ceil(83.2F);
/*     */   
/*     */   public static final int DIGGING_COOLDOWN = 1200;
/*     */   
/*     */   private static final int DISTURBANCE_LOCATION_EXPIRY_TIME = 100;
/*     */   
/*  70 */   private static final List<SensorType<? extends Sensor<? super Warden>>> SENSOR_TYPES = List.of(SensorType.NEAREST_PLAYERS, SensorType.WARDEN_ENTITY_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of((MemoryModuleType<?>[])new MemoryModuleType[] { MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final BehaviorControl<Warden> DIG_COOLDOWN_SETTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 105 */     DIG_COOLDOWN_SETTER = (BehaviorControl<Warden>)BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.DIG_COOLDOWN)).apply((Applicative)i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateActivity(Warden body) {
/* 115 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Warden warden, Dynamic<?> input) {
/* 127 */     Brain.Provider<Warden> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
/* 128 */     Brain<Warden> brain = provider.makeBrain(input);
/*     */     
/* 130 */     initCoreActivity(brain);
/* 131 */     initEmergeActivity(brain);
/* 132 */     initDiggingActivity(brain);
/* 133 */     initIdleActivity(brain);
/* 134 */     initRoarActivity(brain);
/* 135 */     initFightActivity(warden, brain);
/* 136 */     initInvestigateActivity(brain);
/* 137 */     initSniffingActivity(brain);
/*     */     
/* 139 */     brain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 140 */     brain.setDefaultActivity(Activity.IDLE);
/* 141 */     brain.useDefaultActivity();
/*     */     
/* 143 */     return brain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Warden> brain) {
/* 147 */     brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), 
/*     */           
/* 149 */           SetWardenLookTarget.create(), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initEmergeActivity(Brain<Warden> brain) {
/* 156 */     brain.addActivityAndRemoveMemoryWhenStopped(Activity.EMERGE, 5, ImmutableList.of(new Emerging(EMERGE_DURATION)), MemoryModuleType.IS_EMERGING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initDiggingActivity(Brain<Warden> brain) {
/* 162 */     brain.addActivityWithConditions(Activity.DIG, ImmutableList.of(
/* 163 */           Pair.of(0, new ForceUnmount()), 
/* 164 */           Pair.of(1, new Digging(DIGGING_DURATION))), 
/* 165 */         (Set)ImmutableSet.of(
/* 166 */           Pair.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 167 */           Pair.of(MemoryModuleType.DIG_COOLDOWN, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Warden> brain) {
/* 172 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/* 173 */           SetRoarTarget.create(Warden::getEntityAngryAt), 
/* 174 */           TryToSniff.create(), new RunOne(
/* 175 */             (Map)ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_ABSENT), 
/*     */             
/* 177 */             (List)ImmutableList.of(
/* 178 */               Pair.of(RandomStroll.stroll(0.5F), 2), 
/* 179 */               Pair.of(new DoNothing(30, 60), 1)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initInvestigateActivity(Brain<Warden> brain) {
/* 185 */     brain.addActivityAndRemoveMemoryWhenStopped(Activity.INVESTIGATE, 5, ImmutableList.of(
/* 186 */           SetRoarTarget.create(Warden::getEntityAngryAt), 
/* 187 */           GoToTargetLocation.create(MemoryModuleType.DISTURBANCE_LOCATION, 2, 0.7F)), MemoryModuleType.DISTURBANCE_LOCATION);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initSniffingActivity(Brain<Warden> brain) {
/* 192 */     brain.addActivityAndRemoveMemoryWhenStopped(Activity.SNIFF, 5, ImmutableList.of(
/* 193 */           SetRoarTarget.create(Warden::getEntityAngryAt), new Sniffing(SNIFFING_DURATION)), MemoryModuleType.IS_SNIFFING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initRoarActivity(Brain<Warden> brain) {
/* 199 */     brain.addActivityAndRemoveMemoryWhenStopped(Activity.ROAR, 10, ImmutableList.of(new Roar()), MemoryModuleType.ROAR_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Warden body, Brain<Warden> brain) {
/* 205 */     brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(DIG_COOLDOWN_SETTER, 
/*     */           
/* 207 */           StopAttackingIfTargetInvalid.create((level, target) -> (!body.getAngerLevel().isAngry() || !body.canTargetEntity((Entity)target)), WardenAi::onTargetInvalid, false), 
/* 208 */           SetEntityLookTarget.create(entity -> isTarget(body, entity), (float)body.getAttributeValue(Attributes.FOLLOW_RANGE)), 
/* 209 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F), new SonicBoom(), 
/*     */           
/* 211 */           MeleeAttack.create(18)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isTarget(Warden body, LivingEntity living) {
/* 216 */     return body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(e -> (e == living)).isPresent();
/*     */   }
/*     */   
/*     */   private static void onTargetInvalid(ServerLevel level, Warden body, LivingEntity attackTarget) {
/* 220 */     if (!body.canTargetEntity((Entity)attackTarget)) {
/* 221 */       body.clearAnger((Entity)attackTarget);
/*     */     }
/*     */ 
/*     */     
/* 225 */     setDigCooldown((LivingEntity)body);
/*     */   }
/*     */   
/*     */   public static void setDigCooldown(LivingEntity body) {
/* 229 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.DIG_COOLDOWN)) {
/* 230 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setDisturbanceLocation(Warden body, BlockPos position) {
/* 235 */     if (!body.level().getWorldBorder().isWithinBounds(position) || 
/* 236 */       body.getEntityAngryAt().isPresent() || 
/* 237 */       body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
/*     */       return;
/*     */     }
/*     */     
/* 241 */     setDigCooldown((LivingEntity)body);
/* 242 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 100L);
/* 243 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(position), 100L);
/* 244 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, position, 100L);
/* 245 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/warden/WardenAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */