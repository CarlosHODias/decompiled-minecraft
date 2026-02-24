/*    */ package net.minecraft.client.model.player;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public class PlayerCapeModel
/*    */   extends PlayerModel
/*    */ {
/*    */   private static final String CAPE = "cape";
/*    */   private final ModelPart cape;
/*    */   
/*    */   public PlayerCapeModel(ModelPart root) {
/* 21 */     super(root, false);
/* 22 */     this.cape = this.body.getChild("cape");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createCapeLayer() {
/* 26 */     MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
/* 27 */     PartDefinition root = mesh.getRoot().clearRecursively();
/* 28 */     PartDefinition body = root.getChild("body");
/*    */     
/* 30 */     body.addOrReplaceChild("cape", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, CubeDeformation.NONE, 1.0F, 0.5F), 
/* 33 */         PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 3.1415927F, 0.0F));
/*    */ 
/*    */     
/* 36 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(AvatarRenderState state) {
/* 41 */     super.setupAnim(state);
/*    */     
/* 43 */     this.cape.rotateBy(new Quaternionf()
/* 44 */         .rotateY(-3.1415927F)
/* 45 */         .rotateX((6.0F + state.capeLean / 2.0F + state.capeFlap) * 0.017453292F)
/* 46 */         .rotateZ(state.capeLean2 / 2.0F * 0.017453292F)
/* 47 */         .rotateY((180.0F - state.capeLean2 / 2.0F) * 0.017453292F));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/player/PlayerCapeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */