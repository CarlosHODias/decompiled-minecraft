/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class TrialSpawnerBlock extends BaseEntityBlock {
/* 20 */   public static final MapCodec<TrialSpawnerBlock> CODEC = simpleCodec(TrialSpawnerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TrialSpawnerBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;
/* 28 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */   
/*    */   public TrialSpawnerBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/* 32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STATE, (Comparable)TrialSpawnerState.INACTIVE)).setValue((Property)OMINOUS, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 37 */     builder.add(new Property[] { (Property)STATE, (Property)OMINOUS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 42 */     return (BlockEntity)new TrialSpawnerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 47 */     ServerLevel serverLevel = (ServerLevel)level; return (level instanceof ServerLevel) ? 
/* 48 */       createTickerHelper(type, BlockEntityType.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickServer(serverLevel, pos, (Boolean)state.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(false))) : 
/* 49 */       createTickerHelper(type, BlockEntityType.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickClient(innerLevel, pos, (Boolean)state.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(false)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TrialSpawnerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */