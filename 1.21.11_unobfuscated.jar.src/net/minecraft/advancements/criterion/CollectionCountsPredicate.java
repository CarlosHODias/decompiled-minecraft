/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public interface CollectionCountsPredicate<T, P extends Predicate<T>> extends Predicate<Iterable<T>> {
/*    */   List<Entry<T, P>> unpack();
/*    */   
/*    */   static <T, P extends Predicate<T>> Codec<CollectionCountsPredicate<T, P>> codec(Codec<P> elementCodec) {
/* 13 */     return Entry.<T, P>codec(elementCodec).listOf().xmap(CollectionCountsPredicate::of, CollectionCountsPredicate::unpack);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   static <T, P extends Predicate<T>> CollectionCountsPredicate<T, P> of(Entry<T, P>... predicates) {
/* 21 */     return of(List.of(predicates));
/*    */   }
/*    */   
/*    */   static <T, P extends Predicate<T>> CollectionCountsPredicate<T, P> of(List<Entry<T, P>> predicates) {
/* 25 */     switch (predicates.size()) { case 0: case 1: default: break; }  return 
/*    */ 
/*    */       
/* 28 */       new Multiple<>(predicates);
/*    */   }
/*    */   
/*    */   public static class Zero<T, P extends Predicate<T>>
/*    */     implements CollectionCountsPredicate<T, P>
/*    */   {
/*    */     public boolean test(Iterable<T> values) {
/* 35 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public List<CollectionCountsPredicate.Entry<T, P>> unpack() {
/* 40 */       return List.of();
/*    */     } }
/*    */   public static final class Single<T, P extends Predicate<T>> extends Record implements CollectionCountsPredicate<T, P> { private final CollectionCountsPredicate.Entry<T, P> entry;
/*    */     
/* 44 */     public Single(CollectionCountsPredicate.Entry<T, P> entry) { this.entry = entry; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 44 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single<TT;TP;>; } public CollectionCountsPredicate.Entry<T, P> entry() { return this.entry; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single<TT;TP;>; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Single<TT;TP;>; } public boolean test(Iterable<T> values) {
/* 47 */       return this.entry.test(values);
/*    */     }
/*    */ 
/*    */     
/*    */     public List<CollectionCountsPredicate.Entry<T, P>> unpack() {
/* 52 */       return List.of(this.entry);
/*    */     } }
/*    */   public static final class Multiple<T, P extends Predicate<T>> extends Record implements CollectionCountsPredicate<T, P> { private final List<CollectionCountsPredicate.Entry<T, P>> entries;
/*    */     
/* 56 */     public Multiple(List<CollectionCountsPredicate.Entry<T, P>> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 56 */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; } public List<CollectionCountsPredicate.Entry<T, P>> entries() { return this.entries; }
/*    */     
/*    */     public boolean test(Iterable<T> values) {
/* 59 */       for (CollectionCountsPredicate.Entry<T, P> entry : this.entries) {
/* 60 */         if (!entry.test(values)) {
/* 61 */           return false;
/*    */         }
/*    */       } 
/* 64 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public List<CollectionCountsPredicate.Entry<T, P>> unpack() {
/* 69 */       return this.entries;
/*    */     } }
/*    */   public static final class Entry<T, P extends Predicate<T>> extends Record { private final P test; private final MinMaxBounds.Ints count;
/*    */     
/* 73 */     public Entry(P test, MinMaxBounds.Ints count) { this.test = test; this.count = count; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry<TT;TP;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry<TT;TP;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 73 */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Entry<TT;TP;>; } public P test() { return this.test; } public MinMaxBounds.Ints count() { return this.count; }
/*    */      public static <T, P extends Predicate<T>> Codec<Entry<T, P>> codec(Codec<P> elementCodec) {
/* 75 */       return RecordCodecBuilder.create(i -> i.group((App)elementCodec.fieldOf("test").forGetter(Entry::test), (App)MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(Entry::count)).apply((Applicative)i, Entry::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean test(Iterable<T> values) {
/* 82 */       int count = 0;
/* 83 */       for (T value : values) {
/* 84 */         if (this.test.test(value)) {
/* 85 */           count++;
/*    */         }
/*    */       } 
/* 88 */       return this.count.matches(count);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/CollectionCountsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */