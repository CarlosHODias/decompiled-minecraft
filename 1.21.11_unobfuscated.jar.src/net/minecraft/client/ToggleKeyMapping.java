/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.InputConstants;
/*    */ import java.util.function.BooleanSupplier;
/*    */ 
/*    */ 
/*    */ public class ToggleKeyMapping
/*    */   extends KeyMapping
/*    */ {
/*    */   private final BooleanSupplier needsToggle;
/*    */   private boolean releasedByScreenWhenDown;
/*    */   private final boolean shouldRestore;
/*    */   
/*    */   public ToggleKeyMapping(String name, int value, KeyMapping.Category category, BooleanSupplier needsToggle, boolean shouldRestore) {
/* 15 */     this(name, InputConstants.Type.KEYSYM, value, category, needsToggle, shouldRestore);
/*    */   }
/*    */   
/*    */   public ToggleKeyMapping(String name, InputConstants.Type type, int value, KeyMapping.Category category, BooleanSupplier needsToggle, boolean shouldRestore) {
/* 19 */     super(name, type, value, category);
/* 20 */     this.needsToggle = needsToggle;
/* 21 */     this.shouldRestore = shouldRestore;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSetOnIngameFocus() {
/* 26 */     return (super.shouldSetOnIngameFocus() && !this.needsToggle.getAsBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDown(boolean down) {
/* 31 */     if (this.needsToggle.getAsBoolean()) {
/* 32 */       if (down) {
/* 33 */         super.setDown(!isDown());
/*    */       }
/*    */     } else {
/* 36 */       super.setDown(down);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void release() {
/* 42 */     if ((this.needsToggle.getAsBoolean() && isDown()) || this.releasedByScreenWhenDown) {
/* 43 */       this.releasedByScreenWhenDown = true;
/*    */     }
/* 45 */     reset();
/*    */   }
/*    */   
/*    */   public boolean shouldRestoreStateOnScreenClosed() {
/* 49 */     boolean shouldRestore = (this.shouldRestore && this.needsToggle.getAsBoolean() && this.key.getType() == InputConstants.Type.KEYSYM && this.releasedByScreenWhenDown);
/* 50 */     this.releasedByScreenWhenDown = false;
/* 51 */     return shouldRestore;
/*    */   }
/*    */   
/*    */   protected void reset() {
/* 55 */     super.setDown(false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ToggleKeyMapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */