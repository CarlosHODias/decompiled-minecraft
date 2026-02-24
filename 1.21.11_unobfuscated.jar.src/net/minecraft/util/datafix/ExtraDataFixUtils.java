/*     */ package net.minecraft.util.datafix;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ExtraDataFixUtils {
/*     */   public static Dynamic<?> fixBlockPos(Dynamic<?> pos) {
/*  28 */     Optional<Number> x = pos.get("X").asNumber().result();
/*  29 */     Optional<Number> y = pos.get("Y").asNumber().result();
/*  30 */     Optional<Number> z = pos.get("Z").asNumber().result();
/*  31 */     if (x.isEmpty() || y.isEmpty() || z.isEmpty())
/*     */     {
/*  33 */       return pos;
/*     */     }
/*  35 */     return createBlockPos(pos, ((Number)x.get()).intValue(), ((Number)y.get()).intValue(), ((Number)z.get()).intValue());
/*     */   }
/*     */   
/*     */   public static Dynamic<?> fixInlineBlockPos(Dynamic<?> input, String fieldX, String fieldY, String fieldZ, String newField) {
/*  39 */     Optional<Number> x = input.get(fieldX).asNumber().result();
/*  40 */     Optional<Number> y = input.get(fieldY).asNumber().result();
/*  41 */     Optional<Number> z = input.get(fieldZ).asNumber().result();
/*  42 */     if (x.isEmpty() || y.isEmpty() || z.isEmpty()) {
/*  43 */       return input;
/*     */     }
/*  45 */     return input.remove(fieldX).remove(fieldY).remove(fieldZ)
/*  46 */       .set(newField, createBlockPos(input, ((Number)x.get()).intValue(), ((Number)y.get()).intValue(), ((Number)z.get()).intValue()));
/*     */   }
/*     */   
/*     */   public static Dynamic<?> createBlockPos(Dynamic<?> dynamic, int x, int y, int z) {
/*  50 */     return dynamic.createIntList(IntStream.of(new int[] { x, y, z }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, R> Typed<R> cast(Type<R> type, Typed<T> typed) {
/*  57 */     return new Typed(type, typed.getOps(), typed.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Typed<T> cast(Type<T> type, Object value, DynamicOps<?> ops) {
/*  62 */     return new Typed(type, ops, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type<?> patchSubType(Type<?> type, Type<?> find, Type<?> replace) {
/*  67 */     return type.all(typePatcher(find, replace), true, false).view().newType();
/*     */   }
/*     */   
/*     */   private static <A, B> TypeRewriteRule typePatcher(Type<A> inputEntityType, Type<B> outputEntityType) {
/*  71 */     RewriteResult<A, B> view = RewriteResult.create(View.create("Patcher", inputEntityType, outputEntityType, ops -> ()), new BitSet());
/*     */ 
/*     */ 
/*     */     
/*  75 */     return TypeRewriteRule.everywhere(TypeRewriteRule.ifSame(inputEntityType, view), PointFreeRule.nop(), true, true);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Function<Typed<?>, Typed<?>> chainAllFilters(Function<Typed<?>, Typed<?>>... fixers) {
/*  80 */     return typed -> {
/*     */         for (Function<Typed<?>, Typed<?>> fixer : fixers) {
/*     */           typed = fixer.apply(typed);
/*     */         }
/*     */         return typed;
/*     */       };
/*     */   }
/*     */   
/*     */   public static Dynamic<?> blockState(String id, Map<String, String> properties) {
/*  89 */     Dynamic<Tag> dynamic = new Dynamic((DynamicOps)NbtOps.INSTANCE, new CompoundTag());
/*  90 */     Dynamic<Tag> blockState = dynamic.set("Name", dynamic.createString(id));
/*  91 */     if (!properties.isEmpty()) {
/*  92 */       blockState = blockState.set("Properties", dynamic.createMap((Map)properties.entrySet().stream()
/*  93 */             .collect(Collectors.toMap(entry -> dynamic.createString((String)entry.getKey()), entry -> dynamic.createString((String)entry.getValue())))));
/*     */     }
/*     */     
/*  96 */     return blockState;
/*     */   }
/*     */   
/*     */   public static Dynamic<?> blockState(String id) {
/* 100 */     return blockState(id, Map.of());
/*     */   }
/*     */   
/*     */   public static Dynamic<?> fixStringField(Dynamic<?> dynamic, String fieldName, UnaryOperator<String> fix) {
/* 104 */     return dynamic.update(fieldName, field -> {
/*     */           Objects.requireNonNull(dynamic);
/*     */           return DataFixUtils.orElse(field.asString().map(fix).map(dynamic::createString).result(), field);
/*     */         });
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
/*     */   public static String dyeColorIdToName(int id) {
/* 119 */     switch (id) { default: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: break; }  return 
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
/* 135 */       "black";
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Typed<?> readAndSet(Typed<?> target, OpticFinder<T> optic, Dynamic<?> value) {
/* 140 */     return target.set(optic, Util.readTypedOrThrow(optic.type(), value, true));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/ExtraDataFixUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */