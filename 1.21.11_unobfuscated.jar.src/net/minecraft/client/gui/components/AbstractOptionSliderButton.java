/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ 
/*    */ public abstract class AbstractOptionSliderButton extends AbstractSliderButton {
/*    */   protected final Options options;
/*    */   
/*    */   protected AbstractOptionSliderButton(Options options, int x, int y, int width, int height, double initialValue) {
/* 10 */     super(x, y, width, height, CommonComponents.EMPTY, initialValue);
/* 11 */     this.options = options;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractOptionSliderButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */