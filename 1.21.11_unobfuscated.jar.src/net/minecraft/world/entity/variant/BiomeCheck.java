/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public final class BiomeCheck extends Record implements SpawnCondition {
/*    */   private final net.minecraft.core.HolderSet<Biome> requiredBiomes;
/*    */   public static final com.mojang.serialization.MapCodec<BiomeCheck> MAP_CODEC;
/*    */   
/* 10 */   public BiomeCheck(net.minecraft.core.HolderSet<Biome> requiredBiomes) { this.requiredBiomes = requiredBiomes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/BiomeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck; } public net.minecraft.core.HolderSet<Biome> requiredBiomes() { return this.requiredBiomes; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/BiomeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/BiomeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BIOME).fieldOf("biomes").forGetter(BiomeCheck::requiredBiomes)).apply((com.mojang.datafixers.kinds.Applicative)i, BiomeCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 20 */     return this.requiredBiomes.contains(context.biome());
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<BiomeCheck> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/BiomeCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */