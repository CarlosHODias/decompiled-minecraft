/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class ConditionReference extends Record implements LootItemCondition {
/*    */   private final ResourceKey<LootItemCondition> name;
/*    */   
/* 14 */   public ConditionReference(ResourceKey<LootItemCondition> name) { this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference; } public ResourceKey<LootItemCondition> name() { return this.name; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 17 */   } private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<ConditionReference> CODEC;
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(net.minecraft.core.registries.Registries.PREDICATE).fieldOf("name").forGetter(ConditionReference::name)).apply((com.mojang.datafixers.kinds.Applicative)i, ConditionReference::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 25 */     return LootItemConditions.REFERENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 30 */     if (!context.allowsReferences()) {
/* 31 */       context.reportProblem((ProblemReporter.Problem)new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     if (context.hasVisitedElement(this.name)) {
/* 36 */       context.reportProblem((ProblemReporter.Problem)new ValidationContext.RecursiveReferenceProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     super.validate(context);
/*    */     
/* 42 */     context.resolver().get(this.name).ifPresentOrElse(condition -> ((LootItemCondition)context.value()).validate(context.enterElement((ProblemReporter.PathElement)new ProblemReporter.ElementReferencePathElement(this.name), this.name)), () -> context.reportProblem((ProblemReporter.Problem)new ValidationContext.MissingReferenceProblem(this.name)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext lootContext) {
/* 50 */     LootItemCondition condition = lootContext.getResolver().get(this.name).map(Holder.Reference::value).orElse(null);
/* 51 */     if (condition == null) {
/* 52 */       LOGGER.warn("Tried using unknown condition table called {}", this.name.identifier());
/* 53 */       return false;
/*    */     } 
/* 55 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(condition);
/* 56 */     if (lootContext.pushVisitedElement(breadcrumb)) {
/*    */       try {
/* 58 */         return condition.test(lootContext);
/*    */       } finally {
/* 60 */         lootContext.popVisitedElement(breadcrumb);
/*    */       } 
/*    */     }
/* 63 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public static LootItemCondition.Builder conditionReference(ResourceKey<LootItemCondition> name) {
/* 69 */     return () -> new ConditionReference(name);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/ConditionReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */