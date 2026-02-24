/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Armadillo
/*     */   extends Animal
/*     */ {
/*     */   public static final float BABY_SCALE = 0.6F;
/*     */   public static final float MAX_HEAD_ROTATION_EXTENT = 32.5F;
/*     */   public static final int SCARE_CHECK_INTERVAL = 80;
/*     */   private static final double SCARE_DISTANCE_HORIZONTAL = 7.0D;
/*     */   private static final double SCARE_DISTANCE_VERTICAL = 2.0D;
/*  63 */   private static final EntityDataAccessor<ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(Armadillo.class, EntityDataSerializers.ARMADILLO_STATE);
/*  64 */   private long inStateTicks = 0L;
/*  65 */   public final AnimationState rollOutAnimationState = new AnimationState();
/*  66 */   public final AnimationState rollUpAnimationState = new AnimationState();
/*  67 */   public final AnimationState peekAnimationState = new AnimationState();
/*     */   
/*     */   private int scuteTime;
/*     */   
/*     */   private boolean peekReceivedClient = false;
/*     */   
/*     */   public Armadillo(EntityType<? extends Animal> type, Level level) {
/*  74 */     super(type, level);
/*  75 */     getNavigation().setCanFloat(true);
/*  76 */     this.scuteTime = pickNextScuteDropTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  81 */     return (AgeableMob)EntityType.ARMADILLO.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  85 */     return Animal.createAnimalAttributes()
/*  86 */       .add(Attributes.MAX_HEALTH, 12.0D)
/*  87 */       .add(Attributes.MOVEMENT_SPEED, 0.14D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  92 */     super.defineSynchedData(entityData);
/*  93 */     entityData.define(ARMADILLO_STATE, ArmadilloState.IDLE);
/*     */   }
/*     */   
/*     */   public boolean isScared() {
/*  97 */     return (this.entityData.get(ARMADILLO_STATE) != ArmadilloState.IDLE);
/*     */   }
/*     */   
/*     */   public boolean shouldHideInShell() {
/* 101 */     return getState().shouldHideInShell(this.inStateTicks);
/*     */   }
/*     */   
/*     */   public boolean shouldSwitchToScaredState() {
/* 105 */     return (getState() == ArmadilloState.ROLLING && this.inStateTicks > ArmadilloState.ROLLING.animationDuration());
/*     */   }
/*     */   
/*     */   public ArmadilloState getState() {
/* 109 */     return (ArmadilloState)this.entityData.get(ARMADILLO_STATE);
/*     */   }
/*     */   
/*     */   public void switchToState(ArmadilloState state) {
/* 113 */     this.entityData.set(ARMADILLO_STATE, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 118 */     if (ARMADILLO_STATE.equals(accessor)) {
/* 119 */       this.inStateTicks = 0L;
/*     */     }
/* 121 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Armadillo> brainProvider() {
/* 126 */     return ArmadilloAi.brainProvider();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 131 */     return ArmadilloAi.makeBrain(brainProvider().makeBrain(input));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 136 */     ProfilerFiller profiler = Profiler.get();
/* 137 */     profiler.push("armadilloBrain");
/* 138 */     this.brain.tick(level, (LivingEntity)this);
/* 139 */     profiler.pop();
/*     */     
/* 141 */     profiler.push("armadilloActivityUpdate");
/* 142 */     ArmadilloAi.updateActivity(this);
/* 143 */     profiler.pop();
/*     */     
/* 145 */     if (isAlive() && --this.scuteTime <= 0 && shouldDropLoot(level)) {
/* 146 */       if (dropFromGiftLootTable(level, BuiltInLootTables.ARMADILLO_SHED, this::spawnAtLocation)) {
/* 147 */         playSound(SoundEvents.ARMADILLO_SCUTE_DROP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 148 */         gameEvent((Holder)GameEvent.ENTITY_PLACE);
/*     */       } 
/* 150 */       this.scuteTime = pickNextScuteDropTime();
/*     */     } 
/*     */     
/* 153 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private int pickNextScuteDropTime() {
/* 157 */     return this.random.nextInt(20 * TimeUtil.SECONDS_PER_MINUTE * 5) + 20 * TimeUtil.SECONDS_PER_MINUTE * 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 162 */     super.tick();
/* 163 */     if (level().isClientSide()) {
/* 164 */       setupAnimationStates();
/*     */     }
/* 166 */     if (isScared()) {
/* 167 */       clampHeadRotationToBody();
/*     */     }
/*     */     
/* 170 */     this.inStateTicks++;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAgeScale() {
/* 175 */     return isBaby() ? 0.6F : 1.0F;
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 179 */     switch (getState().ordinal()) {
/*     */       case 0:
/* 181 */         this.rollOutAnimationState.stop();
/* 182 */         this.rollUpAnimationState.stop();
/* 183 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 3:
/* 186 */         this.rollOutAnimationState.startIfStopped(this.tickCount);
/* 187 */         this.rollUpAnimationState.stop();
/* 188 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 1:
/* 191 */         this.rollOutAnimationState.stop();
/* 192 */         this.rollUpAnimationState.startIfStopped(this.tickCount);
/* 193 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 2:
/* 196 */         this.rollOutAnimationState.stop();
/* 197 */         this.rollUpAnimationState.stop();
/* 198 */         if (this.peekReceivedClient) {
/* 199 */           this.peekAnimationState.stop();
/* 200 */           this.peekReceivedClient = false;
/*     */         } 
/*     */         
/* 203 */         if (this.inStateTicks == 0L) {
/* 204 */           this.peekAnimationState.start(this.tickCount);
/* 205 */           this.peekAnimationState.fastForward(ArmadilloState.SCARED.animationDuration(), 1.0F); break;
/*     */         } 
/* 207 */         this.peekAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 215 */     if (id == 64 && level().isClientSide()) {
/*     */       
/* 217 */       this.peekReceivedClient = true;
/* 218 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ARMADILLO_PEEK, getSoundSource(), 1.0F, 1.0F, false);
/*     */     } else {
/* 220 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 226 */     return itemStack.is(ItemTags.ARMADILLO_FOOD);
/*     */   }
/*     */   
/*     */   public static boolean checkArmadilloSpawnRules(EntityType<Armadillo> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 230 */     return (level.getBlockState(pos.below()).is(BlockTags.ARMADILLO_SPAWNABLE_ON) && 
/* 231 */       isBrightEnoughToSpawn((BlockAndTintGetter)level, pos));
/*     */   }
/*     */   
/*     */   public boolean isScaredBy(LivingEntity livingEntity) {
/* 235 */     if (!getBoundingBox().inflate(7.0D, 2.0D, 7.0D).intersects(livingEntity.getBoundingBox())) {
/* 236 */       return false;
/*     */     }
/* 238 */     if (livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
/* 239 */       return true;
/*     */     }
/* 241 */     if (getLastHurtByMob() == livingEntity) {
/* 242 */       return true;
/*     */     }
/* 244 */     if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/* 245 */       if (player.isSpectator()) {
/* 246 */         return false;
/*     */       }
/* 248 */       return (player.isSprinting() || player.isPassenger()); }
/*     */     
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 255 */     super.addAdditionalSaveData(output);
/* 256 */     output.store("state", ArmadilloState.CODEC, getState());
/* 257 */     output.putInt("scute_time", this.scuteTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 262 */     super.readAdditionalSaveData(input);
/* 263 */     switchToState(input.read("state", ArmadilloState.CODEC).orElse(ArmadilloState.IDLE));
/*     */     
/* 265 */     input.getInt("scute_time").ifPresent(time -> this.scuteTime = time);
/*     */   }
/*     */   
/*     */   public void rollUp() {
/* 269 */     if (isScared()) {
/*     */       return;
/*     */     }
/* 272 */     stopInPlace();
/* 273 */     resetLove();
/* 274 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 275 */     makeSound(SoundEvents.ARMADILLO_ROLL);
/* 276 */     switchToState(ArmadilloState.ROLLING);
/*     */   }
/*     */   
/*     */   public void rollOut() {
/* 280 */     if (!isScared()) {
/*     */       return;
/*     */     }
/* 283 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 284 */     makeSound(SoundEvents.ARMADILLO_UNROLL_FINISH);
/* 285 */     switchToState(ArmadilloState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 290 */     if (isScared()) {
/* 291 */       damage = (damage - 1.0F) / 2.0F;
/*     */     }
/* 293 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 298 */     super.actuallyHurt(level, source, dmg);
/* 299 */     if (isNoAi() || isDeadOrDying()) {
/*     */       return;
/*     */     }
/*     */     
/* 303 */     if (source.getEntity() instanceof LivingEntity) {
/* 304 */       getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, true, 80L);
/* 305 */       if (canStayRolledUp()) {
/* 306 */         rollUp();
/*     */       }
/* 308 */     } else if (source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)) {
/* 309 */       rollOut();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 315 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 317 */     if (itemStack.is(Items.BRUSH) && brushOffScute((Entity)player, itemStack)) {
/* 318 */       itemStack.hurtAndBreak(16, (LivingEntity)player, hand.asEquipmentSlot());
/* 319 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 322 */     if (isScared()) {
/* 323 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 325 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean brushOffScute(Entity interactingEntity, ItemStack tool) {
/* 329 */     if (isBaby()) {
/* 330 */       return false;
/*     */     }
/* 332 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 333 */       dropFromEntityInteractLootTable(serverLevel, BuiltInLootTables.ARMADILLO_BRUSH, interactingEntity, tool, this::spawnAtLocation);
/* 334 */       playSound(SoundEvents.ARMADILLO_BRUSH);
/* 335 */       gameEvent((Holder)GameEvent.ENTITY_INTERACT); }
/*     */     
/* 337 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canStayRolledUp() {
/* 341 */     return (!isPanicking() && !isInLiquid() && !isLeashed() && !isPassenger() && !isVehicle());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 346 */     return (super.canFallInLove() && !isScared());
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 351 */     if (isScared()) {
/* 352 */       return null;
/*     */     }
/* 354 */     return SoundEvents.ARMADILLO_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playEatingSound() {
/* 359 */     makeSound(SoundEvents.ARMADILLO_EAT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 364 */     return SoundEvents.ARMADILLO_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 369 */     if (isScared()) {
/* 370 */       return SoundEvents.ARMADILLO_HURT_REDUCED;
/*     */     }
/* 372 */     return SoundEvents.ARMADILLO_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 377 */     playSound(SoundEvents.ARMADILLO_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 382 */     if (isScared()) {
/* 383 */       return 0;
/*     */     }
/* 385 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 390 */     return new BodyRotationControl((Mob)this)
/*     */       {
/*     */         public void clientTick() {
/* 393 */           if (!Armadillo.this.isScared())
/* 394 */             super.clientTick(); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public enum ArmadilloState
/*     */     implements StringRepresentable
/*     */   {
/* 402 */     IDLE("idle", false, 0, 0)
/*     */     {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 405 */         return false;
/*     */       }
/*     */     },
/* 408 */     ROLLING("rolling", true, 10, 1)
/*     */     {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 411 */         return (ticksInState > 5L);
/*     */       }
/*     */     },
/* 414 */     SCARED("scared", true, 50, 2)
/*     */     {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 417 */         return true;
/*     */       }
/*     */     },
/* 420 */     UNROLLING("unrolling", true, 30, 3)
/*     */     {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 423 */         return (ticksInState < 26L);
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 428 */     private static final Codec<ArmadilloState> CODEC = (Codec<ArmadilloState>)StringRepresentable.fromEnum(ArmadilloState::values);
/* 429 */     private static final IntFunction<ArmadilloState> BY_ID = ByIdMap.continuous(ArmadilloState::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 431 */     public static final StreamCodec<ByteBuf, ArmadilloState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArmadilloState::id);
/*     */     
/*     */     private final String name;
/*     */     private final boolean isThreatened;
/*     */     private final int animationDuration;
/*     */     private final int id;
/*     */     
/*     */     ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
/* 439 */       this.name = name;
/* 440 */       this.isThreatened = isThreatened;
/* 441 */       this.animationDuration = animationDuration;
/* 442 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 447 */       return this.name;
/*     */     }
/*     */     
/*     */     private int id() {
/* 451 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isThreatened() {
/* 457 */       return this.isThreatened;
/*     */     }
/*     */     
/*     */     public int animationDuration() {
/* 461 */       return this.animationDuration;
/*     */     }
/*     */     
/*     */     public abstract boolean shouldHideInShell(long param1Long);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return (ticksInState > 5L);
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return (ticksInState < 26L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/armadillo/Armadillo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */