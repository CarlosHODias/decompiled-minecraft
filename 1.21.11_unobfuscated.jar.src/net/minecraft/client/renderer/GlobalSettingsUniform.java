/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class GlobalSettingsUniform
/*    */   implements AutoCloseable {
/* 16 */   public static final int UBO_SIZE = new Std140SizeCalculator()
/* 17 */     .putIVec3()
/* 18 */     .putVec3()
/* 19 */     .putVec2()
/* 20 */     .putFloat()
/* 21 */     .putFloat()
/* 22 */     .putInt()
/* 23 */     .putInt()
/* 24 */     .get();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   private final GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Global Settings UBO", 136, UBO_SIZE);
/*    */ 
/*    */   
/*    */   public void update(int width, int height, double glintAlpha, long gameTime, DeltaTracker deltaTracker, int menuBlurRadius, Camera mainCamera, boolean useRgss) {
/* 33 */     Vec3 cameraPos = mainCamera.position();
/* 34 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 35 */     try { int cameraX = Mth.floor(cameraPos.x);
/* 36 */       int cameraY = Mth.floor(cameraPos.y);
/* 37 */       int cameraZ = Mth.floor(cameraPos.z);
/* 38 */       ByteBuffer data = Std140Builder.onStack(stack, UBO_SIZE)
/* 39 */         .putIVec3(cameraX, cameraY, cameraZ)
/* 40 */         .putVec3((float)(cameraX - cameraPos.x), (float)(cameraY - cameraPos.y), (float)(cameraZ - cameraPos.z))
/* 41 */         .putVec2(width, height)
/* 42 */         .putFloat((float)glintAlpha)
/* 43 */         .putFloat(((float)(gameTime % 24000L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24000.0F)
/* 44 */         .putInt(menuBlurRadius)
/* 45 */         .putInt(useRgss ? 1 : 0)
/* 46 */         .get();
/* 47 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), data);
/* 48 */       if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 49 */         try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  RenderSystem.setGlobalSettingsUniform(this.buffer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/GlobalSettingsUniform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */