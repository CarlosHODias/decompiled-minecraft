/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class BastionSharedPools {
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/* 13 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/* 14 */     Holder.Reference reference = pools.getOrThrow(Pools.EMPTY);
/*    */     
/* 16 */     Pools.register(context, "bastion/mobs/piglin", new StructureTemplatePool((Holder)reference, 
/*    */           
/* 18 */           (List)ImmutableList.of(
/* 19 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 1), 
/* 20 */             Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 4), 
/* 21 */             Pair.of(StructurePoolElement.single("bastion/mobs/crossbow_piglin"), 4), 
/* 22 */             Pair.of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     Pools.register(context, "bastion/mobs/hoglin", new StructureTemplatePool((Holder)reference, 
/*    */           
/* 29 */           (List)ImmutableList.of(
/* 30 */             Pair.of(StructurePoolElement.single("bastion/mobs/hoglin"), 2), 
/* 31 */             Pair.of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     Pools.register(context, "bastion/blocks/gold", new StructureTemplatePool((Holder)reference, 
/*    */           
/* 38 */           (List)ImmutableList.of(
/* 39 */             Pair.of(StructurePoolElement.single("bastion/blocks/air"), 3), 
/* 40 */             Pair.of(StructurePoolElement.single("bastion/blocks/gold"), 1)), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 45 */     Pools.register(context, "bastion/mobs/piglin_melee", new StructureTemplatePool((Holder)reference, 
/*    */           
/* 47 */           (List)ImmutableList.of(
/* 48 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin_always"), 1), 
/* 49 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 5), 
/* 50 */             Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 1)), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/BastionSharedPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */