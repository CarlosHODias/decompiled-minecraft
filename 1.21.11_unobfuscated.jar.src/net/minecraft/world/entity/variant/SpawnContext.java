/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ public final class SpawnContext extends Record {
/*    */   private final net.minecraft.core.BlockPos pos;
/*    */   private final net.minecraft.world.level.ServerLevelAccessor level;
/*    */   private final net.minecraft.world.attribute.EnvironmentAttributeReader environmentAttributes;
/*    */   private final net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome;
/*    */   
/*  9 */   public SpawnContext(net.minecraft.core.BlockPos pos, net.minecraft.world.level.ServerLevelAccessor level, net.minecraft.world.attribute.EnvironmentAttributeReader environmentAttributes, net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome) { this.pos = pos; this.level = level; this.environmentAttributes = environmentAttributes; this.biome = biome; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/SpawnContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnContext; } public net.minecraft.core.BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/SpawnContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnContext; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/SpawnContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/SpawnContext;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.ServerLevelAccessor level() { return this.level; } public net.minecraft.world.attribute.EnvironmentAttributeReader environmentAttributes() { return this.environmentAttributes; } public net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome() { return this.biome; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SpawnContext create(net.minecraft.world.level.ServerLevelAccessor level, net.minecraft.core.BlockPos pos) {
/* 16 */     net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome = level.getBiome(pos);
/* 17 */     return new SpawnContext(pos, level, level.environmentAttributes(), biome);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/SpawnContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */