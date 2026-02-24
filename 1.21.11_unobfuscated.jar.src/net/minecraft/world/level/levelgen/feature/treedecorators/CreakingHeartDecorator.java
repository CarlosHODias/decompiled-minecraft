/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CreakingHeartDecorator extends TreeDecorator {
/*    */   public static final com.mojang.serialization.MapCodec<CreakingHeartDecorator> CODEC;
/*    */   
/*    */   static {
/* 19 */     CODEC = com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CreakingHeartDecorator::new, d -> d.probability);
/*    */   }
/*    */   private final float probability;
/*    */   
/*    */   public CreakingHeartDecorator(float probability) {
/* 24 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 29 */     return TreeDecoratorType.CREAKING_HEART;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 34 */     RandomSource random = context.random();
/*    */     
/* 36 */     ObjectArrayList<BlockPos> objectArrayList = context.logs();
/* 37 */     if (objectArrayList.isEmpty()) {
/*    */       return;
/*    */     }
/* 40 */     if (random.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     List<BlockPos> heartPlacements = new java.util.ArrayList<>((java.util.Collection<? extends BlockPos>)objectArrayList);
/* 45 */     Util.shuffle(heartPlacements, random);
/* 46 */     Optional<BlockPos> targetPos = heartPlacements.stream()
/* 47 */       .filter(pos -> {
/*    */           for (Direction dir : Direction.values()) {
/*    */             if (!context.checkBlock(pos.relative(dir), ())) {
/*    */               return false;
/*    */             }
/*    */           } 
/*    */           
/*    */           return true;
/* 55 */         }).findFirst();
/* 56 */     if (targetPos.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 60 */     context.setBlock(targetPos.get(), (BlockState)((BlockState)Blocks.CREAKING_HEART.defaultBlockState().setValue((Property)CreakingHeartBlock.STATE, (Comparable)net.minecraft.world.level.block.state.properties.CreakingHeartState.DORMANT)).setValue((Property)CreakingHeartBlock.NATURAL, true));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/CreakingHeartDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */