/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ChatOptionsScreen extends OptionsSubScreen {
/*  9 */   private static final Component TITLE = (Component)Component.translatable("options.chat.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 12 */     return (OptionInstance<?>[])new OptionInstance[] { 
/* 13 */         options.chatVisibility(), options.chatColors(), 
/* 14 */         options.chatLinks(), options.chatLinksPrompt(), 
/* 15 */         options.chatOpacity(), options.textBackgroundOpacity(), 
/* 16 */         options.chatScale(), options.chatLineSpacing(), 
/* 17 */         options.chatDelay(), options.chatWidth(), 
/* 18 */         options.chatHeightFocused(), options.chatHeightUnfocused(), 
/* 19 */         options.narrator(), options.autoSuggestions(), 
/* 20 */         options.hideMatchedNames(), options.reducedDebugInfo(), 
/* 21 */         options.onlyShowSecureChat(), options.saveChatDrafts() };
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatOptionsScreen(Screen lastScreen, Options options) {
/* 26 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 31 */     this.list.addSmall((OptionInstance[])options(this.options));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/ChatOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */