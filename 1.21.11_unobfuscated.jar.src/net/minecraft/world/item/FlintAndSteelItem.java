/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class FlintAndSteelItem extends Item {
/*    */   public FlintAndSteelItem(Item.Properties properties) {
/* 24 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 29 */     Player player = context.getPlayer();
/* 30 */     Level level = context.getLevel();
/* 31 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 33 */     BlockState state = level.getBlockState(pos);
/* 34 */     if (CampfireBlock.canLight(state) || net.minecraft.world.level.block.CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
/* 35 */       level.playSound((Entity)player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/* 36 */       level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.LIT, true), 11);
/* 37 */       level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_CHANGE, pos);
/* 38 */       if (player != null) {
/* 39 */         context.getItemInHand().hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
/*    */       }
/* 41 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 44 */     BlockPos relativePos = pos.relative(context.getClickedFace());
/* 45 */     if (BaseFireBlock.canBePlacedAt(level, relativePos, context.getHorizontalDirection())) {
/* 46 */       level.playSound((Entity)player, relativePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/*    */       
/* 48 */       BlockState fireState = BaseFireBlock.getState((BlockGetter)level, relativePos);
/* 49 */       level.setBlock(relativePos, fireState, 11);
/* 50 */       level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_PLACE, pos);
/*    */       
/* 52 */       ItemStack itemStack = context.getItemInHand();
/* 53 */       if (player instanceof net.minecraft.server.level.ServerPlayer) {
/* 54 */         CriteriaTriggers.PLACED_BLOCK.trigger((net.minecraft.server.level.ServerPlayer)player, relativePos, itemStack);
/* 55 */         itemStack.hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
/*    */       } 
/*    */       
/* 58 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 61 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/FlintAndSteelItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */