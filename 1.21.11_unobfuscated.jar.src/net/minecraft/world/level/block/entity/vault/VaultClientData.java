/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VaultClientData
/*    */ {
/*    */   public static final float ROTATION_SPEED = 10.0F;
/*    */   private float currentSpin;
/*    */   private float previousSpin;
/*    */   
/*    */   public float currentSpin() {
/* 14 */     return this.currentSpin;
/*    */   }
/*    */   
/*    */   public float previousSpin() {
/* 18 */     return this.previousSpin;
/*    */   }
/*    */   
/*    */   void updateDisplayItemSpin() {
/* 22 */     this.previousSpin = this.currentSpin;
/* 23 */     this.currentSpin = Mth.wrapDegrees(this.currentSpin + 10.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/vault/VaultClientData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */