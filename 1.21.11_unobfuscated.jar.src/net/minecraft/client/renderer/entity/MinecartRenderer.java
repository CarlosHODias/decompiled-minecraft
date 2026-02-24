/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ 
/*    */ public class MinecartRenderer extends AbstractMinecartRenderer<AbstractMinecart, MinecartRenderState> {
/*    */   public MinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
/*  9 */     super(context, model);
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecartRenderState createRenderState() {
/* 14 */     return new MinecartRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/MinecartRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */