/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ 
/*    */ public class GhostSlots
/*    */ {
/* 18 */   private final Reference2ObjectMap<Slot, GhostSlot> ingredients = (Reference2ObjectMap<Slot, GhostSlot>)new Reference2ObjectArrayMap();
/*    */   private final SlotSelectTime slotSelectTime;
/*    */   
/*    */   public GhostSlots(SlotSelectTime slotSelectTime) {
/* 22 */     this.slotSelectTime = slotSelectTime;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 26 */     this.ingredients.clear();
/*    */   }
/*    */   
/*    */   private void setSlot(Slot slot, ContextMap context, SlotDisplay contents, boolean isResult) {
/* 30 */     List<ItemStack> entries = contents.resolveForStacks(context);
/* 31 */     if (!entries.isEmpty()) {
/* 32 */       this.ingredients.put(slot, new GhostSlot(entries, isResult));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void setInput(Slot slot, ContextMap context, SlotDisplay contents) {
/* 37 */     setSlot(slot, context, contents, false);
/*    */   }
/*    */   
/*    */   protected void setResult(Slot slot, ContextMap context, SlotDisplay contents) {
/* 41 */     setSlot(slot, context, contents, true);
/*    */   }
/*    */   
/*    */   public void render(GuiGraphics graphics, Minecraft minecraft, boolean isResultSlotBig) {
/* 45 */     this.ingredients.forEach((slot, ingredient) -> {
/*    */           int x = graphics.x, y = graphics.y;
/*    */           if (ingredient.isResultSlot && isResultSlotBig) {
/*    */             isResultSlotBig.fill(x - 4, y - 4, x + 20, y + 20, 822018048);
/*    */           } else {
/*    */             isResultSlotBig.fill(x, y, x + 16, y + 16, 822018048);
/*    */           } 
/*    */           ItemStack itemStack = ingredient.getItem(this.slotSelectTime.currentIndex());
/*    */           isResultSlotBig.renderFakeItem(itemStack, x, y);
/*    */           isResultSlotBig.fill(x, y, x + 16, y + 16, 822083583);
/*    */           if (ingredient.isResultSlot) {
/*    */             isResultSlotBig.renderItemDecorations(graphics.font, itemStack, x, y);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderTooltip(GuiGraphics graphics, Minecraft minecraft, int mouseX, int mouseY, Slot hoveredSlot) {
/* 68 */     if (hoveredSlot == null) {
/*    */       return;
/*    */     }
/* 71 */     GhostSlot hoveredGhostSlot = (GhostSlot)this.ingredients.get(hoveredSlot);
/* 72 */     if (hoveredGhostSlot != null) {
/* 73 */       ItemStack hoveredItem = hoveredGhostSlot.getItem(this.slotSelectTime.currentIndex());
/* 74 */       graphics.setComponentTooltipForNextFrame(minecraft.font, Screen.getTooltipFromItem(minecraft, hoveredItem), mouseX, mouseY, (Identifier)hoveredItem.get(DataComponents.TOOLTIP_STYLE));
/*    */     } 
/*    */   }
/*    */   private static final class GhostSlot extends Record { private final List<ItemStack> items; private final boolean isResultSlot;
/* 78 */     private GhostSlot(List<ItemStack> items, boolean isResultSlot) { this.items = items; this.isResultSlot = isResultSlot; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 78 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot; } public List<ItemStack> items() { return this.items; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;
/* 78 */       //   0	8	1	o	Ljava/lang/Object; } public boolean isResultSlot() { return this.isResultSlot; }
/*    */      public ItemStack getItem(int itemIndex) {
/* 80 */       int size = this.items.size();
/* 81 */       if (size == 0) {
/* 82 */         return ItemStack.EMPTY;
/*    */       }
/* 84 */       return this.items.get(itemIndex % size);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/GhostSlots.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */