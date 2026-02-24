/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FeatureFlagRegistry
/*     */ {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final FeatureFlagUniverse universe;
/*     */   private final Map<Identifier, FeatureFlag> names;
/*     */   private final FeatureFlagSet allFlags;
/*     */   
/*     */   private FeatureFlagRegistry(FeatureFlagUniverse universe, FeatureFlagSet allFlags, Map<Identifier, FeatureFlag> names) {
/*  27 */     this.universe = universe;
/*  28 */     this.names = names;
/*  29 */     this.allFlags = allFlags;
/*     */   }
/*     */   
/*     */   public boolean isSubset(FeatureFlagSet set) {
/*  33 */     return set.isSubsetOf(this.allFlags);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet allFlags() {
/*  37 */     return this.allFlags;
/*     */   }
/*     */   
/*     */   public FeatureFlagSet fromNames(Iterable<Identifier> flagIds) {
/*  41 */     return fromNames(flagIds, flagId -> LOGGER.warn("Unknown feature flag: {}", flagId));
/*     */   }
/*     */   
/*     */   public FeatureFlagSet subset(FeatureFlag... flags) {
/*  45 */     return FeatureFlagSet.create(this.universe, Arrays.asList(flags));
/*     */   }
/*     */   
/*     */   public FeatureFlagSet fromNames(Iterable<Identifier> flagIds, Consumer<Identifier> unknownFlags) {
/*  49 */     Set<FeatureFlag> flags = Sets.newIdentityHashSet();
/*  50 */     for (Identifier flagId : flagIds) {
/*  51 */       FeatureFlag flag = this.names.get(flagId);
/*  52 */       if (flag == null) {
/*  53 */         unknownFlags.accept(flagId); continue;
/*     */       } 
/*  55 */       flags.add(flag);
/*     */     } 
/*     */     
/*  58 */     return FeatureFlagSet.create(this.universe, flags);
/*     */   }
/*     */   
/*     */   public Set<Identifier> toNames(FeatureFlagSet set) {
/*  62 */     Set<Identifier> result = new HashSet<>();
/*     */     
/*  64 */     this.names.forEach((id, flag) -> {
/*     */           if (set.contains(flag)) {
/*     */             result.add(id);
/*     */           }
/*     */         });
/*  69 */     return result;
/*     */   }
/*     */   
/*     */   public Codec<FeatureFlagSet> codec() {
/*  73 */     return Identifier.CODEC.listOf().comapFlatMap(ids -> {
/*     */           Set<Identifier> unknownIds = new HashSet<>();
/*     */           Objects.requireNonNull(unknownIds);
/*     */           FeatureFlagSet result = fromNames(ids, unknownIds::add);
/*     */           return !unknownIds.isEmpty() ? DataResult.error((), result) : DataResult.success(result);
/*     */         }, set -> List.copyOf(toNames(set)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final FeatureFlagUniverse universe;
/*     */     
/*     */     private int id;
/*     */     
/*  88 */     private final Map<Identifier, FeatureFlag> flags = new LinkedHashMap<>();
/*     */     
/*     */     public Builder(String universeId) {
/*  91 */       this.universe = new FeatureFlagUniverse(universeId);
/*     */     }
/*     */     
/*     */     public FeatureFlag createVanilla(String name) {
/*  95 */       return create(Identifier.withDefaultNamespace(name));
/*     */     }
/*     */     
/*     */     public FeatureFlag create(Identifier name) {
/*  99 */       if (this.id >= 64)
/*     */       {
/* 101 */         throw new IllegalStateException("Too many feature flags");
/*     */       }
/* 103 */       FeatureFlag result = new FeatureFlag(this.universe, this.id++);
/* 104 */       FeatureFlag previous = this.flags.put(name, result);
/* 105 */       if (previous != null) {
/* 106 */         throw new IllegalStateException("Duplicate feature flag " + String.valueOf(name));
/*     */       }
/* 108 */       return result;
/*     */     }
/*     */     
/*     */     public FeatureFlagRegistry build() {
/* 112 */       FeatureFlagSet allValues = FeatureFlagSet.create(this.universe, this.flags.values());
/* 113 */       return new FeatureFlagRegistry(this.universe, allValues, Map.copyOf(this.flags));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/flag/FeatureFlagRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */