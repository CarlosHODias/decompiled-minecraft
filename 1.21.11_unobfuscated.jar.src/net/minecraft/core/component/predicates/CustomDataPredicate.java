/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ 
/*    */ public final class CustomDataPredicate extends Record implements DataComponentPredicate {
/*    */   private final net.minecraft.advancements.criterion.NbtPredicate value;
/*    */   
/*  7 */   public CustomDataPredicate(net.minecraft.advancements.criterion.NbtPredicate value) { this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate; } public net.minecraft.advancements.criterion.NbtPredicate value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.Codec<CustomDataPredicate> CODEC = net.minecraft.advancements.criterion.NbtPredicate.CODEC.xmap(CustomDataPredicate::new, CustomDataPredicate::value);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.core.component.DataComponentGetter components) {
/* 13 */     return this.value.matches(components);
/*    */   }
/*    */   
/*    */   public static CustomDataPredicate customData(net.minecraft.advancements.criterion.NbtPredicate value) {
/* 17 */     return new CustomDataPredicate(value);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/CustomDataPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */