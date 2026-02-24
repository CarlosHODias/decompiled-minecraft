/*    */ package net.minecraft.client.gui.contextualbar;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.PlayerRideableJumping;
/*    */ 
/*    */ public class JumpableVehicleBarRenderer implements ContextualBarRenderer {
/* 14 */   private static final Identifier JUMP_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_background");
/* 15 */   private static final Identifier JUMP_BAR_COOLDOWN_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_cooldown");
/* 16 */   private static final Identifier JUMP_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/jump_bar_progress");
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   private final PlayerRideableJumping playerJumpableVehicle;
/*    */   
/*    */   public JumpableVehicleBarRenderer(Minecraft minecraft) {
/* 22 */     this.minecraft = minecraft;
/* 23 */     this.playerJumpableVehicle = Objects.<PlayerRideableJumping>requireNonNull(((LocalPlayer)Objects.<LocalPlayer>requireNonNull(minecraft.player)).jumpableVehicle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {
/* 28 */     int left = left(this.minecraft.getWindow());
/* 29 */     int top = top(this.minecraft.getWindow());
/*    */     
/* 31 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, JUMP_BAR_BACKGROUND_SPRITE, left, top, 182, 5);
/*    */     
/* 33 */     if (this.playerJumpableVehicle.getJumpCooldown() > 0) {
/* 34 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, JUMP_BAR_COOLDOWN_SPRITE, left, top, 182, 5);
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     int progress = Mth.lerpDiscrete(this.minecraft.player.getJumpRidingScale(), 0, 182);
/* 39 */     if (progress > 0)
/* 40 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, JUMP_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, left, top, progress, 5); 
/*    */   }
/*    */   
/*    */   public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/contextualbar/JumpableVehicleBarRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */