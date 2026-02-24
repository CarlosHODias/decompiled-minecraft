/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HalfTransparentBlock extends Block {
/*  8 */   public static final MapCodec<HalfTransparentBlock> CODEC = simpleCodec(HalfTransparentBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends HalfTransparentBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected HalfTransparentBlock(BlockBehaviour.Properties properties) {
/* 16 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/* 21 */     if (neighborState.is(this)) {
/* 22 */       return true;
/*    */     }
/* 24 */     return super.skipRendering(state, neighborState, direction);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/HalfTransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */