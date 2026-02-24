/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class ServerItemCooldowns
/*    */   extends ItemCooldowns {
/*    */   public ServerItemCooldowns(ServerPlayer player) {
/* 11 */     this.player = player;
/*    */   }
/*    */   private final ServerPlayer player;
/*    */   
/*    */   protected void onCooldownStarted(Identifier cooldownGroup, int duration) {
/* 16 */     super.onCooldownStarted(cooldownGroup, duration);
/* 17 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(cooldownGroup, duration));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCooldownEnded(Identifier cooldownGroup) {
/* 22 */     super.onCooldownEnded(cooldownGroup);
/* 23 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(cooldownGroup, 0));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/ServerItemCooldowns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */