/*    */ package net.minecraft.client.gui.screens.multiplayer;
/*    */ 
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.ConnectScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ServerReconfigScreen extends Screen {
/*    */   private static final int DISCONNECT_TIME = 600;
/*    */   private final Connection connection;
/*    */   private Button disconnectButton;
/*    */   private int delayTicker;
/* 21 */   private final LinearLayout layout = LinearLayout.vertical();
/*    */   
/*    */   public ServerReconfigScreen(Component title, Connection connection) {
/* 24 */     super(title);
/* 25 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 35 */     this.layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
/* 36 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/* 37 */     this.disconnectButton = (Button)this.layout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DISCONNECT, b -> this.connection.disconnect(ConnectScreen.ABORT_CONNECTION)).build());
/* 38 */     this.disconnectButton.active = false;
/* 39 */     this.layout.arrangeElements();
/* 40 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 41 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 46 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     super.tick();
/*    */     
/* 53 */     this.delayTicker++;
/* 54 */     if (this.delayTicker == 600) {
/* 55 */       this.disconnectButton.active = true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 60 */     if (this.connection.isConnected()) {
/* 61 */       this.connection.tick();
/*    */     } else {
/* 63 */       this.connection.handleDisconnection();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/ServerReconfigScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */