/*    */ package net.minecraft.client.model.monster.guardian;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class GuardianParticleModel extends Model<Unit> {
/*    */   public GuardianParticleModel(ModelPart root) {
/* 10 */     super(root, RenderTypes::entityCutoutNoCull);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/guardian/GuardianParticleModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */