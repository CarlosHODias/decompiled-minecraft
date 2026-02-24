/*    */ package net.minecraft.world.level.dimension;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class LevelStem extends Record {
/*    */   private final net.minecraft.core.Holder<DimensionType> type;
/*    */   private final net.minecraft.world.level.chunk.ChunkGenerator generator;
/*    */   public static final com.mojang.serialization.Codec<LevelStem> CODEC;
/*    */   
/* 11 */   public LevelStem(net.minecraft.core.Holder<DimensionType> type, net.minecraft.world.level.chunk.ChunkGenerator generator) { this.type = type; this.generator = generator; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/LevelStem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem; } public net.minecraft.core.Holder<DimensionType> type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/LevelStem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/LevelStem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/LevelStem;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.chunk.ChunkGenerator generator() { return this.generator; }
/*    */ 
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)DimensionType.CODEC.fieldOf("type").forGetter(LevelStem::type), (com.mojang.datafixers.kinds.App)net.minecraft.world.level.chunk.ChunkGenerator.CODEC.fieldOf("generator").forGetter(LevelStem::generator)).apply((com.mojang.datafixers.kinds.Applicative)i, i.stable(LevelStem::new)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final ResourceKey<LevelStem> OVERWORLD = ResourceKey.create(net.minecraft.core.registries.Registries.LEVEL_STEM, net.minecraft.resources.Identifier.withDefaultNamespace("overworld"));
/* 21 */   public static final ResourceKey<LevelStem> NETHER = ResourceKey.create(net.minecraft.core.registries.Registries.LEVEL_STEM, net.minecraft.resources.Identifier.withDefaultNamespace("the_nether"));
/* 22 */   public static final ResourceKey<LevelStem> END = ResourceKey.create(net.minecraft.core.registries.Registries.LEVEL_STEM, net.minecraft.resources.Identifier.withDefaultNamespace("the_end"));
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/dimension/LevelStem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */