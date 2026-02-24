/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import net.minecraft.server.network.Filterable;
/*    */ 
/*    */ public final class PagePredicate extends Record implements java.util.function.Predicate<Filterable<String>> {
/*    */   private final String contents;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */   }
/*    */   
/* 16 */   public PagePredicate(String contents) { this.contents = contents; } public String contents() { return this.contents; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.Codec<PagePredicate> CODEC = com.mojang.serialization.Codec.STRING.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(Filterable<String> value) {
/* 22 */     return ((String)value.raw()).equals(this.contents);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */