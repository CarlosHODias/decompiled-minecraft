/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class SetAttributesFunction extends LootItemConditionalFunction {
/*     */   public static final com.mojang.serialization.MapCodec<SetAttributesFunction> CODEC;
/*     */   private final List<Modifier> modifiers;
/*     */   private final boolean replace;
/*     */   
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Modifier.CODEC.listOf().fieldOf("modifiers").forGetter(()), (App)Codec.BOOL.optionalFieldOf("replace", true).forGetter(()))).apply((Applicative)i, SetAttributesFunction::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SetAttributesFunction(List<LootItemCondition> predicates, List<Modifier> modifiers, boolean replace) {
/*  39 */     super(predicates);
/*  40 */     this.modifiers = List.copyOf(modifiers);
/*  41 */     this.replace = replace;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<SetAttributesFunction> getType() {
/*  46 */     return LootItemFunctions.SET_ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  51 */     return (Set<ContextKey<?>>)this.modifiers.stream().flatMap(m -> m.amount.getReferencedContextParams().stream()).collect(com.google.common.collect.ImmutableSet.toImmutableSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  56 */     if (this.replace) {
/*  57 */       itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, updateModifiers(context, ItemAttributeModifiers.EMPTY));
/*     */     } else {
/*  59 */       itemStack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, itemModifiers -> updateModifiers(context, context));
/*     */     } 
/*  61 */     return itemStack;
/*     */   }
/*     */   
/*     */   private ItemAttributeModifiers updateModifiers(LootContext context, ItemAttributeModifiers itemModifiers) {
/*  65 */     RandomSource random = context.getRandom();
/*  66 */     for (Modifier modifier : this.modifiers) {
/*  67 */       EquipmentSlotGroup slot = (EquipmentSlotGroup)net.minecraft.util.Util.getRandom(modifier.slots, random);
/*  68 */       itemModifiers = itemModifiers.withModifierAdded(modifier.attribute, new AttributeModifier(modifier.id, 
/*     */             
/*  70 */             modifier.amount.getFloat(context), modifier.operation), slot);
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return itemModifiers;
/*     */   }
/*     */   
/*     */   public static class ModifierBuilder
/*     */   {
/*     */     private final Identifier id;
/*     */     private final Holder<Attribute> attribute;
/*     */     private final AttributeModifier.Operation operation;
/*     */     private final NumberProvider amount;
/*  83 */     private final Set<EquipmentSlotGroup> slots = java.util.EnumSet.noneOf(EquipmentSlotGroup.class);
/*     */     
/*     */     public ModifierBuilder(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/*  86 */       this.id = id;
/*  87 */       this.attribute = attribute;
/*  88 */       this.operation = operation;
/*  89 */       this.amount = amount;
/*     */     }
/*     */     
/*     */     public ModifierBuilder forSlot(EquipmentSlotGroup slot) {
/*  93 */       this.slots.add(slot);
/*  94 */       return this;
/*     */     }
/*     */     
/*     */     public SetAttributesFunction.Modifier build() {
/*  98 */       return new SetAttributesFunction.Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final boolean replace;
/* 104 */     private final List<SetAttributesFunction.Modifier> modifiers = com.google.common.collect.Lists.newArrayList();
/*     */     
/*     */     public Builder(boolean replace) {
/* 107 */       this.replace = replace;
/*     */     }
/*     */     
/*     */     public Builder() {
/* 111 */       this(false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withModifier(SetAttributesFunction.ModifierBuilder modifier) {
/* 120 */       this.modifiers.add(modifier.build());
/* 121 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 126 */       return new SetAttributesFunction(getConditions(), this.modifiers, this.replace);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ModifierBuilder modifier(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/* 131 */     return new ModifierBuilder(id, attribute, operation, amount);
/*     */   }
/*     */   
/*     */   public static Builder setAttributes() {
/* 135 */     return new Builder();
/*     */   }
/*     */   private static final class Modifier extends Record { private final Identifier id; private final Holder<Attribute> attribute; private final AttributeModifier.Operation operation; private final NumberProvider amount; private final List<EquipmentSlotGroup> slots;
/* 138 */     private Modifier(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount, List<EquipmentSlotGroup> slots) { this.id = id; this.attribute = attribute; this.operation = operation; this.amount = amount; this.slots = slots; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 138 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetAttributesFunction$Modifier;
/* 138 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Attribute> attribute() { return this.attribute; } public AttributeModifier.Operation operation() { return this.operation; } public NumberProvider amount() { return this.amount; } public List<EquipmentSlotGroup> slots() { return this.slots; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     private static final Codec<List<EquipmentSlotGroup>> SLOTS_CODEC = ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(EquipmentSlotGroup.CODEC)); public static final Codec<Modifier> CODEC;
/*     */     static {
/* 147 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Modifier::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(Modifier::attribute), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(Modifier::operation), (App)NumberProviders.CODEC.fieldOf("amount").forGetter(Modifier::amount), (App)SLOTS_CODEC.fieldOf("slot").forGetter(Modifier::slots)).apply((Applicative)i, Modifier::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetAttributesFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */