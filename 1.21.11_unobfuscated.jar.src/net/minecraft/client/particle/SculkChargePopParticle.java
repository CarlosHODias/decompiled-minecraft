/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SculkChargePopParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private SculkChargePopParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, xd, yd, zd, sprites.first());
/* 12 */     this.friction = 0.96F;
/*    */     
/* 14 */     this.sprites = sprites;
/* 15 */     scale(1.0F);
/*    */     
/* 17 */     this.hasPhysics = false;
/*    */     
/* 19 */     setSpriteFromAge(sprites);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 24 */     return 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 29 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 34 */     super.tick();
/* 35 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   public static final class Provider extends Record implements ParticleProvider<SimpleParticleType> { private final SpriteSet sprite;
/* 38 */     public Provider(SpriteSet sprite) { this.sprite = sprite; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 38 */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider; } public SpriteSet sprite() { return this.sprite; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/particle/SculkChargePopParticle$Provider;
/*    */       //   0	8	1	o	Ljava/lang/Object; } public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 41 */       SculkChargePopParticle particle = new SculkChargePopParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 42 */       particle.setAlpha(1.0F);
/* 43 */       particle.setParticleSpeed(xAux, yAux, zAux);
/* 44 */       particle.setLifetime(random.nextInt(4) + 6);
/* 45 */       return particle;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SculkChargePopParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */