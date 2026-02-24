/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CommandFunction<T>
/*     */ {
/*     */   Identifier id();
/*     */   
/*     */   InstantiatedFunction<T> instantiate(CompoundTag paramCompoundTag, CommandDispatcher<T> paramCommandDispatcher) throws FunctionInstantiationException;
/*     */   
/*     */   private static boolean shouldConcatenateNextLine(CharSequence line) {
/*  28 */     int length = line.length();
/*  29 */     return (length > 0 && line.charAt(length - 1) == '\\');
/*     */   }
/*     */   
/*     */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> CommandFunction<T> fromLines(Identifier id, CommandDispatcher<T> dispatcher, T compilationContext, List<String> lines) {
/*  33 */     FunctionBuilder<T> functionBuilder = new FunctionBuilder<>();
/*  34 */     int i = 0; while (true) { if (i < lines.size())
/*  35 */       { int lineNumber = i + 1;
/*     */ 
/*     */         
/*  38 */         String inputLine = ((String)lines.get(i)).trim();
/*     */         
/*  40 */         if (shouldConcatenateNextLine(inputLine))
/*  41 */         { StringBuilder builder = new StringBuilder(inputLine);
/*     */           for (;; i++)
/*  43 */           { i++;
/*  44 */             if (i == lines.size()) {
/*  45 */               throw new IllegalArgumentException("Line continuation at end of file");
/*     */             }
/*  47 */             builder.deleteCharAt(builder.length() - 1);
/*  48 */             String innerLine = ((String)lines.get(i)).trim();
/*  49 */             builder.append(innerLine);
/*  50 */             checkCommandLineLength(builder);
/*  51 */             if (!shouldConcatenateNextLine(builder))
/*  52 */             { String line = builder.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  57 */               checkCommandLineLength(line);
/*     */               
/*  59 */               StringReader input = new StringReader(line);
/*     */               
/*  61 */               if (!input.canRead() || input.peek() == '#') {
/*     */                 break;
/*     */               }
/*     */               
/*  65 */               if (input.peek() == '/') {
/*  66 */                 input.skip();
/*  67 */                 if (input.peek() == '/') {
/*  68 */                   throw new IllegalArgumentException("Unknown or invalid command '" + line + "' on line " + lineNumber + " (if you intended to make a comment, use '#' not '//')");
/*     */                 }
/*  70 */                 String name = input.readUnquotedString();
/*  71 */                 throw new IllegalArgumentException("Unknown or invalid command '" + line + "' on line " + lineNumber + " (did you mean '" + name + "'? Do not use a preceding forwards slash.)");
/*     */               } 
/*  73 */               if (input.peek() == '$')
/*     */               
/*  75 */               { functionBuilder.addMacro(line.substring(1), lineNumber, compilationContext); }
/*     */               else
/*     */               { 
/*  78 */                 try { functionBuilder.addCommand(parseCommand(dispatcher, compilationContext, input)); }
/*  79 */                 catch (CommandSyntaxException e)
/*  80 */                 { throw new IllegalArgumentException("Whilst parsing command on line " + lineNumber + ": " + e.getMessage()); }  }  } else { continue; }  }  }
/*     */         else { String line = inputLine; continue; }
/*     */          }
/*     */       else { break; }
/*     */        i++; }
/*  85 */      return functionBuilder.build(id);
/*     */   }
/*     */   
/*     */   static void checkCommandLineLength(CharSequence line) {
/*  89 */     if (line.length() > 2000000) {
/*  90 */       CharSequence truncated = line.subSequence(0, Math.min(512, 2000000));
/*  91 */       throw new IllegalStateException("Command too long: " + line.length() + " characters, contents: " + String.valueOf(truncated) + "...");
/*     */     } 
/*     */   }
/*     */   
/*     */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> UnboundEntryAction<T> parseCommand(CommandDispatcher<T> dispatcher, T compilationContext, StringReader input) throws CommandSyntaxException {
/*  96 */     ParseResults<T> parse = dispatcher.parse(input, compilationContext);
/*  97 */     Commands.validateParseResults(parse);
/*     */     
/*  99 */     Optional<ContextChain<T>> commandChain = ContextChain.tryFlatten(parse.getContext().build(input.getString()));
/* 100 */     if (commandChain.isEmpty()) {
/* 101 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
/*     */     }
/* 103 */     return (UnboundEntryAction<T>)new BuildContexts.Unbound(input.getString(), commandChain.get());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/functions/CommandFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */