/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public final class TagPredicate<T> extends Record {
/*    */   private final TagKey<T> tag;
/*    */   private final boolean expected;
/*    */   
/* 10 */   public TagPredicate(TagKey<T> tag, boolean expected) { this.tag = tag; this.expected = expected; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/TagPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public TagKey<T> tag() { return this.tag; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/TagPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/TagPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public boolean expected() { return this.expected; }
/*    */    public static <T> com.mojang.serialization.Codec<TagPredicate<T>> codec(net.minecraft.resources.ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey) {
/* 12 */     return RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)TagKey.codec(registryKey).fieldOf("id").forGetter(TagPredicate::tag), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.fieldOf("expected").forGetter(TagPredicate::expected)).apply((com.mojang.datafixers.kinds.Applicative)i, TagPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> TagPredicate<T> is(TagKey<T> tag) {
/* 19 */     return new TagPredicate<>(tag, true);
/*    */   }
/*    */   
/*    */   public static <T> TagPredicate<T> isNot(TagKey<T> tag) {
/* 23 */     return new TagPredicate<>(tag, false);
/*    */   }
/*    */   
/*    */   public boolean matches(net.minecraft.core.Holder<T> holder) {
/* 27 */     return (holder.is(this.tag) == this.expected);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/TagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */