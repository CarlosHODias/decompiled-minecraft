/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.texture.MipmapStrategy;
/*     */ import net.minecraft.client.renderer.texture.ReloadableTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureContents;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ReloadInstance;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LoadingOverlay
/*     */   extends Overlay {
/*  29 */   public static final Identifier MOJANG_STUDIOS_LOGO_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/mojangstudios.png");
/*  30 */   private static final int LOGO_BACKGROUND_COLOR = ARGB.color(255, 239, 50, 61);
/*  31 */   private static final int LOGO_BACKGROUND_COLOR_DARK = ARGB.color(255, 0, 0, 0);
/*     */   
/*     */   private static final IntSupplier BRAND_BACKGROUND = () -> (Boolean)(Minecraft.getInstance()).options.darkMojangStudiosBackground().get() ? LOGO_BACKGROUND_COLOR_DARK : LOGO_BACKGROUND_COLOR;
/*     */   
/*     */   private static final int LOGO_SCALE = 240;
/*     */   
/*     */   private static final float LOGO_QUARTER_FLOAT = 60.0F;
/*     */   
/*     */   private static final int LOGO_QUARTER = 60;
/*     */   private static final int LOGO_HALF = 120;
/*     */   private static final float LOGO_OVERLAP = 0.0625F;
/*     */   private static final float SMOOTHING = 0.95F;
/*     */   public static final long FADE_OUT_TIME = 1000L;
/*     */   public static final long FADE_IN_TIME = 500L;
/*     */   private final Minecraft minecraft;
/*     */   private final ReloadInstance reload;
/*     */   private final Consumer<Optional<Throwable>> onFinish;
/*     */   private final boolean fadeIn;
/*     */   private float currentProgress;
/*  50 */   private long fadeOutStart = -1L;
/*  51 */   private long fadeInStart = -1L;
/*     */   
/*     */   public LoadingOverlay(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
/*  54 */     this.minecraft = minecraft;
/*  55 */     this.reload = reload;
/*  56 */     this.onFinish = onFinish;
/*  57 */     this.fadeIn = fadeIn;
/*     */   }
/*     */   
/*     */   public static void registerTextures(TextureManager textureManager) {
/*  61 */     textureManager.registerAndLoad(MOJANG_STUDIOS_LOGO_LOCATION, new LogoTexture());
/*     */   }
/*     */   
/*     */   private static int replaceAlpha(int color, int alpha) {
/*  65 */     return color & 0xFFFFFF | alpha << 24;
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     float logoAlpha;
/*  70 */     int width = graphics.guiWidth();
/*  71 */     int height = graphics.guiHeight();
/*  72 */     long now = Util.getMillis();
/*     */     
/*  74 */     if (this.fadeIn && this.fadeInStart == -1L) {
/*  75 */       this.fadeInStart = now;
/*     */     }
/*     */     
/*  78 */     float fadeOutAnim = (this.fadeOutStart > -1L) ? ((float)(now - this.fadeOutStart) / 1000.0F) : -1.0F;
/*  79 */     float fadeInAnim = (this.fadeInStart > -1L) ? ((float)(now - this.fadeInStart) / 500.0F) : -1.0F;
/*     */ 
/*     */     
/*  82 */     if (fadeOutAnim >= 1.0F) {
/*  83 */       if (this.minecraft.screen != null) {
/*  84 */         this.minecraft.screen.renderWithTooltipAndSubtitles(graphics, 0, 0, a);
/*     */       } else {
/*  86 */         this.minecraft.gui.renderDeferredSubtitles();
/*     */       } 
/*  88 */       int alpha = Mth.ceil((1.0F - Mth.clamp(fadeOutAnim - 1.0F, 0.0F, 1.0F)) * 255.0F);
/*  89 */       graphics.nextStratum();
/*  90 */       graphics.fill(0, 0, width, height, replaceAlpha(BRAND_BACKGROUND.getAsInt(), alpha));
/*  91 */       logoAlpha = 1.0F - Mth.clamp(fadeOutAnim - 1.0F, 0.0F, 1.0F);
/*  92 */     } else if (this.fadeIn) {
/*  93 */       if (this.minecraft.screen != null && fadeInAnim < 1.0F) {
/*  94 */         this.minecraft.screen.renderWithTooltipAndSubtitles(graphics, mouseX, mouseY, a);
/*     */       } else {
/*  96 */         this.minecraft.gui.renderDeferredSubtitles();
/*     */       } 
/*  98 */       int alpha = Mth.ceil(Mth.clamp(fadeInAnim, 0.15D, 1.0D) * 255.0D);
/*  99 */       graphics.nextStratum();
/* 100 */       graphics.fill(0, 0, width, height, replaceAlpha(BRAND_BACKGROUND.getAsInt(), alpha));
/* 101 */       logoAlpha = Mth.clamp(fadeInAnim, 0.0F, 1.0F);
/*     */     } else {
/* 103 */       int col = BRAND_BACKGROUND.getAsInt();
/*     */       
/* 105 */       RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.minecraft.getMainRenderTarget().getColorTexture(), col);
/* 106 */       logoAlpha = 1.0F;
/*     */     } 
/*     */     
/* 109 */     int contentX = (int)(graphics.guiWidth() * 0.5D);
/* 110 */     int logoY = (int)(graphics.guiHeight() * 0.5D);
/*     */     
/* 112 */     double logoHeight = Math.min(graphics.guiWidth() * 0.75D, graphics.guiHeight()) * 0.25D;
/* 113 */     int logoHeightHalf = (int)(logoHeight * 0.5D);
/* 114 */     double contentWidth = logoHeight * 4.0D;
/* 115 */     int logoWidthHalf = (int)(contentWidth * 0.5D);
/*     */     
/* 117 */     int color = ARGB.white(logoAlpha);
/* 118 */     graphics.blit(RenderPipelines.MOJANG_LOGO, MOJANG_STUDIOS_LOGO_LOCATION, contentX - logoWidthHalf, logoY - logoHeightHalf, -0.0625F, 0.0F, logoWidthHalf, (int)logoHeight, 120, 60, 120, 120, color);
/* 119 */     graphics.blit(RenderPipelines.MOJANG_LOGO, MOJANG_STUDIOS_LOGO_LOCATION, contentX, logoY - logoHeightHalf, 0.0625F, 60.0F, logoWidthHalf, (int)logoHeight, 120, 60, 120, 120, color);
/*     */     
/* 121 */     int barY = (int)(graphics.guiHeight() * 0.8325D);
/*     */     
/* 123 */     float actualProgress = this.reload.getActualProgress();
/* 124 */     this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + actualProgress * 0.050000012F, 0.0F, 1.0F);
/*     */     
/* 126 */     if (fadeOutAnim < 1.0F) {
/* 127 */       drawProgressBar(graphics, width / 2 - logoWidthHalf, barY - 5, width / 2 + logoWidthHalf, barY + 5, 1.0F - Mth.clamp(fadeOutAnim, 0.0F, 1.0F));
/*     */     }
/*     */     
/* 130 */     if (fadeOutAnim >= 2.0F) {
/* 131 */       this.minecraft.setOverlay(null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 138 */     if (this.fadeOutStart == -1L && this.reload.isDone() && isReadyToFadeOut()) {
/*     */       try {
/* 140 */         this.reload.checkExceptions();
/* 141 */         this.onFinish.accept(Optional.empty());
/* 142 */       } catch (Throwable t) {
/* 143 */         this.onFinish.accept(Optional.of(t));
/*     */       } 
/* 145 */       this.fadeOutStart = Util.getMillis();
/* 146 */       if (this.minecraft.screen != null) {
/*     */         
/* 148 */         Window window = this.minecraft.getWindow();
/* 149 */         this.minecraft.screen.init(window.getGuiScaledWidth(), window.getGuiScaledHeight());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isReadyToFadeOut() {
/* 155 */     return (!this.fadeIn || (this.fadeInStart > -1L && Util.getMillis() - this.fadeInStart >= 1000L));
/*     */   }
/*     */   
/*     */   private void drawProgressBar(GuiGraphics graphics, int x0, int y0, int x1, int y1, float fade) {
/* 159 */     int width = Mth.ceil((x1 - x0 - 2) * this.currentProgress);
/* 160 */     int alpha = Math.round(fade * 255.0F);
/* 161 */     int white = ARGB.color(alpha, 255, 255, 255);
/*     */ 
/*     */     
/* 164 */     graphics.fill(x0 + 2, y0 + 2, x0 + width, y1 - 2, white);
/*     */     
/* 166 */     graphics.fill(x0 + 1, y0, x1 - 1, y0 + 1, white);
/* 167 */     graphics.fill(x0 + 1, y1, x1 - 1, y1 - 1, white);
/* 168 */     graphics.fill(x0, y0, x0 + 1, y1, white);
/* 169 */     graphics.fill(x1, y0, x1 - 1, y1, white);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 174 */     return true;
/*     */   }
/*     */   
/*     */   private static class LogoTexture extends ReloadableTexture {
/*     */     public LogoTexture() {
/* 179 */       super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
/*     */     }
/*     */ 
/*     */     
/*     */     public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 184 */       ResourceProvider vanillaProvider = Minecraft.getInstance().getVanillaPackResources().asProvider();
/* 185 */       InputStream resource = vanillaProvider.open(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION); try {
/* 186 */         TextureContents textureContents = new TextureContents(NativeImage.read(resource), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
/* 187 */         if (resource != null) resource.close(); 
/*     */         return textureContents;
/*     */       } catch (Throwable throwable) {
/*     */         if (resource != null)
/*     */           try {
/*     */             resource.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/LoadingOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */