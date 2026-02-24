/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public abstract class EntityModel<T extends EntityRenderState>
/*    */   extends Model<T> {
/*    */   public static final float MODEL_Y_OFFSET = -1.501F;
/*    */   
/*    */   protected EntityModel(ModelPart root) {
/* 15 */     this(root, RenderTypes::entityCutoutNoCull);
/*    */   }
/*    */   
/*    */   protected EntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 19 */     super(root, renderType);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/EntityModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */