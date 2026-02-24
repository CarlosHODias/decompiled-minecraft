/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public enum ChatFormatting
/*     */   implements StringRepresentable
/*     */ {
/*  19 */   BLACK("BLACK", '0', 0, 0),
/*  20 */   DARK_BLUE("DARK_BLUE", '1', 1, 170),
/*  21 */   DARK_GREEN("DARK_GREEN", '2', 2, 43520),
/*  22 */   DARK_AQUA("DARK_AQUA", '3', 3, 43690),
/*  23 */   DARK_RED("DARK_RED", '4', 4, 11141120),
/*  24 */   DARK_PURPLE("DARK_PURPLE", '5', 5, 11141290),
/*  25 */   GOLD("GOLD", '6', 6, 16755200),
/*  26 */   GRAY("GRAY", '7', 7, 11184810),
/*  27 */   DARK_GRAY("DARK_GRAY", '8', 8, 5592405),
/*  28 */   BLUE("BLUE", '9', 9, 5592575),
/*  29 */   GREEN("GREEN", 'a', 10, 5635925),
/*  30 */   AQUA("AQUA", 'b', 11, 5636095),
/*  31 */   RED("RED", 'c', 12, 16733525),
/*  32 */   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695),
/*  33 */   YELLOW("YELLOW", 'e', 14, 16777045),
/*  34 */   WHITE("WHITE", 'f', 15, 16777215),
/*  35 */   OBFUSCATED("OBFUSCATED", 'k', true),
/*  36 */   BOLD("BOLD", 'l', true),
/*  37 */   STRIKETHROUGH("STRIKETHROUGH", 'm', true),
/*  38 */   UNDERLINE("UNDERLINE", 'n', true),
/*  39 */   ITALIC("ITALIC", 'o', true),
/*  40 */   RESET("RESET", 'r', -1, null);
/*     */   
/*  42 */   public static final Codec<ChatFormatting> CODEC = (Codec<ChatFormatting>)StringRepresentable.fromEnum(ChatFormatting::values); static {
/*  43 */     COLOR_CODEC = CODEC.validate(color -> color.isFormat() ? DataResult.error(()) : DataResult.success(color));
/*     */ 
/*     */     
/*  46 */     FORMATTING_BY_NAME = (Map<String, ChatFormatting>)Arrays.<ChatFormatting>stream(values()).collect(Collectors.toMap(format -> cleanName(format.name), f -> f));
/*  47 */   } private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]"); public static final Codec<ChatFormatting> COLOR_CODEC; public static final char PREFIX_CODE = '§';
/*     */   
/*     */   private static String cleanName(String name) {
/*  50 */     return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Map<String, ChatFormatting> FORMATTING_BY_NAME;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final char code;
/*     */   
/*     */   private final boolean isFormat;
/*     */   
/*     */   private final String toString;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final Integer color;
/*     */   
/*     */   ChatFormatting(String name, char code, boolean isFormat, int id, Integer color) {
/*  69 */     this.name = name;
/*  70 */     this.code = code;
/*  71 */     this.isFormat = isFormat;
/*  72 */     this.id = id;
/*  73 */     this.color = color;
/*     */     
/*  75 */     this.toString = "§" + String.valueOf(code);
/*     */   }
/*     */   
/*     */   public char getChar() {
/*  79 */     return this.code;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  83 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean isFormat() {
/*  87 */     return this.isFormat;
/*     */   }
/*     */   
/*     */   public boolean isColor() {
/*  91 */     return (!this.isFormat && this != RESET);
/*     */   }
/*     */   
/*     */   public Integer getColor() {
/*  95 */     return this.color;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  99 */     return name().toLowerCase(Locale.ROOT);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return this.toString;
/*     */   }
/*     */   
/*     */   @Contract("!null->!null;_->_")
/*     */   public static String stripFormatting(String input) {
/* 109 */     return (input == null) ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
/*     */   }
/*     */   
/*     */   public static ChatFormatting getByName(String name) {
/* 113 */     if (name == null) {
/* 114 */       return null;
/*     */     }
/* 116 */     return FORMATTING_BY_NAME.get(cleanName(name));
/*     */   }
/*     */   
/*     */   public static ChatFormatting getById(int id) {
/* 120 */     if (id < 0) {
/* 121 */       return RESET;
/*     */     }
/* 123 */     for (ChatFormatting format : values()) {
/* 124 */       if (format.getId() == id) {
/* 125 */         return format;
/*     */       }
/*     */     } 
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public static ChatFormatting getByCode(char code) {
/* 132 */     char sanitized = Character.toLowerCase(code);
/* 133 */     for (ChatFormatting format : values()) {
/* 134 */       if (format.code == sanitized) {
/* 135 */         return format;
/*     */       }
/*     */     } 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   public static Collection<String> getNames(boolean getColors, boolean getFormats) {
/* 142 */     List<String> result = Lists.newArrayList();
/*     */     
/* 144 */     for (ChatFormatting format : values()) {
/* 145 */       if (!format.isColor() || getColors)
/*     */       {
/*     */         
/* 148 */         if (!format.isFormat() || getFormats)
/*     */         {
/*     */           
/* 151 */           result.add(format.getName()); } 
/*     */       }
/*     */     } 
/* 154 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/* 159 */     return getName();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/ChatFormatting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */