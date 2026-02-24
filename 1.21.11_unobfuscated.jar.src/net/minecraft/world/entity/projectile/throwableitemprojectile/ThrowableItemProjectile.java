/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*    */ import net.minecraft.world.entity.projectile.ThrowableProjectile;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public abstract class ThrowableItemProjectile extends ThrowableProjectile implements ItemSupplier {
/* 17 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrowableItemProjectile.class, net.minecraft.network.syncher.EntityDataSerializers.ITEM_STACK);
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
/* 20 */     super(type, level);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level level, ItemStack itemStack) {
/* 24 */     super(type, x, y, z, level);
/* 25 */     setItem(itemStack);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, LivingEntity owner, Level level, ItemStack itemStack) {
/* 29 */     this(type, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), level, itemStack);
/* 30 */     setOwner((Entity)owner);
/*    */   }
/*    */   
/*    */   public void setItem(ItemStack source) {
/* 34 */     getEntityData().set(DATA_ITEM_STACK, source.copyWithCount(1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract Item getDefaultItem();
/*    */   
/*    */   public ItemStack getItem() {
/* 41 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 46 */     entityData.define(DATA_ITEM_STACK, new ItemStack((ItemLike)getDefaultItem()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 51 */     super.addAdditionalSaveData(output);
/* 52 */     output.store("Item", ItemStack.CODEC, getItem());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 57 */     super.readAdditionalSaveData(input);
/* 58 */     setItem(input.read("Item", ItemStack.CODEC).orElseGet(() -> new ItemStack((ItemLike)getDefaultItem())));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/throwableitemprojectile/ThrowableItemProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */