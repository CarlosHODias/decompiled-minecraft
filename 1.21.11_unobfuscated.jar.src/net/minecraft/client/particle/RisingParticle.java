/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ 
/*    */ public abstract class RisingParticle extends SingleQuadParticle {
/*    */   protected RisingParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*  8 */     super(level, x, y, z, xd, yd, zd, sprite);
/*  9 */     this.friction = 0.96F;
/* 10 */     this.xd = this.xd * 0.009999999776482582D + xd;
/* 11 */     this.yd = this.yd * 0.009999999776482582D + yd;
/* 12 */     this.zd = this.zd * 0.009999999776482582D + zd;
/* 13 */     this.x += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
/* 14 */     this.y += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
/* 15 */     this.z += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
/*    */     
/* 17 */     this.lifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.2D)) + 4;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/RisingParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */