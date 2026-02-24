/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Horse extends AbstractHorse {
/*  40 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);
/*     */   
/*  42 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.HORSE.getDimensions()
/*  43 */     .withAttachments(EntityAttachments.builder()
/*  44 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.HORSE.getHeight() + 0.125F, 0.0F))
/*     */     
/*  46 */     .scale(0.5F);
/*     */   
/*     */   private static final int DEFAULT_VARIANT = 0;
/*     */   
/*     */   public Horse(EntityType<? extends Horse> type, Level level) {
/*  51 */     super((EntityType)type, level);
/*  52 */     setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
/*  53 */     setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes(RandomSource random) {
/*  58 */     Objects.requireNonNull(random); getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateMaxHealth(random::nextInt));
/*  59 */     Objects.requireNonNull(random); getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateSpeed(random::nextDouble));
/*  60 */     Objects.requireNonNull(random); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(random::nextDouble));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  65 */     super.defineSynchedData(entityData);
/*     */     
/*  67 */     entityData.define(DATA_ID_TYPE_VARIANT, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  72 */     super.addAdditionalSaveData(output);
/*     */     
/*  74 */     output.putInt("Variant", getTypeVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  79 */     super.readAdditionalSaveData(input);
/*     */     
/*  81 */     setTypeVariant(input.getIntOr("Variant", 0));
/*     */   }
/*     */   
/*     */   private void setTypeVariant(int i) {
/*  85 */     this.entityData.set(DATA_ID_TYPE_VARIANT, i);
/*     */   }
/*     */   
/*     */   private int getTypeVariant() {
/*  89 */     return (Integer)this.entityData.get(DATA_ID_TYPE_VARIANT);
/*     */   }
/*     */   
/*     */   private void setVariantAndMarkings(Variant variant, Markings markings) {
/*  93 */     setTypeVariant(variant.getId() & 0xFF | markings.getId() << 8 & 0xFF00);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/*  97 */     return Variant.byId(getTypeVariant() & 0xFF);
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 101 */     setTypeVariant(variant.getId() & 0xFF | getTypeVariant() & 0xFFFFFF00);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 106 */     if (type == DataComponents.HORSE_VARIANT) {
/* 107 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 110 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 115 */     applyImplicitComponentIfPresent(components, DataComponents.HORSE_VARIANT);
/* 116 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 121 */     if (type == DataComponents.HORSE_VARIANT) {
/* 122 */       setVariant((Variant)castComponentValue(DataComponents.HORSE_VARIANT, value));
/* 123 */       return true;
/*     */     } 
/*     */     
/* 126 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public Markings getMarkings() {
/* 130 */     return Markings.byId((getTypeVariant() & 0xFF00) >> 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playGallopSound(SoundType soundType) {
/* 135 */     super.playGallopSound(soundType);
/* 136 */     if (this.random.nextInt(10) == 0) {
/* 137 */       playSound(SoundEvents.HORSE_BREATHE, soundType.getVolume() * 0.6F, soundType.getPitch());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 143 */     return SoundEvents.HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 148 */     return SoundEvents.HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 153 */     return SoundEvents.HORSE_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 158 */     return SoundEvents.HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 163 */     return SoundEvents.HORSE_ANGRY;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 168 */     boolean shouldOpenInventory = (!isBaby() && isTamed() && player.isSecondaryUseActive());
/* 169 */     if (isVehicle() || shouldOpenInventory) {
/* 170 */       return super.mobInteract(player, hand);
/*     */     }
/*     */     
/* 173 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 175 */     if (!itemStack.isEmpty()) {
/* 176 */       if (isFood(itemStack)) {
/* 177 */         return fedFood(player, itemStack);
/*     */       }
/*     */       
/* 180 */       if (!isTamed()) {
/* 181 */         makeMad();
/* 182 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 185 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 190 */     if (partner == this) {
/* 191 */       return false;
/*     */     }
/*     */     
/* 194 */     if (partner instanceof Donkey || partner instanceof Horse) {
/* 195 */       return (canParent() && ((AbstractHorse)partner).canParent());
/*     */     }
/*     */     
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 203 */     if (partner instanceof Donkey) {
/* 204 */       Mule mule = (Mule)EntityType.MULE.create((Level)level, EntitySpawnReason.BREEDING);
/* 205 */       if (mule != null) {
/* 206 */         setOffspringAttributes(partner, mule);
/*     */       }
/* 208 */       return (AgeableMob)mule;
/*     */     } 
/* 210 */     Horse horsePartner = (Horse)partner;
/*     */     
/* 212 */     Horse baby = (Horse)EntityType.HORSE.create((Level)level, EntitySpawnReason.BREEDING);
/* 213 */     if (baby != null) {
/*     */       Variant variant; Markings markings;
/* 215 */       int selectSkin = this.random.nextInt(9);
/* 216 */       if (selectSkin < 4) {
/* 217 */         variant = getVariant();
/* 218 */       } else if (selectSkin < 8) {
/* 219 */         variant = horsePartner.getVariant();
/*     */       } else {
/* 221 */         variant = (Variant)Util.getRandom((Object[])Variant.values(), this.random);
/*     */       } 
/*     */ 
/*     */       
/* 225 */       int selectMarking = this.random.nextInt(5);
/* 226 */       if (selectMarking < 2) {
/* 227 */         markings = getMarkings();
/* 228 */       } else if (selectMarking < 4) {
/* 229 */         markings = horsePartner.getMarkings();
/*     */       } else {
/* 231 */         markings = (Markings)Util.getRandom((Object[])Markings.values(), this.random);
/*     */       } 
/*     */       
/* 234 */       baby.setVariantAndMarkings(variant, markings);
/* 235 */       setOffspringAttributes(partner, baby);
/*     */     } 
/* 237 */     return (AgeableMob)baby;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void hurtArmor(DamageSource damageSource, float damage) {
/* 247 */     doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.BODY });
/*     */   }
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     HorseGroupData horseGroupData;
/*     */     Variant variant;
/* 252 */     RandomSource random = level.getRandom();
/*     */     
/* 254 */     if (groupData instanceof HorseGroupData) {
/* 255 */       variant = ((HorseGroupData)groupData).variant;
/*     */     } else {
/* 257 */       variant = (Variant)Util.getRandom((Object[])Variant.values(), random);
/* 258 */       horseGroupData = new HorseGroupData(variant);
/*     */     } 
/* 260 */     setVariantAndMarkings(variant, (Markings)Util.getRandom((Object[])Markings.values(), random));
/*     */     
/* 262 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)horseGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 267 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public static class HorseGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Variant variant;
/*     */     
/*     */     public HorseGroupData(Variant variant) {
/* 274 */       super(true);
/* 275 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/equine/Horse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */