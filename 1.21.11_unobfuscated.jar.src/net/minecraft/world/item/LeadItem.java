/*    */ package net.minecraft.world.item;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Leashable;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class LeadItem extends Item {
/*    */   public LeadItem(Item.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 24 */     Level level = context.getLevel();
/* 25 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 27 */     BlockState state = level.getBlockState(pos);
/* 28 */     if (state.is(BlockTags.FENCES)) {
/* 29 */       Player player = context.getPlayer();
/* 30 */       if (!level.isClientSide() && player != null) {
/* 31 */         return bindPlayerMobs(player, level, pos);
/*    */       }
/*    */     } 
/*    */     
/* 35 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static InteractionResult bindPlayerMobs(Player player, Level level, BlockPos pos) {
/* 39 */     LeashFenceKnotEntity activeKnot = null;
/*    */     
/* 41 */     List<Leashable> entitiesToLeash = Leashable.leashableInArea(level, net.minecraft.world.phys.Vec3.atCenterOf((Vec3i)pos), l -> (l.getLeashHolder() == player));
/*    */     boolean anyLeashed = false;
/* 43 */     for (Leashable leashable : entitiesToLeash) {
/* 44 */       if (activeKnot == null) {
/* 45 */         activeKnot = LeashFenceKnotEntity.getOrCreateKnot(level, pos);
/* 46 */         activeKnot.playPlacementSound();
/*    */       } 
/* 48 */       if (leashable.canHaveALeashAttachedTo((Entity)activeKnot)) {
/* 49 */         leashable.setLeashedTo((Entity)activeKnot, true);
/* 50 */         anyLeashed = true;
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (anyLeashed) {
/* 55 */       level.gameEvent((Holder)GameEvent.BLOCK_ATTACH, pos, GameEvent.Context.of((Entity)player));
/* 56 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*    */     } 
/*    */     
/* 59 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/LeadItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */