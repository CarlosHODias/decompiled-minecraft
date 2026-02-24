/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.ComponentCollector;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class ComponentRenderUtils
/*    */ {
/* 17 */   private static final FormattedCharSequence INDENT = FormattedCharSequence.codepoint(32, Style.EMPTY);
/*    */   
/*    */   private static String stripColor(String input) {
/* 20 */     return (Boolean)(Minecraft.getInstance()).options.chatColors().get() ? input : ChatFormatting.stripFormatting(input);
/*    */   }
/*    */   
/*    */   public static List<FormattedCharSequence> wrapComponents(FormattedText message, int maxWidth, Font font) {
/* 24 */     ComponentCollector collector = new ComponentCollector();
/* 25 */     message.visit((style, contents) -> { collector.append(FormattedText.of(stripColor(contents), style)); return Optional.empty(); }, Style.EMPTY);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     List<FormattedCharSequence> result = Lists.newArrayList();
/*    */     
/* 32 */     font.getSplitter().splitLines(collector.getResultOrEmpty(), maxWidth, Style.EMPTY, (text, wrapped) -> {
/*    */           FormattedCharSequence reorderedText = Language.getInstance().getVisualOrder(text);
/*    */           
/*    */           result.add(wrapped ? FormattedCharSequence.composite(INDENT, reorderedText) : reorderedText);
/*    */         });
/*    */     
/* 38 */     if (result.isEmpty())
/*    */     {
/* 40 */       return Lists.newArrayList((Object[])new FormattedCharSequence[] { FormattedCharSequence.EMPTY });
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ComponentRenderUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */