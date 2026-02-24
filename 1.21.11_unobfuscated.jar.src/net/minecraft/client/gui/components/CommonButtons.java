/*    */ package net.minecraft.client.gui.components;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CommonButtons {
/*    */   public static SpriteIconButton language(int width, Button.OnPress onPress, boolean iconOnly) {
/*  8 */     return SpriteIconButton.builder((Component)Component.translatable("options.language"), onPress, iconOnly)
/*  9 */       .width(width)
/* 10 */       .sprite(Identifier.withDefaultNamespace("icon/language"), 15, 15)
/* 11 */       .build();
/*    */   }
/*    */   
/*    */   public static SpriteIconButton accessibility(int width, Button.OnPress onPress, boolean iconOnly) {
/* 15 */     MutableComponent mutableComponent = iconOnly ? Component.translatable("options.accessibility") : Component.translatable("accessibility.onboarding.accessibility.button");
/* 16 */     return SpriteIconButton.builder((Component)mutableComponent, onPress, iconOnly)
/* 17 */       .width(width)
/* 18 */       .sprite(Identifier.withDefaultNamespace("icon/accessibility"), 15, 15)
/* 19 */       .build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/CommonButtons.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */