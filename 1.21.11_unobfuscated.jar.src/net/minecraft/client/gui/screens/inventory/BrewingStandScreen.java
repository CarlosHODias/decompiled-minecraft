/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.BrewingStandMenu;
/*    */ 
/*    */ public class BrewingStandScreen
/*    */   extends AbstractContainerScreen<BrewingStandMenu>
/*    */ {
/* 15 */   private static final Identifier FUEL_LENGTH_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/fuel_length");
/* 16 */   private static final Identifier BREW_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/brew_progress");
/* 17 */   private static final Identifier BUBBLES_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/bubbles");
/* 18 */   private static final Identifier BREWING_STAND_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/brewing_stand.png");
/* 19 */   private static final int[] BUBBLELENGTHS = new int[] { 29, 24, 20, 16, 11, 6, 0 };
/*    */   
/*    */   public BrewingStandScreen(BrewingStandMenu menu, Inventory inventory, Component title) {
/* 22 */     super(menu, inventory, title);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 27 */     super.init();
/* 28 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 33 */     super.render(graphics, mouseX, mouseY, a);
/* 34 */     renderTooltip(graphics, mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 39 */     int xo = (this.width - this.imageWidth) / 2;
/* 40 */     int yo = (this.height - this.imageHeight) / 2;
/* 41 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BREWING_STAND_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 42 */     int fuel = this.menu.getFuel();
/* 43 */     int fuelLength = Mth.clamp((18 * fuel + 20 - 1) / 20, 0, 18);
/* 44 */     if (fuelLength > 0) {
/* 45 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FUEL_LENGTH_SPRITE, 18, 4, 0, 0, xo + 60, yo + 44, fuelLength, 4);
/*    */     }
/*    */     
/* 48 */     int tickCount = this.menu.getBrewingTicks();
/* 49 */     if (tickCount > 0) {
/* 50 */       int length = (int)(28.0F * (1.0F - tickCount / 400.0F));
/* 51 */       if (length > 0) {
/* 52 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREW_PROGRESS_SPRITE, 9, 28, 0, 0, xo + 97, yo + 16, 9, length);
/*    */       }
/*    */       
/* 55 */       length = BUBBLELENGTHS[tickCount / 2 % 7];
/* 56 */       if (length > 0)
/* 57 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BUBBLES_SPRITE, 12, 29, 0, 29 - length, xo + 63, yo + 14 + 29 - length, 12, length); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BrewingStandScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */