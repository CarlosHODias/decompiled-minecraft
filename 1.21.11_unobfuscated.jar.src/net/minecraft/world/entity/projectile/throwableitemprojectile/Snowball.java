/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class Snowball
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public Snowball(EntityType<? extends Snowball> type, Level level) {
/* 20 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public Snowball(Level level, LivingEntity mob, ItemStack itemStack) {
/* 24 */     super(EntityType.SNOWBALL, mob, level, itemStack);
/*    */   }
/*    */   
/*    */   public Snowball(Level level, double x, double y, double z, ItemStack itemStack) {
/* 28 */     super(EntityType.SNOWBALL, x, y, z, level, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 33 */     return Items.SNOWBALL;
/*    */   }
/*    */   
/*    */   private ParticleOptions getParticle() {
/* 37 */     ItemStack item = getItem();
/* 38 */     return item.isEmpty() ? (ParticleOptions)ParticleTypes.ITEM_SNOWBALL : (ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, item);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleEntityEvent(byte id) {
/* 43 */     if (id == 3) {
/* 44 */       ParticleOptions particle = getParticle();
/* 45 */       for (int i = 0; i < 8; i++) {
/* 46 */         level().addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/* 53 */     super.onHitEntity(hitResult);
/* 54 */     Entity entity = hitResult.getEntity();
/* 55 */     int damage = (entity instanceof net.minecraft.world.entity.monster.Blaze) ? 3 : 0;
/*    */     
/* 57 */     entity.hurt(damageSources().thrown((Entity)this, getOwner()), damage);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 62 */     super.onHit(hitResult);
/*    */     
/* 64 */     if (!level().isClientSide()) {
/* 65 */       level().broadcastEntityEvent((Entity)this, (byte)3);
/* 66 */       discard();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/throwableitemprojectile/Snowball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */