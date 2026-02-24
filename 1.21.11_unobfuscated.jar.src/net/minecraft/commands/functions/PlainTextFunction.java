/*    */ package net.minecraft.commands.functions;
/*    */ public final class PlainTextFunction<T> extends Record implements CommandFunction<T>, InstantiatedFunction<T> { private final net.minecraft.resources.Identifier id; private final java.util.List<net.minecraft.commands.execution.UnboundEntryAction<T>> entries;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/functions/PlainTextFunction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/functions/PlainTextFunction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>;
/*    */   }
/*    */   
/* 12 */   public PlainTextFunction(net.minecraft.resources.Identifier id, java.util.List<net.minecraft.commands.execution.UnboundEntryAction<T>> entries) { this.id = id; this.entries = entries; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/functions/PlainTextFunction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>; } public net.minecraft.resources.Identifier id() { return this.id; } public java.util.List<net.minecraft.commands.execution.UnboundEntryAction<T>> entries() { return this.entries; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InstantiatedFunction<T> instantiate(net.minecraft.nbt.CompoundTag arguments, com.mojang.brigadier.CommandDispatcher<T> dispatcher) throws net.minecraft.commands.FunctionInstantiationException {
/* 20 */     return this;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/functions/PlainTextFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */