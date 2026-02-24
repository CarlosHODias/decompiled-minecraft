/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.InputConstants;
/*    */ import java.util.Arrays;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class MouseSettingsScreen
/*    */   extends OptionsSubScreen {
/* 13 */   private static final Component TITLE = (Component)Component.translatable("options.mouse_settings.title");
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 15 */     return (OptionInstance<?>[])new OptionInstance[] {
/* 16 */         options.sensitivity(), 
/* 17 */         options.touchscreen(), 
/* 18 */         options.mouseWheelSensitivity(), 
/* 19 */         options.discreteMouseScroll(), 
/* 20 */         options.invertMouseX(), 
/* 21 */         options.invertMouseY(), 
/* 22 */         options.allowCursorChanges()
/*    */       };
/*    */   }
/*    */   
/*    */   public MouseSettingsScreen(Screen lastScreen, Options options) {
/* 27 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 32 */     if (InputConstants.isRawMouseInputSupported()) {
/* 33 */       this.list.addSmall((OptionInstance[])Stream.concat(Arrays.stream((Object[])options(this.options)), Stream.of(this.options.rawMouseInput())).toArray(x$0 -> new OptionInstance[x$0]));
/*    */     } else {
/* 35 */       this.list.addSmall((OptionInstance[])options(this.options));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/MouseSettingsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */