/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ 
/*    */ public final class SpawnPrioritySelectors extends Record {
/*    */   private final java.util.List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors;
/*    */   
/*  7 */   public SpawnPrioritySelectors(java.util.List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors) { this.selectors = selectors; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors; } public java.util.List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.selectors; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 10 */   } public static final SpawnPrioritySelectors EMPTY = new SpawnPrioritySelectors(java.util.List.of());
/*    */   
/*    */   public static SpawnPrioritySelectors single(SpawnCondition condition, int priority) {
/* 13 */     return new SpawnPrioritySelectors(PriorityProvider.single(condition, priority));
/*    */   }
/*    */   
/*    */   public static SpawnPrioritySelectors fallback(int priority) {
/* 17 */     return new SpawnPrioritySelectors(PriorityProvider.alwaysTrue(priority));
/*    */   }
/*    */   
/* 20 */   public static final com.mojang.serialization.Codec<SpawnPrioritySelectors> CODEC = PriorityProvider.Selector.<Context, SpawnCondition>codec(SpawnCondition.CODEC)
/* 21 */     .listOf()
/* 22 */     .xmap(SpawnPrioritySelectors::new, SpawnPrioritySelectors::selectors);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/SpawnPrioritySelectors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */