/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.lwjgl.openal.AL10;
/*    */ 
/*    */ public class Listener {
/*  7 */   private ListenerTransform transform = ListenerTransform.INITIAL;
/*    */   
/*    */   public void setTransform(ListenerTransform transform) {
/* 10 */     this.transform = transform;
/* 11 */     Vec3 position = transform.position();
/* 12 */     Vec3 forward = transform.forward();
/* 13 */     Vec3 up = transform.up();
/* 14 */     AL10.alListener3f(4100, (float)position.x, (float)position.y, (float)position.z);
/* 15 */     AL10.alListenerfv(4111, new float[] { (float)forward.x, (float)forward.y, (float)forward.z, (float)up.x(), (float)up.y(), (float)up.z() });
/*    */   }
/*    */   
/*    */   public void reset() {
/* 19 */     setTransform(ListenerTransform.INITIAL);
/*    */   }
/*    */   
/*    */   public ListenerTransform getTransform() {
/* 23 */     return this.transform;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/Listener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */