/*    */ package net.minecraft.tags;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ 
/*    */ public class TagNetworkSerialization {
/*    */   public static Map<ResourceKey<? extends Registry<?>>, NetworkPayload> serializeTagsToNetwork(LayeredRegistryAccess<RegistryLayer> registries) {
/* 23 */     return (Map<ResourceKey<? extends Registry<?>>, NetworkPayload>)net.minecraft.core.RegistrySynchronization.networkSafeRegistries(registries)
/* 24 */       .map(e -> Pair.of(e.key(), serializeToNetwork(e.value())))
/* 25 */       .filter(e -> !((NetworkPayload)e.getSecond()).isEmpty())
/* 26 */       .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
/*    */   }
/*    */   
/*    */   private static <T> NetworkPayload serializeToNetwork(Registry<T> registry) {
/* 30 */     Map<Identifier, IntList> result = new HashMap<>();
/* 31 */     registry.getTags().forEach(tag -> {
/*    */           IntArrayList intArrayList = new IntArrayList(tag.size());
/*    */           for (Holder<T> holder : (Iterable<Holder<T>>)tag) {
/*    */             if (holder.kind() != Holder.Kind.REFERENCE) {
/*    */               throw new IllegalStateException("Can't serialize unregistered value " + String.valueOf(holder));
/*    */             }
/*    */             intArrayList.add(registry.getId(holder.value()));
/*    */           } 
/*    */           result.put(tag.key().location(), intArrayList);
/*    */         });
/* 41 */     return new NetworkPayload(result);
/*    */   }
/*    */   
/*    */   private static <T> TagLoader.LoadResult<T> deserializeTagsFromNetwork(Registry<T> registry, NetworkPayload payload) {
/* 45 */     ResourceKey<? extends Registry<T>> registryKey = registry.key();
/* 46 */     Map<TagKey<T>, List<Holder<T>>> tags = new HashMap<>();
/*    */     
/* 48 */     payload.tags.forEach((key, ids) -> {
/*    */           TagKey<T> tagKey = TagKey.create(registryKey, key);
/*    */           Objects.requireNonNull(registry);
/*    */           List<Holder<T>> values = (List<Holder<T>>)ids.intStream().mapToObj(registry::get).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/*    */           tags.put(tagKey, values);
/*    */         });
/* 54 */     return new TagLoader.LoadResult<>(registryKey, tags);
/*    */   }
/*    */   
/*    */   public static final class NetworkPayload {
/* 58 */     public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */     
/*    */     private final Map<Identifier, IntList> tags;
/*    */     
/*    */     NetworkPayload(Map<Identifier, IntList> tags) {
/* 63 */       this.tags = tags;
/*    */     }
/*    */     
/*    */     public void write(FriendlyByteBuf buf) {
/* 67 */       buf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList);
/*    */     }
/*    */     
/*    */     public static NetworkPayload read(FriendlyByteBuf buf) {
/* 71 */       return new NetworkPayload(buf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList));
/*    */     }
/*    */     
/*    */     public boolean isEmpty() {
/* 75 */       return this.tags.isEmpty();
/*    */     }
/*    */     
/*    */     public int size() {
/* 79 */       return this.tags.size();
/*    */     }
/*    */     
/*    */     public <T> TagLoader.LoadResult<T> resolve(Registry<T> registry) {
/* 83 */       return TagNetworkSerialization.deserializeTagsFromNetwork(registry, this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TagNetworkSerialization.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */