/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class CoralBlock extends Block {
/* 20 */   public static final MapCodec<Block> DEAD_CORAL_FIELD = net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("dead"); public static final MapCodec<CoralBlock> CODEC; private final Block deadBlock;
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CoralBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CoralBlock(Block deadBlock, BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/* 31 */     this.deadBlock = deadBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<CoralBlock> codec() {
/* 36 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 41 */     if (!scanForWater((BlockGetter)level, pos)) {
/* 42 */       level.setBlock(pos, this.deadBlock.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 48 */     if (!scanForWater((BlockGetter)level, pos)) {
/* 49 */       ticks.scheduleTick(pos, this, 60 + random.nextInt(40));
/*    */     }
/* 51 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected boolean scanForWater(BlockGetter level, BlockPos blockPos) {
/* 55 */     for (Direction direction : Direction.values()) {
/* 56 */       FluidState fluidState = level.getFluidState(blockPos.relative(direction));
/* 57 */       if (fluidState.is(net.minecraft.tags.FluidTags.WATER)) {
/* 58 */         return true;
/*    */       }
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 66 */     if (!scanForWater((BlockGetter)context.getLevel(), context.getClickedPos())) {
/* 67 */       context.getLevel().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
/*    */     }
/* 69 */     return defaultBlockState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CoralBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */