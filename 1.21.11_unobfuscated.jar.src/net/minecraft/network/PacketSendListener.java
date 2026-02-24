/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketSendListener
/*    */ {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static ChannelFutureListener thenRun(Runnable runnable) {
/* 20 */     return future -> {
/*    */         runnable.run();
/*    */         if (!future.isSuccess()) {
/*    */           future.channel().pipeline().fireExceptionCaught(future.cause());
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static ChannelFutureListener exceptionallySend(Supplier<Packet<?>> handler) {
/* 29 */     return future -> {
/*    */         if (!future.isSuccess()) {
/*    */           Packet<?> newPacket = handler.get();
/*    */           if (newPacket != null) {
/*    */             LOGGER.warn("Failed to deliver packet, sending fallback {}", newPacket.type(), future.cause());
/*    */             future.channel().writeAndFlush(newPacket, future.channel().voidPromise());
/*    */           } else {
/*    */             future.channel().pipeline().fireExceptionCaught(future.cause());
/*    */           } 
/*    */         } 
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/PacketSendListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */