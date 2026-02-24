/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.OminousItemSpawner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class OminousItemSpawnerRenderer
/*    */   extends EntityRenderer<OminousItemSpawner, ItemClusterRenderState> {
/*    */   private static final float ROTATION_SPEED = 40.0F;
/* 20 */   private final RandomSource random = RandomSource.create(); private static final int TICKS_SCALING = 50; private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   protected OminousItemSpawnerRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context);
/* 24 */     this.itemModelResolver = context.getItemModelResolver();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemClusterRenderState createRenderState() {
/* 29 */     return new ItemClusterRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(OminousItemSpawner entity, ItemClusterRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/*    */     
/* 36 */     ItemStack item = entity.getItem();
/* 37 */     state.extractItemGroupRenderState((Entity)entity, item, this.itemModelResolver);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ItemClusterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 42 */     if (state.item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     poseStack.pushPose();
/* 47 */     if (state.ageInTicks <= 50.0F) {
/* 48 */       float scale = Math.min(state.ageInTicks, 50.0F) / 50.0F;
/* 49 */       poseStack.scale(scale, scale, scale);
/*    */     } 
/*    */     
/* 52 */     float currentSpin = Mth.wrapDegrees(state.ageInTicks * 40.0F);
/* 53 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(currentSpin));
/* 54 */     ItemEntityRenderer.submitMultipleFromCount(poseStack, submitNodeCollector, 15728880, state, this.random);
/* 55 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/OminousItemSpawnerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */