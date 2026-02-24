/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommonLayouts
/*    */ {
/*    */   private static final int LABEL_SPACING = 4;
/*    */   
/*    */   public static Layout labeledElement(Font font, LayoutElement element, Component label) {
/* 16 */     return labeledElement(font, element, label, s -> {
/*    */         
/*    */         });
/*    */   } public static Layout labeledElement(Font font, LayoutElement element, Component label, Consumer<LayoutSettings> settings) {
/* 20 */     LinearLayout layout = LinearLayout.vertical().spacing(4);
/* 21 */     layout.addChild(new StringWidget(label, font));
/* 22 */     layout.addChild(element, settings);
/* 23 */     return layout;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/CommonLayouts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */