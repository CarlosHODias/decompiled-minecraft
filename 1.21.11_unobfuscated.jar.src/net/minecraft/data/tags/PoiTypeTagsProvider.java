/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.PoiTypeTags;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ 
/*    */ public class PoiTypeTagsProvider extends KeyTagProvider<PoiType> {
/*    */   public PoiTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.POINT_OF_INTEREST_TYPE, lookupProvider);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
/* 20 */       .add((ResourceKey<PoiType>[])new ResourceKey[] { 
/*    */           PoiTypes.ARMORER, PoiTypes.BUTCHER, PoiTypes.CARTOGRAPHER, PoiTypes.CLERIC, PoiTypes.FARMER, PoiTypes.FISHERMAN, PoiTypes.FLETCHER, PoiTypes.LEATHERWORKER, PoiTypes.LIBRARIAN, PoiTypes.MASON, 
/*    */           PoiTypes.SHEPHERD, PoiTypes.TOOLSMITH, PoiTypes.WEAPONSMITH });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     tag(PoiTypeTags.VILLAGE)
/* 37 */       .addTag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
/* 38 */       .add((Object[])new ResourceKey[] { PoiTypes.HOME, PoiTypes.MEETING });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     tag(PoiTypeTags.BEE_HOME)
/* 44 */       .add((ResourceKey<PoiType>[])new ResourceKey[] { PoiTypes.BEEHIVE, PoiTypes.BEE_NEST });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/PoiTypeTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */