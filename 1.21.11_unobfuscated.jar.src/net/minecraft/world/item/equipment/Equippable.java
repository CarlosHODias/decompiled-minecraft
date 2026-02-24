/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class Equippable extends Record {
/*     */   private final EquipmentSlot slot;
/*     */   private final Holder<SoundEvent> equipSound;
/*     */   private final Optional<ResourceKey<EquipmentAsset>> assetId;
/*     */   private final Optional<Identifier> cameraOverlay;
/*     */   private final Optional<HolderSet<EntityType<?>>> allowedEntities;
/*     */   private final boolean dispensable;
/*     */   
/*  33 */   public Equippable(EquipmentSlot slot, Holder<SoundEvent> equipSound, Optional<ResourceKey<EquipmentAsset>> assetId, Optional<Identifier> cameraOverlay, Optional<HolderSet<EntityType<?>>> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt, boolean equipOnInteract, boolean canBeSheared, Holder<SoundEvent> shearingSound) { this.slot = slot; this.equipSound = equipSound; this.assetId = assetId; this.cameraOverlay = cameraOverlay; this.allowedEntities = allowedEntities; this.dispensable = dispensable; this.swappable = swappable; this.damageOnHurt = damageOnHurt; this.equipOnInteract = equipOnInteract; this.canBeSheared = canBeSheared; this.shearingSound = shearingSound; } private final boolean swappable; private final boolean damageOnHurt; private final boolean equipOnInteract; private final boolean canBeSheared; private final Holder<SoundEvent> shearingSound; public static final Codec<Equippable> CODEC; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/Equippable;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/Equippable;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/Equippable;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/Equippable;
/*  33 */     //   0	8	1	o	Ljava/lang/Object; } public EquipmentSlot slot() { return this.slot; } public Holder<SoundEvent> equipSound() { return this.equipSound; } public Optional<ResourceKey<EquipmentAsset>> assetId() { return this.assetId; } public Optional<Identifier> cameraOverlay() { return this.cameraOverlay; } public Optional<HolderSet<EntityType<?>>> allowedEntities() { return this.allowedEntities; } public boolean dispensable() { return this.dispensable; } public boolean swappable() { return this.swappable; } public boolean damageOnHurt() { return this.damageOnHurt; } public boolean equipOnInteract() { return this.equipOnInteract; } public boolean canBeSheared() { return this.canBeSheared; } public Holder<SoundEvent> shearingSound() { return this.shearingSound; }
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
/*     */   
/*     */   static {
/*  46 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EquipmentSlot.CODEC.fieldOf("slot").forGetter(Equippable::slot), (App)SoundEvent.CODEC.optionalFieldOf("equip_sound", SoundEvents.ARMOR_EQUIP_GENERIC).forGetter(Equippable::equipSound), (App)ResourceKey.codec(EquipmentAssets.ROOT_ID).optionalFieldOf("asset_id").forGetter(Equippable::assetId), (App)Identifier.CODEC.optionalFieldOf("camera_overlay").forGetter(Equippable::cameraOverlay), (App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(Equippable::allowedEntities), (App)Codec.BOOL.optionalFieldOf("dispensable", true).forGetter(Equippable::dispensable), (App)Codec.BOOL.optionalFieldOf("swappable", true).forGetter(Equippable::swappable), (App)Codec.BOOL.optionalFieldOf("damage_on_hurt", true).forGetter(Equippable::damageOnHurt), (App)Codec.BOOL.optionalFieldOf("equip_on_interact", false).forGetter(Equippable::equipOnInteract), (App)Codec.BOOL.optionalFieldOf("can_be_sheared", false).forGetter(Equippable::canBeSheared), (App)SoundEvent.CODEC.optionalFieldOf("shearing_sound", BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP)).forGetter(Equippable::shearingSound)).apply((com.mojang.datafixers.kinds.Applicative)i, Equippable::new));
/*     */   }
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
/*     */ 
/*     */   
/*  60 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Equippable> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(EquipmentSlot.STREAM_CODEC, Equippable::slot, SoundEvent.STREAM_CODEC, Equippable::equipSound, 
/*     */ 
/*     */       
/*  63 */       ResourceKey.streamCodec(EquipmentAssets.ROOT_ID).apply(ByteBufCodecs::optional), Equippable::assetId, 
/*  64 */       Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), Equippable::cameraOverlay, 
/*  65 */       ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), Equippable::allowedEntities, ByteBufCodecs.BOOL, Equippable::dispensable, ByteBufCodecs.BOOL, Equippable::swappable, ByteBufCodecs.BOOL, Equippable::damageOnHurt, ByteBufCodecs.BOOL, Equippable::equipOnInteract, ByteBufCodecs.BOOL, Equippable::canBeSheared, SoundEvent.STREAM_CODEC, Equippable::shearingSound, Equippable::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Equippable llamaSwag(DyeColor color) {
/*  76 */     return builder(EquipmentSlot.BODY)
/*  77 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.LLAMA_SWAG)
/*  78 */       .setAsset(EquipmentAssets.CARPETS.get(color))
/*  79 */       .setAllowedEntities((EntityType<?>[])new EntityType[] { EntityType.LLAMA, EntityType.TRADER_LLAMA
/*  80 */         }).setCanBeSheared(true)
/*  81 */       .setShearingSound((Holder<SoundEvent>)SoundEvents.LLAMA_CARPET_UNEQUIP)
/*  82 */       .build();
/*     */   }
/*     */   
/*     */   public static Equippable saddle() {
/*  86 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((net.minecraft.core.Registry)BuiltInRegistries.ENTITY_TYPE);
/*  87 */     return builder(EquipmentSlot.SADDLE)
/*  88 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.HORSE_SADDLE)
/*  89 */       .setAsset(EquipmentAssets.SADDLE)
/*  90 */       .setAllowedEntities((HolderSet<EntityType<?>>)entityGetter.getOrThrow(net.minecraft.tags.EntityTypeTags.CAN_EQUIP_SADDLE))
/*  91 */       .setEquipOnInteract(true)
/*  92 */       .setCanBeSheared(true)
/*  93 */       .setShearingSound((Holder<SoundEvent>)SoundEvents.SADDLE_UNEQUIP)
/*  94 */       .build();
/*     */   }
/*     */   
/*     */   public static Equippable harness(DyeColor color) {
/*  98 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((net.minecraft.core.Registry)BuiltInRegistries.ENTITY_TYPE);
/*  99 */     return builder(EquipmentSlot.BODY)
/* 100 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.HARNESS_EQUIP)
/* 101 */       .setAsset(EquipmentAssets.HARNESSES.get(color))
/* 102 */       .setAllowedEntities((HolderSet<EntityType<?>>)entityGetter.getOrThrow(net.minecraft.tags.EntityTypeTags.CAN_EQUIP_HARNESS))
/* 103 */       .setEquipOnInteract(true)
/* 104 */       .setCanBeSheared(true)
/* 105 */       .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.HARNESS_UNEQUIP))
/* 106 */       .build();
/*     */   }
/*     */   
/*     */   public static Builder builder(EquipmentSlot slot) {
/* 110 */     return new Builder(slot);
/*     */   }
/*     */   
/*     */   public InteractionResult swapWithEquipmentSlot(ItemStack inHand, Player player) {
/* 114 */     if (!player.canUseSlot(this.slot) || !canBeEquippedBy(player.getType())) {
/* 115 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 118 */     ItemStack inEquipmentSlot = player.getItemBySlot(this.slot);
/*     */     
/* 120 */     if ((net.minecraft.world.item.enchantment.EnchantmentHelper.has(inEquipmentSlot, net.minecraft.world.item.enchantment.EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) && !player.isCreative()) || ItemStack.isSameItemSameComponents(inHand, inEquipmentSlot)) {
/* 121 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/* 124 */     if (!player.level().isClientSide()) {
/* 125 */       player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(inHand.getItem()));
/*     */     }
/*     */     
/* 128 */     if (inHand.getCount() <= 1) {
/*     */       
/* 130 */       ItemStack swappedToHand = inEquipmentSlot.isEmpty() ? inHand : inEquipmentSlot.copyAndClear();
/* 131 */       ItemStack itemStack1 = player.isCreative() ? inHand.copy() : inHand.copyAndClear();
/* 132 */       player.setItemSlot(this.slot, itemStack1);
/* 133 */       return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(swappedToHand);
/*     */     } 
/*     */     
/* 136 */     ItemStack swappedToInventory = inEquipmentSlot.copyAndClear();
/* 137 */     ItemStack swappedToEquipment = inHand.consumeAndReturn(1, (LivingEntity)player);
/* 138 */     player.setItemSlot(this.slot, swappedToEquipment);
/* 139 */     if (!player.getInventory().add(swappedToInventory)) {
/* 140 */       player.drop(swappedToInventory, false);
/*     */     }
/* 142 */     return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(inHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult equipOnTarget(Player player, LivingEntity target, ItemStack itemStack) {
/* 147 */     if (!target.isEquippableInSlot(itemStack, this.slot) || target.hasItemInSlot(this.slot) || !target.isAlive()) {
/* 148 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 150 */     if (!player.level().isClientSide()) {
/* 151 */       target.setItemSlot(this.slot, itemStack.split(1));
/* 152 */       if (target instanceof net.minecraft.world.entity.Mob) { net.minecraft.world.entity.Mob mob = (net.minecraft.world.entity.Mob)target;
/* 153 */         mob.setGuaranteedDrop(this.slot); }
/*     */     
/*     */     } 
/* 156 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public boolean canBeEquippedBy(EntityType<?> type) {
/* 160 */     return (this.allowedEntities.isEmpty() || ((HolderSet)this.allowedEntities.get()).contains((Holder)type.builtInRegistryHolder()));
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final EquipmentSlot slot;
/* 165 */     private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/* 166 */     private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
/* 167 */     private Optional<Identifier> cameraOverlay = Optional.empty();
/* 168 */     private Optional<HolderSet<EntityType<?>>> allowedEntities = Optional.empty();
/*     */     private boolean dispensable = true;
/*     */     private boolean swappable = true;
/*     */     private boolean damageOnHurt = true;
/*     */     private boolean equipOnInteract;
/*     */     private boolean canBeSheared;
/* 174 */     private Holder<SoundEvent> shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */     
/*     */     private Builder(EquipmentSlot slot) {
/* 177 */       this.slot = slot;
/*     */     }
/*     */     
/*     */     public Builder setEquipSound(Holder<SoundEvent> equipSound) {
/* 181 */       this.equipSound = equipSound;
/* 182 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAsset(ResourceKey<EquipmentAsset> assetId) {
/* 186 */       this.assetId = Optional.of(assetId);
/* 187 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCameraOverlay(Identifier cameraOverlay) {
/* 191 */       this.cameraOverlay = Optional.of(cameraOverlay);
/* 192 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAllowedEntities(EntityType<?>... allowedEntities) {
/* 196 */       return setAllowedEntities((HolderSet<EntityType<?>>)HolderSet.direct(EntityType::builtInRegistryHolder, (Object[])allowedEntities));
/*     */     }
/*     */     
/*     */     public Builder setAllowedEntities(HolderSet<EntityType<?>> allowedEntities) {
/* 200 */       this.allowedEntities = Optional.of(allowedEntities);
/* 201 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDispensable(boolean dispensable) {
/* 205 */       this.dispensable = dispensable;
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSwappable(boolean swappable) {
/* 210 */       this.swappable = swappable;
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDamageOnHurt(boolean damageOnHurt) {
/* 215 */       this.damageOnHurt = damageOnHurt;
/* 216 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setEquipOnInteract(boolean equipOnInteract) {
/* 220 */       this.equipOnInteract = equipOnInteract;
/* 221 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCanBeSheared(boolean canBeSheared) {
/* 225 */       this.canBeSheared = canBeSheared;
/* 226 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
/* 230 */       this.shearingSound = shearingSound;
/* 231 */       return this;
/*     */     }
/*     */     
/*     */     public Equippable build() {
/* 235 */       return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/equipment/Equippable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */