/*    */ package net.minecraft.client;
/*    */ 
/*    */ import net.minecraft.client.color.item.ItemTintSources;
/*    */ import net.minecraft.client.gui.screens.dialog.DialogScreens;
/*    */ import net.minecraft.client.gui.screens.dialog.body.DialogBodyHandlers;
/*    */ import net.minecraft.client.gui.screens.dialog.input.InputControlHandlers;
/*    */ import net.minecraft.client.renderer.item.ItemModels;
/*    */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
/*    */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
/*    */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderers;
/*    */ import net.minecraft.client.renderer.texture.atlas.SpriteSources;
/*    */ 
/*    */ public class ClientBootstrap {
/*    */   private static volatile boolean isBootstrapped;
/*    */   
/*    */   public static void bootstrap() {
/* 18 */     if (isBootstrapped) {
/*    */       return;
/*    */     }
/* 21 */     isBootstrapped = true;
/*    */     
/* 23 */     ItemModels.bootstrap();
/* 24 */     SpecialModelRenderers.bootstrap();
/* 25 */     ItemTintSources.bootstrap();
/* 26 */     SelectItemModelProperties.bootstrap();
/* 27 */     ConditionalItemModelProperties.bootstrap();
/* 28 */     RangeSelectItemModelProperties.bootstrap();
/* 29 */     SpriteSources.bootstrap();
/* 30 */     DialogScreens.bootstrap();
/* 31 */     InputControlHandlers.bootstrap();
/* 32 */     DialogBodyHandlers.bootstrap();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ClientBootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */