/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LogoRenderer {
/* 10 */   public static final Identifier MINECRAFT_LOGO = Identifier.withDefaultNamespace("textures/gui/title/minecraft.png");
/* 11 */   public static final Identifier EASTER_EGG_LOGO = Identifier.withDefaultNamespace("textures/gui/title/minceraft.png");
/* 12 */   public static final Identifier MINECRAFT_EDITION = Identifier.withDefaultNamespace("textures/gui/title/edition.png");
/*    */   
/*    */   public static final int LOGO_WIDTH = 256;
/*    */   
/*    */   public static final int LOGO_HEIGHT = 44;
/*    */   
/*    */   private static final int LOGO_TEXTURE_WIDTH = 256;
/*    */   
/*    */   private static final int LOGO_TEXTURE_HEIGHT = 64;
/*    */   private static final int EDITION_WIDTH = 128;
/*    */   private static final int EDITION_HEIGHT = 14;
/*    */   private static final int EDITION_TEXTURE_WIDTH = 128;
/*    */   private static final int EDITION_TEXTURE_HEIGHT = 16;
/*    */   public static final int DEFAULT_HEIGHT_OFFSET = 30;
/*    */   private static final int EDITION_LOGO_OVERLAP = 7;
/* 27 */   private final boolean showEasterEgg = (RandomSource.create().nextFloat() < 1.0E-4D);
/*    */   
/*    */   private final boolean keepLogoThroughFade;
/*    */   
/*    */   public LogoRenderer(boolean keepLogoThroughFade) {
/* 32 */     this.keepLogoThroughFade = keepLogoThroughFade;
/*    */   }
/*    */   
/*    */   public void renderLogo(GuiGraphics graphics, int width, float alpha) {
/* 36 */     renderLogo(graphics, width, alpha, 30);
/*    */   }
/*    */   
/*    */   public void renderLogo(GuiGraphics graphics, int width, float alpha, int heightOffset) {
/* 40 */     int logoX = width / 2 - 128;
/* 41 */     float effectiveAlpha = this.keepLogoThroughFade ? 1.0F : alpha;
/* 42 */     int color = ARGB.white(effectiveAlpha);
/* 43 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.showEasterEgg ? EASTER_EGG_LOGO : MINECRAFT_LOGO, logoX, heightOffset, 0.0F, 0.0F, 256, 44, 256, 64, color);
/*    */     
/* 45 */     int editionX = width / 2 - 64;
/* 46 */     int y = heightOffset + 44 - 7;
/* 47 */     graphics.blit(RenderPipelines.GUI_TEXTURED, MINECRAFT_EDITION, editionX, y, 0.0F, 0.0F, 128, 14, 128, 16, color);
/*    */   }
/*    */   
/*    */   public boolean keepLogoThroughFade() {
/* 51 */     return this.keepLogoThroughFade;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/LogoRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */