/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Allay extends PathfinderMob implements InventoryCarrier, VibrationSystem {
/*  75 */   private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
/*     */   
/*     */   private static final int LIFTING_ITEM_ANIMATION_DURATION = 5;
/*     */   
/*     */   private static final float DANCING_LOOP_DURATION = 55.0F;
/*     */   private static final float SPINNING_ANIMATION_DURATION = 15.0F;
/*     */   private static final int DEFAULT_DUPLICATION_COOLDOWN = 0;
/*     */   private static final int DUPLICATION_COOLDOWN_TICKS = 6000;
/*     */   private static final int NUM_OF_DUPLICATION_HEARTS = 3;
/*     */   public static final int MAX_NOTEBLOCK_DISTANCE = 1024;
/*  85 */   private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*  86 */   private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  88 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Allay>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK_POSITION, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING, (Object[])new MemoryModuleType[0]);
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
/*     */ 
/*     */   
/* 110 */   public static final ImmutableList<Float> THROW_SOUND_PITCHES = ImmutableList.of(0.5625F, 
/* 111 */       0.625F, 
/* 112 */       0.75F, 
/* 113 */       0.9375F, 
/* 114 */       1.0F, 
/* 115 */       1.0F, 
/* 116 */       1.125F, 
/* 117 */       1.25F, 
/* 118 */       1.5F, 
/* 119 */       1.875F, 
/* 120 */       2.0F, 
/* 121 */       2.25F, 
/* 122 */       (Object[])new Float[] { 2.5F, 
/* 123 */         3.0F, 
/* 124 */         3.75F, 
/* 125 */         4.0F });
/*     */   
/*     */   private final DynamicGameEventListener<VibrationSystem.Listener> dynamicVibrationListener;
/*     */   
/*     */   private VibrationSystem.Data vibrationData;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   
/*     */   private final DynamicGameEventListener<JukeboxListener> dynamicJukeboxListener;
/*     */   
/* 135 */   private final SimpleContainer inventory = new SimpleContainer(1);
/*     */   private BlockPos jukeboxPos;
/* 137 */   private long duplicationCooldown = 0L;
/*     */   
/*     */   private float holdingItemAnimationTicks;
/*     */   private float holdingItemAnimationTicks0;
/*     */   private float dancingAnimationTicks;
/*     */   private float spinningAnimationTicks;
/*     */   private float spinningAnimationTicks0;
/*     */   
/*     */   public Allay(EntityType<? extends Allay> type, Level level) {
/* 146 */     super(type, level);
/* 147 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 20, true);
/* 148 */     setCanPickUpLoot(canPickUpLoot());
/*     */     
/* 150 */     this.vibrationUser = new VibrationUser();
/* 151 */     this.vibrationData = new VibrationSystem.Data();
/* 152 */     this.dynamicVibrationListener = new DynamicGameEventListener((GameEventListener)new VibrationSystem.Listener(this));
/* 153 */     this.dynamicJukeboxListener = new DynamicGameEventListener(new JukeboxListener(this.vibrationUser.getPositionSource(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Allay> brainProvider() {
/* 158 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 163 */     return AllayAi.makeBrain(brainProvider().makeBrain(input));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Allay> getBrain() {
/* 169 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 173 */     return Mob.createMobAttributes()
/* 174 */       .add(Attributes.MAX_HEALTH, 20.0D)
/* 175 */       .add(Attributes.FLYING_SPEED, 0.10000000149011612D)
/* 176 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/* 177 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 182 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level);
/* 183 */     flyingPathNavigation.setCanOpenDoors(false);
/* 184 */     flyingPathNavigation.setCanFloat(true);
/* 185 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/* 186 */     return (PathNavigation)flyingPathNavigation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 191 */     super.defineSynchedData(entityData);
/* 192 */     entityData.define(DATA_DANCING, false);
/* 193 */     entityData.define(DATA_CAN_DUPLICATE, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 input) {
/* 198 */     travelFlying(input, getSpeed());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 203 */     if (isLikedPlayer(source.getEntity())) {
/* 204 */       return false;
/*     */     }
/* 206 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/* 211 */     return (isLikedPlayer(other) || super.considersEntityAsAlly(other));
/*     */   }
/*     */   
/*     */   private boolean isLikedPlayer(Entity other) {
/* 215 */     if (other instanceof Player) { Player player = (Player)other;
/* 216 */       Optional<UUID> likedPlayer = getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 217 */       return (likedPlayer.isPresent() && player.getUUID().equals(likedPlayer.get())); }
/*     */     
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 234 */     return hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 239 */     return SoundEvents.ALLAY_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 244 */     return SoundEvents.ALLAY_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 249 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 254 */     ProfilerFiller profiler = Profiler.get();
/* 255 */     profiler.push("allayBrain");
/* 256 */     getBrain().tick(level, (LivingEntity)this);
/* 257 */     profiler.pop();
/*     */     
/* 259 */     profiler.push("allayActivityUpdate");
/* 260 */     AllayAi.updateActivity(this);
/* 261 */     profiler.pop();
/*     */     
/* 263 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 268 */     super.aiStep();
/*     */     
/* 270 */     if (!level().isClientSide() && isAlive() && this.tickCount % 10 == 0) {
/* 271 */       heal(1.0F);
/*     */     }
/*     */     
/* 274 */     if (isDancing() && shouldStopDancing() && this.tickCount % 20 == 0) {
/* 275 */       setDancing(false);
/* 276 */       this.jukeboxPos = null;
/*     */     } 
/* 278 */     updateDuplicationCooldown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 283 */     super.tick();
/*     */     
/* 285 */     if (level().isClientSide()) {
/* 286 */       this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
/* 287 */       if (hasItemInHand()) {
/* 288 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
/*     */       } else {
/* 290 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
/*     */       } 
/*     */       
/* 293 */       if (isDancing()) {
/* 294 */         this.dancingAnimationTicks++;
/* 295 */         this.spinningAnimationTicks0 = this.spinningAnimationTicks;
/* 296 */         if (isSpinning()) {
/* 297 */           this.spinningAnimationTicks++;
/*     */         } else {
/* 299 */           this.spinningAnimationTicks--;
/*     */         } 
/* 301 */         this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
/*     */       } else {
/* 303 */         this.dancingAnimationTicks = 0.0F;
/* 304 */         this.spinningAnimationTicks = 0.0F;
/* 305 */         this.spinningAnimationTicks0 = 0.0F;
/*     */       } 
/*     */     } else {
/* 308 */       VibrationSystem.Ticker.tick(level(), this.vibrationData, this.vibrationUser);
/* 309 */       if (isPanicking()) {
/* 310 */         setDancing(false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPickUpLoot() {
/* 317 */     return (!isOnPickupCooldown() && hasItemInHand());
/*     */   }
/*     */   
/*     */   public boolean hasItemInHand() {
/* 321 */     return !getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 326 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isOnPickupCooldown() {
/* 330 */     return getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 335 */     ItemStack interactionItem = player.getItemInHand(hand);
/* 336 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/*     */     
/* 338 */     if (isDancing() && interactionItem.is(ItemTags.DUPLICATES_ALLAYS) && canDuplicate()) {
/* 339 */       duplicateAllay();
/* 340 */       level().broadcastEntityEvent((Entity)this, (byte)18);
/* 341 */       level().playSound((Entity)player, (Entity)this, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 342 */       removeInteractionItem(player, interactionItem);
/* 343 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 346 */     if (itemInHand.isEmpty() && !interactionItem.isEmpty()) {
/* 347 */       ItemStack itemToGive = interactionItem.copyWithCount(1);
/* 348 */       setItemInHand(InteractionHand.MAIN_HAND, itemToGive);
/* 349 */       removeInteractionItem(player, interactionItem);
/* 350 */       level().playSound((Entity)player, (Entity)this, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 351 */       getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
/*     */       
/* 353 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 356 */     if (!itemInHand.isEmpty() && hand == InteractionHand.MAIN_HAND && interactionItem.isEmpty()) {
/* 357 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 358 */       level().playSound((Entity)player, (Entity)this, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 359 */       swing(InteractionHand.MAIN_HAND);
/* 360 */       for (ItemStack itemStack : (Iterable<ItemStack>)getInventory().removeAllItems()) {
/* 361 */         BehaviorUtils.throwItem((LivingEntity)this, itemStack, position());
/*     */       }
/* 363 */       getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
/* 364 */       player.addItem(itemInHand);
/*     */       
/* 366 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 369 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void setJukeboxPlaying(BlockPos jukebox, boolean isPlaying) {
/* 373 */     if (isPlaying) {
/* 374 */       if (!isDancing()) {
/* 375 */         this.jukeboxPos = jukebox;
/* 376 */         setDancing(true);
/*     */       } 
/* 378 */     } else if (jukebox.equals(this.jukeboxPos) || this.jukeboxPos == null) {
/* 379 */       this.jukeboxPos = null;
/* 380 */       setDancing(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 386 */     return this.inventory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3i getPickupReach() {
/* 391 */     return ITEM_PICKUP_REACH;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 396 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/* 397 */     return (!itemInHand.isEmpty() && (Boolean)
/* 398 */       level.getGameRules().get(GameRules.MOB_GRIEFING) && 
/* 399 */       this.inventory.canAddItem(itemStack) && 
/* 400 */       allayConsidersItemEqual(itemInHand, itemStack));
/*     */   }
/*     */   
/*     */   private boolean allayConsidersItemEqual(ItemStack item1, ItemStack item2) {
/* 404 */     return (ItemStack.isSameItem(item1, item2) && !hasNonMatchingPotion(item1, item2));
/*     */   }
/*     */   
/*     */   private boolean hasNonMatchingPotion(ItemStack itemInHand, ItemStack pickupItem) {
/* 408 */     PotionContents potionInHand = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/* 409 */     PotionContents potionInPickupItem = (PotionContents)pickupItem.get(DataComponents.POTION_CONTENTS);
/* 410 */     return !java.util.Objects.equals(potionInHand, potionInPickupItem);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 415 */     InventoryCarrier.pickUpItem(level, (Mob)this, this, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlapping() {
/* 420 */     return !onGround();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
/* 425 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 426 */       action.accept(this.dynamicVibrationListener, serverLevel);
/* 427 */       action.accept(this.dynamicJukeboxListener, serverLevel); }
/*     */   
/*     */   }
/*     */   
/*     */   public boolean isDancing() {
/* 432 */     return (Boolean)this.entityData.get(DATA_DANCING);
/*     */   }
/*     */   
/*     */   public void setDancing(boolean isDancing) {
/* 436 */     if (level().isClientSide() || !isEffectiveAi() || (isDancing && isPanicking())) {
/*     */       return;
/*     */     }
/* 439 */     this.entityData.set(DATA_DANCING, isDancing);
/*     */   }
/*     */   
/*     */   private boolean shouldStopDancing() {
/* 443 */     return (this.jukeboxPos == null || 
/* 444 */       !this.jukeboxPos.closerToCenterThan((Position)position(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()) || 
/* 445 */       !level().getBlockState(this.jukeboxPos).is(Blocks.JUKEBOX));
/*     */   }
/*     */   
/*     */   public float getHoldingItemAnimationProgress(float a) {
/* 449 */     return Mth.lerp(a, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F;
/*     */   }
/*     */   
/*     */   public boolean isSpinning() {
/* 453 */     float spinningProgress = this.dancingAnimationTicks % 55.0F;
/* 454 */     return (spinningProgress < 15.0F);
/*     */   }
/*     */   
/*     */   public float getSpinningProgress(float a) {
/* 458 */     return Mth.lerp(a, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equipmentHasChanged(ItemStack previous, ItemStack current) {
/* 463 */     return !allayConsidersItemEqual(previous, current);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/* 468 */     super.dropEquipment(level);
/* 469 */     this.inventory.removeAllItems().forEach(stack -> spawnAtLocation(level, level));
/*     */ 
/*     */ 
/*     */     
/* 473 */     ItemStack itemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/* 474 */     if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
/* 475 */       spawnAtLocation(level, itemStack);
/* 476 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 482 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 487 */     super.addAdditionalSaveData(output);
/*     */     
/* 489 */     writeInventoryToTag(output);
/*     */     
/* 491 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */     
/* 493 */     output.putLong("DuplicationCooldown", this.duplicationCooldown);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 498 */     super.readAdditionalSaveData(input);
/*     */     
/* 500 */     readInventoryFromTag(input);
/*     */     
/* 502 */     this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */     
/* 504 */     setDuplicationCooldown(input.getIntOr("DuplicationCooldown", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldStayCloseToLeashHolder() {
/* 509 */     return false;
/*     */   }
/*     */   
/*     */   private void updateDuplicationCooldown() {
/* 513 */     if (!level().isClientSide() && this.duplicationCooldown > 0L) {
/* 514 */       setDuplicationCooldown(this.duplicationCooldown - 1L);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setDuplicationCooldown(long duplicationCooldown) {
/* 519 */     this.duplicationCooldown = duplicationCooldown;
/* 520 */     this.entityData.set(DATA_CAN_DUPLICATE, (duplicationCooldown == 0L));
/*     */   }
/*     */   
/*     */   private void duplicateAllay() {
/* 524 */     Allay allay = (Allay)EntityType.ALLAY.create(level(), EntitySpawnReason.BREEDING);
/* 525 */     if (allay != null) {
/* 526 */       allay.snapTo(position());
/* 527 */       allay.setPersistenceRequired();
/* 528 */       allay.resetDuplicationCooldown();
/* 529 */       resetDuplicationCooldown();
/* 530 */       level().addFreshEntity((Entity)allay);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetDuplicationCooldown() {
/* 535 */     setDuplicationCooldown(6000L);
/*     */   }
/*     */   
/*     */   private boolean canDuplicate() {
/* 539 */     return (Boolean)this.entityData.get(DATA_CAN_DUPLICATE);
/*     */   }
/*     */   
/*     */   private void removeInteractionItem(Player player, ItemStack interactionItem) {
/* 543 */     interactionItem.consume(1, (LivingEntity)player);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 548 */     return new Vec3(0.0D, getEyeHeight() * 0.6D, getBbWidth() * 0.1D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 553 */     if (id == 18) {
/* 554 */       for (int i = 0; i < 3; i++) {
/* 555 */         spawnHeartParticle();
/*     */       }
/*     */     } else {
/* 558 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnHeartParticle() {
/* 563 */     double xd = this.random.nextGaussian() * 0.02D;
/* 564 */     double yd = this.random.nextGaussian() * 0.02D;
/* 565 */     double zd = this.random.nextGaussian() * 0.02D;
/* 566 */     level().addParticle((ParticleOptions)ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xd, yd, zd);
/*     */   }
/*     */ 
/*     */   
/*     */   public VibrationSystem.Data getVibrationData() {
/* 571 */     return this.vibrationData;
/*     */   }
/*     */ 
/*     */   
/*     */   public VibrationSystem.User getVibrationUser() {
/* 576 */     return this.vibrationUser;
/*     */   }
/*     */   
/*     */   private class JukeboxListener implements GameEventListener {
/*     */     private final PositionSource listenerSource;
/*     */     private final int listenerRadius;
/*     */     
/*     */     public JukeboxListener(PositionSource listenerSource, int listenerRadius) {
/* 584 */       this.listenerSource = listenerSource;
/* 585 */       this.listenerRadius = listenerRadius;
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getListenerSource() {
/* 590 */       return this.listenerSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/* 595 */       return this.listenerRadius;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 600 */       if (event.is((Holder)GameEvent.JUKEBOX_PLAY)) {
/* 601 */         Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), true);
/* 602 */         return true;
/*     */       } 
/*     */       
/* 605 */       if (event.is((Holder)GameEvent.JUKEBOX_STOP_PLAY)) {
/* 606 */         Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), false);
/* 607 */         return true;
/*     */       } 
/*     */       
/* 610 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class VibrationUser
/*     */     implements VibrationSystem.User {
/*     */     private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
/* 617 */     private final PositionSource positionSource = (PositionSource)new EntityPositionSource((Entity)Allay.this, Allay.this.getEyeHeight());
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/* 621 */       return 16;
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getPositionSource() {
/* 626 */       return this.positionSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 631 */       if (Allay.this.isNoAi()) {
/* 632 */         return false;
/*     */       }
/*     */       
/* 635 */       Optional<GlobalPos> maybeGlobalPos = Allay.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 636 */       if (maybeGlobalPos.isEmpty()) {
/* 637 */         return true;
/*     */       }
/* 639 */       GlobalPos globalPos = maybeGlobalPos.get();
/* 640 */       return (globalPos.isCloseEnough(level.dimension(), Allay.this.blockPosition(), 1024) && globalPos.pos().equals(pos));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 645 */       if (event.is((Holder)GameEvent.NOTE_BLOCK_PLAY)) {
/* 646 */         AllayAi.hearNoteblock((LivingEntity)Allay.this, new BlockPos((Vec3i)pos));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public TagKey<GameEvent> getListenableEvents() {
/* 652 */       return GameEventTags.ALLAY_CAN_LISTEN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/allay/Allay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */