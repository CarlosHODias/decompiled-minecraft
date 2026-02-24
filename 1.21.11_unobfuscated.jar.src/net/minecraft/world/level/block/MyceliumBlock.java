/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MyceliumBlock extends SpreadingSnowyDirtBlock {
/* 11 */   public static final MapCodec<MyceliumBlock> CODEC = simpleCodec(MyceliumBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<MyceliumBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MyceliumBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 24 */     super.animateTick(state, level, pos, random);
/* 25 */     if (random.nextInt(10) == 0)
/* 26 */       level.addParticle((net.minecraft.core.particles.ParticleOptions)net.minecraft.core.particles.ParticleTypes.MYCELIUM, pos.getX() + random.nextDouble(), pos.getY() + 1.1D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MyceliumBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */