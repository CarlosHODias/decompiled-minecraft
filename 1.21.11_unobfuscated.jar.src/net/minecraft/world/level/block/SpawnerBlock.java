/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SpawnerBlock extends BaseEntityBlock {
/* 16 */   public static final MapCodec<SpawnerBlock> CODEC = simpleCodec(SpawnerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SpawnerBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SpawnerBlock(BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 29 */     return (BlockEntity)new SpawnerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 34 */     return createTickerHelper(type, BlockEntityType.MOB_SPAWNER, level.isClientSide() ? SpawnerBlockEntity::clientTick : SpawnerBlockEntity::serverTick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 39 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/*    */     
/* 41 */     if (dropExperience) {
/* 42 */       int magicCount = 15 + level.random.nextInt(15) + level.random.nextInt(15);
/* 43 */       popExperience(level, pos, magicCount);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SpawnerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */