/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ 
/*    */ public class CreditsAndAttributionScreen extends Screen {
/*    */   private static final int BUTTON_SPACING = 8;
/*    */   private static final int BUTTON_WIDTH = 210;
/* 14 */   private static final Component TITLE = (Component)Component.translatable("credits_and_attribution.screen.title");
/* 15 */   private static final Component CREDITS_BUTTON = (Component)Component.translatable("credits_and_attribution.button.credits");
/* 16 */   private static final Component ATTRIBUTION_BUTTON = (Component)Component.translatable("credits_and_attribution.button.attribution");
/* 17 */   private static final Component LICENSES_BUTTON = (Component)Component.translatable("credits_and_attribution.button.licenses");
/*    */   
/*    */   private final Screen lastScreen;
/* 20 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*    */   
/*    */   public CreditsAndAttributionScreen(Screen lastScreen) {
/* 23 */     super(TITLE);
/* 24 */     this.lastScreen = lastScreen;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 29 */     this.layout.addTitleHeader(TITLE, this.font);
/*    */     
/* 31 */     LinearLayout content = ((LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical())).spacing(8);
/* 32 */     content.defaultCellSetting().alignHorizontallyCenter();
/*    */     
/* 34 */     content.addChild((LayoutElement)Button.builder(CREDITS_BUTTON, button -> openCreditsScreen()).width(210).build());
/* 35 */     content.addChild((LayoutElement)Button.builder(ATTRIBUTION_BUTTON, ConfirmLinkScreen.confirmLink(this, CommonLinks.ATTRIBUTION)).width(210).build());
/* 36 */     content.addChild((LayoutElement)Button.builder(LICENSES_BUTTON, ConfirmLinkScreen.confirmLink(this, CommonLinks.LICENSES)).width(210).build());
/*    */     
/* 38 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*    */     
/* 40 */     this.layout.arrangeElements();
/* 41 */     this.layout.visitWidgets(this::addRenderableWidget);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 46 */     this.layout.arrangeElements();
/*    */   }
/*    */   
/*    */   private void openCreditsScreen() {
/* 50 */     this.minecraft.setScreen(new WinScreen(false, () -> this.minecraft.setScreen(this)));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 55 */     this.minecraft.setScreen(this.lastScreen);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/CreditsAndAttributionScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */