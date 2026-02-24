/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ServerboundSeenAdvancementsPacket implements Packet<ServerGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSeenAdvancementsPacket> STREAM_CODEC = Packet.codec(ServerboundSeenAdvancementsPacket::write, ServerboundSeenAdvancementsPacket::new);
/*    */   
/*    */   private final Action action;
/*    */   private final Identifier tab;
/*    */   
/*    */   public ServerboundSeenAdvancementsPacket(Action action, Identifier tab) {
/* 18 */     this.action = action;
/* 19 */     this.tab = tab;
/*    */   }
/*    */   
/*    */   public static ServerboundSeenAdvancementsPacket openedTab(AdvancementHolder tab) {
/* 23 */     return new ServerboundSeenAdvancementsPacket(Action.OPENED_TAB, tab.id());
/*    */   }
/*    */   
/*    */   public static ServerboundSeenAdvancementsPacket closedScreen() {
/* 27 */     return new ServerboundSeenAdvancementsPacket(Action.CLOSED_SCREEN, null);
/*    */   }
/*    */   
/*    */   private ServerboundSeenAdvancementsPacket(FriendlyByteBuf input) {
/* 31 */     this.action = (Action)input.readEnum(Action.class);
/* 32 */     if (this.action == Action.OPENED_TAB) {
/* 33 */       this.tab = input.readIdentifier();
/*    */     } else {
/* 35 */       this.tab = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 40 */     output.writeEnum(this.action);
/* 41 */     if (this.action == Action.OPENED_TAB) {
/* 42 */       output.writeIdentifier(this.tab);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ServerboundSeenAdvancementsPacket> type() {
/* 48 */     return GamePacketTypes.SERVERBOUND_SEEN_ADVANCEMENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 53 */     listener.handleSeenAdvancements(this);
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 57 */     return this.action;
/*    */   }
/*    */   
/*    */   public Identifier getTab() {
/* 61 */     return this.tab;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 65 */     OPENED_TAB,
/* 66 */     CLOSED_SCREEN;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSeenAdvancementsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */