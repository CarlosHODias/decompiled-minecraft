/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AlterGroundDecorator extends TreeDecorator {
/*    */   public static final com.mojang.serialization.MapCodec<AlterGroundDecorator> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, d -> d.provider);
/*    */   }
/*    */   private final BlockStateProvider provider;
/*    */   
/*    */   public AlterGroundDecorator(BlockStateProvider provider) {
/* 17 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 22 */     return TreeDecoratorType.ALTER_GROUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 27 */     List<BlockPos> blockPositions = net.minecraft.world.level.levelgen.feature.TreeFeature.getLowestTrunkOrRootOfTree(context);
/*    */     
/* 29 */     if (blockPositions.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     int minY = ((BlockPos)blockPositions.get(0)).getY();
/* 34 */     blockPositions.stream().filter(pos -> (pos.getY() == minY)).forEach(pos -> {
/*    */           placeCircle(context, context.west().north());
/*    */           placeCircle(context, context.east(2).north());
/*    */           placeCircle(context, context.west().south(2));
/*    */           placeCircle(context, context.east(2).south(2));
/*    */           for (int i = 0; i < 5; i++) {
/*    */             int placement = context.random().nextInt(64), xx = placement % 8, zz = placement / 8;
/*    */             if (xx == 0 || xx == 7 || zz == 0 || zz == 7) {
/*    */               placeCircle(context, context.offset(-3 + xx, 0, -3 + zz));
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void placeCircle(TreeDecorator.Context context, BlockPos pos) {
/* 52 */     for (int xx = -2; xx <= 2; xx++) {
/* 53 */       for (int zz = -2; zz <= 2; zz++) {
/* 54 */         if (Math.abs(xx) != 2 || Math.abs(zz) != 2) {
/* 55 */           placeBlockAt(context, pos.offset(xx, 0, zz));
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void placeBlockAt(TreeDecorator.Context context, BlockPos pos) {
/* 62 */     for (int dy = 2; dy >= -3; dy--) {
/* 63 */       BlockPos blockPos = pos.above(dy);
/* 64 */       if (Feature.isGrassOrDirt(context.level(), blockPos)) {
/* 65 */         context.setBlock(blockPos, this.provider.getState(context.random(), pos)); break;
/*    */       } 
/* 67 */       if (!context.isAir(blockPos) && dy < 0)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/AlterGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */