/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CaveVinesBlock extends GrowingPlantHeadBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesBlock> CODEC = simpleCodec(CaveVinesBlock::new);
/*    */   private static final float CHANCE_OF_BERRIES_ON_GROWTH = 0.11F;
/*    */   
/*    */   public MapCodec<CaveVinesBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CaveVinesBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties, net.minecraft.core.Direction.DOWN, SHAPE, false, 0.1D);
/* 30 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, 0)).setValue((Property)BERRIES, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
/* 35 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState state) {
/* 40 */     return state.isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 45 */     return Blocks.CAVE_VINES_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateBodyAfterConvertedFromHead(BlockState headState, BlockState bodyState) {
/* 50 */     return (BlockState)bodyState.setValue((Property)BERRIES, headState.getValue((Property)BERRIES));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getGrowIntoState(BlockState growFromState, RandomSource random) {
/* 55 */     return (BlockState)super.getGrowIntoState(growFromState, random).setValue((Property)BERRIES, (random.nextFloat() < 0.11F));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 60 */     return new ItemStack((net.minecraft.world.level.ItemLike)net.minecraft.world.item.Items.GLOW_BERRIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 65 */     return CaveVines.use((net.minecraft.world.entity.Entity)player, state, level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 70 */     super.createBlockStateDefinition(builder);
/* 71 */     builder.add(new Property[] { (Property)BERRIES });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 76 */     return !((Boolean)state.getValue((Property)BERRIES));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 86 */     level.setBlock(pos, (BlockState)state.setValue((Property)BERRIES, true), 2);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CaveVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */