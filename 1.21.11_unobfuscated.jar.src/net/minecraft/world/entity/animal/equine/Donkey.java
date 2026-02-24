/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Donkey
/*    */   extends AbstractChestedHorse {
/*    */   public Donkey(EntityType<? extends Donkey> type, Level level) {
/* 16 */     super((EntityType)type, level);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 21 */     return SoundEvents.DONKEY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 26 */     return SoundEvents.DONKEY_ANGRY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 31 */     return SoundEvents.DONKEY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getEatingSound() {
/* 36 */     return SoundEvents.DONKEY_EAT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 41 */     return SoundEvents.DONKEY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canMate(Animal partner) {
/* 46 */     if (partner == this) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     if (partner instanceof Donkey || partner instanceof Horse) {
/* 51 */       return (canParent() && ((AbstractHorse)partner).canParent());
/*    */     }
/*    */     
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playJumpSound() {
/* 59 */     playSound(SoundEvents.DONKEY_JUMP, 0.4F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 64 */     EntityType<? extends AbstractHorse> babyType = (partner instanceof Horse) ? EntityType.MULE : EntityType.DONKEY;
/* 65 */     AbstractHorse baby = (AbstractHorse)babyType.create((Level)level, EntitySpawnReason.BREEDING);
/*    */     
/* 67 */     if (baby != null) {
/* 68 */       setOffspringAttributes(partner, baby);
/*    */     }
/*    */     
/* 71 */     return (AgeableMob)baby;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/equine/Donkey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */