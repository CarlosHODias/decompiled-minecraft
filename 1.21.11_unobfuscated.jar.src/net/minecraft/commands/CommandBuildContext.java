/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public interface CommandBuildContext
/*    */   extends HolderLookup.Provider {
/*    */   static CommandBuildContext simple(final HolderLookup.Provider access, final FeatureFlagSet enabledFeatures) {
/* 13 */     return new CommandBuildContext()
/*    */       {
/*    */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 16 */           return access.listRegistryKeys();
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 21 */           return access.lookup(key).map(lookup -> lookup.filterFeatures(enabledFeatures));
/*    */         }
/*    */ 
/*    */         
/*    */         public FeatureFlagSet enabledFeatures() {
/* 26 */           return enabledFeatures;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   FeatureFlagSet enabledFeatures();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/CommandBuildContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */