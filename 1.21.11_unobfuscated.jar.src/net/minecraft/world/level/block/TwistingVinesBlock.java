/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVinesBlock extends GrowingPlantHeadBlock {
/* 10 */   public static final MapCodec<TwistingVinesBlock> CODEC = simpleCodec(TwistingVinesBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TwistingVinesBlock> codec() {
/* 14 */     return CODEC;
/*    */   }
/*    */   
/* 17 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 15.0D);
/*    */   
/*    */   public TwistingVinesBlock(BlockBehaviour.Properties properties) {
/* 20 */     super(properties, net.minecraft.core.Direction.UP, SHAPE, false, 0.1D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
/* 25 */     return NetherVines.getBlocksToGrowWhenBonemealed(random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 30 */     return Blocks.TWISTING_VINES_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState state) {
/* 35 */     return NetherVines.isValidGrowthState(state);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TwistingVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */