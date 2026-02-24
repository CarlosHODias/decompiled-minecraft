/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ShulkerRenderState;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Shulker;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ShulkerRenderer extends MobRenderer<Shulker, ShulkerRenderState, net.minecraft.client.model.monster.shulker.ShulkerModel> {
/*    */   private static final Identifier DEFAULT_TEXTURE_LOCATION;
/*    */   
/*    */   static {
/* 20 */     DEFAULT_TEXTURE_LOCATION = net.minecraft.client.renderer.Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION.texture().withPath(path -> "textures/" + path + ".png");
/* 21 */     TEXTURE_LOCATION = (Identifier[])net.minecraft.client.renderer.Sheets.SHULKER_TEXTURE_LOCATION.stream().map(location -> location.texture().withPath(())).toArray(x$0 -> new Identifier[x$0]);
/*    */   } private static final Identifier[] TEXTURE_LOCATION;
/*    */   public ShulkerRenderer(EntityRendererProvider.Context context) {
/* 24 */     super(context, new net.minecraft.client.model.monster.shulker.ShulkerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SHULKER)), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getRenderOffset(ShulkerRenderState state) {
/* 29 */     return state.renderOffset;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRender(Shulker entity, Frustum culler, double camX, double camY, double camZ) {
/* 34 */     if (super.shouldRender(entity, culler, camX, camY, camZ)) {
/* 35 */       return true;
/*    */     }
/*    */     
/* 38 */     Vec3 startPos = entity.getRenderPosition(0.0F);
/* 39 */     if (startPos == null) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     EntityType<?> type = entity.getType();
/* 44 */     float halfHeight = type.getHeight() / 2.0F;
/* 45 */     float halfWidth = type.getWidth() / 2.0F;
/*    */     
/* 47 */     Vec3 targetPos = Vec3.atBottomCenterOf((net.minecraft.core.Vec3i)entity.blockPosition());
/* 48 */     return culler.isVisible(new net.minecraft.world.phys.AABB(startPos.x, startPos.y + halfHeight, startPos.z, targetPos.x, targetPos.y + halfHeight, targetPos.z).inflate(halfWidth, halfHeight, halfWidth));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ShulkerRenderState state) {
/* 53 */     return getTextureLocation(state.color);
/*    */   }
/*    */ 
/*    */   
/*    */   public ShulkerRenderState createRenderState() {
/* 58 */     return new ShulkerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Shulker entity, ShulkerRenderState state, float partialTicks) {
/* 63 */     super.extractRenderState(entity, state, partialTicks);
/* 64 */     state.renderOffset = java.util.Objects.<Vec3>requireNonNullElse(entity.getRenderPosition(partialTicks), Vec3.ZERO);
/* 65 */     state.color = entity.getColor();
/* 66 */     state.peekAmount = entity.getClientPeekAmount(partialTicks);
/* 67 */     state.yHeadRot = entity.yHeadRot;
/* 68 */     state.yBodyRot = entity.yBodyRot;
/* 69 */     state.attachFace = entity.getAttachFace();
/*    */   }
/*    */   
/*    */   public static Identifier getTextureLocation(DyeColor color) {
/* 73 */     if (color == null) {
/* 74 */       return DEFAULT_TEXTURE_LOCATION;
/*    */     }
/* 76 */     return TEXTURE_LOCATION[color.getId()];
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(ShulkerRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 81 */     super.setupRotations(state, poseStack, bodyRot + 180.0F, entityScale);
/* 82 */     poseStack.rotateAround((org.joml.Quaternionfc)state.attachFace.getOpposite().getRotation(), 0.0F, 0.5F, 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ShulkerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */