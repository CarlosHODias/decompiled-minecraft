/*    */ package net.minecraft.client.gui.navigation;public final class ScreenPosition extends Record { private final int x; private final int y;
/*    */   
/*  3 */   public ScreenPosition(int x, int y) { this.x = x; this.y = y; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/ScreenPosition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition; } public int x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/ScreenPosition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/ScreenPosition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public int y() { return this.y; }
/*    */    public static ScreenPosition of(ScreenAxis axis, int primaryValue, int secondaryValue) {
/*  5 */     switch (axis) { default: throw new MatchException(null, null);case HORIZONTAL: case VERTICAL: break; }  return 
/*    */       
/*  7 */       new ScreenPosition(secondaryValue, primaryValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenPosition step(ScreenDirection direction) {
/* 12 */     switch (direction) { default: throw new MatchException(null, null);case DOWN: case UP: case LEFT: case RIGHT: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 16 */       new ScreenPosition(this.x + 1, this.y);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCoordinate(ScreenAxis axis) {
/* 21 */     switch (axis) { default: throw new MatchException(null, null);case HORIZONTAL: case VERTICAL: break; }  return 
/*    */       
/* 23 */       this.y;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/navigation/ScreenPosition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */