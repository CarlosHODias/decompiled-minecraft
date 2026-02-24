/*   */ package net.minecraft.client.model;public final class AdultAndBabyModelPair<T extends Model> extends Record { private final T adultModel; private final T babyModel;
/*   */   
/* 3 */   public AdultAndBabyModelPair(T adultModel, T babyModel) { this.adultModel = adultModel; this.babyModel = babyModel; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/AdultAndBabyModelPair;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>; } public T adultModel() { return this.adultModel; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/AdultAndBabyModelPair;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/AdultAndBabyModelPair;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	8	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>; } public T babyModel() { return this.babyModel; }
/*   */    public T getModel(boolean isBaby) {
/* 5 */     return isBaby ? this.babyModel : this.adultModel;
/*   */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/AdultAndBabyModelPair.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */