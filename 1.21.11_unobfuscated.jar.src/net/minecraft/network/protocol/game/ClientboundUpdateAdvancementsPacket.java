/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementProgress;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ClientboundUpdateAdvancementsPacket implements Packet<ClientGamePacketListener> {
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAdvancementsPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateAdvancementsPacket::write, ClientboundUpdateAdvancementsPacket::new);
/*    */   
/*    */   private final boolean reset;
/*    */   private final List<AdvancementHolder> added;
/*    */   private final Set<Identifier> removed;
/*    */   private final Map<Identifier, AdvancementProgress> progress;
/*    */   private final boolean showAdvancements;
/*    */   
/*    */   public ClientboundUpdateAdvancementsPacket(boolean reset, Collection<AdvancementHolder> newAdvancements, Set<Identifier> removedAdvancements, Map<Identifier, AdvancementProgress> progress, boolean showAdvancements) {
/* 28 */     this.reset = reset;
/* 29 */     this.added = List.copyOf(newAdvancements);
/* 30 */     this.removed = Set.copyOf(removedAdvancements);
/* 31 */     this.progress = Map.copyOf(progress);
/* 32 */     this.showAdvancements = showAdvancements;
/*    */   }
/*    */   
/*    */   private ClientboundUpdateAdvancementsPacket(RegistryFriendlyByteBuf input) {
/* 36 */     this.reset = input.readBoolean();
/* 37 */     this.added = (List<AdvancementHolder>)AdvancementHolder.LIST_STREAM_CODEC.decode(input);
/* 38 */     this.removed = (Set<Identifier>)input.readCollection(Sets::newLinkedHashSetWithExpectedSize, FriendlyByteBuf::readIdentifier);
/* 39 */     this.progress = input.readMap(FriendlyByteBuf::readIdentifier, AdvancementProgress::fromNetwork);
/* 40 */     this.showAdvancements = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 44 */     output.writeBoolean(this.reset);
/*    */     
/* 46 */     AdvancementHolder.LIST_STREAM_CODEC.encode(output, this.added);
/* 47 */     output.writeCollection(this.removed, FriendlyByteBuf::writeIdentifier);
/* 48 */     output.writeMap(this.progress, FriendlyByteBuf::writeIdentifier, (buffer, value) -> value.serializeToNetwork(buffer));
/* 49 */     output.writeBoolean(this.showAdvancements);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundUpdateAdvancementsPacket> type() {
/* 54 */     return GamePacketTypes.CLIENTBOUND_UPDATE_ADVANCEMENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 59 */     listener.handleUpdateAdvancementsPacket(this);
/*    */   }
/*    */   
/*    */   public List<AdvancementHolder> getAdded() {
/* 63 */     return this.added;
/*    */   }
/*    */   
/*    */   public Set<Identifier> getRemoved() {
/* 67 */     return this.removed;
/*    */   }
/*    */   
/*    */   public Map<Identifier, AdvancementProgress> getProgress() {
/* 71 */     return this.progress;
/*    */   }
/*    */   
/*    */   public boolean shouldReset() {
/* 75 */     return this.reset;
/*    */   }
/*    */   
/*    */   public boolean shouldShowAdvancements() {
/* 79 */     return this.showAdvancements;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundUpdateAdvancementsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */