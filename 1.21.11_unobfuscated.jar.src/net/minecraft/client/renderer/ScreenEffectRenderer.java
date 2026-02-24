/*     */ package net.minecraft.client.renderer;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ScreenEffectRenderer {
/*  30 */   private static final Identifier UNDERWATER_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final MaterialSet materials;
/*     */   
/*     */   private final MultiBufferSource bufferSource;
/*     */   public static final int ITEM_ACTIVATION_ANIMATION_LENGTH = 40;
/*     */   private ItemStack itemActivationItem;
/*     */   private int itemActivationTicks;
/*     */   private float itemActivationOffX;
/*     */   private float itemActivationOffY;
/*     */   
/*     */   public ScreenEffectRenderer(Minecraft minecraft, MaterialSet materials, MultiBufferSource bufferSource) {
/*  44 */     this.minecraft = minecraft;
/*  45 */     this.materials = materials;
/*  46 */     this.bufferSource = bufferSource;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  50 */     if (this.itemActivationTicks > 0) {
/*  51 */       this.itemActivationTicks--;
/*  52 */       if (this.itemActivationTicks == 0) {
/*  53 */         this.itemActivationItem = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderScreenEffect(boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector) {
/*  59 */     PoseStack poseStack = new PoseStack();
/*  60 */     LocalPlayer localPlayer = this.minecraft.player;
/*  61 */     if (this.minecraft.options.getCameraType().isFirstPerson() && !isSleeping) {
/*  62 */       if (!((Player)localPlayer).noPhysics) {
/*  63 */         BlockState blockState = getViewBlockingState((Player)localPlayer);
/*  64 */         if (blockState != null) {
/*  65 */           renderTex(this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState), poseStack, this.bufferSource);
/*     */         }
/*     */       } 
/*     */       
/*  69 */       if (!this.minecraft.player.isSpectator()) {
/*  70 */         if (this.minecraft.player.isEyeInFluid(FluidTags.WATER)) {
/*  71 */           renderWater(this.minecraft, poseStack, this.bufferSource);
/*     */         }
/*     */         
/*  74 */         if (this.minecraft.player.isOnFire()) {
/*  75 */           TextureAtlasSprite fireSprite = this.materials.get(ModelBakery.FIRE_1);
/*  76 */           renderFire(poseStack, this.bufferSource, fireSprite);
/*     */         } 
/*     */       } 
/*     */     } 
/*  80 */     if (!this.minecraft.options.hideGui) {
/*  81 */       renderItemActivationAnimation(poseStack, partialTicks, submitNodeCollector);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderItemActivationAnimation(PoseStack poseStack, float partialTicks, SubmitNodeCollector submitNodeCollector) {
/*  86 */     if (this.itemActivationItem == null || this.itemActivationTicks <= 0) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     int tick = 40 - this.itemActivationTicks;
/*  91 */     float scale = (tick + partialTicks) / 40.0F;
/*  92 */     float ts = scale * scale;
/*  93 */     float tc = scale * ts;
/*  94 */     float smoothScale = 10.25F * tc * ts - 24.95F * ts * ts + 25.5F * tc - 13.8F * ts + 4.0F * scale;
/*  95 */     float piScale = smoothScale * 3.1415927F;
/*     */     
/*  97 */     float aspectRatio = this.minecraft.getWindow().getWidth() / this.minecraft.getWindow().getHeight();
/*     */     
/*  99 */     float offX = this.itemActivationOffX * 0.3F * aspectRatio;
/* 100 */     float offY = this.itemActivationOffY * 0.3F;
/*     */     
/* 102 */     poseStack.pushPose();
/* 103 */     poseStack.translate(offX * Mth.abs(Mth.sin((piScale * 2.0F))), offY * Mth.abs(Mth.sin((piScale * 2.0F))), -10.0F + 9.0F * Mth.sin(piScale));
/* 104 */     float size = 0.8F;
/* 105 */     poseStack.scale(0.8F, 0.8F, 0.8F);
/* 106 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(piScale))));
/* 107 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(6.0F * Mth.cos((scale * 8.0F))));
/* 108 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(6.0F * Mth.cos((scale * 8.0F))));
/*     */     
/* 110 */     this.minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/* 111 */     ItemStackRenderState itemState = new ItemStackRenderState();
/* 112 */     this.minecraft.getItemModelResolver().updateForTopItem(itemState, this.itemActivationItem, ItemDisplayContext.FIXED, (net.minecraft.world.level.Level)this.minecraft.level, null, 0);
/* 113 */     itemState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
/*     */     
/* 115 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void resetItemActivation() {
/* 119 */     this.itemActivationItem = null;
/*     */   }
/*     */   
/*     */   public void displayItemActivation(ItemStack itemStack, RandomSource random) {
/* 123 */     this.itemActivationItem = itemStack;
/* 124 */     this.itemActivationTicks = 40;
/* 125 */     this.itemActivationOffX = random.nextFloat() * 2.0F - 1.0F;
/* 126 */     this.itemActivationOffY = random.nextFloat() * 2.0F - 1.0F;
/*     */   }
/*     */   
/*     */   private static BlockState getViewBlockingState(Player player) {
/* 130 */     BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
/* 131 */     for (int i = 0; i < 8; i++) {
/* 132 */       double xo = player.getX() + ((((i >> 0) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
/* 133 */       double yo = player.getEyeY() + ((((i >> 1) % 2) - 0.5F) * 0.1F * player.getScale());
/* 134 */       double zo = player.getZ() + ((((i >> 2) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
/*     */       
/* 136 */       testPos.set(xo, yo, zo);
/* 137 */       BlockState blockState = player.level().getBlockState((BlockPos)testPos);
/* 138 */       if (blockState.getRenderShape() != net.minecraft.world.level.block.RenderShape.INVISIBLE && blockState.isViewBlocking((BlockGetter)player.level(), (BlockPos)testPos)) {
/* 139 */         return blockState;
/*     */       }
/*     */     } 
/* 142 */     return null;
/*     */   }
/*     */   
/*     */   private static void renderTex(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource bufferSource) {
/* 146 */     float br = 0.1F;
/* 147 */     int color = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.1F);
/*     */     
/* 149 */     float x0 = -1.0F;
/* 150 */     float x1 = 1.0F;
/* 151 */     float y0 = -1.0F;
/* 152 */     float y1 = 1.0F;
/* 153 */     float z0 = -0.5F;
/*     */     
/* 155 */     float u0 = sprite.getU0();
/* 156 */     float u1 = sprite.getU1();
/* 157 */     float v0 = sprite.getV0();
/* 158 */     float v1 = sprite.getV1();
/*     */     
/* 160 */     Matrix4f pose = poseStack.last().pose();
/*     */     
/* 162 */     VertexConsumer builder = bufferSource.getBuffer(RenderTypes.blockScreenEffect(sprite.atlasLocation()));
/* 163 */     builder.addVertex((Matrix4fc)pose, -1.0F, -1.0F, -0.5F).setUv(u1, v1).setColor(color);
/* 164 */     builder.addVertex((Matrix4fc)pose, 1.0F, -1.0F, -0.5F).setUv(u0, v1).setColor(color);
/* 165 */     builder.addVertex((Matrix4fc)pose, 1.0F, 1.0F, -0.5F).setUv(u0, v0).setColor(color);
/* 166 */     builder.addVertex((Matrix4fc)pose, -1.0F, 1.0F, -0.5F).setUv(u1, v0).setColor(color);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void renderWater(Minecraft minecraft, PoseStack poseStack, MultiBufferSource bufferSource) {
/* 171 */     BlockPos pos = BlockPos.containing(minecraft.player.getX(), minecraft.player.getEyeY(), minecraft.player.getZ());
/* 172 */     float br = LightTexture.getBrightness(minecraft.player.level().dimensionType(), minecraft.player.level().getMaxLocalRawBrightness(pos));
/*     */     
/* 174 */     int color = ARGB.colorFromFloat(0.1F, br, br, br);
/*     */     
/* 176 */     float size = 4.0F;
/*     */     
/* 178 */     float x0 = -1.0F;
/* 179 */     float x1 = 1.0F;
/* 180 */     float y0 = -1.0F;
/* 181 */     float y1 = 1.0F;
/* 182 */     float z0 = -0.5F;
/*     */     
/* 184 */     float uo = -minecraft.player.getYRot() / 64.0F;
/* 185 */     float vo = minecraft.player.getXRot() / 64.0F;
/*     */     
/* 187 */     Matrix4f pose = poseStack.last().pose();
/*     */     
/* 189 */     VertexConsumer builder = bufferSource.getBuffer(RenderTypes.blockScreenEffect(UNDERWATER_LOCATION));
/* 190 */     builder.addVertex((Matrix4fc)pose, -1.0F, -1.0F, -0.5F).setUv(4.0F + uo, 4.0F + vo).setColor(color);
/* 191 */     builder.addVertex((Matrix4fc)pose, 1.0F, -1.0F, -0.5F).setUv(0.0F + uo, 4.0F + vo).setColor(color);
/* 192 */     builder.addVertex((Matrix4fc)pose, 1.0F, 1.0F, -0.5F).setUv(0.0F + uo, 0.0F + vo).setColor(color);
/* 193 */     builder.addVertex((Matrix4fc)pose, -1.0F, 1.0F, -0.5F).setUv(4.0F + uo, 0.0F + vo).setColor(color);
/*     */   }
/*     */   
/*     */   private static void renderFire(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite) {
/* 197 */     VertexConsumer builder = bufferSource.getBuffer(RenderTypes.fireScreenEffect(sprite.atlasLocation()));
/*     */     
/* 199 */     float u0 = sprite.getU0();
/* 200 */     float u1 = sprite.getU1();
/* 201 */     float v0 = sprite.getV0();
/* 202 */     float v1 = sprite.getV1();
/*     */     
/* 204 */     float size = 1.0F;
/* 205 */     for (int i = 0; i < 2; i++) {
/* 206 */       poseStack.pushPose();
/*     */       
/* 208 */       float x0 = -0.5F;
/* 209 */       float x1 = 0.5F;
/* 210 */       float y0 = -0.5F;
/* 211 */       float y1 = 0.5F;
/* 212 */       float z0 = -0.5F;
/* 213 */       poseStack.translate(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
/* 214 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees((i * 2 - 1) * 10.0F));
/*     */       
/* 216 */       Matrix4f pose = poseStack.last().pose();
/* 217 */       builder.addVertex((Matrix4fc)pose, -0.5F, -0.5F, -0.5F).setUv(u1, v1).setColor(1.0F, 1.0F, 1.0F, 0.9F);
/* 218 */       builder.addVertex((Matrix4fc)pose, 0.5F, -0.5F, -0.5F).setUv(u0, v1).setColor(1.0F, 1.0F, 1.0F, 0.9F);
/* 219 */       builder.addVertex((Matrix4fc)pose, 0.5F, 0.5F, -0.5F).setUv(u0, v0).setColor(1.0F, 1.0F, 1.0F, 0.9F);
/* 220 */       builder.addVertex((Matrix4fc)pose, -0.5F, 0.5F, -0.5F).setUv(u1, v0).setColor(1.0F, 1.0F, 1.0F, 0.9F);
/* 221 */       poseStack.popPose();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/ScreenEffectRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */