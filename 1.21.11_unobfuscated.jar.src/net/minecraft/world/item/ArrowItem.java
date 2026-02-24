/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ArrowItem
/*    */   extends Item implements ProjectileItem {
/*    */   public ArrowItem(Item.Properties properties) {
/* 14 */     super(properties);
/*    */   }
/*    */   
/*    */   public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity owner, ItemStack firedFromWeapon) {
/* 18 */     return (AbstractArrow)new Arrow(level, owner, itemStack.copyWithCount(1), firedFromWeapon);
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 23 */     Arrow arrow = new Arrow(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
/* 24 */     arrow.pickup = AbstractArrow.Pickup.ALLOWED;
/*    */     
/* 26 */     return (Projectile)arrow;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/ArrowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */