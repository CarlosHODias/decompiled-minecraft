/*   */ package net.minecraft.core.component.predicates;
/*   */ 
/*   */ public final class AnyValue extends Record implements DataComponentPredicate {
/*   */   private final net.minecraft.core.component.DataComponentType<?> type;
/*   */   
/* 6 */   public AnyValue(net.minecraft.core.component.DataComponentType<?> type) { this.type = type; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/AnyValue;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AnyValue; } public net.minecraft.core.component.DataComponentType<?> type() { return this.type; }
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/AnyValue;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AnyValue; }
/*   */   public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/AnyValue;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/AnyValue;
/*   */     //   0	8	1	o	Ljava/lang/Object; } public boolean matches(net.minecraft.core.component.DataComponentGetter components) {
/* 9 */     return (components.get(this.type) != null);
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/AnyValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */