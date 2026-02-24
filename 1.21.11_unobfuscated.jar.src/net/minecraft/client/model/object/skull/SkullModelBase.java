/*   */ package net.minecraft.client.model.object.skull;
/*   */ 
/*   */ import net.minecraft.client.model.Model;
/*   */ import net.minecraft.client.model.geom.ModelPart;
/*   */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*   */ 
/*   */ public abstract class SkullModelBase extends Model<SkullModelBase.State> {
/*   */   public SkullModelBase(ModelPart root) {
/* 9 */     super(root, RenderTypes::entityTranslucent);
/*   */   }
/*   */   
/*   */   public static class State {
/*   */     public float animationPos;
/*   */     public float yRot;
/*   */     public float xRot;
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/skull/SkullModelBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */