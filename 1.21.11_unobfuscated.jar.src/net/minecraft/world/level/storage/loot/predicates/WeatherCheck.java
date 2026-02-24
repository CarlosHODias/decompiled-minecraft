/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class WeatherCheck extends Record implements LootItemCondition {
/*    */   private final Optional<Boolean> isRaining;
/*    */   private final Optional<Boolean> isThundering;
/*    */   public static final com.mojang.serialization.MapCodec<WeatherCheck> CODEC;
/*    */   
/* 11 */   public WeatherCheck(Optional<Boolean> isRaining, Optional<Boolean> isThundering) { this.isRaining = isRaining; this.isThundering = isThundering; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck; } public Optional<Boolean> isRaining() { return this.isRaining; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Boolean> isThundering() { return this.isThundering; }
/*    */ 
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("raining").forGetter(WeatherCheck::isRaining), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("thundering").forGetter(WeatherCheck::isThundering)).apply((com.mojang.datafixers.kinds.Applicative)i, WeatherCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 22 */     return LootItemConditions.WEATHER_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(net.minecraft.world.level.storage.loot.LootContext context) {
/* 27 */     net.minecraft.server.level.ServerLevel level = context.getLevel();
/*    */     
/* 29 */     if (this.isRaining.isPresent() && (Boolean)this.isRaining.get() != level.isRaining()) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     if (this.isThundering.isPresent() && (Boolean)this.isThundering.get() != level.isThundering()) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 41 */     private Optional<Boolean> isRaining = Optional.empty();
/* 42 */     private Optional<Boolean> isThundering = Optional.empty();
/*    */     
/*    */     public Builder setRaining(boolean raining) {
/* 45 */       this.isRaining = Optional.of(raining);
/* 46 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setThundering(boolean thundering) {
/* 50 */       this.isThundering = Optional.of(thundering);
/* 51 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public WeatherCheck build() {
/* 56 */       return new WeatherCheck(this.isRaining, this.isThundering);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder weather() {
/* 61 */     return new Builder();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/WeatherCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */