/*    */ package net.minecraft.world.entity.boss.enderdragon;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class EnderDragonPart
/*    */   extends Entity
/*    */ {
/*    */   public final EnderDragon parentMob;
/*    */   public final String name;
/*    */   private final EntityDimensions size;
/*    */   
/*    */   public EnderDragonPart(EnderDragon parentMob, String name, float w, float h) {
/* 24 */     super(parentMob.getType(), parentMob.level());
/* 25 */     this.size = EntityDimensions.scalable(w, h);
/* 26 */     refreshDimensions();
/* 27 */     this.parentMob = parentMob;
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {}
/*    */ 
/*    */   
/*    */   public boolean isPickable() {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getPickResult() {
/* 50 */     return this.parentMob.getPickResult();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 55 */     if (isInvulnerableToBase(source)) {
/* 56 */       return false;
/*    */     }
/* 58 */     return this.parentMob.hurt(level, this, source, damage);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Entity other) {
/* 63 */     return (this == other || this.parentMob == other);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 69 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDimensions getDimensions(Pose pose) {
/* 74 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBeSaved() {
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/enderdragon/EnderDragonPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */