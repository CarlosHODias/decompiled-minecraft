/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.entity.ClientAvatarEntity;
/*     */ import net.minecraft.client.entity.ClientMannequin;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.player.AbstractClientPlayer;
/*     */ import net.minecraft.client.renderer.ItemInHandRenderer;
/*     */ import net.minecraft.client.renderer.MapRenderer;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.PlayerModelType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public class EntityRenderDispatcher
/*     */   implements ResourceManagerReloadListener
/*     */ {
/*  45 */   private Map<EntityType<?>, EntityRenderer<?, ?>> renderers = (Map<EntityType<?>, EntityRenderer<?, ?>>)ImmutableMap.of();
/*  46 */   private Map<PlayerModelType, AvatarRenderer<AbstractClientPlayer>> playerRenderers = Map.of();
/*  47 */   private Map<PlayerModelType, AvatarRenderer<ClientMannequin>> mannequinRenderers = Map.of();
/*     */   
/*     */   public final TextureManager textureManager;
/*     */   public Camera camera;
/*     */   public Entity crosshairPickEntity;
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   private final MapRenderer mapRenderer;
/*     */   private final BlockRenderDispatcher blockRenderDispatcher;
/*     */   private final ItemInHandRenderer itemInHandRenderer;
/*     */   private final AtlasManager atlasManager;
/*     */   private final Font font;
/*     */   public final Options options;
/*     */   private final Supplier<EntityModelSet> entityModels;
/*     */   private final EquipmentAssetManager equipmentAssets;
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   public <E extends Entity> int getPackedLightCoords(E entity, float partialTickTime) {
/*  64 */     return getRenderer(entity).getPackedLightCoords(entity, partialTickTime);
/*     */   }
/*     */   
/*     */   public EntityRenderDispatcher(Minecraft minecraft, TextureManager textureManager, ItemModelResolver itemModelResolver, MapRenderer mapRenderer, BlockRenderDispatcher blockRenderDispatcher, AtlasManager atlasManager, Font font, Options options, Supplier<EntityModelSet> entityModels, EquipmentAssetManager equipmentAssets, PlayerSkinRenderCache playerSkinRenderCache) {
/*  68 */     this.textureManager = textureManager;
/*  69 */     this.itemModelResolver = itemModelResolver;
/*  70 */     this.mapRenderer = mapRenderer;
/*  71 */     this.atlasManager = atlasManager;
/*  72 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*  73 */     this.itemInHandRenderer = new ItemInHandRenderer(minecraft, this, itemModelResolver);
/*  74 */     this.blockRenderDispatcher = blockRenderDispatcher;
/*  75 */     this.font = font;
/*  76 */     this.options = options;
/*  77 */     this.entityModels = entityModels;
/*  78 */     this.equipmentAssets = equipmentAssets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Entity> EntityRenderer<? super T, ?> getRenderer(T entity) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   16: lookupswitch default -> 82, 0 -> 44, 1 -> 63
/*     */     //   44: aload_2
/*     */     //   45: checkcast net/minecraft/client/player/AbstractClientPlayer
/*     */     //   48: astore #4
/*     */     //   50: aload_0
/*     */     //   51: aload_0
/*     */     //   52: getfield playerRenderers : Ljava/util/Map;
/*     */     //   55: aload #4
/*     */     //   57: invokevirtual getAvatarRenderer : (Ljava/util/Map;Lnet/minecraft/world/entity/Avatar;)Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;
/*     */     //   60: goto -> 98
/*     */     //   63: aload_2
/*     */     //   64: checkcast net/minecraft/client/entity/ClientMannequin
/*     */     //   67: astore #5
/*     */     //   69: aload_0
/*     */     //   70: aload_0
/*     */     //   71: getfield mannequinRenderers : Ljava/util/Map;
/*     */     //   74: aload #5
/*     */     //   76: invokevirtual getAvatarRenderer : (Ljava/util/Map;Lnet/minecraft/world/entity/Avatar;)Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;
/*     */     //   79: goto -> 98
/*     */     //   82: aload_0
/*     */     //   83: getfield renderers : Ljava/util/Map;
/*     */     //   86: aload_1
/*     */     //   87: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */     //   90: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   95: checkcast net/minecraft/client/renderer/entity/EntityRenderer
/*     */     //   98: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     //   #84	-> 44
/*     */     //   #85	-> 63
/*     */     //   #86	-> 82
/*     */     //   #83	-> 98
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   50	13	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*     */     //   69	13	5	mannequin	Lnet/minecraft/client/entity/ClientMannequin;
/*     */     //   0	99	0	this	Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;
/*     */     //   0	99	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	99	1	entity	TT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AvatarRenderer<AbstractClientPlayer> getPlayerRenderer(AbstractClientPlayer player) {
/*  91 */     return getAvatarRenderer(this.playerRenderers, player);
/*     */   }
/*     */   
/*     */   private <T extends Avatar & ClientAvatarEntity> AvatarRenderer<T> getAvatarRenderer(Map<PlayerModelType, AvatarRenderer<T>> renderers, T entity) {
/*  95 */     PlayerModelType model = ((ClientAvatarEntity)entity).getSkin().model();
/*  96 */     AvatarRenderer<T> playerRenderer = renderers.get(model);
/*  97 */     if (playerRenderer != null) {
/*  98 */       return playerRenderer;
/*     */     }
/* 100 */     return renderers.get(PlayerModelType.WIDE);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S extends EntityRenderState> EntityRenderer<?, ? super S> getRenderer(S entityRenderState) {
/* 105 */     if (entityRenderState instanceof AvatarRenderState) { AvatarRenderState player = (AvatarRenderState)entityRenderState;
/* 106 */       PlayerModelType model = player.skin.model();
/* 107 */       EntityRenderer<? extends Avatar, ?> playerRenderer = (EntityRenderer<? extends Avatar, ?>)this.playerRenderers.get(model);
/* 108 */       if (playerRenderer != null) {
/* 109 */         return (EntityRenderer)playerRenderer;
/*     */       }
/* 111 */       return (EntityRenderer<?, ? super S>)this.playerRenderers.get(PlayerModelType.WIDE); }
/*     */     
/* 113 */     return (EntityRenderer<?, ? super S>)this.renderers.get(((EntityRenderState)entityRenderState).entityType);
/*     */   }
/*     */   
/*     */   public void prepare(Camera camera, Entity crosshairPickEntity) {
/* 117 */     this.camera = camera;
/* 118 */     this.crosshairPickEntity = crosshairPickEntity;
/*     */   }
/*     */   
/*     */   public <E extends Entity> boolean shouldRender(E entity, Frustum culler, double camX, double camY, double camZ) {
/* 122 */     EntityRenderer<? super E, ?> renderer = getRenderer(entity);
/* 123 */     return renderer.shouldRender(entity, culler, camX, camY, camZ);
/*     */   }
/*     */   
/*     */   public <E extends Entity> EntityRenderState extractEntity(E entity, float partialTicks) {
/* 127 */     EntityRenderer<? super E, ?> renderer = getRenderer(entity);
/*     */     
/*     */     try {
/* 130 */       return (EntityRenderState)renderer.createRenderState(entity, partialTicks);
/* 131 */     } catch (Throwable t) {
/* 132 */       CrashReport report = CrashReport.forThrowable(t, "Extracting render state for an entity in world");
/* 133 */       CrashReportCategory entityCat = report.addCategory("Entity being extracted");
/* 134 */       entity.fillCrashReportCategory(entityCat);
/*     */       
/* 136 */       CrashReportCategory rendererCategory = fillRendererDetails(renderer, report);
/* 137 */       rendererCategory.setDetail("Delta", partialTicks);
/*     */       
/* 139 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public <S extends EntityRenderState> void submit(S renderState, CameraRenderState camera, double x, double y, double z, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 144 */     EntityRenderer<?, ? super S> renderer = getRenderer(renderState);
/*     */     
/*     */     try {
/* 147 */       Vec3 pos = renderer.getRenderOffset(renderState);
/* 148 */       double relativeX = x + pos.x();
/* 149 */       double relativeY = y + pos.y();
/* 150 */       double relativeZ = z + pos.z();
/*     */       
/* 152 */       poseStack.pushPose();
/* 153 */       poseStack.translate(relativeX, relativeY, relativeZ);
/* 154 */       renderer.submit(renderState, poseStack, submitNodeCollector, camera);
/*     */       
/* 156 */       if (((EntityRenderState)renderState).displayFireAnimation) {
/* 157 */         submitNodeCollector.submitFlame(poseStack, (EntityRenderState)renderState, Mth.rotationAroundAxis(Mth.Y_AXIS, camera.orientation, new Quaternionf()));
/*     */       }
/*     */       
/* 160 */       if (renderState instanceof AvatarRenderState) {
/* 161 */         poseStack.translate(-pos.x(), -pos.y(), -pos.z());
/*     */       }
/*     */       
/* 164 */       if (!((EntityRenderState)renderState).shadowPieces.isEmpty()) {
/* 165 */         submitNodeCollector.submitShadow(poseStack, ((EntityRenderState)renderState).shadowRadius, ((EntityRenderState)renderState).shadowPieces);
/*     */       }
/*     */       
/* 168 */       if (!(renderState instanceof AvatarRenderState)) {
/* 169 */         poseStack.translate(-pos.x(), -pos.y(), -pos.z());
/*     */       }
/*     */       
/* 172 */       poseStack.popPose();
/* 173 */     } catch (Throwable t) {
/* 174 */       CrashReport report = CrashReport.forThrowable(t, "Rendering entity in world");
/* 175 */       CrashReportCategory entityCat = report.addCategory("EntityRenderState being rendered");
/* 176 */       renderState.fillCrashReportCategory(entityCat);
/*     */       
/* 178 */       fillRendererDetails(renderer, report);
/*     */       
/* 180 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <S extends EntityRenderState> CrashReportCategory fillRendererDetails(EntityRenderer<?, S> renderer, CrashReport report) {
/* 185 */     CrashReportCategory category = report.addCategory("Renderer details");
/* 186 */     category.setDetail("Assigned renderer", renderer);
/* 187 */     return category;
/*     */   }
/*     */   
/*     */   public void resetCamera() {
/* 191 */     this.camera = null;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Entity entity) {
/* 195 */     return this.camera.position().distanceToSqr(entity.position());
/*     */   }
/*     */   
/*     */   public ItemInHandRenderer getItemInHandRenderer() {
/* 199 */     return this.itemInHandRenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 204 */     EntityRendererProvider.Context context = new EntityRendererProvider.Context(this, this.itemModelResolver, this.mapRenderer, this.blockRenderDispatcher, resourceManager, this.entityModels.get(), this.equipmentAssets, this.atlasManager, this.font, this.playerSkinRenderCache);
/* 205 */     this.renderers = EntityRenderers.createEntityRenderers(context);
/* 206 */     this.playerRenderers = EntityRenderers.createAvatarRenderers(context);
/* 207 */     this.mannequinRenderers = EntityRenderers.createAvatarRenderers(context);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EntityRenderDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */