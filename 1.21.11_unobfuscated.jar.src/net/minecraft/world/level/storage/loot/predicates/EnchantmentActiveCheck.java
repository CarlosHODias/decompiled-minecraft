/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class EnchantmentActiveCheck extends Record implements LootItemCondition {
/*    */   private final boolean active;
/*    */   public static final com.mojang.serialization.MapCodec<EnchantmentActiveCheck> CODEC;
/*    */   
/* 12 */   public EnchantmentActiveCheck(boolean active) { this.active = active; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck; } public boolean active() { return this.active; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.fieldOf("active").forGetter(EnchantmentActiveCheck::active)).apply((com.mojang.datafixers.kinds.Applicative)i, EnchantmentActiveCheck::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext lootContext) {
/* 19 */     return ((Boolean)lootContext.getParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ENCHANTMENT_ACTIVE) == this.active);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 24 */     return LootItemConditions.ENCHANTMENT_ACTIVE_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 29 */     return Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ENCHANTMENT_ACTIVE);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder enchantmentActiveCheck() {
/* 33 */     return () -> new EnchantmentActiveCheck(true);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder enchantmentInactiveCheck() {
/* 37 */     return () -> new EnchantmentActiveCheck(false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */