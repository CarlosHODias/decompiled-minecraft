/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class BinomialDistributionGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider n;
/*    */   private final NumberProvider p;
/*    */   public static final com.mojang.serialization.MapCodec<BinomialDistributionGenerator> CODEC;
/*    */   
/* 12 */   public BinomialDistributionGenerator(NumberProvider n, NumberProvider p) { this.n = n; this.p = p; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator; } public NumberProvider n() { return this.n; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public NumberProvider p() { return this.p; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("n").forGetter(BinomialDistributionGenerator::n), (App)NumberProviders.CODEC.fieldOf("p").forGetter(BinomialDistributionGenerator::p)).apply((com.mojang.datafixers.kinds.Applicative)i, BinomialDistributionGenerator::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootNumberProviderType getType() {
/* 23 */     return NumberProviders.BINOMIAL;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getInt(LootContext context) {
/* 30 */     int n = this.n.getInt(context);
/* 31 */     float p = this.p.getFloat(context);
/* 32 */     net.minecraft.util.RandomSource random = context.getRandom();
/* 33 */     int result = 0;
/* 34 */     for (int i = 0; i < n; i++) {
/* 35 */       if (random.nextFloat() < p) {
/* 36 */         result++;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 45 */     return getInt(context);
/*    */   }
/*    */   
/*    */   public static BinomialDistributionGenerator binomial(int n, float p) {
/* 49 */     return new BinomialDistributionGenerator(ConstantValue.exactly(n), ConstantValue.exactly(p));
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 54 */     return (java.util.Set<net.minecraft.util.context.ContextKey<?>>)com.google.common.collect.Sets.union(this.n.getReferencedContextParams(), this.p.getReferencedContextParams());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */