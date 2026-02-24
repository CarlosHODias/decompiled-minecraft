/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderer;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractCraftingMenu;
/*     */ import net.minecraft.world.inventory.InventoryMenu;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class InventoryScreen
/*     */   extends AbstractRecipeBookScreen<InventoryMenu> {
/*     */   private float xMouse;
/*     */   private float yMouse;
/*     */   private boolean buttonClicked;
/*     */   private final EffectsInInventory effects;
/*     */   
/*     */   public InventoryScreen(Player player) {
/*  33 */     super(player.inventoryMenu, (RecipeBookComponent<?>)new CraftingRecipeBookComponent((AbstractCraftingMenu)player.inventoryMenu), player.getInventory(), (Component)Component.translatable("container.crafting"));
/*  34 */     this.titleLabelX = 97;
/*  35 */     this.effects = new EffectsInInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerTick() {
/*  40 */     super.containerTick();
/*     */     
/*  42 */     if (this.minecraft.player.hasInfiniteMaterials()) {
/*  43 */       this.minecraft.setScreen(new CreativeModeInventoryScreen(this.minecraft.player, this.minecraft.player.connection.enabledFeatures(), (Boolean)this.minecraft.options.operatorItemsTab().get()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  49 */     if (this.minecraft.player.hasInfiniteMaterials()) {
/*  50 */       this.minecraft.setScreen(new CreativeModeInventoryScreen(this.minecraft.player, this.minecraft.player.connection.enabledFeatures(), (Boolean)this.minecraft.options.operatorItemsTab().get()));
/*     */       return;
/*     */     } 
/*  53 */     super.init();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScreenPosition getRecipeBookButtonPosition() {
/*  58 */     return new ScreenPosition(this.leftPos + 104, this.height / 2 - 22);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRecipeBookButtonClick() {
/*  63 */     this.buttonClicked = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/*  68 */     graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  73 */     this.effects.render(graphics, mouseX, mouseY);
/*  74 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  76 */     this.xMouse = mouseX;
/*  77 */     this.yMouse = mouseY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showsActiveEffects() {
/*  82 */     return this.effects.canSeeEffects();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isBiggerResultSlot() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/*  92 */     int xo = this.leftPos;
/*  93 */     int yo = this.topPos;
/*  94 */     graphics.blit(RenderPipelines.GUI_TEXTURED, INVENTORY_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */     
/*  96 */     renderEntityInInventoryFollowsMouse(graphics, xo + 26, yo + 8, xo + 75, yo + 78, 30, 0.0625F, this.xMouse, this.yMouse, (LivingEntity)this.minecraft.player);
/*     */   }
/*     */   
/*     */   public static void renderEntityInInventoryFollowsMouse(GuiGraphics graphics, int x0, int y0, int x1, int y1, int size, float offsetY, float mouseX, float mouseY, LivingEntity entity) {
/* 100 */     float centerX = (x0 + x1) / 2.0F;
/* 101 */     float centerY = (y0 + y1) / 2.0F;
/*     */     
/* 103 */     float xAngle = (float)Math.atan(((centerX - mouseX) / 40.0F));
/* 104 */     float yAngle = (float)Math.atan(((centerY - mouseY) / 40.0F));
/*     */     
/* 106 */     Quaternionf rotation = new Quaternionf().rotateZ(3.1415927F);
/* 107 */     Quaternionf xRotation = new Quaternionf().rotateX(yAngle * 20.0F * 0.017453292F);
/* 108 */     rotation.mul((Quaternionfc)xRotation);
/*     */     
/* 110 */     EntityRenderState renderState = extractRenderState(entity);
/* 111 */     if (renderState instanceof LivingEntityRenderState) { LivingEntityRenderState livingRenderState = (LivingEntityRenderState)renderState;
/* 112 */       livingRenderState.bodyRot = 180.0F + xAngle * 20.0F;
/* 113 */       livingRenderState.yRot = xAngle * 20.0F;
/* 114 */       if (livingRenderState.pose != Pose.FALL_FLYING) {
/* 115 */         livingRenderState.xRot = -yAngle * 20.0F;
/*     */       } else {
/* 117 */         livingRenderState.xRot = 0.0F;
/*     */       } 
/* 119 */       livingRenderState.boundingBoxWidth /= livingRenderState.scale;
/* 120 */       livingRenderState.boundingBoxHeight /= livingRenderState.scale;
/* 121 */       livingRenderState.scale = 1.0F; }
/*     */ 
/*     */     
/* 124 */     Vector3f translation = new Vector3f(0.0F, renderState.boundingBoxHeight / 2.0F + offsetY, 0.0F);
/* 125 */     graphics.submitEntityRenderState(renderState, size, translation, rotation, xRotation, x0, y0, x1, y1);
/*     */   }
/*     */   
/*     */   private static EntityRenderState extractRenderState(LivingEntity entity) {
/* 129 */     EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
/* 130 */     EntityRenderer<? super LivingEntity, ?> renderer = entityRenderDispatcher.getRenderer((Entity)entity);
/* 131 */     EntityRenderState renderState = renderer.createRenderState((Entity)entity, 1.0F);
/* 132 */     renderState.lightCoords = 15728880;
/* 133 */     renderState.shadowPieces.clear();
/* 134 */     renderState.outlineColor = 0;
/* 135 */     return renderState;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 140 */     if (this.buttonClicked) {
/* 141 */       this.buttonClicked = false;
/* 142 */       return true;
/*     */     } 
/*     */     
/* 145 */     return super.mouseReleased(event);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/InventoryScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */