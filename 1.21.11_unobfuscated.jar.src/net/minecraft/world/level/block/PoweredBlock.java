/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PoweredBlock extends Block {
/* 11 */   public static final MapCodec<PoweredBlock> CODEC = simpleCodec(PoweredBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<PoweredBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public PoweredBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSignalSource(BlockState state) {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 29 */     return 15;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/PoweredBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */