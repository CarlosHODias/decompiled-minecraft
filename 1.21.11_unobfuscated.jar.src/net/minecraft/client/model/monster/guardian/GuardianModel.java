/*     */ package net.minecraft.client.model.monster.guardian;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.GuardianRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GuardianModel
/*     */   extends EntityModel<GuardianRenderState> {
/*  17 */   public static final MeshTransformer ELDER_GUARDIAN_SCALE = MeshTransformer.scaling(2.35F);
/*     */   
/*  19 */   private static final float[] SPIKE_X_ROT = new float[] { 1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F };
/*  20 */   private static final float[] SPIKE_Y_ROT = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F };
/*  21 */   private static final float[] SPIKE_Z_ROT = new float[] { 0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F };
/*  22 */   private static final float[] SPIKE_X = new float[] { 0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F };
/*  23 */   private static final float[] SPIKE_Y = new float[] { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F };
/*  24 */   private static final float[] SPIKE_Z = new float[] { 8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F };
/*     */   
/*     */   private static final String EYE = "eye";
/*     */   
/*     */   private static final String TAIL_0 = "tail0";
/*     */   private static final String TAIL_1 = "tail1";
/*     */   private static final String TAIL_2 = "tail2";
/*     */   private final ModelPart head;
/*     */   private final ModelPart eye;
/*     */   private final ModelPart[] spikeParts;
/*     */   private final ModelPart[] tailParts;
/*     */   
/*     */   public GuardianModel(ModelPart root) {
/*  37 */     super(root);
/*  38 */     this.spikeParts = new ModelPart[12];
/*     */     
/*  40 */     this.head = root.getChild("head");
/*     */     
/*  42 */     for (int i = 0; i < this.spikeParts.length; i++) {
/*  43 */       this.spikeParts[i] = this.head.getChild(createSpikeName(i));
/*     */     }
/*     */     
/*  46 */     this.eye = this.head.getChild("eye");
/*  47 */     this.tailParts = new ModelPart[3];
/*  48 */     this.tailParts[0] = this.head.getChild("tail0");
/*  49 */     this.tailParts[1] = this.tailParts[0].getChild("tail1");
/*  50 */     this.tailParts[2] = this.tailParts[1].getChild("tail2");
/*     */   }
/*     */   
/*     */   private static String createSpikeName(int i) {
/*  54 */     return "spike" + i;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  58 */     MeshDefinition mesh = new MeshDefinition();
/*  59 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  61 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12.0F, 12.0F, 16.0F)
/*  64 */         .texOffs(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F)
/*  65 */         .texOffs(0, 28).addBox(6.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F, true)
/*  66 */         .texOffs(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 2.0F, 12.0F)
/*  67 */         .texOffs(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12.0F, 2.0F, 12.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  71 */     CubeListBuilder spike = CubeListBuilder.create()
/*  72 */       .texOffs(0, 0).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F);
/*  73 */     for (int i = 0; i < 12; i++) {
/*  74 */       float x = getSpikeX(i, 0.0F, 0.0F);
/*  75 */       float y = getSpikeY(i, 0.0F, 0.0F);
/*  76 */       float z = getSpikeZ(i, 0.0F, 0.0F);
/*  77 */       float xRot = 3.1415927F * SPIKE_X_ROT[i];
/*  78 */       float yRot = 3.1415927F * SPIKE_Y_ROT[i];
/*  79 */       float zRot = 3.1415927F * SPIKE_Z_ROT[i];
/*  80 */       head.addOrReplaceChild(createSpikeName(i), spike, PartPose.offsetAndRotation(x, y, z, xRot, yRot, zRot));
/*     */     } 
/*     */     
/*  83 */     head.addOrReplaceChild("eye", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(8, 0).addBox(-1.0F, 15.0F, 0.0F, 2.0F, 2.0F, 1.0F), 
/*  86 */         PartPose.offset(0.0F, 0.0F, -8.25F));
/*     */     
/*  88 */     PartDefinition tailPart0 = head.addOrReplaceChild("tail0", 
/*  89 */         CubeListBuilder.create()
/*  90 */         .texOffs(40, 0).addBox(-2.0F, 14.0F, 7.0F, 4.0F, 4.0F, 8.0F), PartPose.ZERO);
/*     */     
/*  92 */     PartDefinition tailPart1 = tailPart0.addOrReplaceChild("tail1", 
/*  93 */         CubeListBuilder.create()
/*  94 */         .texOffs(0, 54).addBox(0.0F, 14.0F, 0.0F, 3.0F, 3.0F, 7.0F), 
/*  95 */         PartPose.offset(-1.5F, 0.5F, 14.0F));
/*     */     
/*  97 */     tailPart1.addOrReplaceChild("tail2", 
/*  98 */         CubeListBuilder.create()
/*  99 */         .texOffs(41, 32).addBox(0.0F, 14.0F, 0.0F, 2.0F, 2.0F, 6.0F)
/* 100 */         .texOffs(25, 19).addBox(1.0F, 10.5F, 3.0F, 1.0F, 9.0F, 9.0F), 
/* 101 */         PartPose.offset(0.5F, 0.5F, 6.0F));
/*     */ 
/*     */     
/* 104 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createElderGuardianLayer() {
/* 108 */     return createBodyLayer().apply(ELDER_GUARDIAN_SCALE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(GuardianRenderState state) {
/* 113 */     super.setupAnim(state);
/*     */     
/* 115 */     this.head.yRot = state.yRot * 0.017453292F;
/* 116 */     this.head.xRot = state.xRot * 0.017453292F;
/*     */     
/* 118 */     float withdrawal = (1.0F - state.spikesAnimation) * 0.55F;
/* 119 */     setupSpikes(state.ageInTicks, withdrawal);
/*     */     
/* 121 */     if (state.lookAtPosition != null && state.lookDirection != null) {
/* 122 */       double dy = state.lookAtPosition.y - state.eyePosition.y;
/* 123 */       if (dy > 0.0D) {
/* 124 */         this.eye.y = 0.0F;
/*     */       } else {
/* 126 */         this.eye.y = 1.0F;
/*     */       } 
/*     */       
/* 129 */       Vec3 viewVector = state.lookDirection;
/* 130 */       viewVector = new Vec3(viewVector.x, 0.0D, viewVector.z);
/* 131 */       Vec3 delta = new Vec3(state.eyePosition.x - state.lookAtPosition.x, 0.0D, state.eyePosition.z - state.lookAtPosition.z).normalize().yRot(1.5707964F);
/* 132 */       double dot = viewVector.dot(delta);
/* 133 */       this.eye.x = Mth.sqrt((float)Math.abs(dot)) * 2.0F * (float)Math.signum(dot);
/*     */     } 
/* 135 */     this.eye.visible = true;
/*     */     
/* 137 */     float swim = state.tailAnimation;
/* 138 */     (this.tailParts[0]).yRot = Mth.sin(swim) * 3.1415927F * 0.05F;
/* 139 */     (this.tailParts[1]).yRot = Mth.sin(swim) * 3.1415927F * 0.1F;
/* 140 */     (this.tailParts[2]).yRot = Mth.sin(swim) * 3.1415927F * 0.15F;
/*     */   }
/*     */   
/*     */   private void setupSpikes(float ageInTicks, float withdrawal) {
/* 144 */     for (int i = 0; i < 12; i++) {
/* 145 */       (this.spikeParts[i]).x = getSpikeX(i, ageInTicks, withdrawal);
/* 146 */       (this.spikeParts[i]).y = getSpikeY(i, ageInTicks, withdrawal);
/* 147 */       (this.spikeParts[i]).z = getSpikeZ(i, ageInTicks, withdrawal);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static float getSpikeOffset(int spike, float ageInTicks, float withdrawal) {
/* 152 */     return 1.0F + Mth.cos((ageInTicks * 1.5F + spike)) * 0.01F - withdrawal;
/*     */   }
/*     */   
/*     */   private static float getSpikeX(int spike, float ageInTicks, float withdrawal) {
/* 156 */     return SPIKE_X[spike] * getSpikeOffset(spike, ageInTicks, withdrawal);
/*     */   }
/*     */   
/*     */   private static float getSpikeY(int spike, float ageInTicks, float withdrawal) {
/* 160 */     return 16.0F + SPIKE_Y[spike] * getSpikeOffset(spike, ageInTicks, withdrawal);
/*     */   }
/*     */   
/*     */   private static float getSpikeZ(int spike, float ageInTicks, float withdrawal) {
/* 164 */     return SPIKE_Z[spike] * getSpikeOffset(spike, ageInTicks, withdrawal);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/guardian/GuardianModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */