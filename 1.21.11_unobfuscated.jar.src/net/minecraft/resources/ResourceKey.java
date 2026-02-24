/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.google.common.collect.MapMaker;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ResourceKey<T> {
/*    */   private static final class InternKey extends Record {
/*    */     private final Identifier registry;
/*    */     private final Identifier identifier;
/*    */     
/*    */     private InternKey(Identifier registry, Identifier identifier) {
/* 18 */       this.registry = registry; this.identifier = identifier; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/ResourceKey$InternKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/ResourceKey$InternKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/ResourceKey$InternKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier registry() { return this.registry; } public Identifier identifier() { return this.identifier; }
/*    */      }
/* 20 */   private static final ConcurrentMap<InternKey, ResourceKey<?>> VALUES = new MapMaker().weakValues().makeMap();
/*    */   
/*    */   private final Identifier registryName;
/*    */   private final Identifier identifier;
/*    */   
/*    */   public static <T> Codec<ResourceKey<T>> codec(ResourceKey<? extends Registry<T>> registryName) {
/* 26 */     return Identifier.CODEC.xmap(name -> create(registryName, name), ResourceKey::identifier);
/*    */   }
/*    */   
/*    */   public static <T> StreamCodec<ByteBuf, ResourceKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryName) {
/* 30 */     return Identifier.STREAM_CODEC.map(name -> create(registryName, name), ResourceKey::identifier);
/*    */   }
/*    */   
/*    */   public static <T> ResourceKey<T> create(ResourceKey<? extends Registry<T>> registryName, Identifier location) {
/* 34 */     return create(registryName.identifier, location);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> ResourceKey<Registry<T>> createRegistryKey(Identifier identifier) {
/* 41 */     return create(Registries.ROOT_REGISTRY_NAME, identifier);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> ResourceKey<T> create(Identifier registryName, Identifier identifier) {
/* 46 */     return (ResourceKey<T>)VALUES.computeIfAbsent(new InternKey(registryName, identifier), k -> new ResourceKey(k.registry, k.identifier));
/*    */   }
/*    */   
/*    */   private ResourceKey(Identifier registryName, Identifier identifier) {
/* 50 */     this.registryName = registryName;
/* 51 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "ResourceKey[" + String.valueOf(this.registryName) + " / " + String.valueOf(this.identifier) + "]";
/*    */   }
/*    */   
/*    */   public boolean isFor(ResourceKey<? extends Registry<?>> registry) {
/* 60 */     return this.registryName.equals(registry.identifier());
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> Optional<ResourceKey<E>> cast(ResourceKey<? extends Registry<E>> registry) {
/* 65 */     return isFor(registry) ? (Optional)Optional.of(this) : Optional.<ResourceKey<E>>empty();
/*    */   }
/*    */   
/*    */   public Identifier identifier() {
/* 69 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public Identifier registry() {
/* 73 */     return this.registryName;
/*    */   }
/*    */   
/*    */   public ResourceKey<Registry<T>> registryKey() {
/* 77 */     return createRegistryKey(this.registryName);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/ResourceKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */