/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.model.Variant;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public final class MultiVariant extends Record {
/*    */   private final WeightedList<Variant> variants;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/MultiVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/MultiVariant;
/*    */   }
/*    */   
/* 13 */   public WeightedList<Variant> variants() { return this.variants; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/MultiVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/MultiVariant; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/MultiVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/MultiVariant;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public MultiVariant(WeightedList<Variant> variants) { if (variants.isEmpty())
/* 16 */       throw new IllegalArgumentException("Variant list must contain at least one element"); 
/*    */     this.variants = variants; }
/*    */ 
/*    */   
/*    */   public MultiVariant with(net.minecraft.client.renderer.block.model.VariantMutator mutator) {
/* 21 */     return new MultiVariant(this.variants.map((java.util.function.Function)mutator));
/*    */   }
/*    */   
/*    */   public net.minecraft.client.renderer.block.model.BlockStateModel.Unbaked toUnbaked() {
/* 25 */     java.util.List<net.minecraft.util.random.Weighted<Variant>> entries = this.variants.unwrap();
/* 26 */     return (entries.size() == 1) ? 
/* 27 */       (net.minecraft.client.renderer.block.model.BlockStateModel.Unbaked)new net.minecraft.client.renderer.block.model.SingleVariant.Unbaked((Variant)((net.minecraft.util.random.Weighted)entries.getFirst()).value()) : 
/* 28 */       (net.minecraft.client.renderer.block.model.BlockStateModel.Unbaked)new net.minecraft.client.resources.model.WeightedVariants.Unbaked(this.variants.map(net.minecraft.client.renderer.block.model.SingleVariant.Unbaked::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/MultiVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */