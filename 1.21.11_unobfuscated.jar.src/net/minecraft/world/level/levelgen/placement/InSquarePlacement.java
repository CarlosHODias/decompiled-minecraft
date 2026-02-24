/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InSquarePlacement
/*    */   extends PlacementModifier
/*    */ {
/* 16 */   private static final InSquarePlacement INSTANCE = new InSquarePlacement();
/*    */   
/* 18 */   public static final MapCodec<InSquarePlacement> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public static InSquarePlacement spread() {
/* 21 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 26 */     int x = random.nextInt(16) + origin.getX();
/* 27 */     int z = random.nextInt(16) + origin.getZ();
/*    */     
/* 29 */     return Stream.of(new BlockPos(x, origin.getY(), z));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 34 */     return PlacementModifierType.IN_SQUARE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/InSquarePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */