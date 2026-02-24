/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ 
/*    */ public class TrunkVineDecorator
/*    */   extends TreeDecorator {
/*    */   protected TreeDecoratorType<?> type() {
/* 11 */     return TreeDecoratorType.TRUNK_VINE;
/*    */   }
/*    */   
/* 14 */   public static final MapCodec<TrunkVineDecorator> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 16 */   public static final TrunkVineDecorator INSTANCE = new TrunkVineDecorator();
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 20 */     RandomSource random = context.random();
/* 21 */     context.logs().forEach(pos -> {
/*    */           if (random.nextInt(3) > 0) {
/*    */             BlockPos west = pos.west();
/*    */             if (context.isAir(west))
/*    */               context.placeVine(west, VineBlock.EAST); 
/*    */           } 
/*    */           if (random.nextInt(3) > 0) {
/*    */             BlockPos east = pos.east();
/*    */             if (context.isAir(east))
/*    */               context.placeVine(east, VineBlock.WEST); 
/*    */           } 
/*    */           if (random.nextInt(3) > 0) {
/*    */             BlockPos north = pos.north();
/*    */             if (context.isAir(north))
/*    */               context.placeVine(north, VineBlock.SOUTH); 
/*    */           } 
/*    */           if (random.nextInt(3) > 0) {
/*    */             BlockPos south = pos.south();
/*    */             if (context.isAir(south))
/*    */               context.placeVine(south, VineBlock.NORTH); 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/TrunkVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */