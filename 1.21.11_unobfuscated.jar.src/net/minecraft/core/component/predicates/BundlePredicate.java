/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BundleContents;
/*    */ 
/*    */ public final class BundlePredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<BundleContents> {
/*    */   private final Optional<CollectionPredicate<ItemStack, ItemPredicate>> items;
/*    */   public static final com.mojang.serialization.Codec<BundlePredicate> CODEC;
/*    */   
/* 15 */   public BundlePredicate(Optional<CollectionPredicate<ItemStack, ItemPredicate>> items) { this.items = items; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/BundlePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate; } public Optional<CollectionPredicate<ItemStack, ItemPredicate>> items() { return this.items; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/BundlePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/BundlePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(BundlePredicate::items)).apply((com.mojang.datafixers.kinds.Applicative)i, BundlePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<BundleContents> componentType() {
/* 24 */     return net.minecraft.core.component.DataComponents.BUNDLE_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(BundleContents value) {
/* 29 */     if (this.items.isPresent() && !((CollectionPredicate)this.items.get()).test(value.items())) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/BundlePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */