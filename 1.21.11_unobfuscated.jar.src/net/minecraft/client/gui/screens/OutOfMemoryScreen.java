/*    */ package net.minecraft.client.gui.screens;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class OutOfMemoryScreen extends Screen {
/* 11 */   private static final Component TITLE = (Component)Component.translatable("outOfMemory.title");
/* 12 */   private static final Component MESSAGE = (Component)Component.translatable("outOfMemory.message");
/*    */   
/*    */   private static final int MESSAGE_WIDTH = 300;
/* 15 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*    */   
/*    */   public OutOfMemoryScreen() {
/* 18 */     super(TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 23 */     this.layout.addTitleHeader(TITLE, this.font);
/* 24 */     this.layout.addToContents((LayoutElement)FocusableTextWidget.builder(MESSAGE, this.font).maxWidth(300).build());
/* 25 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 26 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_TO_TITLE, button -> this.minecraft.setScreen(new TitleScreen())).build());
/* 27 */     footer.addChild((LayoutElement)Button.builder((Component)Component.translatable("menu.quit"), button -> this.minecraft.stop()).build());
/* 28 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 29 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 34 */     this.layout.arrangeElements();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/OutOfMemoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */