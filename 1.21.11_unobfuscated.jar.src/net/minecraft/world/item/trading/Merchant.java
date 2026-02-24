/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MerchantMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Merchant
/*    */ {
/*    */   void setTradingPlayer(Player paramPlayer);
/*    */   
/*    */   Player getTradingPlayer();
/*    */   
/*    */   MerchantOffers getOffers();
/*    */   
/*    */   void overrideOffers(MerchantOffers paramMerchantOffers);
/*    */   
/*    */   void notifyTrade(MerchantOffer paramMerchantOffer);
/*    */   
/*    */   default boolean canRestock() {
/* 35 */     return false;
/*    */   } void notifyTradeUpdated(ItemStack paramItemStack); int getVillagerXp(); void overrideXp(int paramInt); boolean showProgressBar();
/*    */   SoundEvent getNotifyTradeSound();
/*    */   default void openTradingScreen(Player player, Component title, int level) {
/* 39 */     OptionalInt containerId = player.openMenu((MenuProvider)new SimpleMenuProvider((id, inventory, p) -> new MerchantMenu(id, inventory, this), title));
/*    */     
/* 41 */     if (containerId.isPresent()) {
/* 42 */       MerchantOffers offers = getOffers();
/* 43 */       if (!offers.isEmpty())
/* 44 */         player.sendMerchantOffers(containerId.getAsInt(), offers, level, getVillagerXp(), showProgressBar(), canRestock()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   boolean isClientSide();
/*    */   
/*    */   boolean stillValid(Player paramPlayer);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/trading/Merchant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */