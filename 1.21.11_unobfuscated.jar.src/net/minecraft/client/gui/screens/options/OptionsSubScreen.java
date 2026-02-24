/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.components.OptionsList;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class OptionsSubScreen
/*    */   extends Screen {
/*    */   protected final Screen lastScreen;
/*    */   protected final Options options;
/*    */   protected OptionsList list;
/* 21 */   public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*    */   
/*    */   public OptionsSubScreen(Screen lastScreen, Options options, Component title) {
/* 24 */     super(title);
/* 25 */     this.lastScreen = lastScreen;
/* 26 */     this.options = options;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 31 */     addTitle();
/* 32 */     addContents();
/* 33 */     addFooter();
/*    */     
/* 35 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 36 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void addTitle() {
/* 40 */     this.layout.addTitleHeader(this.title, this.font);
/*    */   }
/*    */   
/*    */   protected void addContents() {
/* 44 */     this.list = (OptionsList)this.layout.addToContents((LayoutElement)new OptionsList(this.minecraft, this.width, this));
/* 45 */     addOptions();
/*    */     
/* 47 */     AbstractWidget abstractWidget = this.list.findOption(this.options.narrator()); if (abstractWidget instanceof CycleButton) { CycleButton cycleButton = (CycleButton)abstractWidget;
/* 48 */       this.narratorButton = cycleButton;
/* 49 */       this.narratorButton.active = this.minecraft.getNarrator().isActive(); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addFooter() {
/* 56 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 61 */     this.layout.arrangeElements();
/* 62 */     if (this.list != null) {
/* 63 */       this.list.updateSize(this.width, this.layout);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed() {
/* 69 */     this.minecraft.options.save();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 74 */     if (this.list != null) {
/* 75 */       this.list.applyUnsavedChanges();
/*    */     }
/* 77 */     this.minecraft.setScreen(this.lastScreen);
/*    */   }
/*    */   
/*    */   public void resetOption(OptionInstance<?> option) {
/* 81 */     if (this.list != null)
/* 82 */       this.list.resetOption(option); 
/*    */   }
/*    */   
/*    */   protected abstract void addOptions();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/OptionsSubScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */