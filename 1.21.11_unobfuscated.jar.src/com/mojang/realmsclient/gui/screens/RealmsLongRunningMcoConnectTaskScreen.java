/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*    */ import com.mojang.realmsclient.dto.ServiceQuality;
/*    */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.ImageWidget;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class RealmsLongRunningMcoConnectTaskScreen extends RealmsLongRunningMcoTaskScreen {
/*    */   private final LongRunningTask task;
/* 20 */   private final LinearLayout footer = LinearLayout.vertical(); private final RealmsJoinInformation serverAddress;
/*    */   
/*    */   public RealmsLongRunningMcoConnectTaskScreen(Screen lastScreen, RealmsJoinInformation serverAddress, LongRunningTask task) {
/* 23 */     super(lastScreen, new LongRunningTask[] { task });
/* 24 */     this.task = task;
/* 25 */     this.serverAddress = serverAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 30 */     super.init();
/* 31 */     if (this.serverAddress.regionData() == null || this.serverAddress.regionData().region() == null) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     LinearLayout regionInfo = LinearLayout.horizontal().spacing(10);
/* 36 */     StringWidget region = new StringWidget((Component)Component.translatable("mco.connect.region", new Object[] { Component.translatable((this.serverAddress.regionData().region()).translationKey) }), this.font);
/* 37 */     regionInfo.addChild((LayoutElement)region);
/* 38 */     Identifier icon = (this.serverAddress.regionData().serviceQuality() != null) ? this.serverAddress.regionData().serviceQuality().getIcon() : ServiceQuality.UNKNOWN.getIcon();
/* 39 */     regionInfo.addChild((LayoutElement)ImageWidget.sprite(10, 8, icon), LayoutSettings::alignVerticallyTop);
/* 40 */     this.footer.addChild((LayoutElement)regionInfo, layoutSettings -> layoutSettings.paddingTop(40));
/*    */     
/* 42 */     this.footer.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 43 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 48 */     super.repositionElements();
/*    */     
/* 50 */     int contentBottom = this.layout.getY() + this.layout.getHeight();
/* 51 */     ScreenRectangle footerRectangle = new ScreenRectangle(0, contentBottom, this.width, this.height - contentBottom);
/*    */     
/* 53 */     this.footer.arrangeElements();
/* 54 */     FrameLayout.alignInRectangle((LayoutElement)this.footer, footerRectangle, 0.5F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 59 */     super.tick();
/* 60 */     this.task.tick();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void cancel() {
/* 65 */     this.task.abortTask();
/* 66 */     super.cancel();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsLongRunningMcoConnectTaskScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */