/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.CartographyTableMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ public class CartographyTableScreen extends AbstractContainerScreen<CartographyTableMenu> {
/*  19 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/error");
/*  20 */   private static final Identifier SCALED_MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/scaled_map");
/*  21 */   private static final Identifier DUPLICATED_MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/duplicated_map");
/*  22 */   private static final Identifier MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/map");
/*  23 */   private static final Identifier LOCKED_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/locked");
/*  24 */   private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/cartography_table.png");
/*  25 */   private final MapRenderState mapRenderState = new MapRenderState();
/*     */   
/*     */   public CartographyTableScreen(CartographyTableMenu menu, Inventory inventory, Component title) {
/*  28 */     super(menu, inventory, title);
/*  29 */     this.titleLabelY -= 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  34 */     super.render(graphics, mouseX, mouseY, a);
/*  35 */     renderTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/*     */     MapItemSavedData mapData;
/*  40 */     int xo = this.leftPos;
/*  41 */     int yo = this.topPos;
/*  42 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */     
/*  44 */     ItemStack additionalItem = this.menu.getSlot(1).getItem();
/*  45 */     boolean isDuplication = additionalItem.is(Items.MAP);
/*  46 */     boolean isScaling = additionalItem.is(Items.PAPER);
/*  47 */     boolean isLocking = additionalItem.is(Items.GLASS_PANE);
/*     */     
/*  49 */     ItemStack map = this.menu.getSlot(0).getItem();
/*  50 */     MapId mapId = (MapId)map.get(DataComponents.MAP_ID);
/*     */     
/*     */     boolean locked = false;
/*     */     
/*  54 */     if (mapId != null) {
/*  55 */       mapData = MapItem.getSavedData(mapId, (Level)this.minecraft.level);
/*  56 */       if (mapData != null) {
/*  57 */         if (mapData.locked) {
/*  58 */           locked = true;
/*     */           
/*  60 */           if (isScaling || isLocking) {
/*  61 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 35, yo + 31, 28, 21);
/*     */           }
/*     */         } 
/*  64 */         if (isScaling && mapData.scale >= 4) {
/*  65 */           locked = true;
/*  66 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 35, yo + 31, 28, 21);
/*     */         } 
/*     */       } 
/*     */     } else {
/*  70 */       mapData = null;
/*     */     } 
/*     */     
/*  73 */     renderResultingMap(graphics, mapId, mapData, isDuplication, isScaling, isLocking, locked);
/*     */   }
/*     */   
/*     */   private void renderResultingMap(GuiGraphics graphics, MapId id, MapItemSavedData data, boolean isDuplication, boolean isScaling, boolean isLocking, boolean locked) {
/*  77 */     int xo = this.leftPos;
/*  78 */     int yo = this.topPos;
/*     */     
/*  80 */     if (isScaling && !locked) {
/*  81 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCALED_MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  82 */       renderMap(graphics, id, data, xo + 85, yo + 31, 0.226F);
/*  83 */     } else if (isDuplication) {
/*  84 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DUPLICATED_MAP_SPRITE, xo + 67 + 16, yo + 13, 50, 66);
/*  85 */       renderMap(graphics, id, data, xo + 86, yo + 16, 0.34F);
/*  86 */       graphics.nextStratum();
/*  87 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DUPLICATED_MAP_SPRITE, xo + 67, yo + 13 + 16, 50, 66);
/*  88 */       renderMap(graphics, id, data, xo + 70, yo + 32, 0.34F);
/*  89 */     } else if (isLocking) {
/*  90 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  91 */       renderMap(graphics, id, data, xo + 71, yo + 17, 0.45F);
/*     */       
/*  93 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOCKED_SPRITE, xo + 118, yo + 60, 10, 14);
/*     */     } else {
/*  95 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  96 */       renderMap(graphics, id, data, xo + 71, yo + 17, 0.45F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderMap(GuiGraphics graphics, MapId id, MapItemSavedData data, int x, int y, float scale) {
/* 101 */     if (id != null && data != null) {
/* 102 */       graphics.pose().pushMatrix();
/* 103 */       graphics.pose().translate(x, y);
/* 104 */       graphics.pose().scale(scale, scale);
/*     */       
/* 106 */       this.minecraft.getMapRenderer().extractRenderState(id, data, this.mapRenderState);
/* 107 */       graphics.submitMapRenderState(this.mapRenderState);
/*     */       
/* 109 */       graphics.pose().popMatrix();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/CartographyTableScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */