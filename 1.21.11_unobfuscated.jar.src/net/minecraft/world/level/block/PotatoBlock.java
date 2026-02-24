/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class PotatoBlock extends CropBlock {
/* 13 */   public static final MapCodec<PotatoBlock> CODEC = simpleCodec(PotatoBlock::new);
/*    */   private static final VoxelShape[] SHAPES;
/*    */   
/*    */   public MapCodec<PotatoBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   static {
/* 20 */     SHAPES = Block.boxes(7, age -> Block.column(16.0D, 0.0D, (2 + age)));
/*    */   }
/*    */   public PotatoBlock(BlockBehaviour.Properties properties) {
/* 23 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 28 */     return (ItemLike)Items.POTATO;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
/* 33 */     return SHAPES[getAge(state)];
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/PotatoBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */