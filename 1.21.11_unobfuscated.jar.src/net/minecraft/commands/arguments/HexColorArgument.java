/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HexColorArgument
/*    */   implements ArgumentType<Integer> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "F00", "FF0000" }); static {
/* 21 */     ERROR_INVALID_HEX = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.hexcolor.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_HEX;
/*    */   
/*    */   public static HexColorArgument hexColor() {
/* 27 */     return new HexColorArgument();
/*    */   }
/*    */   
/*    */   public static Integer getHexColor(CommandContext<CommandSourceStack> context, String name) {
/* 31 */     return (Integer)context.getArgument(name, Integer.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: invokevirtual readUnquotedString : ()Ljava/lang/String;
/*    */     //   4: astore_2
/*    */     //   5: aload_2
/*    */     //   6: invokevirtual length : ()I
/*    */     //   9: lookupswitch default -> 112, 3 -> 36, 6 -> 78
/*    */     //   36: aload_2
/*    */     //   37: iconst_0
/*    */     //   38: iconst_1
/*    */     //   39: bipush #16
/*    */     //   41: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   44: invokestatic duplicateDigit : (I)I
/*    */     //   47: aload_2
/*    */     //   48: iconst_1
/*    */     //   49: iconst_2
/*    */     //   50: bipush #16
/*    */     //   52: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   55: invokestatic duplicateDigit : (I)I
/*    */     //   58: aload_2
/*    */     //   59: iconst_2
/*    */     //   60: iconst_3
/*    */     //   61: bipush #16
/*    */     //   63: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   66: invokestatic duplicateDigit : (I)I
/*    */     //   69: invokestatic color : (III)I
/*    */     //   72: invokestatic valueOf : (I)Ljava/lang/Integer;
/*    */     //   75: goto -> 121
/*    */     //   78: aload_2
/*    */     //   79: iconst_0
/*    */     //   80: iconst_2
/*    */     //   81: bipush #16
/*    */     //   83: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   86: aload_2
/*    */     //   87: iconst_2
/*    */     //   88: iconst_4
/*    */     //   89: bipush #16
/*    */     //   91: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   94: aload_2
/*    */     //   95: iconst_4
/*    */     //   96: bipush #6
/*    */     //   98: bipush #16
/*    */     //   100: invokestatic parseInt : (Ljava/lang/CharSequence;III)I
/*    */     //   103: invokestatic color : (III)I
/*    */     //   106: invokestatic valueOf : (I)Ljava/lang/Integer;
/*    */     //   109: goto -> 121
/*    */     //   112: getstatic net/minecraft/commands/arguments/HexColorArgument.ERROR_INVALID_HEX : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*    */     //   115: aload_1
/*    */     //   116: aload_2
/*    */     //   117: invokevirtual createWithContext : (Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*    */     //   120: athrow
/*    */     //   121: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     //   #38	-> 5
/*    */     //   #39	-> 36
/*    */     //   #40	-> 41
/*    */     //   #41	-> 52
/*    */     //   #42	-> 63
/*    */     //   #39	-> 69
/*    */     //   #44	-> 78
/*    */     //   #45	-> 83
/*    */     //   #46	-> 91
/*    */     //   #47	-> 100
/*    */     //   #44	-> 103
/*    */     //   #49	-> 112
/*    */     //   #38	-> 121
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	122	0	this	Lnet/minecraft/commands/arguments/HexColorArgument;
/*    */     //   0	122	1	reader	Lcom/mojang/brigadier/StringReader;
/*    */     //   5	117	2	colorString	Ljava/lang/String;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int duplicateDigit(int digit) {
/* 54 */     return digit * 17;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 59 */     return SharedSuggestionProvider.suggest(EXAMPLES, builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 64 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/HexColorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */