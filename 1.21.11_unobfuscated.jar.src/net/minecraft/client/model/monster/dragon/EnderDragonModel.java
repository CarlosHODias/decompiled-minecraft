/*     */ package net.minecraft.client.model.monster.dragon;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.boss.enderdragon.DragonFlightHistory;
/*     */ 
/*     */ public class EnderDragonModel
/*     */   extends EntityModel<EnderDragonRenderState>
/*     */ {
/*     */   private static final int NECK_PART_COUNT = 5;
/*     */   private static final int TAIL_PART_COUNT = 12;
/*     */   private final ModelPart head;
/*  20 */   private final ModelPart[] neckParts = new ModelPart[5];
/*  21 */   private final ModelPart[] tailParts = new ModelPart[12];
/*     */   
/*     */   private final ModelPart jaw;
/*     */   
/*     */   private final ModelPart body;
/*     */   
/*     */   private final ModelPart leftWing;
/*     */   
/*     */   private final ModelPart leftWingTip;
/*     */   
/*     */   private final ModelPart leftFrontLeg;
/*     */   
/*     */   private final ModelPart leftFrontLegTip;
/*     */   private final ModelPart leftFrontFoot;
/*     */   private final ModelPart leftRearLeg;
/*     */   private final ModelPart leftRearLegTip;
/*     */   private final ModelPart leftRearFoot;
/*     */   private final ModelPart rightWing;
/*     */   private final ModelPart rightWingTip;
/*     */   private final ModelPart rightFrontLeg;
/*     */   private final ModelPart rightFrontLegTip;
/*     */   private final ModelPart rightFrontFoot;
/*     */   private final ModelPart rightRearLeg;
/*     */   private final ModelPart rightRearLegTip;
/*     */   private final ModelPart rightRearFoot;
/*     */   
/*     */   private static String neckName(int index) {
/*  48 */     return "neck" + index;
/*     */   }
/*     */   
/*     */   private static String tailName(int index) {
/*  52 */     return "tail" + index;
/*     */   }
/*     */   
/*     */   public EnderDragonModel(ModelPart root) {
/*  56 */     super(root);
/*  57 */     this.head = root.getChild("head");
/*  58 */     this.jaw = this.head.getChild("jaw");
/*  59 */     for (int i = 0; i < this.neckParts.length; i++) {
/*  60 */       this.neckParts[i] = root.getChild(neckName(i));
/*     */     }
/*  62 */     for (int j = 0; j < this.tailParts.length; j++) {
/*  63 */       this.tailParts[j] = root.getChild(tailName(j));
/*     */     }
/*  65 */     this.body = root.getChild("body");
/*  66 */     this.leftWing = this.body.getChild("left_wing");
/*  67 */     this.leftWingTip = this.leftWing.getChild("left_wing_tip");
/*  68 */     this.leftFrontLeg = this.body.getChild("left_front_leg");
/*  69 */     this.leftFrontLegTip = this.leftFrontLeg.getChild("left_front_leg_tip");
/*  70 */     this.leftFrontFoot = this.leftFrontLegTip.getChild("left_front_foot");
/*  71 */     this.leftRearLeg = this.body.getChild("left_hind_leg");
/*  72 */     this.leftRearLegTip = this.leftRearLeg.getChild("left_hind_leg_tip");
/*  73 */     this.leftRearFoot = this.leftRearLegTip.getChild("left_hind_foot");
/*  74 */     this.rightWing = this.body.getChild("right_wing");
/*  75 */     this.rightWingTip = this.rightWing.getChild("right_wing_tip");
/*  76 */     this.rightFrontLeg = this.body.getChild("right_front_leg");
/*  77 */     this.rightFrontLegTip = this.rightFrontLeg.getChild("right_front_leg_tip");
/*  78 */     this.rightFrontFoot = this.rightFrontLegTip.getChild("right_front_foot");
/*  79 */     this.rightRearLeg = this.body.getChild("right_hind_leg");
/*  80 */     this.rightRearLegTip = this.rightRearLeg.getChild("right_hind_leg_tip");
/*  81 */     this.rightRearFoot = this.rightRearLegTip.getChild("right_hind_foot");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  85 */     MeshDefinition mesh = new MeshDefinition();
/*  86 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  88 */     float zo = -16.0F;
/*  89 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16, 176, 44)
/*  92 */         .addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16, 112, 30)
/*  93 */         .mirror()
/*  94 */         .addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0)
/*  95 */         .addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0)
/*  96 */         .mirror()
/*  97 */         .addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0)
/*  98 */         .addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0), 
/*  99 */         PartPose.offset(0.0F, 20.0F, -62.0F));
/*     */     
/* 101 */     head.addOrReplaceChild("jaw", 
/* 102 */         CubeListBuilder.create()
/* 103 */         .addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16, 176, 65), 
/* 104 */         PartPose.offset(0.0F, 4.0F, -8.0F));
/*     */ 
/*     */     
/* 107 */     CubeListBuilder spineCubes = CubeListBuilder.create()
/* 108 */       .addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10, 192, 104)
/* 109 */       .addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6, 48, 0);
/* 110 */     for (int i = 0; i < 5; i++) {
/* 111 */       root.addOrReplaceChild(neckName(i), spineCubes, PartPose.offset(0.0F, 20.0F, -12.0F - i * 10.0F));
/*     */     }
/* 113 */     for (int j = 0; j < 12; j++) {
/* 114 */       root.addOrReplaceChild(tailName(j), spineCubes, PartPose.offset(0.0F, 10.0F, 60.0F + j * 10.0F));
/*     */     }
/*     */     
/* 117 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 118 */         CubeListBuilder.create()
/* 119 */         .addBox("body", -12.0F, 1.0F, -16.0F, 24, 24, 64, 0, 0)
/* 120 */         .addBox("scale", -1.0F, -5.0F, -10.0F, 2, 6, 12, 220, 53)
/* 121 */         .addBox("scale", -1.0F, -5.0F, 10.0F, 2, 6, 12, 220, 53)
/* 122 */         .addBox("scale", -1.0F, -5.0F, 30.0F, 2, 6, 12, 220, 53), 
/* 123 */         PartPose.offset(0.0F, 3.0F, 8.0F));
/*     */ 
/*     */     
/* 126 */     PartDefinition leftWing = body.addOrReplaceChild("left_wing", 
/* 127 */         CubeListBuilder.create()
/* 128 */         .mirror()
/* 129 */         .addBox("bone", 0.0F, -4.0F, -4.0F, 56, 8, 8, 112, 88)
/* 130 */         .addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, -56, 88), 
/* 131 */         PartPose.offset(12.0F, 2.0F, -6.0F));
/*     */     
/* 133 */     leftWing.addOrReplaceChild("left_wing_tip", 
/* 134 */         CubeListBuilder.create()
/* 135 */         .mirror()
/* 136 */         .addBox("bone", 0.0F, -2.0F, -2.0F, 56, 4, 4, 112, 136)
/* 137 */         .addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, -56, 144), 
/* 138 */         PartPose.offset(56.0F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 141 */     PartDefinition leftFrontLeg = body.addOrReplaceChild("left_front_leg", 
/* 142 */         CubeListBuilder.create()
/* 143 */         .addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 112, 104), 
/* 144 */         PartPose.offsetAndRotation(12.0F, 17.0F, -6.0F, 1.3F, 0.0F, 0.0F));
/*     */     
/* 146 */     PartDefinition leftFrontLegTip = leftFrontLeg.addOrReplaceChild("left_front_leg_tip", 
/* 147 */         CubeListBuilder.create()
/* 148 */         .addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 226, 138), 
/* 149 */         PartPose.offsetAndRotation(0.0F, 20.0F, -1.0F, -0.5F, 0.0F, 0.0F));
/*     */     
/* 151 */     leftFrontLegTip.addOrReplaceChild("left_front_foot", 
/* 152 */         CubeListBuilder.create()
/* 153 */         .addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 144, 104), 
/* 154 */         PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.75F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 157 */     PartDefinition leftRearLeg = body.addOrReplaceChild("left_hind_leg", 
/* 158 */         CubeListBuilder.create()
/* 159 */         .addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0, 0), 
/* 160 */         PartPose.offsetAndRotation(16.0F, 13.0F, 34.0F, 1.0F, 0.0F, 0.0F));
/*     */     
/* 162 */     PartDefinition leftRearLegTip = leftRearLeg.addOrReplaceChild("left_hind_leg_tip", 
/* 163 */         CubeListBuilder.create()
/* 164 */         .addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 196, 0), 
/* 165 */         PartPose.offsetAndRotation(0.0F, 32.0F, -4.0F, 0.5F, 0.0F, 0.0F));
/*     */     
/* 167 */     leftRearLegTip.addOrReplaceChild("left_hind_foot", 
/* 168 */         CubeListBuilder.create()
/* 169 */         .addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 112, 0), 
/* 170 */         PartPose.offsetAndRotation(0.0F, 31.0F, 4.0F, 0.75F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 173 */     PartDefinition rightWing = body.addOrReplaceChild("right_wing", 
/* 174 */         CubeListBuilder.create()
/* 175 */         .addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8, 112, 88)
/* 176 */         .addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, -56, 88), 
/* 177 */         PartPose.offset(-12.0F, 2.0F, -6.0F));
/*     */     
/* 179 */     rightWing.addOrReplaceChild("right_wing_tip", 
/* 180 */         CubeListBuilder.create()
/* 181 */         .addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4, 112, 136)
/* 182 */         .addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, -56, 144), 
/* 183 */         PartPose.offset(-56.0F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 186 */     PartDefinition rightFrontLeg = body.addOrReplaceChild("right_front_leg", 
/* 187 */         CubeListBuilder.create()
/* 188 */         .addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 112, 104), 
/* 189 */         PartPose.offsetAndRotation(-12.0F, 17.0F, -6.0F, 1.3F, 0.0F, 0.0F));
/*     */     
/* 191 */     PartDefinition rightFrontLegTip = rightFrontLeg.addOrReplaceChild("right_front_leg_tip", 
/* 192 */         CubeListBuilder.create()
/* 193 */         .addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 226, 138), 
/* 194 */         PartPose.offsetAndRotation(0.0F, 20.0F, -1.0F, -0.5F, 0.0F, 0.0F));
/*     */     
/* 196 */     rightFrontLegTip.addOrReplaceChild("right_front_foot", 
/* 197 */         CubeListBuilder.create()
/* 198 */         .addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 144, 104), 
/* 199 */         PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.75F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 202 */     PartDefinition rightRearLeg = body.addOrReplaceChild("right_hind_leg", 
/* 203 */         CubeListBuilder.create()
/* 204 */         .addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0, 0), 
/* 205 */         PartPose.offsetAndRotation(-16.0F, 13.0F, 34.0F, 1.0F, 0.0F, 0.0F));
/*     */     
/* 207 */     PartDefinition rightRearLegTip = rightRearLeg.addOrReplaceChild("right_hind_leg_tip", 
/* 208 */         CubeListBuilder.create()
/* 209 */         .addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 196, 0), 
/* 210 */         PartPose.offsetAndRotation(0.0F, 32.0F, -4.0F, 0.5F, 0.0F, 0.0F));
/*     */     
/* 212 */     rightRearLegTip.addOrReplaceChild("right_hind_foot", 
/* 213 */         CubeListBuilder.create()
/* 214 */         .addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 112, 0), 
/* 215 */         PartPose.offsetAndRotation(0.0F, 31.0F, 4.0F, 0.75F, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 218 */     return LayerDefinition.create(mesh, 256, 256);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupAnim(EnderDragonRenderState state) {
/* 223 */     super.setupAnim(state);
/*     */     
/* 225 */     float flapTime = state.flapTime * 6.2831855F;
/* 226 */     this.jaw.xRot = (Mth.sin(flapTime) + 1.0F) * 0.2F;
/*     */     
/* 228 */     float bounce = Mth.sin((flapTime - 1.0F)) + 1.0F;
/* 229 */     bounce = (bounce * bounce + bounce * 2.0F) * 0.05F;
/*     */     
/* 231 */     this.root.y = (bounce - 2.0F) * 16.0F;
/* 232 */     this.root.z = -48.0F;
/* 233 */     this.root.xRot = bounce * 2.0F * 0.017453292F;
/*     */     
/* 235 */     float xx = (this.neckParts[0]).x;
/* 236 */     float yy = (this.neckParts[0]).y;
/* 237 */     float zz = (this.neckParts[0]).z;
/*     */     
/* 239 */     float rotScale = 1.5F;
/*     */     
/* 241 */     DragonFlightHistory.Sample start = state.getHistoricalPos(6);
/*     */     
/* 243 */     float rot2 = Mth.wrapDegrees(state.getHistoricalPos(5).yRot() - state.getHistoricalPos(10).yRot());
/* 244 */     float rot = Mth.wrapDegrees(state.getHistoricalPos(5).yRot() + rot2 / 2.0F);
/*     */     
/* 246 */     for (int i = 0; i < 5; i++) {
/* 247 */       ModelPart neck = this.neckParts[i];
/* 248 */       DragonFlightHistory.Sample point = state.getHistoricalPos(5 - i);
/* 249 */       float neckXRot = Mth.cos((i * 0.45F + flapTime)) * 0.15F;
/* 250 */       neck.yRot = Mth.wrapDegrees(point.yRot() - start.yRot()) * 0.017453292F * 1.5F;
/* 251 */       neck.xRot = neckXRot + state.getHeadPartYOffset(i, start, point) * 0.017453292F * 1.5F * 5.0F;
/* 252 */       neck.zRot = -Mth.wrapDegrees(point.yRot() - rot) * 0.017453292F * 1.5F;
/*     */       
/* 254 */       neck.y = yy;
/* 255 */       neck.z = zz;
/* 256 */       neck.x = xx;
/* 257 */       xx -= Mth.sin(neck.yRot) * Mth.cos(neck.xRot) * 10.0F;
/* 258 */       yy += Mth.sin(neck.xRot) * 10.0F;
/* 259 */       zz -= Mth.cos(neck.yRot) * Mth.cos(neck.xRot) * 10.0F;
/*     */     } 
/*     */     
/* 262 */     this.head.y = yy;
/* 263 */     this.head.z = zz;
/* 264 */     this.head.x = xx;
/*     */     
/* 266 */     DragonFlightHistory.Sample current = state.getHistoricalPos(0);
/* 267 */     this.head.yRot = Mth.wrapDegrees(current.yRot() - start.yRot()) * 0.017453292F;
/* 268 */     this.head.xRot = Mth.wrapDegrees(state.getHeadPartYOffset(6, start, current)) * 0.017453292F * 1.5F * 5.0F;
/* 269 */     this.head.zRot = -Mth.wrapDegrees(current.yRot() - rot) * 0.017453292F;
/*     */     
/* 271 */     this.body.zRot = -rot2 * 1.5F * 0.017453292F;
/* 272 */     this.leftWing.xRot = 0.125F - Mth.cos(flapTime) * 0.2F;
/* 273 */     this.leftWing.yRot = -0.25F;
/* 274 */     this.leftWing.zRot = -(Mth.sin(flapTime) + 0.125F) * 0.8F;
/* 275 */     this.leftWingTip.zRot = (Mth.sin((flapTime + 2.0F)) + 0.5F) * 0.75F;
/*     */     
/* 277 */     this.rightWing.xRot = this.leftWing.xRot;
/* 278 */     this.rightWing.yRot = -this.leftWing.yRot;
/* 279 */     this.rightWing.zRot = -this.leftWing.zRot;
/* 280 */     this.rightWingTip.zRot = -this.leftWingTip.zRot;
/*     */     
/* 282 */     poseLimbs(bounce, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftRearLeg, this.leftRearLegTip, this.leftRearFoot);
/* 283 */     poseLimbs(bounce, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightRearLeg, this.rightRearLegTip, this.rightRearFoot);
/*     */     
/* 285 */     float tailXRot = 0.0F;
/* 286 */     yy = (this.tailParts[0]).y;
/* 287 */     zz = (this.tailParts[0]).z;
/* 288 */     xx = (this.tailParts[0]).x;
/* 289 */     start = state.getHistoricalPos(11);
/* 290 */     for (int j = 0; j < 12; j++) {
/* 291 */       DragonFlightHistory.Sample point = state.getHistoricalPos(12 + j);
/* 292 */       tailXRot += Mth.sin((j * 0.45F + flapTime)) * 0.05F;
/* 293 */       ModelPart tail = this.tailParts[j];
/* 294 */       tail.yRot = (Mth.wrapDegrees(point.yRot() - start.yRot()) * 1.5F + 180.0F) * 0.017453292F;
/* 295 */       tail.xRot = tailXRot + (float)(point.y() - start.y()) * 0.017453292F * 1.5F * 5.0F;
/* 296 */       tail.zRot = Mth.wrapDegrees(point.yRot() - rot) * 0.017453292F * 1.5F;
/* 297 */       tail.y = yy;
/* 298 */       tail.z = zz;
/* 299 */       tail.x = xx;
/* 300 */       yy += Mth.sin(tail.xRot) * 10.0F;
/* 301 */       zz -= Mth.cos(tail.yRot) * Mth.cos(tail.xRot) * 10.0F;
/* 302 */       xx -= Mth.sin(tail.yRot) * Mth.cos(tail.xRot) * 10.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseLimbs(float bounce, ModelPart frontLeg, ModelPart frontLegTip, ModelPart frontFoot, ModelPart rearLeg, ModelPart rearLegTip, ModelPart rearFoot) {
/* 307 */     rearLeg.xRot = 1.0F + bounce * 0.1F;
/* 308 */     rearLegTip.xRot = 0.5F + bounce * 0.1F;
/* 309 */     rearFoot.xRot = 0.75F + bounce * 0.1F;
/*     */     
/* 311 */     frontLeg.xRot = 1.3F + bounce * 0.1F;
/* 312 */     frontLegTip.xRot = -0.5F - bounce * 0.1F;
/* 313 */     frontFoot.xRot = 0.75F + bounce * 0.1F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/dragon/EnderDragonModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */