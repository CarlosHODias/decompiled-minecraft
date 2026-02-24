/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface StructurePlacementType<SP extends StructurePlacement> {
/*  8 */   public static final StructurePlacementType<RandomSpreadStructurePlacement> RANDOM_SPREAD = register("random_spread", RandomSpreadStructurePlacement.CODEC);
/*  9 */   public static final StructurePlacementType<ConcentricRingsStructurePlacement> CONCENTRIC_RINGS = register("concentric_rings", ConcentricRingsStructurePlacement.CODEC);
/*    */   
/*    */   MapCodec<SP> codec();
/*    */   
/*    */   private static <SP extends StructurePlacement> StructurePlacementType<SP> register(String id, MapCodec<SP> codec) {
/* 14 */     return (StructurePlacementType<SP>)Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, id, () -> codec);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/placement/StructurePlacementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */