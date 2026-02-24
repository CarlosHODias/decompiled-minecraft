/*    */ package net.minecraft.client.renderer.block.model.multipart;
/*    */ public final class CombinedCondition extends Record implements Condition { private final Operation operation;
/*    */   private final java.util.List<Condition> terms;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;
/*    */   }
/*    */   
/* 13 */   public CombinedCondition(Operation operation, java.util.List<Condition> terms) { this.operation = operation; this.terms = terms; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/CombinedCondition;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Operation operation() { return this.operation; } public java.util.List<Condition> terms() { return this.terms; }
/*    */ 
/*    */   
/*    */   public enum Operation
/*    */     implements net.minecraft.util.StringRepresentable
/*    */   {
/* 19 */     AND("AND")
/*    */     {
/*    */       public <V> java.util.function.Predicate<V> apply(java.util.List<java.util.function.Predicate<V>> terms) {
/* 22 */         return net.minecraft.util.Util.allOf(terms);
/*    */       }
/*    */     },
/* 25 */     OR("OR")
/*    */     {
/*    */       public <V> java.util.function.Predicate<V> apply(java.util.List<java.util.function.Predicate<V>> terms) {
/* 28 */         return net.minecraft.util.Util.anyOf(terms);
/*    */       }
/*    */     };
/*    */ 
/*    */     
/* 33 */     public static final com.mojang.serialization.Codec<Operation> CODEC = (com.mojang.serialization.Codec<Operation>)net.minecraft.util.StringRepresentable.fromEnum(Operation::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     Operation(String name) {
/* 38 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 43 */       return this.name;
/*    */     } public abstract <V> java.util.function.Predicate<V> apply(java.util.List<java.util.function.Predicate<V>> param1List);
/*    */   } enum null { public <V> java.util.function.Predicate<V> apply(java.util.List<java.util.function.Predicate<V>> terms) {
/*    */       return net.minecraft.util.Util.allOf(terms);
/*    */     } } enum null { public <V> java.util.function.Predicate<V> apply(java.util.List<java.util.function.Predicate<V>> terms) {
/*    */       return net.minecraft.util.Util.anyOf(terms);
/*    */     } }
/*    */   public <O, S extends net.minecraft.world.level.block.state.StateHolder<O, S>> java.util.function.Predicate<S> instantiate(net.minecraft.world.level.block.state.StateDefinition<O, S> definition) {
/* 51 */     return this.operation.apply(com.google.common.collect.Lists.transform(this.terms, c -> c.instantiate(definition)));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/multipart/CombinedCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */