/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public final class SimpleBlockConfiguration extends Record implements FeatureConfiguration {
/*    */   private final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toPlace;
/*    */   private final boolean scheduleTick;
/*    */   public static final com.mojang.serialization.Codec<SimpleBlockConfiguration> CODEC;
/*    */   
/*  7 */   public SimpleBlockConfiguration(net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toPlace, boolean scheduleTick) { this.toPlace = toPlace; this.scheduleTick = scheduleTick; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration; } public net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toPlace() { return this.toPlace; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public boolean scheduleTick() { return this.scheduleTick; } static {
/*  8 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("to_place").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("schedule_tick", false).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SimpleBlockConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleBlockConfiguration(net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toPlace) {
/* 14 */     this(toPlace, false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */