/*     */ package net.minecraft.client.model.monster.blaze;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BlazeModel
/*     */   extends EntityModel<LivingEntityRenderState>
/*     */ {
/*     */   private final ModelPart[] upperBodyParts;
/*     */   private final ModelPart head;
/*     */   
/*     */   public BlazeModel(ModelPart root) {
/*  21 */     super(root);
/*  22 */     this.head = root.getChild("head");
/*  23 */     this.upperBodyParts = new ModelPart[12];
/*  24 */     Arrays.setAll(this.upperBodyParts, i -> root.getChild(getPartName(i)));
/*     */   }
/*     */   
/*     */   private static String getPartName(int i) {
/*  28 */     return "part" + i;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  32 */     MeshDefinition mesh = new MeshDefinition();
/*  33 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  35 */     root.addOrReplaceChild("head", 
/*  36 */         CubeListBuilder.create()
/*  37 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     float angle = 0.0F;
/*  43 */     CubeListBuilder rod = CubeListBuilder.create()
/*  44 */       .texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F);
/*     */     
/*  46 */     for (int i = 0; i < 4; i++) {
/*  47 */       float x = Mth.cos(angle) * 9.0F;
/*  48 */       float y = -2.0F + Mth.cos(((i * 2) * 0.25F));
/*  49 */       float z = Mth.sin(angle) * 9.0F;
/*  50 */       root.addOrReplaceChild(getPartName(i), rod, PartPose.offset(x, y, z));
/*  51 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/*  54 */     angle = 0.7853982F;
/*  55 */     for (int k = 4; k < 8; k++) {
/*  56 */       float x = Mth.cos(angle) * 7.0F;
/*  57 */       float y = 2.0F + Mth.cos(((k * 2) * 0.25F));
/*  58 */       float z = Mth.sin(angle) * 7.0F;
/*  59 */       root.addOrReplaceChild(getPartName(k), rod, PartPose.offset(x, y, z));
/*  60 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/*  63 */     angle = 0.47123894F;
/*  64 */     for (int j = 8; j < 12; j++) {
/*  65 */       float x = Mth.cos(angle) * 5.0F;
/*  66 */       float y = 11.0F + Mth.cos((j * 1.5F * 0.5F));
/*  67 */       float z = Mth.sin(angle) * 5.0F;
/*  68 */       root.addOrReplaceChild(getPartName(j), rod, PartPose.offset(x, y, z));
/*  69 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/*  72 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(LivingEntityRenderState state) {
/*  77 */     super.setupAnim(state);
/*     */     
/*  79 */     float angle = state.ageInTicks * 3.1415927F * -0.1F;
/*  80 */     for (int i = 0; i < 4; i++) {
/*  81 */       (this.upperBodyParts[i]).y = -2.0F + Mth.cos((((i * 2) + state.ageInTicks) * 0.25F));
/*  82 */       (this.upperBodyParts[i]).x = Mth.cos(angle) * 9.0F;
/*  83 */       (this.upperBodyParts[i]).z = Mth.sin(angle) * 9.0F;
/*  84 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/*  87 */     angle = 0.7853982F + state.ageInTicks * 3.1415927F * 0.03F;
/*  88 */     for (int k = 4; k < 8; k++) {
/*  89 */       (this.upperBodyParts[k]).y = 2.0F + Mth.cos((((k * 2) + state.ageInTicks) * 0.25F));
/*  90 */       (this.upperBodyParts[k]).x = Mth.cos(angle) * 7.0F;
/*  91 */       (this.upperBodyParts[k]).z = Mth.sin(angle) * 7.0F;
/*  92 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/*  95 */     angle = 0.47123894F + state.ageInTicks * 3.1415927F * -0.05F;
/*  96 */     for (int j = 8; j < 12; j++) {
/*  97 */       (this.upperBodyParts[j]).y = 11.0F + Mth.cos(((j * 1.5F + state.ageInTicks) * 0.5F));
/*  98 */       (this.upperBodyParts[j]).x = Mth.cos(angle) * 5.0F;
/*  99 */       (this.upperBodyParts[j]).z = Mth.sin(angle) * 5.0F;
/* 100 */       angle += 1.5707964F;
/*     */     } 
/*     */     
/* 103 */     this.head.yRot = state.yRot * 0.017453292F;
/* 104 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/blaze/BlazeModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */