/*     */ package net.minecraft.client.gui.spectator;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.spectator.categories.SpectatorPage;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class SpectatorMenu
/*     */ {
/*  16 */   private static final Identifier CLOSE_SPRITE = Identifier.withDefaultNamespace("spectator/close");
/*  17 */   private static final Identifier SCROLL_LEFT_SPRITE = Identifier.withDefaultNamespace("spectator/scroll_left");
/*  18 */   private static final Identifier SCROLL_RIGHT_SPRITE = Identifier.withDefaultNamespace("spectator/scroll_right");
/*  19 */   private static final SpectatorMenuItem CLOSE_ITEM = new CloseSpectatorItem();
/*  20 */   private static final SpectatorMenuItem SCROLL_LEFT = new ScrollMenuItem(-1, true);
/*  21 */   private static final SpectatorMenuItem SCROLL_RIGHT_ENABLED = new ScrollMenuItem(1, true);
/*  22 */   private static final SpectatorMenuItem SCROLL_RIGHT_DISABLED = new ScrollMenuItem(1, false);
/*     */   
/*     */   private static final int MAX_PER_PAGE = 8;
/*     */   
/*  26 */   private static final Component CLOSE_MENU_TEXT = (Component)Component.translatable("spectatorMenu.close");
/*  27 */   private static final Component PREVIOUS_PAGE_TEXT = (Component)Component.translatable("spectatorMenu.previous_page");
/*  28 */   private static final Component NEXT_PAGE_TEXT = (Component)Component.translatable("spectatorMenu.next_page");
/*     */   
/*  30 */   public static final SpectatorMenuItem EMPTY_SLOT = new SpectatorMenuItem()
/*     */     {
/*     */       public void selectItem(SpectatorMenu menu) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public Component getName() {
/*  37 */         return CommonComponents.EMPTY;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {}
/*     */ 
/*     */       
/*     */       public boolean isEnabled() {
/*  46 */         return false;
/*     */       }
/*     */     };
/*     */   
/*     */   private final SpectatorMenuListener listener;
/*     */   private SpectatorMenuCategory category;
/*  52 */   private int selectedSlot = -1;
/*     */   private int page;
/*     */   
/*     */   public SpectatorMenu(SpectatorMenuListener listener) {
/*  56 */     this.category = new RootSpectatorMenuCategory();
/*  57 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   public SpectatorMenuItem getItem(int slot) {
/*  61 */     int index = slot + this.page * 6;
/*     */     
/*  63 */     if (this.page > 0 && slot == 0)
/*  64 */       return SCROLL_LEFT; 
/*  65 */     if (slot == 7) {
/*  66 */       if (index < this.category.getItems().size()) {
/*  67 */         return SCROLL_RIGHT_ENABLED;
/*     */       }
/*  69 */       return SCROLL_RIGHT_DISABLED;
/*     */     } 
/*     */ 
/*     */     
/*  73 */     if (slot == 8) {
/*  74 */       return CLOSE_ITEM;
/*     */     }
/*     */     
/*  77 */     if (index < 0 || index >= this.category.getItems().size()) {
/*  78 */       return EMPTY_SLOT;
/*     */     }
/*  80 */     return (SpectatorMenuItem)MoreObjects.firstNonNull(this.category.getItems().get(index), EMPTY_SLOT);
/*     */   }
/*     */   
/*     */   public List<SpectatorMenuItem> getItems() {
/*  84 */     List<SpectatorMenuItem> items = Lists.newArrayList();
/*     */     
/*  86 */     for (int i = 0; i <= 8; i++) {
/*  87 */       items.add(getItem(i));
/*     */     }
/*     */     
/*  90 */     return items;
/*     */   }
/*     */   
/*     */   public SpectatorMenuItem getSelectedItem() {
/*  94 */     return getItem(this.selectedSlot);
/*     */   }
/*     */   
/*     */   public SpectatorMenuCategory getSelectedCategory() {
/*  98 */     return this.category;
/*     */   }
/*     */   
/*     */   public void selectSlot(int slot) {
/* 102 */     SpectatorMenuItem item = getItem(slot);
/*     */     
/* 104 */     if (item != EMPTY_SLOT) {
/* 105 */       if (this.selectedSlot == slot && item.isEnabled()) {
/* 106 */         item.selectItem(this);
/*     */       } else {
/* 108 */         this.selectedSlot = slot;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void exit() {
/* 114 */     this.listener.onSpectatorMenuClosed(this);
/*     */   }
/*     */   
/*     */   public int getSelectedSlot() {
/* 118 */     return this.selectedSlot;
/*     */   }
/*     */   
/*     */   public void selectCategory(SpectatorMenuCategory category) {
/* 122 */     this.category = category;
/* 123 */     this.selectedSlot = -1;
/* 124 */     this.page = 0;
/*     */   }
/*     */   
/*     */   public SpectatorPage getCurrentPage() {
/* 128 */     return new SpectatorPage(getItems(), this.selectedSlot);
/*     */   }
/*     */   
/*     */   private static class CloseSpectatorItem
/*     */     implements SpectatorMenuItem {
/*     */     public void selectItem(SpectatorMenu menu) {
/* 134 */       menu.exit();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getName() {
/* 139 */       return SpectatorMenu.CLOSE_MENU_TEXT;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/* 144 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.CLOSE_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled() {
/* 149 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ScrollMenuItem implements SpectatorMenuItem {
/*     */     private final int direction;
/*     */     private final boolean enabled;
/*     */     
/*     */     public ScrollMenuItem(int direction, boolean enabled) {
/* 158 */       this.direction = direction;
/* 159 */       this.enabled = enabled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void selectItem(SpectatorMenu menu) {
/* 164 */       menu.page += this.direction;
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getName() {
/* 169 */       return (this.direction < 0) ? SpectatorMenu.PREVIOUS_PAGE_TEXT : SpectatorMenu.NEXT_PAGE_TEXT;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/* 174 */       int color = ARGB.colorFromFloat(alpha, brightness, brightness, brightness);
/* 175 */       if (this.direction < 0) {
/* 176 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_LEFT_SPRITE, 0, 0, 16, 16, color);
/*     */       } else {
/* 178 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_RIGHT_SPRITE, 0, 0, 16, 16, color);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled() {
/* 184 */       return this.enabled;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/SpectatorMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */