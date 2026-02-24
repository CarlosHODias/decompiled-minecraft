/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFish extends WaterAnimal implements Bucketable {
/*  36 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);
/*     */   private static final boolean DEFAULT_FROM_BUCKET = false;
/*     */   
/*     */   public AbstractFish(EntityType<? extends AbstractFish> type, Level level) {
/*  40 */     super((EntityType)type, level);
/*     */     
/*  42 */     this.moveControl = new FishMoveControl(this);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  46 */     return Mob.createMobAttributes()
/*  47 */       .add(Attributes.MAX_HEALTH, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/*  52 */     return (super.requiresCustomPersistence() || fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/*  57 */     return (!fromBucket() && !hasCustomName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/*  62 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  67 */     super.defineSynchedData(entityData);
/*     */     
/*  69 */     entityData.define(FROM_BUCKET, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fromBucket() {
/*  74 */     return (Boolean)this.entityData.get(FROM_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFromBucket(boolean fromBucket) {
/*  79 */     this.entityData.set(FROM_BUCKET, fromBucket);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  84 */     super.addAdditionalSaveData(output);
/*     */     
/*  86 */     output.putBoolean("FromBucket", fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  91 */     super.readAdditionalSaveData(input);
/*     */     
/*  93 */     setFromBucket(input.getBooleanOr("FromBucket", false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  98 */     super.registerGoals();
/*     */     
/* 100 */     this.goalSelector.addGoal(0, (Goal)new PanicGoal(this, 1.25D));
/* 101 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS));
/* 102 */     this.goalSelector.addGoal(4, (Goal)new FishSwimGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 107 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 112 */     moveRelative(0.01F, input);
/* 113 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 115 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 116 */     if (getTarget() == null) {
/* 117 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 123 */     if (!isInWater() && onGround() && this.verticalCollision) {
/* 124 */       setDeltaMovement(getDeltaMovement().add(((
/* 125 */             this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645D, ((
/*     */             
/* 127 */             this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
/*     */       
/* 129 */       setOnGround(false);
/* 130 */       this.needsSync = true;
/* 131 */       makeSound(getFlopSound());
/*     */     } 
/*     */     
/* 134 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 139 */     return Bucketable.bucketMobPickup(player, hand, (LivingEntity)this).orElse(super.mobInteract(player, hand));
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 144 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag tag) {
/* 149 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 154 */     return SoundEvents.BUCKET_FILL_FISH;
/*     */   }
/*     */   
/*     */   private static class FishSwimGoal extends RandomSwimmingGoal {
/*     */     private final AbstractFish fish;
/*     */     
/*     */     public FishSwimGoal(AbstractFish fish) {
/* 161 */       super(fish, 1.0D, 40);
/* 162 */       this.fish = fish;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 167 */       return (this.fish.canRandomSwim() && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canRandomSwim() {
/* 172 */     return true;
/*     */   }
/*     */   protected abstract SoundEvent getFlopSound();
/*     */   
/*     */   private static class FishMoveControl extends MoveControl { private final AbstractFish fish;
/*     */     
/*     */     FishMoveControl(AbstractFish fish) {
/* 179 */       super((Mob)fish);
/* 180 */       this.fish = fish;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 185 */       if (this.fish.isEyeInFluid(FluidTags.WATER))
/*     */       {
/* 187 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */       }
/*     */       
/* 190 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
/* 191 */         this.fish.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 195 */       float targetSpeed = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 196 */       this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), targetSpeed));
/*     */       
/* 198 */       double xd = this.wantedX - this.fish.getX();
/* 199 */       double yd = this.wantedY - this.fish.getY();
/* 200 */       double zd = this.wantedZ - this.fish.getZ();
/*     */       
/* 202 */       if (yd != 0.0D) {
/* 203 */         double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/*     */         
/* 205 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, this.fish.getSpeed() * yd / dd * 0.1D, 0.0D));
/*     */       } 
/*     */       
/* 208 */       if (xd != 0.0D || zd != 0.0D) {
/* 209 */         float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/*     */         
/* 211 */         this.fish.setYRot(rotlerp(this.fish.getYRot(), yRotD, 90.0F));
/* 212 */         this.fish.yBodyRot = this.fish.getYRot();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 221 */     return SoundEvents.FISH_SWIM;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockState) {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/fish/AbstractFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */