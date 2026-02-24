/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherSproutsBlock extends VegetationBlock {
/* 12 */   public static final MapCodec<NetherSproutsBlock> CODEC = simpleCodec(NetherSproutsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<NetherSproutsBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/* 19 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 3.0D);
/*    */   
/*    */   public NetherSproutsBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 27 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 32 */     return (state.is(net.minecraft.tags.BlockTags.NYLIUM) || state.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(state, level, pos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/NetherSproutsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */