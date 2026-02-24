/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
/* 19 */   public static final MapCodec<BeaconBlock> CODEC = simpleCodec(BeaconBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BeaconBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BeaconBlock(BlockBehaviour.Properties properties) {
/* 27 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 32 */     return DyeColor.WHITE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 37 */     return (BlockEntity)new BeaconBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 42 */     return createTickerHelper(type, BlockEntityType.BEACON, BeaconBlockEntity::tick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 47 */     if (!level.isClientSide()) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BeaconBlockEntity) { BeaconBlockEntity beacon = (BeaconBlockEntity)blockEntity;
/* 48 */         player.openMenu((MenuProvider)beacon);
/* 49 */         player.awardStat(Stats.INTERACT_WITH_BEACON); }
/*    */        }
/* 51 */      return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BeaconBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */