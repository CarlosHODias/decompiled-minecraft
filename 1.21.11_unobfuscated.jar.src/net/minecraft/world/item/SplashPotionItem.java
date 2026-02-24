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
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SplashPotionItem extends ThrowablePotionItem {
/*    */   public SplashPotionItem(Item.Properties properties) {
/* 17 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 22 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 23 */     return super.use(level, player, hand);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(ServerLevel level, LivingEntity owner, ItemStack itemStack) {
/* 28 */     return (AbstractThrownPotion)new ThrownSplashPotion((Level)level, owner, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(Level level, Position position, ItemStack itemStack) {
/* 33 */     return (AbstractThrownPotion)new ThrownSplashPotion(level, position.x(), position.y(), position.z(), itemStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/SplashPotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */