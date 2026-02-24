/*    */ package net.minecraft.client.gui.screens.worldselection;
/*    */ 
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.ReloadableServerResources;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ import net.minecraft.world.level.levelgen.WorldOptions;
/*    */ 
/*    */ public final class WorldCreationContext extends Record {
/*    */   private final WorldOptions options;
/*    */   private final net.minecraft.core.Registry<net.minecraft.world.level.dimension.LevelStem> datapackDimensions;
/*    */   private final WorldDimensions selectedDimensions;
/*    */   private final LayeredRegistryAccess<RegistryLayer> worldgenRegistries;
/*    */   private final ReloadableServerResources dataPackResources;
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   private final InitialWorldCreationOptions initialWorldCreationOptions;
/*    */   
/* 19 */   public WorldCreationContext(WorldOptions options, net.minecraft.core.Registry<net.minecraft.world.level.dimension.LevelStem> datapackDimensions, WorldDimensions selectedDimensions, LayeredRegistryAccess<RegistryLayer> worldgenRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) { this.options = options; this.datapackDimensions = datapackDimensions; this.selectedDimensions = selectedDimensions; this.worldgenRegistries = worldgenRegistries; this.dataPackResources = dataPackResources; this.dataConfiguration = dataConfiguration; this.initialWorldCreationOptions = initialWorldCreationOptions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext; } public WorldOptions options() { return this.options; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Registry<net.minecraft.world.level.dimension.LevelStem> datapackDimensions() { return this.datapackDimensions; } public WorldDimensions selectedDimensions() { return this.selectedDimensions; } public LayeredRegistryAccess<RegistryLayer> worldgenRegistries() { return this.worldgenRegistries; } public ReloadableServerResources dataPackResources() { return this.dataPackResources; } public WorldDataConfiguration dataConfiguration() { return this.dataConfiguration; } public InitialWorldCreationOptions initialWorldCreationOptions() { return this.initialWorldCreationOptions; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldCreationContext(net.minecraft.world.level.levelgen.WorldGenSettings worldGenSettings, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration) {
/* 29 */     this(
/* 30 */         worldGenSettings.options(), 
/* 31 */         worldGenSettings.dimensions(), loadedRegistries, dataPackResources, dataConfiguration, new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.SURVIVAL, 
/*    */ 
/*    */ 
/*    */           
/* 35 */           net.minecraft.world.level.gamerules.GameRuleMap.of(), null));
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldCreationContext(WorldOptions worldOptions, WorldDimensions worldDimensions, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) {
/* 40 */     this(worldOptions, 
/*    */         
/* 42 */         loadedRegistries.getLayer(RegistryLayer.DIMENSIONS).lookupOrThrow(net.minecraft.core.registries.Registries.LEVEL_STEM), worldDimensions, 
/*    */         
/* 44 */         loadedRegistries.replaceFrom(RegistryLayer.DIMENSIONS, new net.minecraft.core.RegistryAccess.Frozen[0]), dataPackResources, dataConfiguration, initialWorldCreationOptions);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldCreationContext withSettings(WorldOptions options, WorldDimensions dimensions) {
/* 52 */     return new WorldCreationContext(options, this.datapackDimensions, dimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldCreationContext withOptions(OptionsModifier modifier) {
/* 59 */     return new WorldCreationContext(modifier.apply(this.options), this.datapackDimensions, this.selectedDimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WorldCreationContext withDimensions(DimensionsUpdater modifier) {
/* 66 */     return new WorldCreationContext(this.options, this.datapackDimensions, modifier.apply(worldgenLoadContext(), this.selectedDimensions), this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */   
/*    */   public net.minecraft.core.RegistryAccess.Frozen worldgenLoadContext() {
/* 70 */     return this.worldgenRegistries.compositeAccess();
/*    */   }
/*    */   
/*    */   public void validate() {
/* 74 */     for (net.minecraft.world.level.dimension.LevelStem stem : datapackDimensions())
/* 75 */       stem.generator().validate(); 
/*    */   }
/*    */   
/*    */   public static interface OptionsModifier extends java.util.function.UnaryOperator<WorldOptions> {}
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface DimensionsUpdater extends java.util.function.BiFunction<net.minecraft.core.RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/WorldCreationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */