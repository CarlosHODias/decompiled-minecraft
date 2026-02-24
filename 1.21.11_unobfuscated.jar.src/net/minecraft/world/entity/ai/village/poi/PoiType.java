/*    */ package net.minecraft.world.entity.ai.village.poi;
/*    */ 
/*    */ 
/*    */ public final class PoiType extends Record {
/*    */   private final java.util.Set<net.minecraft.world.level.block.state.BlockState> matchingStates;
/*    */   private final int maxTickets;
/*    */   private final int validRange;
/*    */   
/*  9 */   public java.util.Set<net.minecraft.world.level.block.state.BlockState> matchingStates() { return this.matchingStates; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int maxTickets() { return this.maxTickets; } public int validRange() { return this.validRange; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final java.util.function.Predicate<net.minecraft.core.Holder<PoiType>> NONE = poiType -> false;
/*    */   
/*    */   public PoiType(java.util.Set<net.minecraft.world.level.block.state.BlockState> matchingStates, int maxTickets, int validRange)
/*    */   {
/* 17 */     matchingStates = java.util.Set.copyOf(matchingStates);
/*    */     this.matchingStates = matchingStates;
/*    */     this.maxTickets = maxTickets;
/*    */     this.validRange = validRange; } public boolean is(net.minecraft.world.level.block.state.BlockState state) {
/* 21 */     return this.matchingStates.contains(state);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/village/poi/PoiType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */