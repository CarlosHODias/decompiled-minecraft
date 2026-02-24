/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ public class CrossbowItem
/*     */   extends ProjectileWeaponItem
/*     */ {
/*     */   private static final float MAX_CHARGE_DURATION = 1.25F;
/*     */   public static final int DEFAULT_RANGE = 8;
/*     */   private boolean startSoundPlayed;
/*     */   private boolean midLoadSoundPlayed;
/*     */   private static final float START_SOUND_PERCENT = 0.2F;
/*     */   private static final float MID_SOUND_PERCENT = 0.5F;
/*     */   private static final float ARROW_POWER = 3.15F;
/*     */   private static final float FIREWORK_POWER = 1.6F;
/*     */   public static final float MOB_ARROW_POWER = 1.6F;
/*  54 */   private static final ChargingSounds DEFAULT_SOUNDS = new ChargingSounds(
/*  55 */       Optional.of(SoundEvents.CROSSBOW_LOADING_START), 
/*  56 */       Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), 
/*  57 */       Optional.of(SoundEvents.CROSSBOW_LOADING_END));
/*     */ 
/*     */   
/*     */   public CrossbowItem(Item.Properties properties) {
/*  61 */     super(properties);
/*  62 */     this.startSoundPlayed = false;
/*  63 */     this.midLoadSoundPlayed = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getSupportedHeldProjectiles() {
/*  68 */     return ARROW_OR_FIREWORK;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getAllSupportedProjectiles() {
/*  73 */     return ARROW_ONLY;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*  78 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/*  80 */     ChargedProjectiles chargedProjectiles = (ChargedProjectiles)itemStack.get(DataComponents.CHARGED_PROJECTILES);
/*  81 */     if (chargedProjectiles != null && !chargedProjectiles.isEmpty()) {
/*  82 */       performShooting(level, (LivingEntity)player, hand, itemStack, getShootingPower(chargedProjectiles), 1.0F, null);
/*  83 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*     */     
/*  86 */     if (!player.getProjectile(itemStack).isEmpty()) {
/*  87 */       this.startSoundPlayed = false;
/*  88 */       this.midLoadSoundPlayed = false;
/*  89 */       player.startUsingItem(hand);
/*  90 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*  92 */     return (InteractionResult)InteractionResult.FAIL;
/*     */   }
/*     */   
/*     */   private static float getShootingPower(ChargedProjectiles projectiles) {
/*  96 */     if (projectiles.contains(Items.FIREWORK_ROCKET)) {
/*  97 */       return 1.6F;
/*     */     }
/*  99 */     return 3.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/* 104 */     int timeHeld = getUseDuration(itemStack, entity) - remainingTime;
/* 105 */     return (getPowerForTime(timeHeld, itemStack, entity) >= 1.0F && isCharged(itemStack));
/*     */   }
/*     */   
/*     */   private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack heldItem) {
/* 109 */     List<ItemStack> drawn = draw(heldItem, shooter.getProjectile(heldItem), shooter);
/* 110 */     if (!drawn.isEmpty()) {
/* 111 */       heldItem.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(drawn));
/* 112 */       return true;
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isCharged(ItemStack itemStack) {
/* 118 */     ChargedProjectiles projectiles = (ChargedProjectiles)itemStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
/* 119 */     return !projectiles.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void shootProjectile(LivingEntity livingEntity, Projectile projectileEntity, int index, float power, float uncertainty, float angle, LivingEntity targetOverride) {
/*     */     Vector3f shotVector;
/* 125 */     if (targetOverride != null) {
/* 126 */       double xd = targetOverride.getX() - livingEntity.getX();
/* 127 */       double zd = targetOverride.getZ() - livingEntity.getZ();
/* 128 */       double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 129 */       double yd = targetOverride.getY(0.3333333333333333D) - projectileEntity.getY() + distanceToTarget * 0.20000000298023224D;
/* 130 */       shotVector = getProjectileShotVector(livingEntity, new Vec3(xd, yd, zd), angle);
/*     */     } else {
/* 132 */       Vec3 upVector = livingEntity.getUpVector(1.0F);
/* 133 */       Quaternionf upQuaternion = new Quaternionf().setAngleAxis((angle * 0.017453292F), upVector.x, upVector.y, upVector.z);
/* 134 */       Vec3 viewVec = livingEntity.getViewVector(1.0F);
/* 135 */       shotVector = viewVec.toVector3f().rotate((Quaternionfc)upQuaternion);
/*     */     } 
/* 137 */     projectileEntity.shoot(shotVector.x(), shotVector.y(), shotVector.z(), power, uncertainty);
/* 138 */     float soundPitch = getShotPitch(livingEntity.getRandom(), index);
/* 139 */     livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_SHOOT, livingEntity.getSoundSource(), 1.0F, soundPitch);
/*     */   }
/*     */   
/*     */   private static Vector3f getProjectileShotVector(LivingEntity body, Vec3 originalVector, float angle) {
/* 143 */     Vector3f viewVec = originalVector.toVector3f().normalize();
/* 144 */     Vector3f rightVectorPreRot = new Vector3f((Vector3fc)viewVec).cross((Vector3fc)new Vector3f(0.0F, 1.0F, 0.0F));
/* 145 */     if (rightVectorPreRot.lengthSquared() <= 1.0E-7D) {
/* 146 */       Vec3 up = body.getUpVector(1.0F);
/* 147 */       rightVectorPreRot = new Vector3f((Vector3fc)viewVec).cross((Vector3fc)up.toVector3f());
/*     */     } 
/*     */     
/* 150 */     Vector3f viewVec3f = new Vector3f((Vector3fc)viewVec).rotateAxis(1.5707964F, rightVectorPreRot.x, rightVectorPreRot.y, rightVectorPreRot.z);
/* 151 */     return new Vector3f((Vector3fc)viewVec).rotateAxis(angle * 0.017453292F, viewVec3f.x, viewVec3f.y, viewVec3f.z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack heldItem, ItemStack projectile, boolean isCrit) {
/* 156 */     if (projectile.is(Items.FIREWORK_ROCKET)) {
/* 157 */       return (Projectile)new FireworkRocketEntity(level, projectile, (Entity)shooter, shooter.getX(), shooter.getEyeY() - 0.15000000596046448D, shooter.getZ(), true);
/*     */     }
/*     */     
/* 160 */     Projectile projectileEntity = super.createProjectile(level, shooter, heldItem, projectile, isCrit);
/* 161 */     if (projectileEntity instanceof AbstractArrow) { AbstractArrow arrow = (AbstractArrow)projectileEntity;
/* 162 */       arrow.setSoundEvent(SoundEvents.CROSSBOW_HIT); }
/*     */ 
/*     */     
/* 165 */     return projectileEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDurabilityUse(ItemStack projectile) {
/* 170 */     return projectile.is(Items.FIREWORK_ROCKET) ? 3 : 1;
/*     */   }
/*     */   
/*     */   public void performShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float power, float uncertainty, LivingEntity targetOverride) {
/*     */     ServerLevel serverLevel;
/* 175 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/* 178 */      ChargedProjectiles charged = weapon.<ChargedProjectiles>set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
/* 179 */     if (charged == null || charged.isEmpty()) {
/*     */       return;
/*     */     }
/* 182 */     shoot(serverLevel, shooter, hand, weapon, charged.getItems(), power, uncertainty, shooter instanceof Player, targetOverride);
/*     */     
/* 184 */     if (shooter instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)shooter;
/* 185 */       CriteriaTriggers.SHOT_CROSSBOW.trigger(player, weapon);
/* 186 */       player.awardStat(Stats.ITEM_USED.get(weapon.getItem())); }
/*     */   
/*     */   }
/*     */   
/*     */   private static float getShotPitch(RandomSource random, int index) {
/* 191 */     if (index == 0) {
/* 192 */       return 1.0F;
/*     */     }
/* 194 */     return getRandomShotPitch(((index & 0x1) == 1), random);
/*     */   }
/*     */   
/*     */   private static float getRandomShotPitch(boolean highPitch, RandomSource random) {
/* 198 */     float rangeDecider = highPitch ? 0.63F : 0.43F;
/* 199 */     return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + rangeDecider;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int ticksRemaining) {
/* 204 */     if (!level.isClientSide()) {
/* 205 */       ChargingSounds sounds = getChargingSounds(itemStack);
/* 206 */       float tickPercent = (itemStack.getUseDuration(entity) - ticksRemaining) / getChargeDuration(itemStack, entity);
/*     */       
/* 208 */       if (tickPercent < 0.2F) {
/* 209 */         this.startSoundPlayed = false;
/* 210 */         this.midLoadSoundPlayed = false;
/*     */       } 
/*     */       
/* 213 */       if (tickPercent >= 0.2F && !this.startSoundPlayed) {
/* 214 */         this.startSoundPlayed = true;
/* 215 */         sounds.start().ifPresent(sound -> level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
/*     */       } 
/*     */       
/* 218 */       if (tickPercent >= 0.5F && !this.midLoadSoundPlayed) {
/* 219 */         this.midLoadSoundPlayed = true;
/* 220 */         sounds.mid().ifPresent(sound -> level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
/*     */       } 
/*     */       
/* 223 */       if (tickPercent >= 1.0F && !isCharged(itemStack) && 
/* 224 */         tryLoadProjectiles(entity, itemStack)) {
/* 225 */         sounds.end().ifPresent(sound -> level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), entity.getSoundSource(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 233 */     return 72000;
/*     */   }
/*     */   
/*     */   public static int getChargeDuration(ItemStack crossbow, LivingEntity user) {
/* 237 */     float duration = EnchantmentHelper.modifyCrossbowChargingTime(crossbow, user, 1.25F);
/* 238 */     return Mth.floor(duration * 20.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 243 */     return ItemUseAnimation.CROSSBOW;
/*     */   }
/*     */   
/*     */   ChargingSounds getChargingSounds(ItemStack itemStack) {
/* 247 */     return EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
/*     */   }
/*     */   
/*     */   private static float getPowerForTime(int timeHeld, ItemStack itemStack, LivingEntity holder) {
/* 251 */     float pow = timeHeld / getChargeDuration(itemStack, holder);
/* 252 */     if (pow > 1.0F) {
/* 253 */       pow = 1.0F;
/*     */     }
/* 255 */     return pow;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useOnRelease(ItemStack itemStack) {
/* 260 */     return itemStack.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultProjectileRange() {
/* 265 */     return 8;
/*     */   }
/*     */   public static final class ChargingSounds extends Record { private final Optional<Holder<SoundEvent>> start; private final Optional<Holder<SoundEvent>> mid; private final Optional<Holder<SoundEvent>> end; public static final Codec<ChargingSounds> CODEC;
/* 268 */     public ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) { this.start = start; this.mid = mid; this.end = end; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #268	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 268 */       //   0	7	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds; } public Optional<Holder<SoundEvent>> start() { return this.start; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #268	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #268	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;
/* 268 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Holder<SoundEvent>> mid() { return this.mid; } public Optional<Holder<SoundEvent>> end() { return this.end; } static {
/* 269 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.optionalFieldOf("start").forGetter(ChargingSounds::start), (App)SoundEvent.CODEC.optionalFieldOf("mid").forGetter(ChargingSounds::mid), (App)SoundEvent.CODEC.optionalFieldOf("end").forGetter(ChargingSounds::end)).apply((Applicative)i, ChargingSounds::new));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ChargeType
/*     */     implements StringRepresentable
/*     */   {
/* 278 */     NONE("none"),
/* 279 */     ARROW("arrow"),
/* 280 */     ROCKET("rocket");
/*     */ 
/*     */     
/* 283 */     public static final Codec<ChargeType> CODEC = (Codec<ChargeType>)StringRepresentable.fromEnum(ChargeType::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     ChargeType(String name) {
/* 288 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 293 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/CrossbowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */