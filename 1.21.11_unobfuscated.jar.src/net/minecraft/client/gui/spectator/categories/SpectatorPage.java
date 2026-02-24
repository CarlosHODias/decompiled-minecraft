/*    */ package net.minecraft.client.gui.spectator.categories;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*    */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*    */ 
/*    */ 
/*    */ public class SpectatorPage
/*    */ {
/*    */   public static final int NO_SELECTION = -1;
/*    */   private final List<SpectatorMenuItem> items;
/*    */   private final int selection;
/*    */   
/*    */   public SpectatorPage(List<SpectatorMenuItem> items, int selection) {
/* 16 */     this.items = items;
/* 17 */     this.selection = selection;
/*    */   }
/*    */   
/*    */   public SpectatorMenuItem getItem(int slot) {
/* 21 */     if (slot < 0 || slot >= this.items.size()) {
/* 22 */       return SpectatorMenu.EMPTY_SLOT;
/*    */     }
/*    */     
/* 25 */     return (SpectatorMenuItem)MoreObjects.firstNonNull(this.items.get(slot), SpectatorMenu.EMPTY_SLOT);
/*    */   }
/*    */   
/*    */   public int getSelectedSlot() {
/* 29 */     return this.selection;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/categories/SpectatorPage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */