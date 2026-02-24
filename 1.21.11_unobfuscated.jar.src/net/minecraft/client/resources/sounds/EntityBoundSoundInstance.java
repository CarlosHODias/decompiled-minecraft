/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityBoundSoundInstance extends AbstractTickableSoundInstance {
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityBoundSoundInstance(SoundEvent event, SoundSource source, float volume, float pitch, Entity entity, long seed) {
/* 12 */     super(event, source, RandomSource.create(seed));
/* 13 */     this.volume = volume;
/* 14 */     this.pitch = pitch;
/* 15 */     this.entity = entity;
/*    */     
/* 17 */     this.x = (float)this.entity.getX();
/* 18 */     this.y = (float)this.entity.getY();
/* 19 */     this.z = (float)this.entity.getZ();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaySound() {
/* 24 */     return !this.entity.isSilent();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 29 */     if (this.entity.isRemoved()) {
/* 30 */       stop();
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     this.x = (float)this.entity.getX();
/* 35 */     this.y = (float)this.entity.getY();
/* 36 */     this.z = (float)this.entity.getZ();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/EntityBoundSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */