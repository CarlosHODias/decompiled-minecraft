/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class SwampHutStructure extends Structure {
/* 12 */   public static final MapCodec<SwampHutStructure> CODEC = simpleCodec(SwampHutStructure::new);
/*    */   
/*    */   public SwampHutStructure(Structure.StructureSettings settings) {
/* 15 */     super(settings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 20 */     return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 24 */     builder.addPiece((StructurePiece)new SwampHutPiece((net.minecraft.util.RandomSource)context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ()));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 29 */     return StructureType.SWAMP_HUT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/SwampHutStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */