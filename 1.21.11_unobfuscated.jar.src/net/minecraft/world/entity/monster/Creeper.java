/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SwellGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Creeper
/*     */   extends Monster {
/*  48 */   private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
/*  49 */   private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*  50 */   private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_IGNITED = false;
/*     */   
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   private static final short DEFAULT_MAX_SWELL = 30;
/*     */   private static final byte DEFAULT_EXPLOSION_RADIUS = 3;
/*     */   private int oldSwell;
/*     */   private int swell;
/*  59 */   private int maxSwell = 30;
/*  60 */   private int explosionRadius = 3;
/*     */   private boolean droppedSkulls;
/*     */   
/*     */   public Creeper(EntityType<? extends Creeper> type, Level level) {
/*  64 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  69 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  70 */     this.goalSelector.addGoal(2, (Goal)new SwellGoal(this));
/*  71 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
/*  72 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Cat.class, 6.0F, 1.0D, 1.2D));
/*  73 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  74 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal(this, 0.8D));
/*  75 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  76 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  78 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  79 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class<?>[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  83 */     return Monster.createMonsterAttributes()
/*  84 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFallDistance() {
/*  89 */     if (getTarget() == null) {
/*  90 */       return getComfortableFallDistance(0.0F);
/*     */     }
/*     */     
/*  93 */     return getComfortableFallDistance(getHealth() - 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/*  98 */     boolean damaged = super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*     */     
/* 100 */     this.swell += (int)(fallDistance * 1.5D);
/* 101 */     if (this.swell > this.maxSwell - 5) {
/* 102 */       this.swell = this.maxSwell - 5;
/*     */     }
/* 104 */     return damaged;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 109 */     super.defineSynchedData(entityData);
/*     */     
/* 111 */     entityData.define(DATA_SWELL_DIR, -1);
/* 112 */     entityData.define(DATA_IS_POWERED, false);
/* 113 */     entityData.define(DATA_IS_IGNITED, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 118 */     super.addAdditionalSaveData(output);
/* 119 */     output.putBoolean("powered", isPowered());
/* 120 */     output.putShort("Fuse", (short)this.maxSwell);
/* 121 */     output.putByte("ExplosionRadius", (byte)this.explosionRadius);
/* 122 */     output.putBoolean("ignited", isIgnited());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 127 */     super.readAdditionalSaveData(input);
/* 128 */     this.entityData.set(DATA_IS_POWERED, input.getBooleanOr("powered", false));
/* 129 */     this.maxSwell = input.getShortOr("Fuse", (short)30);
/* 130 */     this.explosionRadius = input.getByteOr("ExplosionRadius", (byte)3);
/* 131 */     if (input.getBooleanOr("ignited", false)) {
/* 132 */       ignite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 138 */     if (isAlive()) {
/* 139 */       this.oldSwell = this.swell;
/*     */ 
/*     */       
/* 142 */       if (isIgnited()) {
/* 143 */         setSwellDir(1);
/*     */       }
/*     */       
/* 146 */       int swellDir = getSwellDir();
/* 147 */       if (swellDir > 0 && this.swell == 0) {
/* 148 */         playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
/* 149 */         gameEvent((Holder)GameEvent.PRIME_FUSE);
/*     */       } 
/* 151 */       this.swell += swellDir;
/* 152 */       if (this.swell < 0) {
/* 153 */         this.swell = 0;
/*     */       }
/* 155 */       if (this.swell >= this.maxSwell) {
/* 156 */         this.swell = this.maxSwell;
/* 157 */         explodeCreeper();
/*     */       } 
/*     */     } 
/* 160 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity target) {
/* 165 */     if (target instanceof net.minecraft.world.entity.animal.goat.Goat) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     super.setTarget(target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 174 */     return SoundEvents.CREEPER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 179 */     return SoundEvents.CREEPER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 184 */     if (shouldDropLoot(level) && isPowered() && !this.droppedSkulls) {
/* 185 */       entity.dropFromLootTable(level, source, false, BuiltInLootTables.CHARGED_CREEPER, itemStack -> {
/*     */             entity.spawnAtLocation(entity, level);
/*     */             this.droppedSkulls = true;
/*     */           });
/*     */     }
/* 190 */     return super.killedEntity(level, entity, source);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/* 199 */     return (Boolean)this.entityData.get(DATA_IS_POWERED);
/*     */   }
/*     */   
/*     */   public float getSwelling(float a) {
/* 203 */     return Mth.lerp(a, this.oldSwell, this.swell) / (this.maxSwell - 2);
/*     */   }
/*     */   
/*     */   public int getSwellDir() {
/* 207 */     return (Integer)this.entityData.get(DATA_SWELL_DIR);
/*     */   }
/*     */   
/*     */   public void setSwellDir(int dir) {
/* 211 */     this.entityData.set(DATA_SWELL_DIR, dir);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 216 */     super.thunderHit(level, lightningBolt);
/* 217 */     this.entityData.set(DATA_IS_POWERED, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 222 */     ItemStack itemStack = player.getItemInHand(hand);
/* 223 */     if (itemStack.is(ItemTags.CREEPER_IGNITERS)) {
/* 224 */       SoundEvent soundEvent = itemStack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
/* 225 */       level().playSound((Entity)player, getX(), getY(), getZ(), soundEvent, getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
/* 226 */       if (!level().isClientSide()) {
/* 227 */         ignite();
/* 228 */         if (!itemStack.isDamageableItem()) {
/* 229 */           itemStack.shrink(1);
/*     */         } else {
/* 231 */           itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*     */         } 
/*     */       } 
/* 234 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 237 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void explodeCreeper() {
/* 241 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 242 */       float explosionMultiplier = isPowered() ? 2.0F : 1.0F;
/* 243 */       this.dead = true;
/* 244 */       serverLevel.explode((Entity)this, getX(), getY(), getZ(), this.explosionRadius * explosionMultiplier, Level.ExplosionInteraction.MOB);
/* 245 */       spawnLingeringCloud();
/* 246 */       triggerOnDeathMobEffects(serverLevel, Entity.RemovalReason.KILLED);
/* 247 */       discard(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void spawnLingeringCloud() {
/* 252 */     Collection<MobEffectInstance> activeEffects = getActiveEffects();
/* 253 */     if (!activeEffects.isEmpty()) {
/* 254 */       AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 255 */       cloud.setRadius(2.5F);
/* 256 */       cloud.setRadiusOnUse(-0.5F);
/* 257 */       cloud.setWaitTime(10);
/* 258 */       cloud.setDuration(300);
/* 259 */       cloud.setPotionDurationScale(0.25F);
/* 260 */       cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
/* 261 */       for (MobEffectInstance mobEffect : activeEffects) {
/* 262 */         cloud.addEffect(new MobEffectInstance(mobEffect));
/*     */       }
/* 264 */       level().addFreshEntity((Entity)cloud);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isIgnited() {
/* 269 */     return (Boolean)this.entityData.get(DATA_IS_IGNITED);
/*     */   }
/*     */   
/*     */   public void ignite() {
/* 273 */     this.entityData.set(DATA_IS_IGNITED, true);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/Creeper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */