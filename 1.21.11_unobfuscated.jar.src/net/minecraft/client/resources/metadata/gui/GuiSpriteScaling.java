/*     */ package net.minecraft.client.resources.metadata.gui;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public interface GuiSpriteScaling {
/*  14 */   public static final Codec<GuiSpriteScaling> CODEC = Type.CODEC.dispatch(GuiSpriteScaling::type, Type::codec);
/*     */   
/*  16 */   public static final GuiSpriteScaling DEFAULT = new Stretch(); Type type(); public static final class Stretch extends Record implements GuiSpriteScaling { public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;
/*     */     } public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;
/*     */     }
/*  21 */     public static final MapCodec<Stretch> CODEC = MapCodec.unit(Stretch::new);
/*     */ 
/*     */     
/*     */     public GuiSpriteScaling.Type type() {
/*  25 */       return GuiSpriteScaling.Type.STRETCH;
/*     */     } }
/*     */   public static final class Tile extends Record implements GuiSpriteScaling { private final int width; private final int height; public static final MapCodec<Tile> CODEC;
/*     */     
/*  29 */     public Tile(int width, int height) { this.width = width; this.height = height; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*  29 */       //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public int height() { return this.height; } static {
/*  30 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Tile::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Tile::height)).apply((Applicative)i, Tile::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GuiSpriteScaling.Type type() {
/*  37 */       return GuiSpriteScaling.Type.TILE;
/*     */     } }
/*     */   public static final class NineSlice extends Record implements GuiSpriteScaling { private final int width; private final int height; private final Border border; private final boolean stretchInner; public static final MapCodec<NineSlice> CODEC;
/*     */     
/*  41 */     public NineSlice(int width, int height, Border border, boolean stretchInner) { this.width = width; this.height = height; this.border = border; this.stretchInner = stretchInner; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*  41 */       //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public int height() { return this.height; } public Border border() { return this.border; } public boolean stretchInner() { return this.stretchInner; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  47 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(NineSlice::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(NineSlice::height), (App)Border.CODEC.fieldOf("border").forGetter(NineSlice::border), (App)Codec.BOOL.optionalFieldOf("stretch_inner", false).forGetter(NineSlice::stretchInner)).apply((Applicative)i, NineSlice::new)).validate(NineSlice::validate);
/*     */     }
/*     */     private static DataResult<NineSlice> validate(NineSlice nineSlice) {
/*  50 */       Border border = nineSlice.border();
/*  51 */       if (border.left() + border.right() >= nineSlice.width()) {
/*  52 */         return DataResult.error(() -> "Nine-sliced texture has no horizontal center slice: " + border.left() + " + " + border.right() + " >= " + nineSlice.width());
/*     */       }
/*  54 */       if (border.top() + border.bottom() >= nineSlice.height()) {
/*  55 */         return DataResult.error(() -> "Nine-sliced texture has no vertical center slice: " + border.top() + " + " + border.bottom() + " >= " + nineSlice.height());
/*     */       }
/*  57 */       return DataResult.success(nineSlice);
/*     */     }
/*     */ 
/*     */     
/*     */     public GuiSpriteScaling.Type type() {
/*  62 */       return GuiSpriteScaling.Type.NINE_SLICE;
/*     */     }
/*     */     public static final class Border extends Record { private final int left; private final int top; private final int right; private final int bottom; private static final Codec<Border> VALUE_CODEC; private static final Codec<Border> RECORD_CODEC; private static final Codec<Border> CODEC;
/*  65 */       public Border(int left, int top, int right, int bottom) { this.left = left; this.top = top; this.right = right; this.bottom = bottom; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #65	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #65	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #65	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;
/*  65 */         //   0	8	1	o	Ljava/lang/Object; } public int left() { return this.left; } public int top() { return this.top; } public int right() { return this.right; } public int bottom() { return this.bottom; } static {
/*  66 */         VALUE_CODEC = ExtraCodecs.POSITIVE_INT.flatComapMap(size -> new Border(size, size, size, size), border -> {
/*     */               OptionalInt size = border.unpackValue();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               return size.isPresent() ? DataResult.success(size.getAsInt()) : DataResult.error(());
/*     */             });
/*     */ 
/*     */ 
/*     */         
/*  77 */         RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(Border::left), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(Border::top), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(Border::right), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(Border::bottom)).apply((Applicative)i, Border::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  85 */         CODEC = Codec.either(VALUE_CODEC, RECORD_CODEC).xmap(Either::unwrap, border -> border.unpackValue().isPresent() ? Either.left(border) : Either.right(border));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private OptionalInt unpackValue()
/*     */       {
/*  94 */         if (left() == top() && top() == right() && right() == bottom()) {
/*  95 */           return OptionalInt.of(left());
/*     */         }
/*  97 */         return OptionalInt.empty(); } } } public static final class Border extends Record { private final int left; private final int top; private final int right; private final int bottom; private static final Codec<Border> VALUE_CODEC; private static final Codec<Border> RECORD_CODEC; private static final Codec<Border> CODEC; public Border(int left, int top, int right, int bottom) { this.left = left; this.top = top; this.right = right; this.bottom = bottom; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;
/*  97 */       //   0	8	1	o	Ljava/lang/Object; } public int left() { return this.left; } private OptionalInt unpackValue() { if (left() == top() && top() == right() && right() == bottom()) return OptionalInt.of(left());  return OptionalInt.empty(); }
/*     */     public int top() { return this.top; } public int right() { return this.right; } public int bottom() { return this.bottom; } static { VALUE_CODEC = ExtraCodecs.POSITIVE_INT.flatComapMap(size -> new Border(size, size, size, size), border -> {
/*     */             OptionalInt size = border.unpackValue(); return size.isPresent() ? DataResult.success(size.getAsInt()) : DataResult.error(());
/*     */           }); RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(Border::left), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(Border::top), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(Border::right), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(Border::bottom)).apply((Applicative)i, Border::new)); CODEC = Codec.either(VALUE_CODEC, RECORD_CODEC).xmap(Either::unwrap, border -> border.unpackValue().isPresent() ? Either.left(border) : Either.right(border)); } }
/*     */    public enum Type implements net.minecraft.util.StringRepresentable
/*     */   {
/* 103 */     STRETCH("stretch", GuiSpriteScaling.Stretch.CODEC),
/* 104 */     TILE("tile", GuiSpriteScaling.Tile.CODEC),
/* 105 */     NINE_SLICE("nine_slice", GuiSpriteScaling.NineSlice.CODEC);
/*     */ 
/*     */     
/* 108 */     public static final Codec<Type> CODEC = (Codec<Type>)net.minecraft.util.StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String key;
/*     */     private final MapCodec<? extends GuiSpriteScaling> codec;
/*     */     
/*     */     Type(String key, MapCodec<? extends GuiSpriteScaling> codec) {
/* 114 */       this.key = key;
/* 115 */       this.codec = codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 120 */       return this.key;
/*     */     }
/*     */     
/*     */     public MapCodec<? extends GuiSpriteScaling> codec() {
/* 124 */       return this.codec;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/gui/GuiSpriteScaling.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */