/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public final class AdvancementRequirements extends Record {
/*     */   private final List<List<String>> requirements;
/*     */   
/*  14 */   public AdvancementRequirements(List<List<String>> requirements) { this.requirements = requirements; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/AdvancementRequirements;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  14 */     //   0	7	0	this	Lnet/minecraft/advancements/AdvancementRequirements; } public List<List<String>> requirements() { return this.requirements; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/AdvancementRequirements;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/AdvancementRequirements;
/*  15 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.Codec<AdvancementRequirements> CODEC = com.mojang.serialization.Codec.STRING.listOf().listOf().xmap(AdvancementRequirements::new, AdvancementRequirements::requirements);
/*     */   
/*  17 */   public static final AdvancementRequirements EMPTY = new AdvancementRequirements(List.of());
/*     */   
/*     */   public AdvancementRequirements(FriendlyByteBuf input) {
/*  20 */     this(input.readList(in -> in.readList(FriendlyByteBuf::readUtf)));
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/*  24 */     output.writeCollection(this.requirements, (out, set) -> out.writeCollection(set, FriendlyByteBuf::writeUtf));
/*     */   }
/*     */   
/*     */   public static AdvancementRequirements allOf(Collection<String> criteria) {
/*  28 */     return new AdvancementRequirements(criteria.stream().map(List::of).toList());
/*     */   }
/*     */   
/*     */   public static AdvancementRequirements anyOf(Collection<String> criteria) {
/*  32 */     return new AdvancementRequirements(List.of(List.copyOf(criteria)));
/*     */   }
/*     */   
/*     */   public int size() {
/*  36 */     return this.requirements.size();
/*     */   }
/*     */   
/*     */   public boolean test(Predicate<String> predicate) {
/*  40 */     if (this.requirements.isEmpty()) {
/*  41 */       return false;
/*     */     }
/*  43 */     for (List<String> set : this.requirements) {
/*  44 */       if (!anyMatch(set, predicate)) {
/*  45 */         return false;
/*     */       }
/*     */     } 
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public int count(Predicate<String> predicate) {
/*  52 */     int count = 0;
/*  53 */     for (List<String> set : this.requirements) {
/*  54 */       if (anyMatch(set, predicate)) {
/*  55 */         count++;
/*     */       }
/*     */     } 
/*  58 */     return count;
/*     */   }
/*     */   
/*     */   private static boolean anyMatch(List<String> criteria, Predicate<String> predicate) {
/*  62 */     for (String criterion : criteria) {
/*  63 */       if (predicate.test(criterion)) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public com.mojang.serialization.DataResult<AdvancementRequirements> validate(Set<String> expectedCriteria) {
/*  71 */     ObjectOpenHashSet<String> objectOpenHashSet = new ObjectOpenHashSet();
/*  72 */     for (List<String> set : this.requirements) {
/*  73 */       if (set.isEmpty() && expectedCriteria.isEmpty()) {
/*  74 */         return com.mojang.serialization.DataResult.error(() -> "Requirement entry cannot be empty");
/*     */       }
/*  76 */       objectOpenHashSet.addAll(set);
/*     */     } 
/*  78 */     if (!expectedCriteria.equals(objectOpenHashSet)) {
/*  79 */       Sets.SetView setView1 = Sets.difference(expectedCriteria, (Set)objectOpenHashSet);
/*  80 */       Sets.SetView setView2 = Sets.difference((Set)objectOpenHashSet, expectedCriteria);
/*  81 */       return com.mojang.serialization.DataResult.error(() -> "Advancement completion requirements did not exactly match specified criteria. Missing: " + String.valueOf(missingCriteria) + ". Unknown: " + String.valueOf(unknownCriteria));
/*     */     } 
/*  83 */     return com.mojang.serialization.DataResult.success(this);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  87 */     return this.requirements.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return this.requirements.toString();
/*     */   }
/*     */   
/*     */   public Set<String> names() {
/*  96 */     ObjectOpenHashSet<String> objectOpenHashSet = new ObjectOpenHashSet();
/*  97 */     for (List<String> set : this.requirements) {
/*  98 */       objectOpenHashSet.addAll(set);
/*     */     }
/* 100 */     return (Set<String>)objectOpenHashSet;
/*     */   }
/*     */   
/*     */   public static interface Strategy {
/* 104 */     public static final Strategy AND = AdvancementRequirements::allOf;
/* 105 */     public static final Strategy OR = AdvancementRequirements::anyOf;
/*     */     
/*     */     AdvancementRequirements create(Collection<String> param1Collection);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementRequirements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */