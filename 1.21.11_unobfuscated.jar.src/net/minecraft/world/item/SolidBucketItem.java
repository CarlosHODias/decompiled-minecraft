/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SolidBucketItem
/*    */   extends BlockItem implements DispensibleContainerItem {
/*    */   public SolidBucketItem(Block content, SoundEvent placeSound, Item.Properties properties) {
/* 21 */     super(content, properties);
/* 22 */     this.placeSound = placeSound;
/*    */   }
/*    */   private final SoundEvent placeSound;
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 27 */     InteractionResult placeResult = super.useOn(context);
/* 28 */     Player player = context.getPlayer();
/*    */     
/* 30 */     if (placeResult.consumesAction() && player != null) {
/* 31 */       player.setItemInHand(context.getHand(), BucketItem.getEmptySuccessItem(context.getItemInHand(), player));
/*    */     }
/*    */     
/* 34 */     return placeResult;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getPlaceSound(BlockState blockState) {
/* 39 */     return this.placeSound;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean emptyContents(LivingEntity user, Level level, BlockPos pos, BlockHitResult hitResult) {
/* 44 */     if (level.isInWorldBounds(pos) && level.isEmptyBlock(pos)) {
/* 45 */       if (!level.isClientSide()) {
/* 46 */         level.setBlock(pos, getBlock().defaultBlockState(), 3);
/*    */       }
/* 48 */       level.gameEvent((Entity)user, (Holder)GameEvent.FLUID_PLACE, pos);
/* 49 */       level.playSound((Entity)user, pos, this.placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/SolidBucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */