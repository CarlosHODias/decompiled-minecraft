/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.BitSet;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class FilterMask
/*     */ {
/*  16 */   public static final Codec<FilterMask> CODEC = StringRepresentable.fromEnum(Type::values).dispatch(FilterMask::type, Type::codec);
/*     */   
/*  18 */   public static final FilterMask FULLY_FILTERED = new FilterMask(new BitSet(0), Type.FULLY_FILTERED);
/*  19 */   public static final FilterMask PASS_THROUGH = new FilterMask(new BitSet(0), Type.PASS_THROUGH);
/*  20 */   public static final Style FILTERED_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.filtered")));
/*     */   
/*  22 */   private static final MapCodec<FilterMask> PASS_THROUGH_CODEC = MapCodec.unit(PASS_THROUGH);
/*  23 */   private static final MapCodec<FilterMask> FULLY_FILTERED_CODEC = MapCodec.unit(FULLY_FILTERED);
/*  24 */   private static final MapCodec<FilterMask> PARTIALLY_FILTERED_CODEC = ExtraCodecs.BIT_SET.xmap(FilterMask::new, FilterMask::mask).fieldOf("value");
/*     */   
/*     */   private static final char HASH = '#';
/*     */   
/*     */   private final BitSet mask;
/*     */   private final Type type;
/*     */   
/*     */   private FilterMask(BitSet mask, Type type) {
/*  32 */     this.mask = mask;
/*  33 */     this.type = type;
/*     */   }
/*     */   
/*     */   private FilterMask(BitSet mask) {
/*  37 */     this.mask = mask;
/*  38 */     this.type = Type.PARTIALLY_FILTERED;
/*     */   }
/*     */   
/*     */   public FilterMask(int length) {
/*  42 */     this(new BitSet(length), Type.PARTIALLY_FILTERED);
/*     */   }
/*     */   
/*     */   private Type type() {
/*  46 */     return this.type;
/*     */   }
/*     */   
/*     */   private BitSet mask() {
/*  50 */     return this.mask;
/*     */   }
/*     */   
/*     */   public static FilterMask read(FriendlyByteBuf input) {
/*  54 */     Type type = (Type)input.readEnum(Type.class);
/*  55 */     switch (type.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*     */ 
/*     */       
/*  58 */       new FilterMask(input.readBitSet(), Type.PARTIALLY_FILTERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void write(FriendlyByteBuf output, FilterMask mask) {
/*  63 */     output.writeEnum(mask.type);
/*  64 */     if (mask.type == Type.PARTIALLY_FILTERED) {
/*  65 */       output.writeBitSet(mask.mask);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFiltered(int index) {
/*  70 */     this.mask.set(index);
/*     */   } public String apply(String text) {
/*     */     char[] chars;
/*     */     int i;
/*  74 */     switch (this.type.ordinal()) { default: throw new MatchException(null, null);
/*     */       case 1: 
/*     */       case 0: 
/*     */       case 2:
/*  78 */         chars = text.toCharArray();
/*  79 */         for (i = 0; i < chars.length && i < this.mask.length(); i++) {
/*  80 */           if (this.mask.get(i))
/*  81 */             chars[i] = '#'; 
/*     */         }  }
/*     */     
/*  84 */     return new String(chars);
/*     */   }
/*     */   public Component applyWithFormatting(String text) {
/*     */     MutableComponent result;
/*     */     int previousIndex;
/*     */     boolean filtered;
/*  90 */     switch (this.type.ordinal()) { default: throw new MatchException(null, null);
/*     */       case 1: 
/*     */       case 0: 
/*     */       case 2:
/*  94 */         result = Component.empty();
/*  95 */         previousIndex = 0;
/*  96 */         filtered = this.mask.get(0);
/*     */         while (true) {
/*  98 */           int nextIndex = filtered ? this.mask.nextClearBit(previousIndex) : this.mask.nextSetBit(previousIndex);
/*  99 */           nextIndex = (nextIndex < 0) ? text.length() : nextIndex;
/* 100 */           if (nextIndex == previousIndex) {
/*     */             break;
/*     */           }
/* 103 */           if (filtered) {
/* 104 */             result.append(Component.literal(StringUtils.repeat('#', nextIndex - previousIndex)).withStyle(FILTERED_STYLE));
/*     */           } else {
/* 106 */             result.append(text.substring(previousIndex, nextIndex));
/*     */           } 
/* 108 */           filtered = !filtered;
/* 109 */           previousIndex = nextIndex;
/*     */         }  }
/* 111 */      return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 117 */     return (this.type == Type.PASS_THROUGH);
/*     */   }
/*     */   
/*     */   public boolean isFullyFiltered() {
/* 121 */     return (this.type == Type.FULLY_FILTERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 126 */     if (this == o) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (o == null || getClass() != o.getClass()) {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     FilterMask that = (FilterMask)o;
/*     */     
/* 135 */     return (this.mask.equals(that.mask) && this.type == that.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 140 */     int result = this.mask.hashCode();
/* 141 */     result = 31 * result + this.type.hashCode();
/* 142 */     return result;
/*     */   }
/*     */   
/*     */   private enum Type implements StringRepresentable {
/* 146 */     PASS_THROUGH("pass_through", () -> FilterMask.PASS_THROUGH_CODEC),
/* 147 */     FULLY_FILTERED("fully_filtered", () -> FilterMask.FULLY_FILTERED_CODEC),
/* 148 */     PARTIALLY_FILTERED("partially_filtered", () -> FilterMask.PARTIALLY_FILTERED_CODEC);
/*     */     
/*     */     private final String serializedName;
/*     */     
/*     */     private final Supplier<MapCodec<FilterMask>> codec;
/*     */     
/*     */     Type(String serializedName, Supplier<MapCodec<FilterMask>> codec) {
/* 155 */       this.serializedName = serializedName;
/* 156 */       this.codec = codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 161 */       return this.serializedName;
/*     */     }
/*     */     
/*     */     private MapCodec<FilterMask> codec() {
/* 165 */       return this.codec.get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/FilterMask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */