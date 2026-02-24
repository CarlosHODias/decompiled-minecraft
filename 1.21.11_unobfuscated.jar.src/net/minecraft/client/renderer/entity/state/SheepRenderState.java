/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.color.ColorLerper;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class SheepRenderState extends LivingEntityRenderState {
/*    */   public float headEatPositionScale;
/*    */   public float headEatAngleScale;
/*    */   public boolean isSheared;
/* 10 */   public DyeColor woolColor = DyeColor.WHITE;
/*    */   public boolean isJebSheep;
/*    */   
/*    */   public int getWoolColor() {
/* 14 */     if (this.isJebSheep) {
/* 15 */       return ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, this.ageInTicks);
/*    */     }
/*    */     
/* 18 */     return ColorLerper.Type.SHEEP.getColor(this.woolColor);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/SheepRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */