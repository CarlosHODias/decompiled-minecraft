/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class CamelHusk
/*     */   extends Camel
/*     */ {
/*     */   public CamelHusk(EntityType<? extends Camel> type, Level level) {
/*  25 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/*  30 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMobControlled() {
/*  35 */     return getFirstPassenger() instanceof net.minecraft.world.entity.Mob;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  40 */     setPersistenceRequired();
/*  41 */     return super.interact(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/*  46 */     return !isMobControlled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  51 */     return itemStack.is(ItemTags.CAMEL_HUSK_FOOD);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  56 */     return SoundEvents.CAMEL_HUSK_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Camel getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  76 */     return SoundEvents.CAMEL_HUSK_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  81 */     return SoundEvents.CAMEL_HUSK_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  86 */     if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
/*  87 */       playSound(SoundEvents.CAMEL_HUSK_STEP_SAND, 0.4F, 1.0F);
/*     */     } else {
/*  89 */       playSound(SoundEvents.CAMEL_HUSK_STEP, 0.4F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDashingSound() {
/*  95 */     return SoundEvents.CAMEL_HUSK_DASH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 100 */     return SoundEvents.CAMEL_HUSK_DASH_READY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 105 */     return SoundEvents.CAMEL_HUSK_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStandUpSound() {
/* 110 */     return SoundEvents.CAMEL_HUSK_STAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSitDownSound() {
/* 115 */     return SoundEvents.CAMEL_HUSK_SIT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Holder.Reference<SoundEvent> getSaddleSound() {
/* 120 */     return SoundEvents.CAMEL_HUSK_SADDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public float chargeSpeedModifier() {
/* 125 */     return 4.0F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/camel/CamelHusk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */