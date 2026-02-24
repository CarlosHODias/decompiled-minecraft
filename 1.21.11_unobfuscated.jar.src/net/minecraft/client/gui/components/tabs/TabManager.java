/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public class TabManager {
/*    */   private final Consumer<AbstractWidget> addWidget;
/*    */   private final Consumer<AbstractWidget> removeWidget;
/*    */   private final Consumer<Tab> onSelected;
/*    */   private final Consumer<Tab> onDeselected;
/*    */   private Tab currentTab;
/*    */   private ScreenRectangle tabArea;
/*    */   
/*    */   public TabManager(Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
/* 22 */     this(addWidget, removeWidget, t -> {
/*    */         
/*    */         }, t -> {
/*    */         
/* 26 */         }); } public TabManager(Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget, Consumer<Tab> onSelected, Consumer<Tab> onDeselected) { this.addWidget = addWidget;
/* 27 */     this.removeWidget = removeWidget;
/* 28 */     this.onSelected = onSelected;
/* 29 */     this.onDeselected = onDeselected; }
/*    */ 
/*    */   
/*    */   public void setTabArea(ScreenRectangle tabArea) {
/* 33 */     this.tabArea = tabArea;
/* 34 */     Tab tab = getCurrentTab();
/* 35 */     if (tab != null) {
/* 36 */       tab.doLayout(tabArea);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setCurrentTab(Tab tab, boolean playSound) {
/* 41 */     if (!Objects.equals(this.currentTab, tab)) {
/* 42 */       if (this.currentTab != null) {
/* 43 */         this.currentTab.visitChildren(this.removeWidget);
/*    */       }
/* 45 */       Tab oldTab = this.currentTab;
/* 46 */       this.currentTab = tab;
/* 47 */       tab.visitChildren(this.addWidget);
/* 48 */       if (this.tabArea != null) {
/* 49 */         tab.doLayout(this.tabArea);
/*    */       }
/* 51 */       if (playSound) {
/* 52 */         Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*    */       }
/* 54 */       this.onDeselected.accept(oldTab);
/* 55 */       this.onSelected.accept(this.currentTab);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Tab getCurrentTab() {
/* 60 */     return this.currentTab;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/tabs/TabManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */