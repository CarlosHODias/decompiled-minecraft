/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.LoomMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.BannerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ public class LoomScreen
/*     */   extends AbstractContainerScreen<LoomMenu> {
/*  36 */   private static final Identifier BANNER_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/banner");
/*  37 */   private static final Identifier DYE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/dye");
/*  38 */   private static final Identifier PATTERN_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/banner_pattern");
/*  39 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/loom/scroller");
/*  40 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/loom/scroller_disabled");
/*  41 */   private static final Identifier PATTERN_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern_selected");
/*  42 */   private static final Identifier PATTERN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern_highlighted");
/*  43 */   private static final Identifier PATTERN_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern");
/*  44 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/loom/error");
/*  45 */   private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/loom.png");
/*     */   
/*     */   private static final int PATTERN_COLUMNS = 4;
/*     */   
/*     */   private static final int PATTERN_ROWS = 4;
/*     */   
/*     */   private static final int SCROLLER_WIDTH = 12;
/*     */   
/*     */   private static final int SCROLLER_HEIGHT = 15;
/*     */   private static final int PATTERN_IMAGE_SIZE = 14;
/*     */   private static final int SCROLLER_FULL_HEIGHT = 56;
/*     */   private static final int PATTERNS_X = 60;
/*     */   private static final int PATTERNS_Y = 13;
/*     */   private static final float BANNER_PATTERN_TEXTURE_SIZE = 64.0F;
/*     */   private static final float BANNER_PATTERN_WIDTH = 21.0F;
/*     */   private static final float BANNER_PATTERN_HEIGHT = 40.0F;
/*     */   private BannerFlagModel flag;
/*     */   private BannerPatternLayers resultBannerPatterns;
/*  63 */   private ItemStack bannerStack = ItemStack.EMPTY;
/*  64 */   private ItemStack dyeStack = ItemStack.EMPTY;
/*  65 */   private ItemStack patternStack = ItemStack.EMPTY;
/*     */   
/*     */   private boolean displayPatterns;
/*     */   private boolean hasMaxPatterns;
/*     */   private float scrollOffs;
/*     */   private boolean scrolling;
/*     */   private int startRow;
/*     */   
/*     */   public LoomScreen(LoomMenu menu, Inventory inventory, Component title) {
/*  74 */     super(menu, inventory, title);
/*  75 */     menu.registerUpdateListener(this::containerChanged);
/*  76 */     this.titleLabelY -= 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  81 */     super.init();
/*  82 */     ModelPart modelPart = this.minecraft.getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
/*  83 */     this.flag = new BannerFlagModel(modelPart);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  88 */     super.render(graphics, mouseX, mouseY, a);
/*  89 */     renderTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   private int totalRowCount() {
/*  93 */     return Mth.positiveCeilDiv(this.menu.getSelectablePatterns().size(), 4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/*  98 */     int xo = this.leftPos;
/*  99 */     int yo = this.topPos;
/* 100 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */     
/* 102 */     Slot bannerSlot = this.menu.getBannerSlot();
/* 103 */     Slot dyeSlot = this.menu.getDyeSlot();
/* 104 */     Slot patternSlot = this.menu.getPatternSlot();
/* 105 */     Slot resultSlot = this.menu.getResultSlot();
/* 106 */     if (!bannerSlot.hasItem()) {
/* 107 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BANNER_SLOT_SPRITE, xo + bannerSlot.x, yo + bannerSlot.y, 16, 16);
/*     */     }
/* 109 */     if (!dyeSlot.hasItem()) {
/* 110 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DYE_SLOT_SPRITE, xo + dyeSlot.x, yo + dyeSlot.y, 16, 16);
/*     */     }
/* 112 */     if (!patternSlot.hasItem()) {
/* 113 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PATTERN_SLOT_SPRITE, xo + patternSlot.x, yo + patternSlot.y, 16, 16);
/*     */     }
/*     */     
/* 116 */     int sy = (int)(41.0F * this.scrollOffs);
/* 117 */     Identifier sprite = this.displayPatterns ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
/* 118 */     int scrollerX = xo + 119;
/* 119 */     int scrollerY = yo + 13 + sy;
/* 120 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, scrollerX, scrollerY, 12, 15);
/* 121 */     if (xm >= scrollerX && xm < scrollerX + 12 && ym >= scrollerY && ym < scrollerY + 15) {
/* 122 */       graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*     */     }
/*     */     
/* 125 */     if (this.resultBannerPatterns != null && !this.hasMaxPatterns) {
/* 126 */       DyeColor baseColor = ((BannerItem)resultSlot.getItem().getItem()).getColor();
/* 127 */       int x0 = xo + 141;
/* 128 */       int y0 = yo + 8;
/* 129 */       graphics.submitBannerPatternRenderState(this.flag, baseColor, this.resultBannerPatterns, x0, y0, x0 + 20, y0 + 40);
/* 130 */     } else if (this.hasMaxPatterns) {
/* 131 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + resultSlot.x - 5, yo + resultSlot.y - 5, 26, 26);
/*     */     } 
/* 133 */     if (this.displayPatterns) {
/* 134 */       int x = xo + 60;
/* 135 */       int y = yo + 13;
/*     */       
/* 137 */       List<Holder<BannerPattern>> selectablePatterns = this.menu.getSelectablePatterns();
/*     */       
/* 139 */       for (int row = 0; row < 4; row++) {
/* 140 */         for (int column = 0; column < 4; column++) {
/* 141 */           Identifier buttonSprite; int actualRow = row + this.startRow;
/* 142 */           int index = actualRow * 4 + column;
/* 143 */           if (index >= selectablePatterns.size()) {
/*     */             // Byte code: goto -> 739
/*     */           }
/*     */           
/* 147 */           int posX = x + column * 14;
/* 148 */           int posY = y + row * 14;
/*     */           
/* 150 */           Holder<BannerPattern> pattern = selectablePatterns.get(index);
/* 151 */           boolean isHighlighted = (xm >= posX && ym >= posY && xm < posX + 14 && ym < posY + 14);
/*     */           
/* 153 */           if (index == this.menu.getSelectedBannerPatternIndex()) {
/* 154 */             buttonSprite = PATTERN_SELECTED_SPRITE;
/* 155 */           } else if (isHighlighted) {
/* 156 */             buttonSprite = PATTERN_HIGHLIGHTED_SPRITE;
/* 157 */             DyeColor patternColor = ((DyeItem)this.dyeStack.getItem()).getDyeColor();
/* 158 */             graphics.setTooltipForNextFrame((Component)Component.translatable(((BannerPattern)pattern.value()).translationKey() + "." + ((BannerPattern)pattern.value()).translationKey()), xm, ym);
/* 159 */             graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */           } else {
/* 161 */             buttonSprite = PATTERN_SPRITE;
/*     */           } 
/* 163 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, buttonSprite, posX, posY, 14, 14);
/* 164 */           TextureAtlasSprite bannerPatternSprite = graphics.getSprite(Sheets.getBannerMaterial(pattern));
/* 165 */           renderBannerOnButton(graphics, posX, posY, bannerPatternSprite);
/*     */         } 
/*     */       } 
/*     */     } 
/* 169 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
/*     */   }
/*     */   
/*     */   private void renderBannerOnButton(GuiGraphics graphics, int posX, int posY, TextureAtlasSprite bannerPatternSprite) {
/* 173 */     graphics.pose().pushMatrix();
/* 174 */     graphics.pose().translate((posX + 4), (posY + 2));
/* 175 */     float patternU0 = bannerPatternSprite.getU0();
/* 176 */     float patternU1 = patternU0 + (bannerPatternSprite.getU1() - bannerPatternSprite.getU0()) * 21.0F / 64.0F;
/* 177 */     float patternVSpan = bannerPatternSprite.getV1() - bannerPatternSprite.getV0();
/* 178 */     float patternV0 = bannerPatternSprite.getV0() + patternVSpan / 64.0F;
/* 179 */     float patternV1 = patternV0 + patternVSpan * 40.0F / 64.0F;
/* 180 */     int bannerWidth = 5;
/* 181 */     int bannerHeight = 10;
/* 182 */     graphics.fill(0, 0, 5, 10, DyeColor.GRAY.getTextureDiffuseColor());
/* 183 */     graphics.blit(bannerPatternSprite.atlasLocation(), 0, 0, 5, 10, patternU0, patternU1, patternV0, patternV1);
/* 184 */     graphics.pose().popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 189 */     if (this.displayPatterns) {
/* 190 */       int xo = this.leftPos + 60;
/* 191 */       int yo = this.topPos + 13;
/*     */       
/* 193 */       for (int row = 0; row < 4; row++) {
/* 194 */         for (int column = 0; column < 4; column++) {
/* 195 */           double xx = event.x() - (xo + column * 14);
/* 196 */           double yy = event.y() - (yo + row * 14);
/* 197 */           int actualRow = row + this.startRow;
/* 198 */           int index = actualRow * 4 + column;
/* 199 */           if (xx >= 0.0D && yy >= 0.0D && xx < 14.0D && yy < 14.0D && this.menu.clickMenuButton((Player)this.minecraft.player, index)) {
/* 200 */             Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0F));
/* 201 */             this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
/* 202 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 207 */       xo = this.leftPos + 119;
/* 208 */       yo = this.topPos + 9;
/* 209 */       if (event.x() >= xo && event.x() < (xo + 12) && event.y() >= yo && event.y() < (yo + 56)) {
/* 210 */         this.scrolling = true;
/*     */       }
/*     */     } 
/*     */     
/* 214 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 219 */     int offscreenRows = totalRowCount() - 4;
/* 220 */     if (this.scrolling && this.displayPatterns && offscreenRows > 0) {
/* 221 */       int yscr = this.topPos + 13;
/* 222 */       int yscr2 = yscr + 56;
/* 223 */       this.scrollOffs = ((float)event.y() - yscr - 7.5F) / ((yscr2 - yscr) - 15.0F);
/* 224 */       this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
/* 225 */       this.startRow = Math.max((int)((this.scrollOffs * offscreenRows) + 0.5D), 0);
/* 226 */       return true;
/*     */     } 
/* 228 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 233 */     this.scrolling = false;
/* 234 */     return super.mouseReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 239 */     if (super.mouseScrolled(x, y, scrollX, scrollY)) {
/* 240 */       return true;
/*     */     }
/*     */     
/* 243 */     int offscreenRows = totalRowCount() - 4;
/* 244 */     if (this.displayPatterns && offscreenRows > 0) {
/* 245 */       float scrolledDelta = (float)scrollY / offscreenRows;
/* 246 */       this.scrollOffs = Mth.clamp(this.scrollOffs - scrolledDelta, 0.0F, 1.0F);
/* 247 */       this.startRow = Math.max((int)(this.scrollOffs * offscreenRows + 0.5F), 0);
/*     */     } 
/* 249 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 254 */     return (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*     */   }
/*     */   
/*     */   private void containerChanged() {
/* 258 */     ItemStack resultStack = this.menu.getResultSlot().getItem();
/* 259 */     if (resultStack.isEmpty()) {
/* 260 */       this.resultBannerPatterns = null;
/*     */     } else {
/* 262 */       this.resultBannerPatterns = (BannerPatternLayers)resultStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/*     */     } 
/*     */     
/* 265 */     ItemStack bannerStack = this.menu.getBannerSlot().getItem();
/* 266 */     ItemStack dyeStack = this.menu.getDyeSlot().getItem();
/* 267 */     ItemStack patternStack = this.menu.getPatternSlot().getItem();
/*     */     
/* 269 */     BannerPatternLayers patterns = (BannerPatternLayers)bannerStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 270 */     this.hasMaxPatterns = (patterns.layers().size() >= 6);
/*     */     
/* 272 */     if (this.hasMaxPatterns) {
/* 273 */       this.resultBannerPatterns = null;
/*     */     }
/*     */     
/* 276 */     if (!ItemStack.matches(bannerStack, this.bannerStack) || !ItemStack.matches(dyeStack, this.dyeStack) || !ItemStack.matches(patternStack, this.patternStack)) {
/* 277 */       this.displayPatterns = (!bannerStack.isEmpty() && !dyeStack.isEmpty() && !this.hasMaxPatterns && !this.menu.getSelectablePatterns().isEmpty());
/*     */     }
/*     */     
/* 280 */     if (this.startRow >= totalRowCount()) {
/* 281 */       this.startRow = 0;
/* 282 */       this.scrollOffs = 0.0F;
/*     */     } 
/* 284 */     this.bannerStack = bannerStack.copy();
/* 285 */     this.dyeStack = dyeStack.copy();
/* 286 */     this.patternStack = patternStack.copy();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/LoomScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */