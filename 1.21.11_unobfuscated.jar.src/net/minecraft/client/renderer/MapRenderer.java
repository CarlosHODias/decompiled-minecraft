/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.MapTextureManager;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ 
/*     */ public class MapRenderer
/*     */ {
/*     */   private static final float MAP_Z_OFFSET = -0.01F;
/*     */   private static final float DECORATION_Z_OFFSET = -0.001F;
/*     */   public static final int WIDTH = 128;
/*     */   
/*     */   public MapRenderer(AtlasManager atlasManager, MapTextureManager mapTextureManager) {
/*  31 */     this.decorationSprites = atlasManager.getAtlasOrThrow(AtlasIds.MAP_DECORATIONS);
/*  32 */     this.mapTextureManager = mapTextureManager;
/*     */   }
/*     */   public static final int HEIGHT = 128; private final TextureAtlas decorationSprites; private final MapTextureManager mapTextureManager;
/*     */   
/*     */   public void render(MapRenderState mapRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, boolean showOnlyFrame, int lightCoords) {
/*  37 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(mapRenderState.texture), (pose, buffer) -> {
/*     */           buffer.addVertex(pose, 0.0F, 128.0F, -0.01F).setColor(-1).setUv(0.0F, 1.0F).setLight(lightCoords);
/*     */           
/*     */           buffer.addVertex(pose, 128.0F, 128.0F, -0.01F).setColor(-1).setUv(1.0F, 1.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, 128.0F, 0.0F, -0.01F).setColor(-1).setUv(1.0F, 0.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, 0.0F, 0.0F, -0.01F).setColor(-1).setUv(0.0F, 0.0F).setLight(lightCoords);
/*     */         });
/*  44 */     int count = 0;
/*  45 */     for (MapRenderState.MapDecorationRenderState decoration : (Iterable<MapRenderState.MapDecorationRenderState>)mapRenderState.decorations) {
/*  46 */       if (showOnlyFrame && !decoration.renderOnFrame) {
/*     */         continue;
/*     */       }
/*  49 */       poseStack.pushPose();
/*  50 */       poseStack.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F, -0.02F);
/*  51 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees((decoration.rot * 360) / 16.0F));
/*  52 */       poseStack.scale(4.0F, 4.0F, 3.0F);
/*  53 */       poseStack.translate(-0.125F, 0.125F, 0.0F);
/*     */       
/*  55 */       TextureAtlasSprite atlasSprite = decoration.atlasSprite;
/*  56 */       if (atlasSprite != null) {
/*  57 */         float z = count * -0.001F;
/*  58 */         submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(atlasSprite.atlasLocation()), (pose, buffer) -> {
/*     */               buffer.addVertex(pose, -1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV0()).setLight(lightCoords);
/*     */               
/*     */               buffer.addVertex(pose, 1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV0()).setLight(lightCoords);
/*     */               buffer.addVertex(pose, 1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV1()).setLight(lightCoords);
/*     */               buffer.addVertex(pose, -1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV1()).setLight(lightCoords);
/*     */             });
/*  65 */         poseStack.popPose();
/*     */       } 
/*     */       
/*  68 */       if (decoration.name != null) {
/*  69 */         Font font = (Minecraft.getInstance()).font;
/*  70 */         float width = font.width((FormattedText)decoration.name);
/*  71 */         Objects.requireNonNull(font); float scale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
/*     */         
/*  73 */         poseStack.pushPose();
/*  74 */         poseStack.translate(decoration.x / 2.0F + 64.0F - width * scale / 2.0F, decoration.y / 2.0F + 64.0F + 4.0F, -0.025F);
/*  75 */         poseStack.scale(scale, scale, -1.0F);
/*  76 */         poseStack.translate(0.0F, 0.0F, 0.1F);
/*  77 */         submitNodeCollector.order(1).submitText(poseStack, 0.0F, 0.0F, decoration.name.getVisualOrderText(), false, Font.DisplayMode.NORMAL, lightCoords, -1, Integer.MIN_VALUE, 0);
/*  78 */         poseStack.popPose();
/*     */       } 
/*     */       
/*  81 */       count++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(MapId mapId, MapItemSavedData mapData, MapRenderState mapRenderState) {
/*  86 */     mapRenderState.texture = this.mapTextureManager.prepareMapTexture(mapId, mapData);
/*  87 */     mapRenderState.decorations.clear();
/*     */     
/*  89 */     for (MapDecoration decoration : (Iterable<MapDecoration>)mapData.getDecorations()) {
/*  90 */       mapRenderState.decorations.add(extractDecorationRenderState(decoration));
/*     */     }
/*     */   }
/*     */   
/*     */   private MapRenderState.MapDecorationRenderState extractDecorationRenderState(MapDecoration decoration) {
/*  95 */     MapRenderState.MapDecorationRenderState state = new MapRenderState.MapDecorationRenderState();
/*  96 */     state.atlasSprite = this.decorationSprites.getSprite(decoration.getSpriteLocation());
/*  97 */     state.x = decoration.x();
/*  98 */     state.y = decoration.y();
/*  99 */     state.rot = decoration.rot();
/* 100 */     state.name = decoration.name().orElse(null);
/* 101 */     state.renderOnFrame = decoration.renderOnFrame();
/* 102 */     return state;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/MapRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */