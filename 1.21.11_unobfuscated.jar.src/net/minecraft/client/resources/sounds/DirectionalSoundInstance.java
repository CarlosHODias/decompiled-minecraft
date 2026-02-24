/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DirectionalSoundInstance extends AbstractTickableSoundInstance {
/*    */   private final Camera camera;
/*    */   private final float xAngle;
/*    */   private final float yAngle;
/*    */   
/*    */   public DirectionalSoundInstance(SoundEvent event, SoundSource source, RandomSource random, Camera camera, float xAngle, float yAngle) {
/* 15 */     super(event, source, random);
/* 16 */     this.camera = camera;
/* 17 */     this.xAngle = xAngle;
/* 18 */     this.yAngle = yAngle;
/* 19 */     setPosition();
/*    */   }
/*    */   
/*    */   private void setPosition() {
/* 23 */     Vec3 direction = Vec3.directionFromRotation(this.xAngle, this.yAngle).scale(10.0D);
/* 24 */     this.x = (this.camera.position()).x + direction.x;
/* 25 */     this.y = (this.camera.position()).y + direction.y;
/* 26 */     this.z = (this.camera.position()).z + direction.z;
/* 27 */     this.attenuation = SoundInstance.Attenuation.NONE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 32 */     setPosition();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/DirectionalSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */