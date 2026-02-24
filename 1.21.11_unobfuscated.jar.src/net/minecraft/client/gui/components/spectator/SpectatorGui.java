/*     */ package net.minecraft.client.gui.components.spectator;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuListener;
/*     */ import net.minecraft.client.gui.spectator.categories.SpectatorPage;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class SpectatorGui implements SpectatorMenuListener {
/*  18 */   private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
/*  19 */   private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
/*     */   
/*     */   private static final long FADE_OUT_DELAY = 5000L;
/*     */   private static final long FADE_OUT_TIME = 2000L;
/*     */   private final Minecraft minecraft;
/*     */   private long lastSelectionTime;
/*     */   private SpectatorMenu menu;
/*     */   
/*     */   public SpectatorGui(Minecraft minecraft) {
/*  28 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void onHotbarSelected(int slot) {
/*  32 */     this.lastSelectionTime = Util.getMillis();
/*     */     
/*  34 */     if (this.menu != null) {
/*  35 */       this.menu.selectSlot(slot);
/*     */     } else {
/*  37 */       this.menu = new SpectatorMenu(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getHotbarAlpha() {
/*  42 */     long delta = this.lastSelectionTime - Util.getMillis() + 5000L;
/*  43 */     return Mth.clamp((float)delta / 2000.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void renderHotbar(GuiGraphics graphics) {
/*  47 */     if (this.menu == null) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     float alpha = getHotbarAlpha();
/*  52 */     if (alpha <= 0.0F) {
/*  53 */       this.menu.exit();
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     int screenCenter = graphics.guiWidth() / 2;
/*  58 */     int y = Mth.floor(graphics.guiHeight() - 22.0F * alpha);
/*     */     
/*  60 */     SpectatorPage page = this.menu.getCurrentPage();
/*     */     
/*  62 */     renderPage(graphics, alpha, screenCenter, y, page);
/*     */   }
/*     */   
/*     */   protected void renderPage(GuiGraphics graphics, float alpha, int screenCenter, int y, SpectatorPage page) {
/*  66 */     int color = ARGB.white(alpha);
/*  67 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, screenCenter - 91, y, 182, 22, color);
/*  68 */     if (page.getSelectedSlot() >= 0) {
/*  69 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + page.getSelectedSlot() * 20, y - 1, 24, 23, color);
/*     */     }
/*  71 */     for (int slot = 0; slot < 9; slot++) {
/*  72 */       renderSlot(graphics, slot, graphics.guiWidth() / 2 - 90 + slot * 20 + 2, (y + 3), alpha, page.getItem(slot));
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderSlot(GuiGraphics graphics, int slot, int x, float y, float alpha, SpectatorMenuItem item) {
/*  77 */     if (item != SpectatorMenu.EMPTY_SLOT) {
/*  78 */       graphics.pose().pushMatrix();
/*  79 */       graphics.pose().translate(x, y);
/*     */       
/*  81 */       float brightness = item.isEnabled() ? 1.0F : 0.25F;
/*  82 */       item.renderIcon(graphics, brightness, alpha);
/*     */       
/*  84 */       graphics.pose().popMatrix();
/*     */       
/*  86 */       if (alpha > 0.0F && item.isEnabled()) {
/*  87 */         Component key = this.minecraft.options.keyHotbarSlots[slot].getTranslatedKeyMessage();
/*  88 */         graphics.drawString(this.minecraft.font, key, x + 19 - 2 - this.minecraft.font.width((FormattedText)key), (int)y + 6 + 3, ARGB.white(alpha));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderAction(GuiGraphics graphics) {
/*  94 */     float alpha = getHotbarAlpha();
/*     */     
/*  96 */     if (alpha > 0.0F && this.menu != null) {
/*  97 */       SpectatorMenuItem item = this.menu.getSelectedItem();
/*  98 */       Component action = (item == SpectatorMenu.EMPTY_SLOT) ? this.menu.getSelectedCategory().getPrompt() : item.getName();
/*  99 */       int strWidth = this.minecraft.font.width((FormattedText)action);
/* 100 */       int x = (graphics.guiWidth() - strWidth) / 2;
/* 101 */       int y = graphics.guiHeight() - 35;
/* 102 */       graphics.drawStringWithBackdrop(this.minecraft.font, action, x, y, strWidth, ARGB.white(alpha));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSpectatorMenuClosed(SpectatorMenu menu) {
/* 108 */     this.menu = null;
/* 109 */     this.lastSelectionTime = 0L;
/*     */   }
/*     */   
/*     */   public boolean isMenuActive() {
/* 113 */     return (this.menu != null);
/*     */   }
/*     */   
/*     */   public void onMouseScrolled(int wheel) {
/* 117 */     int newSlot = this.menu.getSelectedSlot() + wheel;
/* 118 */     while (newSlot >= 0 && newSlot <= 8 && (this.menu.getItem(newSlot) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem(newSlot).isEnabled())) {
/* 119 */       newSlot += wheel;
/*     */     }
/*     */     
/* 122 */     if (newSlot >= 0 && newSlot <= 8) {
/* 123 */       this.menu.selectSlot(newSlot);
/* 124 */       this.lastSelectionTime = Util.getMillis();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onHotbarActionKeyPressed() {
/* 129 */     this.lastSelectionTime = Util.getMillis();
/*     */     
/* 131 */     if (isMenuActive()) {
/* 132 */       int selectedSlot = this.menu.getSelectedSlot();
/* 133 */       if (selectedSlot != -1) {
/* 134 */         this.menu.selectSlot(selectedSlot);
/*     */       }
/*     */     } else {
/* 137 */       this.menu = new SpectatorMenu(this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/spectator/SpectatorGui.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */