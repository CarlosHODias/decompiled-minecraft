/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public class VibrationSignalParticle
/*     */   extends SingleQuadParticle {
/*     */   private final PositionSource target;
/*     */   private float rot;
/*     */   private float rotO;
/*     */   private float pitch;
/*     */   private float pitchO;
/*     */   
/*     */   private VibrationSignalParticle(ClientLevel level, double x, double y, double z, PositionSource target, int arrivalInTicks, TextureAtlasSprite sprite) {
/*  26 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/*     */     
/*  28 */     this.quadSize = 0.3F;
/*     */     
/*  30 */     this.target = target;
/*  31 */     this.lifetime = arrivalInTicks;
/*     */     
/*  33 */     Optional<Vec3> position = target.getPosition((Level)level);
/*  34 */     if (position.isPresent()) {
/*  35 */       Vec3 destination = position.get();
/*  36 */       double dx = x - destination.x();
/*  37 */       double dy = y - destination.y();
/*  38 */       double dz = z - destination.z();
/*  39 */       this.rotO = this.rot = (float)Mth.atan2(dx, dz);
/*  40 */       this.pitchO = this.pitch = (float)Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/*  46 */     float randomSway = Mth.sin(((this.age + partialTickTime - 6.2831855F) * 0.05F)) * 2.0F;
/*  47 */     float lerpedRotation = Mth.lerp(partialTickTime, this.rotO, this.rot);
/*  48 */     float lerpedPitch = Mth.lerp(partialTickTime, this.pitchO, this.pitch) + 1.5707964F;
/*     */     
/*  50 */     Quaternionf rotation = new Quaternionf();
/*  51 */     rotation.rotationY(lerpedRotation).rotateX(-lerpedPitch).rotateY(randomSway);
/*  52 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*     */     
/*  54 */     rotation.rotationY(-3.1415927F + lerpedRotation).rotateX(lerpedPitch).rotateY(randomSway);
/*  55 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/*  60 */     return 240;
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  65 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  70 */     this.xo = this.x;
/*  71 */     this.yo = this.y;
/*  72 */     this.zo = this.z;
/*     */     
/*  74 */     if (this.age++ >= this.lifetime) {
/*  75 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     Optional<Vec3> position = this.target.getPosition((Level)this.level);
/*     */ 
/*     */     
/*  82 */     if (position.isEmpty()) {
/*  83 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     int ticksRemaining = this.lifetime - this.age;
/*  88 */     double alpha = 1.0D / ticksRemaining;
/*     */     
/*  90 */     Vec3 destination = position.get();
/*     */     
/*  92 */     this.x = Mth.lerp(alpha, this.x, destination.x());
/*  93 */     this.y = Mth.lerp(alpha, this.y, destination.y());
/*  94 */     this.z = Mth.lerp(alpha, this.z, destination.z());
/*     */     
/*  96 */     double dx = this.x - destination.x();
/*  97 */     double dy = this.y - destination.y();
/*  98 */     double dz = this.z - destination.z();
/*     */     
/* 100 */     this.rotO = this.rot;
/* 101 */     this.rot = (float)Mth.atan2(dx, dz);
/*     */     
/* 103 */     this.pitchO = this.pitch;
/* 104 */     this.pitch = (float)Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz));
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<VibrationParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/* 111 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(VibrationParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 116 */       VibrationSignalParticle particle = new VibrationSignalParticle(level, x, y, z, options.getDestination(), options.getArrivalInTicks(), this.sprite.get(random));
/* 117 */       particle.setAlpha(1.0F);
/* 118 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/VibrationSignalParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */