/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GateBehavior<E extends LivingEntity>
/*     */   implements BehaviorControl<E>
/*     */ {
/*     */   private final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*     */   private final Set<MemoryModuleType<?>> exitErasedMemories;
/*     */   private final OrderPolicy orderPolicy;
/*     */   private final RunningPolicy runningPolicy;
/*  27 */   private final ShufflingList<BehaviorControl<? super E>> behaviors = new ShufflingList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Behavior.Status status;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GateBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, Set<MemoryModuleType<?>> exitErasedMemories, OrderPolicy orderPolicy, RunningPolicy runningPolicy, List<Pair<? extends BehaviorControl<? super E>, Integer>> behaviors) {
/*  37 */     this.status = Behavior.Status.STOPPED; this.entryCondition = entryCondition;
/*     */     this.exitErasedMemories = exitErasedMemories;
/*     */     this.orderPolicy = orderPolicy;
/*     */     this.runningPolicy = runningPolicy;
/*  41 */     behaviors.forEach(entry -> this.behaviors.add((BehaviorControl<? super E>)entry.getFirst(), (Integer)entry.getSecond())); } public Behavior.Status getStatus() { return this.status; }
/*     */ 
/*     */   
/*     */   private boolean hasRequiredMemories(E body) {
/*  45 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/*  46 */       MemoryModuleType<?> memoryType = entry.getKey();
/*  47 */       MemoryStatus requiredStatus = entry.getValue();
/*  48 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus)) {
/*  49 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  58 */     if (hasRequiredMemories(body)) {
/*  59 */       this.status = Behavior.Status.RUNNING;
/*  60 */       this.orderPolicy.apply(this.behaviors);
/*  61 */       this.runningPolicy.apply(this.behaviors.stream(), level, body, timestamp);
/*  62 */       return true;
/*     */     } 
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  70 */     this.behaviors.stream()
/*  71 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  72 */       .forEach(goal -> goal.tickOrStop(level, body, timestamp));
/*     */ 
/*     */     
/*  75 */     if (this.behaviors.stream().noneMatch(g -> (g.getStatus() == Behavior.Status.RUNNING))) {
/*  76 */       doStop(level, body, timestamp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  82 */     this.status = Behavior.Status.STOPPED;
/*     */     
/*  84 */     this.behaviors.stream()
/*  85 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  86 */       .forEach(goal -> goal.doStop(level, body, timestamp));
/*     */     
/*  88 */     Objects.requireNonNull(body.getBrain()); this.exitErasedMemories.forEach(body.getBrain()::eraseMemory);
/*     */   }
/*     */ 
/*     */   
/*     */   public String debugString() {
/*  93 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public enum OrderPolicy {
/*  97 */     ORDERED(t -> { 
/*  98 */       }), SHUFFLED(ShufflingList::shuffle);
/*     */     
/*     */     private final Consumer<ShufflingList<?>> consumer;
/*     */ 
/*     */     
/*     */     OrderPolicy(Consumer<ShufflingList<?>> consumer) {
/* 104 */       this.consumer = consumer;
/*     */     }
/*     */     
/*     */     public void apply(ShufflingList<?> list) {
/* 108 */       this.consumer.accept(list);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum RunningPolicy {
/* 113 */     RUN_ONE
/*     */     {
/*     */       public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp)
/*     */       {
/* 117 */         behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 118 */           .filter(goal -> goal.tryStart(level, body, timestamp))
/* 119 */           .findFirst();
/*     */       }
/*     */     },
/* 122 */     TRY_ALL
/*     */     {
/*     */       public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp)
/*     */       {
/* 126 */         behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 127 */           .forEach(goal -> goal.tryStart(level, body, timestamp)); } }; public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> param1Stream, ServerLevel param1ServerLevel, E param1E, long param1Long); } enum null { public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) { behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED)).filter(goal -> goal.tryStart(level, body, timestamp)).findFirst(); } } enum null { public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) { behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED)).forEach(goal -> goal.tryStart(level, body, timestamp)); }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     Set<? extends BehaviorControl<? super E>> runningBehaviours = (Set<? extends BehaviorControl<? super E>>)this.behaviors.stream()
/* 138 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/* 139 */       .collect(Collectors.toSet());
/*     */     
/* 141 */     return "(" + getClass().getSimpleName() + "): " + String.valueOf(runningBehaviours);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/GateBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */