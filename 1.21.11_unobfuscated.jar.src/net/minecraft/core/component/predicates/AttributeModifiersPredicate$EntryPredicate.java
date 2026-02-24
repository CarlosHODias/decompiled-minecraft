/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ 
/*    */ public final class EntryPredicate extends Record implements java.util.function.Predicate<ItemAttributeModifiers.Entry> {
/*    */   private final Optional<net.minecraft.core.HolderSet<net.minecraft.world.entity.ai.attributes.Attribute>> attribute;
/*    */   private final Optional<Identifier> id;
/*    */   private final MinMaxBounds.Doubles amount;
/*    */   private final Optional<net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation> operation;
/*    */   private final Optional<EquipmentSlotGroup> slot;
/*    */   public static final com.mojang.serialization.Codec<EntryPredicate> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;
/*    */   }
/*    */   
/* 26 */   public EntryPredicate(Optional<net.minecraft.core.HolderSet<net.minecraft.world.entity.ai.attributes.Attribute>> attribute, Optional<Identifier> id, MinMaxBounds.Doubles amount, Optional<net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation> operation, Optional<EquipmentSlotGroup> slot) { this.attribute = attribute; this.id = id; this.amount = amount; this.operation = operation; this.slot = slot; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;
/* 26 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<net.minecraft.core.HolderSet<net.minecraft.world.entity.ai.attributes.Attribute>> attribute() { return this.attribute; } public Optional<Identifier> id() { return this.id; } public MinMaxBounds.Doubles amount() { return this.amount; } public Optional<net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation> operation() { return this.operation; } public Optional<EquipmentSlotGroup> slot() { return this.slot; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 33 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ATTRIBUTE).optionalFieldOf("attribute").forGetter(EntryPredicate::attribute), (App)Identifier.CODEC.optionalFieldOf("id").forGetter(EntryPredicate::id), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("amount", MinMaxBounds.Doubles.ANY).forGetter(EntryPredicate::amount), (App)net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.CODEC.optionalFieldOf("operation").forGetter(EntryPredicate::operation), (App)EquipmentSlotGroup.CODEC.optionalFieldOf("slot").forGetter(EntryPredicate::slot)).apply((com.mojang.datafixers.kinds.Applicative)i, EntryPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(ItemAttributeModifiers.Entry value) {
/* 43 */     if (this.attribute.isPresent() && !((net.minecraft.core.HolderSet)this.attribute.get()).contains(value.attribute())) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     if (this.id.isPresent() && !((Identifier)this.id.get()).equals(value.modifier().id())) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     if (!this.amount.matches(value.modifier().amount())) {
/* 52 */       return false;
/*    */     }
/*    */     
/* 55 */     if (this.operation.isPresent() && this.operation.get() != value.modifier().operation()) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     if (this.slot.isPresent() && this.slot.get() != value.slot()) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */