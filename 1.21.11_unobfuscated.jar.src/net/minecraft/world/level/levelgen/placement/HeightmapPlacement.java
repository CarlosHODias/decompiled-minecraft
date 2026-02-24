/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapPlacement extends PlacementModifier {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(())).apply((Applicative)i, HeightmapPlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<HeightmapPlacement> CODEC;
/*    */   private final Heightmap.Types heightmap;
/*    */   
/*    */   private HeightmapPlacement(Heightmap.Types heightmap) {
/* 22 */     this.heightmap = heightmap;
/*    */   }
/*    */   
/*    */   public static HeightmapPlacement onHeightmap(Heightmap.Types heightmap) {
/* 26 */     return new HeightmapPlacement(heightmap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 31 */     int x = origin.getX();
/* 32 */     int z = origin.getZ();
/* 33 */     int height = context.getHeight(this.heightmap, x, z);
/* 34 */     if (height > context.getMinY()) {
/* 35 */       return Stream.of(new BlockPos(x, height, z));
/*    */     }
/* 37 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 42 */     return PlacementModifierType.HEIGHTMAP;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/HeightmapPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */