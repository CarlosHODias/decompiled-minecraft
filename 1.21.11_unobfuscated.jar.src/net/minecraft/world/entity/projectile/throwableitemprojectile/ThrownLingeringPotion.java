/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownLingeringPotion extends AbstractThrownPotion {
/*    */   public ThrownLingeringPotion(EntityType<? extends ThrownLingeringPotion> type, Level level) {
/* 16 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level level, LivingEntity owner, ItemStack itemStack) {
/* 20 */     super(EntityType.LINGERING_POTION, level, owner, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level level, double x, double y, double z, ItemStack itemStack) {
/* 24 */     super(EntityType.LINGERING_POTION, level, x, y, z, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 29 */     return Items.LINGERING_POTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onHitAsPotion(ServerLevel level, ItemStack potionItem, HitResult hitResult) {
/* 34 */     AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 35 */     Entity entity = getOwner(); if (entity instanceof LivingEntity) { LivingEntity owner = (LivingEntity)entity;
/* 36 */       cloud.setOwner(owner); }
/*    */     
/* 38 */     cloud.setRadius(3.0F);
/* 39 */     cloud.setRadiusOnUse(-0.5F);
/* 40 */     cloud.setDuration(600);
/* 41 */     cloud.setWaitTime(10);
/* 42 */     cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
/* 43 */     cloud.applyComponentsFromItemStack(potionItem);
/*    */     
/* 45 */     level.addFreshEntity((Entity)cloud);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/throwableitemprojectile/ThrownLingeringPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */