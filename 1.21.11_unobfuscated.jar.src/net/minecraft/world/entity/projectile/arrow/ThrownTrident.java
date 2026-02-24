/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownTrident
/*     */   extends AbstractArrow
/*     */ {
/*  33 */   private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
/*  34 */   private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float WATER_INERTIA = 0.99F;
/*     */   
/*     */   private static final boolean DEFAULT_DEALT_DAMAGE = false;
/*     */   private boolean dealtDamage = false;
/*     */   public int clientSideReturnTridentTickCount;
/*     */   
/*     */   public ThrownTrident(EntityType<? extends ThrownTrident> type, Level level) {
/*  43 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level level, LivingEntity owner, ItemStack tridentItem) {
/*  47 */     super(EntityType.TRIDENT, owner, level, tridentItem, null);
/*  48 */     this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(tridentItem));
/*  49 */     this.entityData.set(ID_FOIL, tridentItem.hasFoil());
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level level, double x, double y, double z, ItemStack tridentItem) {
/*  53 */     super(EntityType.TRIDENT, x, y, z, level, tridentItem, tridentItem);
/*  54 */     this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(tridentItem));
/*  55 */     this.entityData.set(ID_FOIL, tridentItem.hasFoil());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  60 */     super.defineSynchedData(entityData);
/*     */     
/*  62 */     entityData.define(ID_LOYALTY, (byte)0);
/*  63 */     entityData.define(ID_FOIL, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  68 */     if (this.inGroundTime > 4) {
/*  69 */       this.dealtDamage = true;
/*     */     }
/*     */     
/*  72 */     Entity currentOwner = getOwner();
/*  73 */     int loyalty = (Byte)this.entityData.get(ID_LOYALTY);
/*     */     
/*  75 */     if (loyalty > 0 && (this.dealtDamage || isNoPhysics()) && currentOwner != null) {
/*  76 */       if (!isAcceptibleReturnOwner()) {
/*  77 */         Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (this.pickup == AbstractArrow.Pickup.ALLOWED)
/*  78 */             spawnAtLocation(serverLevel, getPickupItem(), 0.1F);  }
/*     */         
/*  80 */         discard();
/*     */       } else {
/*  82 */         if (!(currentOwner instanceof Player) && position().distanceTo(currentOwner.getEyePosition()) < currentOwner.getBbWidth() + 1.0D) {
/*  83 */           discard();
/*     */           return;
/*     */         } 
/*  86 */         setNoPhysics(true);
/*  87 */         Vec3 vec = currentOwner.getEyePosition().subtract(position());
/*  88 */         setPosRaw(getX(), getY() + vec.y * 0.015D * loyalty, getZ());
/*  89 */         double accel = 0.05D * loyalty;
/*  90 */         setDeltaMovement(getDeltaMovement().scale(0.95D).add(vec.normalize().scale(accel)));
/*     */         
/*  92 */         if (this.clientSideReturnTridentTickCount == 0) {
/*  93 */           playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
/*     */         }
/*     */         
/*  96 */         this.clientSideReturnTridentTickCount++;
/*     */       } 
/*     */     }
/*     */     
/* 100 */     super.tick();
/*     */   }
/*     */   
/*     */   private boolean isAcceptibleReturnOwner() {
/* 104 */     Entity currentOwner = getOwner();
/* 105 */     if (currentOwner == null || !currentOwner.isAlive()) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (currentOwner instanceof net.minecraft.server.level.ServerPlayer && currentOwner.isSpectator()) {
/* 109 */       return false;
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFoil() {
/* 115 */     return (Boolean)this.entityData.get(ID_FOIL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
/* 120 */     if (this.dealtDamage) {
/* 121 */       return null;
/*     */     }
/* 123 */     return super.findHitEntity(from, to);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
/* 128 */     EntityHitResult e = findHitEntity(from, to);
/* 129 */     if (e != null) {
/* 130 */       return List.of(e);
/*     */     }
/* 132 */     return List.of();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 137 */     Entity entity = hitResult.getEntity();
/* 138 */     float dmg = 8.0F;
/*     */     
/* 140 */     Entity currentOwner = getOwner();
/* 141 */     DamageSource damageSource = damageSources().trident((Entity)this, (currentOwner == null) ? (Entity)this : currentOwner);
/* 142 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 143 */       dmg = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity, damageSource, dmg); }
/*     */ 
/*     */     
/* 146 */     this.dealtDamage = true;
/*     */     
/* 148 */     if (entity.hurtOrSimulate(damageSource, dmg)) {
/* 149 */       if (entity.getType() == EntityType.ENDERMAN) {
/*     */         return;
/*     */       }
/* 152 */       level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 153 */         EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, getWeaponItem(), weapon -> kill(serverLevel)); }
/*     */       
/* 155 */       if (entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/* 156 */         doKnockback(mob, damageSource);
/* 157 */         doPostHurtEffects(mob); }
/*     */     
/*     */     } 
/* 160 */     deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
/* 161 */     setDeltaMovement(getDeltaMovement().multiply(0.02D, 0.2D, 0.02D));
/*     */     
/* 163 */     playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack weapon) {
/* 168 */     Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
/* 169 */     Entity entity = getOwner(); LivingEntity livingOwner = (LivingEntity)entity; EnchantmentHelper.onHitBlock(level, weapon, (entity instanceof LivingEntity) ? livingOwner : null, (Entity)this, null, compensatedHitPosition, level.getBlockState(hitResult.getBlockPos()), item -> kill(level));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getWeaponItem() {
/* 174 */     return getPickupItemStackOrigin();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean tryPickup(Player player) {
/* 179 */     return (super.tryPickup(player) || (isNoPhysics() && ownedBy((Entity)player) && player.getInventory().add(getPickupItem())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getDefaultPickupItem() {
/* 184 */     return new ItemStack((ItemLike)Items.TRIDENT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 189 */     return SoundEvents.TRIDENT_HIT_GROUND;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player player) {
/* 194 */     if (ownedBy((Entity)player) || getOwner() == null) {
/* 195 */       super.playerTouch(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 201 */     super.readAdditionalSaveData(input);
/*     */     
/* 203 */     this.dealtDamage = input.getBooleanOr("DealtDamage", false);
/*     */     
/* 205 */     this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(getPickupItemStackOrigin()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 210 */     super.addAdditionalSaveData(output);
/*     */     
/* 212 */     output.putBoolean("DealtDamage", this.dealtDamage);
/*     */   }
/*     */   
/*     */   private byte getLoyaltyFromItem(ItemStack tridentItem) {
/* 216 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 217 */       return (byte)Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, tridentItem, (Entity)this), 0, 127); }
/*     */     
/* 219 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickDespawn() {
/* 224 */     int loyalty = (Byte)this.entityData.get(ID_LOYALTY);
/*     */     
/* 226 */     if (this.pickup != AbstractArrow.Pickup.ALLOWED || loyalty <= 0) {
/* 227 */       super.tickDespawn();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterInertia() {
/* 233 */     return 0.99F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 238 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/arrow/ThrownTrident.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */