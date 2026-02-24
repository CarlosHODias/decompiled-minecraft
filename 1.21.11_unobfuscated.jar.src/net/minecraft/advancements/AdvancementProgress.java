/*     */ package net.minecraft.advancements;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class AdvancementProgress implements Comparable<AdvancementProgress> {
/*  26 */   private static final DateTimeFormatter OBTAINED_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z", java.util.Locale.ROOT); static {
/*  27 */     OBTAINED_TIME_CODEC = ExtraCodecs.temporalCodec(OBTAINED_TIME_FORMAT).xmap(Instant::from, instant -> instant.atZone(ZoneId.systemDefault()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  33 */     CRITERIA_CODEC = Codec.unboundedMap((Codec)Codec.STRING, OBTAINED_TIME_CODEC).xmap(map -> Util.mapValues(map, CriterionProgress::new), map -> (Map)map.entrySet().stream().filter(()).collect(Collectors.toMap(Map.Entry::getKey, ())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CRITERIA_CODEC.optionalFieldOf("criteria", Map.of()).forGetter(()), (App)Codec.BOOL.fieldOf("done").orElse(true).forGetter(AdvancementProgress::isDone)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   private static final Codec<Instant> OBTAINED_TIME_CODEC;
/*     */   private static final Codec<Map<String, CriterionProgress>> CRITERIA_CODEC;
/*     */   public static final Codec<AdvancementProgress> CODEC;
/*     */   private final Map<String, CriterionProgress> criteria;
/*  47 */   private AdvancementRequirements requirements = AdvancementRequirements.EMPTY;
/*     */   
/*     */   private AdvancementProgress(Map<String, CriterionProgress> criteria) {
/*  50 */     this.criteria = criteria;
/*     */   }
/*     */   
/*     */   public AdvancementProgress() {
/*  54 */     this.criteria = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   public void update(AdvancementRequirements requirements) {
/*  58 */     Set<String> names = requirements.names();
/*  59 */     this.criteria.entrySet().removeIf(entry -> !names.contains(entry.getKey()));
/*  60 */     for (String name : names) {
/*  61 */       this.criteria.putIfAbsent(name, new CriterionProgress());
/*     */     }
/*  63 */     this.requirements = requirements;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  67 */     return this.requirements.test(this::isCriterionDone);
/*     */   }
/*     */   
/*     */   public boolean hasProgress() {
/*  71 */     for (CriterionProgress progress : this.criteria.values()) {
/*  72 */       if (progress.isDone()) {
/*  73 */         return true;
/*     */       }
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public boolean grantProgress(String name) {
/*  80 */     CriterionProgress progress = this.criteria.get(name);
/*  81 */     if (progress != null && !progress.isDone()) {
/*  82 */       progress.grant();
/*  83 */       return true;
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public boolean revokeProgress(String name) {
/*  89 */     CriterionProgress progress = this.criteria.get(name);
/*  90 */     if (progress != null && progress.isDone()) {
/*  91 */       progress.revoke();
/*  92 */       return true;
/*     */     } 
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return "AdvancementProgress{criteria=" + String.valueOf(this.criteria) + ", requirements=" + String.valueOf(this.requirements) + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeToNetwork(FriendlyByteBuf output) {
/* 106 */     output.writeMap(this.criteria, FriendlyByteBuf::writeUtf, (b, v) -> v.serializeToNetwork(b));
/*     */   }
/*     */   
/*     */   public static AdvancementProgress fromNetwork(FriendlyByteBuf input) {
/* 110 */     Map<String, CriterionProgress> criteria = input.readMap(FriendlyByteBuf::readUtf, CriterionProgress::fromNetwork);
/* 111 */     return new AdvancementProgress(criteria);
/*     */   }
/*     */   
/*     */   public CriterionProgress getCriterion(String id) {
/* 115 */     return this.criteria.get(id);
/*     */   }
/*     */   
/*     */   private boolean isCriterionDone(String criterion) {
/* 119 */     CriterionProgress progress = getCriterion(criterion);
/* 120 */     return (progress != null && progress.isDone());
/*     */   }
/*     */   
/*     */   public float getPercent() {
/* 124 */     if (this.criteria.isEmpty()) {
/* 125 */       return 0.0F;
/*     */     }
/* 127 */     float total = this.requirements.size();
/* 128 */     float complete = countCompletedRequirements();
/* 129 */     return complete / total;
/*     */   }
/*     */   
/*     */   public Component getProgressText() {
/* 133 */     if (this.criteria.isEmpty()) {
/* 134 */       return null;
/*     */     }
/*     */     
/* 137 */     int total = this.requirements.size();
/* 138 */     if (total <= 1) {
/* 139 */       return null;
/*     */     }
/*     */     
/* 142 */     int complete = countCompletedRequirements();
/* 143 */     return (Component)Component.translatable("advancements.progress", new Object[] { complete, total });
/*     */   }
/*     */   
/*     */   private int countCompletedRequirements() {
/* 147 */     return this.requirements.count(this::isCriterionDone);
/*     */   }
/*     */   
/*     */   public Iterable<String> getRemainingCriteria() {
/* 151 */     List<String> remaining = Lists.newArrayList();
/* 152 */     for (Map.Entry<String, CriterionProgress> entry : this.criteria.entrySet()) {
/* 153 */       if (!((CriterionProgress)entry.getValue()).isDone()) {
/* 154 */         remaining.add(entry.getKey());
/*     */       }
/*     */     } 
/* 157 */     return remaining;
/*     */   }
/*     */   
/*     */   public Iterable<String> getCompletedCriteria() {
/* 161 */     List<String> completed = Lists.newArrayList();
/* 162 */     for (Map.Entry<String, CriterionProgress> entry : this.criteria.entrySet()) {
/* 163 */       if (((CriterionProgress)entry.getValue()).isDone()) {
/* 164 */         completed.add(entry.getKey());
/*     */       }
/*     */     } 
/* 167 */     return completed;
/*     */   }
/*     */   
/*     */   public Instant getFirstProgressDate() {
/* 171 */     return this.criteria.values().stream()
/* 172 */       .map(CriterionProgress::getObtained)
/* 173 */       .filter(Objects::nonNull)
/* 174 */       .min(Comparator.naturalOrder())
/* 175 */       .orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(AdvancementProgress o) {
/* 180 */     Instant ourSmallestDate = getFirstProgressDate();
/* 181 */     Instant theirSmallestDate = o.getFirstProgressDate();
/*     */     
/* 183 */     if (ourSmallestDate == null && theirSmallestDate != null) {
/* 184 */       return 1;
/*     */     }
/* 186 */     if (ourSmallestDate != null && theirSmallestDate == null) {
/* 187 */       return -1;
/*     */     }
/* 189 */     if (ourSmallestDate == null && theirSmallestDate == null) {
/* 190 */       return 0;
/*     */     }
/*     */     
/* 193 */     return ourSmallestDate.compareTo(theirSmallestDate);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */