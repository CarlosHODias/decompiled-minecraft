/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ 
/*    */ public class LeaveVineDecorator extends TreeDecorator {
/*    */   public static final MapCodec<LeaveVineDecorator> CODEC;
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 13 */     return TreeDecoratorType.LEAVE_VINE;
/*    */   } private final float probability;
/*    */   static {
/* 16 */     CODEC = com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveVineDecorator::new, d -> d.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   public LeaveVineDecorator(float probability) {
/* 21 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 26 */     RandomSource random = context.random();
/* 27 */     context.leaves().forEach(pos -> {
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos west = random.west();
/*    */             if (random.isAir(west)) {
/*    */               addHangingVine(west, VineBlock.EAST, random);
/*    */             }
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos east = random.east();
/*    */             if (random.isAir(east)) {
/*    */               addHangingVine(east, VineBlock.WEST, random);
/*    */             }
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos north = random.north();
/*    */             if (random.isAir(north)) {
/*    */               addHangingVine(north, VineBlock.SOUTH, random);
/*    */             }
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos south = random.south();
/*    */             if (random.isAir(south)) {
/*    */               addHangingVine(south, VineBlock.NORTH, random);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void addHangingVine(BlockPos pos, BooleanProperty direction, TreeDecorator.Context context) {
/* 59 */     context.placeVine(pos, direction);
/* 60 */     int maxDir = 4;
/*    */     
/* 62 */     pos = pos.below();
/* 63 */     while (context.isAir(pos) && maxDir > 0) {
/* 64 */       context.placeVine(pos, direction);
/* 65 */       pos = pos.below();
/* 66 */       maxDir--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */