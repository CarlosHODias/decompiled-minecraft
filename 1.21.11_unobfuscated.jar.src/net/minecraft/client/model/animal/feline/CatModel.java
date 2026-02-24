/*    */ package net.minecraft.client.model.animal.feline;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ 
/*    */ public class CatModel extends FelineModel<CatRenderState> {
/*  8 */   public static final MeshTransformer CAT_TRANSFORMER = MeshTransformer.scaling(0.8F);
/*    */   
/*    */   public CatModel(ModelPart root) {
/* 11 */     super(root);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/animal/feline/CatModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */