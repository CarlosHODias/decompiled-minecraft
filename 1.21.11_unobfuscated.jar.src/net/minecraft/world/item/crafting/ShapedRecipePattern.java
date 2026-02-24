/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.chars.CharArraySet;
/*     */ import it.unimi.dsi.fastutil.chars.CharSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class ShapedRecipePattern {
/*     */   private static final int MAX_SIZE = 3;
/*     */   public static final char EMPTY_SLOT = ' ';
/*     */   public static final com.mojang.serialization.MapCodec<ShapedRecipePattern> MAP_CODEC;
/*     */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ShapedRecipePattern> STREAM_CODEC;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final List<Optional<Ingredient>> ingredients;
/*     */   private final Optional<Data> data;
/*     */   private final int ingredientCount;
/*     */   private final boolean symmetrical;
/*     */   
/*     */   static {
/*  27 */     MAP_CODEC = Data.MAP_CODEC.flatXmap(ShapedRecipePattern::unpack, pattern -> (DataResult)pattern.data.<DataResult>map(DataResult::success).orElseGet(()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, e -> e.width, ByteBufCodecs.VAR_INT, e -> e.height, 
/*     */ 
/*     */         
/*  35 */         Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), e -> e.ingredients, ShapedRecipePattern::createFromNetwork);
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
/*     */   public ShapedRecipePattern(int width, int height, List<Optional<Ingredient>> ingredients, Optional<Data> data) {
/*  48 */     this.width = width;
/*  49 */     this.height = height;
/*  50 */     this.ingredients = ingredients;
/*  51 */     this.data = data;
/*  52 */     this.ingredientCount = (int)ingredients.stream().flatMap(Optional::stream).count();
/*  53 */     this.symmetrical = net.minecraft.util.Util.isSymmetrical(width, height, ingredients);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ShapedRecipePattern createFromNetwork(Integer width, Integer height, List<Optional<Ingredient>> ingredients) {
/*  58 */     return new ShapedRecipePattern(width, height, ingredients, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, String... pattern) {
/*  62 */     return of(key, List.of(pattern));
/*     */   }
/*     */   
/*     */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, List<String> pattern) {
/*  66 */     Data data = new Data(key, pattern);
/*  67 */     return (ShapedRecipePattern)unpack(data).getOrThrow();
/*     */   }
/*     */   
/*     */   private static DataResult<ShapedRecipePattern> unpack(Data data) {
/*  71 */     String[] shrunkPattern = shrink(data.pattern);
/*  72 */     int width = shrunkPattern[0].length();
/*  73 */     int height = shrunkPattern.length;
/*  74 */     List<Optional<Ingredient>> ingredients = new java.util.ArrayList<>(width * height);
/*  75 */     CharArraySet charArraySet = new CharArraySet(data.key.keySet());
/*     */     
/*  77 */     for (String line : shrunkPattern) {
/*  78 */       for (int x = 0; x < line.length(); x++) {
/*  79 */         Optional<Ingredient> ingredient; char symbol = line.charAt(x);
/*     */         
/*  81 */         if (symbol == ' ') {
/*  82 */           ingredient = Optional.empty();
/*     */         } else {
/*  84 */           Ingredient ingredientForSymbol = data.key.get(symbol);
/*  85 */           if (ingredientForSymbol == null) {
/*  86 */             return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key");
/*     */           }
/*  88 */           ingredient = Optional.of(ingredientForSymbol);
/*     */         } 
/*  90 */         charArraySet.remove(symbol);
/*  91 */         ingredients.add(ingredient);
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (!charArraySet.isEmpty()) {
/*  96 */       return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + String.valueOf(unusedSymbols));
/*     */     }
/*     */     
/*  99 */     return DataResult.success(new ShapedRecipePattern(width, height, ingredients, Optional.of(data)));
/*     */   }
/*     */   
/*     */   @com.google.common.annotations.VisibleForTesting
/*     */   static String[] shrink(List<String> pattern) {
/* 104 */     int left = Integer.MAX_VALUE;
/* 105 */     int right = 0;
/* 106 */     int top = 0;
/* 107 */     int bottom = 0;
/*     */     
/* 109 */     for (int i = 0; i < pattern.size(); i++) {
/* 110 */       String str = pattern.get(i);
/*     */       
/* 112 */       left = Math.min(left, firstNonEmpty(str));
/* 113 */       int lastNonSpace = lastNonEmpty(str);
/* 114 */       right = Math.max(right, lastNonSpace);
/*     */ 
/*     */       
/* 117 */       if (lastNonSpace < 0) {
/* 118 */         if (top == i) {
/* 119 */           top++;
/*     */         }
/* 121 */         bottom++;
/*     */       } else {
/* 123 */         bottom = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     if (pattern.size() == bottom) {
/* 128 */       return new String[0];
/*     */     }
/*     */     
/* 131 */     String[] result = new String[pattern.size() - bottom - top];
/* 132 */     for (int line = 0; line < result.length; line++) {
/* 133 */       result[line] = ((String)pattern.get(line + top)).substring(left, right + 1);
/*     */     }
/*     */     
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   private static int firstNonEmpty(String line) {
/* 140 */     int index = 0;
/* 141 */     while (index < line.length() && line.charAt(index) == ' ') {
/* 142 */       index++;
/*     */     }
/* 144 */     return index;
/*     */   }
/*     */   
/*     */   private static int lastNonEmpty(String line) {
/* 148 */     int index = line.length() - 1;
/* 149 */     while (index >= 0 && line.charAt(index) == ' ') {
/* 150 */       index--;
/*     */     }
/* 152 */     return index;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input) {
/* 156 */     if (input.ingredientCount() != this.ingredientCount) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (input.width() == this.width && input.height() == this.height) {
/* 160 */       if (!this.symmetrical && matches(input, true)) {
/* 161 */         return true;
/*     */       }
/* 163 */       if (matches(input, false)) {
/* 164 */         return true;
/*     */       }
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matches(CraftingInput input, boolean xFlip) {
/* 171 */     for (int y = 0; y < this.height; y++) {
/* 172 */       for (int x = 0; x < this.width; x++) {
/*     */         Optional<Ingredient> expected;
/* 174 */         if (xFlip) {
/* 175 */           expected = this.ingredients.get(this.width - x - 1 + y * this.width);
/*     */         } else {
/* 177 */           expected = this.ingredients.get(x + y * this.width);
/*     */         } 
/* 179 */         net.minecraft.world.item.ItemStack actual = input.getItem(x, y);
/* 180 */         if (!Ingredient.testOptionalIngredient(expected, actual)) {
/* 181 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   public int width() {
/* 189 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 193 */     return this.height;
/*     */   }
/*     */   
/*     */   public List<Optional<Ingredient>> ingredients() {
/* 197 */     return this.ingredients;
/*     */   }
/*     */   public static final class Data extends Record { private final Map<Character, Ingredient> key; private final List<String> pattern; private static final Codec<List<String>> PATTERN_CODEC; private static final Codec<Character> SYMBOL_CODEC; public static final com.mojang.serialization.MapCodec<Data> MAP_CODEC;
/* 200 */     public Data(Map<Character, Ingredient> key, List<String> pattern) { this.key = key; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 200 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data; } public Map<Character, Ingredient> key() { return this.key; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/* 200 */       //   0	8	1	o	Ljava/lang/Object; } public List<String> pattern() { return this.pattern; } static {
/* 201 */       PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
/*     */             if (strings.size() > 3) {
/*     */               return DataResult.error(());
/*     */             }
/*     */             if (strings.isEmpty()) {
/*     */               return DataResult.error(());
/*     */             }
/*     */             int firstLength = ((String)strings.getFirst()).length();
/*     */             for (String line : (Iterable<String>)strings) {
/*     */               if (line.length() > 3)
/*     */                 return DataResult.error(()); 
/*     */               if (firstLength != line.length())
/*     */                 return DataResult.error(()); 
/*     */             } 
/*     */             return DataResult.success(strings);
/* 216 */           }, java.util.function.Function.identity());
/*     */       
/* 218 */       SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> (symbol.length() != 1) ? DataResult.error(()) : (" ".equals(symbol) ? DataResult.error(()) : DataResult.success(symbol.charAt(0))), String::valueOf);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(()), (App)PATTERN_CODEC.fieldOf("pattern").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, Data::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/ShapedRecipePattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */