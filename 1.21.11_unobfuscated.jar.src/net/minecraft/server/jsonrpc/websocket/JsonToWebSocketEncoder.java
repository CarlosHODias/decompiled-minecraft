/*    */ package net.minecraft.server.jsonrpc.websocket;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import java.util.List;
/*    */ 
/*    */ public class JsonToWebSocketEncoder
/*    */   extends MessageToMessageEncoder<JsonElement>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext ctx, JsonElement msg, List<Object> out) {
/* 13 */     out.add(new TextWebSocketFrame(msg.toString()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/websocket/JsonToWebSocketEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */