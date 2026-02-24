/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class Marker
/*    */   extends Entity {
/*    */   public Marker(EntityType<?> type, Level level) {
/* 17 */     super(type, level);
/* 18 */     this.noPhysics = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {}
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
/*    */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 39 */     throw new IllegalStateException("Markers should never be sent");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canAddPassenger(Entity passenger) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean couldAcceptPassenger() {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addPassenger(Entity passenger) {
/* 54 */     throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
/*    */   }
/*    */ 
/*    */   
/*    */   public PushReaction getPistonPushReaction() {
/* 59 */     return PushReaction.IGNORE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isIgnoringBlockTriggers() {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/Marker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */