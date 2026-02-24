/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SculkBlock extends DropExperienceBlock implements SculkBehaviour {
/* 15 */   public static final MapCodec<SculkBlock> CODEC = simpleCodec(SculkBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SculkBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SculkBlock(BlockBehaviour.Properties properties) {
/* 23 */     super((net.minecraft.util.valueproviders.IntProvider)net.minecraft.util.valueproviders.ConstantInt.of(1), properties);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVein) {
/* 29 */     int charge = cursor.getCharge();
/* 30 */     if (charge == 0 || random.nextInt(spreader.chargeDecayRate()) != 0) {
/* 31 */       return charge;
/*    */     }
/*    */     
/* 34 */     BlockPos chargePos = cursor.getPos();
/* 35 */     boolean isCloseToCatalyst = chargePos.closerThan((Vec3i)originPos, spreader.noGrowthRadius());
/* 36 */     if (isCloseToCatalyst || !canPlaceGrowth(level, chargePos)) {
/* 37 */       if (random.nextInt(spreader.additionalDecayRate()) != 0) {
/* 38 */         return charge;
/*    */       }
/* 40 */       return charge - (isCloseToCatalyst ? 1 : getDecayPenalty(spreader, chargePos, originPos, charge));
/*    */     } 
/* 42 */     int xpPerGrowthSpawn = spreader.growthSpawnCost();
/* 43 */     if (random.nextInt(xpPerGrowthSpawn) < charge) {
/* 44 */       BlockPos growthPlacement = chargePos.above();
/* 45 */       BlockState growthState = getRandomGrowthState(level, growthPlacement, random, spreader.isWorldGeneration());
/* 46 */       level.setBlock(growthPlacement, growthState, 3);
/* 47 */       level.playSound(null, chargePos, growthState.getSoundType().getPlaceSound(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */     } 
/* 49 */     return Math.max(0, charge - xpPerGrowthSpawn);
/*    */   }
/*    */   
/*    */   private static int getDecayPenalty(SculkSpreader spreader, BlockPos pos, BlockPos originPos, int charge) {
/* 53 */     int noGrowthRadius = spreader.noGrowthRadius();
/* 54 */     float outerDistanceSquared = Mth.square((float)Math.sqrt(pos.distSqr((Vec3i)originPos)) - noGrowthRadius);
/* 55 */     int maxReachSquared = Mth.square(24 - noGrowthRadius);
/*    */ 
/*    */     
/* 58 */     float distanceFactor = Math.min(1.0F, outerDistanceSquared / maxReachSquared);
/* 59 */     return Math.max(1, (int)(charge * distanceFactor * 0.5F));
/*    */   }
/*    */   
/*    */   private BlockState getRandomGrowthState(LevelAccessor level, BlockPos pos, RandomSource random, boolean isWorldGen) {
/*    */     BlockState state;
/* 64 */     if (random.nextInt(11) == 0) {
/* 65 */       state = (BlockState)Blocks.SCULK_SHRIEKER.defaultBlockState().setValue((Property)SculkShriekerBlock.CAN_SUMMON, isWorldGen);
/*    */     } else {
/* 67 */       state = Blocks.SCULK_SENSOR.defaultBlockState();
/*    */     } 
/*    */     
/* 70 */     if (state.hasProperty((Property)BlockStateProperties.WATERLOGGED) && !level.getFluidState(pos).isEmpty()) {
/* 71 */       return (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, true);
/*    */     }
/* 73 */     return state;
/*    */   }
/*    */   
/*    */   private static boolean canPlaceGrowth(LevelAccessor level, BlockPos pos) {
/* 77 */     BlockState stateAbove = level.getBlockState(pos.above());
/* 78 */     if (!stateAbove.isAir() && (!stateAbove.is(Blocks.WATER) || !stateAbove.getFluidState().is((net.minecraft.world.level.material.Fluid)net.minecraft.world.level.material.Fluids.WATER))) {
/* 79 */       return false;
/*    */     }
/*    */     
/* 82 */     int growthCount = 0;
/* 83 */     for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 2, 4))) {
/* 84 */       BlockState state = level.getBlockState(blockPos);
/* 85 */       if (state.is(Blocks.SCULK_SENSOR) || state.is(Blocks.SCULK_SHRIEKER)) {
/* 86 */         growthCount++;
/*    */       }
/* 88 */       if (growthCount > 2) {
/* 89 */         return false;
/*    */       }
/*    */     } 
/* 92 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canChangeBlockStateOnSpread() {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */