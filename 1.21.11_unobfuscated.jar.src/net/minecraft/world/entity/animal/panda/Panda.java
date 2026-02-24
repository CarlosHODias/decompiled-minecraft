/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.function.IntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.ByIdMap;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityAttachment;
/*      */ import net.minecraft.world.entity.EntityAttachments;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Panda extends Animal {
/*   75 */   private static final EntityDataAccessor<Integer> UNHAPPY_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   76 */   private static final EntityDataAccessor<Integer> SNEEZE_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   77 */   private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   78 */   private static final EntityDataAccessor<Byte> MAIN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*   79 */   private static final EntityDataAccessor<Byte> HIDDEN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   81 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   83 */   private static final TargetingConditions BREED_TARGETING = TargetingConditions.forNonCombat().range(8.0D);
/*      */   
/*   85 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.PANDA.getDimensions().scale(0.5F)
/*   86 */     .withAttachments(EntityAttachments.builder()
/*   87 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.40625F, 0.0F));
/*      */   
/*      */   private static final int FLAG_SNEEZE = 2;
/*      */   
/*      */   private static final int FLAG_ROLL = 4;
/*      */   
/*      */   private static final int FLAG_SIT = 8;
/*      */   
/*      */   private static final int FLAG_ON_BACK = 16;
/*      */   
/*      */   private static final int EAT_TICK_INTERVAL = 5;
/*      */   
/*      */   public static final int TOTAL_ROLL_STEPS = 32;
/*      */   
/*      */   private static final int TOTAL_UNHAPPY_TIME = 32;
/*      */   private boolean gotBamboo;
/*      */   private boolean didBite;
/*      */   public int rollCounter;
/*      */   private Vec3 rollDelta;
/*      */   private float sitAmount;
/*      */   private float sitAmountO;
/*      */   private float onBackAmount;
/*      */   private float onBackAmountO;
/*      */   private float rollAmount;
/*      */   private float rollAmountO;
/*      */   private PandaLookAtPlayerGoal lookAtPlayerGoal;
/*      */   
/*      */   public Panda(EntityType<? extends Panda> type, Level level) {
/*  115 */     super(type, level);
/*      */     
/*  117 */     this.moveControl = new PandaMoveControl(this);
/*      */     
/*  119 */     if (!isBaby()) {
/*  120 */       setCanPickUpLoot(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/*  126 */     return (slot == EquipmentSlot.MAINHAND && canPickUpLoot());
/*      */   }
/*      */   
/*      */   public int getUnhappyCounter() {
/*  130 */     return (Integer)this.entityData.get(UNHAPPY_COUNTER);
/*      */   }
/*      */   
/*      */   public void setUnhappyCounter(int value) {
/*  134 */     this.entityData.set(UNHAPPY_COUNTER, value);
/*      */   }
/*      */   
/*      */   public boolean isSneezing() {
/*  138 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   public boolean isSitting() {
/*  142 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public void sit(boolean value) {
/*  146 */     setFlag(8, value);
/*      */   }
/*      */   
/*      */   public boolean isOnBack() {
/*  150 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public void setOnBack(boolean value) {
/*  154 */     setFlag(16, value);
/*      */   }
/*      */   
/*      */   public boolean isEating() {
/*  158 */     return ((Integer)this.entityData.get(EAT_COUNTER) > 0);
/*      */   }
/*      */   
/*      */   public void eat(boolean value) {
/*  162 */     this.entityData.set(EAT_COUNTER, value ? 1 : 0);
/*      */   }
/*      */   
/*      */   private int getEatCounter() {
/*  166 */     return (Integer)this.entityData.get(EAT_COUNTER);
/*      */   }
/*      */   
/*      */   private void setEatCounter(int value) {
/*  170 */     this.entityData.set(EAT_COUNTER, value);
/*      */   }
/*      */   
/*      */   public void sneeze(boolean value) {
/*  174 */     setFlag(2, value);
/*      */     
/*  176 */     if (!value) {
/*  177 */       setSneezeCounter(0);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getSneezeCounter() {
/*  182 */     return (Integer)this.entityData.get(SNEEZE_COUNTER);
/*      */   }
/*      */   
/*      */   public void setSneezeCounter(int value) {
/*  186 */     this.entityData.set(SNEEZE_COUNTER, value);
/*      */   }
/*      */   
/*      */   public Gene getMainGene() {
/*  190 */     return Gene.byId((Byte)this.entityData.get(MAIN_GENE_ID));
/*      */   }
/*      */   
/*      */   public void setMainGene(Gene gene) {
/*  194 */     if (gene.getId() > 6) {
/*  195 */       gene = Gene.getRandom(this.random);
/*      */     }
/*      */     
/*  198 */     this.entityData.set(MAIN_GENE_ID, (byte)gene.getId());
/*      */   }
/*      */   
/*      */   public Gene getHiddenGene() {
/*  202 */     return Gene.byId((Byte)this.entityData.get(HIDDEN_GENE_ID));
/*      */   }
/*      */   
/*      */   public void setHiddenGene(Gene gene) {
/*  206 */     if (gene.getId() > 6) {
/*  207 */       gene = Gene.getRandom(this.random);
/*      */     }
/*      */     
/*  210 */     this.entityData.set(HIDDEN_GENE_ID, (byte)gene.getId());
/*      */   }
/*      */   
/*      */   public boolean isRolling() {
/*  214 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void roll(boolean value) {
/*  218 */     setFlag(4, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  223 */     super.defineSynchedData(entityData);
/*  224 */     entityData.define(UNHAPPY_COUNTER, 0);
/*  225 */     entityData.define(SNEEZE_COUNTER, 0);
/*  226 */     entityData.define(MAIN_GENE_ID, (byte)0);
/*  227 */     entityData.define(HIDDEN_GENE_ID, (byte)0);
/*  228 */     entityData.define(DATA_ID_FLAGS, (byte)0);
/*  229 */     entityData.define(EAT_COUNTER, 0);
/*      */   }
/*      */   
/*      */   private boolean getFlag(int flag) {
/*  233 */     return (((Byte)this.entityData.get(DATA_ID_FLAGS) & flag) != 0);
/*      */   }
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  237 */     byte current = (Byte)this.entityData.get(DATA_ID_FLAGS);
/*  238 */     if (value) {
/*  239 */       this.entityData.set(DATA_ID_FLAGS, (byte)(current | flag));
/*      */     } else {
/*  241 */       this.entityData.set(DATA_ID_FLAGS, (byte)(current & (flag ^ 0xFFFFFFFF)));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  247 */     super.addAdditionalSaveData(output);
/*      */     
/*  249 */     output.store("MainGene", Gene.CODEC, getMainGene());
/*  250 */     output.store("HiddenGene", Gene.CODEC, getHiddenGene());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  255 */     super.readAdditionalSaveData(input);
/*      */     
/*  257 */     setMainGene(input.read("MainGene", Gene.CODEC).orElse(Gene.NORMAL));
/*  258 */     setHiddenGene(input.read("HiddenGene", Gene.CODEC).orElse(Gene.NORMAL));
/*      */   }
/*      */ 
/*      */   
/*      */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  263 */     Panda baby = (Panda)EntityType.PANDA.create((Level)level, EntitySpawnReason.BREEDING);
/*  264 */     if (baby != null) {
/*  265 */       if (partner instanceof Panda) { Panda partnerPanda = (Panda)partner;
/*  266 */         baby.setGeneFromParents(this, partnerPanda); }
/*      */ 
/*      */       
/*  269 */       baby.setAttributes();
/*      */     } 
/*      */     
/*  272 */     return (AgeableMob)baby;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  277 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  278 */     this.goalSelector.addGoal(2, (Goal)new PandaPanicGoal(this, 2.0D));
/*  279 */     this.goalSelector.addGoal(2, (Goal)new PandaBreedGoal(this, 1.0D));
/*  280 */     this.goalSelector.addGoal(3, (Goal)new PandaAttackGoal(this, 1.2000000476837158D, true));
/*  281 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, i -> i.is(ItemTags.PANDA_FOOD), false));
/*  282 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Player.class, 8.0F, 2.0D, 2.0D));
/*  283 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Monster.class, 4.0F, 2.0D, 2.0D));
/*  284 */     this.goalSelector.addGoal(7, new PandaSitGoal());
/*  285 */     this.goalSelector.addGoal(8, new PandaLieOnBackGoal(this));
/*  286 */     this.goalSelector.addGoal(8, new PandaSneezeGoal(this));
/*  287 */     this.lookAtPlayerGoal = new PandaLookAtPlayerGoal(this, (Class)Player.class, 6.0F);
/*  288 */     this.goalSelector.addGoal(9, (Goal)this.lookAtPlayerGoal);
/*  289 */     this.goalSelector.addGoal(10, (Goal)new RandomLookAroundGoal((Mob)this));
/*  290 */     this.goalSelector.addGoal(12, new PandaRollGoal(this));
/*  291 */     this.goalSelector.addGoal(13, (Goal)new FollowParentGoal(this, 1.25D));
/*  292 */     this.goalSelector.addGoal(14, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*      */     
/*  294 */     this.targetSelector.addGoal(1, (Goal)new PandaHurtByTargetGoal(this, new Class<?>[0]).setAlertOthers(new Class<?>[0]));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  298 */     return Animal.createAnimalAttributes()
/*  299 */       .add(Attributes.MOVEMENT_SPEED, 0.15000000596046448D)
/*  300 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*      */   }
/*      */   
/*      */   public enum Gene
/*      */     implements StringRepresentable
/*      */   {
/*  306 */     NORMAL(0, "normal", false),
/*  307 */     LAZY(1, "lazy", false),
/*  308 */     WORRIED(2, "worried", false),
/*  309 */     PLAYFUL(3, "playful", false),
/*  310 */     BROWN(4, "brown", true),
/*  311 */     WEAK(5, "weak", true),
/*  312 */     AGGRESSIVE(6, "aggressive", false);
/*      */     
/*  314 */     public static final Codec<Gene> CODEC = (Codec<Gene>)StringRepresentable.fromEnum(Gene::values);
/*      */     
/*  316 */     private static final IntFunction<Gene> BY_ID = ByIdMap.continuous(Gene::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*      */     
/*      */     private static final int MAX_GENE = 6;
/*      */     private final int id;
/*      */     private final String name;
/*      */     private final boolean isRecessive;
/*      */     
/*      */     Gene(int id, String name, boolean isRecessive) {
/*  324 */       this.id = id;
/*  325 */       this.name = name;
/*  326 */       this.isRecessive = isRecessive;
/*      */     }
/*      */     
/*      */     public int getId() {
/*  330 */       return this.id;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getSerializedName() {
/*  335 */       return this.name;
/*      */     }
/*      */     
/*      */     public boolean isRecessive() {
/*  339 */       return this.isRecessive;
/*      */     }
/*      */     
/*      */     private static Gene getVariantFromGenes(Gene mainGene, Gene hiddenGene) {
/*  343 */       if (mainGene.isRecessive()) {
/*  344 */         if (mainGene == hiddenGene) {
/*  345 */           return mainGene;
/*      */         }
/*  347 */         return NORMAL;
/*      */       } 
/*      */ 
/*      */       
/*  351 */       return mainGene;
/*      */     }
/*      */     
/*      */     public static Gene byId(int id) {
/*  355 */       return BY_ID.apply(id);
/*      */     }
/*      */     
/*      */     public static Gene getRandom(RandomSource random) {
/*  359 */       int nextInt = random.nextInt(16);
/*  360 */       if (nextInt == 0) {
/*  361 */         return LAZY;
/*      */       }
/*  363 */       if (nextInt == 1) {
/*  364 */         return WORRIED;
/*      */       }
/*  366 */       if (nextInt == 2) {
/*  367 */         return PLAYFUL;
/*      */       }
/*  369 */       if (nextInt == 4) {
/*  370 */         return AGGRESSIVE;
/*      */       }
/*  372 */       if (nextInt < 9) {
/*  373 */         return WEAK;
/*      */       }
/*  375 */       if (nextInt < 11) {
/*  376 */         return BROWN;
/*      */       }
/*      */       
/*  379 */       return NORMAL;
/*      */     }
/*      */   }
/*      */   
/*      */   public Gene getVariant() {
/*  384 */     return Gene.getVariantFromGenes(getMainGene(), getHiddenGene());
/*      */   }
/*      */   
/*      */   public boolean isLazy() {
/*  388 */     return (getVariant() == Gene.LAZY);
/*      */   }
/*      */   
/*      */   public boolean isWorried() {
/*  392 */     return (getVariant() == Gene.WORRIED);
/*      */   }
/*      */   
/*      */   public boolean isPlayful() {
/*  396 */     return (getVariant() == Gene.PLAYFUL);
/*      */   }
/*      */   
/*      */   public boolean isBrown() {
/*  400 */     return (getVariant() == Gene.BROWN);
/*      */   }
/*      */   
/*      */   public boolean isWeak() {
/*  404 */     return (getVariant() == Gene.WEAK);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAggressive() {
/*  409 */     return (getVariant() == Gene.AGGRESSIVE);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeLeashed() {
/*  414 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  419 */     if (!isAggressive()) {
/*  420 */       this.didBite = true;
/*      */     }
/*  422 */     return super.doHurtTarget(level, target);
/*      */   }
/*      */ 
/*      */   
/*      */   public void playAttackSound() {
/*  427 */     playSound(SoundEvents.PANDA_BITE, 1.0F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  432 */     super.tick();
/*      */     
/*  434 */     if (isWorried()) {
/*  435 */       if (level().isThundering() && !isInWater()) {
/*  436 */         sit(true);
/*  437 */         eat(false);
/*  438 */       } else if (!isEating()) {
/*  439 */         sit(false);
/*      */       } 
/*      */     }
/*      */     
/*  443 */     LivingEntity target = getTarget();
/*  444 */     if (target == null) {
/*  445 */       this.gotBamboo = false;
/*  446 */       this.didBite = false;
/*      */     } 
/*      */     
/*  449 */     if (getUnhappyCounter() > 0) {
/*  450 */       if (target != null) {
/*  451 */         lookAt((Entity)target, 90.0F, 90.0F);
/*      */       }
/*      */       
/*  454 */       if (getUnhappyCounter() == 29 || getUnhappyCounter() == 14) {
/*  455 */         playSound(SoundEvents.PANDA_CANT_BREED, 1.0F, 1.0F);
/*      */       }
/*      */       
/*  458 */       setUnhappyCounter(getUnhappyCounter() - 1);
/*      */     } 
/*      */     
/*  461 */     if (isSneezing()) {
/*  462 */       setSneezeCounter(getSneezeCounter() + 1);
/*  463 */       if (getSneezeCounter() > 20) {
/*  464 */         sneeze(false);
/*  465 */         afterSneeze();
/*  466 */       } else if (getSneezeCounter() == 1) {
/*  467 */         playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
/*      */       } 
/*      */     } 
/*      */     
/*  471 */     if (isRolling()) {
/*  472 */       handleRoll();
/*      */     } else {
/*  474 */       this.rollCounter = 0;
/*      */     } 
/*      */     
/*  477 */     if (isSitting()) {
/*  478 */       setXRot(0.0F);
/*      */     }
/*      */     
/*  481 */     updateSitAmount();
/*  482 */     handleEating();
/*  483 */     updateOnBackAnimation();
/*  484 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   public boolean isScared() {
/*  488 */     return (isWorried() && level().isThundering());
/*      */   }
/*      */   
/*      */   private void handleEating() {
/*  492 */     if (!isEating() && isSitting() && !isScared() && !getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
/*  493 */       eat(true);
/*  494 */     } else if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !isSitting()) {
/*  495 */       eat(false);
/*      */     } 
/*      */     
/*  498 */     if (isEating()) {
/*  499 */       addEatingParticles();
/*      */       
/*  501 */       if (!level().isClientSide() && getEatCounter() > 80 && this.random.nextInt(20) == 1) {
/*  502 */         if (getEatCounter() > 100 && getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.PANDA_EATS_FROM_GROUND)) {
/*      */           
/*  504 */           if (!level().isClientSide()) {
/*  505 */             setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*  506 */             gameEvent((Holder)GameEvent.EAT);
/*      */           } 
/*      */           
/*  509 */           sit(false);
/*      */         } 
/*  511 */         eat(false);
/*      */         
/*      */         return;
/*      */       } 
/*  515 */       setEatCounter(getEatCounter() + 1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addEatingParticles() {
/*  520 */     if (getEatCounter() % 5 == 0) {
/*  521 */       playSound(SoundEvents.PANDA_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*      */       
/*  523 */       for (int i = 0; i < 6; i++) {
/*  524 */         Vec3 d = new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, (this.random.nextFloat() - 0.5D) * 0.1D);
/*  525 */         d = d.xRot(-getXRot() * 0.017453292F);
/*  526 */         d = d.yRot(-getYRot() * 0.017453292F);
/*      */         
/*  528 */         double y1 = -this.random.nextFloat() * 0.6D - 0.3D;
/*  529 */         Vec3 p = new Vec3((this.random.nextFloat() - 0.5D) * 0.8D, y1, 1.0D + (this.random.nextFloat() - 0.5D) * 0.4D);
/*  530 */         p = p.yRot(-this.yBodyRot * 0.017453292F);
/*      */         
/*  532 */         p = p.add(getX(), getEyeY() + 1.0D, getZ());
/*  533 */         level().addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, getItemBySlot(EquipmentSlot.MAINHAND)), p.x, p.y, p.z, d.x, d.y + 0.05D, d.z);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateSitAmount() {
/*  539 */     this.sitAmountO = this.sitAmount;
/*  540 */     if (isSitting()) {
/*  541 */       this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
/*      */     } else {
/*  543 */       this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateOnBackAnimation() {
/*  548 */     this.onBackAmountO = this.onBackAmount;
/*  549 */     if (isOnBack()) {
/*  550 */       this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.15F);
/*      */     } else {
/*  552 */       this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateRollAmount() {
/*  557 */     this.rollAmountO = this.rollAmount;
/*  558 */     if (isRolling()) {
/*  559 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.15F);
/*      */     } else {
/*  561 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   public float getSitAmount(float a) {
/*  566 */     return Mth.lerp(a, this.sitAmountO, this.sitAmount);
/*      */   }
/*      */   
/*      */   public float getLieOnBackAmount(float a) {
/*  570 */     return Mth.lerp(a, this.onBackAmountO, this.onBackAmount);
/*      */   }
/*      */   
/*      */   public float getRollAmount(float a) {
/*  574 */     return Mth.lerp(a, this.rollAmountO, this.rollAmount);
/*      */   }
/*      */   
/*      */   private void handleRoll() {
/*  578 */     this.rollCounter++;
/*  579 */     if (this.rollCounter > 32) {
/*  580 */       roll(false);
/*      */       
/*      */       return;
/*      */     } 
/*  584 */     if (!level().isClientSide()) {
/*  585 */       Vec3 movement = getDeltaMovement();
/*  586 */       if (this.rollCounter == 1) {
/*  587 */         float angle = getYRot() * 0.017453292F;
/*  588 */         float multiplier = isBaby() ? 0.1F : 0.2F;
/*  589 */         this
/*      */ 
/*      */           
/*  592 */           .rollDelta = new Vec3(movement.x + (-Mth.sin(angle) * multiplier), 0.0D, movement.z + (Mth.cos(angle) * multiplier));
/*      */         
/*  594 */         setDeltaMovement(this.rollDelta.add(0.0D, 0.27D, 0.0D));
/*  595 */       } else if (this.rollCounter == 7.0F || this.rollCounter == 15.0F || this.rollCounter == 23.0F) {
/*  596 */         setDeltaMovement(0.0D, onGround() ? 0.27D : movement.y, 0.0D);
/*      */       } else {
/*  598 */         setDeltaMovement(this.rollDelta.x, movement.y, this.rollDelta.z);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void afterSneeze() {
/*  604 */     Vec3 movement = getDeltaMovement();
/*  605 */     Level level = level();
/*  606 */     level.addParticle((ParticleOptions)ParticleTypes.SNEEZE, getX() - (getBbWidth() + 1.0F) * 0.5D * Mth.sin((this.yBodyRot * 0.017453292F)), getEyeY() - 0.10000000149011612D, getZ() + (getBbWidth() + 1.0F) * 0.5D * Mth.cos((this.yBodyRot * 0.017453292F)), movement.x, 0.0D, movement.z);
/*  607 */     playSound(SoundEvents.PANDA_SNEEZE, 1.0F, 1.0F);
/*      */ 
/*      */     
/*  610 */     List<Panda> pandas = level.getEntitiesOfClass(Panda.class, getBoundingBox().inflate(10.0D));
/*  611 */     for (Panda panda : pandas) {
/*  612 */       if (!panda.isBaby() && panda.onGround() && !panda.isInWater() && panda.canPerformAction()) {
/*  613 */         panda.jumpFromGround();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  618 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1; if ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
/*  619 */         dropFromGiftLootTable(serverLevel, BuiltInLootTables.PANDA_SNEEZE, this::spawnAtLocation);
/*      */       } }
/*      */   
/*      */   }
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  625 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && canPickUpAndEat(entity)) {
/*  626 */       onItemPickup(entity);
/*  627 */       ItemStack itemStack = entity.getItem();
/*  628 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/*  629 */       setGuaranteedDrop(EquipmentSlot.MAINHAND);
/*  630 */       take((Entity)entity, itemStack.getCount());
/*  631 */       entity.discard();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  637 */     sit(false);
/*  638 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/*  643 */     RandomSource random = level.getRandom();
/*  644 */     setMainGene(Gene.getRandom(random));
/*  645 */     setHiddenGene(Gene.getRandom(random));
/*      */     
/*  647 */     setAttributes();
/*      */     
/*  649 */     if (groupData == null) {
/*  650 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.2F);
/*      */     }
/*      */     
/*  653 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*      */   }
/*      */   
/*      */   public void setGeneFromParents(Panda parent1, Panda parent2) {
/*  657 */     if (parent2 == null) {
/*  658 */       if (this.random.nextBoolean()) {
/*  659 */         setMainGene(parent1.getOneOfGenesRandomly());
/*  660 */         setHiddenGene(Gene.getRandom(this.random));
/*      */       } else {
/*  662 */         setMainGene(Gene.getRandom(this.random));
/*  663 */         setHiddenGene(parent1.getOneOfGenesRandomly());
/*      */       }
/*      */     
/*  666 */     } else if (this.random.nextBoolean()) {
/*  667 */       setMainGene(parent1.getOneOfGenesRandomly());
/*  668 */       setHiddenGene(parent2.getOneOfGenesRandomly());
/*      */     } else {
/*  670 */       setMainGene(parent2.getOneOfGenesRandomly());
/*  671 */       setHiddenGene(parent1.getOneOfGenesRandomly());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  676 */     if (this.random.nextInt(32) == 0) {
/*  677 */       setMainGene(Gene.getRandom(this.random));
/*      */     }
/*      */     
/*  680 */     if (this.random.nextInt(32) == 0) {
/*  681 */       setHiddenGene(Gene.getRandom(this.random));
/*      */     }
/*      */   }
/*      */   
/*      */   private Gene getOneOfGenesRandomly() {
/*  686 */     if (this.random.nextBoolean()) {
/*  687 */       return getMainGene();
/*      */     }
/*      */     
/*  690 */     return getHiddenGene();
/*      */   }
/*      */   
/*      */   public void setAttributes() {
/*  694 */     if (isWeak()) {
/*  695 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
/*      */     }
/*      */     
/*  698 */     if (isLazy()) {
/*  699 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.07000000029802322D);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tryToSit() {
/*  704 */     if (!isInWater()) {
/*  705 */       setZza(0.0F);
/*  706 */       getNavigation().stop();
/*  707 */       sit(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  713 */     ItemStack interactionItemStack = player.getItemInHand(hand);
/*      */     
/*  715 */     if (isScared()) {
/*  716 */       return (InteractionResult)InteractionResult.PASS;
/*      */     }
/*      */     
/*  719 */     if (isOnBack()) {
/*  720 */       setOnBack(false);
/*  721 */       return (InteractionResult)InteractionResult.SUCCESS;
/*      */     } 
/*      */     
/*  724 */     if (isFood(interactionItemStack)) {
/*  725 */       if (getTarget() != null) {
/*  726 */         this.gotBamboo = true;
/*      */       }
/*      */       
/*  729 */       if (isBaby())
/*  730 */       { usePlayerItem(player, hand, interactionItemStack);
/*  731 */         ageUp((int)((-getAge() / 20) * 0.1F), true); }
/*  732 */       else if (!level().isClientSide() && getAge() == 0 && canFallInLove())
/*  733 */       { usePlayerItem(player, hand, interactionItemStack);
/*  734 */         setInLove(player); }
/*  735 */       else { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isSitting() && !isInWater())
/*  736 */           { tryToSit();
/*  737 */             eat(true);
/*      */             
/*  739 */             ItemStack pandasCurrentItem = getItemBySlot(EquipmentSlot.MAINHAND);
/*  740 */             if (!pandasCurrentItem.isEmpty() && !player.hasInfiniteMaterials()) {
/*  741 */               spawnAtLocation(serverLevel, pandasCurrentItem);
/*      */             }
/*  743 */             setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((net.minecraft.world.level.ItemLike)interactionItemStack.getItem(), 1));
/*      */             
/*  745 */             usePlayerItem(player, hand, interactionItemStack); }
/*      */           else
/*  747 */           { return (InteractionResult)InteractionResult.PASS; }  } else { return (InteractionResult)InteractionResult.PASS; }
/*      */          }
/*      */       
/*  750 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*      */     } 
/*      */     
/*  753 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  758 */     if (isAggressive())
/*  759 */       return SoundEvents.PANDA_AGGRESSIVE_AMBIENT; 
/*  760 */     if (isWorried()) {
/*  761 */       return SoundEvents.PANDA_WORRIED_AMBIENT;
/*      */     }
/*  763 */     return SoundEvents.PANDA_AMBIENT;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  769 */     playSound(SoundEvents.PANDA_STEP, 0.15F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  774 */     return itemStack.is(ItemTags.PANDA_FOOD);
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  779 */     return SoundEvents.PANDA_DEATH;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  784 */     return SoundEvents.PANDA_HURT;
/*      */   }
/*      */   
/*      */   public boolean canPerformAction() {
/*  788 */     return (!isOnBack() && !isScared() && !isEating() && !isRolling() && !isSitting());
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityDimensions getDefaultDimensions(Pose pose) {
/*  793 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*      */   }
/*      */   
/*      */   private static class PandaMoveControl extends MoveControl {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaMoveControl(Panda mob) {
/*  800 */       super((Mob)mob);
/*  801 */       this.panda = mob;
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  806 */       if (!this.panda.canPerformAction()) {
/*      */         return;
/*      */       }
/*      */       
/*  810 */       super.tick();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaAttackGoal extends MeleeAttackGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAttackGoal(Panda mob, double speedModifier, boolean trackTarget) {
/*  818 */       super((PathfinderMob)mob, speedModifier, trackTarget);
/*  819 */       this.panda = mob;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  824 */       return (this.panda.canPerformAction() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaLookAtPlayerGoal(Panda mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/*  832 */       super((Mob)mob, lookAtType, lookDistance);
/*  833 */       this.panda = mob;
/*      */     }
/*      */     
/*      */     public void setTarget(LivingEntity entity) {
/*  837 */       this.lookAt = (Entity)entity;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  842 */       return (this.lookAt != null && super.canContinueToUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  847 */       if (this.mob.getRandom().nextFloat() >= this.probability) {
/*  848 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  853 */       if (this.lookAt == null) {
/*  854 */         ServerLevel level = getServerLevel((Entity)this.mob);
/*  855 */         if (this.lookAtType == Player.class) {
/*  856 */           this.lookAt = (Entity)level.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*      */         } else {
/*  858 */           this.lookAt = (Entity)level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), entity -> true), this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*      */         } 
/*      */       } 
/*      */       
/*  862 */       return (this.panda.canPerformAction() && this.lookAt != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  867 */       if (this.lookAt != null)
/*  868 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaRollGoal
/*      */     extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaRollGoal(Panda panda) {
/*  877 */       this.panda = panda;
/*  878 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  883 */       if ((!this.panda.isBaby() && !this.panda.isPlayful()) || !this.panda.onGround()) {
/*  884 */         return false;
/*      */       }
/*      */       
/*  887 */       if (!this.panda.canPerformAction()) {
/*  888 */         return false;
/*      */       }
/*      */       
/*  891 */       float angle = this.panda.getYRot() * 0.017453292F;
/*  892 */       float xDir = -Mth.sin(angle);
/*  893 */       float zDir = Mth.cos(angle);
/*  894 */       int xStep = (Math.abs(xDir) > 0.5D) ? Mth.sign(xDir) : 0;
/*  895 */       int zStep = (Math.abs(zDir) > 0.5D) ? Mth.sign(zDir) : 0;
/*      */       
/*  897 */       if (this.panda.level().getBlockState(this.panda.blockPosition().offset(xStep, -1, zStep)).isAir()) {
/*  898 */         return true;
/*      */       }
/*      */       
/*  901 */       if (this.panda.isPlayful() && this.panda.random.nextInt(reducedTickDelay(60)) == 1) {
/*  902 */         return true;
/*      */       }
/*      */       
/*  905 */       return (this.panda.random.nextInt(reducedTickDelay(500)) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  910 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  915 */       this.panda.roll(true);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isInterruptable() {
/*  920 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaSneezeGoal
/*      */     extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaSneezeGoal(Panda panda) {
/*  929 */       this.panda = panda;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  934 */       if (!this.panda.isBaby() || !this.panda.canPerformAction()) {
/*  935 */         return false;
/*      */       }
/*      */       
/*  938 */       if (this.panda.isWeak() && this.panda.random.nextInt(reducedTickDelay(500)) == 1) {
/*  939 */         return true;
/*      */       }
/*      */       
/*  942 */       return (this.panda.random.nextInt(reducedTickDelay(6000)) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  947 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  952 */       this.panda.sneeze(true);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaBreedGoal extends BreedGoal {
/*      */     private final Panda panda;
/*      */     private int unhappyCooldown;
/*      */     
/*      */     public PandaBreedGoal(Panda panda, double speedModifier) {
/*  961 */       super(panda, speedModifier);
/*  962 */       this.panda = panda;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  967 */       if (super.canUse() && this.panda.getUnhappyCounter() == 0) {
/*  968 */         if (!canFindBamboo()) {
/*  969 */           if (this.unhappyCooldown <= this.panda.tickCount) {
/*  970 */             this.panda.setUnhappyCounter(32);
/*  971 */             this.unhappyCooldown = this.panda.tickCount + 600;
/*  972 */             if (this.panda.isEffectiveAi()) {
/*  973 */               Player player = this.level.getNearestPlayer(Panda.BREED_TARGETING, (LivingEntity)this.panda);
/*  974 */               this.panda.lookAtPlayerGoal.setTarget((LivingEntity)player);
/*      */             } 
/*      */           } 
/*      */           
/*  978 */           return false;
/*      */         } 
/*      */         
/*  981 */         return true;
/*      */       } 
/*      */       
/*  984 */       return false;
/*      */     }
/*      */     
/*      */     private boolean canFindBamboo() {
/*  988 */       BlockPos pandaPos = this.panda.blockPosition();
/*  989 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  990 */       for (int yOff = 0; yOff < 3; yOff++) {
/*  991 */         for (int r = 0; r < 8; r++) {
/*  992 */           for (int x = 0; x <= r; x = (x > 0) ? -x : (1 - x)) {
/*  993 */             int z = (x < r && x > -r) ? r : 0;
/*  994 */             for (; z <= r; z = (z > 0) ? -z : (1 - z)) {
/*  995 */               pos.setWithOffset((Vec3i)pandaPos, x, yOff, z);
/*  996 */               if (this.level.getBlockState((BlockPos)pos).is(Blocks.BAMBOO)) {
/*  997 */                 return true;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1003 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAvoidGoal(Panda panda, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 1011 */       super((PathfinderMob)panda, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_SPECTATORS);
/*      */       
/* 1013 */       this.panda = panda;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1018 */       return (this.panda.isWorried() && this.panda.canPerformAction() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean canPickUpAndEat(ItemEntity entity) {
/* 1023 */     return (entity.getItem().is(ItemTags.PANDA_EATS_FROM_GROUND) && entity.isAlive() && !entity.hasPickUpDelay());
/*      */   }
/*      */   
/*      */   private class PandaSitGoal extends Goal {
/*      */     private int cooldown;
/*      */     
/*      */     public PandaSitGoal() {
/* 1030 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1035 */       if (this.cooldown > Panda.this.tickCount || Panda.this.isBaby() || Panda.this.isInWater() || !Panda.this.canPerformAction() || Panda.this.getUnhappyCounter() > 0) {
/* 1036 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1040 */       if (!Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1041 */         return true;
/*      */       }
/*      */       
/* 1044 */       return !Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(6.0D, 6.0D, 6.0D), Panda::canPickUpAndEat).isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1049 */       if (Panda.this.isInWater() || (!Panda.this.isLazy() && Panda.this.random.nextInt(reducedTickDelay(600)) == 1)) {
/* 1050 */         return false;
/*      */       }
/*      */       
/* 1053 */       if (Panda.this.random.nextInt(reducedTickDelay(2000)) == 1) {
/* 1054 */         return false;
/*      */       }
/*      */       
/* 1057 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1062 */       if (!Panda.this.isSitting() && !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1063 */         Panda.this.tryToSit();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1069 */       if (Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1070 */         List<ItemEntity> items = Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Panda::canPickUpAndEat);
/* 1071 */         if (!items.isEmpty()) {
/* 1072 */           Panda.this.getNavigation().moveTo((Entity)items.getFirst(), 1.2000000476837158D);
/*      */         }
/*      */       } else {
/* 1075 */         Panda.this.tryToSit();
/*      */       } 
/*      */       
/* 1078 */       this.cooldown = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1083 */       ItemStack itemStack = Panda.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1084 */       if (!itemStack.isEmpty()) {
/* 1085 */         Panda.this.spawnAtLocation(getServerLevel(Panda.this.level()), itemStack);
/* 1086 */         Panda.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 1087 */         int waitSeconds = Panda.this.isLazy() ? (Panda.this.random.nextInt(50) + 10) : (Panda.this.random.nextInt(150) + 10);
/* 1088 */         this.cooldown = Panda.this.tickCount + waitSeconds * 20;
/*      */       } 
/*      */       
/* 1091 */       Panda.this.sit(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaLieOnBackGoal extends Goal {
/*      */     private final Panda panda;
/*      */     private int cooldown;
/*      */     
/*      */     public PandaLieOnBackGoal(Panda panda) {
/* 1100 */       this.panda = panda;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1105 */       return (this.cooldown < this.panda.tickCount && this.panda.isLazy() && this.panda.canPerformAction() && this.panda.random.nextInt(reducedTickDelay(400)) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1110 */       if (this.panda.isInWater() || (!this.panda.isLazy() && this.panda.random.nextInt(reducedTickDelay(600)) == 1)) {
/* 1111 */         return false;
/*      */       }
/*      */       
/* 1114 */       if (this.panda.random.nextInt(reducedTickDelay(2000)) == 1) {
/* 1115 */         return false;
/*      */       }
/*      */       
/* 1118 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1123 */       this.panda.setOnBack(true);
/* 1124 */       this.cooldown = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1129 */       this.panda.setOnBack(false);
/* 1130 */       this.cooldown = this.panda.tickCount + 200;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaHurtByTargetGoal extends HurtByTargetGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaHurtByTargetGoal(Panda mob, Class<?>... ignoreDamageFromTheseTypes) {
/* 1138 */       super((PathfinderMob)mob, ignoreDamageFromTheseTypes);
/* 1139 */       this.panda = mob;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1144 */       if (this.panda.gotBamboo || this.panda.didBite) {
/* 1145 */         this.panda.setTarget(null);
/* 1146 */         return false;
/*      */       } 
/* 1148 */       return super.canContinueToUse();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 1153 */       if (other instanceof Panda && other.isAggressive())
/* 1154 */         other.setTarget(hurtByMob); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaPanicGoal
/*      */     extends PanicGoal
/*      */   {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaPanicGoal(Panda mob, double speedModifier) {
/* 1164 */       super((PathfinderMob)mob, speedModifier, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/* 1165 */       this.panda = mob;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1170 */       if (this.panda.isSitting()) {
/* 1171 */         this.panda.getNavigation().stop();
/* 1172 */         return false;
/*      */       } 
/* 1174 */       return super.canContinueToUse();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/panda/Panda.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */