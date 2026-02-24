/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class Pools {
/* 12 */   public static final ResourceKey<StructureTemplatePool> EMPTY = createKey("empty");
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> createKey(Identifier location) {
/* 15 */     return ResourceKey.create(Registries.TEMPLATE_POOL, location);
/*    */   }
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> createKey(String name) {
/* 19 */     return createKey(Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> parseKey(String name) {
/* 23 */     return createKey(Identifier.parse(name));
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<StructureTemplatePool> context, String name, StructureTemplatePool pool) {
/* 27 */     context.register(createKey(name), pool);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/* 31 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/* 32 */     Holder.Reference reference = pools.getOrThrow(EMPTY);
/*    */ 
/*    */     
/* 35 */     context.register(EMPTY, new StructureTemplatePool((Holder)reference, (List)ImmutableList.of(), StructureTemplatePool.Projection.RIGID));
/*    */     
/* 37 */     BastionPieces.bootstrap(context);
/* 38 */     PillagerOutpostPools.bootstrap(context);
/* 39 */     VillagePools.bootstrap(context);
/* 40 */     AncientCityStructurePieces.bootstrap(context);
/* 41 */     TrailRuinsStructurePools.bootstrap(context);
/* 42 */     TrialChambersStructurePools.bootstrap(context);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/Pools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */