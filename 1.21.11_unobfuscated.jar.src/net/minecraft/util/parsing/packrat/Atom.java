/*    */ package net.minecraft.util.parsing.packrat;public final class Atom<T> extends Record { private final String name;
/*    */   
/*  3 */   public Atom(String name) { this.name = name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Atom;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Atom;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  3 */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Atom<TT;>; } public String name() { return this.name; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Atom;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Atom;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Atom<TT;>;
/*    */   } public String toString() {
/*  6 */     return "<" + this.name + ">";
/*    */   }
/*    */   
/*    */   public static <T> Atom<T> of(String name) {
/* 10 */     return new Atom<>(name);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/Atom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */