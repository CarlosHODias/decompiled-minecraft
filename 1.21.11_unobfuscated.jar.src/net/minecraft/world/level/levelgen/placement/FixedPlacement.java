/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FixedPlacement extends PlacementModifier {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPos.CODEC.listOf().fieldOf("positions").forGetter(())).apply((Applicative)i, FixedPlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<FixedPlacement> CODEC;
/*    */   private final List<BlockPos> positions;
/*    */   
/*    */   public static FixedPlacement of(BlockPos... pos) {
/* 23 */     return new FixedPlacement(List.of(pos));
/*    */   }
/*    */   
/*    */   private FixedPlacement(List<BlockPos> positions) {
/* 27 */     this.positions = positions;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 32 */     int chunkX = SectionPos.blockToSectionCoord(origin.getX());
/* 33 */     int chunkZ = SectionPos.blockToSectionCoord(origin.getZ());
/*    */     boolean hasPositions = false;
/* 35 */     for (BlockPos position : this.positions) {
/* 36 */       if (isSameChunk(chunkX, chunkZ, position)) {
/* 37 */         hasPositions = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 41 */     if (!hasPositions) {
/* 42 */       return Stream.empty();
/*    */     }
/* 44 */     return this.positions.stream().filter(pos -> isSameChunk(chunkX, chunkZ, pos));
/*    */   }
/*    */   
/*    */   private static boolean isSameChunk(int chunkX, int chunkZ, BlockPos position) {
/* 48 */     return (chunkX == SectionPos.blockToSectionCoord(position.getX()) && chunkZ == SectionPos.blockToSectionCoord(position.getZ()));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 53 */     return PlacementModifierType.FIXED_PLACEMENT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/FixedPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */