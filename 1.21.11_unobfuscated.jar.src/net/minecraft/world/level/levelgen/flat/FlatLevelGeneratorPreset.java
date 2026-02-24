/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public final class FlatLevelGeneratorPreset extends Record {
/*    */   private final Holder<net.minecraft.world.item.Item> displayItem;
/*    */   private final FlatLevelGeneratorSettings settings;
/*    */   public static final com.mojang.serialization.Codec<FlatLevelGeneratorPreset> DIRECT_CODEC;
/*    */   
/* 10 */   public FlatLevelGeneratorPreset(Holder<net.minecraft.world.item.Item> displayItem, FlatLevelGeneratorSettings settings) { this.displayItem = displayItem; this.settings = settings; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset; } public Holder<net.minecraft.world.item.Item> displayItem() { return this.displayItem; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public FlatLevelGeneratorSettings settings() { return this.settings; }
/*    */ 
/*    */   
/*    */   static {
/* 14 */     DIRECT_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.Item.CODEC.fieldOf("display").forGetter(()), (com.mojang.datafixers.kinds.App)FlatLevelGeneratorSettings.CODEC.fieldOf("settings").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, FlatLevelGeneratorPreset::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final com.mojang.serialization.Codec<Holder<FlatLevelGeneratorPreset>> CODEC = (com.mojang.serialization.Codec<Holder<FlatLevelGeneratorPreset>>)net.minecraft.resources.RegistryFileCodec.create(net.minecraft.core.registries.Registries.FLAT_LEVEL_GENERATOR_PRESET, DIRECT_CODEC);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */