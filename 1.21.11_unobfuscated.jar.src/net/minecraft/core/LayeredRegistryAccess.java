/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayeredRegistryAccess<T>
/*     */ {
/*     */   private final List<T> keys;
/*     */   private final List<RegistryAccess.Frozen> values;
/*     */   private final RegistryAccess.Frozen composite;
/*     */   
/*     */   public LayeredRegistryAccess(List<T> keys) {
/*  23 */     this(keys, 
/*     */         
/*  25 */         (List<RegistryAccess.Frozen>)Util.make(() -> {
/*     */             RegistryAccess.Frozen[] layers = new RegistryAccess.Frozen[keys.size()];
/*     */             Arrays.fill((Object[])layers, RegistryAccess.EMPTY);
/*     */             return Arrays.asList(layers);
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   private LayeredRegistryAccess(List<T> keys, List<RegistryAccess.Frozen> values) {
/*  34 */     this.keys = List.copyOf(keys);
/*  35 */     this.values = List.copyOf(values);
/*  36 */     this.composite = new RegistryAccess.ImmutableRegistryAccess(collectRegistries(values.stream())).freeze();
/*     */   }
/*     */   
/*     */   private int getLayerIndexOrThrow(T layer) {
/*  40 */     int index = this.keys.indexOf(layer);
/*  41 */     if (index == -1) {
/*  42 */       throw new IllegalStateException("Can't find " + String.valueOf(layer) + " inside " + String.valueOf(this.keys));
/*     */     }
/*  44 */     return index;
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getLayer(T layer) {
/*  48 */     int index = getLayerIndexOrThrow(layer);
/*  49 */     return this.values.get(index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessForLoading(T forLayer) {
/*  53 */     int index = getLayerIndexOrThrow(forLayer);
/*  54 */     return getCompositeAccessForLayers(0, index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessFrom(T forLayer) {
/*  58 */     int index = getLayerIndexOrThrow(forLayer);
/*  59 */     return getCompositeAccessForLayers(index, this.values.size());
/*     */   }
/*     */   
/*     */   private RegistryAccess.Frozen getCompositeAccessForLayers(int from, int to) {
/*  63 */     return new RegistryAccess.ImmutableRegistryAccess(collectRegistries(this.values.subList(from, to).stream())).freeze();
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, RegistryAccess.Frozen... layers) {
/*  67 */     return replaceFrom(fromLayer, Arrays.asList(layers));
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, List<RegistryAccess.Frozen> layers) {
/*  71 */     int index = getLayerIndexOrThrow(fromLayer);
/*     */     
/*  73 */     if (layers.size() > this.values.size() - index) {
/*  74 */       throw new IllegalStateException("Too many values to replace");
/*     */     }
/*     */     
/*  77 */     List<RegistryAccess.Frozen> newValues = new ArrayList<>();
/*     */     
/*  79 */     for (int i = 0; i < index; i++) {
/*  80 */       newValues.add(this.values.get(i));
/*     */     }
/*     */     
/*  83 */     newValues.addAll(layers);
/*     */     
/*  85 */     while (newValues.size() < this.values.size()) {
/*  86 */       newValues.add(RegistryAccess.EMPTY);
/*     */     }
/*  88 */     return new LayeredRegistryAccess(this.keys, newValues);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen compositeAccess() {
/*  92 */     return this.composite;
/*     */   }
/*     */   
/*     */   private static Map<ResourceKey<? extends Registry<?>>, Registry<?>> collectRegistries(Stream<? extends RegistryAccess> registries) {
/*  96 */     Map<ResourceKey<? extends Registry<?>>, Registry<?>> result = new HashMap<>();
/*     */     
/*  98 */     registries.forEach(access -> access.registries().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/LayeredRegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */