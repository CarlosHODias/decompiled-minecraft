/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ 
/*    */ public class NoRenderParticle extends Particle {
/*    */   protected NoRenderParticle(ClientLevel level, double x, double y, double z) {
/*  7 */     super(level, x, y, z);
/*    */   }
/*    */   
/*    */   protected NoRenderParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
/* 11 */     super(level, x, y, z, xa, ya, za);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleRenderType getGroup() {
/* 16 */     return ParticleRenderType.NO_RENDER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/NoRenderParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */