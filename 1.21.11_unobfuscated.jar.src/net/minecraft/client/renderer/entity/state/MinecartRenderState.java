/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartRenderState
/*    */   extends EntityRenderState {
/*    */   public float xRot;
/*    */   public float yRot;
/*    */   public long offsetSeed;
/*    */   public int hurtDir;
/*    */   public float hurtTime;
/*    */   public float damageTime;
/*    */   public int displayOffset;
/* 16 */   public BlockState displayBlockState = Blocks.AIR.defaultBlockState();
/*    */   public boolean isNewRender;
/*    */   public Vec3 renderPos;
/*    */   public Vec3 posOnRail;
/*    */   public Vec3 frontPos;
/*    */   public Vec3 backPos;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/MinecartRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */