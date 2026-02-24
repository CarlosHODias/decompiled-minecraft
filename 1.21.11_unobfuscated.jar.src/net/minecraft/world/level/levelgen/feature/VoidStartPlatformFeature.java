/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VoidStartPlatformFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 12 */   private static final BlockPos PLATFORM_OFFSET = new BlockPos(8, 3, 8);
/* 13 */   private static final ChunkPos PLATFORM_ORIGIN_CHUNK = new ChunkPos(PLATFORM_OFFSET);
/*    */   private static final int PLATFORM_RADIUS = 16;
/*    */   private static final int PLATFORM_RADIUS_CHUNKS = 1;
/*    */   
/*    */   public VoidStartPlatformFeature(Codec<NoneFeatureConfiguration> codec) {
/* 18 */     super(codec);
/*    */   }
/*    */   
/*    */   private static int checkerboardDistance(int xa, int za, int xb, int zb) {
/* 22 */     return Math.max(Math.abs(xa - xb), Math.abs(za - zb));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 27 */     WorldGenLevel level = context.level();
/* 28 */     ChunkPos currentChunkPos = new ChunkPos(context.origin());
/* 29 */     if (checkerboardDistance(currentChunkPos.x, currentChunkPos.z, PLATFORM_ORIGIN_CHUNK.x, PLATFORM_ORIGIN_CHUNK.z) > 1) {
/* 30 */       return true;
/*    */     }
/*    */     
/* 33 */     BlockPos platformOrigin = PLATFORM_OFFSET.atY(context.origin().getY() + PLATFORM_OFFSET.getY());
/* 34 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 35 */     for (int z = currentChunkPos.getMinBlockZ(); z <= currentChunkPos.getMaxBlockZ(); z++) {
/* 36 */       for (int x = currentChunkPos.getMinBlockX(); x <= currentChunkPos.getMaxBlockX(); x++) {
/* 37 */         if (checkerboardDistance(platformOrigin.getX(), platformOrigin.getZ(), x, z) <= 16) {
/*    */ 
/*    */           
/* 40 */           blockPos.set(x, platformOrigin.getY(), z);
/* 41 */           if (blockPos.equals(platformOrigin)) {
/* 42 */             level.setBlock((BlockPos)blockPos, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*    */           } else {
/* 44 */             level.setBlock((BlockPos)blockPos, Blocks.STONE.defaultBlockState(), 2);
/*    */           } 
/*    */         } 
/*    */       } 
/* 48 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/VoidStartPlatformFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */