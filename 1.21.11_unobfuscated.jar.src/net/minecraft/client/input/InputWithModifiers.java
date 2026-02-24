/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.InputConstants.Value;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ 
/*    */ public interface InputWithModifiers
/*    */ {
/*    */   public static final int NOT_DIGIT = -1;
/*    */   
/*    */   @com.mojang.blaze3d.platform.InputConstants.Value
/*    */   int input();
/*    */   
/*    */   @Modifiers
/*    */   int modifiers();
/*    */   
/*    */   default boolean isSelection() {
/* 21 */     return (input() == 257 || 
/* 22 */       input() == 32 || 
/* 23 */       input() == 335);
/*    */   }
/*    */   
/*    */   default boolean isConfirmation() {
/* 27 */     return (input() == 257 || 
/* 28 */       input() == 335);
/*    */   }
/*    */   
/*    */   default boolean isEscape() {
/* 32 */     return (input() == 256);
/*    */   }
/*    */   
/*    */   default boolean isLeft() {
/* 36 */     return (input() == 263);
/*    */   }
/*    */   
/*    */   default boolean isRight() {
/* 40 */     return (input() == 262);
/*    */   }
/*    */   
/*    */   default boolean isUp() {
/* 44 */     return (input() == 265);
/*    */   }
/*    */   
/*    */   default boolean isDown() {
/* 48 */     return (input() == 264);
/*    */   }
/*    */   
/*    */   default boolean isCycleFocus() {
/* 52 */     return (input() == 258);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getDigit() {
/* 59 */     int value = input() - 48;
/* 60 */     if (value >= 0 && value <= 9) {
/* 61 */       return value;
/*    */     }
/* 63 */     return -1;
/*    */   }
/*    */   
/*    */   default boolean hasAltDown() {
/* 67 */     return ((modifiers() & 0x4) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasShiftDown() {
/* 71 */     return ((modifiers() & 0x1) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasControlDown() {
/* 75 */     return ((modifiers() & 0x2) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasControlDownWithQuirk() {
/* 79 */     return ((modifiers() & InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER) != 0);
/*    */   }
/*    */   
/*    */   default boolean isSelectAll() {
/* 83 */     return (input() == 65 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isCopy() {
/* 87 */     return (input() == 67 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isPaste() {
/* 91 */     return (input() == 86 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isCut() {
/* 95 */     return (input() == 88 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
/*    */   public static @interface Modifiers {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/input/InputWithModifiers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */