/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public final class WorldDataConfiguration extends Record {
/*    */   private final DataPackConfig dataPacks;
/*    */   private final net.minecraft.world.flag.FeatureFlagSet enabledFeatures;
/*    */   public static final String ENABLED_FEATURES_ID = "enabled_features";
/*    */   public static final com.mojang.serialization.MapCodec<WorldDataConfiguration> MAP_CODEC;
/*    */   
/*  9 */   public WorldDataConfiguration(DataPackConfig dataPacks, net.minecraft.world.flag.FeatureFlagSet enabledFeatures) { this.dataPacks = dataPacks; this.enabledFeatures = enabledFeatures; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/WorldDataConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration; } public DataPackConfig dataPacks() { return this.dataPacks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/WorldDataConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/WorldDataConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/WorldDataConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.flag.FeatureFlagSet enabledFeatures() { return this.enabledFeatures; }
/*    */   
/*    */   static {
/* 12 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)DataPackConfig.CODEC.lenientOptionalFieldOf("DataPacks", DataPackConfig.DEFAULT).forGetter(WorldDataConfiguration::dataPacks), (com.mojang.datafixers.kinds.App)net.minecraft.world.flag.FeatureFlags.CODEC.lenientOptionalFieldOf("enabled_features", net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS).forGetter(WorldDataConfiguration::enabledFeatures)).apply((com.mojang.datafixers.kinds.Applicative)i, WorldDataConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/* 16 */   public static final com.mojang.serialization.Codec<WorldDataConfiguration> CODEC = MAP_CODEC.codec();
/*    */   
/* 18 */   public static final WorldDataConfiguration DEFAULT = new WorldDataConfiguration(DataPackConfig.DEFAULT, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldDataConfiguration expandFeatures(net.minecraft.world.flag.FeatureFlagSet newEnabledFeatures) {
/* 24 */     return new WorldDataConfiguration(this.dataPacks, this.enabledFeatures.join(newEnabledFeatures));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/WorldDataConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */