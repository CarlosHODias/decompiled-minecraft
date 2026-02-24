/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.chat.SignableCommand;
/*    */ 
/*    */ public final class ArgumentSignatures extends Record {
/*    */   private final List<Entry> entries;
/*    */   
/* 12 */   public ArgumentSignatures(List<Entry> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ArgumentSignatures;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures; } public List<Entry> entries() { return this.entries; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ArgumentSignatures;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ArgumentSignatures;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public static final ArgumentSignatures EMPTY = new ArgumentSignatures(List.of());
/*    */   
/*    */   private static final int MAX_ARGUMENT_COUNT = 8;
/*    */   private static final int MAX_ARGUMENT_NAME_LENGTH = 16;
/*    */   
/*    */   public ArgumentSignatures(FriendlyByteBuf input) {
/* 19 */     this((List<Entry>)input.readCollection(FriendlyByteBuf.limitValue(java.util.ArrayList::new, 8), Entry::new));
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 23 */     output.writeCollection(this.entries, (out, entry) -> entry.write(out));
/*    */   }
/*    */   
/*    */   public static ArgumentSignatures signCommand(SignableCommand<?> command, Signer signer) {
/* 27 */     List<Entry> entries = command.arguments().stream()
/* 28 */       .map(argument -> {
/*    */           MessageSignature signature = signer.sign(argument.value());
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           return (signature != null) ? new Entry(argument.name(), signature) : null;
/* 35 */         }).filter(java.util.Objects::nonNull)
/* 36 */       .toList();
/*    */     
/* 38 */     return new ArgumentSignatures(entries);
/*    */   }
/*    */   @FunctionalInterface
/*    */   public static interface Signer {
/*    */     MessageSignature sign(String param1String); }
/*    */   public static final class Entry extends Record { private final String name;
/*    */     private final MessageSignature signature;
/*    */     
/* 46 */     public Entry(String name, MessageSignature signature) { this.name = name; this.signature = signature; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ArgumentSignatures$Entry;
/* 46 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public MessageSignature signature() { return this.signature; }
/*    */      public Entry(FriendlyByteBuf input) {
/* 48 */       this(input.readUtf(16), MessageSignature.read(input));
/*    */     }
/*    */     
/*    */     public void write(FriendlyByteBuf output) {
/* 52 */       output.writeUtf(this.name, 16);
/* 53 */       MessageSignature.write(output, this.signature);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/ArgumentSignatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */