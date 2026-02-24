/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class FluidTagsProvider extends IntrinsicHolderTagsProvider<Fluid> {
/*    */   public FluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.FLUID, lookupProvider, e -> e.builtInRegistryHolder().key());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(FluidTags.WATER).add(new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER });
/* 20 */     tag(FluidTags.LAVA).add(new Fluid[] { (Fluid)Fluids.LAVA, (Fluid)Fluids.FLOWING_LAVA });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/FluidTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */