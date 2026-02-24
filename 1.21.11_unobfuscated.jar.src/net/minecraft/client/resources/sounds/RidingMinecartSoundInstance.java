/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ 
/*    */ public class RidingMinecartSoundInstance
/*    */   extends RidingEntitySoundInstance {
/*    */   private final Player player;
/*    */   private final AbstractMinecart minecart;
/*    */   private final boolean underwaterSound;
/*    */   
/*    */   public RidingMinecartSoundInstance(Player player, AbstractMinecart minecart, boolean underwaterSound, SoundEvent soundEvent, float volumeMin, float volumeMax, float volumeAmplifier) {
/* 16 */     super(player, (Entity)minecart, underwaterSound, soundEvent, SoundSource.NEUTRAL, volumeMin, volumeMax, volumeAmplifier);
/* 17 */     this.player = player;
/* 18 */     this.minecart = minecart;
/* 19 */     this.underwaterSound = underwaterSound;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldNotPlayUnderwaterSound() {
/* 24 */     return (this.underwaterSound != this.player.isUnderWater());
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getEntitySpeed() {
/* 29 */     return (float)this.minecart.getDeltaMovement().horizontalDistance();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shoudlPlaySound() {
/* 34 */     return (this.minecart.isOnRails() || !(this.minecart.getBehavior() instanceof net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/RidingMinecartSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */