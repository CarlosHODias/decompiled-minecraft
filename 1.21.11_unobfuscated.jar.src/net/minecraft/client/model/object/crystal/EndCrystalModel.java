/*    */ package net.minecraft.client.model.object.crystal;
/*    */ 
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.EndCrystalRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ 
/*    */ public class EndCrystalModel
/*    */   extends EntityModel<EndCrystalRenderState>
/*    */ {
/*    */   private static final String OUTER_GLASS = "outer_glass";
/*    */   private static final String INNER_GLASS = "inner_glass";
/*    */   private static final String BASE = "base";
/* 22 */   private static final float SIN_45 = (float)Math.sin(0.7853981633974483D);
/*    */   
/*    */   public final ModelPart base;
/*    */   public final ModelPart outerGlass;
/*    */   public final ModelPart innerGlass;
/*    */   public final ModelPart cube;
/*    */   
/*    */   public EndCrystalModel(ModelPart root) {
/* 30 */     super(root);
/* 31 */     this.base = root.getChild("base");
/* 32 */     this.outerGlass = root.getChild("outer_glass");
/* 33 */     this.innerGlass = this.outerGlass.getChild("inner_glass");
/* 34 */     this.cube = this.innerGlass.getChild("cube");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 38 */     MeshDefinition mesh = new MeshDefinition();
/* 39 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 41 */     float scale = 0.875F;
/*    */     
/* 43 */     CubeListBuilder glassCube = CubeListBuilder.create()
/* 44 */       .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
/* 45 */     PartDefinition outerGlass = root.addOrReplaceChild("outer_glass", glassCube, PartPose.offset(0.0F, 24.0F, 0.0F));
/* 46 */     PartDefinition innerGlass = outerGlass.addOrReplaceChild("inner_glass", glassCube, PartPose.ZERO.withScale(0.875F));
/* 47 */     innerGlass.addOrReplaceChild("cube", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), 
/* 50 */         PartPose.ZERO.withScale(0.765625F));
/*    */     
/* 52 */     root.addOrReplaceChild("base", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 4.0F, 12.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 58 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(EndCrystalRenderState state) {
/* 63 */     super.setupAnim(state);
/*    */     
/* 65 */     this.base.visible = state.showsBottom;
/*    */     
/* 67 */     float animationSpeed = state.ageInTicks * 3.0F;
/* 68 */     float crystalY = EndCrystalRenderer.getY(state.ageInTicks) * 16.0F;
/*    */     
/* 70 */     this.outerGlass.y += crystalY / 2.0F;
/* 71 */     this.outerGlass.rotateBy(Axis.YP.rotationDegrees(animationSpeed)
/* 72 */         .rotateAxis(1.0471976F, SIN_45, 0.0F, SIN_45));
/*    */     
/* 74 */     this.innerGlass.rotateBy(new Quaternionf().setAngleAxis(1.0471976F, SIN_45, 0.0F, SIN_45)
/* 75 */         .rotateY(animationSpeed * 0.017453292F));
/*    */     
/* 77 */     this.cube.rotateBy(new Quaternionf().setAngleAxis(1.0471976F, SIN_45, 0.0F, SIN_45)
/* 78 */         .rotateY(animationSpeed * 0.017453292F));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/object/crystal/EndCrystalModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */