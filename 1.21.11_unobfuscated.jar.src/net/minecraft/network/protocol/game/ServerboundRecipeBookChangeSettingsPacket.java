/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ public class ServerboundRecipeBookChangeSettingsPacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRecipeBookChangeSettingsPacket> STREAM_CODEC = Packet.codec(ServerboundRecipeBookChangeSettingsPacket::write, ServerboundRecipeBookChangeSettingsPacket::new);
/*    */   
/*    */   private final RecipeBookType bookType;
/*    */   private final boolean isOpen;
/*    */   private final boolean isFiltering;
/*    */   
/*    */   public ServerboundRecipeBookChangeSettingsPacket(RecipeBookType bookType, boolean isOpen, boolean isFiltering) {
/* 17 */     this.bookType = bookType;
/* 18 */     this.isOpen = isOpen;
/* 19 */     this.isFiltering = isFiltering;
/*    */   }
/*    */   
/*    */   private ServerboundRecipeBookChangeSettingsPacket(FriendlyByteBuf input) {
/* 23 */     this.bookType = (RecipeBookType)input.readEnum(RecipeBookType.class);
/* 24 */     this.isOpen = input.readBoolean();
/* 25 */     this.isFiltering = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeEnum((Enum)this.bookType);
/* 30 */     output.writeBoolean(this.isOpen);
/* 31 */     output.writeBoolean(this.isFiltering);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundRecipeBookChangeSettingsPacket> type() {
/* 36 */     return GamePacketTypes.SERVERBOUND_RECIPE_BOOK_CHANGE_SETTINGS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 41 */     listener.handleRecipeBookChangeSettingsPacket(this);
/*    */   }
/*    */   
/*    */   public RecipeBookType getBookType() {
/* 45 */     return this.bookType;
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 49 */     return this.isOpen;
/*    */   }
/*    */   
/*    */   public boolean isFiltering() {
/* 53 */     return this.isFiltering;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundRecipeBookChangeSettingsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */