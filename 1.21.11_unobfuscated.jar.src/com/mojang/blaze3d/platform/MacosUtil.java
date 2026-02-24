/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import ca.weblite.objc.Client;
/*    */ import ca.weblite.objc.NSObject;
/*    */ import com.sun.jna.Pointer;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Base64;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import org.lwjgl.glfw.GLFWNativeCocoa;
/*    */ 
/*    */ 
/*    */ public class MacosUtil
/*    */ {
/* 17 */   public static final boolean IS_MACOS = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
/*    */   
/*    */   private static final int NS_RESIZABLE_WINDOW_MASK = 8;
/*    */   
/*    */   private static final int NS_FULL_SCREEN_WINDOW_MASK = 16384;
/*    */   
/*    */   public static void exitNativeFullscreen(Window window) {
/* 24 */     getNsWindow(window).filter(MacosUtil::isInNativeFullscreen).ifPresent(MacosUtil::toggleNativeFullscreen);
/*    */   }
/*    */   
/*    */   public static void clearResizableBit(Window window) {
/* 28 */     getNsWindow(window).ifPresent(nsWindow -> {
/*    */           long styleMask = getStyleMask(nsWindow);
/*    */           nsWindow.send("setStyleMask:", new Object[] { styleMask & 0xFFFFFFFFFFFFFFF7L });
/*    */         });
/*    */   }
/*    */   
/*    */   private static Optional<NSObject> getNsWindow(Window window) {
/* 35 */     long nsWindow = GLFWNativeCocoa.glfwGetCocoaWindow(window.handle());
/* 36 */     if (nsWindow != 0L) {
/* 37 */       return Optional.of(new NSObject(new Pointer(nsWindow)));
/*    */     }
/* 39 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   private static boolean isInNativeFullscreen(NSObject nsWindow) {
/* 43 */     return ((getStyleMask(nsWindow) & 0x4000L) != 0L);
/*    */   }
/*    */   
/*    */   private static long getStyleMask(NSObject nsWindow) {
/* 47 */     return (Long)nsWindow.sendRaw("styleMask", new Object[0]);
/*    */   }
/*    */   
/*    */   private static void toggleNativeFullscreen(NSObject nsWindow) {
/* 51 */     nsWindow.send("toggleFullScreen:", new Object[] { Pointer.NULL });
/*    */   }
/*    */   
/*    */   public static void loadIcon(IoSupplier<InputStream> icon) throws IOException {
/* 55 */     InputStream iconStream = (InputStream)icon.get(); try {
/* 56 */       String base64Icon = Base64.getEncoder().encodeToString(iconStream.readAllBytes());
/* 57 */       Client objc = Client.getInstance();
/*    */       
/* 59 */       Object data = objc.sendProxy("NSData", "alloc", new Object[0]).send("initWithBase64Encoding:", new Object[] { base64Icon });
/* 60 */       Object image = objc.sendProxy("NSImage", "alloc", new Object[0]).send("initWithData:", new Object[] { data });
/*    */       
/* 62 */       objc.sendProxy("NSApplication", "sharedApplication", new Object[0]).send("setApplicationIconImage:", new Object[] { image });
/* 63 */       if (iconStream != null) iconStream.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (iconStream != null)
/*    */         try {
/*    */           iconStream.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/MacosUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */