/*     */ package net.minecraft.world.timeline;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Timeline {
/*  26 */   public static final Codec<Holder<Timeline>> CODEC = (Codec<Holder<Timeline>>)RegistryFixedCodec.create(Registries.TIMELINE);
/*     */   
/*  28 */   private static final Codec<Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>> TRACKS_CODEC = Codec.dispatchedMap(net.minecraft.world.attribute.EnvironmentAttributes.CODEC, Util.memoize(AttributeTrack::createCodec));
/*     */   
/*     */   public static final Codec<Timeline> DIRECT_CODEC;
/*     */   
/*     */   static {
/*  33 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("period_ticks").forGetter(()), (App)TRACKS_CODEC.optionalFieldOf("tracks", Map.of()).forGetter(())).apply((Applicative)i, Timeline::new)).validate(Timeline::validateInternal);
/*     */   }
/*  35 */   public static final Codec<Timeline> NETWORK_CODEC = DIRECT_CODEC.xmap(Timeline::filterSyncableTracks, Timeline::filterSyncableTracks); private final Optional<Integer> periodTicks; private final Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks;
/*     */   
/*     */   private static Timeline filterSyncableTracks(Timeline timeline) {
/*  38 */     Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> syncableTracks = Map.copyOf(Maps.filterKeys(timeline.tracks, EnvironmentAttribute::isSyncable));
/*  39 */     return new Timeline(timeline.periodTicks, syncableTracks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Timeline(Optional<Integer> periodTicks, Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks) {
/*  49 */     this.periodTicks = periodTicks;
/*  50 */     this.tracks = tracks;
/*     */   }
/*     */   
/*     */   private static DataResult<Timeline> validateInternal(Timeline timeline) {
/*  54 */     if (timeline.periodTicks.isEmpty()) {
/*  55 */       return DataResult.success(timeline);
/*     */     }
/*  57 */     int periodTicks = (Integer)timeline.periodTicks.get();
/*  58 */     DataResult<Timeline> result = DataResult.success(timeline);
/*  59 */     for (AttributeTrack<?, ?> track : timeline.tracks.values()) {
/*  60 */       result = result.apply2stable((t, $) -> t, 
/*     */           
/*  62 */           AttributeTrack.validatePeriod(track, periodTicks));
/*     */     }
/*     */     
/*  65 */     return result;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  69 */     return new Builder();
/*     */   }
/*     */   
/*     */   public long getCurrentTicks(Level level) {
/*  73 */     long totalTicks = getTotalTicks(level);
/*  74 */     if (this.periodTicks.isEmpty()) {
/*  75 */       return totalTicks;
/*     */     }
/*  77 */     return totalTicks % (Integer)this.periodTicks.get();
/*     */   }
/*     */   
/*     */   public long getTotalTicks(Level level) {
/*  81 */     return level.getDayTime();
/*     */   }
/*     */   
/*     */   public Optional<Integer> periodTicks() {
/*  85 */     return this.periodTicks;
/*     */   }
/*     */   
/*     */   public Set<EnvironmentAttribute<?>> attributes() {
/*  89 */     return this.tracks.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public <Value> AttributeTrackSampler<Value, ?> createTrackSampler(EnvironmentAttribute<Value> attribute, LongSupplier dayTimeGetter) {
/*  94 */     AttributeTrack<Value, ?> track = (AttributeTrack<Value, ?>)this.tracks.get(attribute);
/*  95 */     if (track == null) {
/*  96 */       throw new IllegalStateException("Timeline has no track for " + String.valueOf(attribute));
/*     */     }
/*  98 */     return track.bakeSampler(attribute, this.periodTicks, dayTimeGetter);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 102 */     private Optional<Integer> periodTicks = Optional.empty();
/* 103 */     private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPeriodTicks(int periodTicks) {
/* 109 */       this.periodTicks = Optional.of(periodTicks);
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier, Consumer<KeyframeTrack.Builder<Argument>> builder) {
/* 114 */       attribute.type().checkAllowedModifier(modifier);
/* 115 */       KeyframeTrack.Builder<Argument> argumentTrack = new KeyframeTrack.Builder();
/* 116 */       builder.accept(argumentTrack);
/* 117 */       this.tracks.put(attribute, new AttributeTrack<>(modifier, argumentTrack.build()));
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public <Value> Builder addTrack(EnvironmentAttribute<Value> attribute, Consumer<KeyframeTrack.Builder<Value>> builder) {
/* 122 */       return addModifierTrack(attribute, AttributeModifier.override(), builder);
/*     */     }
/*     */     
/*     */     public Timeline build() {
/* 126 */       return new Timeline(this.periodTicks, (Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>)this.tracks.build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/timeline/Timeline.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */