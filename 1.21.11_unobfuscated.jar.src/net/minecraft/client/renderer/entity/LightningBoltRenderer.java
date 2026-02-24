/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LightningBoltRenderState;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class LightningBoltRenderer extends EntityRenderer<LightningBolt, LightningBoltRenderState> {
/*     */   public LightningBoltRenderer(EntityRendererProvider.Context context) {
/*  15 */     super(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(LightningBoltRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.CameraRenderState camera) {
/*  20 */     float[] xOffs = new float[8];
/*  21 */     float[] zOffs = new float[8];
/*  22 */     float xOff = 0.0F;
/*  23 */     float zOff = 0.0F;
/*     */     
/*  25 */     RandomSource random = RandomSource.create(state.seed);
/*  26 */     for (int h = 7; h >= 0; h--) {
/*  27 */       xOffs[h] = xOff;
/*  28 */       zOffs[h] = zOff;
/*  29 */       xOff += (random.nextInt(11) - 5);
/*  30 */       zOff += (random.nextInt(11) - 5);
/*     */     } 
/*     */ 
/*     */     
/*  34 */     float finalXOff = xOff;
/*  35 */     float finalZOff = zOff;
/*  36 */     submitNodeCollector.submitCustomGeometry(poseStack, net.minecraft.client.renderer.rendertype.RenderTypes.lightning(), (pose, buffer) -> {
/*     */           Matrix4f poseMatrix = pose.pose();
/*     */           for (int r = 0; r < 4; r++) {
/*     */             RandomSource random = RandomSource.create(state.seed);
/*     */             for (int p = 0; p < 3; p++) {
/*     */               int hs = 7, ht = 0;
/*     */               if (p > 0) {
/*     */                 hs = 7 - p;
/*     */               }
/*     */               if (p > 0) {
/*     */                 ht = hs - 2;
/*     */               }
/*     */               float xo0 = xOffs[hs] - finalXOff, zo0 = zOffs[hs] - finalZOff;
/*     */               for (int h = hs; h >= ht; h--) {
/*     */                 float xo1 = xo0, zo1 = zo0;
/*     */                 if (p == 0) {
/*     */                   xo0 += (random.nextInt(11) - 5);
/*     */                   zo0 += (random.nextInt(11) - 5);
/*     */                 } else {
/*     */                   xo0 += (random.nextInt(31) - 15);
/*     */                   zo0 += (random.nextInt(31) - 15);
/*     */                 } 
/*     */                 float br = 0.5F, boltRed = 0.45F, boltGreen = 0.45F, boltBlue = 0.5F, rr1 = 0.1F + r * 0.2F;
/*     */                 if (p == 0) {
/*     */                   rr1 *= h * 0.1F + 1.0F;
/*     */                 }
/*     */                 float rr2 = 0.1F + r * 0.2F;
/*     */                 if (p == 0) {
/*     */                   rr2 *= (h - 1.0F) * 0.1F + 1.0F;
/*     */                 }
/*     */                 quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, 0.45F, 0.45F, 0.5F, rr1, rr2, false, false, true, false);
/*     */                 quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, 0.45F, 0.45F, 0.5F, rr1, rr2, true, false, true, true);
/*     */                 quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, 0.45F, 0.45F, 0.5F, rr1, rr2, true, true, false, true);
/*     */                 quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, 0.45F, 0.45F, 0.5F, rr1, rr2, false, true, false, false);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void quad(Matrix4f pose, VertexConsumer buffer, float xo0, float zo0, int h, float xo1, float zo1, float boltRed, float boltGreen, float boltBlue, float rr1, float rr2, boolean px1, boolean pz1, boolean px2, boolean pz2) {
/*  89 */     buffer.addVertex((Matrix4fc)pose, xo0 + (
/*     */         
/*  91 */         px1 ? rr2 : -rr2), (h * 16), zo0 + (
/*     */         
/*  93 */         pz1 ? rr2 : -rr2))
/*     */       
/*  95 */       .setColor(boltRed, boltGreen, boltBlue, 0.3F);
/*     */ 
/*     */     
/*  98 */     buffer.addVertex((Matrix4fc)pose, xo1 + (
/*     */         
/* 100 */         px1 ? rr1 : -rr1), ((h + 1) * 16), zo1 + (
/*     */         
/* 102 */         pz1 ? rr1 : -rr1))
/*     */       
/* 104 */       .setColor(boltRed, boltGreen, boltBlue, 0.3F);
/*     */ 
/*     */     
/* 107 */     buffer.addVertex((Matrix4fc)pose, xo1 + (
/*     */         
/* 109 */         px2 ? rr1 : -rr1), ((h + 1) * 16), zo1 + (
/*     */         
/* 111 */         pz2 ? rr1 : -rr1))
/*     */       
/* 113 */       .setColor(boltRed, boltGreen, boltBlue, 0.3F);
/*     */ 
/*     */     
/* 116 */     buffer.addVertex((Matrix4fc)pose, xo0 + (
/*     */         
/* 118 */         px2 ? rr2 : -rr2), (h * 16), zo0 + (
/*     */         
/* 120 */         pz2 ? rr2 : -rr2))
/*     */       
/* 122 */       .setColor(boltRed, boltGreen, boltBlue, 0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public LightningBoltRenderState createRenderState() {
/* 127 */     return new LightningBoltRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(LightningBolt entity, LightningBoltRenderState state, float partialTicks) {
/* 132 */     super.extractRenderState(entity, state, partialTicks);
/* 133 */     state.seed = entity.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean affectedByCulling(LightningBolt entity) {
/* 138 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/LightningBoltRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */