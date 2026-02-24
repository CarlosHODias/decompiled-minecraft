/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*    */ 
/*    */ public class DiskFeature extends Feature<DiskConfiguration> {
/*    */   public DiskFeature(Codec<DiskConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<DiskConfiguration> context) {
/* 18 */     DiskConfiguration config = context.config();
/* 19 */     BlockPos origin = context.origin();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     RandomSource random = context.random();
/*    */     
/*    */     boolean placedAny = false;
/* 24 */     int originY = origin.getY();
/* 25 */     int top = originY + config.halfHeight();
/* 26 */     int bottom = originY - config.halfHeight() - 1;
/*    */     
/* 28 */     int r = config.radius().sample(random);
/*    */     
/* 30 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*    */     
/* 32 */     for (BlockPos columnPos : (Iterable<BlockPos>)BlockPos.betweenClosed(origin.offset(-r, 0, -r), origin.offset(r, 0, r))) {
/* 33 */       int xd = columnPos.getX() - origin.getX();
/* 34 */       int zd = columnPos.getZ() - origin.getZ();
/* 35 */       if (xd * xd + zd * zd > r * r) {
/*    */         continue;
/*    */       }
/*    */       
/* 39 */       placedAny |= placeColumn(config, level, random, top, bottom, mutablePos.set((Vec3i)columnPos));
/*    */     } 
/*    */     
/* 42 */     return placedAny;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeColumn(DiskConfiguration config, WorldGenLevel level, RandomSource random, int top, int bottom, BlockPos.MutableBlockPos pos) {
/*    */     boolean placedAny = false;
/*    */     boolean placedAbove = false;
/* 49 */     for (int y = top; y > bottom; y--) {
/* 50 */       pos.setY(y);
/* 51 */       if (config.target().test(level, pos)) {
/* 52 */         BlockState state = config.stateProvider().getState(level, random, (BlockPos)pos);
/* 53 */         level.setBlock((BlockPos)pos, state, 2);
/* 54 */         if (!placedAbove) {
/* 55 */           markAboveForPostProcessing(level, (BlockPos)pos);
/*    */         }
/* 57 */         placedAny = true;
/* 58 */         placedAbove = true;
/*    */       } else {
/* 60 */         placedAbove = false;
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return placedAny;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/DiskFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */