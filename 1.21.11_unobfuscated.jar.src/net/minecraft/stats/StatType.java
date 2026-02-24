/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class StatType<T>
/*    */   implements Iterable<Stat<T>> {
/*    */   private final Registry<T> registry;
/* 15 */   private final Map<T, Stat<T>> map = new IdentityHashMap<>();
/*    */   
/*    */   private final Component displayName;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec;
/*    */   
/*    */   public StatType(Registry<T> registry, Component displayName) {
/* 21 */     this.registry = registry;
/* 22 */     this.displayName = displayName;
/* 23 */     this.streamCodec = ByteBufCodecs.registry(registry.key()).map(this::get, Stat::getValue);
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec() {
/* 27 */     return this.streamCodec;
/*    */   }
/*    */   
/*    */   public boolean contains(T key) {
/* 31 */     return this.map.containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stat<T> get(T argument, StatFormatter formatter) {
/* 36 */     return this.map.computeIfAbsent(argument, t -> new Stat<>(this, (T)formatter, formatter));
/*    */   }
/*    */   
/*    */   public Registry<T> getRegistry() {
/* 40 */     return this.registry;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Stat<T>> iterator() {
/* 45 */     return this.map.values().iterator();
/*    */   }
/*    */   
/*    */   public Stat<T> get(T argument) {
/* 49 */     return get(argument, StatFormatter.DEFAULT);
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 53 */     return this.displayName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/StatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */