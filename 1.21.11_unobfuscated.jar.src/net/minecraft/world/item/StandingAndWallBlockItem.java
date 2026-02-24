/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ 
/*    */ public class StandingAndWallBlockItem
/*    */   extends BlockItem {
/*    */   protected final Block wallBlock;
/*    */   private final Direction attachmentDirection;
/*    */   
/*    */   public StandingAndWallBlockItem(Block block, Block wallBlock, Direction attachmentDirection, Item.Properties properties) {
/* 19 */     super(block, properties);
/* 20 */     this.wallBlock = wallBlock;
/* 21 */     this.attachmentDirection = attachmentDirection;
/*    */   }
/*    */   
/*    */   protected boolean canPlace(LevelReader level, BlockState possibleState, BlockPos pos) {
/* 25 */     return possibleState.canSurvive(level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getPlacementState(BlockPlaceContext context) {
/* 30 */     BlockState wallState = this.wallBlock.getStateForPlacement(context);
/*    */     
/* 32 */     BlockState stateForPlacement = null;
/*    */     
/* 34 */     Level level = context.getLevel();
/* 35 */     BlockPos pos = context.getClickedPos();
/* 36 */     for (Direction direction : context.getNearestLookingDirections()) {
/* 37 */       if (direction != this.attachmentDirection.getOpposite()) {
/*    */ 
/*    */ 
/*    */         
/* 41 */         BlockState possibleState = (direction == this.attachmentDirection) ? getBlock().getStateForPlacement(context) : wallState;
/* 42 */         if (possibleState != null && canPlace((LevelReader)level, possibleState, pos)) {
/* 43 */           stateForPlacement = possibleState;
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 48 */     return (stateForPlacement != null && level.isUnobstructed(stateForPlacement, pos, CollisionContext.empty())) ? stateForPlacement : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerBlocks(Map<Block, Item> map, Item item) {
/* 53 */     super.registerBlocks(map, item);
/*    */     
/* 55 */     map.put(this.wallBlock, item);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/StandingAndWallBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */