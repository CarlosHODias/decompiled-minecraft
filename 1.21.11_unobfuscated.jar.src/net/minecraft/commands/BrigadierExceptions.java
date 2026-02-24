/*     */ package net.minecraft.commands;
/*     */ 
/*     */ public class BrigadierExceptions implements com.mojang.brigadier.exceptions.BuiltInExceptionProvider {
/*     */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType DOUBLE_TOO_SMALL;
/*     */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType DOUBLE_TOO_BIG;
/*     */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType FLOAT_TOO_SMALL;
/*     */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType FLOAT_TOO_BIG;
/*     */   
/*     */   static {
/*  10 */     DOUBLE_TOO_SMALL = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, min) -> net.minecraft.network.chat.Component.translatableEscape("argument.double.low", new Object[] { min, found }));
/*  11 */     DOUBLE_TOO_BIG = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, max) -> net.minecraft.network.chat.Component.translatableEscape("argument.double.big", new Object[] { max, found }));
/*     */     
/*  13 */     FLOAT_TOO_SMALL = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, min) -> net.minecraft.network.chat.Component.translatableEscape("argument.float.low", new Object[] { min, found }));
/*  14 */     FLOAT_TOO_BIG = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, max) -> net.minecraft.network.chat.Component.translatableEscape("argument.float.big", new Object[] { max, found }));
/*     */     
/*  16 */     INTEGER_TOO_SMALL = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, min) -> net.minecraft.network.chat.Component.translatableEscape("argument.integer.low", new Object[] { min, found }));
/*  17 */     INTEGER_TOO_BIG = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, max) -> net.minecraft.network.chat.Component.translatableEscape("argument.integer.big", new Object[] { max, found }));
/*     */     
/*  19 */     LONG_TOO_SMALL = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, min) -> net.minecraft.network.chat.Component.translatableEscape("argument.long.low", new Object[] { min, found }));
/*  20 */     LONG_TOO_BIG = new com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType((found, max) -> net.minecraft.network.chat.Component.translatableEscape("argument.long.big", new Object[] { max, found }));
/*     */     
/*  22 */     LITERAL_INCORRECT = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(expected -> net.minecraft.network.chat.Component.translatableEscape("argument.literal.incorrect", new Object[] { expected }));
/*     */   }
/*  24 */   private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType INTEGER_TOO_SMALL; private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType INTEGER_TOO_BIG; private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType LONG_TOO_SMALL; private static final com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType LONG_TOO_BIG; private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType LITERAL_INCORRECT; private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.quote.expected.start"));
/*  25 */   private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.quote.expected.end")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_ESCAPE; private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_BOOL; private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_INT; static {
/*  26 */     READER_INVALID_ESCAPE = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(character -> net.minecraft.network.chat.Component.translatableEscape("parsing.quote.escape", new Object[] { character }));
/*  27 */     READER_INVALID_BOOL = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(value -> net.minecraft.network.chat.Component.translatableEscape("parsing.bool.invalid", new Object[] { value }));
/*  28 */     READER_INVALID_INT = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(value -> net.minecraft.network.chat.Component.translatableEscape("parsing.int.invalid", new Object[] { value }));
/*  29 */   } private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_INT = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.int.expected")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_LONG; static {
/*  30 */     READER_INVALID_LONG = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(value -> net.minecraft.network.chat.Component.translatableEscape("parsing.long.invalid", new Object[] { value }));
/*  31 */   } private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_LONG = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.long.expected")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_DOUBLE; static {
/*  32 */     READER_INVALID_DOUBLE = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(value -> net.minecraft.network.chat.Component.translatableEscape("parsing.double.invalid", new Object[] { value }));
/*  33 */   } private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.double.expected")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_INVALID_FLOAT; static {
/*  34 */     READER_INVALID_FLOAT = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(value -> net.minecraft.network.chat.Component.translatableEscape("parsing.float.invalid", new Object[] { value }));
/*  35 */   } private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_FLOAT = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.float.expected"));
/*  36 */   private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType READER_EXPECTED_BOOL = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("parsing.bool.expected")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType READER_EXPECTED_SYMBOL; static {
/*  37 */     READER_EXPECTED_SYMBOL = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(symbol -> net.minecraft.network.chat.Component.translatableEscape("parsing.expected", new Object[] { symbol }));
/*     */   }
/*  39 */   private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("command.unknown.command"));
/*  40 */   private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("command.unknown.argument"));
/*  41 */   private static final com.mojang.brigadier.exceptions.SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new com.mojang.brigadier.exceptions.SimpleCommandExceptionType((com.mojang.brigadier.Message)net.minecraft.network.chat.Component.translatable("command.expected.separator")); private static final com.mojang.brigadier.exceptions.DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION; static {
/*  42 */     DISPATCHER_PARSE_EXCEPTION = new com.mojang.brigadier.exceptions.DynamicCommandExceptionType(message -> net.minecraft.network.chat.Component.translatableEscape("command.exception", new Object[] { message }));
/*     */   }
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType doubleTooLow() {
/*  46 */     return DOUBLE_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType doubleTooHigh() {
/*  51 */     return DOUBLE_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType floatTooLow() {
/*  56 */     return FLOAT_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType floatTooHigh() {
/*  61 */     return FLOAT_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType integerTooLow() {
/*  66 */     return INTEGER_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType integerTooHigh() {
/*  71 */     return INTEGER_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType longTooLow() {
/*  76 */     return LONG_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType longTooHigh() {
/*  81 */     return LONG_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType literalIncorrect() {
/*  86 */     return LITERAL_INCORRECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedStartOfQuote() {
/*  91 */     return READER_EXPECTED_START_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedEndOfQuote() {
/*  96 */     return READER_EXPECTED_END_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidEscape() {
/* 101 */     return READER_INVALID_ESCAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidBool() {
/* 106 */     return READER_INVALID_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidInt() {
/* 111 */     return READER_INVALID_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedInt() {
/* 116 */     return READER_EXPECTED_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidLong() {
/* 121 */     return READER_INVALID_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedLong() {
/* 126 */     return READER_EXPECTED_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidDouble() {
/* 131 */     return READER_INVALID_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedDouble() {
/* 136 */     return READER_EXPECTED_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerInvalidFloat() {
/* 141 */     return READER_INVALID_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedFloat() {
/* 146 */     return READER_EXPECTED_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType readerExpectedBool() {
/* 151 */     return READER_EXPECTED_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType readerExpectedSymbol() {
/* 156 */     return READER_EXPECTED_SYMBOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType dispatcherUnknownCommand() {
/* 161 */     return DISPATCHER_UNKNOWN_COMMAND;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType dispatcherUnknownArgument() {
/* 166 */     return DISPATCHER_UNKNOWN_ARGUMENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
/* 171 */     return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.brigadier.exceptions.DynamicCommandExceptionType dispatcherParseException() {
/* 176 */     return DISPATCHER_PARSE_EXCEPTION;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/BrigadierExceptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */