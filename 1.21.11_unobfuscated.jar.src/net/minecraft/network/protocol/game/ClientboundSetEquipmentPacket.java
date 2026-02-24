/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClientboundSetEquipmentPacket implements Packet<ClientGamePacketListener> {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetEquipmentPacket> STREAM_CODEC = Packet.codec(ClientboundSetEquipmentPacket::write, ClientboundSetEquipmentPacket::new);
/*    */   
/*    */   private static final byte CONTINUE_MASK = -128;
/*    */   private final int entity;
/*    */   private final List<Pair<EquipmentSlot, ItemStack>> slots;
/*    */   
/*    */   public ClientboundSetEquipmentPacket(int entity, List<Pair<EquipmentSlot, ItemStack>> slots) {
/* 22 */     this.entity = entity;
/* 23 */     this.slots = slots;
/*    */   }
/*    */   private ClientboundSetEquipmentPacket(RegistryFriendlyByteBuf input) {
/*    */     int slotId;
/* 27 */     this.entity = input.readVarInt();
/*    */     
/* 29 */     this.slots = Lists.newArrayList();
/*    */     do {
/* 31 */       slotId = input.readByte();
/* 32 */       EquipmentSlot slot = EquipmentSlot.VALUES.get(slotId & 0x7F);
/* 33 */       ItemStack itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/* 34 */       this.slots.add(Pair.of(slot, itemStack));
/* 35 */     } while ((slotId & 0xFFFFFF80) != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 42 */     output.writeVarInt(this.entity);
/*    */     
/* 44 */     int size = this.slots.size();
/* 45 */     for (int i = 0; i < size; i++) {
/* 46 */       Pair<EquipmentSlot, ItemStack> e = this.slots.get(i);
/* 47 */       EquipmentSlot slotType = (EquipmentSlot)e.getFirst();
/* 48 */       boolean shouldContinue = (i != size - 1);
/* 49 */       int slotId = slotType.ordinal();
/* 50 */       output.writeByte(shouldContinue ? (slotId | 0xFFFFFF80) : slotId);
/* 51 */       ItemStack.OPTIONAL_STREAM_CODEC.encode(output, e.getSecond());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundSetEquipmentPacket> type() {
/* 57 */     return GamePacketTypes.CLIENTBOUND_SET_EQUIPMENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 62 */     listener.handleSetEquipment(this);
/*    */   }
/*    */   
/*    */   public int getEntity() {
/* 66 */     return this.entity;
/*    */   }
/*    */   
/*    */   public List<Pair<EquipmentSlot, ItemStack>> getSlots() {
/* 70 */     return this.slots;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetEquipmentPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */