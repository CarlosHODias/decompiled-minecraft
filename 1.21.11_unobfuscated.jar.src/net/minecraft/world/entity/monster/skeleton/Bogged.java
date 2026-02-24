/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Bogged extends AbstractSkeleton implements Shearable {
/*  34 */   private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(Bogged.class, EntityDataSerializers.BOOLEAN);
/*     */   private static final String SHEARED_TAG_NAME = "sheared";
/*     */   private static final boolean DEFAULT_SHEARED = false;
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  39 */     return AbstractSkeleton.createAttributes()
/*  40 */       .add(Attributes.MAX_HEALTH, 16.0D);
/*     */   }
/*     */   
/*     */   public Bogged(EntityType<? extends Bogged> type, Level level) {
/*  44 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  49 */     super.defineSynchedData(entityData);
/*     */     
/*  51 */     entityData.define(DATA_SHEARED, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  56 */     super.addAdditionalSaveData(output);
/*  57 */     output.putBoolean("sheared", isSheared());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  62 */     super.readAdditionalSaveData(input);
/*  63 */     setSheared(input.getBooleanOr("sheared", false));
/*     */   }
/*     */   
/*     */   public boolean isSheared() {
/*  67 */     return (Boolean)this.entityData.get(DATA_SHEARED);
/*     */   }
/*     */   
/*     */   public void setSheared(boolean sheared) {
/*  71 */     this.entityData.set(DATA_SHEARED, sheared);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  76 */     ItemStack itemStack = player.getItemInHand(hand);
/*  77 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/*  78 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  79 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/*  80 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/*  81 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot()); }
/*     */       
/*  83 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  86 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  91 */     return SoundEvents.BOGGED_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  96 */     return SoundEvents.BOGGED_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 101 */     return SoundEvents.BOGGED_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 106 */     return SoundEvents.BOGGED_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 111 */     AbstractArrow abstractArrow = super.getArrow(projectile, power, firingWeapon);
/* 112 */     if (abstractArrow instanceof Arrow) { Arrow arrow = (Arrow)abstractArrow;
/* 113 */       arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 100)); }
/*     */     
/* 115 */     return abstractArrow;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getHardAttackInterval() {
/* 120 */     return 50;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAttackInterval() {
/* 125 */     return 70;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 130 */     level.playSound(null, (Entity)this, SoundEvents.BOGGED_SHEAR, soundSource, 1.0F, 1.0F);
/*     */     
/* 132 */     spawnShearedMushrooms(level, tool);
/* 133 */     setSheared(true);
/*     */   }
/*     */   
/*     */   private void spawnShearedMushrooms(ServerLevel level, ItemStack tool) {
/* 137 */     dropFromShearingLootTable(level, BuiltInLootTables.BOGGED_SHEAR, tool, (l, drop) -> spawnAtLocation(l, drop, getBbHeight()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 142 */     return (!isSheared() && isAlive());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/skeleton/Bogged.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */