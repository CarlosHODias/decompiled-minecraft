/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.DisplayData;
/*    */ import com.mojang.blaze3d.platform.MonitorCreator;
/*    */ import com.mojang.blaze3d.platform.ScreenManager;
/*    */ import com.mojang.blaze3d.platform.Window;
/*    */ import com.mojang.blaze3d.platform.WindowEventHandler;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public final class VirtualScreen implements AutoCloseable {
/*    */   private final Minecraft minecraft;
/*    */   private final ScreenManager screenManager;
/*    */   
/*    */   public VirtualScreen(Minecraft minecraft) {
/* 15 */     this.minecraft = minecraft;
/* 16 */     this.screenManager = new ScreenManager(com.mojang.blaze3d.platform.Monitor::new);
/*    */   }
/*    */   
/*    */   public Window newWindow(DisplayData displayData, String fullscreenVideoModeString, String title) {
/* 20 */     return new Window((WindowEventHandler)this.minecraft, this.screenManager, displayData, fullscreenVideoModeString, title);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 25 */     this.screenManager.shutdown();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/VirtualScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */