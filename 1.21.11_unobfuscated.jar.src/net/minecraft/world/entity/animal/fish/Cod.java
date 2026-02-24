/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Cod extends AbstractSchoolingFish {
/*    */   public Cod(EntityType<? extends Cod> type, Level level) {
/* 13 */     super((EntityType)type, level);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getBucketItemStack() {
/* 18 */     return new ItemStack((ItemLike)Items.COD_BUCKET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 23 */     return SoundEvents.COD_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 28 */     return SoundEvents.COD_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 33 */     return SoundEvents.COD_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 38 */     return SoundEvents.COD_FLOP;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/fish/Cod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */