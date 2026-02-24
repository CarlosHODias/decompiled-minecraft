/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class TagKey<T> extends Record {
/*    */   private final ResourceKey<? extends Registry<T>> registry;
/*    */   private final Identifier location;
/*    */   
/* 15 */   public ResourceKey<? extends Registry<T>> registry() { return this.registry; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagKey;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagKey<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 15 */     //   0	8	0	this	Lnet/minecraft/tags/TagKey<TT;>; } public Identifier location() { return this.location; }
/* 16 */    private static final com.google.common.collect.Interner<TagKey<?>> VALUES = com.google.common.collect.Interners.newWeakInterner();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public TagKey(ResourceKey<? extends Registry<T>> registry, Identifier location) {
/* 23 */     this.registry = registry; this.location = location;
/*    */   }
/*    */   
/*    */   public static <T> Codec<TagKey<T>> codec(ResourceKey<? extends Registry<T>> registryName) {
/* 27 */     return Identifier.CODEC.xmap(name -> create(registryName, name), TagKey::location);
/*    */   }
/*    */   
/*    */   public static <T> Codec<TagKey<T>> hashedCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 31 */     return Codec.STRING.comapFlatMap(name -> name.startsWith("#") ? Identifier.read(name.substring(1)).map(()) : DataResult.error(()), e -> "#" + String.valueOf(e.location));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> net.minecraft.network.codec.StreamCodec<ByteBuf, TagKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 37 */     return Identifier.STREAM_CODEC.map(location -> create(registryName, location), TagKey::location);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registry, Identifier location) {
/* 42 */     return (TagKey<T>)VALUES.intern(new TagKey<>(registry, location));
/*    */   }
/*    */   
/*    */   public boolean isFor(ResourceKey<? extends Registry<?>> registry) {
/* 46 */     return (this.registry == registry);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> Optional<TagKey<E>> cast(ResourceKey<? extends Registry<E>> registry) {
/* 51 */     return isFor(registry) ? (Optional)Optional.of(this) : Optional.<TagKey<E>>empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "TagKey[" + String.valueOf(this.registry.identifier()) + " / " + String.valueOf(this.location) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TagKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */