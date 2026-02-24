/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class VecDeltaCodec
/*    */ {
/*    */   private static final double TRUNCATION_STEPS = 4096.0D;
/*  9 */   private Vec3 base = Vec3.ZERO;
/*    */   
/*    */   @VisibleForTesting
/*    */   static long encode(double input) {
/* 13 */     return Math.round(input * 4096.0D);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static double decode(long v) {
/* 18 */     return v / 4096.0D;
/*    */   }
/*    */   
/*    */   public Vec3 decode(long xa, long ya, long za) {
/* 22 */     if (xa == 0L && ya == 0L && za == 0L) {
/* 23 */       return this.base;
/*    */     }
/* 25 */     double x = (xa == 0L) ? this.base.x : decode(encode(this.base.x) + xa);
/* 26 */     double y = (ya == 0L) ? this.base.y : decode(encode(this.base.y) + ya);
/* 27 */     double z = (za == 0L) ? this.base.z : decode(encode(this.base.z) + za);
/* 28 */     return new Vec3(x, y, z);
/*    */   }
/*    */   
/*    */   public long encodeX(Vec3 pos) {
/* 32 */     return encode(pos.x) - encode(this.base.x);
/*    */   }
/*    */   
/*    */   public long encodeY(Vec3 pos) {
/* 36 */     return encode(pos.y) - encode(this.base.y);
/*    */   }
/*    */   
/*    */   public long encodeZ(Vec3 pos) {
/* 40 */     return encode(pos.z) - encode(this.base.z);
/*    */   }
/*    */   
/*    */   public Vec3 delta(Vec3 pos) {
/* 44 */     return pos.subtract(this.base);
/*    */   }
/*    */   
/*    */   public void setBase(Vec3 base) {
/* 48 */     this.base = base;
/*    */   }
/*    */   
/*    */   public Vec3 getBase() {
/* 52 */     return this.base;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/VecDeltaCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */