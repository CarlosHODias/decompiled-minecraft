/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TransparentBlock extends HalfTransparentBlock {
/* 12 */   public static final MapCodec<TransparentBlock> CODEC = simpleCodec(TransparentBlock::new);
/*    */   protected TransparentBlock(BlockBehaviour.Properties properties) {
/* 14 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends TransparentBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 24 */     return Shapes.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/* 29 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */