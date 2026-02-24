/*    */ package net.minecraft.client.gui.navigation;
/*    */ public interface FocusNavigationEvent { ScreenDirection getVerticalDirectionForInitialFocus();
/*    */   
/*    */   public static final class TabNavigation extends Record implements FocusNavigationEvent { private final boolean forward;
/*    */     
/*  6 */     public TabNavigation(boolean forward) { this.forward = forward; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #6	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*  6 */       //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation; } public boolean forward() { return this.forward; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #6	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #6	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;
/*    */       //   0	8	1	o	Ljava/lang/Object; } public ScreenDirection getVerticalDirectionForInitialFocus() {
/*  9 */       return this.forward ? ScreenDirection.DOWN : ScreenDirection.UP;
/*    */     } }
/*    */ 
/*    */   
/*    */   public static class InitialFocus
/*    */     implements FocusNavigationEvent {
/*    */     public ScreenDirection getVerticalDirectionForInitialFocus() {
/* 16 */       return ScreenDirection.DOWN;
/*    */     } }
/*    */   public static final class ArrowNavigation extends Record implements FocusNavigationEvent { private final ScreenDirection direction;
/*    */     
/* 20 */     public ArrowNavigation(ScreenDirection direction) { this.direction = direction; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation;
/* 20 */       //   0	8	1	o	Ljava/lang/Object; } public ScreenDirection direction() { return this.direction; }
/*    */     
/*    */     public ScreenDirection getVerticalDirectionForInitialFocus() {
/* 23 */       return (this.direction.getAxis() == ScreenAxis.VERTICAL) ? this.direction : ScreenDirection.DOWN;
/*    */     } }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/navigation/FocusNavigationEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */