/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ 
/*    */ public final class PotionsPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<PotionContents> {
/*    */   private final HolderSet<Potion> potions;
/*    */   
/* 16 */   public PotionsPredicate(HolderSet<Potion> potions) { this.potions = potions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/PotionsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/PotionsPredicate; } public HolderSet<Potion> potions() { return this.potions; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/PotionsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/PotionsPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/PotionsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/PotionsPredicate;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public static final Codec<PotionsPredicate> CODEC = RegistryCodecs.homogeneousList(Registries.POTION).xmap(PotionsPredicate::new, PotionsPredicate::potions);
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<PotionContents> componentType() {
/* 21 */     return net.minecraft.core.component.DataComponents.POTION_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(PotionContents potionContents) {
/* 26 */     Optional<Holder<Potion>> potion = potionContents.potion();
/* 27 */     if (potion.isEmpty() || !this.potions.contains(potion.get())) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public static DataComponentPredicate potions(HolderSet<Potion> potions) {
/* 35 */     return (DataComponentPredicate)new PotionsPredicate(potions);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/PotionsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */