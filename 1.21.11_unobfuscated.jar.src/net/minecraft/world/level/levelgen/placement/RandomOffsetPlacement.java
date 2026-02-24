/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class RandomOffsetPlacement extends PlacementModifier {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProvider.codec(-16, 16).fieldOf("xz_spread").forGetter(()), (App)IntProvider.codec(-16, 16).fieldOf("y_spread").forGetter(())).apply((Applicative)i, RandomOffsetPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomOffsetPlacement> CODEC;
/*    */   private final IntProvider xzSpread;
/*    */   private final IntProvider ySpread;
/*    */   
/*    */   public static RandomOffsetPlacement of(IntProvider xzSpread, IntProvider ySpread) {
/* 25 */     return new RandomOffsetPlacement(xzSpread, ySpread);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement vertical(IntProvider ySpread) {
/* 29 */     return new RandomOffsetPlacement((IntProvider)ConstantInt.of(0), ySpread);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement horizontal(IntProvider xzSpread) {
/* 33 */     return new RandomOffsetPlacement(xzSpread, (IntProvider)ConstantInt.of(0));
/*    */   }
/*    */   
/*    */   private RandomOffsetPlacement(IntProvider xzSpread, IntProvider ySpread) {
/* 37 */     this.xzSpread = xzSpread;
/* 38 */     this.ySpread = ySpread;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 43 */     int scatterX = origin.getX() + this.xzSpread.sample(random);
/* 44 */     int scatterY = origin.getY() + this.ySpread.sample(random);
/* 45 */     int scatterZ = origin.getZ() + this.xzSpread.sample(random);
/* 46 */     return Stream.of(new BlockPos(scatterX, scatterY, scatterZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 51 */     return PlacementModifierType.RANDOM_OFFSET;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/RandomOffsetPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */