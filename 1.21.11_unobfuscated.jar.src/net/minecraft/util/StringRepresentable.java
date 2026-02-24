/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ public interface StringRepresentable
/*     */ {
/*     */   public static final int PRE_BUILT_MAP_THRESHOLD = 16;
/*     */   
/*     */   public static class StringRepresentableCodec<S extends StringRepresentable>
/*     */     implements Codec<S>
/*     */   {
/*     */     private final Codec<S> codec;
/*     */     
/*     */     public StringRepresentableCodec(S[] valueArray, Function<String, S> nameResolver, ToIntFunction<S> idResolver) {
/*  28 */       this.codec = ExtraCodecs.orCompressed(
/*  29 */           Codec.stringResolver(StringRepresentable::getSerializedName, nameResolver), 
/*  30 */           ExtraCodecs.idResolverCodec(idResolver, i -> (i >= 0 && i < valueArray.length) ? valueArray[i] : null, -1));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
/*  36 */       return this.codec.decode(ops, input);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
/*  41 */       return this.codec.encode(input, ops, prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EnumCodec<E extends Enum<E> & StringRepresentable> extends StringRepresentableCodec<E> {
/*     */     private final Function<String, E> resolver;
/*     */     
/*     */     public EnumCodec(E[] valueArray, Function<String, E> nameResolver) {
/*  49 */       super(valueArray, nameResolver, rec$ -> ((Enum)rec$).ordinal());
/*  50 */       this.resolver = nameResolver;
/*     */     }
/*     */     
/*     */     public E byName(String name) {
/*  54 */       return this.resolver.apply(name);
/*     */     }
/*     */     
/*     */     public E byName(String name, E _default) {
/*  58 */       return (E)Objects.<Enum>requireNonNullElse((Enum)byName(name), (Enum)_default);
/*     */     }
/*     */     
/*     */     public E byName(String name, Supplier<? extends E> defaultSupplier) {
/*  62 */       return (E)Objects.<Enum>requireNonNullElseGet((Enum)byName(name), defaultSupplier);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E extends Enum<E> & StringRepresentable> EnumCodec<E> fromEnum(Supplier<E[]> values) {
/*  70 */     return fromEnumWithMapping(values, s -> s);
/*     */   }
/*     */   
/*     */   static <E extends Enum<E> & StringRepresentable> EnumCodec<E> fromEnumWithMapping(Supplier<E[]> values, Function<String, String> converter) {
/*  74 */     Enum[] arrayOfEnum = (Enum[])values.get();
/*  75 */     Function<String, E> lookupFunction = createNameLookup((E[])arrayOfEnum, e -> (String)converter.apply(((StringRepresentable)e).getSerializedName()));
/*  76 */     return new EnumCodec<>((E[])arrayOfEnum, lookupFunction);
/*     */   }
/*     */   
/*     */   static <T extends StringRepresentable> Codec<T> fromValues(Supplier<T[]> values) {
/*  80 */     StringRepresentable[] arrayOfStringRepresentable = (StringRepresentable[])values.get();
/*  81 */     Function<String, T> lookupFunction = createNameLookup((T[])arrayOfStringRepresentable);
/*  82 */     ToIntFunction<T> indexLookup = Util.createIndexLookup(Arrays.asList((T[])arrayOfStringRepresentable));
/*  83 */     return new StringRepresentableCodec<>((T[])arrayOfStringRepresentable, lookupFunction, indexLookup);
/*     */   }
/*     */   
/*     */   static <T extends StringRepresentable> Function<String, T> createNameLookup(T[] valueArray) {
/*  87 */     return createNameLookup(valueArray, StringRepresentable::getSerializedName);
/*     */   }
/*     */   
/*     */   static <T> Function<String, T> createNameLookup(T[] valueArray, Function<T, String> converter) {
/*  91 */     if (valueArray.length > 16) {
/*  92 */       Map<String, T> byName = (Map<String, T>)Arrays.<T>stream(valueArray).collect(Collectors.toMap(converter, d -> d));
/*  93 */       Objects.requireNonNull(byName); return byName::get;
/*     */     } 
/*  95 */     return id -> {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: astore_3
/*     */         //   2: aload_3
/*     */         //   3: arraylength
/*     */         //   4: istore #4
/*     */         //   6: iconst_0
/*     */         //   7: istore #5
/*     */         //   9: iload #5
/*     */         //   11: iload #4
/*     */         //   13: if_icmpge -> 49
/*     */         //   16: aload_3
/*     */         //   17: iload #5
/*     */         //   19: aaload
/*     */         //   20: astore #6
/*     */         //   22: aload_1
/*     */         //   23: aload #6
/*     */         //   25: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */         //   30: checkcast java/lang/String
/*     */         //   33: aload_2
/*     */         //   34: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */         //   37: ifeq -> 43
/*     */         //   40: aload #6
/*     */         //   42: areturn
/*     */         //   43: iinc #5, 1
/*     */         //   46: goto -> 9
/*     */         //   49: aconst_null
/*     */         //   50: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #96	-> 0
/*     */         //   #97	-> 22
/*     */         //   #98	-> 40
/*     */         //   #96	-> 43
/*     */         //   #101	-> 49
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   22	21	6	value	Ljava/lang/Object;
/*     */         //   0	51	0	valueArray	[Ljava/lang/Object;
/*     */         //   0	51	1	converter	Ljava/util/function/Function;
/*     */         //   0	51	2	id	Ljava/lang/String;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   22	21	6	value	TT;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Keyable keys(final StringRepresentable[] values) {
/* 106 */     return new Keyable()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 109 */           Objects.requireNonNull(ops); return Arrays.<StringRepresentable>stream(values).map(StringRepresentable::getSerializedName).map(ops::createString);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   String getSerializedName();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/StringRepresentable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */