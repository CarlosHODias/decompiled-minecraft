/*    */ package net.minecraft.data.registries;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.Cloner;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RegistryPatchGenerator {
/*    */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla, RegistrySetBuilder packBuilder) {
/* 19 */     return vanilla.thenApply(parent -> {
/*    */           RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */           Cloner.Factory cloner = new Cloner.Factory();
/*    */           RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(());
/*    */           RegistrySetBuilder.PatchedRegistries newRegistries = packBuilder.buildPatch((RegistryAccess)staticRegistries, parent, cloner);
/*    */           HolderLookup.Provider fullPatchedRegistry = newRegistries.full();
/*    */           Optional<? extends HolderLookup.RegistryLookup<Biome>> biomes = fullPatchedRegistry.lookup(Registries.BIOME);
/*    */           Optional<? extends HolderLookup.RegistryLookup<PlacedFeature>> features = fullPatchedRegistry.lookup(Registries.PLACED_FEATURE);
/*    */           if (biomes.isPresent() || features.isPresent())
/*    */             VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter<PlacedFeature>)DataFixUtils.orElseGet(features, ()), (HolderLookup<Biome>)DataFixUtils.orElseGet(biomes, ())); 
/*    */           return newRegistries;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/registries/RegistryPatchGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */