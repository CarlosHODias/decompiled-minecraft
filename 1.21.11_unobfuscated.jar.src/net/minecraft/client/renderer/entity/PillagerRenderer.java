/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.illager.Pillager;
/*    */ 
/*    */ public class PillagerRenderer extends IllagerRenderer<Pillager, IllagerRenderState> {
/* 11 */   private static final Identifier PILLAGER = Identifier.withDefaultNamespace("textures/entity/illager/pillager.png");
/*    */   
/*    */   public PillagerRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new IllagerModel(context.bakeLayer(ModelLayers.PILLAGER)), 0.5F);
/*    */     
/* 16 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(IllagerRenderState state) {
/* 21 */     return PILLAGER;
/*    */   }
/*    */ 
/*    */   
/*    */   public IllagerRenderState createRenderState() {
/* 26 */     return new IllagerRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PillagerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */