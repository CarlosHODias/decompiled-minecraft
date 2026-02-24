/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class NoopRenderer<T extends Entity> extends EntityRenderer<T, EntityRenderState> {
/*    */   public NoopRenderer(EntityRendererProvider.Context context) {
/*  8 */     super(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 13 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/NoopRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */