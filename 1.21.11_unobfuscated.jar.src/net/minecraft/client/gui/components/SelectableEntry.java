/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ public interface SelectableEntry {
/*    */   default boolean mouseOverIcon(int relX, int relY, int size) {
/*  5 */     return (relX >= 0 && relX < size && relY >= 0 && relY < size);
/*    */   }
/*    */   
/*    */   default boolean mouseOverLeftHalf(int relX, int relY, int size) {
/*  9 */     return (relX >= 0 && relX < size / 2 && relY >= 0 && relY < size);
/*    */   }
/*    */   
/*    */   default boolean mouseOverRightHalf(int relX, int relY, int size) {
/* 13 */     return (relX >= size / 2 && relX < size && relY >= 0 && relY < size);
/*    */   }
/*    */   
/*    */   default boolean mouseOverTopRightQuarter(int relX, int relY, int size) {
/* 17 */     return (relX >= size / 2 && relX < size && relY >= 0 && relY < size / 2);
/*    */   }
/*    */   
/*    */   default boolean mouseOverBottomRightQuarter(int relX, int relY, int size) {
/* 21 */     return (relX >= size / 2 && relX < size && relY >= size / 2 && relY < size);
/*    */   }
/*    */   
/*    */   default boolean mouseOverTopLeftQuarter(int relX, int relY, int size) {
/* 25 */     return (relX >= 0 && relX < size / 2 && relY >= 0 && relY < size / 2);
/*    */   }
/*    */   
/*    */   default boolean mouseOverBottomLeftQuarter(int relX, int relY, int size) {
/* 29 */     return (relX >= 0 && relX < size / 2 && relY >= size / 2 && relY < size);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/SelectableEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */