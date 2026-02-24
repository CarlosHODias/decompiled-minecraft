/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ConcretePowderBlock extends FallingBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("concrete").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ConcretePowderBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ConcretePowderBlock> CODEC;
/*    */   private final Block concrete;
/*    */   
/*    */   public MapCodec<ConcretePowderBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ConcretePowderBlock(Block concrete, BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/* 33 */     this.concrete = concrete;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, net.minecraft.world.entity.item.FallingBlockEntity entity) {
/* 38 */     if (shouldSolidify((BlockGetter)level, pos, replacedBlock)) {
/* 39 */       level.setBlock(pos, this.concrete.defaultBlockState(), 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     Level level = context.getLevel();
/* 46 */     BlockPos pos = context.getClickedPos();
/* 47 */     BlockState replacedBlock = level.getBlockState(pos);
/*    */     
/* 49 */     if (shouldSolidify((BlockGetter)level, pos, replacedBlock)) {
/* 50 */       return this.concrete.defaultBlockState();
/*    */     }
/* 52 */     return super.getStateForPlacement(context);
/*    */   }
/*    */   
/*    */   private static boolean shouldSolidify(BlockGetter level, BlockPos pos, BlockState replacedBlock) {
/* 56 */     return (canSolidify(replacedBlock) || touchesLiquid(level, pos));
/*    */   }
/*    */   
/*    */   private static boolean touchesLiquid(BlockGetter level, BlockPos pos) {
/*    */     boolean touchesLiquid = false;
/* 61 */     BlockPos.MutableBlockPos testPos = pos.mutable();
/* 62 */     for (Direction direction : Direction.values()) {
/* 63 */       BlockState blockState = level.getBlockState((BlockPos)testPos);
/* 64 */       if (direction != Direction.DOWN || canSolidify(blockState)) {
/*    */ 
/*    */         
/* 67 */         testPos.setWithOffset((net.minecraft.core.Vec3i)pos, direction);
/* 68 */         blockState = level.getBlockState((BlockPos)testPos);
/* 69 */         if (canSolidify(blockState) && !blockState.isFaceSturdy(level, pos, direction.getOpposite())) {
/* 70 */           touchesLiquid = true; break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 74 */     return touchesLiquid;
/*    */   }
/*    */   
/*    */   private static boolean canSolidify(BlockState state) {
/* 78 */     return state.getFluidState().is(net.minecraft.tags.FluidTags.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 83 */     if (touchesLiquid((BlockGetter)level, pos)) {
/* 84 */       return this.concrete.defaultBlockState();
/*    */     }
/*    */     
/* 87 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
/* 92 */     return (blockState.getMapColor(level, pos)).col;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ConcretePowderBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */