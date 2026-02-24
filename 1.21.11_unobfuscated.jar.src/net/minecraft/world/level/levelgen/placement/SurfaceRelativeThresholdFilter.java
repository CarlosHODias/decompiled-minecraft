/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceRelativeThresholdFilter extends PlacementFilter {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(()), (App)Codec.INT.optionalFieldOf("min_inclusive", Integer.MIN_VALUE).forGetter(()), (App)Codec.INT.optionalFieldOf("max_inclusive", Integer.MAX_VALUE).forGetter(())).apply((Applicative)i, SurfaceRelativeThresholdFilter::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SurfaceRelativeThresholdFilter> CODEC;
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   private SurfaceRelativeThresholdFilter(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
/* 26 */     this.heightmap = heightmap;
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public static SurfaceRelativeThresholdFilter of(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
/* 32 */     return new SurfaceRelativeThresholdFilter(heightmap, minInclusive, maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 37 */     long surfaceY = context.getHeight(this.heightmap, origin.getX(), origin.getZ());
/*    */     
/* 39 */     long minY = surfaceY + this.minInclusive;
/* 40 */     long maxY = surfaceY + this.maxInclusive;
/*    */     
/* 42 */     return (minY <= origin.getY() && origin.getY() <= maxY);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 47 */     return PlacementModifierType.SURFACE_RELATIVE_THRESHOLD_FILTER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/SurfaceRelativeThresholdFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */