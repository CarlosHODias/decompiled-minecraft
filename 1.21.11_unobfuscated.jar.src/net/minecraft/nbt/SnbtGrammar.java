/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import it.unimi.dsi.fastutil.chars.CharList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.DelayedException;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ import net.minecraft.util.parsing.packrat.Rule;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.GreedyPatternParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.GreedyPredicateParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.NumberRunParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*     */ import net.minecraft.util.parsing.packrat.commands.UnquotedStringParseRule;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SnbtGrammar
/*     */ {
/*     */   private static final DynamicCommandExceptionType ERROR_NUMBER_PARSE_FAILURE;
/*     */   private static final DynamicCommandExceptionType ERROR_EXPECTED_HEX_ESCAPE;
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_CODEPOINT;
/*     */   private static final DynamicCommandExceptionType ERROR_NO_SUCH_OPERATION;
/*     */   
/*     */   static {
/*  66 */     ERROR_NUMBER_PARSE_FAILURE = new DynamicCommandExceptionType(message -> Component.translatableEscape("snbt.parser.number_parse_failure", new Object[] { message }));
/*  67 */     ERROR_EXPECTED_HEX_ESCAPE = new DynamicCommandExceptionType(length -> Component.translatableEscape("snbt.parser.expected_hex_escape", new Object[] { length }));
/*  68 */     ERROR_INVALID_CODEPOINT = new DynamicCommandExceptionType(codepoint -> Component.translatableEscape("snbt.parser.invalid_codepoint", new Object[] { codepoint }));
/*  69 */     ERROR_NO_SUCH_OPERATION = new DynamicCommandExceptionType(operation -> Component.translatableEscape("snbt.parser.no_such_operation", new Object[] { operation }));
/*     */   }
/*  71 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_INTEGER_TYPE = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_integer_type")));
/*  72 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_FLOAT_TYPE = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_float_type")));
/*  73 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_NON_NEGATIVE_NUMBER = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_non_negative_number")));
/*  74 */   private static final DelayedException<CommandSyntaxException> ERROR_INVALID_CHARACTER_NAME = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.invalid_character_name")));
/*  75 */   private static final DelayedException<CommandSyntaxException> ERROR_INVALID_ARRAY_ELEMENT_TYPE = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.invalid_array_element_type")));
/*  76 */   private static final DelayedException<CommandSyntaxException> ERROR_INVALID_UNQUOTED_START = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.invalid_unquoted_start")));
/*  77 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_UNQUOTED_STRING = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_unquoted_string")));
/*  78 */   private static final DelayedException<CommandSyntaxException> ERROR_INVALID_STRING_CONTENTS = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.invalid_string_contents")));
/*  79 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_BINARY_NUMERAL = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_binary_numeral")));
/*  80 */   private static final DelayedException<CommandSyntaxException> ERROR_UNDESCORE_NOT_ALLOWED = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.underscore_not_allowed")));
/*  81 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_DECIMAL_NUMERAL = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_decimal_numeral")));
/*  82 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_HEX_NUMERAL = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.expected_hex_numeral")));
/*  83 */   private static final DelayedException<CommandSyntaxException> ERROR_EMPTY_KEY = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.empty_key")));
/*  84 */   private static final DelayedException<CommandSyntaxException> ERROR_LEADING_ZERO_NOT_ALLOWED = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.leading_zero_not_allowed")));
/*  85 */   private static final DelayedException<CommandSyntaxException> ERROR_INFINITY_NOT_ALLOWED = DelayedException.create(new SimpleCommandExceptionType((Message)Component.translatable("snbt.parser.infinity_not_allowed")));
/*     */   
/*  87 */   private static final HexFormat HEX_ESCAPE = HexFormat.of().withUpperCase();
/*     */   
/*     */   private static DelayedException<CommandSyntaxException> createNumberParseError(NumberFormatException ex) {
/*  90 */     return DelayedException.create(ERROR_NUMBER_PARSE_FAILURE, ex.getMessage());
/*     */   }
/*     */   
/*     */   public static String escapeControlCharacters(char c) {
/*  94 */     switch (c) { case '\b': 
/*     */       case '\t': 
/*     */       case '\n': 
/*     */       case '\f': 
/*     */       case '\r':
/*     */       
/*     */       default:
/* 101 */         if (c < ' '); break; }  return null;
/*     */   }
/*     */   
/*     */   private enum Sign
/*     */   {
/* 106 */     PLUS,
/* 107 */     MINUS;
/*     */ 
/*     */     
/*     */     public void append(StringBuilder output) {
/* 111 */       if (this == MINUS)
/* 112 */         output.append("-"); 
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Base
/*     */   {
/* 118 */     BINARY,
/* 119 */     DECIMAL,
/* 120 */     HEX;
/*     */   }
/*     */   
/*     */   private enum TypeSuffix {
/* 124 */     FLOAT,
/* 125 */     DOUBLE,
/* 126 */     BYTE,
/* 127 */     SHORT,
/* 128 */     INT,
/* 129 */     LONG;
/*     */   }
/*     */   
/*     */   private enum SignedPrefix {
/* 133 */     SIGNED,
/* 134 */     UNSIGNED; }
/*     */   private static final class IntegerSuffix extends Record { private final SnbtGrammar.SignedPrefix signed; private final SnbtGrammar.TypeSuffix type;
/*     */     
/* 137 */     private IntegerSuffix(SnbtGrammar.SignedPrefix signed, SnbtGrammar.TypeSuffix type) { this.signed = signed; this.type = type; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;
/* 137 */       //   0	8	1	o	Ljava/lang/Object; } public SnbtGrammar.SignedPrefix signed() { return this.signed; } public SnbtGrammar.TypeSuffix type() { return this.type; }
/*     */ 
/*     */ 
/*     */     
/* 141 */     public static final IntegerSuffix EMPTY = new IntegerSuffix(null, null); }
/*     */ 
/*     */   
/*     */   private enum ArrayPrefix {
/* 145 */     BYTE(SnbtGrammar.TypeSuffix.BYTE, new SnbtGrammar.TypeSuffix[0]) {
/* 146 */       private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
/*     */ 
/*     */       
/*     */       public <T> T create(DynamicOps<T> ops) {
/* 150 */         return (T)ops.createByteList(EMPTY_BUFFER);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 155 */         ByteArrayList byteArrayList = new ByteArrayList();
/* 156 */         for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 157 */           Number parsedNumber = buildNumber(entry, state);
/* 158 */           if (parsedNumber == null) {
/* 159 */             return null;
/*     */           }
/* 161 */           byteArrayList.add(parsedNumber.byteValue());
/*     */         } 
/* 163 */         return (T)ops.createByteList(ByteBuffer.wrap(byteArrayList.toByteArray()));
/*     */       }
/*     */     },
/* 166 */     INT(SnbtGrammar.TypeSuffix.INT, new SnbtGrammar.TypeSuffix[] { SnbtGrammar.TypeSuffix.BYTE, SnbtGrammar.TypeSuffix.SHORT })
/*     */     {
/*     */       public <T> T create(DynamicOps<T> ops) {
/* 169 */         return (T)ops.createIntList(IntStream.empty());
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 174 */         IntStream.Builder result = IntStream.builder();
/* 175 */         for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 176 */           Number parsedNumber = buildNumber(entry, state);
/* 177 */           if (parsedNumber == null) {
/* 178 */             return null;
/*     */           }
/* 180 */           result.add(parsedNumber.intValue());
/*     */         } 
/* 182 */         return (T)ops.createIntList(result.build());
/*     */       }
/*     */     },
/* 185 */     LONG(SnbtGrammar.TypeSuffix.LONG, new SnbtGrammar.TypeSuffix[] { SnbtGrammar.TypeSuffix.BYTE, SnbtGrammar.TypeSuffix.SHORT, SnbtGrammar.TypeSuffix.INT })
/*     */     {
/*     */       public <T> T create(DynamicOps<T> ops) {
/* 188 */         return (T)ops.createLongList(LongStream.empty());
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) {
/* 193 */         LongStream.Builder result = LongStream.builder();
/* 194 */         for (SnbtGrammar.IntegerLiteral entry : entries) {
/* 195 */           Number parsedNumber = buildNumber(entry, state);
/* 196 */           if (parsedNumber == null) {
/* 197 */             return null;
/*     */           }
/* 199 */           result.add(parsedNumber.longValue());
/*     */         } 
/* 201 */         return (T)ops.createLongList(result.build());
/*     */       }
/*     */     };
/*     */     
/*     */     private final SnbtGrammar.TypeSuffix defaultType;
/*     */     
/*     */     private final Set<SnbtGrammar.TypeSuffix> additionalTypes;
/*     */     
/*     */     ArrayPrefix(SnbtGrammar.TypeSuffix defaultType, SnbtGrammar.TypeSuffix... additionalTypes) {
/* 210 */       this.additionalTypes = Set.of(additionalTypes);
/* 211 */       this.defaultType = defaultType;
/*     */     }
/*     */     
/*     */     public boolean isAllowed(SnbtGrammar.TypeSuffix type) {
/* 215 */       return (type == this.defaultType || this.additionalTypes.contains(type));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Number buildNumber(SnbtGrammar.IntegerLiteral entry, ParseState<?> state) {
/* 223 */       SnbtGrammar.TypeSuffix actualType = computeType(entry.suffix);
/* 224 */       if (actualType == null) {
/* 225 */         state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_INVALID_ARRAY_ELEMENT_TYPE);
/* 226 */         return null;
/*     */       } 
/* 228 */       return entry.<Number>create((DynamicOps<Number>)JavaOps.INSTANCE, actualType, state);
/*     */     }
/*     */     
/*     */     private SnbtGrammar.TypeSuffix computeType(SnbtGrammar.IntegerSuffix value) {
/* 232 */       SnbtGrammar.TypeSuffix type = value.type();
/* 233 */       if (type == null) {
/* 234 */         return this.defaultType;
/*     */       }
/* 236 */       if (!isAllowed(type)) {
/* 237 */         return null;
/*     */       }
/* 239 */       return type;
/*     */     } public abstract <T> T create(DynamicOps<T> param1DynamicOps); public abstract <T> T create(DynamicOps<T> param1DynamicOps, List<SnbtGrammar.IntegerLiteral> param1List, ParseState<?> param1ParseState);
/*     */   } enum null { private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]); public <T> T create(DynamicOps<T> ops) { return (T)ops.createByteList(EMPTY_BUFFER); } public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) { ByteArrayList byteArrayList = new ByteArrayList(); for (SnbtGrammar.IntegerLiteral entry : entries) { Number parsedNumber = buildNumber(entry, state); if (parsedNumber == null)
/*     */           return null;  byteArrayList.add(parsedNumber.byteValue()); }  return (T)ops.createByteList(ByteBuffer.wrap(byteArrayList.toByteArray())); } }
/* 243 */   private static final NumberRunParseRule BINARY_NUMERAL = new NumberRunParseRule(ERROR_EXPECTED_BINARY_NUMERAL, ERROR_UNDESCORE_NOT_ALLOWED)
/*     */     {
/*     */       protected boolean isAccepted(char c) {
/* 246 */         switch (c) { case '0': case '1': case '_': default: break; }  return false;
/*     */       }
/*     */     }; enum null {
/*     */     public <T> T create(DynamicOps<T> ops) { return (T)ops.createIntList(IntStream.empty()); } public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) { IntStream.Builder result = IntStream.builder(); for (SnbtGrammar.IntegerLiteral entry : entries) {
/*     */         Number parsedNumber = buildNumber(entry, state); if (parsedNumber == null)
/*     */           return null;  result.add(parsedNumber.intValue());
/*     */       } 
/* 253 */       return (T)ops.createIntList(result.build()); } } private static final NumberRunParseRule DECIMAL_NUMERAL = new NumberRunParseRule(ERROR_EXPECTED_DECIMAL_NUMERAL, ERROR_UNDESCORE_NOT_ALLOWED)
/*     */     {
/*     */       protected boolean isAccepted(char c) {
/* 256 */         switch (c) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '_': default: break; }  return false;
/*     */       }
/*     */     }; enum null {
/*     */     public <T> T create(DynamicOps<T> ops) { return (T)ops.createLongList(LongStream.empty()); } public <T> T create(DynamicOps<T> ops, List<SnbtGrammar.IntegerLiteral> entries, ParseState<?> state) { LongStream.Builder result = LongStream.builder(); for (SnbtGrammar.IntegerLiteral entry : entries) {
/*     */         Number parsedNumber = buildNumber(entry, state); if (parsedNumber == null)
/*     */           return null;  result.add(parsedNumber.longValue());
/*     */       } 
/* 263 */       return (T)ops.createLongList(result.build()); } } private static final NumberRunParseRule HEX_NUMERAL = new NumberRunParseRule(ERROR_EXPECTED_HEX_NUMERAL, ERROR_UNDESCORE_NOT_ALLOWED)
/*     */     {
/*     */       protected boolean isAccepted(char c) {
/* 266 */         switch (c) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case '_': case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': default: break; }  return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SimpleHexLiteralParseRule
/*     */     extends GreedyPredicateParseRule
/*     */   {
/*     */     public SimpleHexLiteralParseRule(int size) {
/* 278 */       super(size, size, DelayedException.create(SnbtGrammar.ERROR_EXPECTED_HEX_ESCAPE, String.valueOf(size)));
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isAccepted(char c) {
/* 283 */       switch (c) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': default: break; }  return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 292 */   private static final GreedyPredicateParseRule PLAIN_STRING_CHUNK = new GreedyPredicateParseRule(1, ERROR_INVALID_STRING_CONTENTS)
/*     */     {
/*     */       protected boolean isAccepted(char c) {
/* 295 */         switch (c) { case '"': case '\'': case '\\': default: break; }  return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAllowedToStartUnquotedString(char c) {
/* 303 */     return !canStartNumber(c);
/*     */   }
/*     */   
/*     */   private static boolean canStartNumber(char c) {
/* 307 */     switch (c) { case '+': case '-': case '.': case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': default: break; }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   private static final StringReaderTerms.TerminalCharacters NUMBER_LOOKEAHEAD = new StringReaderTerms.TerminalCharacters(CharList.of())
/*     */     {
/*     */       protected boolean isAccepted(char c) {
/* 316 */         return SnbtGrammar.canStartNumber(c);
/*     */       }
/*     */     };
/*     */   
/* 320 */   private static final Pattern UNICODE_NAME = Pattern.compile("[-a-zA-Z0-9 ]+");
/*     */   
/*     */   private static boolean needsUnderscoreRemoval(String contents) {
/* 323 */     return (contents.indexOf('_') != -1);
/*     */   }
/*     */   
/*     */   private static void cleanAndAppend(StringBuilder output, String contents) {
/* 327 */     cleanAndAppend(output, contents, needsUnderscoreRemoval(contents));
/*     */   }
/*     */   
/*     */   private static void cleanAndAppend(StringBuilder output, String contents, boolean needsUnderscoreRemoval) {
/* 331 */     if (needsUnderscoreRemoval) {
/* 332 */       for (char c : contents.toCharArray()) {
/* 333 */         if (c != '_') {
/* 334 */           output.append(c);
/*     */         }
/*     */       } 
/*     */     } else {
/* 338 */       output.append(contents);
/*     */     } 
/*     */   }
/*     */   private static final class IntegerLiteral extends Record { private final SnbtGrammar.Sign sign; private final SnbtGrammar.Base base; private final String digits; private final SnbtGrammar.IntegerSuffix suffix;
/* 342 */     private IntegerLiteral(SnbtGrammar.Sign sign, SnbtGrammar.Base base, String digits, SnbtGrammar.IntegerSuffix suffix) { this.sign = sign; this.base = base; this.digits = digits; this.suffix = suffix; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;
/* 342 */       //   0	8	1	o	Ljava/lang/Object; } public SnbtGrammar.Sign sign() { return this.sign; } public SnbtGrammar.Base base() { return this.base; } public String digits() { return this.digits; } public SnbtGrammar.IntegerSuffix suffix() { return this.suffix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SnbtGrammar.SignedPrefix signedOrDefault() {
/* 349 */       if (this.suffix.signed != null) {
/* 350 */         return this.suffix.signed;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 355 */       switch (this.base.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  return 
/*     */         
/* 357 */         SnbtGrammar.SignedPrefix.SIGNED;
/*     */     }
/*     */ 
/*     */     
/*     */     private String cleanupDigits(SnbtGrammar.Sign sign) {
/* 362 */       boolean needsUnderscoreRemoval = SnbtGrammar.needsUnderscoreRemoval(this.digits);
/*     */       
/* 364 */       if (sign == SnbtGrammar.Sign.MINUS || needsUnderscoreRemoval) {
/* 365 */         StringBuilder result = new StringBuilder();
/* 366 */         sign.append(result);
/* 367 */         SnbtGrammar.cleanAndAppend(result, this.digits, needsUnderscoreRemoval);
/* 368 */         return result.toString();
/*     */       } 
/* 370 */       return this.digits;
/*     */     }
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops, ParseState<?> state) {
/* 374 */       return create(ops, Objects.<SnbtGrammar.TypeSuffix>requireNonNullElse(this.suffix.type, SnbtGrammar.TypeSuffix.INT), state);
/*     */     }
/*     */     
/*     */     public <T> T create(DynamicOps<T> ops, SnbtGrammar.TypeSuffix type, ParseState<?> state) {
/* 378 */       boolean isSigned = (signedOrDefault() == SnbtGrammar.SignedPrefix.SIGNED);
/* 379 */       if (!isSigned && this.sign == SnbtGrammar.Sign.MINUS) {
/*     */         
/* 381 */         state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_NON_NEGATIVE_NUMBER);
/* 382 */         return null;
/*     */       } 
/*     */       
/* 385 */       String fixedDigits = cleanupDigits(this.sign);
/* 386 */       switch (this.base.ordinal()) { default: throw new MatchException(null, null);
/*     */         case 0: 
/*     */         case 1: 
/* 389 */         case 2: break; }  int radix = 16;
/*     */ 
/*     */       
/*     */       try {
/* 393 */         if (isSigned) {
/* 394 */           switch (type.ordinal()) { case 2: 
/*     */             case 3: 
/*     */             case 4: 
/*     */             case 5:
/*     */             
/*     */             default:
/* 400 */               state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE); break; }
/* 401 */            return null;
/*     */         } 
/*     */ 
/*     */         
/* 405 */         switch (type.ordinal()) { case 2: 
/*     */           case 3: 
/*     */           case 4: 
/*     */           case 5:
/*     */           
/*     */           default:
/* 411 */             state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE); break; }
/* 412 */          return null;
/*     */ 
/*     */       
/*     */       }
/* 416 */       catch (NumberFormatException e) {
/*     */ 
/*     */         
/* 419 */         state.errorCollector().store(state.mark(), SnbtGrammar.createNumberParseError(e));
/* 420 */         return null;
/*     */       } 
/*     */     } }
/*     */   private static final class Signed<T> extends Record { private final SnbtGrammar.Sign sign; private final T value;
/*     */     
/* 425 */     private Signed(SnbtGrammar.Sign sign, T value) { this.sign = sign; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$Signed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$Signed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$Signed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 425 */       //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$Signed<TT;>; } public SnbtGrammar.Sign sign() { return this.sign; } public T value() { return this.value; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static short parseUnsignedShort(String string, int radix) {
/* 433 */     int parse = Integer.parseInt(string, radix);
/* 434 */     if (parse >> 16 == 0) {
/* 435 */       return (short)parse;
/*     */     }
/* 437 */     throw new NumberFormatException("out of range: " + parse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T createFloat(DynamicOps<T> ops, Sign sign, String whole, String fraction, Signed<String> exponent, TypeSuffix typeSuffix, ParseState<?> state) {
/*     */     // Byte code:
/*     */     //   0: new java/lang/StringBuilder
/*     */     //   3: dup
/*     */     //   4: invokespecial <init> : ()V
/*     */     //   7: astore #7
/*     */     //   9: aload_1
/*     */     //   10: aload #7
/*     */     //   12: invokevirtual append : (Ljava/lang/StringBuilder;)V
/*     */     //   15: aload_2
/*     */     //   16: ifnull -> 25
/*     */     //   19: aload #7
/*     */     //   21: aload_2
/*     */     //   22: invokestatic cleanAndAppend : (Ljava/lang/StringBuilder;Ljava/lang/String;)V
/*     */     //   25: aload_3
/*     */     //   26: ifnull -> 43
/*     */     //   29: aload #7
/*     */     //   31: bipush #46
/*     */     //   33: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   36: pop
/*     */     //   37: aload #7
/*     */     //   39: aload_3
/*     */     //   40: invokestatic cleanAndAppend : (Ljava/lang/StringBuilder;Ljava/lang/String;)V
/*     */     //   43: aload #4
/*     */     //   45: ifnull -> 79
/*     */     //   48: aload #7
/*     */     //   50: bipush #101
/*     */     //   52: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   55: pop
/*     */     //   56: aload #4
/*     */     //   58: invokevirtual sign : ()Lnet/minecraft/nbt/SnbtGrammar$Sign;
/*     */     //   61: aload #7
/*     */     //   63: invokevirtual append : (Ljava/lang/StringBuilder;)V
/*     */     //   66: aload #7
/*     */     //   68: aload #4
/*     */     //   70: getfield value : Ljava/lang/Object;
/*     */     //   73: checkcast java/lang/String
/*     */     //   76: invokestatic cleanAndAppend : (Ljava/lang/StringBuilder;Ljava/lang/String;)V
/*     */     //   79: aload #7
/*     */     //   81: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   84: astore #8
/*     */     //   86: aload #5
/*     */     //   88: astore #9
/*     */     //   90: iconst_0
/*     */     //   91: istore #10
/*     */     //   93: aload #9
/*     */     //   95: iload #10
/*     */     //   97: <illegal opcode> enumSwitch : (Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;I)I
/*     */     //   102: tableswitch default -> 170, -1 -> 156, 0 -> 128, 1 -> 142
/*     */     //   128: aload_0
/*     */     //   129: aload #6
/*     */     //   131: aload #8
/*     */     //   133: invokestatic convertFloat : (Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/parsing/packrat/ParseState;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   136: checkcast java/lang/Object
/*     */     //   139: goto -> 193
/*     */     //   142: aload_0
/*     */     //   143: aload #6
/*     */     //   145: aload #8
/*     */     //   147: invokestatic convertDouble : (Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/parsing/packrat/ParseState;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   150: checkcast java/lang/Object
/*     */     //   153: goto -> 193
/*     */     //   156: aload_0
/*     */     //   157: aload #6
/*     */     //   159: aload #8
/*     */     //   161: invokestatic convertDouble : (Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/parsing/packrat/ParseState;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   164: checkcast java/lang/Object
/*     */     //   167: goto -> 193
/*     */     //   170: aload #6
/*     */     //   172: invokeinterface errorCollector : ()Lnet/minecraft/util/parsing/packrat/ErrorCollector;
/*     */     //   177: aload #6
/*     */     //   179: invokeinterface mark : ()I
/*     */     //   184: getstatic net/minecraft/nbt/SnbtGrammar.ERROR_EXPECTED_FLOAT_TYPE : Lnet/minecraft/util/parsing/packrat/DelayedException;
/*     */     //   187: invokeinterface store : (ILjava/lang/Object;)V
/*     */     //   192: aconst_null
/*     */     //   193: areturn
/*     */     //   194: astore #8
/*     */     //   196: aload #6
/*     */     //   198: invokeinterface errorCollector : ()Lnet/minecraft/util/parsing/packrat/ErrorCollector;
/*     */     //   203: aload #6
/*     */     //   205: invokeinterface mark : ()I
/*     */     //   210: aload #8
/*     */     //   212: invokestatic createNumberParseError : (Ljava/lang/NumberFormatException;)Lnet/minecraft/util/parsing/packrat/DelayedException;
/*     */     //   215: invokeinterface store : (ILjava/lang/Object;)V
/*     */     //   220: aconst_null
/*     */     //   221: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #442	-> 0
/*     */     //   #444	-> 9
/*     */     //   #445	-> 15
/*     */     //   #446	-> 19
/*     */     //   #449	-> 25
/*     */     //   #450	-> 29
/*     */     //   #451	-> 37
/*     */     //   #454	-> 43
/*     */     //   #455	-> 48
/*     */     //   #456	-> 56
/*     */     //   #457	-> 66
/*     */     //   #461	-> 79
/*     */     //   #462	-> 86
/*     */     //   #463	-> 128
/*     */     //   #464	-> 142
/*     */     //   #465	-> 156
/*     */     //   #467	-> 170
/*     */     //   #468	-> 192
/*     */     //   #462	-> 193
/*     */     //   #471	-> 194
/*     */     //   #474	-> 196
/*     */     //   #475	-> 220
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   86	108	8	contents	Ljava/lang/String;
/*     */     //   196	26	8	e	Ljava/lang/NumberFormatException;
/*     */     //   0	222	0	ops	Lcom/mojang/serialization/DynamicOps;
/*     */     //   0	222	1	sign	Lnet/minecraft/nbt/SnbtGrammar$Sign;
/*     */     //   0	222	2	whole	Ljava/lang/String;
/*     */     //   0	222	3	fraction	Ljava/lang/String;
/*     */     //   0	222	4	exponent	Lnet/minecraft/nbt/SnbtGrammar$Signed;
/*     */     //   0	222	5	typeSuffix	Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   0	222	6	state	Lnet/minecraft/util/parsing/packrat/ParseState;
/*     */     //   9	213	7	result	Ljava/lang/StringBuilder;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	222	0	ops	Lcom/mojang/serialization/DynamicOps<TT;>;
/*     */     //   0	222	4	exponent	Lnet/minecraft/nbt/SnbtGrammar$Signed<Ljava/lang/String;>;
/*     */     //   0	222	6	state	Lnet/minecraft/util/parsing/packrat/ParseState<*>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   79	193	194	java/lang/NumberFormatException
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T convertFloat(DynamicOps<T> ops, ParseState<?> state, String contents) {
/* 480 */     float value = Float.parseFloat(contents);
/* 481 */     if (!Float.isFinite(value)) {
/* 482 */       state.errorCollector().store(state.mark(), ERROR_INFINITY_NOT_ALLOWED);
/* 483 */       return null;
/*     */     } 
/* 485 */     return (T)ops.createFloat(value);
/*     */   }
/*     */   
/*     */   private static <T> T convertDouble(DynamicOps<T> ops, ParseState<?> state, String contents) {
/* 489 */     double value = Double.parseDouble(contents);
/* 490 */     if (!Double.isFinite(value)) {
/* 491 */       state.errorCollector().store(state.mark(), ERROR_INFINITY_NOT_ALLOWED);
/* 492 */       return null;
/*     */     } 
/* 494 */     return (T)ops.createDouble(value);
/*     */   }
/*     */   
/*     */   private static String joinList(List<String> list) {
/* 498 */     switch (list.size()) { case 0: case 1: default: break; }  return 
/*     */ 
/*     */       
/* 501 */       String.join("", (Iterable)list);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Grammar<T> createParser(DynamicOps<T> ops) {
/* 506 */     T trueValue = (T)ops.createBoolean(true);
/* 507 */     T falseValue = (T)ops.createBoolean(false);
/* 508 */     T emptyMapValue = (T)ops.emptyMap();
/* 509 */     T emptyList = (T)ops.emptyList();
/*     */     
/* 511 */     Dictionary<StringReader> rules = new Dictionary();
/*     */     
/* 513 */     Atom<Sign> sign = Atom.of("sign");
/* 514 */     rules.put(sign, 
/* 515 */         Term.alternative(new Term[] {
/* 516 */             Term.sequence(new Term[] { StringReaderTerms.character('+'), Term.marker(sign, Sign.PLUS)
/* 517 */               }), Term.sequence(new Term[] { StringReaderTerms.character('-'), Term.marker(sign, Sign.MINUS) })
/*     */           }), scope -> (Sign)scope.getOrThrow(sign));
/*     */ 
/*     */     
/* 521 */     Atom<IntegerSuffix> integerSuffix = Atom.of("integer_suffix");
/* 522 */     rules.put(integerSuffix, 
/* 523 */         Term.alternative(new Term[] {
/* 524 */             Term.sequence(new Term[] {
/* 525 */                 StringReaderTerms.characters('u', 'U'), 
/* 526 */                 Term.alternative(new Term[] {
/* 527 */                     Term.sequence(new Term[] { StringReaderTerms.characters('b', 'B'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.UNSIGNED, TypeSuffix.BYTE))
/* 528 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('s', 'S'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.UNSIGNED, TypeSuffix.SHORT))
/* 529 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('i', 'I'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.UNSIGNED, TypeSuffix.INT))
/* 530 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('l', 'L'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.UNSIGNED, TypeSuffix.LONG))
/*     */                       })
/*     */                   })
/* 533 */               }), Term.sequence(new Term[] {
/* 534 */                 StringReaderTerms.characters('s', 'S'), 
/* 535 */                 Term.alternative(new Term[] {
/* 536 */                     Term.sequence(new Term[] { StringReaderTerms.characters('b', 'B'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.SIGNED, TypeSuffix.BYTE))
/* 537 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('s', 'S'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.SIGNED, TypeSuffix.SHORT))
/* 538 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('i', 'I'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.SIGNED, TypeSuffix.INT))
/* 539 */                       }), Term.sequence(new Term[] { StringReaderTerms.characters('l', 'L'), Term.marker(integerSuffix, new IntegerSuffix(SignedPrefix.SIGNED, TypeSuffix.LONG))
/*     */                       })
/*     */                   })
/* 542 */               }), Term.sequence(new Term[] { StringReaderTerms.characters('b', 'B'), Term.marker(integerSuffix, new IntegerSuffix(null, TypeSuffix.BYTE))
/* 543 */               }), Term.sequence(new Term[] { StringReaderTerms.characters('s', 'S'), Term.marker(integerSuffix, new IntegerSuffix(null, TypeSuffix.SHORT))
/* 544 */               }), Term.sequence(new Term[] { StringReaderTerms.characters('i', 'I'), Term.marker(integerSuffix, new IntegerSuffix(null, TypeSuffix.INT))
/* 545 */               }), Term.sequence(new Term[] { StringReaderTerms.characters('l', 'L'), Term.marker(integerSuffix, new IntegerSuffix(null, TypeSuffix.LONG)) })
/*     */           }), scope -> (IntegerSuffix)scope.getOrThrow(integerSuffix));
/*     */ 
/*     */ 
/*     */     
/* 550 */     Atom<String> binaryNumeral = Atom.of("binary_numeral");
/* 551 */     rules.put(binaryNumeral, (Rule)BINARY_NUMERAL);
/*     */     
/* 553 */     Atom<String> decimalNumeral = Atom.of("decimal_numeral");
/* 554 */     rules.put(decimalNumeral, (Rule)DECIMAL_NUMERAL);
/*     */     
/* 556 */     Atom<String> hexNumeral = Atom.of("hex_numeral");
/* 557 */     rules.put(hexNumeral, (Rule)HEX_NUMERAL);
/*     */     
/* 559 */     Atom<IntegerLiteral> integerLiteral = Atom.of("integer_literal");
/* 560 */     NamedRule<StringReader, IntegerLiteral> integerLiteralRule = rules.put(integerLiteral, 
/* 561 */         Term.sequence(new Term[] {
/* 562 */             Term.optional(rules.named(sign)), 
/* 563 */             Term.alternative(new Term[] {
/* 564 */                 Term.sequence(new Term[] {
/* 565 */                     StringReaderTerms.character('0'), 
/* 566 */                     Term.cut(), 
/* 567 */                     Term.alternative(new Term[] {
/* 568 */                         Term.sequence(new Term[] { StringReaderTerms.characters('x', 'X'), Term.cut(), rules.named(hexNumeral)
/* 569 */                           }), Term.sequence(new Term[] { StringReaderTerms.characters('b', 'B'), rules.named(binaryNumeral)
/* 570 */                           }), Term.sequence(new Term[] { rules.named(decimalNumeral), Term.cut(), Term.fail(ERROR_LEADING_ZERO_NOT_ALLOWED)
/* 571 */                           }), Term.marker(decimalNumeral, "0")
/*     */                       
/*     */                       })
/* 574 */                   }), rules.named(decimalNumeral)
/*     */               
/* 576 */               }), Term.optional(rules.named(integerSuffix))
/*     */           }), scope -> {
/*     */           IntegerSuffix suffix = (IntegerSuffix)scope.getOrDefault(integerSuffix, IntegerSuffix.EMPTY);
/*     */           
/*     */           Sign signValue = (Sign)scope.getOrDefault(sign, Sign.PLUS);
/*     */           
/*     */           String decimalContents = (String)scope.get(decimalNumeral);
/*     */           
/*     */           if (decimalContents != null) {
/*     */             return new IntegerLiteral(signValue, Base.DECIMAL, decimalContents, suffix);
/*     */           }
/*     */           
/*     */           String hexContents = (String)scope.get(hexNumeral);
/*     */           
/*     */           if (hexContents != null) {
/*     */             return new IntegerLiteral(signValue, Base.HEX, hexContents, suffix);
/*     */           }
/*     */           
/*     */           String binaryContents = (String)scope.getOrThrow(binaryNumeral);
/*     */           return new IntegerLiteral(signValue, Base.BINARY, binaryContents, suffix);
/*     */         });
/* 597 */     Atom<TypeSuffix> floatTypeSuffix = Atom.of("float_type_suffix");
/* 598 */     rules.put(floatTypeSuffix, 
/* 599 */         Term.alternative(new Term[] {
/* 600 */             Term.sequence(new Term[] { StringReaderTerms.characters('f', 'F'), Term.marker(floatTypeSuffix, TypeSuffix.FLOAT)
/* 601 */               }), Term.sequence(new Term[] { StringReaderTerms.characters('d', 'D'), Term.marker(floatTypeSuffix, TypeSuffix.DOUBLE) })
/*     */           }), scope -> (TypeSuffix)scope.getOrThrow(floatTypeSuffix));
/*     */ 
/*     */     
/* 605 */     Atom<Signed<String>> floatExponentPart = Atom.of("float_exponent_part");
/* 606 */     rules.put(floatExponentPart, 
/* 607 */         Term.sequence(new Term[] { StringReaderTerms.characters('e', 'E'), Term.optional(rules.named(sign)), rules.named(decimalNumeral) }), scope -> new Signed<>((Sign)scope.getOrDefault(sign, Sign.PLUS), (String)scope.getOrThrow(decimalNumeral)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 614 */     Atom<String> floatWholePart = Atom.of("float_whole_part");
/* 615 */     Atom<String> floatFractionPart = Atom.of("float_fraction_part");
/* 616 */     Atom<T> floatLiteral = Atom.of("float_literal");
/* 617 */     rules.putComplex(floatLiteral, 
/* 618 */         Term.sequence(new Term[] {
/* 619 */             Term.optional(rules.named(sign)), 
/* 620 */             Term.alternative(new Term[] {
/*     */                 
/* 622 */                 Term.sequence(new Term[] { rules.namedWithAlias(decimalNumeral, floatWholePart), StringReaderTerms.character('.'), Term.cut(), Term.optional(rules.namedWithAlias(decimalNumeral, floatFractionPart)), Term.optional(rules.named(floatExponentPart)), Term.optional(rules.named(floatTypeSuffix))
/*     */                   
/* 624 */                   }), Term.sequence(new Term[] { StringReaderTerms.character('.'), Term.cut(), rules.namedWithAlias(decimalNumeral, floatFractionPart), Term.optional(rules.named(floatExponentPart)), Term.optional(rules.named(floatTypeSuffix))
/*     */ 
/*     */                   
/* 627 */                   }), Term.sequence(new Term[] { rules.namedWithAlias(decimalNumeral, floatWholePart), rules.named(floatExponentPart), Term.cut(), Term.optional(rules.named(floatTypeSuffix))
/*     */                   
/* 629 */                   }), Term.sequence(new Term[] { rules.namedWithAlias(decimalNumeral, floatWholePart), Term.optional(rules.named(floatExponentPart)), rules.named(floatTypeSuffix) })
/*     */               })
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */           
/*     */           Sign wholeSign = (Sign)scope.getOrDefault(sign, Sign.PLUS);
/*     */           
/*     */           String whole = (String)scope.get(floatWholePart), fraction = (String)scope.get(floatFractionPart);
/*     */           
/*     */           Signed<String> exponent = (Signed<String>)scope.get(floatExponentPart);
/*     */           
/*     */           TypeSuffix typeSuffix = (TypeSuffix)scope.get(floatTypeSuffix);
/*     */           return createFloat(ops, wholeSign, whole, fraction, exponent, typeSuffix, state);
/*     */         });
/* 643 */     Atom<String> stringHex2 = Atom.of("string_hex_2");
/* 644 */     rules.put(stringHex2, (Rule)new SimpleHexLiteralParseRule(2));
/*     */     
/* 646 */     Atom<String> stringHex4 = Atom.of("string_hex_4");
/* 647 */     rules.put(stringHex4, (Rule)new SimpleHexLiteralParseRule(4));
/*     */     
/* 649 */     Atom<String> stringHex8 = Atom.of("string_hex_8");
/* 650 */     rules.put(stringHex8, (Rule)new SimpleHexLiteralParseRule(8));
/*     */     
/* 652 */     Atom<String> stringUnicodeName = Atom.of("string_unicode_name");
/* 653 */     rules.put(stringUnicodeName, (Rule)new GreedyPatternParseRule(UNICODE_NAME, ERROR_INVALID_CHARACTER_NAME));
/*     */     
/* 655 */     Atom<String> stringEscapeSequence = Atom.of("string_escape_sequence");
/* 656 */     rules.putComplex(stringEscapeSequence, 
/* 657 */         Term.alternative(new Term[] {
/*     */             
/* 659 */             Term.sequence(new Term[] { StringReaderTerms.character('b'), Term.marker(stringEscapeSequence, "\b")
/* 660 */               }), Term.sequence(new Term[] { StringReaderTerms.character('s'), Term.marker(stringEscapeSequence, " ")
/* 661 */               }), Term.sequence(new Term[] { StringReaderTerms.character('t'), Term.marker(stringEscapeSequence, "\t")
/* 662 */               }), Term.sequence(new Term[] { StringReaderTerms.character('n'), Term.marker(stringEscapeSequence, "\n")
/* 663 */               }), Term.sequence(new Term[] { StringReaderTerms.character('f'), Term.marker(stringEscapeSequence, "\f")
/* 664 */               }), Term.sequence(new Term[] { StringReaderTerms.character('r'), Term.marker(stringEscapeSequence, "\r")
/* 665 */               }), Term.sequence(new Term[] { StringReaderTerms.character('\\'), Term.marker(stringEscapeSequence, "\\")
/* 666 */               }), Term.sequence(new Term[] { StringReaderTerms.character('\''), Term.marker(stringEscapeSequence, "'")
/* 667 */               }), Term.sequence(new Term[] { StringReaderTerms.character('"'), Term.marker(stringEscapeSequence, "\"")
/* 668 */               }), Term.sequence(new Term[] { StringReaderTerms.character('x'), rules.named(stringHex2) }), 
/* 669 */             Term.sequence(new Term[] { StringReaderTerms.character('u'), rules.named(stringHex4)
/* 670 */               }), Term.sequence(new Term[] { StringReaderTerms.character('U'), rules.named(stringHex8)
/* 671 */               }), Term.sequence(new Term[] { StringReaderTerms.character('N'), StringReaderTerms.character('{'), rules.named(stringUnicodeName), StringReaderTerms.character('}') })
/*     */           }), state -> {
/*     */           int codePoint;
/*     */           
/*     */           Scope scope = state.scope();
/*     */           
/*     */           String plainEscape = (String)scope.getAny(new Atom[] { stringEscapeSequence });
/*     */           
/*     */           if (plainEscape != null) {
/*     */             return plainEscape;
/*     */           }
/*     */           
/*     */           String hexEscape = (String)scope.getAny(new Atom[] { stringHex2, stringHex4, stringHex8 });
/*     */           if (hexEscape != null) {
/*     */             int i = HexFormat.fromHexDigits(hexEscape);
/*     */             if (!Character.isValidCodePoint(i)) {
/*     */               state.errorCollector().store(state.mark(), DelayedException.create(ERROR_INVALID_CODEPOINT, String.format(Locale.ROOT, "U+%08X", new Object[] { i })));
/*     */               return null;
/*     */             } 
/*     */             return Character.toString(i);
/*     */           } 
/*     */           String character = (String)scope.getOrThrow(stringUnicodeName);
/*     */           try {
/*     */             codePoint = Character.codePointOf(character);
/* 695 */           } catch (IllegalArgumentException e) {
/*     */             state.errorCollector().store(state.mark(), ERROR_INVALID_CHARACTER_NAME);
/*     */             
/*     */             return null;
/*     */           } 
/*     */           
/*     */           return Character.toString(codePoint);
/*     */         });
/* 703 */     Atom<String> stringPlainContents = Atom.of("string_plain_contents");
/* 704 */     rules.put(stringPlainContents, (Rule)PLAIN_STRING_CHUNK);
/*     */ 
/*     */     
/* 707 */     Atom<List<String>> stringChunks = Atom.of("string_chunks");
/* 708 */     Atom<String> stringContents = Atom.of("string_contents");
/*     */     
/* 710 */     Atom<String> singleQuotedStringChunk = Atom.of("single_quoted_string_chunk");
/* 711 */     NamedRule<StringReader, String> singleQuotedStringChunkRule = rules.put(singleQuotedStringChunk, 
/* 712 */         Term.alternative(new Term[] {
/*     */             
/* 714 */             rules.namedWithAlias(stringPlainContents, stringContents), 
/* 715 */             Term.sequence(new Term[] { StringReaderTerms.character('\\'), rules.namedWithAlias(stringEscapeSequence, stringContents)
/* 716 */               }), Term.sequence(new Term[] { StringReaderTerms.character('"'), Term.marker(stringContents, "\"") })
/*     */           }), scope -> (String)scope.getOrThrow(stringContents));
/*     */ 
/*     */ 
/*     */     
/* 721 */     Atom<String> singleQuotedStringContents = Atom.of("single_quoted_string_contents");
/* 722 */     rules.put(singleQuotedStringContents, 
/* 723 */         Term.repeated(singleQuotedStringChunkRule, stringChunks), scope -> joinList((List<String>)scope.getOrThrow(stringChunks)));
/*     */ 
/*     */ 
/*     */     
/* 727 */     Atom<String> doubleQuotedStringChunk = Atom.of("double_quoted_string_chunk");
/* 728 */     NamedRule<StringReader, String> doubleQuotedStringChunkRule = rules.put(doubleQuotedStringChunk, 
/* 729 */         Term.alternative(new Term[] {
/*     */             
/* 731 */             rules.namedWithAlias(stringPlainContents, stringContents), 
/* 732 */             Term.sequence(new Term[] { StringReaderTerms.character('\\'), rules.namedWithAlias(stringEscapeSequence, stringContents)
/* 733 */               }), Term.sequence(new Term[] { StringReaderTerms.character('\''), Term.marker(stringContents, "'") })
/*     */           }), scope -> (String)scope.getOrThrow(stringContents));
/*     */ 
/*     */ 
/*     */     
/* 738 */     Atom<String> doubleQuotedStringContents = Atom.of("double_quoted_string_contents");
/* 739 */     rules.put(doubleQuotedStringContents, 
/* 740 */         Term.repeated(doubleQuotedStringChunkRule, stringChunks), scope -> joinList((List<String>)scope.getOrThrow(stringChunks)));
/*     */ 
/*     */ 
/*     */     
/* 744 */     Atom<String> quotedStringLiteral = Atom.of("quoted_string_literal");
/* 745 */     rules.put(quotedStringLiteral, 
/* 746 */         Term.alternative(new Term[] {
/* 747 */             Term.sequence(new Term[] { StringReaderTerms.character('"'), Term.cut(), Term.optional(rules.namedWithAlias(doubleQuotedStringContents, stringContents)), StringReaderTerms.character('"')
/* 748 */               }), Term.sequence(new Term[] { StringReaderTerms.character('\''), Term.optional(rules.namedWithAlias(singleQuotedStringContents, stringContents)), StringReaderTerms.character('\'') })
/*     */           }), scope -> (String)scope.getOrThrow(stringContents));
/*     */ 
/*     */ 
/*     */     
/* 753 */     Atom<String> unquotedString = Atom.of("unquoted_string");
/* 754 */     rules.put(unquotedString, (Rule)new UnquotedStringParseRule(1, ERROR_EXPECTED_UNQUOTED_STRING));
/*     */     
/* 756 */     Atom<T> literal = Atom.of("literal");
/*     */     
/* 758 */     Atom<List<T>> argumentList = Atom.of("arguments");
/* 759 */     rules.put(argumentList, 
/* 760 */         Term.repeatedWithTrailingSeparator(rules.forward(literal), argumentList, StringReaderTerms.character(',')), scope -> (List)scope.getOrThrow(argumentList));
/*     */ 
/*     */ 
/*     */     
/* 764 */     Atom<T> unquotedStringOrBuiltIn = Atom.of("unquoted_string_or_builtin");
/* 765 */     rules.putComplex(unquotedStringOrBuiltIn, 
/* 766 */         Term.sequence(new Term[] {
/* 767 */             rules.named(unquotedString), 
/* 768 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character('('), rules.named(argumentList), StringReaderTerms.character(')') }))
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */ 
/*     */           
/*     */           String contents = (String)scope.getOrThrow(unquotedString);
/*     */ 
/*     */           
/*     */           if (contents.isEmpty() || !isAllowedToStartUnquotedString(contents.charAt(0))) {
/*     */             state.errorCollector().store(state.mark(), SnbtOperations.BUILTIN_IDS, ERROR_INVALID_UNQUOTED_START);
/*     */ 
/*     */             
/*     */             return null;
/*     */           } 
/*     */ 
/*     */           
/*     */           List<T> arguments = (List<T>)scope.get(argumentList);
/*     */ 
/*     */           
/*     */           if (arguments != null) {
/*     */             SnbtOperations.BuiltinKey key = new SnbtOperations.BuiltinKey(contents, arguments.size());
/*     */ 
/*     */             
/*     */             SnbtOperations.BuiltinOperation operation = SnbtOperations.BUILTIN_OPERATIONS.get(key);
/*     */ 
/*     */             
/*     */             if (operation != null) {
/*     */               return operation.run(ops, arguments, state);
/*     */             }
/*     */ 
/*     */             
/*     */             state.errorCollector().store(state.mark(), DelayedException.create(ERROR_NO_SUCH_OPERATION, key.toString()));
/*     */             
/*     */             return null;
/*     */           } 
/*     */           
/*     */           return contents.equalsIgnoreCase("true") ? trueValue : (contents.equalsIgnoreCase("false") ? falseValue : ops.createString(contents));
/*     */         });
/*     */     
/* 807 */     Atom<String> mapKey = Atom.of("map_key");
/* 808 */     rules.put(mapKey, 
/* 809 */         Term.alternative(new Term[] {
/* 810 */             rules.named(quotedStringLiteral), 
/* 811 */             rules.named(unquotedString)
/*     */           }), scope -> (String)scope.getAnyOrThrow(new Atom[] { quotedStringLiteral, unquotedString }));
/*     */ 
/*     */ 
/*     */     
/* 816 */     Atom<Map.Entry<String, T>> mapEntry = Atom.of("map_entry");
/* 817 */     NamedRule<StringReader, Map.Entry<String, T>> mapEntryRule = rules.putComplex(mapEntry, 
/* 818 */         Term.sequence(new Term[] {
/* 819 */             rules.named(mapKey), 
/* 820 */             StringReaderTerms.character(':'), 
/* 821 */             rules.named(literal)
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */           
/*     */           String key = (String)scope.getOrThrow(mapKey);
/*     */           
/*     */           if (key.isEmpty()) {
/*     */             state.errorCollector().store(state.mark(), ERROR_EMPTY_KEY);
/*     */             
/*     */             return null;
/*     */           } 
/*     */           T value = (T)scope.getOrThrow(literal);
/*     */           return Map.entry(key, value);
/*     */         });
/* 835 */     Atom<List<Map.Entry<String, T>>> mapEntries = Atom.of("map_entries");
/* 836 */     rules.put(mapEntries, 
/* 837 */         Term.repeatedWithTrailingSeparator(mapEntryRule, mapEntries, StringReaderTerms.character(',')), scope -> (List)scope.getOrThrow(mapEntries));
/*     */ 
/*     */ 
/*     */     
/* 841 */     Atom<T> mapLiteral = Atom.of("map_literal");
/* 842 */     rules.put(mapLiteral, 
/* 843 */         Term.sequence(new Term[] {
/* 844 */             StringReaderTerms.character('{'), rules.named(mapEntries), StringReaderTerms.character('}')
/*     */           }), scope -> {
/*     */           List<Map.Entry<String, T>> entries = (List<Map.Entry<String, T>>)scope.getOrThrow(mapEntries);
/*     */           
/*     */           if (entries.isEmpty()) {
/*     */             return emptyMapValue;
/*     */           }
/*     */           
/*     */           ImmutableMap.Builder<T, T> builder = ImmutableMap.builderWithExpectedSize(entries.size());
/*     */           
/*     */           for (Map.Entry<String, T> e : entries) {
/*     */             builder.put(ops.createString(e.getKey()), e.getValue());
/*     */           }
/*     */           return ops.createMap((Map)builder.buildKeepingLast());
/*     */         });
/* 859 */     Atom<List<T>> listEntries = Atom.of("list_entries");
/* 860 */     rules.put(listEntries, 
/* 861 */         Term.repeatedWithTrailingSeparator(rules.forward(literal), listEntries, StringReaderTerms.character(',')), scope -> (List)scope.getOrThrow(listEntries));
/*     */ 
/*     */ 
/*     */     
/* 865 */     Atom<ArrayPrefix> arrayPrefix = Atom.of("array_prefix");
/* 866 */     rules.put(arrayPrefix, 
/* 867 */         Term.alternative(new Term[] {
/* 868 */             Term.sequence(new Term[] { StringReaderTerms.character('B'), Term.marker(arrayPrefix, ArrayPrefix.BYTE)
/* 869 */               }), Term.sequence(new Term[] { StringReaderTerms.character('L'), Term.marker(arrayPrefix, ArrayPrefix.LONG)
/* 870 */               }), Term.sequence(new Term[] { StringReaderTerms.character('I'), Term.marker(arrayPrefix, ArrayPrefix.INT) })
/*     */           }), scope -> (ArrayPrefix)scope.getOrThrow(arrayPrefix));
/*     */ 
/*     */     
/* 874 */     Atom<List<IntegerLiteral>> intArrayEntries = Atom.of("int_array_entries");
/* 875 */     rules.put(intArrayEntries, 
/* 876 */         Term.repeatedWithTrailingSeparator(integerLiteralRule, intArrayEntries, StringReaderTerms.character(',')), scope -> (List)scope.getOrThrow(intArrayEntries));
/*     */ 
/*     */ 
/*     */     
/* 880 */     Atom<T> listLiteral = Atom.of("list_literal");
/* 881 */     rules.putComplex(listLiteral, 
/* 882 */         Term.sequence(new Term[] {
/* 883 */             StringReaderTerms.character('['), 
/* 884 */             Term.alternative(new Term[] {
/* 885 */                 Term.sequence(new Term[] { rules.named(arrayPrefix), StringReaderTerms.character(';'), rules.named(intArrayEntries)
/* 886 */                   }), rules.named(listEntries)
/*     */               
/* 888 */               }), StringReaderTerms.character(']')
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */           
/*     */           ArrayPrefix arrayType = (ArrayPrefix)scope.get(arrayPrefix);
/*     */           
/*     */           if (arrayType != null) {
/*     */             List<IntegerLiteral> list = (List<IntegerLiteral>)scope.getOrThrow(intArrayEntries);
/*     */             
/*     */             return list.isEmpty() ? arrayType.create(ops) : arrayType.create(ops, list, state);
/*     */           } 
/*     */           
/*     */           List<T> entries = (List<T>)scope.getOrThrow(listEntries);
/*     */           
/*     */           return entries.isEmpty() ? emptyList : ops.createList(entries.stream());
/*     */         });
/* 904 */     NamedRule<StringReader, T> literalRule = rules.putComplex(literal, 
/* 905 */         Term.alternative(new Term[] {
/* 906 */             Term.sequence(new Term[] {
/* 907 */                 Term.positiveLookahead((Term)NUMBER_LOOKEAHEAD), 
/* 908 */                 Term.alternative(new Term[] {
/* 909 */                     rules.namedWithAlias(floatLiteral, literal), 
/* 910 */                     rules.named(integerLiteral)
/*     */                   
/*     */                   })
/* 913 */               }), Term.sequence(new Term[] { Term.positiveLookahead(StringReaderTerms.characters('"', '\'')), Term.cut(), rules.named(quotedStringLiteral)
/* 914 */               }), Term.sequence(new Term[] { Term.positiveLookahead(StringReaderTerms.character('{')), Term.cut(), rules.namedWithAlias(mapLiteral, literal)
/* 915 */               }), Term.sequence(new Term[] { Term.positiveLookahead(StringReaderTerms.character('[')), Term.cut(), rules.namedWithAlias(listLiteral, literal)
/*     */               
/* 917 */               }), rules.namedWithAlias(unquotedStringOrBuiltIn, literal)
/*     */           }), state -> {
/*     */           Scope scope = state.scope();
/*     */ 
/*     */           
/*     */           String quotedString = (String)scope.get(quotedStringLiteral);
/*     */ 
/*     */           
/*     */           if (quotedString != null) {
/*     */             return ops.createString(quotedString);
/*     */           }
/*     */ 
/*     */           
/*     */           IntegerLiteral integer = (IntegerLiteral)scope.get(integerLiteral);
/*     */           
/*     */           return (integer != null) ? integer.create(ops, state) : scope.getOrThrow(literal);
/*     */         });
/*     */     
/* 935 */     return new Grammar(rules, literalRule);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/SnbtGrammar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */