/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.PlayerModelType;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerSkinWidget
/*    */   extends AbstractWidget
/*    */ {
/*    */   private static final float MODEL_HEIGHT = 2.125F;
/*    */   private static final float FIT_SCALE = 0.97F;
/*    */   private static final float ROTATION_SENSITIVITY = 2.5F;
/*    */   private static final float DEFAULT_ROTATION_X = -5.0F;
/*    */   private static final float DEFAULT_ROTATION_Y = 30.0F;
/*    */   private static final float ROTATION_X_LIMIT = 50.0F;
/*    */   private final PlayerModel wideModel;
/*    */   private final PlayerModel slimModel;
/*    */   private final Supplier<PlayerSkin> skin;
/* 36 */   private float rotationX = -5.0F;
/* 37 */   private float rotationY = 30.0F;
/*    */   
/*    */   public PlayerSkinWidget(int width, int height, EntityModelSet models, Supplier<PlayerSkin> skin) {
/* 40 */     super(0, 0, width, height, CommonComponents.EMPTY);
/* 41 */     this.wideModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER), false);
/* 42 */     this.slimModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER_SLIM), true);
/* 43 */     this.skin = skin;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 48 */     float scale = 0.97F * getHeight() / 2.125F;
/* 49 */     float pivotY = -1.0625F;
/* 50 */     PlayerSkin skin = this.skin.get();
/* 51 */     PlayerModel model = (skin.model() == PlayerModelType.SLIM) ? this.slimModel : this.wideModel;
/* 52 */     graphics.submitSkinRenderState(model, skin.body().texturePath(), scale, this.rotationX, this.rotationY, -1.0625F, getX(), getY(), getRight(), getBottom());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/* 57 */     this.rotationX = Mth.clamp(this.rotationX - (float)dy * 2.5F, -50.0F, 50.0F);
/* 58 */     this.rotationY += (float)dx * 2.5F;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */ 
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/PlayerSkinWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */