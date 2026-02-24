/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ 
/*    */ public class JungleTempleStructure extends SinglePieceStructure {
/*  8 */   public static final com.mojang.serialization.MapCodec<JungleTempleStructure> CODEC = simpleCodec(JungleTempleStructure::new);
/*    */   
/*    */   public JungleTempleStructure(Structure.StructureSettings settings) {
/* 11 */     super(JungleTemplePiece::new, 12, 15, settings);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 16 */     return StructureType.JUNGLE_TEMPLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/JungleTempleStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */