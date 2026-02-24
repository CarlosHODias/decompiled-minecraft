/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ServerboundInteractPacket implements Packet<ServerGamePacketListener> {
/*  18 */   public static final StreamCodec<FriendlyByteBuf, ServerboundInteractPacket> STREAM_CODEC = Packet.codec(ServerboundInteractPacket::write, ServerboundInteractPacket::new);
/*     */   
/*     */   private final int entityId;
/*     */   private final Action action;
/*     */   private final boolean usingSecondaryAction;
/*     */   
/*     */   private ServerboundInteractPacket(int entityId, boolean usingSecondaryAction, Action action) {
/*  25 */     this.entityId = entityId;
/*  26 */     this.action = action;
/*  27 */     this.usingSecondaryAction = usingSecondaryAction;
/*     */   }
/*     */   
/*     */   public static ServerboundInteractPacket createAttackPacket(Entity entity, boolean usingSecondaryAction) {
/*  31 */     return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, ATTACK_ACTION);
/*     */   }
/*     */   
/*     */   public static ServerboundInteractPacket createInteractionPacket(Entity entity, boolean usingSecondaryAction, InteractionHand hand) {
/*  35 */     return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, new InteractionAction(hand));
/*     */   }
/*     */   
/*     */   public static ServerboundInteractPacket createInteractionPacket(Entity entity, boolean usingSecondaryAction, InteractionHand hand, Vec3 location) {
/*  39 */     return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, new InteractionAtLocationAction(hand, location));
/*     */   }
/*     */   
/*     */   private ServerboundInteractPacket(FriendlyByteBuf input) {
/*  43 */     this.entityId = input.readVarInt();
/*  44 */     ActionType type = (ActionType)input.readEnum(ActionType.class);
/*  45 */     this.action = type.reader.apply(input);
/*  46 */     this.usingSecondaryAction = input.readBoolean();
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/*  50 */     output.writeVarInt(this.entityId);
/*  51 */     output.writeEnum(this.action.getType());
/*  52 */     this.action.write(output);
/*  53 */     output.writeBoolean(this.usingSecondaryAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ServerboundInteractPacket> type() {
/*  58 */     return GamePacketTypes.SERVERBOUND_INTERACT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ServerGamePacketListener listener) {
/*  63 */     listener.handleInteract(this);
/*     */   }
/*     */   
/*     */   public Entity getTarget(ServerLevel level) {
/*  67 */     return level.getEntityOrPart(this.entityId);
/*     */   }
/*     */   
/*     */   public boolean isUsingSecondaryAction() {
/*  71 */     return this.usingSecondaryAction;
/*     */   }
/*     */   
/*     */   public boolean isWithinRange(ServerPlayer player, AABB aabb, double buffer) {
/*  75 */     if (this.action.getType() == ActionType.ATTACK) {
/*  76 */       return player.isWithinAttackRange(aabb, buffer);
/*     */     }
/*  78 */     return player.isWithinEntityInteractionRange(aabb, buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(Handler handler) {
/*  83 */     this.action.dispatch(handler);
/*     */   }
/*     */   
/*     */   private enum ActionType {
/*  87 */     INTERACT(InteractionAction::new),
/*  88 */     ATTACK(input -> ServerboundInteractPacket.ATTACK_ACTION),
/*  89 */     INTERACT_AT(InteractionAtLocationAction::new);
/*     */     
/*     */     private final Function<FriendlyByteBuf, ServerboundInteractPacket.Action> reader;
/*     */ 
/*     */     
/*     */     ActionType(Function<FriendlyByteBuf, ServerboundInteractPacket.Action> reader) {
/*  95 */       this.reader = reader;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InteractionAction
/*     */     implements Action
/*     */   {
/*     */     private final InteractionHand hand;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private InteractionAction(InteractionHand hand) {
/* 119 */       this.hand = hand;
/*     */     }
/*     */     
/*     */     private InteractionAction(FriendlyByteBuf input) {
/* 123 */       this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerboundInteractPacket.ActionType getType() {
/* 128 */       return ServerboundInteractPacket.ActionType.INTERACT;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispatch(ServerboundInteractPacket.Handler handler) {
/* 133 */       handler.onInteraction(this.hand);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 138 */       output.writeEnum((Enum)this.hand);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InteractionAtLocationAction implements Action {
/*     */     private final InteractionHand hand;
/*     */     private final Vec3 location;
/*     */     
/*     */     private InteractionAtLocationAction(InteractionHand hand, Vec3 location) {
/* 147 */       this.hand = hand;
/* 148 */       this.location = location;
/*     */     }
/*     */     
/*     */     private InteractionAtLocationAction(FriendlyByteBuf input) {
/* 152 */       this.location = new Vec3(input.readFloat(), input.readFloat(), input.readFloat());
/* 153 */       this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerboundInteractPacket.ActionType getType() {
/* 158 */       return ServerboundInteractPacket.ActionType.INTERACT_AT;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispatch(ServerboundInteractPacket.Handler handler) {
/* 163 */       handler.onInteraction(this.hand, this.location);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 168 */       output.writeFloat((float)this.location.x);
/* 169 */       output.writeFloat((float)this.location.y);
/* 170 */       output.writeFloat((float)this.location.z);
/* 171 */       output.writeEnum((Enum)this.hand);
/*     */     }
/*     */   }
/*     */   
/* 175 */   private static final Action ATTACK_ACTION = new Action()
/*     */     {
/*     */       public ServerboundInteractPacket.ActionType getType() {
/* 178 */         return ServerboundInteractPacket.ActionType.ATTACK;
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispatch(ServerboundInteractPacket.Handler handler) {
/* 183 */         handler.onAttack();
/*     */       }
/*     */       
/*     */       public void write(FriendlyByteBuf output) {}
/*     */     };
/*     */   
/*     */   private static interface Action {
/*     */     ServerboundInteractPacket.ActionType getType();
/*     */     
/*     */     void dispatch(ServerboundInteractPacket.Handler param1Handler);
/*     */     
/*     */     void write(FriendlyByteBuf param1FriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public static interface Handler {
/*     */     void onInteraction(InteractionHand param1InteractionHand);
/*     */     
/*     */     void onInteraction(InteractionHand param1InteractionHand, Vec3 param1Vec3);
/*     */     
/*     */     void onAttack();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundInteractPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */