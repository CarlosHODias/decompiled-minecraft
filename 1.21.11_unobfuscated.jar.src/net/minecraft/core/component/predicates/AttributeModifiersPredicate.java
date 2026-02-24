/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ 
/*    */ public final class AttributeModifiersPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<ItemAttributeModifiers> {
/*    */   private final Optional<CollectionPredicate<ItemAttributeModifiers.Entry, EntryPredicate>> modifiers;
/*    */   public static final com.mojang.serialization.Codec<AttributeModifiersPredicate> CODEC;
/*    */   
/* 22 */   public AttributeModifiersPredicate(Optional<CollectionPredicate<ItemAttributeModifiers.Entry, EntryPredicate>> modifiers) { this.modifiers = modifiers; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate; } public Optional<CollectionPredicate<ItemAttributeModifiers.Entry, EntryPredicate>> modifiers() { return this.modifiers; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public static final class EntryPredicate extends Record implements java.util.function.Predicate<ItemAttributeModifiers.Entry> {
/*    */     private final Optional<HolderSet<Attribute>> attribute; private final Optional<Identifier> id; private final MinMaxBounds.Doubles amount; private final Optional<AttributeModifier.Operation> operation; private final Optional<EquipmentSlotGroup> slot; public static final com.mojang.serialization.Codec<EntryPredicate> CODEC; public EntryPredicate(Optional<HolderSet<Attribute>> attribute, Optional<Identifier> id, MinMaxBounds.Doubles amount, Optional<AttributeModifier.Operation> operation, Optional<EquipmentSlotGroup> slot) {
/* 26 */       this.attribute = attribute; this.id = id; this.amount = amount; this.operation = operation; this.slot = slot; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/AttributeModifiersPredicate$EntryPredicate;
/* 26 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<HolderSet<Attribute>> attribute() { return this.attribute; } public Optional<Identifier> id() { return this.id; } public MinMaxBounds.Doubles amount() { return this.amount; } public Optional<AttributeModifier.Operation> operation() { return this.operation; } public Optional<EquipmentSlotGroup> slot() { return this.slot; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ATTRIBUTE).optionalFieldOf("attribute").forGetter(EntryPredicate::attribute), (App)Identifier.CODEC.optionalFieldOf("id").forGetter(EntryPredicate::id), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("amount", MinMaxBounds.Doubles.ANY).forGetter(EntryPredicate::amount), (App)AttributeModifier.Operation.CODEC.optionalFieldOf("operation").forGetter(EntryPredicate::operation), (App)EquipmentSlotGroup.CODEC.optionalFieldOf("slot").forGetter(EntryPredicate::slot)).apply((Applicative)i, EntryPredicate::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean test(ItemAttributeModifiers.Entry value) {
/* 43 */       if (this.attribute.isPresent() && !((HolderSet)this.attribute.get()).contains(value.attribute())) {
/* 44 */         return false;
/*    */       }
/*    */       
/* 47 */       if (this.id.isPresent() && !((Identifier)this.id.get()).equals(value.modifier().id())) {
/* 48 */         return false;
/*    */       }
/*    */       
/* 51 */       if (!this.amount.matches(value.modifier().amount())) {
/* 52 */         return false;
/*    */       }
/*    */       
/* 55 */       if (this.operation.isPresent() && this.operation.get() != value.modifier().operation()) {
/* 56 */         return false;
/*    */       }
/*    */       
/* 59 */       if (this.slot.isPresent() && this.slot.get() != value.slot()) {
/* 60 */         return false;
/*    */       }
/*    */       
/* 63 */       return true;
/*    */     } }
/*    */   
/*    */   static {
/* 67 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(EntryPredicate.CODEC).optionalFieldOf("modifiers").forGetter(AttributeModifiersPredicate::modifiers)).apply((Applicative)i, AttributeModifiersPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<ItemAttributeModifiers> componentType() {
/* 73 */     return net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemAttributeModifiers value) {
/* 78 */     if (this.modifiers.isPresent() && !((CollectionPredicate)this.modifiers.get()).test(value.modifiers())) {
/* 79 */       return false;
/*    */     }
/*    */     
/* 82 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/AttributeModifiersPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */