/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public interface LoggedChatEvent
/*    */ {
/* 10 */   public static final Codec<LoggedChatEvent> CODEC = StringRepresentable.fromEnum(Type::values).dispatch(LoggedChatEvent::type, Type::codec);
/*    */   
/*    */   Type type();
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 15 */     PLAYER("player", () -> LoggedChatMessage.Player.CODEC),
/* 16 */     SYSTEM("system", () -> LoggedChatMessage.System.CODEC);
/*    */     
/*    */     private final String serializedName;
/*    */     
/*    */     private final Supplier<MapCodec<? extends LoggedChatEvent>> codec;
/*    */     
/*    */     Type(String serializedName, Supplier<MapCodec<? extends LoggedChatEvent>> codec) {
/* 23 */       this.serializedName = serializedName;
/* 24 */       this.codec = codec;
/*    */     }
/*    */     
/*    */     private MapCodec<? extends LoggedChatEvent> codec() {
/* 28 */       return this.codec.get();
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 33 */       return this.serializedName;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/LoggedChatEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */