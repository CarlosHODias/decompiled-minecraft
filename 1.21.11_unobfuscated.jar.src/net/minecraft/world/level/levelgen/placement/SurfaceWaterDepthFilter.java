/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceWaterDepthFilter
/*    */   extends PlacementFilter {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("max_water_depth").forGetter(())).apply((Applicative)i, SurfaceWaterDepthFilter::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SurfaceWaterDepthFilter> CODEC;
/*    */   private final int maxWaterDepth;
/*    */   
/*    */   private SurfaceWaterDepthFilter(int maxWaterDepth) {
/* 23 */     this.maxWaterDepth = maxWaterDepth;
/*    */   }
/*    */   
/*    */   public static SurfaceWaterDepthFilter forMaxDepth(int maxWaterDepth) {
/* 27 */     return new SurfaceWaterDepthFilter(maxWaterDepth);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 32 */     int yOceanFloor = context.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ());
/* 33 */     int ySurfaceFloor = context.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX(), origin.getZ());
/*    */     
/* 35 */     return (ySurfaceFloor - yOceanFloor <= this.maxWaterDepth);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 40 */     return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/SurfaceWaterDepthFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */