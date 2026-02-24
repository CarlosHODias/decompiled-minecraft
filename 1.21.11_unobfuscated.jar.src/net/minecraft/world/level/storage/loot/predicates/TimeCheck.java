/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class TimeCheck extends Record implements LootItemCondition {
/*    */   private final Optional<Long> period;
/*    */   private final IntRange value;
/*    */   public static final com.mojang.serialization.MapCodec<TimeCheck> CODEC;
/*    */   
/* 14 */   public TimeCheck(Optional<Long> period, IntRange value) { this.period = period; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck; } public Optional<Long> period() { return this.period; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntRange value() { return this.value; }
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.LONG.optionalFieldOf("period").forGetter(TimeCheck::period), (App)IntRange.CODEC.fieldOf("value").forGetter(TimeCheck::value)).apply((com.mojang.datafixers.kinds.Applicative)i, TimeCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 25 */     return LootItemConditions.TIME_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 30 */     return this.value.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 35 */     net.minecraft.server.level.ServerLevel level = context.getLevel();
/*    */     
/* 37 */     long time = level.getDayTime();
/*    */     
/* 39 */     if (this.period.isPresent()) {
/* 40 */       time %= (Long)this.period.get();
/*    */     }
/*    */     
/* 43 */     return this.value.test(context, (int)time);
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 47 */     private Optional<Long> period = Optional.empty();
/*    */     private final IntRange value;
/*    */     
/*    */     public Builder(IntRange value) {
/* 51 */       this.value = value;
/*    */     }
/*    */     
/*    */     public Builder setPeriod(long period) {
/* 55 */       this.period = Optional.of(period);
/* 56 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public TimeCheck build() {
/* 61 */       return new TimeCheck(this.period, this.value);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder time(IntRange period) {
/* 66 */     return new Builder(period);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/TimeCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */