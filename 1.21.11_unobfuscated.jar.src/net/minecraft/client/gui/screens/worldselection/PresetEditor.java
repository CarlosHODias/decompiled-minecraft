/*    */ package net.minecraft.client.gui.screens.worldselection;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.gui.screens.CreateBuffetWorldScreen;
/*    */ import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*    */ 
/*    */ public interface PresetEditor {
/*    */   public static final Map<Optional<ResourceKey<WorldPreset>>, PresetEditor> EDITORS;
/*    */   
/*    */   static {
/* 31 */     EDITORS = Map.of(
/* 32 */         Optional.of(WorldPresets.FLAT), (parent, settings) -> {
/*    */           ChunkGenerator overworld = settings.selectedDimensions().overworld();
/*    */ 
/*    */ 
/*    */           
/*    */           RegistryAccess.Frozen frozen = settings.worldgenLoadContext();
/*    */ 
/*    */           
/*    */           Registry registry1 = frozen.lookupOrThrow(Registries.BIOME), registry2 = frozen.lookupOrThrow(Registries.STRUCTURE_SET), registry3 = frozen.lookupOrThrow(Registries.PLACED_FEATURE);
/*    */ 
/*    */           
/*    */           return new CreateFlatWorldScreen(parent, (), (overworld instanceof FlatLevelSource) ? ((FlatLevelSource)overworld).settings() : FlatLevelGeneratorSettings.getDefault((HolderGetter)registry1, (HolderGetter)registry2, (HolderGetter)registry3));
/* 44 */         }, Optional.of(WorldPresets.SINGLE_BIOME_SURFACE), (parent, settings) -> new CreateBuffetWorldScreen(parent, settings, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static WorldCreationContext.DimensionsUpdater flatWorldConfigurator(FlatLevelGeneratorSettings generatorSettings) {
/* 52 */     return (registryAccess, dimensions) -> {
/*    */         FlatLevelSource flatLevelSource = new FlatLevelSource(generatorSettings);
/*    */         return dimensions.replaceOverworldGenerator((HolderLookup.Provider)registryAccess, (ChunkGenerator)flatLevelSource);
/*    */       };
/*    */   }
/*    */   
/*    */   private static WorldCreationContext.DimensionsUpdater fixedBiomeConfigurator(Holder<Biome> biome) {
/* 59 */     return (registryAccess, dimensions) -> {
/*    */         Registry<NoiseGeneratorSettings> noiseGeneratorSettings = registryAccess.lookupOrThrow(Registries.NOISE_SETTINGS);
/*    */         Holder.Reference reference = noiseGeneratorSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/*    */         FixedBiomeSource fixedBiomeSource = new FixedBiomeSource(biome);
/*    */         NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator((BiomeSource)fixedBiomeSource, (Holder)reference);
/*    */         return dimensions.replaceOverworldGenerator((HolderLookup.Provider)registryAccess, (ChunkGenerator)noiseBasedChunkGenerator);
/*    */       };
/*    */   }
/*    */   
/*    */   Screen createEditScreen(CreateWorldScreen paramCreateWorldScreen, WorldCreationContext paramWorldCreationContext);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/PresetEditor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */