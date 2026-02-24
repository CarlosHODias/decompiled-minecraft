/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AirBlock extends Block {
/* 12 */   public static final MapCodec<AirBlock> CODEC = simpleCodec(AirBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<AirBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public AirBlock(BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderShape getRenderShape(BlockState state) {
/* 25 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
/* 30 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AirBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */