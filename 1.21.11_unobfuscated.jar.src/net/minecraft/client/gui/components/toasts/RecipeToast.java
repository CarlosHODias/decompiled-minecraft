/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class RecipeToast
/*    */   implements Toast {
/* 18 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/recipe");
/*    */   private static final long DISPLAY_TIME = 5000L;
/* 20 */   private static final Component TITLE_TEXT = (Component)Component.translatable("recipe.toast.title");
/* 21 */   private static final Component DESCRIPTION_TEXT = (Component)Component.translatable("recipe.toast.description");
/*    */   
/* 23 */   private final List<Entry> recipeItems = new ArrayList<>();
/*    */   private long lastChanged;
/*    */   private boolean changed;
/* 26 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*    */ 
/*    */   
/*    */   private int displayedRecipeIndex;
/*    */ 
/*    */ 
/*    */   
/*    */   public Toast.Visibility getWantedVisibility() {
/* 34 */     return this.wantedVisibility;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(ToastManager manager, long fullyVisibleForMs) {
/* 39 */     if (this.changed) {
/* 40 */       this.lastChanged = fullyVisibleForMs;
/* 41 */       this.changed = false;
/*    */     } 
/* 43 */     if (this.recipeItems.isEmpty()) {
/* 44 */       this.wantedVisibility = Toast.Visibility.HIDE;
/*    */     } else {
/* 46 */       this.wantedVisibility = ((fullyVisibleForMs - this.lastChanged) >= 5000.0D * manager.getNotificationDisplayTimeMultiplier()) ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
/*    */     } 
/* 48 */     this.displayedRecipeIndex = (int)(fullyVisibleForMs / Math.max(1.0D, 5000.0D * manager.getNotificationDisplayTimeMultiplier() / this.recipeItems.size()) % this.recipeItems.size());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
/* 53 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());
/*    */     
/* 55 */     graphics.drawString(font, TITLE_TEXT, 30, 7, -11534256, false);
/* 56 */     graphics.drawString(font, DESCRIPTION_TEXT, 30, 18, -16777216, false);
/*    */     
/* 58 */     Entry items = this.recipeItems.get(this.displayedRecipeIndex);
/*    */     
/* 60 */     graphics.pose().pushMatrix();
/* 61 */     graphics.pose().scale(0.6F, 0.6F);
/* 62 */     graphics.renderFakeItem(items.categoryItem(), 3, 3);
/* 63 */     graphics.pose().popMatrix();
/*    */     
/* 65 */     graphics.renderFakeItem(items.unlockedItem(), 8, 8);
/*    */   }
/*    */   
/*    */   private void addItem(ItemStack craftingStation, ItemStack unlockedItem) {
/* 69 */     this.recipeItems.add(new Entry(craftingStation, unlockedItem));
/* 70 */     this.changed = true;
/*    */   }
/*    */   
/*    */   public static void addOrUpdate(ToastManager toastManager, RecipeDisplay recipe) {
/* 74 */     RecipeToast toast = toastManager.<RecipeToast>getToast(RecipeToast.class, NO_TOKEN);
/*    */     
/* 76 */     if (toast == null) {
/* 77 */       toast = new RecipeToast();
/* 78 */       toastManager.addToast(toast);
/*    */     } 
/*    */     
/* 81 */     ContextMap context = SlotDisplayContext.fromLevel((Level)(toastManager.getMinecraft()).level);
/*    */     
/* 83 */     ItemStack categoryItem = recipe.craftingStation().resolveForFirstStack(context);
/* 84 */     ItemStack unlockedItem = recipe.result().resolveForFirstStack(context);
/*    */     
/* 86 */     toast.addItem(categoryItem, unlockedItem);
/*    */   }
/*    */   private static final class Entry extends Record { private final ItemStack categoryItem; private final ItemStack unlockedItem;
/* 89 */     private Entry(ItemStack categoryItem, ItemStack unlockedItem) { this.categoryItem = categoryItem; this.unlockedItem = unlockedItem; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 89 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry; } public ItemStack categoryItem() { return this.categoryItem; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #89	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;
/* 89 */       //   0	8	1	o	Ljava/lang/Object; } public ItemStack unlockedItem() { return this.unlockedItem; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/RecipeToast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */