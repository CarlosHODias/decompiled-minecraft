/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class WolfRenderState
/*    */   extends LivingEntityRenderState
/*    */ {
/* 11 */   private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/wolf/wolf.png");
/*    */   public boolean isAngry;
/*    */   public boolean isSitting;
/* 14 */   public float tailAngle = 0.62831855F;
/*    */   public float headRollAngle;
/*    */   public float shakeAnim;
/* 17 */   public float wetShade = 1.0F;
/* 18 */   public Identifier texture = DEFAULT_TEXTURE;
/*    */   public DyeColor collarColor;
/* 20 */   public ItemStack bodyArmorItem = ItemStack.EMPTY;
/*    */   
/*    */   public float getBodyRollAngle(float offset) {
/* 23 */     float progress = (this.shakeAnim + offset) / 1.8F;
/* 24 */     if (progress < 0.0F) {
/* 25 */       progress = 0.0F;
/* 26 */     } else if (progress > 1.0F) {
/* 27 */       progress = 1.0F;
/*    */     } 
/* 29 */     return Mth.sin((progress * 3.1415927F)) * Mth.sin((progress * 3.1415927F * 11.0F)) * 0.15F * 3.1415927F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/WolfRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */