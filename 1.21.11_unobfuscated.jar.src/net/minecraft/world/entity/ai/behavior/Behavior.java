/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Behavior<E extends LivingEntity>
/*     */   implements BehaviorControl<E>
/*     */ {
/*     */   public static final int DEFAULT_DURATION = 60;
/*     */   protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*  18 */   private Status status = Status.STOPPED;
/*     */   private long endTimestamp;
/*     */   private final int minDuration;
/*     */   private final int maxDuration;
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
/*  24 */     this(entryCondition, 60);
/*     */   }
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int timeOutDuration) {
/*  28 */     this(entryCondition, timeOutDuration, timeOutDuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
/*  35 */     this.minDuration = minDuration;
/*  36 */     this.maxDuration = maxDuration;
/*  37 */     this.entryCondition = entryCondition;
/*     */   }
/*     */ 
/*     */   
/*     */   public Status getStatus() {
/*  42 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  47 */     if (hasRequiredMemories(body) && checkExtraStartConditions(level, body)) {
/*  48 */       this.status = Status.RUNNING;
/*  49 */       int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/*  50 */       this.endTimestamp = timestamp + duration;
/*  51 */       start(level, body, timestamp);
/*  52 */       return true;
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  65 */     if (!timedOut(timestamp) && canStillUse(level, body, timestamp)) {
/*  66 */       tick(level, body, timestamp);
/*     */     } else {
/*  68 */       doStop(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  80 */     this.status = Status.STOPPED;
/*  81 */     stop(level, body, timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/* 106 */     return (timestamp > this.endTimestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String debugString() {
/* 119 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected boolean hasRequiredMemories(E body) {
/* 123 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/* 124 */       MemoryModuleType<?> memoryType = entry.getKey();
/* 125 */       MemoryStatus requiredStatus = entry.getValue();
/* 126 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus)) {
/* 127 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public enum Status {
/* 135 */     STOPPED,
/* 136 */     RUNNING;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/Behavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */