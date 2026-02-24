/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public class CameraRenderState {
/*  8 */   public BlockPos blockPos = BlockPos.ZERO;
/*  9 */   public Vec3 pos = new Vec3(0.0D, 0.0D, 0.0D);
/*    */   public boolean initialized;
/* 11 */   public Vec3 entityPos = new Vec3(0.0D, 0.0D, 0.0D);
/* 12 */   public Quaternionf orientation = new Quaternionf();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/CameraRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */