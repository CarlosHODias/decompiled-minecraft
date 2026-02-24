/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.Scoreboard;
/*    */ 
/*    */ public final class EntityHasScoreCondition extends Record implements LootItemCondition {
/*    */   private final Map<String, IntRange> scores;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   public static final com.mojang.serialization.MapCodec<EntityHasScoreCondition> CODEC;
/*    */   
/* 20 */   public EntityHasScoreCondition(Map<String, IntRange> scores, LootContext.EntityTarget entityTarget) { this.scores = scores; this.entityTarget = entityTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition; } public Map<String, IntRange> scores() { return this.scores; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public LootContext.EntityTarget entityTarget() { return this.entityTarget; }
/*    */ 
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap((Codec)Codec.STRING, IntRange.CODEC).fieldOf("scores").forGetter(EntityHasScoreCondition::scores), (App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(EntityHasScoreCondition::entityTarget)).apply((com.mojang.datafixers.kinds.Applicative)i, EntityHasScoreCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 31 */     return LootItemConditions.ENTITY_SCORES;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 36 */     return (java.util.Set<net.minecraft.util.context.ContextKey<?>>)Stream.concat(Stream.of(this.entityTarget.contextParam()), this.scores.values().stream().flatMap(r -> r.getReferencedContextParams().stream())).collect(com.google.common.collect.ImmutableSet.toImmutableSet());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 41 */     Entity entity = (Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/*    */     
/* 43 */     if (entity == null) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     net.minecraft.server.ServerScoreboard serverScoreboard = context.getLevel().getScoreboard();
/* 48 */     for (Map.Entry<String, IntRange> entry : this.scores.entrySet()) {
/* 49 */       if (!hasScore(context, entity, (Scoreboard)serverScoreboard, entry.getKey(), entry.getValue())) {
/* 50 */         return false;
/*    */       }
/*    */     } 
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   protected boolean hasScore(LootContext context, Entity entity, Scoreboard scoreboard, String objectiveName, IntRange range) {
/* 57 */     Objective objective = scoreboard.getObjective(objectiveName);
/* 58 */     if (objective == null) {
/* 59 */       return false;
/*    */     }
/* 61 */     ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo((net.minecraft.world.scores.ScoreHolder)entity, objective);
/* 62 */     if (scoreInfo == null) {
/* 63 */       return false;
/*    */     }
/* 65 */     return range.test(context, scoreInfo.value());
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 69 */     private final com.google.common.collect.ImmutableMap.Builder<String, IntRange> scores = com.google.common.collect.ImmutableMap.builder();
/*    */     private final LootContext.EntityTarget entityTarget;
/*    */     
/*    */     public Builder(LootContext.EntityTarget entityTarget) {
/* 73 */       this.entityTarget = entityTarget;
/*    */     }
/*    */     
/*    */     public Builder withScore(String score, IntRange bounds) {
/* 77 */       this.scores.put(score, bounds);
/* 78 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemCondition build() {
/* 83 */       return new EntityHasScoreCondition((Map<String, IntRange>)this.scores.build(), this.entityTarget);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder hasScores(LootContext.EntityTarget target) {
/* 88 */     return new Builder(target);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */