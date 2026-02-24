/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorType;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.render.state.BlitRenderState;
/*     */ import net.minecraft.client.gui.render.state.ColoredRectangleRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiElementRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiItemRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiTextRenderState;
/*     */ import net.minecraft.client.gui.render.state.TiledBlitRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiBannerResultRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiBookModelRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiProfilerChartRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiSignRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.GuiSkinRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.model.object.book.BookModel;
/*     */ import net.minecraft.client.model.player.PlayerModel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ResultField;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fStack;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector2ic;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class GuiGraphics
/*     */ {
/*     */   private static final int EXTRA_SPACE_AFTER_FIRST_TOOLTIP_LINE = 2;
/*     */   private final Minecraft minecraft;
/*     */   private final Matrix3x2fStack pose;
/*  94 */   private final ScissorStack scissorStack = new ScissorStack();
/*     */   private final MaterialSet materials;
/*     */   private final TextureAtlas guiSprites;
/*     */   private final GuiRenderState guiRenderState;
/*  98 */   private CursorType pendingCursor = CursorType.DEFAULT;
/*     */   
/*     */   private final int mouseX;
/*     */   
/*     */   private final int mouseY;
/*     */   private Runnable deferredTooltip;
/*     */   private Style hoveredTextStyle;
/*     */   private Style clickableTextStyle;
/*     */   
/*     */   private GuiGraphics(Minecraft minecraft, Matrix3x2fStack pose, GuiRenderState guiRenderState, int mouseX, int mouseY) {
/* 108 */     this.minecraft = minecraft;
/* 109 */     this.pose = pose;
/* 110 */     this.mouseX = mouseX;
/* 111 */     this.mouseY = mouseY;
/* 112 */     AtlasManager atlasManager = minecraft.getAtlasManager();
/* 113 */     this.materials = (MaterialSet)atlasManager;
/* 114 */     this.guiSprites = atlasManager.getAtlasOrThrow(AtlasIds.GUI);
/* 115 */     this.guiRenderState = guiRenderState;
/*     */   }
/*     */   
/*     */   public GuiGraphics(Minecraft minecraft, GuiRenderState guiRenderState, int mouseX, int mouseY) {
/* 119 */     this(minecraft, new Matrix3x2fStack(16), guiRenderState, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public void requestCursor(CursorType cursorType) {
/* 123 */     this.pendingCursor = cursorType;
/*     */   }
/*     */   
/*     */   public void applyCursor(Window window) {
/* 127 */     window.selectCursor(this.pendingCursor);
/*     */   }
/*     */   
/*     */   public int guiWidth() {
/* 131 */     return this.minecraft.getWindow().getGuiScaledWidth();
/*     */   }
/*     */   
/*     */   public int guiHeight() {
/* 135 */     return this.minecraft.getWindow().getGuiScaledHeight();
/*     */   }
/*     */   
/*     */   public void nextStratum() {
/* 139 */     this.guiRenderState.nextStratum();
/*     */   }
/*     */   
/*     */   public void blurBeforeThisStratum() {
/* 143 */     this.guiRenderState.blurBeforeThisStratum();
/*     */   }
/*     */   
/*     */   public Matrix3x2fStack pose() {
/* 147 */     return this.pose;
/*     */   }
/*     */   
/*     */   public void hLine(int x0, int x1, int y, int col) {
/* 151 */     if (x1 < x0) {
/* 152 */       int tmp = x0;
/* 153 */       x0 = x1;
/* 154 */       x1 = tmp;
/*     */     } 
/* 156 */     fill(x0, y, x1 + 1, y + 1, col);
/*     */   }
/*     */   
/*     */   public void vLine(int x, int y0, int y1, int col) {
/* 160 */     if (y1 < y0) {
/* 161 */       int tmp = y0;
/* 162 */       y0 = y1;
/* 163 */       y1 = tmp;
/*     */     } 
/* 165 */     fill(x, y0 + 1, x + 1, y1, col);
/*     */   }
/*     */   
/*     */   public void enableScissor(int x0, int y0, int x1, int y1) {
/* 169 */     ScreenRectangle rectangle = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)
/* 170 */       .transformAxisAligned((Matrix3x2fc)this.pose);
/* 171 */     this.scissorStack.push(rectangle);
/*     */   }
/*     */   
/*     */   public void disableScissor() {
/* 175 */     this.scissorStack.pop();
/*     */   }
/*     */   
/*     */   public boolean containsPointInScissor(int x, int y) {
/* 179 */     return this.scissorStack.containsPoint(x, y);
/*     */   }
/*     */   
/*     */   public void fill(int x0, int y0, int x1, int y1, int col) {
/* 183 */     fill(RenderPipelines.GUI, x0, y0, x1, y1, col);
/*     */   }
/*     */   
/*     */   public void fill(RenderPipeline pipeline, int x0, int y0, int x1, int y1, int col) {
/* 187 */     if (x0 < x1) {
/* 188 */       int tmp = x0;
/* 189 */       x0 = x1;
/* 190 */       x1 = tmp;
/*     */     } 
/* 192 */     if (y0 < y1) {
/* 193 */       int tmp = y0;
/* 194 */       y0 = y1;
/* 195 */       y1 = tmp;
/*     */     } 
/* 197 */     submitColoredRectangle(pipeline, TextureSetup.noTexture(), x0, y0, x1, y1, col, null);
/*     */   }
/*     */   
/*     */   public void fillGradient(int x0, int y0, int x1, int y1, int col1, int col2) {
/* 201 */     submitColoredRectangle(RenderPipelines.GUI, TextureSetup.noTexture(), x0, y0, x1, y1, col1, col2);
/*     */   }
/*     */   
/*     */   public void fill(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1) {
/* 205 */     submitColoredRectangle(renderPipeline, textureSetup, x0, y0, x1, y1, -1, null);
/*     */   }
/*     */   
/*     */   private void submitColoredRectangle(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1, int color1, Integer color2) {
/* 209 */     this.guiRenderState.submitGuiElement((GuiElementRenderState)new ColoredRectangleRenderState(renderPipeline, textureSetup, (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), x0, y0, x1, y1, color1, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 215 */           (color2 != null) ? color2 : color1, 
/* 216 */           this.scissorStack.peek()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void textHighlight(int x0, int y0, int x1, int y1, boolean invertText) {
/* 221 */     if (invertText) {
/* 222 */       fill(RenderPipelines.GUI_INVERT, x0, y0, x1, y1, -1);
/*     */     }
/* 224 */     fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x0, y0, x1, y1, -16776961);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(Font font, String str, int x, int y, int color) {
/* 228 */     drawString(font, str, x - font.width(str) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(Font font, Component text, int x, int y, int color) {
/* 232 */     FormattedCharSequence toRender = text.getVisualOrderText();
/* 233 */     drawString(font, toRender, x - font.width(toRender) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(Font font, FormattedCharSequence text, int x, int y, int color) {
/* 237 */     drawString(font, text, x - font.width(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void drawString(Font font, String str, int x, int y, int color) {
/* 241 */     drawString(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void drawString(Font font, String str, int x, int y, int color, boolean dropShadow) {
/* 245 */     if (str == null) {
/*     */       return;
/*     */     }
/* 248 */     drawString(font, Language.getInstance().getVisualOrder(FormattedText.of(str)), x, y, color, dropShadow);
/*     */   }
/*     */   
/*     */   public void drawString(Font font, FormattedCharSequence str, int x, int y, int color) {
/* 252 */     drawString(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void drawString(Font font, FormattedCharSequence str, int x, int y, int color, boolean dropShadow) {
/* 256 */     if (ARGB.alpha(color) == 0) {
/*     */       return;
/*     */     }
/* 259 */     this.guiRenderState.submitText(new GuiTextRenderState(font, str, (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), x, y, color, 0, dropShadow, false, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void drawString(Font font, Component str, int x, int y, int color) {
/* 263 */     drawString(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void drawString(Font font, Component str, int x, int y, int color, boolean dropShadow) {
/* 267 */     drawString(font, str.getVisualOrderText(), x, y, color, dropShadow);
/*     */   }
/*     */   
/*     */   public void drawWordWrap(Font font, FormattedText string, int x, int y, int w, int col) {
/* 271 */     drawWordWrap(font, string, x, y, w, col, true);
/*     */   }
/*     */   
/*     */   public void drawWordWrap(Font font, FormattedText string, int x, int y, int w, int col, boolean dropShadow) {
/* 275 */     for (FormattedCharSequence line : font.split(string, w)) {
/* 276 */       drawString(font, line, x, y, col, dropShadow);
/* 277 */       Objects.requireNonNull(font); y += 9;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawStringWithBackdrop(Font font, Component str, int textX, int textY, int textWidth, int textColor) {
/* 282 */     int backgroundColor = this.minecraft.options.getBackgroundColor(0.0F);
/* 283 */     if (backgroundColor != 0) {
/* 284 */       int padding = 2;
/* 285 */       Objects.requireNonNull(font); fill(textX - 2, textY - 2, textX + textWidth + 2, textY + 9 + 2, ARGB.multiply(backgroundColor, textColor));
/*     */     } 
/* 287 */     drawString(font, str, textX, textY, textColor, true);
/*     */   }
/*     */   
/*     */   public void renderOutline(int x, int y, int width, int height, int color) {
/* 291 */     fill(x, y, x + width, y + 1, color);
/* 292 */     fill(x, y + height - 1, x + width, y + height, color);
/* 293 */     fill(x, y + 1, x + 1, y + height - 1, color);
/* 294 */     fill(x + width - 1, y + 1, x + width, y + height - 1, color);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height) {
/* 298 */     blitSprite(renderPipeline, location, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, float alpha) {
/* 302 */     blitSprite(renderPipeline, location, x, y, width, height, ARGB.white(alpha));
/*     */   }
/*     */   
/*     */   private static GuiSpriteScaling getSpriteScaling(TextureAtlasSprite sprite) {
/* 306 */     return ((GuiMetadataSection)sprite.contents().getAdditionalMetadata(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT)).scaling();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, int color) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield guiSprites : Lnet/minecraft/client/renderer/texture/TextureAtlas;
/*     */     //   4: aload_2
/*     */     //   5: invokevirtual getSprite : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
/*     */     //   8: astore #8
/*     */     //   10: aload #8
/*     */     //   12: invokestatic getSpriteScaling : (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;
/*     */     //   15: astore #9
/*     */     //   17: aload #9
/*     */     //   19: dup
/*     */     //   20: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   23: pop
/*     */     //   24: astore #10
/*     */     //   26: iconst_0
/*     */     //   27: istore #11
/*     */     //   29: aload #10
/*     */     //   31: iload #11
/*     */     //   33: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   38: tableswitch default -> 166, 0 -> 64, 1 -> 90, 2 -> 138
/*     */     //   64: aload #10
/*     */     //   66: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch
/*     */     //   69: astore #12
/*     */     //   71: aload_0
/*     */     //   72: aload_1
/*     */     //   73: aload #8
/*     */     //   75: iload_3
/*     */     //   76: iload #4
/*     */     //   78: iload #5
/*     */     //   80: iload #6
/*     */     //   82: iload #7
/*     */     //   84: invokevirtual blitSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIII)V
/*     */     //   87: goto -> 166
/*     */     //   90: aload #10
/*     */     //   92: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile
/*     */     //   95: astore #13
/*     */     //   97: aload_0
/*     */     //   98: aload_1
/*     */     //   99: aload #8
/*     */     //   101: iload_3
/*     */     //   102: iload #4
/*     */     //   104: iload #5
/*     */     //   106: iload #6
/*     */     //   108: iconst_0
/*     */     //   109: iconst_0
/*     */     //   110: aload #13
/*     */     //   112: invokevirtual width : ()I
/*     */     //   115: aload #13
/*     */     //   117: invokevirtual height : ()I
/*     */     //   120: aload #13
/*     */     //   122: invokevirtual width : ()I
/*     */     //   125: aload #13
/*     */     //   127: invokevirtual height : ()I
/*     */     //   130: iload #7
/*     */     //   132: invokevirtual blitTiledSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIIIIIIII)V
/*     */     //   135: goto -> 166
/*     */     //   138: aload #10
/*     */     //   140: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice
/*     */     //   143: astore #14
/*     */     //   145: aload_0
/*     */     //   146: aload_1
/*     */     //   147: aload #8
/*     */     //   149: aload #14
/*     */     //   151: iload_3
/*     */     //   152: iload #4
/*     */     //   154: iload #5
/*     */     //   156: iload #6
/*     */     //   158: iload #7
/*     */     //   160: invokevirtual blitNineSlicedSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;IIIII)V
/*     */     //   163: goto -> 166
/*     */     //   166: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #310	-> 0
/*     */     //   #311	-> 10
/*     */     //   #312	-> 17
/*     */     //   #313	-> 64
/*     */     //   #314	-> 71
/*     */     //   #315	-> 90
/*     */     //   #316	-> 97
/*     */     //   #317	-> 138
/*     */     //   #318	-> 145
/*     */     //   #322	-> 166
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   71	19	12	stretch	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;
/*     */     //   97	41	13	tile	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*     */     //   145	21	14	nineSlice	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*     */     //   0	167	0	this	Lnet/minecraft/client/gui/GuiGraphics;
/*     */     //   0	167	1	renderPipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   0	167	2	location	Lnet/minecraft/resources/Identifier;
/*     */     //   0	167	3	x	I
/*     */     //   0	167	4	y	I
/*     */     //   0	167	5	width	I
/*     */     //   0	167	6	height	I
/*     */     //   0	167	7	color	I
/*     */     //   10	157	8	sprite	Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
/*     */     //   17	150	9	scaling	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height) {
/* 325 */     blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
/* 329 */     TextureAtlasSprite sprite = this.guiSprites.getSprite(location);
/* 330 */     GuiSpriteScaling scaling = getSpriteScaling(sprite);
/* 331 */     if (scaling instanceof GuiSpriteScaling.Stretch) {
/* 332 */       blitSprite(renderPipeline, sprite, spriteWidth, spriteHeight, textureX, textureY, x, y, width, height, color);
/*     */     } else {
/*     */       
/* 335 */       enableScissor(x, y, x + width, y + height);
/* 336 */       blitSprite(renderPipeline, location, x - textureX, y - textureY, spriteWidth, spriteHeight, color);
/* 337 */       disableScissor();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height) {
/* 342 */     blitSprite(renderPipeline, sprite, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int color) {
/* 346 */     if (width == 0 || height == 0) {
/*     */       return;
/*     */     }
/* 349 */     innerBlit(renderPipeline, 
/* 350 */         sprite.atlasLocation(), x, x + width, y, y + height, 
/*     */ 
/*     */         
/* 353 */         sprite.getU0(), sprite.getU1(), 
/* 354 */         sprite.getV0(), sprite.getV1(), color);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
/* 360 */     if (width == 0 || height == 0) {
/*     */       return;
/*     */     }
/* 363 */     innerBlit(renderPipeline, 
/* 364 */         sprite.atlasLocation(), x, x + width, y, y + height, 
/*     */ 
/*     */         
/* 367 */         sprite.getU(textureX / spriteWidth), 
/* 368 */         sprite.getU((textureX + width) / spriteWidth), 
/* 369 */         sprite.getV(textureY / spriteHeight), 
/* 370 */         sprite.getV((textureY + height) / spriteHeight), color);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void blitNineSlicedSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int width, int height, int color) {
/* 376 */     GuiSpriteScaling.NineSlice.Border border = nineSlice.border();
/* 377 */     int borderLeft = Math.min(border.left(), width / 2);
/* 378 */     int borderRight = Math.min(border.right(), width / 2);
/* 379 */     int borderTop = Math.min(border.top(), height / 2);
/* 380 */     int borderBottom = Math.min(border.bottom(), height / 2);
/* 381 */     if (width == nineSlice.width() && height == nineSlice.height()) {
/* 382 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, height, color); return;
/*     */     } 
/* 384 */     if (height == nineSlice.height()) {
/* 385 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, borderLeft, height, color);
/* 386 */       blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y, width - borderRight - borderLeft, height, borderLeft, 0, nineSlice.width() - borderRight - borderLeft, nineSlice.height(), nineSlice.width(), nineSlice.height(), color);
/* 387 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, 0, x + width - borderRight, y, borderRight, height, color); return;
/*     */     } 
/* 389 */     if (width == nineSlice.width()) {
/* 390 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, borderTop, color);
/* 391 */       blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x, y + borderTop, width, height - borderBottom - borderTop, 0, borderTop, nineSlice.width(), nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/* 392 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - borderBottom, x, y + height - borderBottom, width, borderBottom, color);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 397 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, borderLeft, borderTop, color);
/*     */     
/* 399 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y, width - borderRight - borderLeft, borderTop, borderLeft, 0, nineSlice.width() - borderRight - borderLeft, borderTop, nineSlice.width(), nineSlice.height(), color);
/*     */     
/* 401 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, 0, x + width - borderRight, y, borderRight, borderTop, color);
/*     */     
/* 403 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - borderBottom, x, y + height - borderBottom, borderLeft, borderBottom, color);
/*     */     
/* 405 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y + height - borderBottom, width - borderRight - borderLeft, borderBottom, borderLeft, nineSlice.height() - borderBottom, nineSlice.width() - borderRight - borderLeft, borderBottom, nineSlice.width(), nineSlice.height(), color);
/*     */     
/* 407 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, nineSlice.height() - borderBottom, x + width - borderRight, y + height - borderBottom, borderRight, borderBottom, color);
/*     */     
/* 409 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x, y + borderTop, borderLeft, height - borderBottom - borderTop, 0, borderTop, borderLeft, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/*     */     
/* 411 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y + borderTop, width - borderRight - borderLeft, height - borderBottom - borderTop, borderLeft, borderTop, nineSlice.width() - borderRight - borderLeft, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/*     */     
/* 413 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + width - borderRight, y + borderTop, borderRight, height - borderBottom - borderTop, nineSlice.width() - borderRight, borderTop, borderRight, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/*     */   }
/*     */   
/*     */   private void blitNineSliceInnerSegment(RenderPipeline renderPipeline, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight, int spriteWidth, int spriteHeight, int color) {
/* 417 */     if (width <= 0 || height <= 0) {
/*     */       return;
/*     */     }
/* 420 */     if (nineSlice.stretchInner()) {
/* 421 */       innerBlit(renderPipeline, 
/* 422 */           sprite.atlasLocation(), x, x + width, y, y + height, 
/*     */ 
/*     */           
/* 425 */           sprite.getU(textureX / spriteWidth), 
/* 426 */           sprite.getU((textureX + textureWidth) / spriteWidth), 
/* 427 */           sprite.getV(textureY / spriteHeight), 
/* 428 */           sprite.getV((textureY + textureHeight) / spriteHeight), color);
/*     */     }
/*     */     else {
/*     */       
/* 432 */       blitTiledSprite(renderPipeline, sprite, x, y, width, height, textureX, textureY, textureWidth, textureHeight, spriteWidth, spriteHeight, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void blitTiledSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int tileWidth, int tileHeight, int spriteWidth, int spriteHeight, int color) {
/* 437 */     if (width <= 0 || height <= 0) {
/*     */       return;
/*     */     }
/* 440 */     if (tileWidth <= 0 || tileHeight <= 0) {
/* 441 */       throw new IllegalArgumentException("Tile size must be positive, got " + tileWidth + "x" + tileHeight);
/*     */     }
/* 443 */     AbstractTexture spriteTexture = this.minecraft.getTextureManager().getTexture(sprite.atlasLocation());
/* 444 */     GpuTextureView texture = spriteTexture.getTextureView();
/* 445 */     submitTiledBlit(renderPipeline, texture, spriteTexture.getSampler(), tileWidth, tileHeight, x, y, x + width, y + height, 
/*     */ 
/*     */         
/* 448 */         sprite.getU(textureX / spriteWidth), 
/* 449 */         sprite.getU((textureX + tileWidth) / spriteWidth), 
/* 450 */         sprite.getV(textureY / spriteHeight), 
/* 451 */         sprite.getV((textureY + tileHeight) / spriteHeight), color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
/* 456 */     blit(renderPipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
/* 460 */     blit(renderPipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
/* 464 */     blit(renderPipeline, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight, -1);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color) {
/* 468 */     innerBlit(renderPipeline, texture, x, x + width, y, y + height, (u + 0.0F) / textureWidth, (u + srcWidth) / textureWidth, (v + 0.0F) / textureHeight, (v + srcHeight) / textureHeight, color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void blit(Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
/* 478 */     innerBlit(RenderPipelines.GUI_TEXTURED, location, x0, x1, y0, y1, u0, u1, v0, v1, -1);
/*     */   }
/*     */   
/*     */   private void innerBlit(RenderPipeline renderPipeline, Identifier location, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
/* 482 */     AbstractTexture texture = this.minecraft.getTextureManager().getTexture(location);
/* 483 */     submitBlit(renderPipeline, texture.getTextureView(), texture.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1, color);
/*     */   }
/*     */   
/*     */   private void submitBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
/* 487 */     this.guiRenderState.submitGuiElement((GuiElementRenderState)new BlitRenderState(pipeline, TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f((Matrix3x2fc)this.pose), x0, y0, x1, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   private void submitTiledBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int tileWidth, int tileHeight, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
/* 491 */     this.guiRenderState.submitGuiElement((GuiElementRenderState)new TiledBlitRenderState(pipeline, TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f((Matrix3x2fc)this.pose), tileWidth, tileHeight, x0, y0, x1, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void renderItem(ItemStack itemStack, int x, int y) {
/* 495 */     renderItem((LivingEntity)this.minecraft.player, (Level)this.minecraft.level, itemStack, x, y, 0);
/*     */   }
/*     */   
/*     */   public void renderItem(ItemStack itemStack, int x, int y, int seed) {
/* 499 */     renderItem((LivingEntity)this.minecraft.player, (Level)this.minecraft.level, itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   public void renderFakeItem(ItemStack itemStack, int x, int y) {
/* 503 */     renderFakeItem(itemStack, x, y, 0);
/*     */   }
/*     */   
/*     */   public void renderFakeItem(ItemStack itemStack, int x, int y, int seed) {
/* 507 */     renderItem(null, (Level)this.minecraft.level, itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   public void renderItem(LivingEntity owner, ItemStack itemStack, int x, int y, int seed) {
/* 511 */     renderItem(owner, owner.level(), itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   private void renderItem(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed) {
/* 515 */     if (itemStack.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 519 */     TrackingItemStackRenderState itemStackRenderState = new TrackingItemStackRenderState();
/* 520 */     this.minecraft.getItemModelResolver().updateForTopItem((ItemStackRenderState)itemStackRenderState, itemStack, ItemDisplayContext.GUI, level, (ItemOwner)owner, seed);
/*     */     try {
/* 522 */       this.guiRenderState.submitItem(new GuiItemRenderState(
/* 523 */             itemStack.getItem().getName().toString(), new Matrix3x2f((Matrix3x2fc)this.pose), itemStackRenderState, x, y, 
/*     */ 
/*     */ 
/*     */             
/* 527 */             this.scissorStack.peek()));
/*     */     }
/* 529 */     catch (Throwable t) {
/* 530 */       CrashReport report = CrashReport.forThrowable(t, "Rendering item");
/* 531 */       CrashReportCategory category = report.addCategory("Item being rendered");
/*     */       
/* 533 */       category.setDetail("Item Type", () -> String.valueOf(itemStack.getItem()));
/* 534 */       category.setDetail("Item Components", () -> String.valueOf(itemStack.getComponents()));
/* 535 */       category.setDetail("Item Foil", () -> String.valueOf(itemStack.hasFoil()));
/*     */       
/* 537 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderItemDecorations(Font font, ItemStack itemStack, int x, int y) {
/* 542 */     renderItemDecorations(font, itemStack, x, y, null);
/*     */   }
/*     */   
/*     */   public void renderItemDecorations(Font font, ItemStack itemStack, int x, int y, String countText) {
/* 546 */     if (itemStack.isEmpty()) {
/*     */       return;
/*     */     }
/* 549 */     this.pose.pushMatrix();
/* 550 */     renderItemBar(itemStack, x, y);
/* 551 */     renderItemCooldown(itemStack, x, y);
/* 552 */     renderItemCount(font, itemStack, x, y, countText);
/* 553 */     this.pose.popMatrix();
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Component component, int x, int y) {
/* 557 */     setTooltipForNextFrame(List.of(component.getVisualOrderText()), x, y);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(List<FormattedCharSequence> formattedCharSequences, int x, int y) {
/* 561 */     setTooltipForNextFrame(this.minecraft.font, formattedCharSequences, DefaultTooltipPositioner.INSTANCE, x, y, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, ItemStack itemStack, int xo, int yo) {
/* 565 */     setTooltipForNextFrame(font, Screen.getTooltipFromItem(this.minecraft, itemStack), itemStack.getTooltipImage(), xo, yo, (Identifier)itemStack.get(DataComponents.TOOLTIP_STYLE));
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo) {
/* 569 */     setTooltipForNextFrame(font, texts, optionalImage, xo, yo, null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo, Identifier style) {
/* 573 */     List<ClientTooltipComponent> components = (List<ClientTooltipComponent>)texts.stream()
/* 574 */       .map(Component::getVisualOrderText)
/* 575 */       .map(ClientTooltipComponent::create)
/* 576 */       .collect(Util.toMutableList());
/* 577 */     optionalImage.ifPresent(image -> components.add(components.isEmpty() ? 0 : 1, ClientTooltipComponent.create(image)));
/* 578 */     setTooltipForNextFrameInternal(font, components, xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, Component text, int xo, int yo) {
/* 582 */     setTooltipForNextFrame(font, text, xo, yo, null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, Component text, int xo, int yo, Identifier style) {
/* 586 */     setTooltipForNextFrame(font, List.of(text.getVisualOrderText()), xo, yo, style);
/*     */   }
/*     */   
/*     */   public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo) {
/* 590 */     setComponentTooltipForNextFrame(font, lines, xo, yo, null);
/*     */   }
/*     */   
/*     */   public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo, Identifier style) {
/* 594 */     setTooltipForNextFrameInternal(font, lines.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList(), xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo) {
/* 598 */     setTooltipForNextFrame(font, lines, xo, yo, null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo, Identifier style) {
/* 602 */     setTooltipForNextFrameInternal(font, (List<ClientTooltipComponent>)lines.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> tooltip, ClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting) {
/* 606 */     setTooltipForNextFrameInternal(font, (List<ClientTooltipComponent>)tooltip.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), xo, yo, positioner, null, replaceExisting);
/*     */   }
/*     */   
/*     */   private void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style, boolean replaceExisting) {
/* 610 */     if (lines.isEmpty()) {
/*     */       return;
/*     */     }
/* 613 */     if (this.deferredTooltip == null || replaceExisting) {
/* 614 */       this.deferredTooltip = (() -> renderTooltip(font, lines, xo, yo, positioner, style));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style) {
/* 620 */     int textWidth = 0;
/* 621 */     int tempHeight = (lines.size() == 1) ? -2 : 0;
/* 622 */     for (ClientTooltipComponent line : lines) {
/* 623 */       int lineWidth = line.getWidth(font);
/* 624 */       if (lineWidth > textWidth) {
/* 625 */         textWidth = lineWidth;
/*     */       }
/* 627 */       tempHeight += line.getHeight(font);
/*     */     } 
/*     */     
/* 630 */     int w = textWidth;
/* 631 */     int h = tempHeight;
/*     */     
/* 633 */     Vector2ic positionedTooltip = positioner.positionTooltip(guiWidth(), guiHeight(), xo, yo, w, h);
/* 634 */     int x = positionedTooltip.x();
/* 635 */     int y = positionedTooltip.y();
/*     */     
/* 637 */     this.pose.pushMatrix();
/*     */     
/* 639 */     TooltipRenderUtil.renderTooltipBackground(this, x, y, w, h, style);
/*     */ 
/*     */     
/* 642 */     int localY = y;
/* 643 */     for (int i = 0; i < lines.size(); i++) {
/* 644 */       ClientTooltipComponent line = lines.get(i);
/* 645 */       line.renderText(this, font, x, localY);
/* 646 */       localY += line.getHeight(font) + ((i == 0) ? 2 : 0);
/*     */     } 
/*     */ 
/*     */     
/* 650 */     localY = y;
/* 651 */     for (int j = 0; j < lines.size(); j++) {
/* 652 */       ClientTooltipComponent line = lines.get(j);
/* 653 */       line.renderImage(font, x, localY, w, h, this);
/* 654 */       localY += line.getHeight(font) + ((j == 0) ? 2 : 0);
/*     */     } 
/*     */     
/* 657 */     this.pose.popMatrix();
/*     */   }
/*     */   
/*     */   public void renderDeferredElements() {
/* 661 */     if (this.hoveredTextStyle != null) {
/* 662 */       renderComponentHoverEffect(this.minecraft.font, this.hoveredTextStyle, this.mouseX, this.mouseY);
/*     */     }
/*     */     
/* 665 */     if (this.clickableTextStyle != null && this.clickableTextStyle.getClickEvent() != null) {
/* 666 */       requestCursor(CursorTypes.POINTING_HAND);
/*     */     }
/*     */     
/* 669 */     if (this.deferredTooltip != null) {
/* 670 */       nextStratum();
/* 671 */       this.deferredTooltip.run();
/* 672 */       this.deferredTooltip = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderItemBar(ItemStack itemStack, int x, int y) {
/* 677 */     if (itemStack.isBarVisible()) {
/* 678 */       int left = x + 2;
/* 679 */       int top = y + 13;
/* 680 */       fill(RenderPipelines.GUI, left, top, left + 13, top + 2, -16777216);
/* 681 */       fill(RenderPipelines.GUI, left, top, left + itemStack.getBarWidth(), top + 1, ARGB.opaque(itemStack.getBarColor()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderItemCount(Font font, ItemStack itemStack, int x, int y, String countText) {
/* 686 */     if (itemStack.getCount() != 1 || countText != null) {
/* 687 */       String amount = (countText == null) ? String.valueOf(itemStack.getCount()) : countText;
/* 688 */       drawString(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderItemCooldown(ItemStack itemStack, int x, int y) {
/* 693 */     LocalPlayer player = this.minecraft.player;
/* 694 */     float cooldown = (player == null) ? 0.0F : player.getCooldowns().getCooldownPercent(itemStack, this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true));
/* 695 */     if (cooldown > 0.0F) {
/* 696 */       int top = y + Mth.floor(16.0F * (1.0F - cooldown));
/* 697 */       int bottom = top + Mth.ceil(16.0F * cooldown);
/* 698 */       fill(RenderPipelines.GUI, x, top, x + 16, bottom, Integer.MAX_VALUE);
/*     */     } 
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
/*     */   public void renderComponentHoverEffect(Font font, Style hoveredStyle, int xMouse, int yMouse) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: ifnonnull -> 5
/*     */     //   4: return
/*     */     //   5: aload_2
/*     */     //   6: invokevirtual getHoverEvent : ()Lnet/minecraft/network/chat/HoverEvent;
/*     */     //   9: ifnull -> 205
/*     */     //   12: aload_2
/*     */     //   13: invokevirtual getHoverEvent : ()Lnet/minecraft/network/chat/HoverEvent;
/*     */     //   16: dup
/*     */     //   17: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: pop
/*     */     //   21: astore #5
/*     */     //   23: iconst_0
/*     */     //   24: istore #6
/*     */     //   26: aload #5
/*     */     //   28: iload #6
/*     */     //   30: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   35: tableswitch default -> 185, 0 -> 60, 1 -> 91, 2 -> 138
/*     */     //   60: aload #5
/*     */     //   62: checkcast net/minecraft/network/chat/HoverEvent$ShowItem
/*     */     //   65: astore #7
/*     */     //   67: aload #7
/*     */     //   69: invokevirtual item : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   72: astore #9
/*     */     //   74: aload #9
/*     */     //   76: astore #8
/*     */     //   78: aload_0
/*     */     //   79: aload_1
/*     */     //   80: aload #8
/*     */     //   82: iload_3
/*     */     //   83: iload #4
/*     */     //   85: invokevirtual setTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V
/*     */     //   88: goto -> 185
/*     */     //   91: aload #5
/*     */     //   93: checkcast net/minecraft/network/chat/HoverEvent$ShowEntity
/*     */     //   96: astore #9
/*     */     //   98: aload #9
/*     */     //   100: invokevirtual entity : ()Lnet/minecraft/network/chat/HoverEvent$EntityTooltipInfo;
/*     */     //   103: astore #11
/*     */     //   105: aload #11
/*     */     //   107: astore #10
/*     */     //   109: aload_0
/*     */     //   110: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   113: getfield options : Lnet/minecraft/client/Options;
/*     */     //   116: getfield advancedItemTooltips : Z
/*     */     //   119: ifeq -> 185
/*     */     //   122: aload_0
/*     */     //   123: aload_1
/*     */     //   124: aload #10
/*     */     //   126: invokevirtual getTooltipLines : ()Ljava/util/List;
/*     */     //   129: iload_3
/*     */     //   130: iload #4
/*     */     //   132: invokevirtual setComponentTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V
/*     */     //   135: goto -> 185
/*     */     //   138: aload #5
/*     */     //   140: checkcast net/minecraft/network/chat/HoverEvent$ShowText
/*     */     //   143: astore #11
/*     */     //   145: aload #11
/*     */     //   147: invokevirtual value : ()Lnet/minecraft/network/chat/Component;
/*     */     //   150: astore #13
/*     */     //   152: aload #13
/*     */     //   154: astore #12
/*     */     //   156: aload_0
/*     */     //   157: aload_1
/*     */     //   158: aload_1
/*     */     //   159: aload #12
/*     */     //   161: aload_0
/*     */     //   162: invokevirtual guiWidth : ()I
/*     */     //   165: iconst_2
/*     */     //   166: idiv
/*     */     //   167: sipush #200
/*     */     //   170: invokestatic max : (II)I
/*     */     //   173: invokevirtual split : (Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;
/*     */     //   176: iload_3
/*     */     //   177: iload #4
/*     */     //   179: invokevirtual setTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V
/*     */     //   182: goto -> 185
/*     */     //   185: goto -> 205
/*     */     //   188: astore #5
/*     */     //   190: new java/lang/MatchException
/*     */     //   193: dup
/*     */     //   194: aload #5
/*     */     //   196: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   199: aload #5
/*     */     //   201: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   204: athrow
/*     */     //   205: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #703	-> 0
/*     */     //   #704	-> 4
/*     */     //   #707	-> 5
/*     */     //   #708	-> 12
/*     */     //   #709	-> 60
/*     */     //   #710	-> 91
/*     */     //   #711	-> 109
/*     */     //   #712	-> 122
/*     */     //   #715	-> 138
/*     */     //   #716	-> 156
/*     */     //   #718	-> 185
/*     */     //   #715	-> 188
/*     */     //   #721	-> 205
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   78	13	8	item	Lnet/minecraft/world/item/ItemStack;
/*     */     //   109	29	10	entity	Lnet/minecraft/network/chat/HoverEvent$EntityTooltipInfo;
/*     */     //   156	29	12	text	Lnet/minecraft/network/chat/Component;
/*     */     //   0	206	0	this	Lnet/minecraft/client/gui/GuiGraphics;
/*     */     //   0	206	1	font	Lnet/minecraft/client/gui/Font;
/*     */     //   0	206	2	hoveredStyle	Lnet/minecraft/network/chat/Style;
/*     */     //   0	206	3	xMouse	I
/*     */     //   0	206	4	yMouse	I
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   69	72	188	java/lang/Throwable
/*     */     //   100	103	188	java/lang/Throwable
/*     */     //   147	150	188	java/lang/Throwable
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
/*     */   public void submitMapRenderState(MapRenderState mapRenderState) {
/* 724 */     Minecraft minecraft = Minecraft.getInstance();
/* 725 */     TextureManager textureManager = minecraft.getTextureManager();
/* 726 */     AbstractTexture texture = textureManager.getTexture(mapRenderState.texture);
/*     */     
/* 728 */     submitBlit(RenderPipelines.GUI_TEXTURED, texture.getTextureView(), texture.getSampler(), 0, 0, 128, 128, 0.0F, 1.0F, 0.0F, 1.0F, -1);
/*     */     
/* 730 */     for (MapRenderState.MapDecorationRenderState decoration : (Iterable<MapRenderState.MapDecorationRenderState>)mapRenderState.decorations) {
/* 731 */       if (!decoration.renderOnFrame) {
/*     */         continue;
/*     */       }
/* 734 */       this.pose.pushMatrix();
/* 735 */       this.pose.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F);
/* 736 */       this.pose.rotate(0.017453292F * decoration.rot * 360.0F / 16.0F);
/* 737 */       this.pose.scale(4.0F, 4.0F);
/* 738 */       this.pose.translate(-0.125F, 0.125F);
/*     */       
/* 740 */       TextureAtlasSprite atlasSprite = decoration.atlasSprite;
/* 741 */       if (atlasSprite != null) {
/* 742 */         AbstractTexture decorationTexture = textureManager.getTexture(atlasSprite.atlasLocation());
/* 743 */         submitBlit(RenderPipelines.GUI_TEXTURED, decorationTexture.getTextureView(), decorationTexture.getSampler(), -1, -1, 1, 1, atlasSprite.getU0(), atlasSprite.getU1(), atlasSprite.getV1(), atlasSprite.getV0(), -1);
/*     */       } 
/* 745 */       this.pose.popMatrix();
/*     */       
/* 747 */       if (decoration.name != null) {
/* 748 */         Font font = minecraft.font;
/* 749 */         float width = font.width((FormattedText)decoration.name);
/* 750 */         Objects.requireNonNull(font); float scale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
/*     */         
/* 752 */         this.pose.pushMatrix();
/* 753 */         this.pose.translate(decoration.x / 2.0F + 64.0F - width * scale / 2.0F, decoration.y / 2.0F + 64.0F + 4.0F);
/* 754 */         this.pose.scale(scale, scale);
/* 755 */         this.guiRenderState.submitText(new GuiTextRenderState(font, decoration.name.getVisualOrderText(), (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), 0, 0, -1, Integer.MIN_VALUE, false, false, this.scissorStack.peek()));
/* 756 */         this.pose.popMatrix();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitEntityRenderState(EntityRenderState renderState, float scale, Vector3f translation, Quaternionf rotation, Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1) {
/* 762 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiEntityRenderState(renderState, translation, rotation, overrideCameraAngle, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void submitSkinRenderState(PlayerModel playerModel, Identifier texture, float scale, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1) {
/* 766 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiSkinRenderState(playerModel, texture, rotationX, rotationY, pivotY, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void submitBookModelRenderState(BookModel bookModel, Identifier texture, float scale, float open, float flip, int x0, int y0, int x1, int y1) {
/* 770 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiBookModelRenderState(bookModel, texture, open, flip, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void submitBannerPatternRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1) {
/* 774 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiBannerResultRenderState(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void submitSignRenderState(Model.Simple signModel, float scale, WoodType woodType, int x0, int y0, int x1, int y1) {
/* 778 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiSignRenderState(signModel, woodType, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void submitProfilerChartRenderState(List<ResultField> chartData, int x0, int y0, int x1, int y1) {
/* 782 */     this.guiRenderState.submitPicturesInPictureState((PictureInPictureRenderState)new GuiProfilerChartRenderState(chartData, x0, y0, x1, y1, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getSprite(Material sprite) {
/* 786 */     return this.materials.get(sprite);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRendererForWidget(AbstractWidget owner, HoveredTextEffects hoveredTextEffects) {
/* 790 */     return new RenderingTextCollector(createDefaultTextParameters(owner.getAlpha()), hoveredTextEffects, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ActiveTextCollector textRenderer() {
/* 795 */     return textRenderer(HoveredTextEffects.TOOLTIP_ONLY);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRenderer(HoveredTextEffects hoveredTextEffects) {
/* 799 */     return textRenderer(hoveredTextEffects, null);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRenderer(HoveredTextEffects hoveredTextEffects, Consumer<Style> additionalHoverStyleConsumer) {
/* 803 */     return new RenderingTextCollector(createDefaultTextParameters(1.0F), hoveredTextEffects, additionalHoverStyleConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   private ActiveTextCollector.Parameters createDefaultTextParameters(float opacity) {
/* 808 */     return new ActiveTextCollector.Parameters((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), opacity, this.scissorStack.peek());
/*     */   }
/*     */   
/*     */   private static class ScissorStack {
/* 812 */     private final Deque<ScreenRectangle> stack = new ArrayDeque<>();
/*     */     
/*     */     public ScreenRectangle push(ScreenRectangle rectangle) {
/* 815 */       ScreenRectangle lastRectangle = this.stack.peekLast();
/* 816 */       if (lastRectangle != null) {
/* 817 */         ScreenRectangle intersection = Objects.<ScreenRectangle>requireNonNullElse(rectangle.intersection(lastRectangle), ScreenRectangle.empty());
/* 818 */         this.stack.addLast(intersection);
/* 819 */         return intersection;
/*     */       } 
/* 821 */       this.stack.addLast(rectangle);
/* 822 */       return rectangle;
/*     */     }
/*     */ 
/*     */     
/*     */     public ScreenRectangle pop() {
/* 827 */       if (this.stack.isEmpty()) {
/* 828 */         throw new IllegalStateException("Scissor stack underflow");
/*     */       }
/* 830 */       this.stack.removeLast();
/* 831 */       return this.stack.peekLast();
/*     */     }
/*     */     
/*     */     public ScreenRectangle peek() {
/* 835 */       return this.stack.peekLast();
/*     */     }
/*     */     
/*     */     public boolean containsPoint(int x, int y) {
/* 839 */       if (this.stack.isEmpty()) {
/* 840 */         return true;
/*     */       }
/* 842 */       return ((ScreenRectangle)this.stack.peek()).containsPoint(x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum HoveredTextEffects {
/* 847 */     NONE(false, false),
/* 848 */     TOOLTIP_ONLY(true, false),
/* 849 */     TOOLTIP_AND_CURSOR(true, true);
/*     */     
/*     */     public final boolean allowTooltip;
/*     */     
/*     */     public final boolean allowCursorChanges;
/*     */     
/*     */     HoveredTextEffects(boolean allowTooltip, boolean allowCursorChanges) {
/* 856 */       this.allowTooltip = allowTooltip;
/* 857 */       this.allowCursorChanges = allowCursorChanges;
/*     */     }
/*     */     
/*     */     public static HoveredTextEffects notClickable(boolean canTooltip) {
/* 861 */       return canTooltip ? TOOLTIP_ONLY : NONE;
/*     */     }
/*     */   }
/*     */   
/*     */   private class RenderingTextCollector implements ActiveTextCollector, Consumer<Style> {
/*     */     private ActiveTextCollector.Parameters defaultParameters;
/*     */     private final GuiGraphics.HoveredTextEffects hoveredTextEffects;
/*     */     private final Consumer<Style> additionalConsumer;
/*     */     
/*     */     private RenderingTextCollector(ActiveTextCollector.Parameters initialParameters, GuiGraphics.HoveredTextEffects hoveredTextEffects, Consumer<Style> additonalConsumer) {
/* 871 */       this.defaultParameters = initialParameters;
/* 872 */       this.hoveredTextEffects = hoveredTextEffects;
/* 873 */       this.additionalConsumer = additonalConsumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public ActiveTextCollector.Parameters defaultParameters() {
/* 878 */       return this.defaultParameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 883 */       this.defaultParameters = newParameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Style style) {
/* 888 */       if (this.hoveredTextEffects.allowTooltip && style.getHoverEvent() != null) {
/* 889 */         GuiGraphics.this.hoveredTextStyle = style;
/*     */       }
/*     */       
/* 892 */       if (this.hoveredTextEffects.allowCursorChanges && style.getClickEvent() != null) {
/* 893 */         GuiGraphics.this.clickableTextStyle = style;
/*     */       }
/*     */       
/* 896 */       if (this.additionalConsumer != null) {
/* 897 */         this.additionalConsumer.accept(style);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 903 */       boolean needsFullStyleScan = (this.hoveredTextEffects.allowCursorChanges || this.hoveredTextEffects.allowTooltip || this.additionalConsumer != null);
/* 904 */       int leftX = alignment.calculateLeft(anchorX, GuiGraphics.this.minecraft.font, text);
/* 905 */       GuiTextRenderState renderState = new GuiTextRenderState(GuiGraphics.this.minecraft.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, needsFullStyleScan, parameters.scissor());
/*     */       
/* 907 */       if (ARGB.as8BitChannel(parameters.opacity()) != 0) {
/* 908 */         GuiGraphics.this.guiRenderState.submitText(renderState);
/*     */       }
/*     */       
/* 911 */       if (needsFullStyleScan) {
/* 912 */         ActiveTextCollector.findElementUnderCursor(renderState, GuiGraphics.this.mouseX, GuiGraphics.this.mouseY, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 918 */       int lineWidth = GuiGraphics.this.minecraft.font.width((FormattedText)message);
/* 919 */       Objects.requireNonNull(GuiGraphics.this.minecraft.font); int lineHeight = 9;
/* 920 */       defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/GuiGraphics.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */