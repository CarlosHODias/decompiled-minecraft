/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ public class AvatarRenderState
/*    */   extends HumanoidRenderState {
/* 12 */   public PlayerSkin skin = DefaultPlayerSkin.getDefaultSkin();
/*    */   public float capeFlap;
/*    */   public float capeLean;
/*    */   public float capeLean2;
/*    */   public int arrowCount;
/*    */   public int stingerCount;
/*    */   public boolean isSpectator;
/*    */   public boolean showHat = true;
/*    */   public boolean showJacket = true;
/*    */   public boolean showLeftPants = true;
/*    */   public boolean showRightPants = true;
/*    */   public boolean showLeftSleeve = true;
/*    */   public boolean showRightSleeve = true;
/*    */   public boolean showCape = true;
/*    */   public float fallFlyingTimeInTicks;
/*    */   public boolean shouldApplyFlyingYRot;
/*    */   public float flyingYRot;
/*    */   public Component scoreText;
/*    */   public Parrot.Variant parrotOnLeftShoulder;
/*    */   public Parrot.Variant parrotOnRightShoulder;
/*    */   public int id;
/*    */   public boolean showExtraEars = false;
/* 34 */   public final ItemStackRenderState heldOnHead = new ItemStackRenderState();
/*    */   
/*    */   public float fallFlyingScale() {
/* 37 */     return Mth.clamp(this.fallFlyingTimeInTicks * this.fallFlyingTimeInTicks / 100.0F, 0.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/AvatarRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */