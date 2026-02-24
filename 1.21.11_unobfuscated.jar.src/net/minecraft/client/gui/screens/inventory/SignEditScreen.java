/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.blockentity.SignRenderer;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class SignEditScreen
/*    */   extends AbstractSignEditScreen
/*    */ {
/*    */   public static final float MAGIC_SCALE_NUMBER = 62.500004F;
/*    */   public static final float MAGIC_TEXT_SCALE = 0.9765628F;
/* 14 */   private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);
/*    */   private Model.Simple signModel;
/*    */   
/*    */   public SignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
/* 18 */     super(sign, isFrontText, shouldFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 23 */     super.init();
/*    */     
/* 25 */     boolean standing = this.sign.getBlockState().getBlock() instanceof net.minecraft.world.level.block.StandingSignBlock;
/* 26 */     this.signModel = SignRenderer.createSignModel(this.minecraft.getEntityModels(), this.woodType, standing);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getSignYOffset() {
/* 31 */     return 90.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderSignBackground(GuiGraphics graphics) {
/* 36 */     if (this.signModel == null) {
/*    */       return;
/*    */     }
/* 39 */     int centerX = this.width / 2;
/* 40 */     int x0 = centerX - 48;
/* 41 */     int y0 = 66;
/* 42 */     int x1 = centerX + 48;
/* 43 */     int y1 = 168;
/* 44 */     graphics.submitSignRenderState(this.signModel, 62.500004F, this.woodType, x0, 66, x1, 168);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vector3f getSignTextScale() {
/* 49 */     return TEXT_SCALE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/SignEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */