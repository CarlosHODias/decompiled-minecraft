/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class LingeringPotionItem extends ThrowablePotionItem {
/*    */   public LingeringPotionItem(Item.Properties properties) {
/* 17 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 22 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 23 */     return super.use(level, player, hand);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(ServerLevel level, LivingEntity owner, ItemStack itemStack) {
/* 28 */     return (AbstractThrownPotion)new ThrownLingeringPotion((Level)level, owner, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(Level level, Position position, ItemStack itemStack) {
/* 33 */     return (AbstractThrownPotion)new ThrownLingeringPotion(level, position.x(), position.y(), position.z(), itemStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/LingeringPotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */