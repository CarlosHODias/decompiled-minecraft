/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class WorldGenSettings extends Record {
/*    */   private final WorldOptions options;
/*    */   private final WorldDimensions dimensions;
/*    */   public static final com.mojang.serialization.Codec<WorldGenSettings> CODEC;
/*    */   
/* 10 */   public WorldGenSettings(WorldOptions options, WorldDimensions dimensions) { this.options = options; this.dimensions = dimensions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings; } public WorldOptions options() { return this.options; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public WorldDimensions dimensions() { return this.dimensions; }
/*    */ 
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)WorldOptions.CODEC.forGetter(WorldGenSettings::options), (com.mojang.datafixers.kinds.App)WorldDimensions.CODEC.forGetter(WorldGenSettings::dimensions)).apply((com.mojang.datafixers.kinds.Applicative)i, i.stable(WorldGenSettings::new)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> com.mojang.serialization.DataResult<T> encode(com.mojang.serialization.DynamicOps<T> ops, WorldOptions options, WorldDimensions dimensions) {
/* 20 */     return CODEC.encodeStart(ops, new WorldGenSettings(options, dimensions));
/*    */   }
/*    */   
/*    */   public static <T> com.mojang.serialization.DataResult<T> encode(com.mojang.serialization.DynamicOps<T> ops, WorldOptions options, net.minecraft.core.RegistryAccess registryAccess) {
/* 24 */     return encode(ops, options, new WorldDimensions(registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.LEVEL_STEM)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/WorldGenSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */