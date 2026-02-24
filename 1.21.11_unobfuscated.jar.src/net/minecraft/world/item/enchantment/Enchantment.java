/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ 
/*     */ public final class Enchantment extends Record {
/*     */   private final Component description;
/*     */   private final EnchantmentDefinition definition;
/*     */   private final HolderSet<Enchantment> exclusiveSet;
/*     */   private final DataComponentMap effects;
/*     */   public static final int MAX_LEVEL = 255;
/*     */   public static final Codec<Enchantment> DIRECT_CODEC;
/*     */   
/*  61 */   public Enchantment(Component description, EnchantmentDefinition definition, HolderSet<Enchantment> exclusiveSet, DataComponentMap effects) { this.description = description; this.definition = definition; this.exclusiveSet = exclusiveSet; this.effects = effects; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment;
/*  61 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public EnchantmentDefinition definition() { return this.definition; } public HolderSet<Enchantment> exclusiveSet() { return this.exclusiveSet; } public DataComponentMap effects() { return this.effects; }
/*     */    public static final class Cost extends Record {
/*     */     private final int base; private final int perLevelAboveFirst; public static final Codec<Cost> CODEC; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*     */     }
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*     */     }
/*  69 */     public Cost(int base, int perLevelAboveFirst) { this.base = base; this.perLevelAboveFirst = perLevelAboveFirst; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*  69 */       //   0	8	1	o	Ljava/lang/Object; } public int base() { return this.base; } public int perLevelAboveFirst() { return this.perLevelAboveFirst; } static {
/*  70 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("base").forGetter(Cost::base), (App)Codec.INT.fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst)).apply((Applicative)i, Cost::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int calculate(int level) {
/*  76 */       return this.base + this.perLevelAboveFirst * (level - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Cost constantCost(int base) {
/*  81 */     return new Cost(base, 0);
/*     */   }
/*     */   
/*     */   public static Cost dynamicCost(int base, int perLevel) {
/*  85 */     return new Cost(base, perLevel);
/*     */   }
/*     */   public static final class EnchantmentDefinition extends Record { private final HolderSet<Item> supportedItems; private final Optional<HolderSet<Item>> primaryItems; private final int weight; private final int maxLevel; private final Enchantment.Cost minCost; private final Enchantment.Cost maxCost; private final int anvilCost; private final List<EquipmentSlotGroup> slots; public static final com.mojang.serialization.MapCodec<EnchantmentDefinition> CODEC;
/*  88 */     public EnchantmentDefinition(HolderSet<Item> supportedItems, Optional<HolderSet<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots) { this.supportedItems = supportedItems; this.primaryItems = primaryItems; this.weight = weight; this.maxLevel = maxLevel; this.minCost = minCost; this.maxCost = maxCost; this.anvilCost = anvilCost; this.slots = slots; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public HolderSet<Item> supportedItems() { return this.supportedItems; } public Optional<HolderSet<Item>> primaryItems() { return this.primaryItems; } public int weight() { return this.weight; } public int maxLevel() { return this.maxLevel; } public Enchantment.Cost minCost() { return this.minCost; } public Enchantment.Cost maxCost() { return this.maxCost; } public int anvilCost() { return this.anvilCost; } public List<EquipmentSlotGroup> slots() { return this.slots; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  98 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentDefinition::supportedItems), (App)RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("primary_items").forGetter(EnchantmentDefinition::primaryItems), (App)ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight), (App)ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel), (App)Enchantment.Cost.CODEC.fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost), (App)Enchantment.Cost.CODEC.fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost), (App)EquipmentSlotGroup.CODEC.listOf().fieldOf("slots").forGetter(EnchantmentDefinition::slots)).apply((Applicative)i, EnchantmentDefinition::new));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, HolderSet<Item> primaryItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) {
/* 111 */     return new EnchantmentDefinition(supportedItems, Optional.of(primaryItems), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots));
/*     */   }
/*     */   
/*     */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) {
/* 115 */     return new EnchantmentDefinition(supportedItems, Optional.empty(), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots));
/*     */   }
/*     */   static {
/* 118 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("description").forGetter(Enchantment::description), (App)EnchantmentDefinition.CODEC.forGetter(Enchantment::definition), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("exclusive_set", HolderSet.direct(new Holder[0])).forGetter(Enchantment::exclusiveSet), (App)EnchantmentEffectComponents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(Enchantment::effects)).apply((Applicative)i, Enchantment::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static final Codec<Holder<Enchantment>> CODEC = (Codec<Holder<Enchantment>>)net.minecraft.resources.RegistryFixedCodec.create(Registries.ENCHANTMENT);
/* 126 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<Enchantment>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT);
/*     */   
/*     */   public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity entity) {
/* 129 */     Map<EquipmentSlot, ItemStack> itemStacks = com.google.common.collect.Maps.newEnumMap(EquipmentSlot.class);
/* 130 */     for (EquipmentSlot slot : (Iterable<EquipmentSlot>)EquipmentSlot.VALUES) {
/* 131 */       if (matchingSlot(slot)) {
/* 132 */         ItemStack itemStack = entity.getItemBySlot(slot);
/* 133 */         if (!itemStack.isEmpty()) {
/* 134 */           itemStacks.put(slot, itemStack);
/*     */         }
/*     */       } 
/*     */     } 
/* 138 */     return itemStacks;
/*     */   }
/*     */   
/*     */   public HolderSet<Item> getSupportedItems() {
/* 142 */     return this.definition.supportedItems();
/*     */   }
/*     */   
/*     */   public boolean matchingSlot(EquipmentSlot slot) {
/* 146 */     return this.definition.slots().stream().anyMatch(group -> group.test(slot));
/*     */   }
/*     */   
/*     */   public boolean isPrimaryItem(ItemStack item) {
/* 150 */     return (isSupportedItem(item) && (this.definition.primaryItems.isEmpty() || item.is(this.definition.primaryItems.get())));
/*     */   }
/*     */   
/*     */   public boolean isSupportedItem(ItemStack item) {
/* 154 */     return item.is(this.definition.supportedItems);
/*     */   }
/*     */   
/*     */   public int getWeight() {
/* 158 */     return this.definition.weight();
/*     */   }
/*     */   
/*     */   public int getAnvilCost() {
/* 162 */     return this.definition.anvilCost();
/*     */   }
/*     */   
/*     */   public int getMinLevel() {
/* 166 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMaxLevel() {
/* 170 */     return this.definition.maxLevel();
/*     */   }
/*     */   
/*     */   public int getMinCost(int level) {
/* 174 */     return this.definition.minCost().calculate(level);
/*     */   }
/*     */   
/*     */   public int getMaxCost(int level) {
/* 178 */     return this.definition.maxCost().calculate(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return "Enchantment " + this.description.getString();
/*     */   }
/*     */   
/*     */   public static boolean areCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other) {
/* 188 */     return (!enchantment.equals(other) && !((Enchantment)enchantment.value()).exclusiveSet.contains(other) && !((Enchantment)other.value()).exclusiveSet.contains(enchantment));
/*     */   }
/*     */   
/*     */   public static Component getFullname(Holder<Enchantment> enchantment, int level) {
/* 192 */     MutableComponent result = ((Enchantment)enchantment.value()).description.copy();
/* 193 */     if (enchantment.is(net.minecraft.tags.EnchantmentTags.CURSE)) {
/* 194 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(net.minecraft.ChatFormatting.RED));
/*     */     } else {
/* 196 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(net.minecraft.ChatFormatting.GRAY));
/*     */     } 
/* 198 */     if (level != 1 || ((Enchantment)enchantment.value()).getMaxLevel() != 1) {
/* 199 */       result.append(net.minecraft.network.chat.CommonComponents.SPACE).append((Component)Component.translatable("enchantment.level." + level));
/*     */     }
/* 201 */     return (Component)result;
/*     */   }
/*     */   
/*     */   public boolean canEnchant(ItemStack itemStack) {
/* 205 */     return this.definition.supportedItems().contains(itemStack.getItemHolder());
/*     */   }
/*     */   
/*     */   public <T> List<T> getEffects(DataComponentType<List<T>> type) {
/* 209 */     return (List<T>)this.effects.getOrDefault(type, List.of());
/*     */   }
/*     */   
/*     */   public boolean isImmuneToDamage(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 213 */     LootContext context = damageContext(serverLevel, enchantmentLevel, victim, source);
/* 214 */     for (ConditionalEffect<DamageImmunity> filteredEffect : getEffects(EnchantmentEffectComponents.DAMAGE_IMMUNITY)) {
/* 215 */       if (filteredEffect.matches(context)) {
/* 216 */         return true;
/*     */       }
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   public void modifyDamageProtection(ServerLevel serverLevel, int enchantmentLevel, ItemStack item, Entity victim, DamageSource source, MutableFloat protection) {
/* 223 */     LootContext context = damageContext(serverLevel, enchantmentLevel, victim, source);
/* 224 */     for (ConditionalEffect<EnchantmentValueEffect> conditionalEffect : getEffects(EnchantmentEffectComponents.DAMAGE_PROTECTION)) {
/* 225 */       if (conditionalEffect.matches(context)) {
/* 226 */         protection.setValue(((EnchantmentValueEffect)conditionalEffect.effect()).process(enchantmentLevel, victim.getRandom(), protection.floatValue()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void modifyDurabilityChange(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 232 */     modifyItemFilteredCount(EnchantmentEffectComponents.ITEM_DAMAGE, serverLevel, enchantmentLevel, itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyAmmoCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 236 */     modifyItemFilteredCount(EnchantmentEffectComponents.AMMO_USE, serverLevel, enchantmentLevel, itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyPiercingCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) {
/* 240 */     modifyItemFilteredCount(EnchantmentEffectComponents.PROJECTILE_PIERCING, serverLevel, enchantmentLevel, itemStack, count);
/*     */   }
/*     */   
/*     */   public void modifyBlockExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) {
/* 244 */     modifyItemFilteredCount(EnchantmentEffectComponents.BLOCK_EXPERIENCE, serverLevel, enchantmentLevel, itemStack, count);
/*     */   }
/*     */   
/*     */   public void modifyMobExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity killer, MutableFloat experience) {
/* 248 */     modifyEntityFilteredValue(EnchantmentEffectComponents.MOB_EXPERIENCE, serverLevel, enchantmentLevel, itemStack, killer, experience);
/*     */   }
/*     */   
/*     */   public void modifyDurabilityToRepairFromXp(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 252 */     modifyItemFilteredCount(EnchantmentEffectComponents.REPAIR_WITH_XP, serverLevel, enchantmentLevel, itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyTridentReturnToOwnerAcceleration(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity trident, MutableFloat count) {
/* 256 */     modifyEntityFilteredValue(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, serverLevel, enchantmentLevel, itemStack, trident, count);
/*     */   }
/*     */   
/*     */   public void modifyTridentSpinAttackStrength(RandomSource random, int enchantmentLevel, MutableFloat strength) {
/* 260 */     modifyUnfilteredValue(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, random, enchantmentLevel, strength);
/*     */   }
/*     */   
/*     */   public void modifyFishingTimeReduction(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat timeReduction) {
/* 264 */     modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, serverLevel, enchantmentLevel, itemStack, fisher, timeReduction);
/*     */   }
/*     */   
/*     */   public void modifyFishingLuckBonus(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat luck) {
/* 268 */     modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_LUCK_BONUS, serverLevel, enchantmentLevel, itemStack, fisher, luck);
/*     */   }
/*     */   
/*     */   public void modifyDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 272 */     modifyDamageFilteredValue(EnchantmentEffectComponents.DAMAGE, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyFallBasedDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 276 */     modifyDamageFilteredValue(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyKnockback(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 280 */     modifyDamageFilteredValue(EnchantmentEffectComponents.KNOCKBACK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyArmorEffectivness(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 284 */     modifyDamageFilteredValue(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void doPostAttack(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, EnchantmentTarget forTarget, Entity victim, DamageSource damageSource) {
/* 288 */     for (TargetedConditionalEffect<EnchantmentEntityEffect> effect : getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
/* 289 */       if (forTarget == effect.enchanted()) {
/* 290 */         doPostAttack(effect, serverLevel, enchantmentLevel, item, victim, damageSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void doPostAttack(TargetedConditionalEffect<EnchantmentEntityEffect> effect, ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity victim, DamageSource damageSource) {
/* 296 */     if (effect.matches(damageContext(serverLevel, enchantmentLevel, victim, damageSource))) {
/* 297 */       switch (effect.affected()) { default: throw new MatchException(null, null);
/*     */         case ATTACKER: 
/*     */         case DAMAGING_ENTITY: 
/* 300 */         case VICTIM: break; }  Entity target = victim;
/*     */       
/* 302 */       if (target != null) {
/* 303 */         ((EnchantmentEntityEffect)effect.effect()).apply(serverLevel, enchantmentLevel, item, target, target.position());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void doLunge(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity user) {
/* 309 */     applyEffects(
/* 310 */         getEffects(EnchantmentEffectComponents.POST_PIERCING_ATTACK), 
/* 311 */         entityContext(serverLevel, enchantmentLevel, user, user.position()), e -> e.apply(serverLevel, enchantmentLevel, item, user, user.position()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void modifyProjectileCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat count) {
/* 317 */     modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_COUNT, serverLevel, enchantmentLevel, weapon, shooter, count);
/*     */   }
/*     */   
/*     */   public void modifyProjectileSpread(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat angle) {
/* 321 */     modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_SPREAD, serverLevel, enchantmentLevel, weapon, shooter, angle);
/*     */   }
/*     */   
/*     */   public void modifyCrossbowChargeTime(RandomSource random, int enchantmentLevel, MutableFloat time) {
/* 325 */     modifyUnfilteredValue(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, random, enchantmentLevel, time);
/*     */   }
/*     */   
/*     */   public void modifyUnfilteredValue(DataComponentType<EnchantmentValueEffect> component, RandomSource random, int enchantmentLevel, MutableFloat value) {
/* 329 */     EnchantmentValueEffect effect = (EnchantmentValueEffect)this.effects.get(component);
/* 330 */     if (effect != null) {
/* 331 */       value.setValue(effect.process(enchantmentLevel, random, value.floatValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity) {
/* 336 */     applyEffects(
/* 337 */         getEffects(EnchantmentEffectComponents.TICK), 
/* 338 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), e -> e.apply(serverLevel, enchantmentLevel, item, entity, entity.position()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onProjectileSpawned(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile) {
/* 344 */     applyEffects(
/* 345 */         getEffects(EnchantmentEffectComponents.PROJECTILE_SPAWNED), 
/* 346 */         entityContext(serverLevel, enchantmentLevel, projectile, projectile.position()), e -> e.apply(serverLevel, enchantmentLevel, weapon, projectile, projectile.position()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHitBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile, Vec3 position, BlockState hitBlock) {
/* 352 */     applyEffects(
/* 353 */         getEffects(EnchantmentEffectComponents.HIT_BLOCK), 
/* 354 */         blockHitContext(serverLevel, enchantmentLevel, projectile, position, hitBlock), e -> e.apply(serverLevel, enchantmentLevel, weapon, projectile, position));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void modifyItemFilteredCount(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat value) {
/* 360 */     applyEffects(
/* 361 */         getEffects(effectType), 
/* 362 */         itemContext(serverLevel, enchantmentLevel, itemStack), e -> value.setValue(e.process(enchantmentLevel, serverLevel.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void modifyEntityFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity entity, MutableFloat value) {
/* 368 */     applyEffects(
/* 369 */         getEffects(effectType), 
/* 370 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), e -> value.setValue(e.process(enchantmentLevel, entity.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void modifyDamageFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat value) {
/* 376 */     applyEffects(
/* 377 */         getEffects(effectType), 
/* 378 */         damageContext(serverLevel, enchantmentLevel, victim, damageSource), e -> value.setValue(e.process(enchantmentLevel, victim.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootContext damageContext(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 384 */     LootParams params = new LootParams.Builder(serverLevel)
/* 385 */       .withParameter(LootContextParams.THIS_ENTITY, victim)
/* 386 */       .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
/* 387 */       .withParameter(LootContextParams.ORIGIN, victim.position())
/* 388 */       .withParameter(LootContextParams.DAMAGE_SOURCE, source)
/* 389 */       .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity())
/* 390 */       .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity())
/* 391 */       .create(LootContextParamSets.ENCHANTED_DAMAGE);
/* 392 */     return new LootContext.Builder(params).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext itemContext(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack) {
/* 396 */     LootParams params = new LootParams.Builder(serverLevel)
/* 397 */       .withParameter(LootContextParams.TOOL, itemStack)
/* 398 */       .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
/* 399 */       .create(LootContextParamSets.ENCHANTED_ITEM);
/* 400 */     return new LootContext.Builder(params).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext locationContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, boolean active) {
/* 404 */     LootParams params = new LootParams.Builder(serverLevel)
/* 405 */       .withParameter(LootContextParams.THIS_ENTITY, entity)
/* 406 */       .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
/* 407 */       .withParameter(LootContextParams.ORIGIN, entity.position())
/* 408 */       .withParameter(LootContextParams.ENCHANTMENT_ACTIVE, active)
/* 409 */       .create(LootContextParamSets.ENCHANTED_LOCATION);
/* 410 */     return new LootContext.Builder(params).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext entityContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position) {
/* 414 */     LootParams params = new LootParams.Builder(serverLevel)
/* 415 */       .withParameter(LootContextParams.THIS_ENTITY, entity)
/* 416 */       .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
/* 417 */       .withParameter(LootContextParams.ORIGIN, position)
/* 418 */       .create(LootContextParamSets.ENCHANTED_ENTITY);
/* 419 */     return new LootContext.Builder(params).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext blockHitContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position, BlockState hitBlock) {
/* 423 */     LootParams params = new LootParams.Builder(serverLevel)
/* 424 */       .withParameter(LootContextParams.THIS_ENTITY, entity)
/* 425 */       .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
/* 426 */       .withParameter(LootContextParams.ORIGIN, position)
/* 427 */       .withParameter(LootContextParams.BLOCK_STATE, hitBlock)
/* 428 */       .create(LootContextParamSets.HIT_BLOCK);
/* 429 */     return new LootContext.Builder(params).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext filterData, Consumer<T> action) {
/* 433 */     for (ConditionalEffect<T> conditionalEffect : effects) {
/* 434 */       if (conditionalEffect.matches(filterData))
/* 435 */         action.accept(conditionalEffect.effect()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runLocationChangedEffects(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/*     */     ObjectArraySet<EnchantmentLocationBasedEffect> objectArraySet;
/* 441 */     EquipmentSlot slot = item.inSlot();
/* 442 */     if (slot == null) {
/*     */       return;
/*     */     }
/* 445 */     Map<Enchantment, Set<EnchantmentLocationBasedEffect>> activeLocationDependentEffects = entity.activeLocationDependentEnchantments(slot);
/* 446 */     if (!matchingSlot(slot)) {
/* 447 */       Set<EnchantmentLocationBasedEffect> set = activeLocationDependentEffects.remove(this);
/* 448 */       if (set != null) {
/* 449 */         set.forEach(effect -> effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 454 */     Set<EnchantmentLocationBasedEffect> activeEffects = activeLocationDependentEffects.get(this);
/* 455 */     for (ConditionalEffect<EnchantmentLocationBasedEffect> filteredEffect : getEffects(EnchantmentEffectComponents.LOCATION_CHANGED)) {
/* 456 */       EnchantmentLocationBasedEffect effect = filteredEffect.effect();
/* 457 */       boolean wasActive = (activeEffects != null && activeEffects.contains(effect));
/* 458 */       if (filteredEffect.matches(locationContext(serverLevel, enchantmentLevel, (Entity)entity, wasActive))) {
/* 459 */         if (!wasActive) {
/* 460 */           if (activeEffects == null) {
/* 461 */             objectArraySet = new ObjectArraySet();
/* 462 */             activeLocationDependentEffects.put(this, objectArraySet);
/*     */           } 
/* 464 */           objectArraySet.add(effect);
/*     */         } 
/* 466 */         effect.onChangedBlock(serverLevel, enchantmentLevel, item, (Entity)entity, entity.position(), !wasActive); continue;
/* 467 */       }  if (objectArraySet != null && objectArraySet.remove(effect)) {
/* 468 */         effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel);
/*     */       }
/*     */     } 
/* 471 */     if (objectArraySet != null && objectArraySet.isEmpty()) {
/* 472 */       activeLocationDependentEffects.remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopLocationBasedEffects(int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/* 477 */     EquipmentSlot slot = item.inSlot();
/* 478 */     if (slot == null) {
/*     */       return;
/*     */     }
/* 481 */     Set<EnchantmentLocationBasedEffect> activeEffects = (Set<EnchantmentLocationBasedEffect>)entity.activeLocationDependentEnchantments(slot).remove(this);
/* 482 */     if (activeEffects == null) {
/*     */       return;
/*     */     }
/*     */     
/* 486 */     for (EnchantmentLocationBasedEffect effect : activeEffects)
/* 487 */       effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel); 
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final Enchantment.EnchantmentDefinition definition;
/* 493 */     private HolderSet<Enchantment> exclusiveSet = (HolderSet<Enchantment>)HolderSet.direct(new Holder[0]);
/* 494 */     private final Map<DataComponentType<?>, List<?>> effectLists = new java.util.HashMap<>();
/* 495 */     private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
/*     */     
/*     */     public Builder(Enchantment.EnchantmentDefinition definition) {
/* 498 */       this.definition = definition;
/*     */     }
/*     */     
/*     */     public Builder exclusiveWith(HolderSet<Enchantment> set) {
/* 502 */       this.exclusiveSet = set;
/* 503 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
/* 507 */       getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.of(condition.build())));
/* 508 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
/* 512 */       getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.empty()));
/* 513 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect, LootItemCondition.Builder condition) {
/* 517 */       getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(condition.build())));
/* 518 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect) {
/* 522 */       getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty()));
/* 523 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> type, EnchantmentAttributeEffect effect) {
/* 527 */       getEffectsList(type).add(effect);
/* 528 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <E> Builder withSpecialEffect(DataComponentType<E> type, E effect) {
/* 533 */       this.effectMapBuilder.set(type, effect);
/* 534 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<Unit> type) {
/* 538 */       this.effectMapBuilder.set(type, Unit.INSTANCE);
/* 539 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private <E> List<E> getEffectsList(DataComponentType<List<E>> type) {
/* 544 */       return (List<E>)this.effectLists.computeIfAbsent(type, k -> {
/*     */             ArrayList<E> newList = new ArrayList<>();
/*     */             this.effectMapBuilder.set(type, newList);
/*     */             return newList;
/*     */           });
/*     */     }
/*     */     
/*     */     public Enchantment build(Identifier descriptionKey) {
/* 552 */       return new Enchantment((Component)Component.translatable(net.minecraft.util.Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder enchantment(EnchantmentDefinition definition) {
/* 557 */     return new Builder(definition);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/Enchantment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */