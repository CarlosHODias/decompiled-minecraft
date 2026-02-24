/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LootParams
/*     */ {
/*     */   private final ServerLevel level;
/*     */   private final ContextMap params;
/*     */   private final Map<Identifier, DynamicDrop> dynamicDrops;
/*     */   private final float luck;
/*     */   
/*     */   public LootParams(ServerLevel level, ContextMap params, Map<Identifier, DynamicDrop> dynamicDrops, float luck) {
/*  27 */     this.level = level;
/*  28 */     this.params = params;
/*  29 */     this.dynamicDrops = dynamicDrops;
/*  30 */     this.luck = luck;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/*  34 */     return this.level;
/*     */   }
/*     */   
/*     */   public ContextMap contextMap() {
/*  38 */     return this.params;
/*     */   }
/*     */   
/*     */   public void addDynamicDrops(Identifier location, Consumer<ItemStack> output) {
/*  42 */     DynamicDrop dynamicDrop = this.dynamicDrops.get(location);
/*  43 */     if (dynamicDrop != null) {
/*  44 */       dynamicDrop.add(output);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getLuck() {
/*  49 */     return this.luck;
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface DynamicDrop {
/*     */     void add(Consumer<ItemStack> param1Consumer); }
/*  54 */   public static class Builder { private final ContextMap.Builder params = new ContextMap.Builder(); private final ServerLevel level;
/*  55 */     private final Map<Identifier, LootParams.DynamicDrop> dynamicDrops = Maps.newHashMap();
/*     */     private float luck;
/*     */     
/*     */     public Builder(ServerLevel level) {
/*  59 */       this.level = level;
/*     */     }
/*     */     
/*     */     public ServerLevel getLevel() {
/*  63 */       return this.level;
/*     */     }
/*     */     
/*     */     public <T> Builder withParameter(ContextKey<T> param, T value) {
/*  67 */       this.params.withParameter(param, value);
/*  68 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder withOptionalParameter(ContextKey<T> param, T value) {
/*  72 */       this.params.withOptionalParameter(param, value);
/*  73 */       return this;
/*     */     }
/*     */     
/*     */     public <T> T getParameter(ContextKey<T> param) {
/*  77 */       return (T)this.params.getParameter(param);
/*     */     }
/*     */     
/*     */     public <T> T getOptionalParameter(ContextKey<T> param) {
/*  81 */       return (T)this.params.getOptionalParameter(param);
/*     */     }
/*     */     
/*     */     public Builder withDynamicDrop(Identifier location, LootParams.DynamicDrop dynamicDrop) {
/*  85 */       LootParams.DynamicDrop prev = this.dynamicDrops.put(location, dynamicDrop);
/*     */       
/*  87 */       if (prev != null) {
/*  88 */         throw new IllegalStateException("Duplicated dynamic drop '" + String.valueOf(this.dynamicDrops) + "'");
/*     */       }
/*     */       
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withLuck(float luck) {
/*  95 */       this.luck = luck;
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     public LootParams create(ContextKeySet contextKeySet) {
/* 100 */       ContextMap keySet = this.params.create(contextKeySet);
/* 101 */       return new LootParams(this.level, keySet, this.dynamicDrops, this.luck);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/LootParams.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */