/*    */ package net.minecraft.client.model.object.statue;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class CopperGolemStatueModel
/*    */   extends Model<Direction> {
/*    */   public CopperGolemStatueModel(ModelPart root) {
/* 11 */     super(root, RenderTypes::entityCutoutNoCull);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(Direction direction) {
/* 16 */     this.root.y = 0.0F;
/* 17 */     this.root.yRot = direction.getOpposite().toYRot() * 0.017453292F;
/* 18 */     this.root.zRot = 3.1415927F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/statue/CopperGolemStatueModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */