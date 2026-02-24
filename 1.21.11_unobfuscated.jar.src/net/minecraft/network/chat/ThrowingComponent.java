/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public class ThrowingComponent extends Exception {
/*    */   private final Component component;
/*    */   
/*    */   public ThrowingComponent(Component component) {
/*  7 */     super(component.getString());
/*  8 */     this.component = component;
/*    */   }
/*    */   
/*    */   public ThrowingComponent(Component component, Throwable cause) {
/* 12 */     super(component.getString(), cause);
/* 13 */     this.component = component;
/*    */   }
/*    */   
/*    */   public Component getComponent() {
/* 17 */     return this.component;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/ThrowingComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */