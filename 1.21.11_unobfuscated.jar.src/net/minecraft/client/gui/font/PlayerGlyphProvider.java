/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class PlayerGlyphProvider
/*     */ {
/*  23 */   private static final GlyphInfo GLYPH_INFO = GlyphInfo.simple(8.0F);
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*  26 */   private final LoadingCache<FontDescription.PlayerSprite, GlyphSource> wrapperCache = CacheBuilder.newBuilder()
/*  27 */     .expireAfterAccess(PlayerSkinRenderCache.CACHE_DURATION)
/*  28 */     .build(new CacheLoader<FontDescription.PlayerSprite, GlyphSource>()
/*     */       {
/*     */         public GlyphSource load(FontDescription.PlayerSprite playerInfo) {
/*  31 */           final Supplier<PlayerSkinRenderCache.RenderInfo> skin = PlayerGlyphProvider.this.playerSkinRenderCache.createLookup(playerInfo.profile());
/*  32 */           final boolean hat = playerInfo.hat();
/*     */           
/*  34 */           return new SingleSpriteSource(new BakedGlyph(this)
/*     */               {
/*     */                 public GlyphInfo info() {
/*  37 */                   return PlayerGlyphProvider.GLYPH_INFO;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/*  42 */                   return new PlayerGlyphProvider.Instance(skin, hat, x, y, color, shadowColor, shadowOffset, style);
/*     */                 }
/*     */               });
/*     */         }
/*     */       });
/*     */   
/*     */   public PlayerGlyphProvider(PlayerSkinRenderCache playerSkinRenderCache) {
/*  49 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*     */   }
/*     */   
/*     */   public GlyphSource sourceForPlayer(FontDescription.PlayerSprite playerInfo) {
/*  53 */     return (GlyphSource)this.wrapperCache.getUnchecked(playerInfo);
/*     */   }
/*     */   private static final class Instance extends Record implements PlainTextRenderable { private final Supplier<PlayerSkinRenderCache.RenderInfo> skin; private final boolean hat; private final float x; private final float y; private final int color; private final int shadowColor; private final float shadowOffset; private final Style style;
/*  56 */     private Instance(Supplier<PlayerSkinRenderCache.RenderInfo> skin, boolean hat, float x, float y, int color, int shadowColor, float shadowOffset, Style style) { this.skin = skin; this.hat = hat; this.x = x; this.y = y; this.color = color; this.shadowColor = shadowColor; this.shadowOffset = shadowOffset; this.style = style; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  56 */       //   0	7	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance; } public Supplier<PlayerSkinRenderCache.RenderInfo> skin() { return this.skin; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;
/*  56 */       //   0	8	1	o	Ljava/lang/Object; } public boolean hat() { return this.hat; } public float x() { return this.x; } public float y() { return this.y; } public int color() { return this.color; } public int shadowColor() { return this.shadowColor; } public float shadowOffset() { return this.shadowOffset; } public Style style() { return this.style; }
/*     */     
/*     */     public void renderSprite(Matrix4f pose, VertexConsumer buffer, int packedLightCoords, float offsetX, float offsetY, float z, int color) {
/*  59 */       float x0 = offsetX + left();
/*  60 */       float x1 = offsetX + right();
/*  61 */       float y0 = offsetY + top();
/*  62 */       float y1 = offsetY + bottom();
/*     */       
/*  64 */       renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 8.0F, 8.0F, 8, 8, 64, 64);
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
/*  77 */       if (this.hat) {
/*  78 */         renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 40.0F, 8.0F, 8, 8, 64, 64);
/*     */       }
/*     */     }
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
/*     */     private static void renderQuad(Matrix4f pose, VertexConsumer buffer, int packedLightCoords, float x0, float x1, float y0, float y1, float z, int color, float u, float v, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
/*  94 */       float u0 = (u + 0.0F) / textureWidth;
/*  95 */       float u1 = (u + srcWidth) / textureWidth;
/*  96 */       float v0 = (v + 0.0F) / textureHeight;
/*  97 */       float v1 = (v + srcHeight) / textureHeight;
/*     */       
/*  99 */       buffer.addVertex((Matrix4fc)pose, x0, y0, z).setUv(u0, v0).setColor(color).setLight(packedLightCoords);
/* 100 */       buffer.addVertex((Matrix4fc)pose, x0, y1, z).setUv(u0, v1).setColor(color).setLight(packedLightCoords);
/* 101 */       buffer.addVertex((Matrix4fc)pose, x1, y1, z).setUv(u1, v1).setColor(color).setLight(packedLightCoords);
/* 102 */       buffer.addVertex((Matrix4fc)pose, x1, y0, z).setUv(u1, v0).setColor(color).setLight(packedLightCoords);
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderType renderType(Font.DisplayMode displayMode) {
/* 107 */       return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).glyphRenderTypes().select(displayMode);
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderPipeline guiPipeline() {
/* 112 */       return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).glyphRenderTypes().guiPipeline();
/*     */     }
/*     */ 
/*     */     
/*     */     public GpuTextureView textureView() {
/* 117 */       return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).textureView();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/PlayerGlyphProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */