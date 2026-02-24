/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Style {
/*     */   public static final int NO_SHADOW = 0;
/*     */   private final TextColor color;
/*     */   private final Integer shadowColor;
/*     */   private final Boolean bold;
/*     */   private final Boolean italic;
/*     */   private final Boolean underlined;
/*  17 */   public static final Style EMPTY = new Style(null, null, null, null, null, null, null, null, null, null, null); private final Boolean strikethrough; private final Boolean obfuscated; private final ClickEvent clickEvent; private final HoverEvent hoverEvent; private final String insertion;
/*     */   private final FontDescription font;
/*     */   
/*     */   public static class Serializer { static {
/*  21 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TextColor.CODEC.optionalFieldOf("color").forGetter(()), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("shadow_color").forGetter(()), (App)Codec.BOOL.optionalFieldOf("bold").forGetter(()), (App)Codec.BOOL.optionalFieldOf("italic").forGetter(()), (App)Codec.BOOL.optionalFieldOf("underlined").forGetter(()), (App)Codec.BOOL.optionalFieldOf("strikethrough").forGetter(()), (App)Codec.BOOL.optionalFieldOf("obfuscated").forGetter(()), (App)ClickEvent.CODEC.optionalFieldOf("click_event").forGetter(()), (App)HoverEvent.CODEC.optionalFieldOf("hover_event").forGetter(()), (App)Codec.STRING.optionalFieldOf("insertion").forGetter(()), (App)FontDescription.CODEC.optionalFieldOf("font").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, Style::create));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final com.mojang.serialization.MapCodec<Style> MAP_CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  36 */     public static final Codec<Style> CODEC = MAP_CODEC.codec();
/*  37 */     public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC); }
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
/*     */   private static Style create(Optional<TextColor> color, Optional<Integer> shadowColor, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated, Optional<ClickEvent> clickEvent, Optional<HoverEvent> hoverEvent, Optional<String> insertion, Optional<FontDescription> font) {
/*  55 */     Style result = new Style(color.orElse(null), shadowColor.orElse(null), bold.orElse(null), italic.orElse(null), underlined.orElse(null), strikethrough.orElse(null), obfuscated.orElse(null), clickEvent.orElse(null), hoverEvent.orElse(null), insertion.orElse(null), font.orElse(null));
/*  56 */     if (result.equals(EMPTY)) {
/*  57 */       return EMPTY;
/*     */     }
/*  59 */     return result;
/*     */   }
/*     */   
/*     */   private Style(TextColor color, Integer shadowColor, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, FontDescription font) {
/*  63 */     this.color = color;
/*  64 */     this.shadowColor = shadowColor;
/*  65 */     this.bold = bold;
/*  66 */     this.italic = italic;
/*  67 */     this.underlined = underlined;
/*  68 */     this.strikethrough = strikethrough;
/*  69 */     this.obfuscated = obfuscated;
/*  70 */     this.clickEvent = clickEvent;
/*  71 */     this.hoverEvent = hoverEvent;
/*  72 */     this.insertion = insertion;
/*  73 */     this.font = font;
/*     */   }
/*     */   
/*     */   public TextColor getColor() {
/*  77 */     return this.color;
/*     */   }
/*     */   
/*     */   public Integer getShadowColor() {
/*  81 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public boolean isBold() {
/*  85 */     return (this.bold == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isItalic() {
/*  89 */     return (this.italic == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isStrikethrough() {
/*  93 */     return (this.strikethrough == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isUnderlined() {
/*  97 */     return (this.underlined == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isObfuscated() {
/* 101 */     return (this.obfuscated == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 105 */     return (this == EMPTY);
/*     */   }
/*     */   
/*     */   public ClickEvent getClickEvent() {
/* 109 */     return this.clickEvent;
/*     */   }
/*     */   
/*     */   public HoverEvent getHoverEvent() {
/* 113 */     return this.hoverEvent;
/*     */   }
/*     */   
/*     */   public String getInsertion() {
/* 117 */     return this.insertion;
/*     */   }
/*     */   
/*     */   public FontDescription getFont() {
/* 121 */     return (this.font != null) ? this.font : FontDescription.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Style checkEmptyAfterChange(Style newStyle, T previous, T next) {
/* 127 */     if (previous != null && next == null && newStyle.equals(EMPTY)) {
/* 128 */       return EMPTY;
/*     */     }
/* 130 */     return newStyle;
/*     */   }
/*     */   
/*     */   public Style withColor(TextColor color) {
/* 134 */     if (Objects.equals(this.color, color)) {
/* 135 */       return this;
/*     */     }
/* 137 */     return checkEmptyAfterChange(new Style(color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.color, color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withColor(ChatFormatting color) {
/* 144 */     return withColor((color != null) ? TextColor.fromLegacyFormat(color) : null);
/*     */   }
/*     */   
/*     */   public Style withColor(int color) {
/* 148 */     return withColor(TextColor.fromRgb(color));
/*     */   }
/*     */   
/*     */   public Style withShadowColor(int shadowColor) {
/* 152 */     if (Objects.equals(this.shadowColor, shadowColor)) {
/* 153 */       return this;
/*     */     }
/*     */     
/* 156 */     return checkEmptyAfterChange(new Style(this.color, shadowColor, 
/* 157 */           this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.shadowColor, shadowColor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withoutShadow() {
/* 163 */     return withShadowColor(0);
/*     */   }
/*     */   
/*     */   public Style withBold(Boolean bold) {
/* 167 */     if (Objects.equals(this.bold, bold)) {
/* 168 */       return this;
/*     */     }
/* 170 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.bold, bold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withItalic(Boolean italic) {
/* 177 */     if (Objects.equals(this.italic, italic)) {
/* 178 */       return this;
/*     */     }
/* 180 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.italic, italic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withUnderlined(Boolean underlined) {
/* 187 */     if (Objects.equals(this.underlined, underlined)) {
/* 188 */       return this;
/*     */     }
/* 190 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.underlined, underlined);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withStrikethrough(Boolean strikethrough) {
/* 197 */     if (Objects.equals(this.strikethrough, strikethrough)) {
/* 198 */       return this;
/*     */     }
/* 200 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.strikethrough, strikethrough);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withObfuscated(Boolean obfuscated) {
/* 207 */     if (Objects.equals(this.obfuscated, obfuscated)) {
/* 208 */       return this;
/*     */     }
/* 210 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.obfuscated, obfuscated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withClickEvent(ClickEvent clickEvent) {
/* 217 */     if (Objects.equals(this.clickEvent, clickEvent)) {
/* 218 */       return this;
/*     */     }
/* 220 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, clickEvent, this.hoverEvent, this.insertion, this.font), this.clickEvent, clickEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withHoverEvent(HoverEvent hoverEvent) {
/* 227 */     if (Objects.equals(this.hoverEvent, hoverEvent)) {
/* 228 */       return this;
/*     */     }
/* 230 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, hoverEvent, this.insertion, this.font), this.hoverEvent, hoverEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withInsertion(String insertion) {
/* 237 */     if (Objects.equals(this.insertion, insertion)) {
/* 238 */       return this;
/*     */     }
/* 240 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, insertion, this.font), this.insertion, insertion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withFont(FontDescription font) {
/* 247 */     if (Objects.equals(this.font, font)) {
/* 248 */       return this;
/*     */     }
/* 250 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, font), this.font, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style applyFormat(ChatFormatting format) {
/* 257 */     TextColor color = this.color;
/* 258 */     Boolean bold = this.bold;
/* 259 */     Boolean italic = this.italic;
/* 260 */     Boolean strikethrough = this.strikethrough;
/* 261 */     Boolean underlined = this.underlined;
/* 262 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 264 */     switch (format) {
/*     */       case OBFUSCATED:
/* 266 */         obfuscated = true;
/*     */         break;
/*     */       case BOLD:
/* 269 */         bold = true;
/*     */         break;
/*     */       case STRIKETHROUGH:
/* 272 */         strikethrough = true;
/*     */         break;
/*     */       case UNDERLINE:
/* 275 */         underlined = true;
/*     */         break;
/*     */       case ITALIC:
/* 278 */         italic = true;
/*     */         break;
/*     */       case RESET:
/* 281 */         return EMPTY;
/*     */       default:
/* 283 */         color = TextColor.fromLegacyFormat(format);
/*     */         break;
/*     */     } 
/* 286 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyLegacyFormat(ChatFormatting format) {
/* 290 */     TextColor color = this.color;
/* 291 */     Boolean bold = this.bold;
/* 292 */     Boolean italic = this.italic;
/* 293 */     Boolean strikethrough = this.strikethrough;
/* 294 */     Boolean underlined = this.underlined;
/* 295 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 297 */     switch (format) {
/*     */       case OBFUSCATED:
/* 299 */         obfuscated = true;
/*     */         break;
/*     */       case BOLD:
/* 302 */         bold = true;
/*     */         break;
/*     */       case STRIKETHROUGH:
/* 305 */         strikethrough = true;
/*     */         break;
/*     */       case UNDERLINE:
/* 308 */         underlined = true;
/*     */         break;
/*     */       case ITALIC:
/* 311 */         italic = true;
/*     */         break;
/*     */       case RESET:
/* 314 */         return EMPTY;
/*     */       
/*     */       default:
/* 317 */         obfuscated = false;
/* 318 */         bold = false;
/* 319 */         strikethrough = false;
/* 320 */         underlined = false;
/* 321 */         italic = false;
/* 322 */         color = TextColor.fromLegacyFormat(format);
/*     */         break;
/*     */     } 
/* 325 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyFormats(ChatFormatting... formats) {
/* 329 */     TextColor color = this.color;
/* 330 */     Boolean bold = this.bold;
/* 331 */     Boolean italic = this.italic;
/* 332 */     Boolean strikethrough = this.strikethrough;
/* 333 */     Boolean underlined = this.underlined;
/* 334 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 336 */     for (ChatFormatting format : formats) {
/* 337 */       switch (format) {
/*     */         case OBFUSCATED:
/* 339 */           obfuscated = true;
/*     */           break;
/*     */         case BOLD:
/* 342 */           bold = true;
/*     */           break;
/*     */         case STRIKETHROUGH:
/* 345 */           strikethrough = true;
/*     */           break;
/*     */         case UNDERLINE:
/* 348 */           underlined = true;
/*     */           break;
/*     */         case ITALIC:
/* 351 */           italic = true;
/*     */           break;
/*     */         case RESET:
/* 354 */           return EMPTY;
/*     */         default:
/* 356 */           color = TextColor.fromLegacyFormat(format);
/*     */           break;
/*     */       } 
/*     */     } 
/* 360 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyTo(Style other) {
/* 364 */     if (this == EMPTY) {
/* 365 */       return other;
/*     */     }
/*     */     
/* 368 */     if (other == EMPTY) {
/* 369 */       return this;
/*     */     }
/*     */     
/* 372 */     return new Style(
/* 373 */         (this.color != null) ? this.color : other.color, 
/* 374 */         (this.shadowColor != null) ? this.shadowColor : other.shadowColor, 
/* 375 */         (this.bold != null) ? this.bold : other.bold, 
/* 376 */         (this.italic != null) ? this.italic : other.italic, 
/* 377 */         (this.underlined != null) ? this.underlined : other.underlined, 
/* 378 */         (this.strikethrough != null) ? this.strikethrough : other.strikethrough, 
/* 379 */         (this.obfuscated != null) ? this.obfuscated : other.obfuscated, 
/* 380 */         (this.clickEvent != null) ? this.clickEvent : other.clickEvent, 
/* 381 */         (this.hoverEvent != null) ? this.hoverEvent : other.hoverEvent, 
/* 382 */         (this.insertion != null) ? this.insertion : other.insertion, 
/* 383 */         (this.font != null) ? this.font : other.font);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 389 */     final StringBuilder result = new StringBuilder("{");
/*     */     class Collector { private boolean isNotFirst;
/*     */       
/*     */       Collector(Style this$0) {}
/*     */       
/*     */       private void prependSeparator() {
/* 395 */         if (this.isNotFirst) {
/* 396 */           result.append(',');
/*     */         }
/* 398 */         this.isNotFirst = true;
/*     */       }
/*     */       
/*     */       private void addFlagString(String name, Boolean value) {
/* 402 */         if (value != null) {
/* 403 */           prependSeparator();
/* 404 */           if (!value) {
/* 405 */             result.append('!');
/*     */           }
/* 407 */           result.append(name);
/*     */         } 
/*     */       }
/*     */       
/*     */       private void addValueString(String name, Object value) {
/* 412 */         if (value != null) {
/* 413 */           prependSeparator();
/* 414 */           result.append(name);
/* 415 */           result.append('=');
/* 416 */           result.append(value);
/*     */         } 
/*     */       } }
/*     */     ;
/*     */     
/* 421 */     Collector collector = new Collector(this);
/*     */     
/* 423 */     collector.addValueString("color", this.color);
/*     */     
/* 425 */     collector.addValueString("shadowColor", this.shadowColor);
/*     */     
/* 427 */     collector.addFlagString("bold", this.bold);
/* 428 */     collector.addFlagString("italic", this.italic);
/* 429 */     collector.addFlagString("underlined", this.underlined);
/* 430 */     collector.addFlagString("strikethrough", this.strikethrough);
/* 431 */     collector.addFlagString("obfuscated", this.obfuscated);
/*     */     
/* 433 */     collector.addValueString("clickEvent", this.clickEvent);
/* 434 */     collector.addValueString("hoverEvent", this.hoverEvent);
/* 435 */     collector.addValueString("insertion", this.insertion);
/* 436 */     collector.addValueString("font", this.font);
/*     */     
/* 438 */     result.append("}");
/* 439 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 444 */     if (this == o) {
/* 445 */       return true;
/*     */     }
/* 447 */     if (o instanceof Style) { Style style = (Style)o;
/* 448 */       return (this.bold == style.bold && 
/* 449 */         Objects.equals(getColor(), style.getColor()) && 
/* 450 */         Objects.equals(getShadowColor(), style.getShadowColor()) && this.italic == style.italic && this.obfuscated == style.obfuscated && this.strikethrough == style.strikethrough && this.underlined == style.underlined && 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 455 */         Objects.equals(this.clickEvent, style.clickEvent) && 
/* 456 */         Objects.equals(this.hoverEvent, style.hoverEvent) && 
/* 457 */         Objects.equals(this.insertion, style.insertion) && 
/* 458 */         Objects.equals(this.font, style.font)); }
/*     */ 
/*     */ 
/*     */     
/* 462 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 467 */     return Objects.hash(new Object[] { this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/Style.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */