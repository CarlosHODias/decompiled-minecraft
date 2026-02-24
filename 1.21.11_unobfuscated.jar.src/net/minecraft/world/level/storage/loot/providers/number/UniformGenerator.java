/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class UniformGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider min;
/*    */   private final NumberProvider max;
/*    */   public static final com.mojang.serialization.MapCodec<UniformGenerator> CODEC;
/*    */   
/* 12 */   public UniformGenerator(NumberProvider min, NumberProvider max) { this.min = min; this.max = max; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator; } public NumberProvider min() { return this.min; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public NumberProvider max() { return this.max; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("min").forGetter(UniformGenerator::min), (App)NumberProviders.CODEC.fieldOf("max").forGetter(UniformGenerator::max)).apply((com.mojang.datafixers.kinds.Applicative)i, UniformGenerator::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootNumberProviderType getType() {
/* 23 */     return NumberProviders.UNIFORM;
/*    */   }
/*    */   
/*    */   public static UniformGenerator between(float min, float max) {
/* 27 */     return new UniformGenerator(ConstantValue.exactly(min), ConstantValue.exactly(max));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt(LootContext context) {
/* 32 */     return net.minecraft.util.Mth.nextInt(context.getRandom(), this.min.getInt(context), this.max.getInt(context));
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 37 */     return net.minecraft.util.Mth.nextFloat(context.getRandom(), this.min.getFloat(context), this.max.getFloat(context));
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 42 */     return (java.util.Set<net.minecraft.util.context.ContextKey<?>>)com.google.common.collect.Sets.union(this.min.getReferencedContextParams(), this.max.getReferencedContextParams());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/number/UniformGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */