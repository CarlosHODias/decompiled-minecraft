/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.LoomMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class LoomBlock extends HorizontalDirectionalBlock {
/* 20 */   public static final MapCodec<LoomBlock> CODEC = simpleCodec(LoomBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<LoomBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.loom");
/*    */   
/*    */   protected LoomBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 35 */     if (!level.isClientSide()) {
/* 36 */       player.openMenu(state.getMenuProvider(level, pos));
/* 37 */       player.awardStat(net.minecraft.stats.Stats.INTERACT_WITH_LOOM);
/*    */     } 
/* 39 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 44 */     return (MenuProvider)new net.minecraft.world.SimpleMenuProvider((containerId, inventory, player) -> new LoomMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 49 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 54 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LoomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */