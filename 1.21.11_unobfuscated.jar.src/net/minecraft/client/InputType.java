/*    */ package net.minecraft.client;
/*    */ 
/*    */ public enum InputType {
/*  4 */   NONE,
/*  5 */   MOUSE,
/*  6 */   KEYBOARD_ARROW,
/*  7 */   KEYBOARD_TAB;
/*    */ 
/*    */   
/*    */   public boolean isMouse() {
/* 11 */     return (this == MOUSE);
/*    */   }
/*    */   
/*    */   public boolean isKeyboard() {
/* 15 */     return (this == KEYBOARD_ARROW || this == KEYBOARD_TAB);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/InputType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */