/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FontOptionsScreen extends OptionsSubScreen {
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 10 */     return (OptionInstance<?>[])new OptionInstance[] {
/* 11 */         options.forceUnicodeFont(), options.japaneseGlyphVariants()
/*    */       };
/*    */   }
/*    */   
/*    */   public FontOptionsScreen(Screen lastScreen, Options options) {
/* 16 */     super(lastScreen, options, (Component)Component.translatable("options.font.title"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 21 */     this.list.addSmall((OptionInstance[])options(this.options));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/FontOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */