/*    */ package net.minecraft.client.gui.spectator;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.PlayerFaceRenderer;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ public class PlayerMenuItem implements SpectatorMenuItem {
/*    */   private final PlayerInfo playerInfo;
/*    */   
/*    */   public PlayerMenuItem(PlayerInfo playerInfo) {
/* 16 */     this.playerInfo = playerInfo;
/* 17 */     this.name = (Component)Component.literal(playerInfo.getProfile().name());
/*    */   }
/*    */   private final Component name;
/*    */   
/*    */   public void selectItem(SpectatorMenu menu) {
/* 22 */     Minecraft.getInstance().getConnection().send((Packet)new ServerboundTeleportToEntityPacket(this.playerInfo.getProfile().id()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName() {
/* 27 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/* 32 */     PlayerFaceRenderer.draw(graphics, this.playerInfo.getSkin(), 2, 2, 12, ARGB.white(alpha));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled() {
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/PlayerMenuItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */