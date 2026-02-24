/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ 
/*    */ public final class LootDataType<T> extends Record {
/*    */   private final ResourceKey<Registry<T>> registryKey;
/*    */   private final Codec<T> codec;
/*    */   private final Validator<T> validator;
/*    */   
/* 14 */   public LootDataType(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Validator<T> validator) { this.registryKey = registryKey; this.codec = codec; this.validator = validator; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/LootDataType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>; } public ResourceKey<Registry<T>> registryKey() { return this.registryKey; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/LootDataType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/LootDataType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>; } public Codec<T> codec() { return this.codec; } public Validator<T> validator() { return this.validator; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final LootDataType<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> PREDICATE = new LootDataType(Registries.PREDICATE, net.minecraft.world.level.storage.loot.predicates.LootItemCondition.DIRECT_CODEC, (Validator)createSimpleValidator());
/* 20 */   public static final LootDataType<net.minecraft.world.level.storage.loot.functions.LootItemFunction> MODIFIER = new LootDataType(Registries.ITEM_MODIFIER, net.minecraft.world.level.storage.loot.functions.LootItemFunctions.ROOT_CODEC, (Validator)createSimpleValidator());
/* 21 */   public static final LootDataType<LootTable> TABLE = new LootDataType(Registries.LOOT_TABLE, (Codec)LootTable.DIRECT_CODEC, (Validator)createLootTableValidator());
/*    */   
/*    */   public void runValidation(ValidationContext rootContext, ResourceKey<T> key, T value) {
/* 24 */     this.validator.run(rootContext, key, value);
/*    */   }
/*    */   
/*    */   public static java.util.stream.Stream<LootDataType<?>> values() {
/* 28 */     return java.util.stream.Stream.of((LootDataType<?>[])new LootDataType[] { PREDICATE, MODIFIER, TABLE });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T extends LootContextUser> Validator<T> createSimpleValidator() {
/* 37 */     return (rootContext, key, value) -> value.validate(rootContext.enterElement((ProblemReporter.PathElement)new ProblemReporter.RootElementPathElement(key), key));
/*    */   }
/*    */   
/*    */   private static Validator<LootTable> createLootTableValidator() {
/* 41 */     return (rootContext, key, value) -> value.validate(rootContext.setContextKeySet(value.getParamSet()).enterElement((ProblemReporter.PathElement)new ProblemReporter.RootElementPathElement(key), key));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Validator<T> {
/*    */     void run(ValidationContext param1ValidationContext, ResourceKey<T> param1ResourceKey, T param1T);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/LootDataType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */