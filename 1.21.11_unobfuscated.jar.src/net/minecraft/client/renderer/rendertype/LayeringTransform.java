/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.function.Consumer;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fStack;
/*    */ 
/*    */ public class LayeringTransform
/*    */ {
/*    */   private final String name;
/*    */   private final Consumer<Matrix4fStack> modifier;
/*    */   
/*    */   public LayeringTransform(String name, Consumer<Matrix4fStack> modifier) {
/* 14 */     this.name = name;
/* 15 */     this.modifier = modifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return "LayeringTransform[" + this.name + "]";
/*    */   }
/*    */   
/*    */   public Consumer<Matrix4fStack> getModifier() {
/* 24 */     return this.modifier;
/*    */   }
/*    */   
/* 27 */   public static final LayeringTransform NO_LAYERING = new LayeringTransform("no_layering", null);
/*    */   static {
/* 29 */     VIEW_OFFSET_Z_LAYERING = new LayeringTransform("view_offset_z_layering", modelViewStack -> RenderSystem.getProjectionType().applyLayeringTransform((Matrix4f)modelViewStack, 1.0F));
/*    */ 
/*    */ 
/*    */     
/* 33 */     VIEW_OFFSET_Z_LAYERING_FORWARD = new LayeringTransform("view_offset_z_layering_forward", modelViewStack -> RenderSystem.getProjectionType().applyLayeringTransform((Matrix4f)modelViewStack, -1.0F));
/*    */   }
/*    */   
/*    */   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING;
/*    */   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING_FORWARD;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/rendertype/LayeringTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */