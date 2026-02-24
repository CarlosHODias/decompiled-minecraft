/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface Nameable
/*    */ {
/*    */   Component getName();
/*    */   
/*    */   default String getPlainTextName() {
/* 10 */     return getName().getString();
/*    */   }
/*    */   
/*    */   default boolean hasCustomName() {
/* 14 */     return (getCustomName() != null);
/*    */   }
/*    */   
/*    */   default Component getDisplayName() {
/* 18 */     return getName();
/*    */   }
/*    */   
/*    */   default Component getCustomName() {
/* 22 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/Nameable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */