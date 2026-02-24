/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.SmithingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SmithingTableBlock extends CraftingTableBlock {
/* 18 */   public static final MapCodec<SmithingTableBlock> CODEC = simpleCodec(SmithingTableBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SmithingTableBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SmithingTableBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/*    */   }
/*    */   
/* 29 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.upgrade");
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 33 */     return (MenuProvider)new net.minecraft.world.SimpleMenuProvider((containerId, inventory, player) -> new SmithingMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 38 */     if (!level.isClientSide()) {
/* 39 */       player.openMenu(state.getMenuProvider(level, pos));
/* 40 */       player.awardStat(net.minecraft.stats.Stats.INTERACT_WITH_SMITHING_TABLE);
/*    */     } 
/* 42 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SmithingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */