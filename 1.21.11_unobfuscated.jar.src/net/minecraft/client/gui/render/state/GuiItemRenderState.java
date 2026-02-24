/*    */ package net.minecraft.client.gui.render.state;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Matrix3x2f;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public final class GuiItemRenderState
/*    */   implements ScreenArea {
/*    */   private final String name;
/*    */   private final Matrix3x2f pose;
/*    */   private final TrackingItemStackRenderState itemStackRenderState;
/*    */   private final int x;
/*    */   private final int y;
/*    */   private final ScreenRectangle scissorArea;
/*    */   private final ScreenRectangle oversizedItemBounds;
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public GuiItemRenderState(String name, Matrix3x2f pose, TrackingItemStackRenderState itemStackRenderState, int x, int y, ScreenRectangle scissorArea) {
/* 22 */     this.name = name;
/* 23 */     this.pose = pose;
/* 24 */     this.itemStackRenderState = itemStackRenderState;
/* 25 */     this.x = x;
/* 26 */     this.y = y;
/* 27 */     this.scissorArea = scissorArea;
/* 28 */     this.oversizedItemBounds = itemStackRenderState().isOversizedInGui() ? calculateOversizedItemBounds() : null;
/* 29 */     this.bounds = calculateBounds((this.oversizedItemBounds != null) ? this.oversizedItemBounds : new ScreenRectangle(this.x, this.y, 16, 16));
/*    */   }
/*    */   
/*    */   private ScreenRectangle calculateOversizedItemBounds() {
/* 33 */     AABB aabb = this.itemStackRenderState.getModelBoundingBox();
/* 34 */     int actualXSize = Mth.ceil(aabb.getXsize() * 16.0D);
/* 35 */     int actualYSize = Mth.ceil(aabb.getYsize() * 16.0D);
/* 36 */     if (actualXSize > 16 || actualYSize > 16) {
/* 37 */       float xOffset = (float)(aabb.minX * 16.0D);
/* 38 */       float yOffset = (float)(aabb.maxY * 16.0D);
/* 39 */       int flooredXOffset = Mth.floor(xOffset);
/* 40 */       int flooredYOffset = Mth.floor(yOffset);
/* 41 */       int actualX = this.x + flooredXOffset + 8;
/* 42 */       int actualY = this.y - flooredYOffset + 8;
/* 43 */       return new ScreenRectangle(actualX, actualY, actualXSize, actualYSize);
/*    */     } 
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private ScreenRectangle calculateBounds(ScreenRectangle itemBounds) {
/* 50 */     ScreenRectangle bounds = itemBounds.transformMaxBounds((Matrix3x2fc)this.pose);
/* 51 */     return (this.scissorArea != null) ? this.scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */   
/*    */   public String name() {
/* 55 */     return this.name;
/*    */   }
/*    */   
/*    */   public Matrix3x2f pose() {
/* 59 */     return this.pose;
/*    */   }
/*    */   
/*    */   public TrackingItemStackRenderState itemStackRenderState() {
/* 63 */     return this.itemStackRenderState;
/*    */   }
/*    */   
/*    */   public int x() {
/* 67 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/* 71 */     return this.y;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 75 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public ScreenRectangle oversizedItemBounds() {
/* 79 */     return this.oversizedItemBounds;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 84 */     return this.bounds;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/GuiItemRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */