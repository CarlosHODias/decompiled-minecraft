/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.ServerboundPacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface ServerPacketListener extends ServerboundPacketListener {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/*    */   default void onPacketError(Packet packet, Exception e) throws ReportedException {
/* 14 */     LOGGER.error("Failed to handle packet {}, suppressing error", packet, e);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */