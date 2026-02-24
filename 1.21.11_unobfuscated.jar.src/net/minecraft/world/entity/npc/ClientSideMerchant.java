/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ 
/*    */ public class ClientSideMerchant
/*    */   implements Merchant {
/*    */   private final Player source;
/* 14 */   private MerchantOffers offers = new MerchantOffers();
/*    */   private int xp;
/*    */   
/*    */   public ClientSideMerchant(Player source) {
/* 18 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Player getTradingPlayer() {
/* 23 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTradingPlayer(Player player) {}
/*    */ 
/*    */   
/*    */   public MerchantOffers getOffers() {
/* 32 */     return this.offers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void overrideOffers(MerchantOffers offers) {
/* 37 */     this.offers = offers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notifyTrade(MerchantOffer offer) {
/* 42 */     offer.increaseUses();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void notifyTradeUpdated(ItemStack itemStack) {}
/*    */ 
/*    */   
/*    */   public boolean isClientSide() {
/* 51 */     return this.source.level().isClientSide();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player player) {
/* 56 */     return (this.source == player);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVillagerXp() {
/* 61 */     return this.xp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void overrideXp(int xp) {
/* 66 */     this.xp = xp;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean showProgressBar() {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getNotifyTradeSound() {
/* 76 */     return SoundEvents.VILLAGER_YES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/npc/ClientSideMerchant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */