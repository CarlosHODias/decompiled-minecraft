/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GlowLichenBlock extends MultifaceSpreadeableBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<GlowLichenBlock> CODEC = simpleCodec(GlowLichenBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<GlowLichenBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private final MultifaceSpreader spreader = new MultifaceSpreader(this);
/*    */   
/*    */   public GlowLichenBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/*    */   }
/*    */   
/*    */   public static ToIntFunction<BlockState> emission(int lightEmission) {
/* 29 */     return state -> MultifaceBlock.hasAnyFace(state) ? lightEmission : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 34 */     return Direction.stream().anyMatch(face -> this.spreader.canSpreadInAnyDirection(state, (BlockGetter)state, level, pos.getOpposite()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 44 */     this.spreader.spreadFromRandomFaceTowardRandomDirection(state, (net.minecraft.world.level.LevelAccessor)level, pos, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 49 */     return state.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public MultifaceSpreader getSpreader() {
/* 54 */     return this.spreader;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/GlowLichenBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */