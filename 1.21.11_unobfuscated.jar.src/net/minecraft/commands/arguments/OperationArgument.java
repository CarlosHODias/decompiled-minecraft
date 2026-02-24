/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ 
/*     */ public class OperationArgument implements ArgumentType<OperationArgument.Operation> {
/*  21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "=", ">", "<" });
/*  22 */   private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType((Message)Component.translatable("arguments.operation.invalid"));
/*  23 */   private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType((Message)Component.translatable("arguments.operation.div0"));
/*     */   
/*     */   public static OperationArgument operation() {
/*  26 */     return new OperationArgument();
/*     */   }
/*     */   
/*     */   public static Operation getOperation(CommandContext<CommandSourceStack> context, String name) {
/*  30 */     return (Operation)context.getArgument(name, Operation.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Operation parse(StringReader reader) throws CommandSyntaxException {
/*  35 */     if (reader.canRead()) {
/*  36 */       int start = reader.getCursor();
/*  37 */       while (reader.canRead() && reader.peek() != ' ') {
/*  38 */         reader.skip();
/*     */       }
/*  40 */       return getOperation(reader.getString().substring(start, reader.getCursor()));
/*     */     } 
/*     */     
/*  43 */     throw ERROR_INVALID_OPERATION.createWithContext(reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/*  48 */     return SharedSuggestionProvider.suggest(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  53 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   private static Operation getOperation(String op) throws CommandSyntaxException {
/*  57 */     if (op.equals("><")) {
/*  58 */       return (a, b) -> {
/*     */           int swap = a.get();
/*     */           
/*     */           a.set(b.get());
/*     */           b.set(swap);
/*     */         };
/*     */     }
/*  65 */     return getSimpleOperation(op);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SimpleOperation getSimpleOperation(String op) throws CommandSyntaxException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: astore_1
/*     */     //   2: iconst_m1
/*     */     //   3: istore_2
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual hashCode : ()I
/*     */     //   8: lookupswitch default -> 195, 60 -> 168, 61 -> 84, 62 -> 183, 1208 -> 154, 1363 -> 126, 1394 -> 98, 1456 -> 112, 1518 -> 140
/*     */     //   84: aload_1
/*     */     //   85: ldc '='
/*     */     //   87: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   90: ifeq -> 195
/*     */     //   93: iconst_0
/*     */     //   94: istore_2
/*     */     //   95: goto -> 195
/*     */     //   98: aload_1
/*     */     //   99: ldc '+='
/*     */     //   101: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   104: ifeq -> 195
/*     */     //   107: iconst_1
/*     */     //   108: istore_2
/*     */     //   109: goto -> 195
/*     */     //   112: aload_1
/*     */     //   113: ldc '-='
/*     */     //   115: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   118: ifeq -> 195
/*     */     //   121: iconst_2
/*     */     //   122: istore_2
/*     */     //   123: goto -> 195
/*     */     //   126: aload_1
/*     */     //   127: ldc '*='
/*     */     //   129: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   132: ifeq -> 195
/*     */     //   135: iconst_3
/*     */     //   136: istore_2
/*     */     //   137: goto -> 195
/*     */     //   140: aload_1
/*     */     //   141: ldc '/='
/*     */     //   143: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   146: ifeq -> 195
/*     */     //   149: iconst_4
/*     */     //   150: istore_2
/*     */     //   151: goto -> 195
/*     */     //   154: aload_1
/*     */     //   155: ldc '%='
/*     */     //   157: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   160: ifeq -> 195
/*     */     //   163: iconst_5
/*     */     //   164: istore_2
/*     */     //   165: goto -> 195
/*     */     //   168: aload_1
/*     */     //   169: ldc '<'
/*     */     //   171: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   174: ifeq -> 195
/*     */     //   177: bipush #6
/*     */     //   179: istore_2
/*     */     //   180: goto -> 195
/*     */     //   183: aload_1
/*     */     //   184: ldc '>'
/*     */     //   186: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   189: ifeq -> 195
/*     */     //   192: bipush #7
/*     */     //   194: istore_2
/*     */     //   195: iload_2
/*     */     //   196: tableswitch default -> 308, 0 -> 244, 1 -> 252, 2 -> 260, 3 -> 268, 4 -> 276, 5 -> 284, 6 -> 292, 7 -> 300
/*     */     //   244: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   249: goto -> 315
/*     */     //   252: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   257: goto -> 315
/*     */     //   260: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   265: goto -> 315
/*     */     //   268: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   273: goto -> 315
/*     */     //   276: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   281: goto -> 315
/*     */     //   284: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   289: goto -> 315
/*     */     //   292: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   297: goto -> 315
/*     */     //   300: <illegal opcode> apply : ()Lnet/minecraft/commands/arguments/OperationArgument$SimpleOperation;
/*     */     //   305: goto -> 315
/*     */     //   308: getstatic net/minecraft/commands/arguments/OperationArgument.ERROR_INVALID_OPERATION : Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
/*     */     //   311: invokevirtual create : ()Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   314: athrow
/*     */     //   315: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #69	-> 0
/*     */     //   #70	-> 244
/*     */     //   #71	-> 252
/*     */     //   #72	-> 260
/*     */     //   #73	-> 268
/*     */     //   #74	-> 276
/*     */     //   #80	-> 284
/*     */     //   #86	-> 292
/*     */     //   #87	-> 300
/*     */     //   #88	-> 308
/*     */     //   #69	-> 315
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	316	0	op	Ljava/lang/String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Operation
/*     */   {
/*     */     void apply(ScoreAccess param1ScoreAccess1, ScoreAccess param1ScoreAccess2) throws CommandSyntaxException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface SimpleOperation
/*     */     extends Operation
/*     */   {
/*     */     int apply(int param1Int1, int param1Int2) throws CommandSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default void apply(ScoreAccess a, ScoreAccess b) throws CommandSyntaxException {
/* 103 */       a.set(apply(a.get(), b.get()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/OperationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */