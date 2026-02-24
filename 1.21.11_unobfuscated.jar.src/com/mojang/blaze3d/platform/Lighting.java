/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class Lighting implements AutoCloseable {
/* 17 */   private static final Vector3f DIFFUSE_LIGHT_0 = new Vector3f(0.2F, 1.0F, -0.7F).normalize();
/* 18 */   private static final Vector3f DIFFUSE_LIGHT_1 = new Vector3f(-0.2F, 1.0F, 0.7F).normalize();
/*    */   
/* 20 */   private static final Vector3f NETHER_DIFFUSE_LIGHT_0 = new Vector3f(0.2F, 1.0F, -0.7F).normalize();
/* 21 */   private static final Vector3f NETHER_DIFFUSE_LIGHT_1 = new Vector3f(-0.2F, -1.0F, 0.7F).normalize();
/*    */   
/* 23 */   private static final Vector3f INVENTORY_DIFFUSE_LIGHT_0 = new Vector3f(0.2F, -1.0F, 1.0F).normalize();
/* 24 */   private static final Vector3f INVENTORY_DIFFUSE_LIGHT_1 = new Vector3f(-0.2F, -1.0F, 0.0F).normalize();
/*    */   
/* 26 */   public static final int UBO_SIZE = new Std140SizeCalculator()
/* 27 */     .putVec3()
/* 28 */     .putVec3()
/* 29 */     .get();
/*    */   
/*    */   private final GpuBuffer buffer;
/*    */   private final long paddedSize;
/*    */   
/*    */   public Lighting() {
/* 35 */     GpuDevice device = RenderSystem.getDevice();
/* 36 */     this.paddedSize = Mth.roundToward(UBO_SIZE, device.getUniformOffsetAlignment());
/* 37 */     this.buffer = device.createBuffer(() -> "Lighting UBO", 136, this.paddedSize * (Entry.values()).length);
/*    */     
/* 39 */     Matrix4f flatPose = new Matrix4f()
/* 40 */       .rotationY(-0.3926991F)
/* 41 */       .rotateX(2.3561945F);
/* 42 */     updateBuffer(Entry.ITEMS_FLAT, flatPose.transformDirection((Vector3fc)DIFFUSE_LIGHT_0, new Vector3f()), flatPose.transformDirection((Vector3fc)DIFFUSE_LIGHT_1, new Vector3f()));
/*    */     
/* 44 */     Matrix4f item3DPose = new Matrix4f()
/* 45 */       .scaling(1.0F, -1.0F, 1.0F)
/* 46 */       .rotateYXZ(1.0821041F, 3.2375858F, 0.0F)
/* 47 */       .rotateYXZ(-0.3926991F, 2.3561945F, 0.0F);
/* 48 */     updateBuffer(Entry.ITEMS_3D, item3DPose.transformDirection((Vector3fc)DIFFUSE_LIGHT_0, new Vector3f()), item3DPose.transformDirection((Vector3fc)DIFFUSE_LIGHT_1, new Vector3f()));
/*    */     
/* 50 */     updateBuffer(Entry.ENTITY_IN_UI, INVENTORY_DIFFUSE_LIGHT_0, INVENTORY_DIFFUSE_LIGHT_1);
/*    */     
/* 52 */     Matrix4f playerSkinPose = new Matrix4f();
/* 53 */     updateBuffer(Entry.PLAYER_SKIN, playerSkinPose.transformDirection((Vector3fc)INVENTORY_DIFFUSE_LIGHT_0, new Vector3f()), playerSkinPose.transformDirection((Vector3fc)INVENTORY_DIFFUSE_LIGHT_1, new Vector3f()));
/*    */   }
/*    */   
/*    */   public void updateLevel(DimensionType.CardinalLightType type) {
/* 57 */     switch (type) { case DEFAULT:
/* 58 */         updateBuffer(Entry.LEVEL, DIFFUSE_LIGHT_0, DIFFUSE_LIGHT_1); break;
/* 59 */       case NETHER: updateBuffer(Entry.LEVEL, NETHER_DIFFUSE_LIGHT_0, NETHER_DIFFUSE_LIGHT_1);
/*    */         break; }
/*    */   
/*    */   }
/*    */   
/* 64 */   private void updateBuffer(Entry entry, Vector3f light0, Vector3f light1) { MemoryStack stack = MemoryStack.stackPush(); 
/* 65 */     try { ByteBuffer byteBuffer = Std140Builder.onStack(stack, UBO_SIZE).putVec3((Vector3fc)light0).putVec3((Vector3fc)light1).get();
/* 66 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(entry.ordinal() * this.paddedSize, this.paddedSize), byteBuffer);
/* 67 */       if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/*    */         try { stack.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 71 */      } public void setupFor(Entry entry) { RenderSystem.setShaderLights(this.buffer.slice(entry.ordinal() * this.paddedSize, UBO_SIZE)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 76 */     this.buffer.close();
/*    */   }
/*    */   
/*    */   public enum Entry {
/* 80 */     LEVEL,
/* 81 */     ITEMS_FLAT,
/* 82 */     ITEMS_3D,
/* 83 */     ENTITY_IN_UI,
/* 84 */     PLAYER_SKIN;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/Lighting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */